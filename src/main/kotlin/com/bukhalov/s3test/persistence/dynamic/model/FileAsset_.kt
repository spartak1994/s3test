package com.bukhalov.s3test.persistence.dynamic.model

import org.mybatis.dynamic.sql.SqlTable
import org.mybatis.dynamic.sql.util.kotlin.elements.column
import java.sql.JDBCType.CHAR
import java.sql.JDBCType.NUMERIC
import java.sql.JDBCType.TIMESTAMP
import java.sql.JDBCType.VARCHAR
import java.time.ZonedDateTime

class FileAsset_ : SqlTable("epub_file_asset") {
    val filename = column<String>(name = "FILENAME", jdbcType = VARCHAR)
    val fileAssetId = column<String>(name = "FILE_ASSET_ID", jdbcType = VARCHAR)
    val lastModified = column<ZonedDateTime>(name = "LAST_MODIFIED", jdbcType = TIMESTAMP)
    val sizeBytes = column<Int>(name = "SIZE_BYTES", jdbcType = NUMERIC)
    val parentFolder = column<String>(name = "PARENT_FOLDER", jdbcType = VARCHAR)
    val isHead = column<Boolean>(name = "IS_HEAD", jdbcType = CHAR)
    val assetVersion = column<Int>(name = "ASSET_VERSION", jdbcType = NUMERIC)
    val type = column<Int>(name = "TYPE", jdbcType = NUMERIC)

    val all = listOf(
        filename,
        fileAssetId,
        lastModified,
        sizeBytes,
        parentFolder,
        isHead,
        assetVersion,
        type
    )

    companion object {
        val fileAsset_ = FileAsset_()
    }
}
