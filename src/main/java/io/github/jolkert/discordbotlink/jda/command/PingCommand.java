package io.github.jolkert.discordbotlink.jda.command;

import io.github.jolkert.discordbotlink.DiscordBotLink;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;

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

	@Override
	public void executeSlashCommand(@NotNull SlashCommandInteractionEvent event)
	{

	}
}
