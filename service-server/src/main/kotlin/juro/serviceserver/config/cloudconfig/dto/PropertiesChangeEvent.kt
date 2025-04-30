package com.naver.nspa.support.logicswitch.dto

/**
 * 로직 스위치 리프레시 이벤트 모델
 */
data class PropertiesChangeEvent(
    // 리프레시 되어야 할 deployPhase
    val deployPhase: String,
    // 리프레시 대상 application. "application"일 경우 모든 application 리프레시.
    val refreshTargetApplication: String,
    // 모듈 별 버전 정보
    val versionProperties: List<VersionProperty>,
) {

    data class VersionProperty(
        // 모듈 명
        val application: String,
        // 모듈의 프로퍼티 버전
        val version: Long,
    )
}
