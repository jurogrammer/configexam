package juro.serviceserver.service

import io.github.oshai.kotlinlogging.KotlinLogging
import jakarta.annotation.PostConstruct
import juro.serviceserver.config.cloudconfig.annotation.ConditionalOnRefresh
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

private val log = KotlinLogging.logger {}

@Component
@ConditionalOnRefresh(propertyPrefixes = ["switchable.test-kafka-stream-server"])
class LazyKafkaStreamRunner{
    private lateinit var startAt: ZonedDateTime

    @Value("\${switchable.test-kafka-stream-server}")
    private lateinit var testKafkaStreamServer: String

    companion object {
        private val FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        private val SEOUL_ZONE_ID = ZoneId.of("Asia/Seoul")
    }

    @PostConstruct
    fun start() {
        startAt = ZonedDateTime.now(SEOUL_ZONE_ID)
        log.info { "Starting LazyEagerKafkaStreamRunner. bootstrapServer=${this.streamServer()}, startAt=${this.startAt()}" }
    }

    fun startAt(): String {
        return startAt.format(FORMATTER)
    }

    fun streamServer(): String {
        return testKafkaStreamServer
    }
}
