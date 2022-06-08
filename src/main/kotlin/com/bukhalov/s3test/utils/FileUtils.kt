package com.bukhalov.s3test.utils

import org.springframework.util.ResourceUtils
import java.io.File
import java.nio.file.Files

object FileUtils {
    fun readFile(path: String): ByteArray? = try {
//        val file: File = ResourceUtils.getFile("C://testfiles/1.jpg")
        val file: File = ResourceUtils.getFile(path)
        // Read File Content
        Files.readAllBytes(file.toPath())
    } catch (e: Exception) {
        println(e.message)
        null
    }
}
