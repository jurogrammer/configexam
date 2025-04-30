package juro.serviceserver.config.cloudconfig


import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.kotlin.readValue
import com.naver.nspa.support.logicswitch.dto.PropertiesChangeEvent
import io.github.oshai.kotlinlogging.KotlinLogging
import org.apache.kafka.common.serialization.Deserializer

private val log = KotlinLogging.logger {}

class PropertiesChangeEventDeserializer: Deserializer<PropertiesChangeEvent> {
    private val objectMapper = ObjectMapper().registerModules(KotlinModule.Builder().build())

    override fun deserialize(topic: String, data: ByteArray?): PropertiesChangeEvent? {
        try {
            if (data == null) {
                return null
            }
            return objectMapper.readValue<PropertiesChangeEvent>(data)
        } catch (e: Exception) {
            log.error(e) { "Failed to deserialize data: $data" }
            return null
        }
    }
}
