package juro.serviceserver.controller

import io.github.oshai.kotlinlogging.KotlinLogging
import juro.serviceserver.service.EagerKafkaStreamRunner
import juro.serviceserver.service.LazyKafkaStreamRunner
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

private val log = KotlinLogging.logger {}

@RestController
class TestController(
    private val switchableProperties: SwitchableProperties,
    private val lazyKafkaStreamRunner: LazyKafkaStreamRunner,
    private val eagerKafkaStreamRunner: EagerKafkaStreamRunner,
) {

    @GetMapping("/feature-on-status")
    fun propertyChange(): String {
        return if (switchableProperties.featureOn) {
            "Feature is ON"
        } else {
            "Feature is OFF"
        }
    }

    @GetMapping("/eager-lazy-kafka-stream")
    fun kafkaStreamTest(): Map<String, Any> {
        log.info { "kafkaStreamStartAt is called" }

        return mapOf(
            "lazy" to mapOf(
                "startAt" to lazyKafkaStreamRunner.startAt(),
                "streamServer" to lazyKafkaStreamRunner.streamServer(),
            ),
            "eager" to mapOf(
                "startAt" to eagerKafkaStreamRunner.startAt(),
                "streamServer" to eagerKafkaStreamRunner.streamServer(),
            )
        )
    }
}

