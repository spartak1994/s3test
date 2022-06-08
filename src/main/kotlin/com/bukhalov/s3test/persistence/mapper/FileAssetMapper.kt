package com.bukhalov.s3test.persistence.mapper

import com.bukhalov.s3test.model.FileAsset
import com.bukhalov.s3test.persistence.dynamic.model.FileAsset_.Companion.fileAsset_
import org.apache.ibatis.annotations.Mapper
import org.apache.ibatis.annotations.Result
import org.apache.ibatis.annotations.Results
import org.apache.ibatis.annotations.SelectProvider
import org.mybatis.dynamic.sql.select.render.SelectStatementProvider
import org.mybatis.dynamic.sql.util.SqlProviderAdapter
import org.mybatis.dynamic.sql.util.kotlin.SelectCompleter
import org.mybatis.dynamic.sql.util.kotlin.mybatis3.selectList
import org.springframework.stereotype.Repository

@Mapper
@Repository
interface FileAssetMapper {
    /**
     * Поиск FileAsset.
     *
     * @param selectStatement Параметры запроса.
     * @return List FileAsset.
     */
    @SelectProvider(type = SqlProviderAdapter::class, method = "select")
    @Results(
        id = "fileAssetMap",
        value = [
            Result(property = "filename", column = "FILENAME"),
            Result(property = "fileAssetId", column = "FILE_ASSET_ID"),
            Result(property = "lastModified", column = "LAST_MODIFIED"),
            Result(property = "sizeBytes", column = "SIZE_BYTES"),
            Result(property = "type", column = "TYPE"),
            Result(property = "isHead", column = "IS_HEAD"),
            Result(property = "parentFolder", column = "PARENT_FOLDER"),
            Result(property = "assetVersion", column = "ASSET_VERSION")
        ]
    )
    fun findListBy(selectStatement: SelectStatementProvider): List<FileAsset>
}

fun FileAssetMapper.selectMany(completer: SelectCompleter) =
    selectList(this::findListBy, fileAsset_.all, fileAsset_, completer)
