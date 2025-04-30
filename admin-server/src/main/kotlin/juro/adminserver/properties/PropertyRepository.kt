package juro.adminserver.properties

import juro.adminserver.properties.model.Property
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface PropertyRepository : CrudRepository<Property, Long> {
    fun findByApplicationAndDeployPhaseAndPropertyKey(application: String, deployPhase: String, propertyKey: String): Property?
    fun findAllByDeployPhaseAndPropertyKey(deployPhase: String, propertyKey: String): List<Property>
}
