package com.bukhalov.s3test.persistence.dynamic.model

import org.mybatis.dynamic.sql.SqlTable
import org.mybatis.dynamic.sql.util.kotlin.elements.column
import java.sql.JDBCType.CHAR
import java.sql.JDBCType.NUMERIC
import java.sql.JDBCType.VARCHAR

class FileFolder_ : SqlTable("pub.epub_file_folder") {
    val folderName = column<String>(name = "FOLDER_NAME", jdbcType = VARCHAR)
    val folderId = column<String>(name = "FOLDER_ID", jdbcType = VARCHAR)
    val parentFolder = column<String>(name = "PARENT_FOLDER", jdbcType = VARCHAR)
    val isHead = column<Boolean>(name = "IS_HEAD", jdbcType = CHAR)
    val assetVersion = column<Int>(name = "ASSET_VERSION", jdbcType = NUMERIC)
    val versionDeleted = column<Boolean>(name = "VERSION_DELETED", jdbcType = CHAR)

    val all = listOf(
        folderName,
        folderId,
        parentFolder,
        isHead,
        assetVersion,
        versionDeleted
    )

    companion object {
        val fileFolder_ = FileFolder_()
    }
}
