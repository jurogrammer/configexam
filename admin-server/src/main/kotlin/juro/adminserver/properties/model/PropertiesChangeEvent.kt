package juro.adminserver.properties.model

data class PropertiesChangeEvent(
    val deployPhase: String,
    val refreshTargetApplication: String,
    val versionProperties: List<VersionProperty>,
) {
    data class VersionProperty(
        val application: String,
        val version: Long,
    ) {
        companion object {
            fun of(dto: Property): VersionProperty {
                return with(dto) {
                    VersionProperty(
                        application = application,
                        version = propertyValue.toLong(),
                    )
                }
            }
        }
    }

    companion object {
        fun of(request: CloudConfigRefreshRequest, versionProperties: List<Property>): Any {
            return PropertiesChangeEvent(
                deployPhase = request.deployPhase,
                refreshTargetApplication = request.application,
                versionProperties = versionProperties.map { VersionProperty.of(it) },
            )
        }
    }
}
