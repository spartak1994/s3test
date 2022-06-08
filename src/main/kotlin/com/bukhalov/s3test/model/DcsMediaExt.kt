package com.bukhalov.s3test.model

import java.io.Serializable

class DcsMediaExt(
    var assetVersion: Int? = null,
    var mediaId: String? = null,
    var url: String? = null
) : Serializable
