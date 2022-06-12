package jolkert.discordbotlink.jda.command;

import com.mojang.authlib.GameProfile;
import jolkert.discordbotlink.DiscordBotLink;
import jolkert.discordbotlink.data.UserData;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.minecraft.server.ServerConfigHandler;
import net.minecraft.server.Whitelist;
import net.minecraft.server.WhitelistEntry;

import java.util.UUID;

public class WhitelistCommand extends DiscordCommand
{
	public WhitelistCommand()
	{
		this.name = "whitelist";
		this.aliases = new String[]{"wl"};
	}
	// TODO: maybe make this accept UUIDs? idk
	@Override
	public void execute(String[] args, MessageReceivedEvent context)
	{
		if (args.length < 2 || args[1].isBlank())
			return;
		
		addToWhitelist(args[1], context);
	}
	
	private void addToWhitelist(String username, MessageReceivedEvent context)
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
				UserData oldData = DiscordBotLink.Bot.getUserData().getUser(context.getAuthor());
				if (oldData != null)
				{
					oldData = new UserData(oldData);
					
					UserData user = DiscordBotLink.Bot.getUserData().getUser(context.getAuthor());
					GameProfile toRemove = new GameProfile(oldData.getMinecraftData().getUuid(), oldData.getMinecraftData().getName());
					DiscordBotLink.Bot.getUserData().updateMinecraftData(context.getAuthor(), userProfile);
					whitelist.remove(toRemove);
				}
				else
				{
					UserData userData = new UserData(userProfile, context.getAuthor());
					DiscordBotLink.Bot.getUserData().addUser(userData);
				}
				
				whitelist.add(entry);
				embed = new EmbedBuilder()
						.setTitle("User added to whitelist!")
						.setDescription("User **<" + username	+ ">** added to the whitelist, and linked to " + context.getAuthor().getAsMention() +
								(oldData != null ? "\nReplaced **<" + oldData.getMinecraftData().getName() + ">**" : ""))
						.setColor(0x009900)
						.build();
			}
			
		}
		
		context.getChannel().sendMessageEmbeds(embed).queue();
	}
}
