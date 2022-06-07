package jolkert.discordbotlink.jda.listener;

import jolkert.discordbotlink.DiscordBotLink;
import jolkert.discordbotlink.jda.config.RoleInfo;
import jolkert.discordbotlink.jda.config.UserData;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRoleAddEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRoleRemoveEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberUpdateEvent;
import net.dv8tion.jda.api.events.user.update.UserUpdateDiscriminatorEvent;
import net.dv8tion.jda.api.events.user.update.UserUpdateNameEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.lang.reflect.Member;

public class UserUpdateListener extends ListenerAdapter
{
	
	
	@Override
	public void onGuildMemberRoleAdd(GuildMemberRoleAddEvent event)
	{
		updateRoleInformation(event.getUser());
	}
	
	@Override
	public void onGuildMemberRoleRemove(GuildMemberRoleRemoveEvent event)
	{
		updateRoleInformation(event.getUser());
	}
	
	@Override
	public void onUserUpdateName(UserUpdateNameEvent event)
	{
		updateRoleInformation(event.getUser());
		DiscordBotLink.Bot.getUserData().updateDiscordData(event.getUser());
	}
	
	@Override
	public void onUserUpdateDiscriminator(UserUpdateDiscriminatorEvent event)
	{
		updateRoleInformation(event.getUser());
		DiscordBotLink.Bot.getUserData().updateDiscordData(event.getUser());
	}
	
	public void updateRoleInformation(User user)
	{
		DiscordBotLink.Logger.info("Updating user info for " + user.getName() +  "#" + user.getDiscriminator());
		DiscordBotLink.Bot.getUserData().updateRoleInfo(user);
	}
}
