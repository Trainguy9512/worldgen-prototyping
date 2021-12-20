package gg.moonflower.worldgenprototyping.core.worldgen;

import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.data.worldgen.BiomeDefaultFeatures;
import net.minecraft.data.worldgen.biome.OverworldBiomes;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.Music;
import net.minecraft.sounds.Musics;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.*;
import org.jetbrains.annotations.Nullable;

public class PrototypeBiomes {

    protected static final int NORMAL_WATER_COLOR = 4159204;
    protected static final int NORMAL_WATER_FOG_COLOR = 329011;

    public static final ResourceKey<Biome> UNDERGROUND = registerKey("underground");

    public static Biome buildBiomeUnderground() {
        MobSpawnSettings.Builder spawnBuilder = new MobSpawnSettings.Builder();
        BiomeDefaultFeatures.commonSpawns(spawnBuilder);

        BiomeGenerationSettings.Builder generationBuilder = new BiomeGenerationSettings.Builder();
        globalOverworldGeneration(generationBuilder);
        BiomeDefaultFeatures.addPlainGrass(generationBuilder);
        BiomeDefaultFeatures.addDefaultOres(generationBuilder);
        BiomeDefaultFeatures.addDefaultSoftDisks(generationBuilder);

        Music music = Musics.createGameMusic(SoundEvents.MUSIC_BIOME_DRIPSTONE_CAVES);

        return biome(Biome.Precipitation.RAIN, Biome.BiomeCategory.UNDERGROUND, 0.5f, 0.5f, NORMAL_WATER_COLOR, NORMAL_WATER_FOG_COLOR, 0xFFF, 0x555, spawnBuilder, generationBuilder, music);
    }

    private static void globalOverworldGeneration(BiomeGenerationSettings.Builder builder) {
        BiomeDefaultFeatures.addDefaultCarversAndLakes(builder);
        BiomeDefaultFeatures.addDefaultCrystalFormations(builder);
        BiomeDefaultFeatures.addDefaultMonsterRoom(builder);
        BiomeDefaultFeatures.addDefaultUndergroundVariety(builder);
        BiomeDefaultFeatures.addDefaultSprings(builder);
        BiomeDefaultFeatures.addSurfaceFreezing(builder);
    }

    private static Biome biome(Biome.Precipitation precipitation, Biome.BiomeCategory biomeCategory, float temperature, float humidity, int normalWaterColor, int normalWaterFogColor, int fogColor, int skyColor, MobSpawnSettings.Builder builder, BiomeGenerationSettings.Builder builder2, @Nullable Music music) {
        return new Biome.BiomeBuilder().precipitation(precipitation).biomeCategory(biomeCategory).temperature(temperature).downfall(humidity).specialEffects(new BiomeSpecialEffects.Builder().waterColor(normalWaterColor).waterFogColor(normalWaterFogColor).fogColor(fogColor).skyColor(skyColor).ambientMoodSound(AmbientMoodSettings.LEGACY_CAVE_SETTINGS).backgroundMusic(music).build()).mobSpawnSettings(builder.build()).generationSettings(builder2.build()).build();
    }

    private static ResourceKey<Biome> registerKey(String string) {
        return ResourceKey.create(Registry.BIOME_REGISTRY, new ResourceLocation(string));
    }
}
