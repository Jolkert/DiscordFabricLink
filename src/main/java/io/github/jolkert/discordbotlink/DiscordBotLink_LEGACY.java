package io.github.jolkert.discordbotlink;

import io.github.jolkert.discordbotlink.jda.config.BotConfig;
import io.github.jolkert.discordbotlink.jda.DiscordBot;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.server.MinecraftServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.security.auth.login.LoginException;
import java.io.*;
import java.util.Properties;

public class DiscordBotLink_LEGACY implements ModInitializer
{
	public static MinecraftServer Server;
	public static final Logger Logger = LoggerFactory.getLogger("Discord Bot Link");
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
			properties.setProperty("linkChannelId", "");
			PrintWriter writer = new PrintWriter(file);
			properties.store(writer, "Config for DiscordBotLink");
			writer.close();
			
			config = new BotConfig("", DEFAULT_PREFIX, "");
		}
		else
		{
			InputStream inputStream = new FileInputStream(file);
			Properties properties = new Properties();
			properties.load(inputStream);
			
			String token = properties.getProperty("token", "");
			String prefix = properties.getProperty("prefix", DEFAULT_PREFIX);
			String linkChannelId = properties.getProperty("linkChannelId", "");

			inputStream.close();
			config = new BotConfig(token, prefix, linkChannelId);

			PrintWriter writer = new PrintWriter(file);
			properties.store(writer, "Config for DiscordBotLink");
			writer.close();
		}
		
		return config;
	}
}
