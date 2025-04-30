package juro.adminserver.properties.model

import com.fasterxml.jackson.annotation.JsonProperty

enum class DeployPhase {
    @JsonProperty("local")
    LOCAL,

    @JsonProperty("dev")
    DEV,

    @JsonProperty("real")
    REAL;
    fun toLowerCase(): String {
        return this.name.lowercase()
    }
}
