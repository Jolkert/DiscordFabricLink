package io.github.jolkert.discordbotlink.util;

import io.github.jolkert.discordbotlink.data.UserData;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;

public final class NameUtils
{
	private NameUtils() {}
	
	public static String nameWithDiscriminator(User user)
	{
		return user.getName() + "#" + user.getDiscriminator();
	}
	
	public static String nicknameWithUsername(UserData data)
	{
		if (data.getRoleInfo().getNickname() == null)
			return data.getDiscordData().getName();
		else
			return data.getRoleInfo().getNickname() + " (" + data.getDiscordData().getName() + ")";
	}

	public static String getNickname(Member member)
	{
		if (member.getNickname() != null)
			return member.getNickname();
		else
			return member.getUser().getName();
	}
}
