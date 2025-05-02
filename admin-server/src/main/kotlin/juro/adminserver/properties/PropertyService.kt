package juro.adminserver.properties

import com.fasterxml.jackson.databind.ObjectMapper
import juro.adminserver.properties.model.CloudConfigRefreshRequest
import juro.adminserver.properties.model.LogicSwitchConstants
import juro.adminserver.properties.model.PropertiesChangeEvent
import juro.adminserver.properties.model.Property
import juro.adminserver.properties.model.PropertyRequest
import juro.adminserver.properties.model.PropertyResponse
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.support.KafkaHeaders
import org.springframework.messaging.MessageHeaders
import org.springframework.messaging.support.MessageBuilder
import org.springframework.stereotype.Service

@Service
class PropertyService(
    private val repository: PropertyRepository,
    private val cluster1KafkaTemplate: KafkaTemplate<String, String>,
    private val cluster2KafkaTemplate: KafkaTemplate<String, String>,
) {
    private val objectMapper = ObjectMapper()
    fun getProperty(id: Long): PropertyResponse {
        val property = repository.findById(id).orElseThrow()
        return PropertyResponse.of(property)
    }

    fun createProperty(request: PropertyRequest): PropertyResponse {
        val existingProperty = repository.findByApplicationAndDeployPhaseAndPropertyKey(request.application, request.deployPhase, request.propertyKey)
        if (existingProperty != null) {
            throw IllegalArgumentException("Property key already exists. request=$request")
        }

        val property = request.toEntity()
        val savedProperty = repository.save(property)
        updateVersion(request.application, request.deployPhase)
        return PropertyResponse.of(savedProperty)
    }

    fun updateProperty(id: Long, request: PropertyRequest): PropertyResponse {
        val property = repository.findById(id).orElseThrow()
        property.update(request)
        val updatedProperty = repository.save(property)
        updateVersion(request.application, request.deployPhase)
        return PropertyResponse.of(updatedProperty)
    }

    fun deleteProperty(id: Long) {
        val existingProperty = repository.findById(id).orElseThrow()
        repository.deleteById(id)
        updateVersion(existingProperty.application, existingProperty.deployPhase)
    }

    fun refresh(request: CloudConfigRefreshRequest) {
        val versionProperties = getAllVersionOfModules(request.deployPhase)
        val changeEvent = PropertiesChangeEvent.of(request, versionProperties)
        val messageHeaders = MessageHeaders(mapOf(KafkaHeaders.TOPIC to LogicSwitchConstants.KAFKA_TOPIC))

        val kafkaMessage = objectMapper.writeValueAsString(changeEvent)
        val message = MessageBuilder.createMessage(kafkaMessage, messageHeaders)

        cluster1KafkaTemplate.send(message)
        cluster2KafkaTemplate.send(message)
    }

    fun getAllVersionOfModules(deployPhase: String): List<Property> {
        return repository.findAllByDeployPhaseAndPropertyKey(deployPhase, LogicSwitchConstants.VERSION_PROPERTY_KEY)
    }


    fun getByApplicationAndDeployPhaseAndPropertyKey(
        application: String,
        deployPhase: String,
        propertyKey: String
    ): PropertyResponse {
        val property = repository.findByApplicationAndDeployPhaseAndPropertyKey(application, deployPhase, propertyKey)
            ?: throw IllegalArgumentException("Property not found. application=$application, deployPhase=$deployPhase, propertyKey=$propertyKey")
        return PropertyResponse.of(property)
    }

    fun deleteByApplicationAndDeployPhaseAndPropertyKey(
        application: String,
        deployPhase: String,
        propertyKey: String
    ) {
        val id = repository.findByApplicationAndDeployPhaseAndPropertyKey(application, deployPhase, propertyKey)?.id
        if (id != null) {
            deleteProperty(id)
        }
    }

    private fun updateVersion(application: String, deployPhase: String) {
        val find = repository.findByApplicationAndDeployPhaseAndPropertyKey(application, deployPhase, LogicSwitchConstants.VERSION_PROPERTY_KEY)
        if (find == null) {
            val versionProperty = Property.ofVersion(application = application, deployPhase = deployPhase)
            repository.save(versionProperty)
        } else {
            find.updateVersion()
            repository.save(find)
        }
    }

}
