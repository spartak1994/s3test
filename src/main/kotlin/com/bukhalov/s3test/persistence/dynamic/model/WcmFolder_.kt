package com.bukhalov.s3test.persistence.dynamic.model

import org.mybatis.dynamic.sql.SqlTable
import org.mybatis.dynamic.sql.util.kotlin.elements.column
import java.sql.JDBCType.CHAR
import java.sql.JDBCType.NUMERIC
import java.sql.JDBCType.TIMESTAMP
import java.sql.JDBCType.VARCHAR
import java.time.ZonedDateTime

class WcmFolder_ : SqlTable("pub.wcm_folder") {
    val name = column<String>(name = "NAME", jdbcType = VARCHAR)
    val creationDate = column<ZonedDateTime>(name = "CREATION_DATE", jdbcType = TIMESTAMP)
    val id = column<String>(name = "ID", jdbcType = VARCHAR)
    val parentFolderId = column<String>(name = "PARENT_FOLDER_ID", jdbcType = VARCHAR)
    val isHead = column<Boolean>(name = "IS_HEAD", jdbcType = CHAR)
    val assetVersion = column<Int>(name = "ASSET_VERSION", jdbcType = NUMERIC)
    val versionDeleted = column<Int>(name = "VERSION_DELETED", jdbcType = CHAR)

    val all = listOf(
        name,
        creationDate,
        id,
        parentFolderId,
        isHead,
        assetVersion,
        versionDeleted
    )

    companion object {
        val wcmFolder_ = WcmFolder_()
    }
}
