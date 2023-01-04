package jolkert.discordbotlink.mixin;

import jolkert.discordbotlink.DiscordBotLink;
import jolkert.discordbotlink.data.UserData;
import net.minecraft.client.util.telemetry.SentTelemetryEvent;
import net.minecraft.network.message.MessageType;
import net.minecraft.network.message.SentMessage;
import net.minecraft.network.message.SignedMessage;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.*;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

import java.util.function.Predicate;

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
		UserData data = DiscordBotLink.Bot.getUserData().getUser(message.getSender());
		if (data == null)
			return params;

		Style style = Style.EMPTY.withColor(data.getRoleInfo().getTopColor())
								 .withHoverEvent(HoverEvent.Action.SHOW_TEXT.buildHoverEvent(Text.of(data.getHoverText())))
								 .withClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/msg " + params.name().getString()));
		MutableText styledName = MutableText.of(params.name().getContent()).setStyle(style);
		return new MessageType.Parameters(params.type(), styledName, params.targetName());
	}
}
