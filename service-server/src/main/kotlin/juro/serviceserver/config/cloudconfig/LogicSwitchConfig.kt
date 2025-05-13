package juro.serviceserver.config.cloudconfig


import com.naver.nspa.support.logicswitch.dto.PropertiesChangeEvent
import io.github.oshai.kotlinlogging.KotlinLogging
import juro.serviceserver.config.cloudconfig.constant.LogicSwitchConstants
import org.apache.kafka.clients.CommonClientConfigs
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.common.serialization.StringDeserializer
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.AutoConfigureAfter
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.autoconfigure.kafka.KafkaProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.cloud.autoconfigure.RefreshAutoConfiguration
import org.springframework.cloud.context.refresh.LegacyContextRefresher
import org.springframework.cloud.context.scope.refresh.RefreshScope
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.core.ConsumerFactory
import org.springframework.kafka.core.DefaultKafkaConsumerFactory
import org.springframework.kafka.listener.CommonLoggingErrorHandler
import org.springframework.kafka.listener.ContainerProperties
import org.springframework.kafka.listener.KafkaMessageListenerContainer
import java.time.Duration
import java.util.*

private val log = KotlinLogging.logger {}

@Configuration
@EnableConfigurationProperties(CustomKafkaProperties::class)
@AutoConfigureAfter(RefreshAutoConfiguration::class)
@ConditionalOnProperty(name = [RefreshAutoConfiguration.REFRESH_SCOPE_ENABLED], matchIfMissing = true)
class LogicSwitchConfig(
    private val customKafkaProperties: CustomKafkaProperties,

    @Value("\${spring.profiles.active}")
    private val deployPhase: String,

    @Value("\${spring.application.name}")
    private val applicationName: String,
) {
    private val getKafkaGroupId = { "logic-switch-$applicationName-${UUID.randomUUID()}" }


    @Bean
    fun cluster1KafkaListenerContainerFactory(
        refresher: Refresher,
        propertiesVersion: PropertiesVersion,
        cluster1ConsumerFactory: ConsumerFactory<String, PropertiesChangeEvent>,
    ): KafkaMessageListenerContainer<String, PropertiesChangeEvent> {
        return buildKafkaMessageListenerContainer(refresher, propertiesVersion, cluster1ConsumerFactory)
    }

    @Bean
    fun cluster2KafkaListenerContainerFactory(
        refresher: Refresher,
        propertiesVersion: PropertiesVersion,
        cluster2ConsumerFactory: ConsumerFactory<String, PropertiesChangeEvent>,
    ): KafkaMessageListenerContainer<String, PropertiesChangeEvent> {
        return buildKafkaMessageListenerContainer(refresher, propertiesVersion, cluster2ConsumerFactory)
    }


    @Bean
    fun cluster1ConsumerFactory(): ConsumerFactory<String, PropertiesChangeEvent> {
        return DefaultKafkaConsumerFactory(
            consumerConfigs(customKafkaProperties.cluster1),
            StringDeserializer(),
            PropertiesChangeEventDeserializer(),
        )
    }

    @Bean
    fun cluster2ConsumerFactory(): ConsumerFactory<String, PropertiesChangeEvent> {
        return DefaultKafkaConsumerFactory(
            consumerConfigs(customKafkaProperties.cluster2),
            StringDeserializer(),
            PropertiesChangeEventDeserializer(),
        )
    }

    private fun consumerConfigs(kafkaProperties: KafkaProperties): Map<String, Any> {
        val config = HashMap<String, Any>()

        config[ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG] = StringDeserializer::class.java
        config[ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG] = StringDeserializer::class.java
        config[CommonClientConfigs.BOOTSTRAP_SERVERS_CONFIG] = kafkaProperties.bootstrapServers
        config[ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG] = Duration.ofSeconds(30).toMillis().toString()
        config[ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG] = Duration.ofSeconds(5).toMillis().toString()
        config[ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG] = Duration.ofSeconds(15).toMillis().toString()
        config[ConsumerConfig.RECONNECT_BACKOFF_MAX_MS_CONFIG] = 10000L
        config[ConsumerConfig.RECONNECT_BACKOFF_MS_CONFIG] = 3000L
        config[ConsumerConfig.AUTO_OFFSET_RESET_CONFIG] = "latest"
        return config
    }

    @Bean
    fun propertiesVersion(): PropertiesVersion {
        return PropertiesVersion()
    }

    @Bean
    fun refresher(refreshScope: RefreshScope, contextRefresher: LegacyContextRefresher, ac: ConfigurableApplicationContext): Refresher {
        return Refresher(refreshScope, contextRefresher, ac)
    }

    @Bean
    fun eagerLoadingListener(ac: ConfigurableApplicationContext): EagerLoadingListener {
        return EagerLoadingListener(ac)
    }

    private fun buildKafkaMessageListenerContainer(
        refresher: Refresher,
        propertiesVersion: PropertiesVersion,
        consumerFactory: ConsumerFactory<String, PropertiesChangeEvent>
    ): KafkaMessageListenerContainer<String, PropertiesChangeEvent> {
        val containerProperties = ContainerProperties(LogicSwitchConstants.KAFKA_TOPIC)

        containerProperties.setGroupId(getKafkaGroupId())
        containerProperties.messageListener = PropertiesVersionChangeListener(refresher, propertiesVersion, deployPhase, applicationName)

        val factory = KafkaMessageListenerContainer(
            consumerFactory,
            containerProperties,
        )
        factory.commonErrorHandler = CommonLoggingErrorHandler()

        return factory
    }

}
