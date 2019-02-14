package me.urielsalis.youtubebot.domain.service

import com.google.api.client.auth.oauth2.Credential

interface AuthenticatorService {
    fun authorize(): Credential
}