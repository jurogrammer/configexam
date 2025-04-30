package juro.serviceserver.config.cloudconfig


import com.naver.nspa.support.logicswitch.dto.PropertiesChangeEvent
import io.github.oshai.kotlinlogging.KotlinLogging
import juro.serviceserver.config.cloudconfig.constant.LogicSwitchConstants
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.springframework.kafka.listener.MessageListener

private val log = KotlinLogging.logger {}

class PropertiesVersionChangeListener(
    private val refresher: Refresher,
    private val propertiesVersion: PropertiesVersion,
    private val deployPhase: String,
    private val currentModuleApplication: String,
) : MessageListener<String, PropertiesChangeEvent> {
    private val TO_BE_REFRESHED_APPLICATION_NAMES = listOf(currentModuleApplication, LogicSwitchConstants.ALL_APPLICATION)

    override fun onMessage(data: ConsumerRecord<String?, PropertiesChangeEvent>) {
        try {
            val changeEvent = data.value()
            log.info { "receive message = $changeEvent" }

            validateChangeEventMessage(changeEvent)

            if (changeEvent.deployPhase != deployPhase) {
                log.info { "deploy phase mismatch. applicationDeployPhase=$deployPhase, messageDeployPhase=${changeEvent.deployPhase}" }
                return
            }

            if (TO_BE_REFRESHED_APPLICATION_NAMES.none { it == changeEvent.refreshTargetApplication }
            ) {
                log.info {
                    "application name mismatch. " +
                            "currentModuleApplication=$currentModuleApplication, targetRefreshApplication=${changeEvent.refreshTargetApplication}"
                }
                return
            }

            val newVersion = changeEvent.versionProperties
                .filter { it.application in TO_BE_REFRESHED_APPLICATION_NAMES }
                .map { it.version }
                .maxOrNull() ?: return

            val currentVersion = propertiesVersion.getVersion()

            if (currentVersion >= newVersion) {
                log.info { "version is already updated. currentVersion=$currentVersion, newVersion=$newVersion" }
                return
            }

            val changed = propertiesVersion.compareAndSet(currentVersion, newVersion)

            if (changed) {
                log.info { "start refresh beans." }
                refresher.refresh()
                log.info { "end refresh beans." }
            } else {
                log.info { "properties already updated by other changeListener." }
            }
        } catch (e: Exception) {
            log.error(e) { "refresh is fail." }
        }
    }

    private fun validateChangeEventMessage(changeEvent: PropertiesChangeEvent) {
        require(changeEvent.refreshTargetApplication.isNotEmpty()) { "applicationName must not be empty." }
        require(changeEvent.deployPhase.isNotEmpty()) { "deployPhase must not be empty." }
        require(changeEvent.versionProperties.isNotEmpty()) { "versionProperties must not be empty." }
        changeEvent.versionProperties.forEach { versionProperty ->
            requireNotNull(versionProperty.version) { "version cannot be null" }
            require(versionProperty.application.isNotBlank()) { "application cannot be null" }
        }
    }
}
