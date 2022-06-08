package com.bukhalov.s3test.model

import java.io.Serializable
import java.time.ZonedDateTime

class WcmFolder(
    var name: String? = null,
    var creationDate: ZonedDateTime? = null,
    var id: String? = null,
    var parentFolderId: String? = null,
    val isHead: Boolean? = null,
    val assetVersion: Int? = null,
    var versionDeleted: Boolean = false
) : Serializable
