package gg.moonflower.worldgenprototyping.core.mixin;

import gg.moonflower.worldgenprototyping.core.WorldgenPrototyping;
import net.minecraft.data.worldgen.BiomeDefaultFeatures;
import net.minecraft.data.worldgen.biome.OverworldBiomes;
import net.minecraft.world.level.biome.BiomeGenerationSettings;
import net.minecraft.world.level.levelgen.GenerationStep;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(OverworldBiomes.class)
public class MixinOverworldBiomes {
    @Redirect(method = "plains", at = @At(value = "INVOKE", target = "Lnet/minecraft/data/worldgen/BiomeDefaultFeatures;addPlainGrass(Lnet/minecraft/world/level/biome/BiomeGenerationSettings$Builder;)V"))
    private static void addPlainsBoulders(BiomeGenerationSettings.Builder builder){
        BiomeDefaultFeatures.addPlainGrass(builder);
        builder.addFeature(GenerationStep.Decoration.RAW_GENERATION, WorldgenPrototyping.PLACED_PLAINS_BOULDER);
    }

    @Redirect(method = "forest", at = @At(value = "INVOKE", target = "Lnet/minecraft/data/worldgen/BiomeDefaultFeatures;addOtherBirchTrees(Lnet/minecraft/world/level/biome/BiomeGenerationSettings$Builder;)V"))
    private static void addForestBoulders(BiomeGenerationSettings.Builder builder){
        BiomeDefaultFeatures.addOtherBirchTrees(builder);
        builder.addFeature(GenerationStep.Decoration.RAW_GENERATION, WorldgenPrototyping.PLACED_TEST_BOULDER);
    }
}
