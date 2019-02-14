package me.urielsalis.youtubebot.domain.service

import com.google.api.client.auth.oauth2.Credential
import me.urielsalis.youtubebot.domain.model.stream.YoutubeStream

interface YoutubeService {
    fun getStreams(credential: Credential): YoutubeStream
    fun getMessages(credential: Credential, youtubeStream: YoutubeStream, pageToken: String): YoutubeMessageResponse
    fun sendMessage(credential: Credential, youtubeStream: YoutubeStream, message: String)
}