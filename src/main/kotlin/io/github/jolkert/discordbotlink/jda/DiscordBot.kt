package io.github.jolkert.discordbotlink.jda

import io.github.jolkert.discordbotlink.DiscordBotLink.LOGGER
import io.github.jolkert.discordbotlink.jda.command.WhitelistButtonCommand
import io.github.jolkert.discordbotlink.jda.data.BotConfig
import io.github.jolkert.discordbotlink.jda.listener.CommandHandler
import io.github.jolkert.discordbotlink.jda.listener.LinkChannelListener
import io.github.jolkert.discordbotlink.jda.listener.UserUpdateListener
import net.dv8tion.jda.api.JDABuilder
import net.dv8tion.jda.api.entities.Activity
import net.dv8tion.jda.api.entities.Guild
import net.dv8tion.jda.api.entities.User
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel
import net.dv8tion.jda.api.events.session.ReadyEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import net.dv8tion.jda.api.requests.GatewayIntent
import net.dv8tion.jda.api.utils.MemberCachePolicy

class DiscordBot(private val config: BotConfig) : ListenerAdapter()
{
	private val jda = JDABuilder.createLight(config.token, GatewayIntent.getIntents(GatewayIntent.ALL_INTENTS))
		.addEventListeners(this, CommandHandler(config.commandPrefix), UserUpdateListener, LinkChannelListener, WhitelistButtonCommand.Companion.ButtonListener)
		.setActivity(Activity.playing("Prefix: ${config.commandPrefix}"))
		.setMemberCachePolicy(MemberCachePolicy.ALL)
		.build()
		.apply { updateCommands().addCommands(CommandHandler.commands.map { it.slashCommandData }).queue() }

	private lateinit var primaryGuild: Guild
	var linkChannel: TextChannel? = null
		private set

	val self: User
		get() = jda.selfUser

	override fun onReady(event: ReadyEvent)
	{
		primaryGuild = jda.guilds.first()
		primaryGuild.loadMembers()
		linkChannel = jda.getTextChannelById(config.linkChannelId)

		LOGGER.info("Discord bot ready!")
		LOGGER.info("Primary Guild: ${primaryGuild.name}")
	}

	fun sendMessageInLinkChannel(message: String)
	{
		linkChannel?.sendMessage(message)?.queue()
	}
}