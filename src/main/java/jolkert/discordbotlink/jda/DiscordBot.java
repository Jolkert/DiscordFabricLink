package jolkert.discordbotlink.jda;

import com.mojang.authlib.GameProfile;
import jolkert.discordbotlink.DiscordBotLink;
import jolkert.discordbotlink.jda.config.*;
import jolkert.discordbotlink.jda.listener.CommandHandler;
import jolkert.discordbotlink.jda.listener.UserUpdateListener;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.dv8tion.jda.api.utils.concurrent.Task;
import net.fabricmc.loader.api.FabricLoader;

import javax.security.auth.login.LoginException;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DiscordBot extends ListenerAdapter
{
	private JDA jda;
	private final UserDataHolder userData;
	public Guild primaryGuild;
	
	private static final String FILE_PATH = FabricLoader.getInstance().getConfigDir().toString() + "/DiscordBotLink/user_data.json";
	
	
	public DiscordBot(BotConfig config) throws LoginException, InterruptedException
	{
		jda = JDABuilder.createLight(config.token(), GatewayIntent.getIntents(GatewayIntent.ALL_INTENTS))
				.addEventListeners(new CommandHandler(config.prefix()), this, new UserUpdateListener())
				.setActivity(Activity.playing("Prefix: " + config.prefix()))
				.setMemberCachePolicy(MemberCachePolicy.ALL)
				.build();
		
		userData = new UserDataHolder(FILE_PATH);
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
		
		Task<List<Member>> task = primaryGuild.loadMembers();

		
		DiscordBotLink.Logger.info("Discord bot ready!");
	}
}
