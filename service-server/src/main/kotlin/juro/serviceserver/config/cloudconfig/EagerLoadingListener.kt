package juro.serviceserver.config.cloudconfig

import io.github.oshai.kotlinlogging.KotlinLogging
import juro.serviceserver.config.cloudconfig.annotation.ConditionalOnRefresh
import org.springframework.aop.scope.ScopedProxyUtils
import org.springframework.cloud.context.scope.refresh.RefreshScopeRefreshedEvent
import org.springframework.context.ApplicationContext
import org.springframework.context.ApplicationListener

private val log = KotlinLogging.logger {}

class EagerLoadingListener(
    private val ac: ApplicationContext,
) : ApplicationListener<RefreshScopeRefreshedEvent> {

    override fun onApplicationEvent(event: RefreshScopeRefreshedEvent) {
        log.info { "get refresh event. event=$event" }

        if (RefreshScopeRefreshedEvent.DEFAULT_NAME == event.name) {
            log.warn { "cannot published refresh_all event. please check refresh event" }
            return
        }

        if (!ScopedProxyUtils.isScopedTarget(event.name)) {
            log.error { "event is not scoped target name. name=${event.name}" }
            return
        }
        val beanName = ScopedProxyUtils.getOriginalBeanName(event.name)
        val annotation = ac.findAnnotationOnBean(beanName, ConditionalOnRefresh::class.java)
        if (annotation == null) {
            log.warn { "object is refreshed but don't have ConditionalOnRefresh annotation. beanName = $beanName" }
            return
        }
        if (annotation.eagerLoading) {
            val bean = ac.getBean(beanName)
            log.info { "eagerLoading start. beanName=$beanName" }
            initialize(bean)
            log.info { "eagerLoading end. beanName=$beanName" }
        }
    }

    // 임의 메서드 호출해서 이니셜 라이즈 유도
    private fun initialize(bean: Any) {
        bean.toString()
    }
}
