package gg.moonflower.worldgenprototyping.core.mixin;

import gg.moonflower.worldgenprototyping.core.WorldgenPrototyping;
import net.minecraft.data.worldgen.BiomeDefaultFeatures;
import net.minecraft.data.worldgen.biome.Biomes;
import net.minecraft.world.level.biome.BiomeGenerationSettings;
import net.minecraft.world.level.levelgen.GenerationStep;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BiomeDefaultFeatures.class)
public class MixinBiomeDefaultFeatures {
    @Inject(method = "addDefaultUndergroundVariety", at = @At("HEAD"))
    private static void addExtraUndergroundFeaturesOverworld(BiomeGenerationSettings.Builder builder, CallbackInfo ci){
        //builder.addFeature(GenerationStep.Decoration.RAW_GENERATION, WorldgenPrototyping.PLACED_TEST_BOULDER);
    }
}
