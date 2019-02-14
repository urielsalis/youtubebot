package me.urielsalis.youtubebot.domain.model.message

data class YoutubeAuthor(
        val id: YoutubeAuthorId,
        val username: YoutubeAuthorUsername,
        val permissions: YoutubeAuthorPermissions
) {
    fun hasPermissions() = permissions.chatModerator || permissions.chatOwner
}
