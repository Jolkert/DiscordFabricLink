package io.github.jolkert.discordbotlink.jda.data

import net.dv8tion.jda.api.entities.Guild
import net.dv8tion.jda.api.entities.Message
import net.dv8tion.jda.api.entities.User
import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.events.message.MessageReceivedEvent

data class CommandContext(val guild: Guild?, val channel: MessageChannelUnion, val author: User, val message: Message?)
{
	val authorAsMember
		get() = guild?.getMember(author)

	companion object
	{
		@JvmStatic
		fun of(event: MessageReceivedEvent): CommandContext =
			CommandContext(event.guild, event.channel, event.author, event.message)

		@JvmStatic
		fun of(event: SlashCommandInteractionEvent): CommandContext =
			CommandContext(event.guild, event.channel, event.user, null)
	}
}