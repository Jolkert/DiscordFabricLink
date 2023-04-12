package io.github.jolkert.discordbotlink.jda;

import io.github.jolkert.discordbotlink.DiscordBotLink_LEGACY;
import io.github.jolkert.discordbotlink.data.UserDataHolder;
import io.github.jolkert.discordbotlink.jda.config.BotConfig;
import io.github.jolkert.discordbotlink.jda.listener.CommandHandler;
import io.github.jolkert.discordbotlink.jda.listener.LinkChannelListener;
import io.github.jolkert.discordbotlink.jda.listener.UserUpdateListener;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.dv8tion.jda.api.utils.concurrent.Task;
import net.fabricmc.loader.api.FabricLoader;

import javax.security.auth.login.LoginException;
import java.util.List;

public class DiscordBot extends ListenerAdapter
{
	private JDA jda;
	private final UserDataHolder userData;
	public Guild primaryGuild;
	private final BotConfig config;

	private TextChannel linkChannel = null;

	private static final String FILE_PATH = FabricLoader.getInstance().getConfigDir().toString() + "/DiscordBotLink/user_data.json";
	
	
	public DiscordBot(BotConfig config) throws LoginException
	{
		jda = JDABuilder.createLight(config.token(), GatewayIntent.getIntents(GatewayIntent.ALL_INTENTS))
				.addEventListeners(new CommandHandler(config.prefix()), this, new UserUpdateListener(), new LinkChannelListener())
				.setActivity(Activity.playing("Prefix: " + config.prefix()))
				.setMemberCachePolicy(MemberCachePolicy.ALL)
				.build();

		jda.updateCommands().addCommands(
				Commands.slash("whitelist", "Adds a user to the Minecraft whitelist")
						.addOption(OptionType.STRING, "username", "Your minecraft username. You can only add one Minecraft account per Discord account.")
		).queue();

		userData = new UserDataHolder(FILE_PATH);
		this.config = config;
	}
	
	public UserDataHolder getUserData()
	{
		return userData;
	}
	
	@Override
	public void onReady(ReadyEvent event)
	{
		jda = event.getJDA();
		
		primaryGuild = jda.getGuilds().stream().findFirst().orElseThrow();
		
		Task<List<Member>> _ignored = primaryGuild.loadMembers();
		
		DiscordBotLink_LEGACY.Logger.info("Discord bot ready!");
	}

	public BotConfig getConfig()
	{
		return config;
	}

	public TextChannel getLinkChannel()
	{
		if (linkChannel == null)
			linkChannel = jda.getTextChannelById(config.linkChannelId());

		return linkChannel;
	}

	public JDA getJda()
	{
		return jda;
	}
}
