package gg.moonflower.worldgenprototyping.core;

import gg.moonflower.pollen.api.platform.Platform;
import gg.moonflower.worldgenprototyping.core.worldgen.PrototypeBiomes;
import gg.moonflower.worldgenprototyping.core.worldgen.feature.BoulderFeature;
import gg.moonflower.worldgenprototyping.core.worldgen.feature.configurations.BoulderFeatureConfiguration;
import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.data.worldgen.features.FeatureUtils;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.placement.BiomeFilter;
import net.minecraft.world.level.levelgen.placement.CountPlacement;
import net.minecraft.world.level.levelgen.placement.InSquarePlacement;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

public class WorldgenPrototyping {

    public static final Feature<BoulderFeatureConfiguration> BOULDER_FEATURE = new BoulderFeature(BoulderFeatureConfiguration.CODEC);

    public static final ConfiguredFeature<BoulderFeatureConfiguration, ?> CONFIGURED_TEST_BOULDER = BOULDER_FEATURE.configured(new BoulderFeatureConfiguration(
            BlockStateProvider.simple(Blocks.COBBLESTONE),
            BlockStateProvider.simple(Blocks.MOSSY_COBBLESTONE)
    ));

    public static final ConfiguredFeature<BoulderFeatureConfiguration, ?> CONFIGURED_PLAINS_BOULDER = BOULDER_FEATURE.configured(new BoulderFeatureConfiguration(
            BlockStateProvider.simple(Blocks.COBBLESTONE),
            BlockStateProvider.simple(Blocks.MOSSY_COBBLESTONE)
    ));

    public static final PlacedFeature PLACED_TEST_BOULDER = CONFIGURED_TEST_BOULDER.placed(CountPlacement.of(1), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, BiomeFilter.biome());
    public static final PlacedFeature PLACED_PLAINS_BOULDER = CONFIGURED_PLAINS_BOULDER.placed(CountPlacement.of(1), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, BiomeFilter.biome());

    public static final String MOD_ID = "worldgenprototyping";
    public static final Platform PLATFORM = Platform.builder(MOD_ID)
            .clientInit(WorldgenPrototyping::onClientInit)
            .clientPostInit(WorldgenPrototyping::onClientPostInit)
            .commonInit(WorldgenPrototyping::onCommonInit)
            .commonPostInit(WorldgenPrototyping::onCommonPostInit)
            .build();

    public static void onClientInit() {
    }

    public static void onClientPostInit(Platform.ModSetupContext ctx) {
    }

    public static void onCommonInit() {
        registerFeatures();
        registerBiomes();
    }

    public static void onCommonPostInit(Platform.ModSetupContext ctx) {
    }

    private static void registerFeatures(){
        Registry.register(Registry.FEATURE, new ResourceLocation(MOD_ID, "boulder"), BOULDER_FEATURE);

        FeatureUtils.register(new ResourceLocation(MOD_ID, "test_boulder").toString(), CONFIGURED_TEST_BOULDER);
        PlacementUtils.register(new ResourceLocation(MOD_ID, "test_boulder").toString(), PLACED_TEST_BOULDER);
        FeatureUtils.register(new ResourceLocation(MOD_ID, "plains_boulder").toString(), CONFIGURED_PLAINS_BOULDER);
        PlacementUtils.register(new ResourceLocation(MOD_ID, "plains_boulder").toString(), PLACED_PLAINS_BOULDER);
    }

    private static void registerBiomes(){
        Registry.register(BuiltinRegistries.BIOME, PrototypeBiomes.UNDERGROUND, PrototypeBiomes.buildBiomeUnderground());
    }
}
