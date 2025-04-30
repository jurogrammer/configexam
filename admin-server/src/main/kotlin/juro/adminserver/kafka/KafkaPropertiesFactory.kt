package juro.adminserver.kafka

import org.apache.kafka.clients.CommonClientConfigs
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.common.serialization.StringSerializer
import org.springframework.boot.autoconfigure.kafka.KafkaProperties
import java.time.Duration
import java.util.*

object KafkaPropertiesFactory {

    fun getKafkaProducerProperties(kafkaProperties: KafkaProperties): Properties {
        return Properties().apply {
            // KafkaProperties에서 설정한 bootstrap.servers를 사용
            set(CommonClientConfigs.BOOTSTRAP_SERVERS_CONFIG, "http://localhost:9092")
            set(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer::class.java)
            set(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer::class.java)

            set(ProducerConfig.ACKS_CONFIG, "all")

            // default: INT.MAX_VALUE
            set(ProducerConfig.RETRIES_CONFIG, 20)
            set(ProducerConfig.RETRY_BACKOFF_MS_CONFIG, CommonClientConfigs.DEFAULT_RETRY_BACKOFF_MS)
            set(ProducerConfig.RETRY_BACKOFF_MAX_MS_CONFIG, CommonClientConfigs.DEFAULT_RETRY_BACKOFF_MAX_MS)

            set(ProducerConfig.BATCH_SIZE_CONFIG, 16384)
            set(ProducerConfig.LINGER_MS_CONFIG, 1)
            set(ProducerConfig.BUFFER_MEMORY_CONFIG, 33554432)
            set(ProducerConfig.MAX_BLOCK_MS_CONFIG, Duration.ofSeconds(30).toMillis())

        }
    }

}
