package juro.adminserver

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class AdminserverApplication

fun main(args: Array<String>) {
    runApplication<AdminserverApplication>(*args)
}
