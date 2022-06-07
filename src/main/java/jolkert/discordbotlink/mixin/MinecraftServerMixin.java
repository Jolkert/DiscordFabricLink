package jolkert.discordbotlink.mixin;

import jolkert.discordbotlink.DiscordBotLink;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.dedicated.DedicatedServer;
import net.minecraft.server.dedicated.MinecraftDedicatedServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MinecraftDedicatedServer.class)
// @Environment(EnvType.SERVER)
public class MinecraftServerMixin
{
	@Inject(method = "setupServer", at = @At("HEAD"))
	protected void onWorldLoad(CallbackInfoReturnable<Boolean> cir)
	{
		DiscordBotLink.Server = (MinecraftServer) (Object) this;
	}
}
