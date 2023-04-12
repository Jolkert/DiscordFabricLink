package io.github.jolkert.discordbotlink.jda.command;

import com.mojang.authlib.GameProfile;
import io.github.jolkert.discordbotlink.data.UserData;
import io.github.jolkert.discordbotlink.DiscordBotLink;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.minecraft.server.ServerConfigHandler;
import net.minecraft.server.Whitelist;
import net.minecraft.server.WhitelistEntry;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.UUID;

public class WhitelistCommand extends DiscordCommand
{
	public static final String NAME = "whitelist";

	public WhitelistCommand()
	{
		this.name = NAME;
		this.aliases = new String[]{"wl"};
	}
	// TODO: maybe make this accept UUIDs? idk
	@Override
	public void execute(String[] args, MessageReceivedEvent event)
	{
		if (args.length < 2 || args[1].isBlank())
			return;

		MessageEmbed embed = addToWhitelist(args[1], CommandContext.of(event));
		event.getChannel().sendMessageEmbeds(embed).queue();
	}
	@Override
	public void executeSlashCommand(@NotNull SlashCommandInteractionEvent event)
	{
		event.deferReply().queue();
		MessageEmbed embed = addToWhitelist(Objects.requireNonNull(event.getOption("username")).getAsString(), CommandContext.of(event));
		event.getHook().sendMessageEmbeds(embed).queue();
	}

	private MessageEmbed addToWhitelist(String username, CommandContext context)
	{
		Whitelist whitelist = DiscordBotLink.Server.getPlayerManager().getWhitelist();
		
		MessageEmbed embed;
		DiscordBotLink.Logger.info("Adding [" + username + "] to the whitelist");
		UUID uuid = ServerConfigHandler.getPlayerUuidByName(DiscordBotLink.Server, username);
		if (uuid == null)
		{
			embed = new EmbedBuilder()
					.setTitle("User not found!")
					.setDescription("User **<" + username + ">** could not be found!")
					.setColor(0xff00000)
					.build();
		}
		else
		{
			GameProfile userProfile = new GameProfile(uuid, username);
			if (whitelist.isAllowed(userProfile))
			{
				embed = new EmbedBuilder().setTitle("Cannot add to whitelist!")
						.setDescription("User **<" + username + ">** is already on the whitelist!")
						.setColor(0xff0000)
						.build();
			}
			else
			{
				WhitelistEntry entry = new WhitelistEntry(userProfile);
				UserData oldData = DiscordBotLink.Bot.getUserData().getUser(context.author());
				if (oldData != null)
				{
					oldData = new UserData(oldData);
					
					UserData user = DiscordBotLink.Bot.getUserData().getUser(context.author());
					GameProfile toRemove = new GameProfile(oldData.getMinecraftData().getUuid(), oldData.getMinecraftData().getName());
					DiscordBotLink.Bot.getUserData().updateMinecraftData(context.author(), userProfile);
					whitelist.remove(toRemove);
				}
				else
				{
					UserData userData = new UserData(userProfile, context.author());
					DiscordBotLink.Bot.getUserData().addUser(userData);
				}
				
				whitelist.add(entry);
				embed = new EmbedBuilder()
						.setTitle("User added to whitelist!")
						.setDescription("User **<" + username	+ ">** added to the whitelist, and linked to " + context.author().getAsMention() +
								(oldData != null ? "\nReplaced **<" + oldData.getMinecraftData().getName() + ">**" : ""))
						.setColor(0x009900)
						.build();
			}
			
		}
		
		return embed;
	}
}
