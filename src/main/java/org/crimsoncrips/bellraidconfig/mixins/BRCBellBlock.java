package org.crimsoncrips.bellraidconfig.mixins;


import net.minecraft.core.BlockPos;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.level.block.entity.BellBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import org.crimsoncrips.bellraidconfig.BRCConfig;
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
public class BRCBellBlock extends BlockEntity {


    @Shadow private long lastRingTimestamp;

    @Shadow private List<LivingEntity> nearbyEntities;

    public BRCBellBlock(BlockEntityType<?> pType, BlockPos pPos, BlockState pBlockState) {
        super(pType, pPos, pBlockState);
    }

    @Inject(method = "isRaiderWithinRange", at = @At("HEAD"), cancellable = true)
    private static void injected(BlockPos pPos, LivingEntity pRaider, CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(raiderIsWithinRange(pPos,pRaider));
    }

    @Unique
    private static boolean raiderIsWithinRange(BlockPos pos, LivingEntity raider) {
        return raider.isAlive() && !raider.isRemoved() && pos.closerToCenterThan(raider.position(), BRCConfig.giveRange) && raider.getType().is(EntityTypeTags.RAIDERS);

    }

    @Inject(method = "updateEntities", at = @At("HEAD"), cancellable = true)
    private void updateEntities(CallbackInfo ci) {
        ci.cancel();
        BlockPos $$0 = this.getBlockPos();
        if (this.level.getGameTime() > this.lastRingTimestamp + 60L || this.nearbyEntities == null) {
            this.lastRingTimestamp = this.level.getGameTime();
            AABB $$1 = (new AABB($$0)).inflate(BRCConfig.giveRange);
            this.nearbyEntities = this.level.getEntitiesOfClass(LivingEntity.class, $$1);
        }

        if (!this.level.isClientSide) {
            Iterator var4 = this.nearbyEntities.iterator();

            while(var4.hasNext()) {
                LivingEntity $$2 = (LivingEntity)var4.next();
                if ($$2.isAlive() && !$$2.isRemoved() && $$0.closerToCenterThan($$2.position(), BRCConfig.detectionRange)) {
                    $$2.getBrain().setMemory(MemoryModuleType.HEARD_BELL_TIME, this.level.getGameTime());
                }
            }
        }
    }

    @Inject(method = "areRaidersNearby", at = @At("HEAD"), cancellable = true)
    private static void areRaidersNearby(BlockPos pPos, List<LivingEntity> pRaiders, CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(raidersAreNearby(pRaiders, pPos));
    }

    @Unique
    private static boolean raidersAreNearby(List<LivingEntity> pRaiders,BlockPos pPos) {
        Iterator var2 = pRaiders.iterator();

        LivingEntity $$2;
        do {
            if (!var2.hasNext()) {
                return false;
            }

            $$2 = (LivingEntity)var2.next();
        } while(!$$2.isAlive() || $$2.isRemoved() || !pPos.closerToCenterThan($$2.position(), BRCConfig.detectionRange) || !$$2.getType().is(EntityTypeTags.RAIDERS));

        return true;
    }













}
