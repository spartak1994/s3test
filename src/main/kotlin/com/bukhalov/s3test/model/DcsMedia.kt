package com.bukhalov.s3test.model

import java.io.Serializable
import java.time.ZonedDateTime

class DcsMedia(
    var name: String? = null,
    var path: String? = null,
    var creationDate: ZonedDateTime? = null,
    var mediaType: Int? = null,
    var mediaId: String? = null,
    var parentFolderId: String? = null,
    var isHead: Boolean = false,
    var versionDeleted: Boolean = false,
    val assetVersion: Int? = null
) : Serializable
