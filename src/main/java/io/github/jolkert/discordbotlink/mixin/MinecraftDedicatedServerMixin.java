package io.github.jolkert.discordbotlink.mixin;

import io.github.jolkert.discordbotlink.DiscordBotLink_LEGACY;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.dedicated.MinecraftDedicatedServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MinecraftDedicatedServer.class)
public class MinecraftDedicatedServerMixin
{
	@Inject(method = "setupServer", at = @At("HEAD"))
	protected void onWorldLoad(CallbackInfoReturnable<Boolean> cir)
	{
		DiscordBotLink_LEGACY.Server = (MinecraftServer) (Object) this;
	}
}
