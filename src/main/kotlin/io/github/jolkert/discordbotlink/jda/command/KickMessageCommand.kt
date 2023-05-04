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

	override fun executeTextCommand(event: MessageReceivedEvent, vararg args: String)
	{
		if (args.size < 2 || args[1].isBlank())
			return

		setKickMessage(args[1])
	}
	override fun executeSlashCommand(event: SlashCommandInteractionEvent)
	{
		setKickMessage(event.getOption("message")!!.asString)
		event.reply("Updated kick message!").queue()
	}

	private fun setKickMessage(message: String)
	{
		DiscordBotLink.Bot.kickMessage = Text.literal(message)

	}
}