package jolkert.discordbotlink.jda.listener;

import jolkert.discordbotlink.DiscordBotLink;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class ReadyListener extends ListenerAdapter
{
	@Override
	public void onReady(ReadyEvent event)
	{
		DiscordBotLink.Logger.error("WE MADE IT");
	}
}
