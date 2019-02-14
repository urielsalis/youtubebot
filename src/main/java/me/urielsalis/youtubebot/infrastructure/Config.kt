package me.urielsalis.youtubebot.infrastructure

import com.google.api.client.http.HttpTransport
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.jackson2.JacksonFactory
import me.urielsalis.youtubebot.application.messageparsing.MessageParsingUseCase
import me.urielsalis.youtubebot.domain.repository.CommandRepository
import me.urielsalis.youtubebot.domain.service.AuthenticatorService
import me.urielsalis.youtubebot.domain.service.YoutubeService
import me.urielsalis.youtubebot.infrastructure.db.MapDBCommandRepository
import me.urielsalis.youtubebot.infrastructure.service.GoogleAuthenticatorService
import me.urielsalis.youtubebot.infrastructure.service.YoutubeServiceImpl
import org.koin.dsl.module

val config = module {
    single { GoogleAuthenticatorService() as AuthenticatorService }
    single { YoutubeServiceImpl(get()) as YoutubeService }
    single { MapDBCommandRepository() as CommandRepository }
    single { MessageParsingUseCase(get(), get()) }
}

val JSON_FACTORY = JacksonFactory.getDefaultInstance()
val HTTP_TRANSPORT: HttpTransport = NetHttpTransport()
