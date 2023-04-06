package jolkert.discordbotlink.jda.command;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public record CommandContext(Guild guild,
							 MessageChannelUnion channel,
							 User author,
							 Message message)
{
	public static CommandContext of(MessageReceivedEvent event)
	{
		return new CommandContext(event.getGuild(), event.getChannel(), event.getAuthor(), event.getMessage());
	}
	public static CommandContext of(SlashCommandInteractionEvent event)
	{
		return new CommandContext(event.getGuild(), event.getChannel(), event.getUser(), null);
	}
}
