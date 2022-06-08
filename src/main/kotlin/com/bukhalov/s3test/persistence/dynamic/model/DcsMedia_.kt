package com.bukhalov.s3test.persistence.dynamic.model

import org.mybatis.dynamic.sql.SqlTable
import org.mybatis.dynamic.sql.util.kotlin.elements.column
import java.sql.JDBCType.CHAR
import java.sql.JDBCType.NUMERIC
import java.sql.JDBCType.TIMESTAMP
import java.sql.JDBCType.VARCHAR
import java.time.ZonedDateTime

class DcsMedia_ : SqlTable("pub.dcs_media") {
    val name = column<String>(name = "NAME", jdbcType = VARCHAR)
    val path = column<String>(name = "PATH", jdbcType = VARCHAR)
    val creationDate = column<ZonedDateTime>(name = "CREATION_DATE", jdbcType = TIMESTAMP)
    val mediaType = column<Int>(name = "MEDIA_TYPE", jdbcType = NUMERIC)
    val mediaId = column<String>(name = "MEDIA_ID", jdbcType = VARCHAR)
    val parentFolderId = column<String>(name = "PARENT_FOLDER_ID", jdbcType = VARCHAR)
    val isHead = column<Boolean>(name = "IS_HEAD", jdbcType = CHAR)
    val versionDeleted = column<Boolean>(name = "VERSION_DELETED", jdbcType = CHAR)
    val assetVersion = column<Int>(name = "ASSET_VERSION", jdbcType = NUMERIC)

    val all = listOf(
        name,
        path,
        creationDate,
        mediaType,
        mediaId,
        parentFolderId,
        isHead,
        versionDeleted,
        assetVersion
    )

    companion object {
        val dcsMedia_ = DcsMedia_()
    }
}
