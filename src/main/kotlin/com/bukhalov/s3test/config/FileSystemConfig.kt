package com.bukhalov.s3test.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
class FileSystemConfig(
    @Value("\${fileSystem.localDirectory}")
    val localDirectory: String,

    @Value("\${fileSystem.s3DBCatalog}")
    val s3DBCatalog: String,

    @Value("\${fileSystem.s3FileSystemCatalog}")
    val s3FileSystemCatalog: String,
)
