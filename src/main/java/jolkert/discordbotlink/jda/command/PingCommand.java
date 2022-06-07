package jolkert.discordbotlink.jda.command;

import jolkert.discordbotlink.DiscordBotLink;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class PingCommand extends DiscordCommand
{
	public PingCommand()
	{
		this.name = "ping";
	}
	
	@Override
	public void execute(String[] args, MessageReceivedEvent context)
	{
		context.getChannel().sendMessage("Online!").queue();
		DiscordBotLink.Bot.primaryGuild = context.getGuild();
	}
}
