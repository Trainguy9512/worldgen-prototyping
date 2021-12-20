package gg.moonflower.worldgenprototyping.core.worldgen.feature.configurations;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;

public class BoulderFeatureConfiguration implements FeatureConfiguration {

    public static final Codec<BoulderFeatureConfiguration> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            BlockStateProvider.CODEC.fieldOf("inner_provider").forGetter(boulderFeatureConfiguration -> boulderFeatureConfiguration.rockProvider),
            BlockStateProvider.CODEC.fieldOf("outer_provider").forGetter(boulderFeatureConfiguration -> boulderFeatureConfiguration.mossProvider))
            .apply(instance, BoulderFeatureConfiguration::new));

    public final BlockStateProvider rockProvider;
    public final BlockStateProvider mossProvider;

    public BoulderFeatureConfiguration(BlockStateProvider rockProvider, BlockStateProvider mossProvider){
        this.rockProvider = rockProvider;
        this.mossProvider = mossProvider;
    }
}
