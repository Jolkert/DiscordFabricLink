package jolkert.discordbotlink.mixin;

import net.minecraft.entity.mob.CreeperEntity;
import net.minecraft.world.GameRules;
import net.minecraft.world.explosion.Explosion;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(CreeperEntity.class)
public class CreeperEntityMixin
{
	@ModifyArg(
			method = "explode",
			at= @At(value = "INVOKE", target = "Lnet/minecraft/world/World;createExplosion(Lnet/minecraft/entity/Entity;DDDFLnet/minecraft/world/explosion/Explosion$DestructionType;)Lnet/minecraft/world/explosion/Explosion;")
	)
	private Explosion.DestructionType explode(Explosion.DestructionType entity)
	{
		CreeperEntity self = (CreeperEntity)(Object) this;
		return self.world.getGameRules().getBoolean(GameRules.DO_MOB_GRIEFING) ? Explosion.DestructionType.BREAK : Explosion.DestructionType.NONE;
	}
}
