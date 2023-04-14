package io.github.jolkert.discordbotlink.jda.command

import com.mojang.authlib.GameProfile
import io.github.jolkert.discordbotlink.DiscordBotLink
import io.github.jolkert.discordbotlink.DiscordBotLink.LOGGER
import io.github.jolkert.discordbotlink.jda.data.CommandContext
import io.github.jolkert.discordbotlink.jda.data.UserData
import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.entities.MessageEmbed
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import net.dv8tion.jda.api.interactions.commands.OptionType
import net.dv8tion.jda.api.interactions.commands.build.Commands
import net.minecraft.server.ServerConfigHandler
import net.minecraft.server.WhitelistEntry

class WhitelistCommand : Command()
{
	override val name = "whitelist"
	override val aliases = arrayOf("wl")
	override val slashCommandData =
		Commands.slash("whitelist", "Add a Minecraft user to the whitelist")
			.addOption(
				OptionType.STRING,
				"username",
				"Your minecraft username. You can only add one Minecraft account per Discord account.",
				true
			)
			.setGuildOnly(true)

	override fun executeTextCommand(event: MessageReceivedEvent, vararg args: String)
	{
		if (args.size < 2 || args[1].isBlank())
			return

		val embed = addToWhitelist(args[1], CommandContext.of(event)).toEmbed()
		event.channel.sendMessageEmbeds(embed).queue()
	}

	override fun executeSlashCommand(event: SlashCommandInteractionEvent)
	{
		val user = event.getOption("username")!!.asString

		event.deferReply().queue()
		event.hook.sendMessageEmbeds(addToWhitelist(user, CommandContext.of(event)).toEmbed()).queue()
	}

	private fun addToWhitelist(username: String, context: CommandContext): WhitelistAddResult
	{
		val whitelist = DiscordBotLink.Server.playerManager.whitelist

		LOGGER.info("Attempting to add <$username> to the whitelist")
		val uuid = ServerConfigHandler.getPlayerUuidByName(DiscordBotLink.Server, username)
			?: return WhitelistAddResult.USER_NOT_FOUND

		val userProfile = GameProfile(uuid, username)
		if (whitelist.isAllowed(userProfile))
			return WhitelistAddResult.USER_ALREADY_WHITELISTED

		val oldData = DiscordBotLink.users[context.author.id]
		if (oldData == null)
			DiscordBotLink.users.add(UserData(userProfile, context.authorAsMember!!))
		else
		{
			whitelist.remove(oldData.minecraftData.toGameProfile())
			DiscordBotLink.users[oldData.uuid] =
				UserData(oldData, minecraftData = UserData.MinecraftUserData(userProfile))
		}

		whitelist.add(WhitelistEntry(userProfile))
		return WhitelistAddResult.SUCCESS
	}

	private fun WhitelistAddResult.toEmbed(): MessageEmbed
	{
		return EmbedBuilder().setTitle("This is a test").setDescription("Testing but in kotlin!").build()
	}

	private enum class WhitelistAddResult
	{
		SUCCESS,
		USER_ALREADY_WHITELISTED,
		USER_NOT_FOUND
	}
}