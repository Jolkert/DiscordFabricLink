package jolkert.discordbotlink.jda.command;

import jolkert.discordbotlink.DiscordBotLink;
import jolkert.discordbotlink.jda.DiscordBot;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public abstract class DiscordCommand
{
	protected String name;
	protected String[] aliases = {};

	public abstract void execute(String[] args, MessageReceivedEvent context);
	public abstract void executeSlashCommand(@NotNull SlashCommandInteractionEvent event);

	public String[] getAllAliases()
	{
		if (aliases == null || aliases.length == 0)
			return new String[] {name.toLowerCase()};
		
		String[] allAliases = new String[aliases.length + 1];
		allAliases[0] = name.toLowerCase();
		for (int i = 0; i < aliases.length; i++)
			allAliases[i + 1] = aliases[i].toLowerCase();
		
		return allAliases;
	}
	
	public String getMainAlias()
	{
		return name.toLowerCase();
	}
}
