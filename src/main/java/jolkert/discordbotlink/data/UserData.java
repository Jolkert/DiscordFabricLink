package jolkert.discordbotlink.data;

import com.mojang.authlib.GameProfile;
import jolkert.discordbotlink.util.NameUtils;
import net.dv8tion.jda.api.entities.User;

import java.util.UUID;

@SuppressWarnings("FieldMayBeFinal") // anger -jolk 2022-06-12
public class UserData
{
	private MinecraftData minecraftData;
	private DiscordData discordData;
	private RoleInfo roleInfo;
	
	public UserData(GameProfile minecraftProfile, User discordUser)
	{
		this.minecraftData = new MinecraftData(minecraftProfile);
		this.discordData = new DiscordData(discordUser);
		this.roleInfo = RoleInfo.of(discordUser);
	}
	
	public UserData(UserData data)
	{
		MinecraftData minecraftData = new MinecraftData(new UUID(data.minecraftData.uuid.getMostSignificantBits(), data.minecraftData.uuid.getLeastSignificantBits()), data.minecraftData.name);
		DiscordData discordData = new DiscordData(data.discordData.id, data.discordData.name);
		RoleInfo roleInfo = new RoleInfo(data.roleInfo.getTopColor(), data.roleInfo.getPronouns(), data.roleInfo.getNickname(), data.roleInfo.pronounsAreOverridden());
		
		this.minecraftData = minecraftData;
		this.discordData = discordData;
		this.roleInfo = roleInfo;
	}
	
	public MinecraftData getMinecraftData()
	{
		return minecraftData;
	}
	void setMinecraftData(MinecraftData minecraftData)
	{
		this.minecraftData = minecraftData;
	}
	void setMinecraftData(GameProfile profile)
	{
		this.minecraftData = new MinecraftData(profile);
	}
	
	public DiscordData getDiscordData()
	{
		return discordData;
	}
	void setDiscordData(DiscordData discordData)
	{
		this.discordData = discordData;
	}
	void setDiscordData(User discordUser)
	{
		this.discordData = new DiscordData(discordUser);
	}
	
	public RoleInfo getRoleInfo()
	{
		return roleInfo;
	}
	void setRoleInfo(RoleInfo roleInfo)
	{
		this.roleInfo = roleInfo;
	}
	
	public String getHoverText()
	{
		
		return (roleInfo.getPronouns() + "\n" + NameUtils.nicknameWithUsername(this)).trim();
	}
	
	
	// yea intellij. i know i can make those fields final. but the gson ez deserializer doesnt like that -jolk 2022-06-06
	
	public static class MinecraftData
	{
		private UUID uuid;
		private String name;
		
		public MinecraftData(UUID uuid, String name)
		{
			this.uuid = uuid;
			this.name = name;
		}
		
		public MinecraftData(GameProfile profile)
		{
			this(profile.getId(), profile.getName());
		}
		
		public UUID getUuid()
		{
			return uuid;
		}
		
		public String getName()
		{
			return name;
		}
		
	}
	
	public static class DiscordData
	{
		private  String id;
		private String name;
		
		public DiscordData(String id, String name)
		{
			this.id = id;
			this.name = name;
		}
		
		public DiscordData(User user)
		{
			this(user.getId(), NameUtils.nameWithDiscriminator(user));
		}
		
		public String getId()
		{
			return id;
		}
		
		public String getName()
		{
			return name;
		}
	}
}
