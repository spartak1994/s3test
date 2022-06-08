package com.bukhalov.s3test.service

import com.bukhalov.s3test.config.FileSystemConfig
import com.bukhalov.s3test.model.DcsMedia
import com.bukhalov.s3test.model.DcsMediaBin
import com.bukhalov.s3test.model.DcsMediaExt
import com.bukhalov.s3test.model.FileAsset
import com.bukhalov.s3test.model.WcmMediaContent
import com.bukhalov.s3test.persistence.dynamic.model.DcsMediaBin_.Companion.dcsMediaBin_
import com.bukhalov.s3test.persistence.dynamic.model.DcsMedia_.Companion.dcsMedia_
import com.bukhalov.s3test.persistence.dynamic.model.FileAsset_.Companion.fileAsset_
import com.bukhalov.s3test.persistence.dynamic.model.WcmFolder_.Companion.wcmFolder_
import com.bukhalov.s3test.persistence.dynamic.model.WcmMediaContent_.Companion.wcmMediaContent_
import com.bukhalov.s3test.persistence.mapper.DcsMediaBinMapper
import com.bukhalov.s3test.persistence.mapper.DcsMediaExtMapper
import com.bukhalov.s3test.persistence.mapper.DcsMediaMapper
import com.bukhalov.s3test.persistence.mapper.FileAssetMapper
import com.bukhalov.s3test.persistence.mapper.WcmFolderMapper
import com.bukhalov.s3test.persistence.mapper.WcmMediaContentMapper
import com.bukhalov.s3test.persistence.mapper.findBy
import com.bukhalov.s3test.persistence.mapper.insert
import com.bukhalov.s3test.persistence.mapper.selectMany
import com.bukhalov.s3test.persistence.mapper.update
import com.bukhalov.s3test.utils.FileUtils.readFile
import com.bukhalov.s3test.utils.StringUtils.toJavaName
import com.bukhalov.s3test.utils.StringUtils.transliterate
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.launch
import mu.KotlinLogging.logger
import org.mybatis.dynamic.sql.util.kotlin.elements.isEqualTo
import org.mybatis.dynamic.sql.util.kotlin.elements.isEqualToWhenPresent
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import software.amazon.awssdk.core.sync.RequestBody
import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.services.s3.model.ObjectCannedACL
import software.amazon.awssdk.services.s3.model.PutObjectRequest
import java.sql.Blob
import java.util.concurrent.Executors
import kotlin.time.Duration
import kotlin.time.ExperimentalTime
import kotlin.time.measureTimedValue

private val log = logger {}
private const val DELETED_URL = "https://s3.tele2.ru/ds-site/file-deleted"
val exceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
    log.error { "Exception in $coroutineContext, $throwable" }
}

val dispatcher = Executors
    .newFixedThreadPool(29)
    .asCoroutineDispatcher()

val context = Job() + dispatcher + CoroutineName("myCoroutine") + exceptionHandler
val scope = CoroutineScope(context)

