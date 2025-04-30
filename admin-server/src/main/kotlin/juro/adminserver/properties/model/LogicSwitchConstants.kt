package juro.adminserver.properties.model

object LogicSwitchConstants {
    // Spring Cloud에서 default application-name으로 사용. 반드시 조회
    const val ALL_APPLICATION = "application"

    // 멱등성 보장을 위한 버전 관리 키
    const val VERSION_PROPERTY_KEY = "config.client.version"
}
