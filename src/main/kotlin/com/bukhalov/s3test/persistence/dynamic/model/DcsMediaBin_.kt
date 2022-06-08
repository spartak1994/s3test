package com.bukhalov.s3test.persistence.dynamic.model

import org.mybatis.dynamic.sql.SqlTable
import org.mybatis.dynamic.sql.util.kotlin.elements.column
import java.sql.JDBCType.BLOB
import java.sql.JDBCType.NUMERIC
import java.sql.JDBCType.TIMESTAMP
import java.sql.JDBCType.VARCHAR
import java.time.ZonedDateTime

class DcsMediaBin_ : SqlTable("pub.dcs_media_bin") {
    val assetVersion = column<Int>(name = "ASSET_VERSION", jdbcType = NUMERIC)
    val mediaId = column<String>(name = "MEDIA_ID", jdbcType = VARCHAR)
    val length = column<Int>(name = "LENGTH", jdbcType = NUMERIC)
    val lastModified = column<ZonedDateTime>(name = "LAST_MODIFIED", jdbcType = TIMESTAMP)
    val data = column<Any>(name = "DATA", jdbcType = BLOB)

    val all = listOf(
        assetVersion,
        mediaId,
        length,
        lastModified,
        data
    )

    companion object {
        val dcsMediaBin_ = DcsMediaBin_()
    }
}
