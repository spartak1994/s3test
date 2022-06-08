package com.bukhalov.s3test.persistence.dynamic.model

import org.mybatis.dynamic.sql.SqlTable
import org.mybatis.dynamic.sql.util.kotlin.elements.column
import java.sql.JDBCType.CHAR
import java.sql.JDBCType.NUMERIC
import java.sql.JDBCType.TIMESTAMP
import java.sql.JDBCType.VARCHAR
import java.time.ZonedDateTime

class WcmMediaContent_ : SqlTable("pub.wcm_media_content") {
    val name = column<String>(name = "NAME", jdbcType = VARCHAR)
    val url = column<String>(name = "URL", jdbcType = VARCHAR)
    val creationDate = column<ZonedDateTime>(name = "CREATION_DATE", jdbcType = TIMESTAMP)
    val mediaType = column<Int>(name = "MEDIA_TYPE", jdbcType = NUMERIC)
    val id = column<String>(name = "ID", jdbcType = VARCHAR)
    val parentFolderId = column<String>(name = "PARENT_FOLDER_ID", jdbcType = VARCHAR)
    val isHead = column<Boolean>(name = "IS_HEAD", jdbcType = CHAR)
    val fileUpload = column<Boolean>(name = "FILE_UPLOAD", jdbcType = CHAR)
    val assetVersion = column<Int>(name = "ASSET_VERSION", jdbcType = NUMERIC)
    val versionDeleted = column<Int>(name = "VERSION_DELETED", jdbcType = CHAR)
    val contentType = column<Int>(name = "CONTENT_TYPE", jdbcType = NUMERIC)

    val all = listOf(
        name,
        url,
        creationDate,
        mediaType,
        id,
        parentFolderId,
        isHead,
        fileUpload,
        assetVersion,
        versionDeleted,
        contentType
    )

    companion object {
        val wcmMediaContent_ = WcmMediaContent_()
    }
}
