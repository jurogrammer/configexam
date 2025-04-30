package juro.adminserver.properties.model

import java.time.Instant


data class PropertyResponse(
    val id: String,
    val application: String,
    val deployPhase: String,
    val propertyKey: String,
    val propertyValue: String,
    val description: String?,
    val createdAt: Instant,
    val updatedAt: Instant,
) {
    companion object {
        fun of(entity: Property): PropertyResponse {
            return with(entity) {
                PropertyResponse(
                    id = id!!,
                    application = application,
                    deployPhase = deployPhase,
                    propertyKey = propertyKey,
                    propertyValue = propertyValue,
                    description = description,
                    createdAt = createdAt!!,
                    updatedAt = updatedAt!!,
                )
            }
        }
    }
}
