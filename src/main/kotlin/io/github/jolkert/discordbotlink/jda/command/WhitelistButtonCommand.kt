package io.github.jolkert.discordbotlink.jda.command

import io.github.jolkert.discordbotlink.extension.toNullableInt
import io.github.jolkert.discordbotlink.jda.command.WhitelistCommand.Companion.toEmbed
import io.github.jolkert.discordbotlink.jda.data.CommandContext
import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions
import net.dv8tion.jda.api.interactions.commands.OptionType
import net.dv8tion.jda.api.interactions.commands.build.Commands
import net.dv8tion.jda.api.interactions.components.buttons.Button
import net.dv8tion.jda.api.interactions.components.text.TextInput
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle
import net.dv8tion.jda.api.interactions.modals.Modal
import java.awt.Color

class WhitelistButtonCommand : Command()
{
	override val name = "whitelistbutton"
	override val aliases = arrayOf("wlmd")
	override val slashCommandData =
		Commands.slash("whitelistbutton", "Send the whitelist modal")
			.addOption(
				OptionType.STRING,
				"title",
				"The title of the embed",
				true
			)
			.addOption(
				OptionType.STRING,
				"description",
				"The body text of the embed",
				true
			)
			.addOption(
				OptionType.STRING,
				"color",
				"The color attached to the embed"
			)
			.addOption(
				OptionType.STRING,
				"button-label",
				"The label of the whitelist button (default: \"Whitelist\")"
			)
			.addOption(
				OptionType.CHANNEL,
				"channel",
				"The channel to send the message in (same as current channel if left blank)"
			)
			.setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.MANAGE_SERVER))
			.setGuildOnly(true)

	override fun executeTextCommand(event: MessageReceivedEvent, vararg args: String)
	{

	}

	override fun executeSlashCommand(event: SlashCommandInteractionEvent)
	{
		event.deferReply().setEphemeral(true).queue()
		val channel = event.getOption("channel")?.asChannel?.asTextChannel() ?: event.channel
		val color = event.getOption("color")?.asString?.substringAfter('#')?.let {
			if (it.length != 6)
				return@let null

			val colorValue = it.toNullableInt(16)
			return@let if (colorValue != null) Color(colorValue) else null
		} ?: Color.WHITE

		channel.sendMessageEmbeds(EmbedBuilder()
				.setTitle(event.getOption("title")!!.asString)
				.setDescription(event.getOption("description")!!.asString)
				.setColor(color)
				.build())
			.addActionRow(
				Button.success("whitelist-button", event.getOption("button-label")?.asString ?: "Whitelist")
			)
			.queue()

		event.hook.sendMessage("Message sent!").queue()
	}

	companion object
	{
		object ButtonListener : ListenerAdapter()
		{
			override fun onButtonInteraction(event: ButtonInteractionEvent)
			{
				if (event.componentId != "whitelist-button")
					return

				val inputField = TextInput.create("username", "Minecraft Username", TextInputStyle.SHORT)
					.setPlaceholder("Username")
					.setRequiredRange(3, 16)
					.setRequired(true)
					.build()

				val form = Modal.create("whitelist-form", "Whitelist")
					.addActionRow(inputField)
					.build()

				event.replyModal(form).queue()
			}

			override fun onModalInteraction(event: ModalInteractionEvent)
			{
				if (event.modalId != "whitelist-form")
					return

				val result = WhitelistCommand.addToWhitelist(event.getValue("username")!!.asString, CommandContext.of(event))
				event.replyEmbeds(result.toEmbed()).setEphemeral(true).queue()
			}
		}
	}
}