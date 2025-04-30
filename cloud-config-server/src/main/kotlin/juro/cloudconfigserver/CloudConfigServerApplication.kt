package juro.cloudconfigserver

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.config.server.EnableConfigServer

@EnableConfigServer
@SpringBootApplication
class CloudConfigServerApplication

fun main(args: Array<String>) {
    runApplication<CloudConfigServerApplication>(*args)
}
