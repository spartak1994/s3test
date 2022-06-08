package com.bukhalov.s3test.model

import java.io.Serializable
import java.time.ZonedDateTime

class WcmMediaContent(
    var name: String? = null,
    var creationDate: ZonedDateTime? = null,
    var mediaType: Int? = null,
    var id: String? = null,
    var url: String? = null,
    var parentFolderId: String? = null,
    var fileUpload: Boolean? = null,
    val isHead: Boolean? = null,
    val assetVersion: Int? = null,
    var versionDeleted: Boolean = false,
    val contentType: Int? = null
) : Serializable
