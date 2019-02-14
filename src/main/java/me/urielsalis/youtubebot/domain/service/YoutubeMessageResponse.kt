package me.urielsalis.youtubebot.domain.service

import me.urielsalis.youtubebot.domain.model.message.YoutubeMessage

data class YoutubeMessageResponse(
        val nextPageToken: String,
        val messages: List<YoutubeMessage>,
        val sleep: Long
)
