package juro.serviceserver

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication

@SpringBootApplication
@ConfigurationPropertiesScan
class ServiceServerApplication

fun main(args: Array<String>) {
	runApplication<ServiceServerApplication>(*args)
}
