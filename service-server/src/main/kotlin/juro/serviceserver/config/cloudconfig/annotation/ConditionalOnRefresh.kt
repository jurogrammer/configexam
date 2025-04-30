package juro.serviceserver.config.cloudconfig.annotation

import org.springframework.cloud.context.config.annotation.RefreshScope
import org.springframework.context.annotation.ScopedProxyMode
import org.springframework.core.annotation.AliasFor
import java.lang.annotation.Inherited

/**
 * 객체 리프레시 대상 지정하는 어노테이션
 *
 * 외부 저장소에 의해 변경된 properties 중 propertyPrefixes에 선언한 property에 하나라도 접두사가 동일할 경우, 리프레시 대상
 *
 * ex:)
 * 변경한 외부 저장소 properties: `resilience4j.circuitbreaker.configs.default`
 *
 * `@ConditionalOnRefresh(propertyPrefixes=["resilience4j.circuitbreaker"])` -> 리프레시 대상 객체 O
 * `@ConditionalOnRefresh(propertyPrefixes=["resilience4j.circuitbreaker.configs.default"])` -> 리프레시 대상 객체 O
 * `@ConditionalOnRefresh(propertyPrefixes=["resilience4j"])` -> 리프레시 대상 객체 X
 *
 */
@Target(AnnotationTarget.CLASS, AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY_GETTER, AnnotationTarget.PROPERTY_SETTER)
@Retention(AnnotationRetention.RUNTIME)
@RefreshScope
@MustBeDocumented
@Inherited
annotation class ConditionalOnRefresh(
    @get:AliasFor("propertyPrefixes")
    vararg val value: String = [],

    @get:AliasFor("value")
    val propertyPrefixes: Array<String> = [],

    val eagerLoading: Boolean = false,

    @get:AliasFor(annotation = RefreshScope::class)
    val proxyMode: ScopedProxyMode = ScopedProxyMode.TARGET_CLASS,
)
