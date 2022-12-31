package jolkert.discordbotlink.mixin;

import jolkert.discordbotlink.DiscordBotLink;
import jolkert.discordbotlink.data.UserData;
import net.minecraft.network.message.SignedMessage;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.*;
import org.apache.logging.log4j.core.jmx.Server;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

import java.util.ArrayList;
import java.util.List;

@Mixin(PlayerManager.class)
public class PlayerManagerMixin
{
	@ModifyArg(
			method = "broadcast(Lnet/minecraft/network/message/SignedMessage;Lnet/minecraft/server/network/ServerPlayerEntity;Lnet/minecraft/network/message/MessageType$Parameters;)V",
			at = @At(value = "INVOKE", target = "Lnet/minecraft/server/PlayerManager;broadcast(Lnet/minecraft/network/message/SignedMessage;Ljava/util/function/Predicate;Lnet/minecraft/server/network/ServerPlayerEntity;Lnet/minecraft/network/message/MessageType$Parameters;)V"))
	public SignedMessage addHoverTextToSender(SignedMessage message)
	{
		UserData data = DiscordBotLink.Bot.getUserData().getUser(message.getSender());
		// if (data == null)
		// return message;

		// int topColor = data.getRoleInfo().getTopColor();
		String content = message.getContent().getString();
		String[] isolatedContent = isolateUsername(content);

		MutableText text = MutableText.of(LiteralTextContent.EMPTY);

        System.out.println("{" + String.join(", ", isolatedContent) + "}");
		for (int i = 0; i < isolatedContent.length; i++)
		{
			if (i != 1)
				text.append(isolatedContent[i]);
			else
			{
				Style style = Style.EMPTY.withColor(0xff0000)
						.withHoverEvent(HoverEvent.Action.SHOW_TEXT.buildHoverEvent(Text.of(data.getHoverText())))
						.withClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/msg" + isolatedContent[i]));

				text.append(MutableText.of(new LiteralTextContent(isolatedContent[i])).setStyle(style));
			}
		}

		return message.withUnsignedContent(text);
	}

	private static String[] isolateUsername(String str)
	{
		String[] arr = new String[3];

		arr[0] = str.substring(0, str.indexOf('<'));
		arr[1] = str.substring(str.indexOf('<') + 1, str.indexOf('>'));
		arr[2] = str.substring(str.indexOf('>') + 1);

		return arr;
	}
}
