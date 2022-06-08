package com.bukhalov.s3test.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import software.amazon.awssdk.core.client.config.ClientOverrideConfiguration
import software.amazon.awssdk.regions.Region.US_WEST_1
import software.amazon.awssdk.services.s3.S3Client
import java.net.URI
import java.time.Duration.ofSeconds

@Configuration
class S3Config {

    @Bean
    fun s3Template() = S3Client.builder()
        .region(US_WEST_1)
        .overrideConfiguration(
            ClientOverrideConfiguration.builder()
                .apiCallTimeout(ofSeconds(120))
                .build()
        )
        .endpointOverride(URI.create("https://s3.tele2.ru"))
        .build()
}
