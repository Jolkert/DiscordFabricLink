package io.github.jolkert.discordbotlink.jda.command

import io.github.jolkert.discordbotlink.DiscordBotLink
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import net.dv8tion.jda.api.interactions.commands.OptionType
import net.dv8tion.jda.api.interactions.commands.build.Commands
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData
import net.minecraft.text.Text

class KickMessageCommand : Command()

{
	override val name = "kick-message"
	override val aliases = arrayOf("km")
	override val slashCommandData =
		Commands.slash("kick-message", "Change the message shown when a player is kicked for not being on the whitelist")
		.addOption(
			OptionType.STRING,
			"message",
			"The message to be shown when a player is kicked for not being on the whitelist",
			true
		)

	override fun executeTextCommand(event: MessageReceivedEvent, vararg args: String) { }
	override fun executeSlashCommand(event: SlashCommandInteractionEvent)
	{
		val message = event.getOption("message")!!.asString
		setKickMessage(message)
		event.reply("Updated kick message to `$message`!").setEphemeral(true).queue()
	}

	private fun setKickMessage(message: String)
	{
		DiscordBotLink.Bot.kickMessage = Text.literal(message)
	}
}