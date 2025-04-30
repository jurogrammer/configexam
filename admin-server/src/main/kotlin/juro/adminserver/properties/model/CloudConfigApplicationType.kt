package juro.adminserver.properties.model

import com.fasterxml.jackson.annotation.JsonProperty

enum class CloudConfigApplicationType {
    // 전체
    @JsonProperty("application")
    APPLICATION,

    @JsonProperty("service-server")
    SERVICE_SERVER
    ;

    fun toLowerCase(): String {
        return this.name.lowercase()
    }
}
