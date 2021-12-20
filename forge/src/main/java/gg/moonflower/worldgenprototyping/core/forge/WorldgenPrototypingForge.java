package gg.moonflower.worldgenprototyping.core.forge;

import gg.moonflower.worldgenprototyping.core.WorldgenPrototyping;
import net.minecraftforge.fml.common.Mod;

@Mod(WorldgenPrototyping.MOD_ID)
public class WorldgenPrototypingForge {
    public WorldgenPrototypingForge() {
        WorldgenPrototyping.PLATFORM.setup();
    }
}
