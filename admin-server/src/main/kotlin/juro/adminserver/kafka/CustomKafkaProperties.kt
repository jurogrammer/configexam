package juro.adminserver.kafka

import org.springframework.boot.autoconfigure.kafka.KafkaProperties
import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "kafka")
data class CustomKafkaProperties(
    val cluster1: KafkaProperties,
    val cluster2: KafkaProperties,
)
