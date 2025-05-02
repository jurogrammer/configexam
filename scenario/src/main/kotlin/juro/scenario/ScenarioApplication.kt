package juro.scenario

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class ScenarioApplication

fun main(args: Array<String>) {
	runApplication<ScenarioApplication>(*args)
}
