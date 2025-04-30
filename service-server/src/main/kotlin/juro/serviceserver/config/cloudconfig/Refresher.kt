package juro.serviceserver.config.cloudconfig


import io.github.oshai.kotlinlogging.KotlinLogging
import juro.serviceserver.config.cloudconfig.annotation.ConditionalOnRefresh
import org.springframework.aop.scope.ScopedProxyUtils
import org.springframework.cloud.context.refresh.LegacyContextRefresher
import org.springframework.cloud.context.scope.refresh.RefreshScope
import org.springframework.context.ConfigurableApplicationContext

private val log = KotlinLogging.logger {}

class Refresher(
    private val refreshScope: RefreshScope,
    private val contextRefresher: LegacyContextRefresher,
    private val ac: ConfigurableApplicationContext,
) {

    fun refresh() {
        val refreshedBeanNames: MutableSet<String> = mutableSetOf()
        val refreshPropertyKeys = contextRefresher.refreshEnvironment()
        val beanNames = ac.getBeanNamesForAnnotation(ConditionalOnRefresh::class.java)

        for (beanName in beanNames) {
            if (alreadyRefreshed(beanName, refreshedBeanNames)) {
                continue
            }

            if (!ScopedProxyUtils.isScopedTarget(beanName)) {
                continue
            }

            val annotation = ac.findAnnotationOnBean(beanName, ConditionalOnRefresh::class.java) ?: continue

            val propertyPrefixes = annotation.value
            val tobeRefresh = propertyPrefixes.any { propertyPrefix: String -> refreshPropertyKeys.any { it.startsWith(propertyPrefix) } }

            if (tobeRefresh) {
                log.info { "start refreshing a bean. beanName=$beanName" }
                refreshScope.refresh(beanName)
                log.info { "end refreshing a bean. beanName=$beanName" }

                refreshedBeanNames.add(beanName)
            }
        }
    }

    // property key를 복수 개 설정했을 경우, 중복하여 refresh되는 것을 막기 위해 이미 refresh 되었는 지 검사
    private fun alreadyRefreshed(beanName: String, refreshedBeanNames: Set<String>): Boolean {
        return refreshedBeanNames.contains(beanName)
    }
}
