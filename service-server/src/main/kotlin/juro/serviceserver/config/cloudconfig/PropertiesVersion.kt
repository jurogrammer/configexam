package juro.serviceserver.config.cloudconfig


import java.util.concurrent.atomic.AtomicLong

class PropertiesVersion {
    private val timestamp = AtomicLong(INIT_VERSION)

    fun compareAndSet(currentVersion: Long, newVersion: Long): Boolean {
        return timestamp.compareAndSet(currentVersion, newVersion)
    }

    fun getVersion(): Long = timestamp.get()

    companion object {
        private const val INIT_VERSION = Long.MIN_VALUE
    }
}
