package jolkert.discordbotlink.jda.config;

import jolkert.discordbotlink.DiscordBotLink;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;

import java.util.ArrayList;
import java.util.List;

public class RoleInfo
{
	private int topColor;
	private String pronouns;
	
	RoleInfo(int topColor, String pronouns)
	{
		this.topColor = topColor;
		this.pronouns = pronouns;
	}
	
	
	public static RoleInfo of(User discordUser)
	{
		return of(discordUser.getId());
	}
	
	public static RoleInfo of(String discordId)
	{
		Member guildUser = DiscordBotLink.Bot.primaryGuild.getMemberById(discordId);
		if (guildUser == null)
			System.out.println("guildUser is NULL!");
		
		if (guildUser == null)
			return null;
		
		int topColor = 0x99aab5;
		List<String> pronounRoles = new ArrayList<String>();
		for (Role role : guildUser.getRoles())
		{
			if (topColor == 0x99aab5 && role.getColorRaw() != 0x99aab5)
				topColor = role.getColorRaw();
			
			if (role.getName().matches("(?i)he/him|she/her|they/them|other"))
				pronounRoles.add(role.getName());
		}
		
		String pronouns = "";
		if (pronounRoles.size() > 1)
			pronounRoles.replaceAll(str -> str.split("/")[0]);
		
		if (pronounRoles.size() > 0)
			pronouns = String.join("/", pronounRoles);
		
		return new RoleInfo(topColor, pronouns);
	}
	
	public int getTopColor()
	{
		return topColor;
	}
	
	public String getPronouns()
	{
		return pronouns;
	}
}
