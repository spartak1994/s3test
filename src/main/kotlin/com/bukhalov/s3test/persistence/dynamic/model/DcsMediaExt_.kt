package com.bukhalov.s3test.persistence.dynamic.model

import org.mybatis.dynamic.sql.SqlTable
import org.mybatis.dynamic.sql.util.kotlin.elements.column
import java.sql.JDBCType.NUMERIC
import java.sql.JDBCType.VARCHAR

class DcsMediaExt_ : SqlTable("pub.dcs_media_ext") {
    val assetVersion = column<Int>(name = "ASSET_VERSION", jdbcType = NUMERIC)
    val mediaId = column<String>(name = "MEDIA_ID", jdbcType = VARCHAR)
    val url = column<String>(name = "URL", jdbcType = VARCHAR)

    val all = listOf(
        assetVersion,
        mediaId,
        url
    )

    companion object {
        val dcsMediaExt_ = DcsMediaExt_()
    }
}
