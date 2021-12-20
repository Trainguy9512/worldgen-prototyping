package gg.moonflower.worldgenprototyping.core.mixin;

import com.mojang.datafixers.util.Pair;
import gg.moonflower.worldgenprototyping.core.worldgen.PrototypeBiomes;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Climate;
import net.minecraft.world.level.biome.OverworldBiomeBuilder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Consumer;

@Mixin(OverworldBiomeBuilder.class)
public class MixinOverworldBiomeBuilder {

    private final Climate.Parameter FULL_RANGE = Climate.Parameter.span(-1.0f, 1.0f);

    @Inject(method = "addUndergroundBiomes", at = @At("HEAD"))
    private void addPrototypeUndergroundBiomes(Consumer<Pair<Climate.ParameterPoint, ResourceKey<Biome>>> consumer, CallbackInfo ci){
        consumer.accept(Pair.of(Climate.parameters(FULL_RANGE, FULL_RANGE, FULL_RANGE, FULL_RANGE, Climate.Parameter.span(0.2f, 1.0f), FULL_RANGE, 0.0F), PrototypeBiomes.UNDERGROUND));
    }
}
