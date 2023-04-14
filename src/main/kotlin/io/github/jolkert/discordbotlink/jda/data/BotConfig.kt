package io.github.jolkert.discordbotlink.jda.data

data class BotConfig(val token: String, val commandPrefix: String, val linkChannelId: String)
{
	companion object
}