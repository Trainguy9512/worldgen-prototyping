package gg.moonflower.worldgenprototyping.core.fabric;

import gg.moonflower.worldgenprototyping.core.WorldgenPrototyping;
import net.fabricmc.api.ModInitializer;

public class WorldgenPrototypingFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        WorldgenPrototyping.PLATFORM.setup();

    }
}
