package me.urielsalis.youtubebot.application.messageparsing

import com.google.api.client.auth.oauth2.Credential
import me.urielsalis.youtubebot.domain.model.message.YoutubeMessage
import me.urielsalis.youtubebot.domain.model.stream.YoutubeStream

data class MessageParsingRequest(
        val credential: Credential,
        val stream: YoutubeStream,
        val youtubeMessage: YoutubeMessage
)
