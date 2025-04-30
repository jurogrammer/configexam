package juro.adminserver.properties.model

import java.time.Instant

data class PropertyRequest(
    val application: String, // CloudConfigApplicationType
    val deployPhase: String, // DeployPhase
    val propertyKey: String,
    val propertyValue: String,
    val description: String?,
) {
    fun toEntity(): Property {
        return Property(
            id = null,
            application = application,
            deployPhase = deployPhase,
            propertyKey = propertyKey,
            propertyValue = propertyValue,
            description = description,
            createdAt = Instant.now(),
            updatedAt = Instant.now(),
        )
    }
}
