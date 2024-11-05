package org.crimsoncrips.bellraidconfig;

import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(BellRaidConfig.MODID)
public class BellRaidConfig {

    // Define mod id in a common place for everything to reference
    public static final String MODID = "bellraidconfig";


    public BellRaidConfig() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, BRCConfig.SPEC);
    }

}
