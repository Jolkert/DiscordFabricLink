package io.github.jolkert.discordbotlink.jda.listener

import io.github.jolkert.discordbotlink.DiscordBotLink
import io.github.jolkert.discordbotlink.DiscordBotLink.Bot
import io.github.jolkert.discordbotlink.DiscordBotLink.users
import io.github.jolkert.discordbotlink.extension.isPerson
import io.github.jolkert.discordbotlink.extension.nameWithDiscriminator
import io.github.jolkert.discordbotlink.extension.nicknameOrDefault
import io.github.jolkert.discordbotlink.jda.data.UserData
import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import net.minecraft.text.*
import java.util.*

object LinkChannelListener : ListenerAdapter()
{
	private const val MINECRAFT_COMMAND_PREFIX: String = "./"
	private val DUMMY_UUID = UUID.randomUUID()

	override fun onMessageReceived(event: MessageReceivedEvent)
	{
		if (event.channel.id != Bot.linkChannel?.id || !event.author.isPerson)
			return

		val authorAsMember = event.member
			?: return // smart cast wont work with `event.member` and i dont wanna put !! everywhere -morgan 2023-04-14
		val message = event.message.contentRaw
		val server = DiscordBotLink.Server

		if (message.startsWith(MINECRAFT_COMMAND_PREFIX) && authorAsMember.hasPermission(Permission.MANAGE_SERVER))
		{
			server.sendMessage(Text.of("${event.author.nameWithDiscriminator}: $message"))
			server.commandManager.executeWithPrefix(
				server.commandSource,
				message.substring(MINECRAFT_COMMAND_PREFIX.length - 1)
			)
		} else
		{
			val authorData = users[event.author.id] ?: UserData(
				minecraftData = UserData.MinecraftUserData("",	DUMMY_UUID), // dummy mc data cause we need it. maybe change that later -morgan 2023-04-14
				discordData = UserData.DiscordUserData(event.author),
				memberData = UserData.GuildMemberData.of(authorAsMember)
			)
			val senderText = MutableText.of(LiteralTextContent(authorAsMember.nicknameOrDefault))
				.setStyle(
					Style.EMPTY
						.withColor(authorData.memberData.topColor)
						.withHoverEvent(HoverEvent.Action.SHOW_TEXT.buildHoverEvent(Text.of(authorData.hoverText)))
				)

			val finalMessage = MutableText.of(LiteralTextContent("[")).append(senderText).append("] $message")

			server.playerManager.broadcast(finalMessage, false)
		}
	}
}