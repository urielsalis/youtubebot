package me.urielsalis.youtubebot.domain.repository

interface CommandRepository {
    fun findByKey(key: String): String?
    fun save(key: String, value: String)
}