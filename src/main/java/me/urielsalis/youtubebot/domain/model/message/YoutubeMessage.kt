package me.urielsalis.youtubebot.domain.model.message

data class YoutubeMessage(
        val message: YoutubeMessageContent,
        val date: YoutubeMessageDate,
        val author: YoutubeAuthor
)
