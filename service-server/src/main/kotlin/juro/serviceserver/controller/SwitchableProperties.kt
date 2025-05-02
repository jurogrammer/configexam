package juro.serviceserver.controller

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "switchable")
class SwitchableProperties {
    var featureOn: Boolean = false
    lateinit var someBaseUrl: String
    lateinit var testKafkaStreamServer: String
}
