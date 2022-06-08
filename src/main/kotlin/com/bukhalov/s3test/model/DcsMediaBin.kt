package com.bukhalov.s3test.model

import java.io.Serializable
import java.time.ZonedDateTime

class DcsMediaBin(
    var assetVersion: Int? = null,
    var mediaId: String? = null,
    var length: Int? = null,
    var lastModified: ZonedDateTime? = null,
    var data: Any? = null
) : Serializable
