package juro.adminserver

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class AdminServerApplication

fun main(args: Array<String>) {
    runApplication<AdminServerApplication>(*args)
}
