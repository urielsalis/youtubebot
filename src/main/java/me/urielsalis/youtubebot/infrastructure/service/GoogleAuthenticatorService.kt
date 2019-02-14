package me.urielsalis.youtubebot.infrastructure.service

import com.google.api.client.auth.oauth2.Credential
import com.google.api.client.auth.oauth2.StoredCredential
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets
import com.google.api.client.util.store.FileDataStoreFactory
import me.urielsalis.youtubebot.domain.service.AuthenticatorService
import me.urielsalis.youtubebot.infrastructure.HTTP_TRANSPORT
import me.urielsalis.youtubebot.infrastructure.JSON_FACTORY
import java.io.File
import java.io.IOException

const val APPLICATION_NAME = "MinecraftBot/1.0"

class GoogleAuthenticatorService : AuthenticatorService {
    val scopes = listOf(
            "https://www.googleapis.com/auth/youtube.force-ssl",
            "https://www.googleapis.com/auth/youtube.readonly",
            "https://www.googleapis.com/auth/youtube"
    )

    override fun authorize(): Credential {
        val secrets = try {
            File("google_credentials.json").reader()
        } catch (e: IOException) {
            System.out.println("Enter Client ID and Secret from Google API Console into google_credentials.json")
            null
        }

        secrets.let {
            val clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, it)

            // This creates the credentials datastore at ~/.oauth-credentials/credentialDatastore
            val fileDataStoreFactory = FileDataStoreFactory(File(System.getProperty("user.home") + "/.oauth-credentials"))
            val datastore = fileDataStoreFactory.getDataStore<StoredCredential>("minecraftbot")

            val flow = GoogleAuthorizationCodeFlow.Builder(
                    HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, scopes).setCredentialDataStore(datastore)
                    .build()

            // Build the local server and bind it to port 8080
            val localReceiver = LocalServerReceiver.Builder().setPort(8080).build()
            return AuthorizationCodeInstalledApp(flow, localReceiver).authorize("user")
        }


    }

}