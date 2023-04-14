package io.github.jolkert.discordbotlink.extension

import io.github.jolkert.discordbotlink.jda.data.UserData
import net.dv8tion.jda.api.entities.Member
import net.dv8tion.jda.api.entities.User

val User.nameWithDiscriminator: String
	get() = "$name#$discriminator"
val Member.nameWithDiscriminator: String
	get() = user.nameWithDiscriminator

val Member.nicknameOrDefault: String
	get() = nickname ?: user.name

val User.isPerson: Boolean
	get() = !isBot && !isSystem

val UserData.nicknameWithUsername: String
		get() = if (memberData.nickname == "") discordData.username
				else memberData.nickname + " (" + discordData.username + ")"
