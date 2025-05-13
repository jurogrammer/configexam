package juro.serviceserver.controller

import org.springframework.boot.context.properties.ConfigurationProperties

/**
 * 코틀린에서는 var 필드 변수로 선언해주어야 정상 동작 - [git issue](https://github.com/spring-cloud/spring-cloud-commons/issues/1029)
 */
@ConfigurationProperties(prefix = "switchable")
class SwitchableProperties {
    var featureOn: Boolean = false
}
