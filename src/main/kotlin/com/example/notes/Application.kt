package com.example.notes

import io.micronaut.runtime.Micronaut.build

fun main(args: Array<String>) {
    build()
        .args(*args)
        .packages("com.example.notes")
        .deduceEnvironment(false)
        .start()
}
