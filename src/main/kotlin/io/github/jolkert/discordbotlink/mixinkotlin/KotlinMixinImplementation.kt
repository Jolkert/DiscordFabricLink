package io.github.jolkert.discordbotlink.mixinkotlin // apparently nothing else is allowed to be in the mixin package or any subpackages. thanks fabric -morgan 2023-04-14

import io.github.jolkert.discordbotlink.DiscordBotLink
import net.minecraft.network.message.MessageType
import net.minecraft.network.message.SignedMessage
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.text.*

object KotlinMixinImplementation
{
	@JvmStatic
	fun `PlayerManager$addHoverTextToSender`(
		messageParameters: MessageType.Parameters,
		message: SignedMessage
	): MessageType.Parameters
	{
		val data = DiscordBotLink.users[message.sender] ?: return messageParameters

		return MessageType.Parameters(
			messageParameters.type,
			MutableText.of(messageParameters.name.content).apply {
				style = Style.EMPTY
					.withColor(data.memberData.topColor)
					.withHoverEvent(HoverEvent.Action.SHOW_TEXT.buildHoverEvent(Text.of(data.hoverText)))
					.withClickEvent(
						ClickEvent(
							ClickEvent.Action.SUGGEST_COMMAND,
							"/msg ${messageParameters.name().string}"
						)
					)
			},
			messageParameters.targetName
		)
	}

	@JvmStatic
	fun `PlayerManager$broadcastChatToDiscord`(message: SignedMessage, sender: ServerPlayerEntity) =
		sendDiscordMessage("<${sender.name.string}> ${message.content.string}")

	@JvmStatic
	fun `PlayerManager$broadcastChatToDiscord`(message: Text)
	{
		if (!message.string.startsWith("["))
			sendDiscordMessage("*${message.string}*")
	}

	private fun sendDiscordMessage(message: String) = DiscordBotLink.Bot.sendMessageInLinkChannel(message)
}