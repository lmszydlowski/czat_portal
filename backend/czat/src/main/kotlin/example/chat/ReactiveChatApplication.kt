package com.example.chat

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScan

import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories


@SpringBootApplication
@ComponentScan(basePackages = ["com.example.chat", "example.chat.repository"])
@EnableR2dbcRepositories(basePackages = ["example.chat.repository"]) // For R2DBC
class ReactiveChatApplication

fun main(args: Array<String>) {
    runApplication<ReactiveChatApplication>(*args)
}
