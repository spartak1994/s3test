package com.bukhalov.s3test.persistence.mapper

import com.bukhalov.s3test.model.FileFolder
import com.bukhalov.s3test.model.WcmFolder
import com.bukhalov.s3test.persistence.dynamic.model.FileFolder_.Companion.fileFolder_
import org.apache.ibatis.annotations.Mapper
import org.apache.ibatis.annotations.Result
import org.apache.ibatis.annotations.ResultMap
import org.apache.ibatis.annotations.Results
import org.apache.ibatis.annotations.SelectProvider
import org.apache.ibatis.annotations.UpdateProvider
import org.mybatis.dynamic.sql.select.render.SelectStatementProvider
import org.mybatis.dynamic.sql.update.render.UpdateStatementProvider
import org.mybatis.dynamic.sql.util.SqlProviderAdapter
import org.mybatis.dynamic.sql.util.kotlin.SelectCompleter
import org.mybatis.dynamic.sql.util.kotlin.UpdateCompleter
import org.mybatis.dynamic.sql.util.kotlin.mybatis3.selectList
import org.mybatis.dynamic.sql.util.kotlin.mybatis3.selectOne
import org.mybatis.dynamic.sql.util.kotlin.mybatis3.update
import org.springframework.stereotype.Repository

@Mapper
@Repository
interface FileFolderMapper {
    /**
     * Поиск FileFolder.
     *
     * @param selectStatement Параметры запроса.
     * @return List FileFolder.
     */
    @SelectProvider(type = SqlProviderAdapter::class, method = "select")
    @Results(
        id = "fileFolderMap",
        value = [
            Result(property = "folderName", column = "FOLDER_NAME"),
            Result(property = "folderId", column = "FOLDER_ID"),
            Result(property = "isHead", column = "IS_HEAD"),
            Result(property = "parentFolder", column = "PARENT_FOLDER"),
            Result(property = "assetVersion", column = "ASSET_VERSION"),
            Result(property = "versionDeleted", column = "VERSION_DELETED")
        ]
    )
    fun findListBy(selectStatement: SelectStatementProvider): List<FileFolder>

    /**
     * Обновление FileFolder.
     *
     * @param updateStatement FileFolder
     * @return Количество обновленных записей.
     */
    @UpdateProvider(type = SqlProviderAdapter::class, method = "update")
    fun update(updateStatement: UpdateStatementProvider): Int

    /**
     * Поиск FileFolder.
     *
     * @param selectStatement Параметры запроса.
     * @return FileFolder.
     */
    @SelectProvider(type = SqlProviderAdapter::class, method = "select")
    @ResultMap("fileFolderMap")
    fun find(selectStatement: SelectStatementProvider): FileFolder?
}

fun FileFolderMapper.selectMany(completer: SelectCompleter) =
    selectList(this::findListBy, fileFolder_.all, fileFolder_, completer)

fun FileFolderMapper.update(completer: UpdateCompleter) =
    update(this::update, fileFolder_, completer)

fun FileFolderMapper.findBy(completer: SelectCompleter) =
    selectOne(this::find, fileFolder_.all, fileFolder_, completer)