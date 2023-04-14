package io.github.jolkert.discordbotlink.jda.command

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import net.dv8tion.jda.api.interactions.commands.build.Commands
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData

class PingCommand : Command()
{
	override val name = "ping"
	override val aliases = emptyArray<String>()
	override val slashCommandData: SlashCommandData = Commands.slash("ping", "Ping the bot")

	override fun executeTextCommand(event: MessageReceivedEvent, vararg args: String) =
		event.channel.sendMessage("Online!").queue()

	override fun executeSlashCommand(event: SlashCommandInteractionEvent) =
		event.reply("Online!").setEphemeral(true).queue()
}