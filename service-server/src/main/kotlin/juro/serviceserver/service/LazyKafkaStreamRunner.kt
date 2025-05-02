package juro.serviceserver.service

import io.github.oshai.kotlinlogging.KotlinLogging
import jakarta.annotation.PostConstruct
import juro.serviceserver.config.cloudconfig.annotation.ConditionalOnRefresh
import juro.serviceserver.controller.SwitchableProperties
import org.springframework.stereotype.Component
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

private val log = KotlinLogging.logger {}

@Component
@ConditionalOnRefresh(propertyPrefixes = ["switchable.test-kafka-stream-server"])
class LazyKafkaStreamRunner(
    private val properties: SwitchableProperties
) {
    private lateinit var startAt: ZonedDateTime

    companion object {
        private val FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
    }

    @PostConstruct
    fun start() {
        startAt = ZonedDateTime.now(ZoneId.of("Asia/Seoul"))
        log.info { "Starting LazyEagerKafkaStreamRunner. bootstrapServer=${properties.testKafkaStreamServer}, startAt=$startAt" }
    }

    fun startAt(): String {
        return startAt.format(FORMATTER)
    }

    fun streamServer(): String {
        return properties.testKafkaStreamServer
    }
}
