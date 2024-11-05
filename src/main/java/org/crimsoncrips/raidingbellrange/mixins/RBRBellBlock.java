package org.crimsoncrips.raidingbellrange.mixins;


import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.entity.BellBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import org.crimsoncrips.raidingbellrange.RBRConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Iterator;
import java.util.List;


@Mixin(BellBlockEntity.class)
public abstract class RBRBellBlock extends BlockEntity {


    @Shadow private long lastRingTimestamp;

    @Shadow private List<LivingEntity> nearbyEntities;

    public RBRBellBlock(BlockEntityType<?> pType, BlockPos pPos, BlockState pBlockState) {
        super(pType, pPos, pBlockState);
    }

    @Inject(method = "isRaiderWithinRange", at = @At("HEAD"), cancellable = true, remap = false)
    private static void injected(BlockPos pPos, LivingEntity pRaider, CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(bornInConfiguration$newExecution(pPos,pRaider));
    }

    @Unique
    private static boolean bornInConfiguration$newExecution(BlockPos pos, LivingEntity raider) {
        return raider.isAlive() && !raider.isRemoved() && pos.closerToCenterThan(raider.position(), RBRConfig.giveRange) && raider.getType().is(EntityTypeTags.RAIDERS);

    }

    @Inject(method = "updateEntities", at = @At("HEAD"), cancellable = true, remap = false)
    private void updateEntities(CallbackInfo ci) {
        ci.cancel();
        BlockPos $$0 = this.getBlockPos();
        if (this.level.getGameTime() > this.lastRingTimestamp + 60L || this.nearbyEntities == null) {
            this.lastRingTimestamp = this.level.getGameTime();
            AABB $$1 = (new AABB($$0)).inflate(RBRConfig.giveRange);
            this.nearbyEntities = this.level.getEntitiesOfClass(LivingEntity.class, $$1);
        }

        if (!this.level.isClientSide) {
            Iterator var4 = this.nearbyEntities.iterator();

            while(var4.hasNext()) {
                LivingEntity $$2 = (LivingEntity)var4.next();
                if ($$2.isAlive() && !$$2.isRemoved() && $$0.closerToCenterThan($$2.position(), RBRConfig.detectionRange)) {
                    $$2.getBrain().setMemory(MemoryModuleType.HEARD_BELL_TIME, this.level.getGameTime());
                }
            }
        }
    }

    @Inject(method = "areRaidersNearby", at = @At("HEAD"), cancellable = true, remap = false)
    private static void areRaidersNearby(BlockPos pPos, List<LivingEntity> pRaiders, CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(raiderAreNearby(pRaiders, pPos));
    }

    @Unique
    private static boolean raiderAreNearby(List<LivingEntity> pRaiders,BlockPos pPos) {
        Iterator var2 = pRaiders.iterator();

        LivingEntity $$2;
        do {
            if (!var2.hasNext()) {
                return false;
            }

            $$2 = (LivingEntity)var2.next();
        } while(!$$2.isAlive() || $$2.isRemoved() || !pPos.closerToCenterThan($$2.position(), RBRConfig.detectionRange) || !$$2.getType().is(EntityTypeTags.RAIDERS));

        return true;
    }













}