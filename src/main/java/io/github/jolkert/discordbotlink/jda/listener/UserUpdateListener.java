package io.github.jolkert.discordbotlink.jda.listener;

import io.github.jolkert.discordbotlink.DiscordBotLink_LEGACY;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRoleAddEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRoleRemoveEvent;
import net.dv8tion.jda.api.events.guild.member.update.GuildMemberUpdateNicknameEvent;
import net.dv8tion.jda.api.events.user.update.UserUpdateDiscriminatorEvent;
import net.dv8tion.jda.api.events.user.update.UserUpdateNameEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class UserUpdateListener extends ListenerAdapter
{
	@Override
	public void onGuildMemberUpdateNickname(GuildMemberUpdateNicknameEvent event)
	{
		updateRoleInformation(event.getUser());
	}
	
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
		DiscordBotLink_LEGACY.Bot.getUserData().updateDiscordData(event.getUser());
	}
	
	@Override
	public void onUserUpdateDiscriminator(UserUpdateDiscriminatorEvent event)
	{
		updateRoleInformation(event.getUser());
		DiscordBotLink_LEGACY.Bot.getUserData().updateDiscordData(event.getUser());
	}
	
	public void updateRoleInformation(User user)
	{
		DiscordBotLink_LEGACY.Bot.getUserData().updateRoleInfo(user);
	}
}
