package jolkert.discordbotlink.mixin;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerAbilities;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity
{
	protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world)
	{
		super(entityType, world);
	}

	@Shadow
	protected abstract void dropShoulderEntities();

	@Shadow @Final @Mutable
	private PlayerAbilities abilities;

	@Redirect(method = "tickMovement",
		at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;dropShoulderEntities()V"))
	public void dropShoulder(PlayerEntity instance)
	{
		if (!instance.world.isClient && instance.isTouchingWater() || abilities.flying || instance.isSleeping() || instance.inPowderSnow)
			((PlayerInvoker)instance).invokeDropShoulderEntities();
	}
}

