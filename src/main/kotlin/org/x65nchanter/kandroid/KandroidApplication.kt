package org.x65nchanter.kandroid

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication
class KandroidApplication

fun main(args: Array<String>) {
	SpringApplication.run(KandroidApplication::class.java, *args)
}
