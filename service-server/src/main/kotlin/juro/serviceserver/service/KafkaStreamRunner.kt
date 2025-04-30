package juro.serviceserver.service

import io.github.oshai.kotlinlogging.KotlinLogging
import jakarta.annotation.PostConstruct
import juro.serviceserver.config.cloudconfig.annotation.ConditionalOnRefresh
import juro.serviceserver.controller.SwitchableProperties
import org.springframework.stereotype.Component

private val log = KotlinLogging.logger {}

@Component
@ConditionalOnRefresh(propertyPrefixes = ["switchable.kafka-bootstrap-server"], eagerLoading = true)
class KafkaStreamRunner(
    private val properties: SwitchableProperties
) {
    @PostConstruct
    fun start() {
        log.info { "Starting Kafka Stream Runner. bootstrapServer=${properties.kafkaBootstrapServer}" }
    }
}

