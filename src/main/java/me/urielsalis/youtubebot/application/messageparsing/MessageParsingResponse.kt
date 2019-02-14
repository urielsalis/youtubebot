package me.urielsalis.youtubebot.application.messageparsing

sealed class MessageParsingResponse
object SucessfulMessageParsingResponse : MessageParsingResponse()
open class FailedMessageParsingResponse : MessageParsingResponse()
object NotEnoughPermissionsMessageParsingResponse : FailedMessageParsingResponse()