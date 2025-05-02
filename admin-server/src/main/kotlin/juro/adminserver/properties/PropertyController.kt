package juro.adminserver.properties

import juro.adminserver.properties.model.CloudConfigRefreshRequest
import juro.adminserver.properties.model.PropertyRequest
import juro.adminserver.properties.model.PropertyResponse
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController


@RestController
class PropertyController(
    private val propertyService: PropertyService,
) {
    @GetMapping("/v1/properties/{id}")
    fun getProperty(@PathVariable id: Long): PropertyResponse {
        return propertyService.getProperty(id)
    }

    @PostMapping("/v1/properties")
    fun createProperties(@RequestBody request: PropertyRequest): PropertyResponse {
        return propertyService.createProperty(request)
    }

    @PutMapping("/v1/properties/{id}")
    fun updateProperty(
        @PathVariable id: Long,
        @RequestBody apiRequest: PropertyRequest,
    ): PropertyResponse {
        return propertyService.updateProperty(id, apiRequest)
    }

    @DeleteMapping("/v1/properties/{id}")
    fun deleteProperty(@PathVariable id: Long) {
        propertyService.deleteProperty(id)
    }

    @PostMapping("/v1/properties/refresh")
    fun refreshProperties(@RequestBody apiRequest: CloudConfigRefreshRequest) {
        propertyService.refresh(apiRequest)
    }
}
