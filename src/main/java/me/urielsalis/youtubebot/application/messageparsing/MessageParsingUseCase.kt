package me.urielsalis.youtubebot.application.messageparsing

import com.google.api.client.auth.oauth2.Credential
import me.urielsalis.youtubebot.domain.model.message.YoutubeMessage
import me.urielsalis.youtubebot.domain.model.stream.YoutubeStream
import me.urielsalis.youtubebot.domain.repository.CommandRepository
import me.urielsalis.youtubebot.domain.service.YoutubeService
import java.text.SimpleDateFormat
import java.util.*

class MessageParsingUseCase(
        private val commandRepository: CommandRepository,
        private val youtubeService: YoutubeService
) {
    private val dateFormat = SimpleDateFormat("HH:mm:ss")

    operator fun invoke(request: MessageParsingRequest): MessageParsingResponse {
        val message = request.youtubeMessage
        val permissionOutput = when {
            message.author.permissions.chatOwner -> "@"
            message.author.permissions.chatModerator -> "+"
            else -> ""
        }
        val date = dateFormat.format(Date(message.date.value.value))
        val text = message.message.value

        println("[$date] $permissionOutput${message.author.username.value}: $text")
        return if (text.startsWith("!")) {
            parseCommand(message, request.credential, request.stream)
        } else {
            SucessfulMessageParsingResponse
        }
    }

    private fun parseCommand(
            message: YoutubeMessage,
            credential: Credential,
            stream: YoutubeStream
    ): MessageParsingResponse {
        val command = message.message.value.substring(1)

        return if (command.startsWith("add")) {
            if (!message.author.hasPermissions()) {
                NotEnoughPermissionsMessageParsingResponse
            }

            val split = command.split(" ")
            val commandName = split[1]
            val text = split.subList(2, split.size).joinToString(" ")
            commandRepository.save(commandName, text)
            youtubeService.sendMessage(credential, stream, "Added command $commandName - $text")
            SucessfulMessageParsingResponse
        } else {
            val text = commandRepository.findByKey(command)
            text?.let {
                youtubeService.sendMessage(credential, stream, it)
            }
            SucessfulMessageParsingResponse
        }
    }

}