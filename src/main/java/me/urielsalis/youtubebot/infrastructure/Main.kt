package me.urielsalis.youtubebot.infrastructure

import me.urielsalis.youtubebot.infrastructure.service.MinecraftBot
import org.koin.core.context.startKoin

fun main() {
    startKoin {
        modules(config)
    }

    val bot = MinecraftBot()
    bot.listenForEvents()
}