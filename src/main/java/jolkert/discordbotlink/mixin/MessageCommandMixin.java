package jolkert.discordbotlink.mixin;

import jolkert.discordbotlink.DiscordBotLink;
import net.minecraft.command.argument.MessageArgumentType;
import net.minecraft.server.command.MessageCommand;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Mixin(MessageCommand.class)
// @Environment(EnvType.SERVER)
public class MessageCommandMixin
{
	@Inject(at = @At("TAIL"), method = "execute")//, locals = LocalCapture.CAPTURE_FAILSOFT)
	private static void logMessageToConsole(ServerCommandSource source, Collection<ServerPlayerEntity> targets, MessageArgumentType.SignedMessage signedMessage, CallbackInfoReturnable<Integer> cir)
	{
		List<ServerPlayerEntity> players = new ArrayList<>(targets);
		System.out.println("TARGETS: " + targets.size() + "\nPLAYERS: " + targets.size());
		
		for (ServerPlayerEntity player : players)
			DiscordBotLink.Logger.info(player.getName().getString());
		
		DiscordBotLink.Logger.info(signedMessage.plain());
	}
}
