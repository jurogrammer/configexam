package juro.serviceserver.config.cloudconfig

import org.apache.kafka.clients.CommonClientConfigs
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.common.serialization.StringDeserializer
import org.springframework.boot.autoconfigure.kafka.KafkaProperties
import java.time.Duration
import java.util.*

object KafkaPropertiesFactory {

    fun getKafkaConsumerProperties(kafkaProperties: KafkaProperties): Properties {
        return Properties().apply {
            set(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer::class.java)
            set(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer::class.java)

            set(CommonClientConfigs.BOOTSTRAP_SERVERS_CONFIG, kafkaProperties.bootstrapServers)

            set(ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG, Duration.ofSeconds(30).toMillis().toString())
            set(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, Duration.ofSeconds(5).toMillis().toString())
            set(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, Duration.ofSeconds(15).toMillis().toString())
            set(ConsumerConfig.FETCH_MAX_WAIT_MS_CONFIG, 200)
            set(ConsumerConfig.RECEIVE_BUFFER_CONFIG, 65536)
            set(ConsumerConfig.ALLOW_AUTO_CREATE_TOPICS_CONFIG, false)
            set(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, 5000)
        }
    }

}
