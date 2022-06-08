package com.bukhalov.s3test.controller

import com.bukhalov.s3test.service.S3Service
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import mu.KotlinLogging.logger
import org.springframework.http.HttpStatus.OK
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import javax.ws.rs.core.MediaType.APPLICATION_JSON

private val log = logger {}

@Api(value = "Base Controller", description = "Api for S3", tags = ["S3 Test"])
@RestController
class RestController(private val s3Service: S3Service) {
    /**
     * @return ResponseEntity
     */
    @ApiOperation(value = "Метод для перекладывания из базы данных в S3")
    @GetMapping(value = ["/db"], produces = [APPLICATION_JSON])
    fun doWorkUnderDB(): ResponseEntity<String> {
        s3Service.putFileFromDcmMedia()
        return ResponseEntity("Please, monitoring log.txt", OK)
    }

/**
     * @return ResponseEntity
     */
    @ApiOperation(value = "Метод для перекладывания из файловой системы в S3")
    @GetMapping(value = ["/filesystem"], produces = [APPLICATION_JSON])
    fun doWorkUnderFileSystem(): ResponseEntity<String> {
        s3Service.putFileFromFileSystem()
        return ResponseEntity("Please, monitoring log.txt", OK)
    }
}
