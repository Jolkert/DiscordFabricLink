package jolkert.discordbotlink.mixin;

import jolkert.discordbotlink.DiscordBotLink;
import jolkert.discordbotlink.data.UserData;
import net.minecraft.network.message.MessageSender;
import net.minecraft.server.PlayerManager;
import net.minecraft.text.*;
import net.minecraft.util.Formatting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(PlayerManager.class)
public class PlayerManagerMixin
{
	@ModifyArg(
		method = "broadcast(Lnet/minecraft/network/message/SignedMessage;Ljava/util/function/Function;Lnet/minecraft/network/message/MessageSender;Lnet/minecraft/util/registry/RegistryKey;)V",
		at = @At(value = "INVOKE", target = "Lnet/minecraft/server/network/ServerPlayerEntity;sendChatMessage(Lnet/minecraft/network/message/SignedMessage;Lnet/minecraft/network/message/MessageSender;Lnet/minecraft/util/registry/RegistryKey;)V"))
	public MessageSender addHoverTextToSender(MessageSender sender)
	{
		
		UserData data = DiscordBotLink.Bot.getUserData().getUser(sender.uuid());
		
		if (data != null)
		{
			int topColor = data.getRoleInfo().getTopColor();
			
			return new MessageSender(sender.uuid(), MutableText.of(new LiteralTextContent(sender.name().getString())).setStyle(
					Style.EMPTY.withColor(TextColor.fromRgb(Math.min(topColor, 0xffffff)))
							   .withHoverEvent(HoverEvent.Action.SHOW_TEXT.buildHoverEvent(Text.of(data.getHoverText())))
							   .withClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/msg " + sender.name().getString()))));
		}
		else
			return sender;
	}
}
