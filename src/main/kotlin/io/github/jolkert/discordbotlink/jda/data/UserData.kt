package io.github.jolkert.discordbotlink.jda.data

import com.mojang.authlib.GameProfile
import io.github.jolkert.discordbotlink.extension.nameWithDiscriminator
import io.github.jolkert.discordbotlink.extension.nicknameWithUsername
import net.dv8tion.jda.api.entities.Member
import net.dv8tion.jda.api.entities.User
import java.util.*

data class UserData(
	val minecraftData: MinecraftUserData,
	val discordData: DiscordUserData,
	val memberData: GuildMemberData
)
{
	val uuid
		get() = minecraftData.uuid
	val discordId
		get() = discordData.id

	val hoverText
		get() = "${memberData.pronouns}\n${this.nicknameWithUsername}".trim()

	constructor(minecraftProfile: GameProfile, guildMember: Member, setPronouns: String? = null) : this(
		MinecraftUserData(minecraftProfile.name, minecraftProfile.id),
		DiscordUserData(guildMember.nameWithDiscriminator, guildMember.id),
		GuildMemberData.of(guildMember, setPronouns)
	)

	constructor(
		base: UserData,
		minecraftData: MinecraftUserData? = null,
		discordData: DiscordUserData? = null,
		memberData: GuildMemberData? = null
	) : this(
		minecraftData ?: MinecraftUserData(base.minecraftData),
		discordData ?: DiscordUserData(base.discordData),
		memberData ?: GuildMemberData(base.memberData)
	)

	data class MinecraftUserData(val username: String, val uuid: UUID)
	{
		constructor(
			base: MinecraftUserData,
			username: String? = null,
			uuid: UUID? = null
		) : this(username ?: base.username, uuid ?: base.uuid)

		constructor(gameProfile: GameProfile) : this(gameProfile.name, gameProfile.id)

		fun toGameProfile(): GameProfile = GameProfile(uuid, username)
	}

	data class DiscordUserData(val username: String, val id: String)
	{
		constructor(user: User) : this(user.nameWithDiscriminator, user.id)

		constructor(
			base: DiscordUserData,
			username: String? = null,
			id: String? = null
		) : this(username ?: base.username, id ?: base.id)
	}

	data class GuildMemberData(val topColor: Int, val pronouns: String, val nickname: String)
	{
		constructor(
			base: GuildMemberData,
			topColor: Int? = null,
			pronouns: String? = null,
			nickname: String? = null
		) : this(topColor ?: base.topColor, pronouns ?: base.pronouns, nickname ?: base.nickname)

		companion object
		{
			private const val DefaultTopColor = 0x1fffffff
			private val PronounRegex = "(?i)he/him|she/her|they/them|other".toRegex()

			fun of(member: Member, setPronouns: String? = null): GuildMemberData
			{
				var topColor = DefaultTopColor
				val pronounRoles = ArrayList<String>()
				for (role in member.roles)
				{
					if (topColor == DefaultTopColor && role.colorRaw != DefaultTopColor)
						topColor = role.colorRaw

					if (setPronouns == null && role.name.matches(PronounRegex))
						pronounRoles.add(role.name)
				}


				if (pronounRoles.size > 1)
					pronounRoles.replaceAll { it.split('/')[0] }

				val pronouns =
					setPronouns ?: if (pronounRoles.size > 0) pronounRoles.joinToString(separator = "/") else ""

				val nickname = member.nickname ?: ""
				return GuildMemberData(topColor, pronouns, nickname)
			}
		}
	}
}
