package io.github.jolkert.discordbotlink.jda.listener

import io.github.jolkert.discordbotlink.DiscordBotLink.users
import io.github.jolkert.discordbotlink.jda.data.UserData
import net.dv8tion.jda.api.entities.Member
import net.dv8tion.jda.api.entities.User
import net.dv8tion.jda.api.events.guild.member.GuildMemberRoleAddEvent
import net.dv8tion.jda.api.events.guild.member.GuildMemberRoleRemoveEvent
import net.dv8tion.jda.api.events.guild.member.update.GuildMemberUpdateNicknameEvent
import net.dv8tion.jda.api.events.user.update.UserUpdateDiscriminatorEvent
import net.dv8tion.jda.api.events.user.update.UserUpdateNameEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter

object UserUpdateListener : ListenerAdapter()
{
	override fun onGuildMemberUpdateNickname(event: GuildMemberUpdateNicknameEvent) = updateData(event.member)
	override fun onGuildMemberRoleAdd(event: GuildMemberRoleAddEvent) = this.updateData(event.member)
	override fun onGuildMemberRoleRemove(event: GuildMemberRoleRemoveEvent) = this.updateData(event.member)
	override fun onUserUpdateName(event: UserUpdateNameEvent) = updateData(event.user)
	override fun onUserUpdateDiscriminator(event: UserUpdateDiscriminatorEvent) = updateData(event.user)


	private fun updateData(user: User)
	{
		val userData = users[user.id] ?: return
		users[user.id] = UserData(userData, discordData = UserData.DiscordUserData(user.name, user.id))
	}

	private fun updateData(member: Member)
	{
		val userData = users[member.id] ?: return
		users[member.id] = UserData(userData, memberData = UserData.GuildMemberData.of(member))
	}
}