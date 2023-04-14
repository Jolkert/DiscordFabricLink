package io.github.jolkert.discordbotlink.jda.listener

import io.github.jolkert.discordbotlink.DiscordBotLink.LOGGER
import io.github.jolkert.discordbotlink.extension.nameWithDiscriminator
import io.github.jolkert.discordbotlink.jda.command.Command
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import kotlin.reflect.full.createInstance

class CommandHandler(private val commandPrefix: String) : ListenerAdapter()
{
	// TODO: try to make this a singleton object? I think that would make sense?
	companion object
	{
		val commands: Array<Command> =
			Command::class.sealedSubclasses.let { Array<Command>(it.size) { i: Int -> it[i].createInstance() } }

		private val commandMap: HashMap<String, Command> =
			HashMap<String, Command>().apply { commands.forEach { putValueAtKeys(it, it.allIdentifiers) } }

		// extensions
		private fun <K, V> HashMap<K, V>.putValueAtKeys(value: V, keys: Sequence<K>)
		{
			for (key in keys)
				this[key] = value
		}

		private fun String.afterPrefix(prefix: String): String = substring(prefix.length)
	}

	override fun onMessageReceived(event: MessageReceivedEvent)
	{
		if (event.author.isBot || event.author.isSystem || !event.message.contentRaw.startsWith(commandPrefix))
			return

		val args: Array<String> = event.message.contentRaw.afterPrefix(commandPrefix).split(' ').toTypedArray()
			.also { it[0] = it[0].lowercase() }
		LOGGER.info("${event.author.nameWithDiscriminator} attempting to run command [${args[0]}]")

		val command = commandMap[args[0]]
		if (command == null)
		{
			LOGGER.info("Could not find command [${args[0]}]")
			return
		}

		val startTime = System.currentTimeMillis()
		command.executeTextCommand(event, *args)
		LOGGER.info("Command [${command.name}] executed in ${System.currentTimeMillis() - startTime}ms")
	}

	override fun onSlashCommandInteraction(event: SlashCommandInteractionEvent)
	{
		LOGGER.info("${event.user.nameWithDiscriminator} running slash command [${event.name}]")
		val startTime = System.currentTimeMillis()
		commandMap[event.name]?.executeSlashCommand(event)
		LOGGER.info("Command [${event.name}] executed in ${System.currentTimeMillis() - startTime}ms")
	}
}