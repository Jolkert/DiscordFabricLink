package jolkert.discordbotlink.mixin;

import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.network.ServerPlayerInteractionManager;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ServerPlayerInteractionManager.class)
public class ServerPlayerInteractionManagerMixin
{
	@Shadow @Final protected ServerPlayerEntity player;

	@Inject(method = "interactBlock", at = @At("RETURN"), cancellable = true)
	public void dropShoulders(ServerPlayerEntity player, World world, ItemStack stack, Hand hand, BlockHitResult hitResult, CallbackInfoReturnable<ActionResult> callbackInfo)
	{
		if (!callbackInfo.getReturnValue().isAccepted()
				&& hasShoulderEntity(player)
				&& player.isSneaking()
				&& player.getMainHandStack().isEmpty())
		{
			((PlayerInvoker) player).invokeDropShoulderEntities();
			callbackInfo.setReturnValue(ActionResult.SUCCESS);
			callbackInfo.cancel();
		}
	}

	private static boolean hasShoulderEntity(ServerPlayerEntity player)
	{
		return !(player.getShoulderEntityLeft().isEmpty() && player.getShoulderEntityRight().isEmpty());
	}
}
