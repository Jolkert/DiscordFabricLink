package io.github.jolkert.discordbotlink.jda.data

import com.google.gson.GsonBuilder
import java.io.File
import java.io.FileReader
import java.io.PrintWriter
import java.util.*

class UserDataManager(private val jsonFile: File)
{
	private var backingList = ArrayList<UserData>()
	private val gson = GsonBuilder().setPrettyPrinting().create()

	init
	{
		readJson()
	}

	constructor(filepath: String) : this(File(filepath))

	operator fun get(uuid: UUID): UserData?
	{
		for (user in backingList)
			if (user.uuid == uuid)
				return user

		return null
	}
	operator fun get(discordId: String): UserData?
	{
		for (user in backingList)
			if (user.discordId == discordId)
				return user

		return null
	}

	operator fun set(uuid: UUID, userData: UserData)
	{
		var index = -1
		for (i in backingList.indices)
			if (backingList[i].uuid == uuid)
				index = i
		if (index != -1)
			backingList[index] = userData

		writeJson()
	}
	operator fun set(discordId: String, userData: UserData)
	{
		var index = -1
		for (i in backingList.indices)
			if (backingList[i].discordId == discordId)
				index = i
		if (index != -1)
			backingList[index] = userData

		writeJson()
	}

	fun add(userData: UserData)
	{
		backingList.add(userData)
		writeJson()
	}


	private fun readJson()
	{
		File(jsonFile.parent.toString()).mkdirs()
		if (jsonFile.createNewFile())
			writeJson()
		else
		{
			FileReader(jsonFile).also { reader ->
				backingList = ArrayList(gson.fromJson(reader, Array<UserData>::class.java).toList())
			}.close()
		}
	}
	private fun writeJson()
	{
		PrintWriter(jsonFile).also {
			it.println(gson.toJson(backingList.toTypedArray()))
		}.close()
	}
}