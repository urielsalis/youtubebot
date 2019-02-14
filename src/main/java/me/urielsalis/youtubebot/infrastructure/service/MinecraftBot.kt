package me.urielsalis.youtubebot.infrastructure.service

import me.urielsalis.youtubebot.application.messageparsing.MessageParsingRequest
import me.urielsalis.youtubebot.application.messageparsing.MessageParsingUseCase
import me.urielsalis.youtubebot.domain.service.AuthenticatorService
import me.urielsalis.youtubebot.domain.service.YoutubeService
import org.koin.core.KoinComponent
import org.koin.core.inject
import java.util.concurrent.TimeUnit

class MinecraftBot : KoinComponent {
    private val youtubeService by inject<YoutubeService>()
    private val authenticatorService by inject<AuthenticatorService>()
    private val messageParsingUseCase by inject<MessageParsingUseCase>()

    private val credential = authenticatorService.authorize()
    private val stream = youtubeService.getStreams(credential)

    fun listenForEvents() {
        var previousToken = youtubeService.getMessages(credential, stream, "").nextPageToken

        while (true) {
            val content = youtubeService.getMessages(credential, stream, previousToken)
            previousToken = content.nextPageToken

            content.messages.forEach {
                messageParsingUseCase(MessageParsingRequest(credential, stream, it))
            }

            if (credential.expiresInSeconds < 10) {
                credential.refreshToken()
            }

            // We can sleep for content.sleep miliseconds but keeping it low to stay in quota
            TimeUnit.SECONDS.sleep(5)
        }
    }
}
