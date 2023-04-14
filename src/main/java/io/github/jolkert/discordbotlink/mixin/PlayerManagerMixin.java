package io.github.jolkert.discordbotlink.mixin;

import io.github.jolkert.discordbotlink.mixinkotlin.KotlinMixinImplementation;
import net.minecraft.network.message.MessageType;
import net.minecraft.network.message.SignedMessage;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
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
			ordinal = 0
	)
	public MessageType.Parameters addHoverTextToSender(MessageType.Parameters params, SignedMessage message)
	{
		return KotlinMixinImplementation.PlayerManager$addHoverTextToSender(params, message);
	}

	@Inject(
			method = "broadcast(Lnet/minecraft/network/message/SignedMessage;Lnet/minecraft/server/network/ServerPlayerEntity;Lnet/minecraft/network/message/MessageType$Parameters;)V",
			at = @At("HEAD")
	)
	public void broadcastChatToDiscord(SignedMessage message, ServerPlayerEntity sender, MessageType.Parameters params, CallbackInfo ci)
	{
		KotlinMixinImplementation.PlayerManager$broadcastChatToDiscord(message, sender);
	}

	@Inject(
			method = "broadcast(Lnet/minecraft/text/Text;Z)V",
			at = @At("HEAD")
	)
	public void broadcastChatToDiscord(Text message, boolean overlay, CallbackInfo ci)
	{
		KotlinMixinImplementation.PlayerManager$broadcastChatToDiscord(message);
	}

}
