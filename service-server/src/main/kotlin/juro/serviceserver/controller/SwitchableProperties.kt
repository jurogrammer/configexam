package juro.serviceserver.controller

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "switchable")
class SwitchableProperties {
    lateinit var featureOn: String
    lateinit var someBaseUrl: String
    lateinit var kafkaBootstrapServer: String
}
