package com.terrasage.api

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class TerrasageApiApplication

fun main(args: Array<String>) {
    runApplication<TerrasageApiApplication>(*args)
}
