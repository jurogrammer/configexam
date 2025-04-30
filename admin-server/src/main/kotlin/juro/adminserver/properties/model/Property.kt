package juro.adminserver.properties.model

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.time.Instant


@Table("properties")
class Property(
    @field:Id
    var id: String?,
    var application: String,
    @Column("profile")
    var deployPhase: String,
    var propertyKey: String,
    var propertyValue: String,
    var description: String?,
    var createdAt: Instant?,
    var updatedAt: Instant?,
) {
    fun update(request: PropertyRequest) {
        this.application = request.application
        this.deployPhase = request.deployPhase
        this.propertyKey = request.propertyKey
        this.propertyValue = request.propertyValue
        this.description = request.description
        this.updatedAt = Instant.now()
    }

    fun updateVersion() {
        this.propertyValue = System.currentTimeMillis().toString()
        this.updatedAt = Instant.now()
    }

    companion object {
        fun ofVersion(application: String, deployPhase: String): Property {
            return Property(
                id = null,
                application = application,
                deployPhase = deployPhase,
                propertyKey = LogicSwitchConstants.VERSION_PROPERTY_KEY,
                propertyValue = System.currentTimeMillis().toString(),
                description = "멱등성 보장 위한 버전 관리",
                createdAt = Instant.now(),
                updatedAt = Instant.now(),
            )


        }
    }
}