@Service
class S3ServiceImpl(
    private val fileAssetMapper: FileAssetMapper,
    private val dcsMediaMapper: DcsMediaMapper,
    private val dcsMediaBinMapper: DcsMediaBinMapper,
    private val dcsMediaExtMapper: DcsMediaExtMapper,
    private val wcmMediaContentMapper: WcmMediaContentMapper,
    private val wcmFolderMapper: WcmFolderMapper,
    private val fileSystemConfig: FileSystemConfig,
    private val s3: S3Client
) : S3Service {
    @OptIn(ExperimentalTime::class)
    override fun putFileFromDcmMedia() {
        val (media: List<DcsMedia>, getDcsMediaTime: Duration) = measureTimedValue {
            dcsMediaMapper.selectMany {
                where(dcsMedia_.mediaId, isEqualTo("2"))
                    .and(dcsMedia_.isHead, isEqualTo(true))
                    .orderBy(dcsMedia_.mediaId)
            }
        }
        log.info { "Получение всех dcsMedia из базы заняло $getDcsMediaTime" }

        log.info { "Количество файлов для перекладывания: ${media.size}" }

//        runBlocking {
        val allJobs = mutableListOf<Job>()
        media.forEach {
            log.error { "Пробуем создать корутину для ${it.mediaId}" }
            val job = scope.launch { processDscMedia(it) }
//                allJobs.add(job)
        }
//            allJobs.forEach { it.join() }
//        }
        log.info { "The end" }
    }

    @OptIn(ExperimentalTime::class)
    override fun putFileFromFileSystem(path: String) {
//        FileUtils.readFile(path)
        val (content: List<WcmMediaContent>, getWcmMediaTime: Duration) = measureTimedValue {
            wcmMediaContentMapper.selectMany {
                where(wcmMediaContent_.isHead, isEqualTo(true))
                    .and(wcmMediaContent_.fileUpload, isEqualTo(true))
                    .orderBy(wcmMediaContent_.id)
            }
        }
        log.info { "Получение всех wcmMediaContent из базы заняло $getWcmMediaTime" }
        log.info { "Количество данных контента для перекладывания: ${content.size}" }

        //        runBlocking {
        val allJobs = mutableListOf<Job>()
        content.forEach {
            log.error { "Пробуем создать корутину для ${it.id}" }
            val job = scope.launch { processWcmContent(it) }
//                allJobs.add(job)
        }
//            allJobs.forEach { it.join() }
//        }
        log.info { "The end" }
    }

    @OptIn(ExperimentalTime::class)
    private fun processDscMedia(dcsMedia: DcsMedia) {
        val s3Link: String?
        if (!dcsMedia.versionDeleted) {
            val (mediaBin: DcsMediaBin?, getMediaBinTime: Duration) = measureTimedValue {
                dcsMediaBinMapper.findBy {
                    where(dcsMediaBin_.mediaId, isEqualTo(dcsMedia.mediaId!!))
                    and(dcsMediaBin_.assetVersion, isEqualTo(dcsMedia.assetVersion!!))
                }
            }
            log.info { "Получение mediaBin с mediaId = ${mediaBin?.mediaId} заняло $getMediaBinTime" }
            val data = (mediaBin!!.data as Blob).binaryStream
            val path = dcsMedia.path
            val updatedPath = updatePath(path!!, mediaBin.mediaId!!)
            val translitPath = transliterate(updatedPath)
            val finalS3Path = "BCC/catalog/$translitPath"
            val (tempLink: String, uploadTime: Duration) = measureTimedValue { uploadFileToBucket(finalS3Path, data.readAllBytes(), s3) }
            s3Link = tempLink
            log.info { "Загрузка файла $s3Link в s3 заняла $uploadTime. Размер этого файла был ${mediaBin.length}" }
        } else {
            s3Link = DELETED_URL
            log.info { "У файла с mediaId =  ${dcsMedia.mediaId} была version_deleted. Поэтому его не кладем в s3." }
        }
        val inserted = insertToMediaExt(dcsMedia.mediaId!!, s3Link)
        if (inserted) {
            update(dcsMedia.apply { this.mediaType = 1 })
        }
    }

    private fun insertToMediaExt(mediaId: String, s3Link: String): Boolean = try {
        log.info { "Попытка внести записи в таблицу dcsMediaExt. mediaId = [$mediaId], s3Link = [$s3Link]." }
        val allMediaAsset = dcsMediaMapper.selectMany { where(dcsMedia_.mediaId, isEqualTo(mediaId)) }
        var count = 0
        allMediaAsset.forEach {
            val mediaExt = DcsMediaExt(assetVersion = it.assetVersion, mediaId = mediaId, url = "test_$s3Link")
            dcsMediaExtMapper.insert(mediaExt)
            count++
        }
        log.info { "Успешно внесены записи в таблицу dcsMediaExt. В количестве $count штук. mediaId = [$mediaId], s3Link = [$s3Link]." }
        true
    } catch (e: Exception) {
        log.error(e) { "Ошибка при попытке внести записи в таблицу dcsMediaExt для mediaId = [$mediaId] и s3Link = [$s3Link]" }
        false
    }

    @Transactional
    private fun update(media: DcsMedia) {
        try {
            // обновит сразу все записи с указанным mediaId
            dcsMediaMapper.update {
                set(dcsMedia_.mediaType).equalToWhenPresent(1)
                    .where(dcsMedia_.mediaId, isEqualToWhenPresent(media.mediaId))
            }
            log.info { "Сущность DcsMedia с mediaId = ${media.mediaId} успешно обновлена." }
        } catch (ex: Exception) {
            log.error(ex) { "Error in process update DcsMedia (mediaId = ${media.mediaId}): ${ex.message}" }
        }
    }

    private fun uploadFileToBucket(path: String, data: ByteArray, s3: S3Client): String {
        val filePath = "$path"
        var s3Link = ""
        var count = 0
        val por = PutObjectRequest
            .builder()
            .bucket("ds-test")
            .key(filePath)
            .acl(ObjectCannedACL.PUBLIC_READ)
            .build()
        val requestBody = RequestBody.fromBytes(data)
        log.info { "В методе uploadFileToBucket, скоро будем пробовать положить файл $filePath в S3." }
        while (s3Link.isEmpty() && count < 3) {
            count++
            return try {
                log.info { "Попытка #$count положить файл $filePath в S3." }
                val response = s3.putObject(por, requestBody)
                log.info { "Файл $filePath успешно положен в S3." }
                "https://s3.tele2.ru/ds-test/$filePath"
                //            val gor = GetObjectRequest.builder().bucket("ds-test").key(filePath).build()
                //            log.info { "Попытка получить файл $filePath из S3." }
                //            val responseInputStream = s3.getObject(gor)
                //            log.info { "Файл $filePath успешно получен из S3." }
            } catch (e: Exception) {
                log.error(e) { "Произошла ошибка при попытке положить файл $filePath в S3. Текст ошибки: ${e.message}" }
                "" // необходимо вернуть пустую строку в случае ошибки, чтобы работал цикл
            }
        }
        return s3Link
    }

    private fun updatePath(path: String, mediaId: String) =
        "${path.substringBeforeLast(".")}_$mediaId.${path.substringAfterLast(".")}"

    @OptIn(ExperimentalTime::class)
    private fun processWcmContent(wcmMediaContent: WcmMediaContent) {
        val s3Link: String?
        if (!wcmMediaContent.versionDeleted) {
            val fileName = wcmMediaContent.url?.substringAfterLast("/")
            val filePath = getFilePath(wcmMediaContent)
            val fileAssetWithSameName = fileAssetMapper.selectMany {
                where(fileAsset_.filename, isEqualTo(fileName!!))
                    .and(fileAsset_.type, isEqualTo(10001))
                    .and(fileAsset_.isHead, isEqualTo(true))
            }

            var fileAsset: FileAsset? = null
            if (fileAssetWithSameName.size == 1) {
                fileAsset = fileAssetWithSameName.first()
            } else {
                // метод проверки какой именно файл относится к нам
                fileAssetWithSameName.first()
            }

            // по fileAsset получаем бинарный файл
            val realPath = getRealPathOnFileSystem(fileAsset!!)
            val file = getFileByPath(realPath)
            // кладем бинарный файл в s3
            if (file != null) {
                val (tempLink: String, uploadTime: Duration) = measureTimedValue { uploadFileToBucket("bukhalov/test/$filePath", file, s3) }
                s3Link = tempLink
                log.info { "Загрузка файла $s3Link в s3 заняла $uploadTime. Размер этого файла был ${file.size}" }
            } else {
                log.error { "Файл по пути $realPath по какой-то причине пустой, поэтому не кладем его в S3." }
            }
        } else {
            s3Link = DELETED_URL
            log.info { "У файла контента с id =  ${wcmMediaContent.id} была version_deleted. Поэтому его не кладем в s3." }
        }
        // Выполнить какую-то работу по обновлению данных в базе
//        val inserted = insertToMediaExt(dcsMedia.mediaId!!, s3Link)
//        if (inserted) {
//            update(dcsMedia.apply { this.mediaType = 1 })
//        }
    }

    private fun addParentFolderPath(parentFolderId: String): String {
        var filePath: String = ""
        var parentFolder = wcmFolderMapper.findBy {
            where(wcmFolder_.id, isEqualToWhenPresent(parentFolderId))
                .and(wcmFolder_.isHead, isEqualTo(true))
        }
        if (parentFolder != null) {
            filePath = parentFolder.name + "/" + filePath
            if (!parentFolder.parentFolderId.isNullOrEmpty()) {
                filePath = addParentFolderPath(parentFolder.parentFolderId!!)
            }
        }
        return filePath
    }

    private fun getFilePath(wcmMediaContent: WcmMediaContent): String {
        val fileName = wcmMediaContent.url?.substringAfterLast("/")
        var filePath = ""
        if (!wcmMediaContent.parentFolderId.isNullOrEmpty()) {
            val parentFolderId = wcmMediaContent.parentFolderId
            filePath = addParentFolderPath(parentFolderId!!)
        } else {
            filePath += "/"
        }
        filePath += "$fileName"
        log.info { "У файла контента с id =  ${wcmMediaContent.id} мы поняли, что путь к файлу будет вот такой. $filePath" }
        return filePath
    }

    private fun getRealPathOnFileSystem(fileAsset: FileAsset): String {
        val localDirectory = fileSystemConfig.localDirectory
        val idDir = fileAsset.fileAssetId?.substring(0, 5)
        if (idDir.isNullOrEmpty()) {
            log.error { "У fileAsset c id = ${fileAsset.fileAssetId} неправильный идентификатор" }
        }
        val versionDir = (fileAsset.assetVersion!! - 1) / 30
        val fileNameRep = toJavaName(fileAsset.filename)
        val fileIdRep = toJavaName(fileAsset.fileAssetId)
        val filePath = "$localDirectory/$idDir/$versionDir/$fileNameRep.$fileIdRep#${fileAsset.assetVersion}"
        log.info { "Для fileAsset c id = ${fileAsset.fileAssetId} мы решили, что файл хранится по пути $filePath" }
        return filePath
    }

    private fun getFileByPath(path: String): ByteArray? {
        log.info { "Пробуем получить файл по пути: $path" }
        var file: ByteArray? = null
        try {
            file = readFile(path)
        } catch (e: Exception) {
            log.error { "Ошибка при попытке получить файл по пути: $path Подробнее ${e.message}" }
        }
        return file
    }
}
