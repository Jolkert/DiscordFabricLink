package jolkert.discordbotlink.mixin;

import jolkert.discordbotlink.DiscordBotLink;
import jolkert.discordbotlink.data.UserData;
import net.minecraft.network.message.MessageType;
import net.minecraft.network.message.SentMessage;
import net.minecraft.server.PlayerManager;
import net.minecraft.text.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(PlayerManager.class)
public class PlayerManagerMixin
{
	@ModifyArg(
			method = "broadcast(Lnet/minecraft/network/message/SignedMessage;Ljava/util/function/Predicate;Lnet/minecraft/server/network/ServerPlayerEntity;Lnet/minecraft/network/message/MessageType$Parameters;)V",
			at = @At(value = "INVOKE", target = "Lnet/minecraft/server/network/ServerPlayerEntity;sendChatMessage(Lnet/minecraft/network/message/SentMessage;ZLnet/minecraft/network/message/MessageType$Parameters;)V"),
			index = 2)
	public MessageType.Parameters addHoverTextToSender(SentMessage message, boolean bool, MessageType.Parameters params)
	{
		if (!(message instanceof SentMessage.Chat chatMessage))
			return params;

		UserData data = DiscordBotLink.Bot.getUserData().getUser(chatMessage.message().getSender());

		if (data == null)
			return params;

		Style style = Style.EMPTY.withColor(data.getRoleInfo().getTopColor())
				.withHoverEvent(HoverEvent.Action.SHOW_TEXT.buildHoverEvent(Text.of(data.getHoverText())))
				.withClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/msg " + params.name().getString()));

		MutableText styledName = MutableText.of(params.name().getContent()).setStyle(style);
		return new MessageType.Parameters(params.type(), styledName, params.targetName());
	}
}
