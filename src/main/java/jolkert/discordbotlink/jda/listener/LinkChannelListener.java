package jolkert.discordbotlink.jda.listener;

import jolkert.discordbotlink.DiscordBotLink;
import jolkert.discordbotlink.data.UserData;
import jolkert.discordbotlink.util.NameUtils;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.minecraft.server.MinecraftServer;
import net.minecraft.text.*;

import java.util.Objects;

public class LinkChannelListener extends ListenerAdapter
{
	private static final String COMMAND_PREFIX = "./";

	@Override
	public void onMessageReceived(MessageReceivedEvent event)
	{
		if (event.getAuthor().isBot() || event.getAuthor().isSystem() || !event.getChannel().getId().equals(DiscordBotLink.Bot.getConfig().linkChannelId()))
			return;

		String message = event.getMessage().getContentRaw();
		MinecraftServer server = DiscordBotLink.Server;
		if (message.startsWith(COMMAND_PREFIX) && Objects.requireNonNull(event.getMember()).hasPermission(Permission.MANAGE_SERVER))
		{
			server.sendMessage(Text.of(NameUtils.nameWithDiscriminator(event.getAuthor()) + ": " + message));
			server.getCommandManager().executeWithPrefix(server.getCommandSource(), message.substring(COMMAND_PREFIX.length() - 1));
		}
		else
		{
			UserData author = DiscordBotLink.Bot.getUserData().getUser(event.getAuthor().getId());
			MutableText senderText = MutableText.of(new LiteralTextContent(NameUtils.getNickname(Objects.requireNonNull(event.getMember()))))
					.setStyle(Style.EMPTY
							.withColor(author.getRoleInfo().getTopColor())
							.withHoverEvent(HoverEvent.Action.SHOW_TEXT.buildHoverEvent(Text.of(author.getHoverText()))));

			MutableText finalMessage = MutableText.of(new LiteralTextContent("["))
					.append(SERVER_TEXT)
					.append("/")
					.append(senderText)
					.append("] " + message);

			server.getPlayerManager().broadcast(finalMessage, false);
		}

	}

	private static final MutableText SERVER_TEXT = MutableText.of(new LiteralTextContent("Server")).setStyle(Style.EMPTY.withColor(0xdabbed));
}
