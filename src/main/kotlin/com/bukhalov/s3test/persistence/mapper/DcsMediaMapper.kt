package com.bukhalov.s3test.persistence.mapper

import com.bukhalov.s3test.model.DcsMedia
import com.bukhalov.s3test.persistence.dynamic.model.DcsMedia_.Companion.dcsMedia_
import org.apache.ibatis.annotations.Mapper
import org.apache.ibatis.annotations.Result
import org.apache.ibatis.annotations.Results
import org.apache.ibatis.annotations.SelectProvider
import org.apache.ibatis.annotations.UpdateProvider
import org.mybatis.dynamic.sql.select.render.SelectStatementProvider
import org.mybatis.dynamic.sql.update.render.UpdateStatementProvider
import org.mybatis.dynamic.sql.util.SqlProviderAdapter
import org.mybatis.dynamic.sql.util.kotlin.SelectCompleter
import org.mybatis.dynamic.sql.util.kotlin.UpdateCompleter
import org.mybatis.dynamic.sql.util.kotlin.mybatis3.selectList
import org.mybatis.dynamic.sql.util.kotlin.mybatis3.update
import org.springframework.stereotype.Repository

@Mapper
@Repository
interface DcsMediaMapper {
    /**
     * Поиск DcsMedia.
     *
     * @param selectStatement Параметры запроса.
     * @return List DcsMedia.
     */
    @SelectProvider(type = SqlProviderAdapter::class, method = "select")
    @Results(
        id = "dscMediaMap",
        value = [
            Result(property = "name", column = "NAME"),
            Result(property = "path", column = "PATH"),
            Result(property = "creationDate", column = "CREATION_DATE"),
            Result(property = "mediaType", column = "MEDIA_TYPE"),
            Result(property = "parentFolderId", column = "PARENT_FOLDER_ID"),
            Result(property = "isHead", column = "IS_HEAD"),
            Result(property = "versionDeleted", column = "VERSION_DELETED"),
            Result(property = "assetVersion", column = "ASSET_VERSION"),
            Result(property = "mediaId", column = "MEDIA_ID")
        ]
    )
    fun findListBy(selectStatement: SelectStatementProvider): List<DcsMedia>

    /**
     * Обновление DcsMedia.
     *
     * @param updateStatement DcsMedia
     * @return Количество обновленных записей.
     */
    @UpdateProvider(type = SqlProviderAdapter::class, method = "update")
    fun update(updateStatement: UpdateStatementProvider): Int
}

fun DcsMediaMapper.selectMany(completer: SelectCompleter) =
    selectList(this::findListBy, dcsMedia_.all, dcsMedia_, completer)

fun DcsMediaMapper.update(completer: UpdateCompleter) =
    update(
        this::update,
        dcsMedia_,
        completer
    )
