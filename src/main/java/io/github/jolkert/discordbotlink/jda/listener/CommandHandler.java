package io.github.jolkert.discordbotlink.jda.listener;

import io.github.jolkert.discordbotlink.DiscordBotLink_LEGACY;
import io.github.jolkert.discordbotlink.jda.command.WhitelistCommand;
import io.github.jolkert.discordbotlink.util.NameUtils;
import io.github.jolkert.discordbotlink.jda.command.DiscordCommand;
import io.github.jolkert.discordbotlink.jda.command.PingCommand;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public class CommandHandler extends ListenerAdapter
{
	private final String prefix;
	private final HashMap<String, DiscordCommand> commandMap;
	public static final DiscordCommand[] COMMANDS = {new WhitelistCommand(), new PingCommand()};
	
	public CommandHandler(String prefix)
	{
		this.prefix	= prefix;
		
		commandMap = new HashMap<String, DiscordCommand>();
		for (DiscordCommand command : COMMANDS)
			for (String alias : command.getAllAliases())
				commandMap.put(alias, command);
	}
	
	@Override
	public void onMessageReceived(MessageReceivedEvent event)
	{
		if (event.getAuthor().isBot() || event.getAuthor().isSystem())
			return;

		if (containsPrefix(event.getMessage().getContentRaw(), prefix))
		{
			String[] args = event.getMessage().getContentRaw().substring(prefix.length()).split(" ");
			args[0] = args[0].toLowerCase();
			
			DiscordBotLink_LEGACY.Logger.info(NameUtils.nameWithDiscriminator(event.getAuthor()) + " attempting to run [" + args[0] + "]");
			if (commandMap.containsKey(args[0]))
			{
				DiscordCommand command = commandMap.get(args[0]);
				long start = System.currentTimeMillis();
				command.execute(args, event);
				DiscordBotLink_LEGACY.Logger.info("Command [" + command.getMainAlias() + "] took " + (System.currentTimeMillis() - start) + "ms");
			}
			else
				DiscordBotLink_LEGACY.Logger.info("Could not find command [" + args[0] + "]");
		}
	}
	@Override
	public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event)
	{
		if (!commandMap.containsKey(event.getName()))
			return;

		commandMap.get(event.getName()).executeSlashCommand(event);
	}


	private static boolean containsPrefix(String content, String prefix)
	{// TODO: eventually make this check for a mention prefix too
		return content.startsWith(prefix);
	}
}
