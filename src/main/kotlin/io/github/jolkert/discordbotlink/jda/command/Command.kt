package io.github.jolkert.discordbotlink.jda.command

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData

sealed class Command
{
	abstract val name: String
	abstract val aliases: Array<String>
	abstract val slashCommandData: SlashCommandData

	val allIdentifiers = sequence<String> {
		yield(name)
		aliases.forEach { yield(it) }
	}

	abstract fun executeTextCommand(event: MessageReceivedEvent, vararg args: String)
	abstract fun executeSlashCommand(event: SlashCommandInteractionEvent)
}