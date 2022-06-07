package jolkert.discordbotlink.jda.command;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public abstract class DiscordCommand
{
	protected String name;
	protected String[] aliases = {};
	
	public abstract void execute(String[] args, MessageReceivedEvent context);
	
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
