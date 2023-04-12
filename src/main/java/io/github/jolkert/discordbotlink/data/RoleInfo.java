package io.github.jolkert.discordbotlink.data;

import io.github.jolkert.discordbotlink.DiscordBotLink;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("FieldMayBeFinal")
public class RoleInfo
{
	private static final int DEFAULT_COLOR = 0x1fffffff;
	
	private int topColor;
	private String pronouns;
	private String nickname;
	private boolean pronounsAreOverridden;
	
	RoleInfo(int topColor, String pronouns, String nickname, boolean pronounsAreOverridden)
	{
		this.topColor = topColor;
		this.pronouns = pronouns;
		this.nickname = nickname;
		this.pronounsAreOverridden = pronounsAreOverridden;
	}
	
	
	public static RoleInfo of(User discordUser)
	{
		return of(discordUser.getId(), null);
	}
	public static RoleInfo of(User discordUser, String lockedPronouns)
	{
		return of(discordUser.getId(), lockedPronouns);
	}

	public static RoleInfo of(String discordId, String lockedPronouns)
	{
		Member guildUser = DiscordBotLink.Bot.primaryGuild.getMemberById(discordId);
		if (guildUser == null)
			System.out.println("guildUser is NULL!");
		
		if (guildUser == null)
			return null;
		
		int topColor = DEFAULT_COLOR;
		List<String> pronounRoles = new ArrayList<String>();
		for (Role role : guildUser.getRoles())
		{
			if (topColor == DEFAULT_COLOR && role.getColorRaw() != DEFAULT_COLOR)
				topColor = role.getColorRaw();
			
			if (role.getName().matches("(?i)he/him|she/her|they/them|other"))
				pronounRoles.add(role.getName());
		}

		String pronouns = lockedPronouns;
		if (pronouns == null)
		{
			if (pronounRoles.size() > 1)
				pronounRoles.replaceAll(str -> str.split("/")[0]);

			if (pronounRoles.size() > 0)
				pronouns = String.join("/", pronounRoles);
		}
		String nickname = guildUser.getNickname();
		
		return new RoleInfo(topColor, pronouns, nickname, lockedPronouns != null);
	}
	
	public int getTopColor()
	{
		return topColor;
	}
	
	public String getPronouns()
	{
		return pronouns;
	}
	
	public String getNickname()
	{
		return nickname;
	}

	public boolean pronounsAreOverridden()
	{
		return pronounsAreOverridden;
	}
}
