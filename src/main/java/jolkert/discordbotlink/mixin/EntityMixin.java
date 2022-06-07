package jolkert.discordbotlink.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public class EntityMixin
{
	@Inject(at = @At("HEAD"), method = "setCustomName")
	public void onCustomNameChange(Text name, CallbackInfo ci)
	{
		if ((Entity)(Object) this instanceof PassiveEntity entity)
			if (name.getString().matches("(?i)brian|ayitspauley"))
				entity.setBaby(true);
	}
}
