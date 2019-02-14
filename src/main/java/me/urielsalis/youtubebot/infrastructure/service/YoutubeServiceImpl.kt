package me.urielsalis.youtubebot.infrastructure.service

import com.google.api.client.auth.oauth2.Credential
import com.google.api.services.youtube.YouTube
import com.google.api.services.youtube.model.LiveChatMessage
import com.google.api.services.youtube.model.LiveChatMessageSnippet
import com.google.api.services.youtube.model.LiveChatTextMessageDetails
import me.urielsalis.youtubebot.domain.model.message.*
import me.urielsalis.youtubebot.domain.model.stream.YoutubeStream
import me.urielsalis.youtubebot.domain.model.stream.YoutubeStreamId
import me.urielsalis.youtubebot.domain.model.stream.YoutubeStreamTitle
import me.urielsalis.youtubebot.domain.service.AuthenticatorService
import me.urielsalis.youtubebot.domain.service.YoutubeMessageResponse
import me.urielsalis.youtubebot.domain.service.YoutubeService
import me.urielsalis.youtubebot.infrastructure.HTTP_TRANSPORT
import me.urielsalis.youtubebot.infrastructure.JSON_FACTORY

class YoutubeServiceImpl(
        private val authenticatorService: AuthenticatorService
) : YoutubeService {
    override fun sendMessage(credential: Credential, youtubeStream: YoutubeStream, message: String) {
        val youtube = YouTube.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential)
                .setApplicationName("minecraftbot")
                .build()

        val liveChatMessage = LiveChatMessage()
        liveChatMessage.snippet = LiveChatMessageSnippet()
        liveChatMessage.snippet.textMessageDetails = LiveChatTextMessageDetails()
        liveChatMessage.snippet.type = "textMessageEvent"
        liveChatMessage.snippet.liveChatId = youtubeStream.streamId.value
        liveChatMessage.snippet.textMessageDetails.messageText = message
        val request = youtube.liveChatMessages().insert("snippet", liveChatMessage)
        request.execute()
    }

    override fun getMessages(credential: Credential, youtubeStream: YoutubeStream, pageToken: String): YoutubeMessageResponse {
        val youtube = YouTube.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential)
                .setApplicationName("minecraftbot")
                .build()

        val request = youtube.liveChatMessages().list(youtubeStream.streamId.value, "snippet, authorDetails")
        request.liveChatId = youtubeStream.streamId.value
        request.pageToken = pageToken
        val response = request.execute()

        val nextToken = response.nextPageToken
        val returnedList = response.items
        return YoutubeMessageResponse(
                nextPageToken = nextToken,
                sleep = response.pollingIntervalMillis,
                messages = returnedList.map {
                    YoutubeMessage(
                            YoutubeMessageContent(it.snippet.displayMessage),
                            YoutubeMessageDate(it.snippet.publishedAt),
                            YoutubeAuthor(
                                    YoutubeAuthorId(it.authorDetails.channelId),
                                    YoutubeAuthorUsername(it.authorDetails.displayName),
                                    YoutubeAuthorPermissions(it.authorDetails.isChatOwner, it.authorDetails.isChatModerator)
                            )
                    )
                }
        )
    }

    override fun getStreams(credential: Credential): YoutubeStream {
        val youtube = YouTube.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential)
                .setApplicationName("minecraftbot")
                .build()

        val request = youtube.liveBroadcasts().list("id, snippet, contentDetails")
        request.broadcastStatus = "active"
        request.broadcastType = "all"
        val response = request.execute()

        val returnedList = response.items

        val stream = returnedList.first()
        return YoutubeStream(
                YoutubeStreamId(stream.id),
                YoutubeStreamTitle(stream.snippet.title),
                YoutubeStreamId(stream.snippet.liveChatId)
        )
    }

}