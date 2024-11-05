package org.crimsoncrips.raidingbellrange.mixins;


import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.GlobalPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.CompassItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BellBlockEntity;
import net.minecraft.world.level.levelgen.Heightmap;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;


@Mixin(BellBlockEntity.class)
public abstract class AMIDimensionalCarver  {

    @Inject(method = "isRaiderWithinRange", at = @At("HEAD"),cancellable = true,remap = false)
    private static void raiderRange(BlockPos p_155197_, LivingEntity p_155198_, CallbackInfoReturnable<Boolean> cir) {
        cir.cancel();
        return p_155198_.isAlive() && !p_155198_.isRemoved() && p_155197_.closerToCenterThan(p_155198_.position(), 48.0) && p_155198_.getType().is(EntityTypeTags.RAIDERS);

    }

    @ModifyE(
            method = "targetMethod",
            at = @At(value = "INVOKE", target = "Ltarget/Class;shouldFly()Z")
    )
    private boolean onlyFlyIfAllowed(boolean original) {
        return original && YourMod.isFlyingAllowed();
    }







}
