package com.bukhalov.s3test

import mu.KotlinLogging.logger
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

private val log = logger {}

@SpringBootApplication
class S3testApplication

fun main(args: Array<String>) {
    runApplication<S3testApplication>(*args)
    log.info { "It starts" }
}
