package jolkert.discordbotlink.jda.command;

import jolkert.discordbotlink.DiscordBotLink;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.LayoutComponent;
import net.dv8tion.jda.api.interactions.components.buttons.ButtonInteraction;
import net.dv8tion.jda.api.interactions.components.text.TextInput;
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle;
import net.dv8tion.jda.api.interactions.modals.Modal;
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
