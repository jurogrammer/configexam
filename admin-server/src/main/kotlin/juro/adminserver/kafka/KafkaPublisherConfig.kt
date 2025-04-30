package juro.adminserver.kafka

import org.springframework.boot.autoconfigure.kafka.KafkaProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.core.DefaultKafkaProducerFactory
import org.springframework.kafka.core.KafkaTemplate

@Configuration
@EnableConfigurationProperties(CustomKafkaProperties::class)
class KafkaPublisherConfig(private val kafkaProperties: CustomKafkaProperties) {
    @Bean
    fun cluster1KafkaTemplate(): KafkaTemplate<String, String> {
        return getKafkaTemplate(kafkaProperties.cluster1)
    }

    @Bean
    fun cluster2KafkaTemplate(): KafkaTemplate<String, String> {
        return getKafkaTemplate(kafkaProperties.cluster2)
    }


    private fun getKafkaTemplate(kafkaProperties: KafkaProperties): KafkaTemplate<String, String> {
        val properties = KafkaPropertiesFactory.getKafkaProducerProperties(kafkaProperties)

        val producerConfigs = properties.entries.associate { it.key.toString() to it.value }
        return KafkaTemplate<String, String>(DefaultKafkaProducerFactory(producerConfigs))
    }

}
