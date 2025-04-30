package juro.adminserver.kafka

import org.springframework.boot.autoconfigure.kafka.KafkaProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.core.DefaultKafkaProducerFactory
import org.springframework.kafka.core.KafkaTemplate

@Configuration
@EnableConfigurationProperties(KafkaProperties::class)
class KafkaPublisherConfig(private val kafkaProperties: KafkaProperties) {
    @Bean
    fun kafkaTemplate(): KafkaTemplate<String, String> {
        val properties = KafkaPropertiesFactory.getKafkaProducerProperties(kafkaProperties)

        val producerConfigs = properties.entries.associate { it.key.toString() to it.value }
        return KafkaTemplate<String, String>(DefaultKafkaProducerFactory(producerConfigs))
    }
}
