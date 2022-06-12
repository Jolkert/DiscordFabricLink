package jolkert.discordbotlink.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mojang.authlib.GameProfile;
import jolkert.discordbotlink.DiscordBotLink;
import jolkert.discordbotlink.util.NameUtils;
import net.dv8tion.jda.api.entities.User;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class UserDataHolder
{
	private List<UserData> users;
	private final File dataFile;
	private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
	
	public UserDataHolder(String filePath)
	{
		dataFile = new File(filePath);
		readJson();
	}
	
	// Accessors
	public UserData getUser(String discordId)
	{
		for (UserData data : users)
			if (discordId.equals(data.getDiscordData().getId()))
				return data;
		
		return null;
	}
	public UserData getUser(User user)
	{
		return getUser(user.getId());
	}
	public UserData getUser(UUID uuid)
	{
		for (UserData data : users)
			if (uuid.equals(data.getMinecraftData().getUuid()))
				return data;
		
		return null;
	}
	
	// Modifiers (make sure these all write at the end!)
	public void addUser(UserData data)
	{
		users.add(data);
		writeJson();
	}
	
	public void updateDiscordData(User discordUser)
	{
		UserData data = getUser(discordUser);
		if (data != null)
			data.setDiscordData(discordUser);
		
		writeJson();
	}
	
	public void updateMinecraftData(User discordUser, GameProfile profile)
	{
		updateMinecraftData(discordUser.getId(), profile);
	}
	public void updateMinecraftData(String discordId, GameProfile profile)
	{
		UserData data = getUser(discordId);
		if (data != null)
			data.setMinecraftData(profile);
		writeJson();
	}
	
	public void updateRoleInfo(User discordUser)
	{
		UserData data = getUser(discordUser);
		if (data != null)
		{
			DiscordBotLink.Logger.info("Updating user info for " + NameUtils.nameWithDiscriminator(discordUser));
			data.setRoleInfo(RoleInfo.of(discordUser));
		}
		
		writeJson();
	}
	
	private void writeJson()
	{
		try
		{
			String json = GSON.toJson(users.toArray());
			PrintWriter writer = new PrintWriter(dataFile);
			writer.println(json);
			writer.close();
		}
		catch (FileNotFoundException exception)
		{
			DiscordBotLink.Logger.error("COULD NOT FIND FILE " + dataFile.getPath(), exception);
		}
	}
	
	private void readJson()
	{
		boolean ignore = new File(dataFile.toPath().getParent().toString()).mkdir();
		// TODO: prob do this better lmao
		try
		{
			if (!dataFile.createNewFile())
			{
				FileReader reader = new FileReader(dataFile);
				// this is very dumb lol
				// if you use Arrays.asList(), the resulting ArrayList is fixed-length and doesnt support .add() or .remove()
				// but if you toss that ArrayList into a copy constructor, everything is fine. brilliant design Oracle. you're doing great
				// but also you cant just do .fromJson on an ArrayList because gson doesnt support type arguments and it also doesnt like using raw ArrayLists so -jolk 2022-06-04
				users = new ArrayList<UserData>(Arrays.asList(GSON.fromJson(reader, UserData[].class)));
				reader.close();
			}
			else
			{
				users = new ArrayList<>();
				writeJson();
			}
		}
		catch (IOException exception)
		{
			DiscordBotLink.Logger.error("COULD NOT READ FILE " + dataFile.getPath(), exception);
		}
	}
}
