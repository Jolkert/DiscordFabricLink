package io.github.jolkert.discordbotlink.mixin;

import io.github.jolkert.discordbotlink.DiscordBotLink_LEGACY;
import io.github.jolkert.discordbotlink.data.UserData;
import net.minecraft.network.message.MessageType;
import net.minecraft.network.message.SignedMessage;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerManager.class)
public class PlayerManagerMixin
{
	@ModifyVariable(
			method = "broadcast(Lnet/minecraft/network/message/SignedMessage;Ljava/util/function/Predicate;Lnet/minecraft/server/network/ServerPlayerEntity;Lnet/minecraft/network/message/MessageType$Parameters;)V",
			at = @At("HEAD"),
			argsOnly = true,
			ordinal = 0)
	public MessageType.Parameters addHoverTextToSender(MessageType.Parameters params, SignedMessage message)
	{
		UserData data = DiscordBotLink_LEGACY.Bot.getUserData().getUser(message.getSender());
		if (data == null)
			return params;

		Style style = Style.EMPTY.withColor(data.getRoleInfo().getTopColor())
								 .withHoverEvent(HoverEvent.Action.SHOW_TEXT.buildHoverEvent(Text.of(data.getHoverText())))
								 .withClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/msg " + params.name().getString()));
		MutableText styledName = MutableText.of(params.name().getContent()).setStyle(style);
		return new MessageType.Parameters(params.type(), styledName, params.targetName());
	}

	@Inject(
			method = "broadcast(Lnet/minecraft/network/message/SignedMessage;Lnet/minecraft/server/network/ServerPlayerEntity;Lnet/minecraft/network/message/MessageType$Parameters;)V",
			at = @At("HEAD")
	)
	public void broadcastChatToDiscord(SignedMessage message, ServerPlayerEntity sender, MessageType.Parameters params, CallbackInfo ci)
	{
		sendDiscordMessage("<" + sender.getName().getString() + "> " + message.getContent().getString());
	}

	@Inject(
			method = "broadcast(Lnet/minecraft/text/Text;Z)V",
			at = @At("HEAD")
	)
	public void broadcastChatToDiscord(Text message, boolean overlay, CallbackInfo ci)
	{
		if (!message.getString().startsWith("[Server/"))
			sendDiscordMessage("*" + message.getString() + "*");
	}

	private void sendDiscordMessage(String message)
	{
		if (DiscordBotLink_LEGACY.Bot != null && DiscordBotLink_LEGACY.Bot.getLinkChannel() != null)
			DiscordBotLink_LEGACY.Bot.getLinkChannel().sendMessage(message).queue();
	}
}
