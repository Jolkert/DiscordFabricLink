package io.github.jolkert.discordbotlink.jda.data

import io.github.jolkert.discordbotlink.extension.pascalToSnake
import java.util.*

data class BotConfig(val token: String, val commandPrefix: String, val linkChannelId: String, val kickMessage: String)
{
	fun toProperties(): Properties = Properties().apply {
		setProperty(BotConfig::token.name.pascalToSnake(), token)
		setProperty(BotConfig::commandPrefix.name.pascalToSnake(), commandPrefix)
		setProperty(BotConfig::linkChannelId.name.pascalToSnake(), linkChannelId)
		setProperty(BotConfig::kickMessage.name.pascalToSnake(), kickMessage)
	}

	companion object
	{
		val DEFAULT = BotConfig("0", "!", "0", "")
	}
}