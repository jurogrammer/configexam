package juro.serviceserver.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class TestController(
    private val switchableProperties: SwitchableProperties,
) {

    @GetMapping("/test")
    fun propertyChange(): String {
        return if (switchableProperties.featureOn) {
            "Feature is ON"
        } else {
            "Feature is OFF"
        }
    }
}

