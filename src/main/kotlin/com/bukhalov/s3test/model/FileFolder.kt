package com.bukhalov.s3test.model

import java.io.Serializable

data class FileFolder(
    var folderName: String? = null,
    var folderId: String? = null,
    var parentFolder: String? = null,
    var isHead: Boolean = false,
    val assetVersion: Int? = null,
    val versionDeleted: Boolean = false
) : Serializable
