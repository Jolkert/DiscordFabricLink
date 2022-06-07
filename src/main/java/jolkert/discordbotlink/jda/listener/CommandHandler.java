package jolkert.discordbotlink.jda.listener;

import jolkert.discordbotlink.DiscordBotLink;
import jolkert.discordbotlink.jda.command.DiscordCommand;
import jolkert.discordbotlink.jda.command.PingCommand;
import jolkert.discordbotlink.jda.command.WhitelistCommand;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.HashMap;

public class CommandHandler extends ListenerAdapter
{
	private final String prefix;
	private final HashMap<String, DiscordCommand> commandMap;
	private static final DiscordCommand[] COMMANDS = {new WhitelistCommand(), new PingCommand()};
	
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
			
			DiscordBotLink.Logger.info(nameWithDiscriminator(event.getAuthor()) + " attempting to run [" + args[0] + "]");
			if (commandMap.containsKey(args[0]))
			{
				DiscordCommand command = commandMap.get(args[0]);
				long start = System.currentTimeMillis();
				command.execute(args, event);
				DiscordBotLink.Logger.info("Command [" + command.getMainAlias() + "] took " + (System.currentTimeMillis() - start) + "ms");
			}
			else
				DiscordBotLink.Logger.info("Could not find command [" + args[0] + "]");
		}
	}
	
	private static boolean containsPrefix(String content, String prefix)
	{// TODO: eventually make this check for a mention prefix too
		return content.startsWith(prefix);
	}
	
	private static String nameWithDiscriminator(User user)
	{
		return user.getName() + "#" + user.getDiscriminator();
	}
}
