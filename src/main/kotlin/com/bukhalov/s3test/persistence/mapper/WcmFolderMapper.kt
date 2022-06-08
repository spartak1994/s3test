package com.bukhalov.s3test.persistence.mapper

import com.bukhalov.s3test.model.WcmFolder
import com.bukhalov.s3test.persistence.dynamic.model.WcmFolder_.Companion.wcmFolder_
import org.apache.ibatis.annotations.Mapper
import org.apache.ibatis.annotations.Result
import org.apache.ibatis.annotations.Results
import org.apache.ibatis.annotations.SelectProvider
import org.mybatis.dynamic.sql.select.render.SelectStatementProvider
import org.mybatis.dynamic.sql.util.SqlProviderAdapter
import org.mybatis.dynamic.sql.util.kotlin.SelectCompleter
import org.mybatis.dynamic.sql.util.kotlin.mybatis3.selectOne
import org.springframework.stereotype.Repository

@Mapper
@Repository
interface WcmFolderMapper {
    /**
     * Поиск WcmFolder.
     *
     * @param selectStatement Параметры запроса.
     * @return WcmFolder.
     */
    @SelectProvider(type = SqlProviderAdapter::class, method = "select")
    @Results(
        id = "wcmFolderMap",
        value = [
            Result(property = "name", column = "NAME"),
            Result(property = "path", column = "PATH"),
            Result(property = "creationDate", column = "CREATION_DATE"),
            Result(property = "parentFolderId", column = "PARENT_FOLDER_ID"),
            Result(property = "isHead", column = "IS_HEAD"),
            Result(property = "assetVersion", column = "ASSET_VERSION"),
            Result(property = "versionDeleted", column = "VERSION_DELETED")
        ]
    )
    fun find(selectStatement: SelectStatementProvider): WcmFolder?
}

fun WcmFolderMapper.findBy(completer: SelectCompleter) =
    selectOne(this::find, wcmFolder_.all, wcmFolder_, completer)
