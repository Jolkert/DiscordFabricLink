package jolkert.discordbotlink.mixin;

import jolkert.discordbotlink.DiscordBotLink;
import jolkert.discordbotlink.jda.config.UserData;
import net.minecraft.network.message.MessageSender;
import net.minecraft.server.PlayerManager;
import net.minecraft.text.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(PlayerManager.class)
// @Environment(EnvType.SERVER)
public class PlayerManagerMixin
{
	@ModifyArg(
		method = "broadcast(Lnet/minecraft/network/message/SignedMessage;Ljava/util/function/Function;Lnet/minecraft/network/message/MessageSender;Lnet/minecraft/util/registry/RegistryKey;)V",
		at = @At(value = "INVOKE", target = "Lnet/minecraft/server/network/ServerPlayerEntity;sendChatMessage(Lnet/minecraft/network/message/SignedMessage;Lnet/minecraft/network/message/MessageSender;Lnet/minecraft/util/registry/RegistryKey;)V"))
	public MessageSender sendChatMessage(MessageSender sender)
	{
		
		UserData data = DiscordBotLink.Bot.getUserData().getUser(sender.uuid());
		
		if (data != null)
			return new MessageSender(sender.uuid(), MutableText.of(new LiteralTextContent(sender.name().getString())).setStyle(
					Style.EMPTY.withColor(data.getRoleInfo().getTopColor())
							   .withHoverEvent(HoverEvent.Action.SHOW_TEXT.buildHoverEvent(Text.of(data.getRoleInfo().getPronouns() + " | " + data.getDiscordData().getName())))));
		else
			return sender;
	}
}
