package juro.serviceserver

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class ServiceServerApplication

fun main(args: Array<String>) {
	runApplication<ServiceServerApplication>(*args)
}
