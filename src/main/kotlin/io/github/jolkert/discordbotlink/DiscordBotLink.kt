package io.github.jolkert.discordbotlink

import io.github.jolkert.discordbotlink.extension.nameWithDiscriminator
import io.github.jolkert.discordbotlink.extension.pascalToSnake
import io.github.jolkert.discordbotlink.extension.pascalToTitle
import io.github.jolkert.discordbotlink.jda.DiscordBot
import io.github.jolkert.discordbotlink.jda.data.BotConfig
import io.github.jolkert.discordbotlink.jda.data.UserDataManager
import net.dv8tion.jda.api.exceptions.InvalidTokenException
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents
import net.fabricmc.loader.api.FabricLoader
import net.minecraft.server.MinecraftServer
import org.apache.logging.log4j.core.jmx.Server
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.File
import java.io.FileInputStream
import java.io.IOException
import java.io.PrintWriter
import java.util.*

object DiscordBotLink : ModInitializer
{
	@JvmStatic val LOGGER: Logger = LoggerFactory.getLogger(DiscordBotLink::class.simpleName!!.pascalToTitle())
	private val DEFAULT_CONFIG = BotConfig("0", "!", "0")

	lateinit var Bot: DiscordBot private set
	lateinit var Server: MinecraftServer private set

	val users = UserDataManager("${FabricLoader.getInstance().configDir.toString()}/${DiscordBotLink::class.simpleName}/user_data.json")

	override fun onInitialize()
	{
		LOGGER.info("Loading bot config...")
		ServerLifecycleEvents.SERVER_STARTED.register { Server = it }

		val config: BotConfig? = loadConfig()
		if (config != null)
		{
			try
			{
				Bot = DiscordBot(config)
				LOGGER.info("Logged in bot ${Bot.self.nameWithDiscriminator}!")
			} catch (exception: InvalidTokenException)
			{
				LOGGER.error("Provided bot token was not valid!", exception)
			}
		}

		if (!::Bot.isInitialized)
			LOGGER.error("Unable to start Discord bot. This will almost certainly cause a crash later!")


	}
	private fun loadConfig(): BotConfig?
	{
		try
		{
			val file =
				File("${FabricLoader.getInstance().configDir}/${this::class.simpleName!!.pascalToSnake()}.properties")

			if (!file.createNewFile())
				return BotConfig.fromProperties(Properties().apply { FileInputStream(file).also { load(it) }.close() })
			else
			{
				val properties = Properties().apply {
					setProperty(BotConfig::token.name.pascalToSnake(), DEFAULT_CONFIG.token)
					setProperty(BotConfig::commandPrefix.name.pascalToSnake(), DEFAULT_CONFIG.commandPrefix)
					setProperty(BotConfig::linkChannelId.name.pascalToSnake(), DEFAULT_CONFIG.linkChannelId)
				}

				PrintWriter(file).also {
					properties.store(it, "Config for ${this::class.simpleName!!.pascalToTitle()}")
				}.close()

				return DEFAULT_CONFIG
			}
		} catch (exception: IOException)
		{
			LOGGER.error("Could not load bot config file!", exception)
			LOGGER.error("Could not log in bot!")
			return null
		}
	}

	private fun BotConfig.Companion.fromProperties(properties: Properties): BotConfig = properties.let {
		BotConfig(
			it.getProperty(BotConfig::token.name.pascalToSnake(), DEFAULT_CONFIG.token),
			it.getProperty(BotConfig::commandPrefix.name.pascalToSnake(), DEFAULT_CONFIG.commandPrefix),
			it.getProperty(BotConfig::linkChannelId.name.pascalToSnake(), DEFAULT_CONFIG.linkChannelId)
		)
	}
}