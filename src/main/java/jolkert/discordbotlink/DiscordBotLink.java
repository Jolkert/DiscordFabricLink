package jolkert.discordbotlink;

import com.mojang.logging.LogUtils;
import jolkert.discordbotlink.jda.DiscordBot;
import jolkert.discordbotlink.jda.config.BotConfig;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.server.MinecraftServer;
import org.slf4j.Logger;

import javax.security.auth.login.LoginException;
import java.io.*;
import java.util.Properties;

public class DiscordBotLink implements ModInitializer
{
	public static MinecraftServer Server;
	public static final Logger Logger = LogUtils.getLogger();
	public static DiscordBot Bot;
	
	public static final String DEFAULT_PREFIX = "!";
	
	@Override
	public void onInitialize()
	{
		
		BotConfig botConfig = null;
		try
		{
			botConfig = loadConfig();
		}
		catch (IOException exception)
		{
			Logger.error("COULD NOT LOAD CONFIG FILE!", exception);
		}
		
		
		Bot = null;
		if (botConfig == null)
			Logger.error("COULD NOT LOG IN BOT");
		else
		{
			try
			{
				Bot = new DiscordBot(botConfig);
			}
			catch (LoginException exception)
			{
				Logger.error("COULD NOT LOG IN BOT", exception);
			}
		}
	}
	
	public static BotConfig loadConfig() throws IOException
	{
		String filePath = FabricLoader.getInstance().getConfigDir().toString() + "/discord_bot_link.properties";
		File file = new File(filePath);
		
		BotConfig config;
		
		
		
		if (file.createNewFile())
		{
			Properties properties = new Properties();
			properties.setProperty("token", "");
			properties.setProperty("prefix", DEFAULT_PREFIX);
			PrintWriter writer = new PrintWriter(file);
			properties.store(writer, "Config for DiscordBotLink");
			writer.close();
			
			config = new BotConfig("", DEFAULT_PREFIX);
		}
		else
		{
			InputStream inputStream = new FileInputStream(file);
			Properties properties = new Properties();
			properties.load(inputStream);
			
			String token = properties.getProperty("token", "");
			String prefix = properties.getProperty("prefix", DEFAULT_PREFIX);
			
			inputStream.close();
			config = new BotConfig(token, prefix);
		}
		
		return config;
	}
}
