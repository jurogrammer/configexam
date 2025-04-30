package juro.adminserver.properties.model

data class CloudConfigRefreshRequest(
    val application: String,
    val deployPhase: String,
)
