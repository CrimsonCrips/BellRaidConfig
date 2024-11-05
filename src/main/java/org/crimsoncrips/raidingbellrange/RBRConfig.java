package org.crimsoncrips.raidingbellrange;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;


@Mod.EventBusSubscriber(modid = RaidingBellRange.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class RBRConfig {
    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

    private static final ForgeConfigSpec.IntValue GIVE_RANGE = BUILDER.comment("Range of giving glowness to raiders (blocks)").defineInRange("giveRange", 48, 1, Integer.MAX_VALUE);

    private static final ForgeConfigSpec.IntValue DETECTION_RANGE = BUILDER.comment("Range of raider detection (blocks)").defineInRange("detectionRange", 32, 1, Integer.MAX_VALUE);

    static final ForgeConfigSpec SPEC = BUILDER.build();

    public static int giveRange;
    public static int detectionRange;


    @SubscribeEvent
    static void onLoad(final ModConfigEvent event) {
        giveRange = GIVE_RANGE.get();
        detectionRange = DETECTION_RANGE.get();
    }
}
