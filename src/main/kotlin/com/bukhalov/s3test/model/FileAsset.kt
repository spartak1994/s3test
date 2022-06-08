package com.bukhalov.s3test.model

import java.io.Serializable
import java.time.ZonedDateTime

data class FileAsset(
    var filename: String? = null,
    var fileAssetId: String? = null,
    var lastModified: ZonedDateTime? = null,
    var sizeBytes: Int? = null,
    var parentFolder: String? = null,
    var isHead: Boolean = false,
    val assetVersion: Int? = null,
    val versionDeleted: Boolean = false,
    var type: Int? = null
) : Serializable
