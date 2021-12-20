package gg.moonflower.worldgenprototyping.core.worldgen.feature;

import com.mojang.serialization.Codec;
import gg.moonflower.worldgenprototyping.core.util.FastNoiseLite;
import gg.moonflower.worldgenprototyping.core.worldgen.feature.configurations.BoulderFeatureConfiguration;
import net.minecraft.core.BlockPos;
import net.minecraft.core.SectionPos;
import net.minecraft.util.Mth;
import net.minecraft.util.datafix.fixes.CavesAndCliffsRenames;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.biome.MultiNoiseBiomeSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkStatus;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.BlockBlobFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;

import java.util.List;
import java.util.Random;

public class BoulderFeature extends Feature<BoulderFeatureConfiguration> {

    private final FastNoiseLite boulderShapeNoise;
    private final FastNoiseLite boulderinessNoise;

    public BoulderFeature(Codec<BoulderFeatureConfiguration> codec) {
        super(codec);

        this.boulderShapeNoise = new FastNoiseLite();
        this.boulderShapeNoise.SetNoiseType(FastNoiseLite.NoiseType.OpenSimplex2);
        this.boulderShapeNoise.SetFrequency(0.1F);
        this.boulderShapeNoise.SetFractalOctaves(1);
        this.boulderShapeNoise.SetRotationType3D(FastNoiseLite.RotationType3D.ImproveXZPlanes);

        this.boulderinessNoise = new FastNoiseLite();
        this.boulderinessNoise.SetNoiseType(FastNoiseLite.NoiseType.OpenSimplex2);
        this.boulderinessNoise.SetFractalType(FastNoiseLite.FractalType.Ridged);
        this.boulderinessNoise.SetFrequency(0.002F);
        this.boulderinessNoise.SetFractalOctaves(1);
        this.boulderinessNoise.SetRotationType3D(FastNoiseLite.RotationType3D.ImproveXZPlanes);

    }

    @Override
    public boolean place(FeaturePlaceContext<BoulderFeatureConfiguration> featurePlaceContext) {
        BlockState detectedBlockState;
        Random random = featurePlaceContext.random();
        BoulderFeatureConfiguration boulderFeatureConfiguration = featurePlaceContext.config();
        WorldGenLevel worldGenLevel = featurePlaceContext.level();
        BlockPos originBlockPos = featurePlaceContext.origin();

        // Make sure the ground is valid
        while (originBlockPos.getY() > 60 && (worldGenLevel.isEmptyBlock(originBlockPos.below()) || !isDirt(detectedBlockState = worldGenLevel.getBlockState(originBlockPos.below())) && !BlockBlobFeature.isStone(detectedBlockState))) {
            originBlockPos = originBlockPos.below();
        }
        if (originBlockPos.getY() <= 60) {
            return false;
        }

        // Check if it's on a slope or not
        int baseHeight = worldGenLevel.getHeight(Heightmap.Types.WORLD_SURFACE_WG, originBlockPos.getX(), originBlockPos.getZ());
        for(int x = -2; x <= 2; x++){
            for(int z = -2; z <= 2; z++){
                float currentHeight = worldGenLevel.getHeight(Heightmap.Types.WORLD_SURFACE_WG, originBlockPos.offset(x, 0, 0).getX(), originBlockPos.offset(0, 0, z).getZ());
                if(Mth.abs(currentHeight - baseHeight) >= 4){
                    return false;
                }
            }
        }

        /*
        // Random chance of placement based on multinoise erosion
        // Erosion ranges from -1 to 1, with -1 being the rockiest
        int i = QuartPos.fromBlock(originBlockPos.getX());
        int j = QuartPos.fromBlock(originBlockPos.getY());
        int k = QuartPos.fromBlock(originBlockPos.getZ());
        Climate.Sampler climateSampler = featurePlaceContext.chunkGenerator().climateSampler();
        Climate.TargetPoint targetPoint = climateSampler.sample(i, j, k);
        float erosion = Climate.unquantizeCoord(targetPoint.erosion());

        float chanceOfBoulder = Mth.lerp(
                (Mth.clamp(erosion, -1, 0F) * -1),
                0,
                0.5F
        );

        if(random.nextFloat() > chanceOfBoulder){
            return false;
        }
        */

        // Use boulderiness to determine the distribution of surface boulders
        float boulderinessNoiseValue = boulderinessNoise.GetNoise(originBlockPos.getX(), originBlockPos.getZ());
        boulderinessNoiseValue = Mth.clamp(Mth.lerp(boulderinessNoiseValue * 0.5F + 0.5F, -1, 1), 0.15F, 0.8F);
        boulderinessNoiseValue = boulderinessNoiseValue * boulderinessNoiseValue;
        if(boulderinessNoiseValue < random.nextFloat()){
            return false;
        }


        // Randomly offset the rock to add more shape variation
        int radius = random.nextInt(4) == 0 ? 4 : 3;
        if(random.nextBoolean()){
            originBlockPos.below();
        }

        // Cancel if current placement is next to a structure
        List<Block> listOfStructureBlocks = List.of(Blocks.OAK_PLANKS, Blocks.OAK_FENCE, Blocks.SPRUCE_PLANKS, Blocks.CHISELED_SANDSTONE, Blocks.CUT_SANDSTONE, Blocks.DIRT_PATH, Blocks.WHITE_STAINED_GLASS_PANE, Blocks.OAK_STAIRS);
        for(int x = -radius; x <= radius; x++) {
            for (int z = -radius; z <= radius; z++) {
                for (int y = -radius; y <= radius; y++) {
                    if(listOfStructureBlocks.contains(worldGenLevel.getBlockState(originBlockPos.offset(x, y, z)).getBlock())){
                        return false;
                    }
                }
            }
        }

        for(int x = -radius; x <= radius; x++){
            for(int z = -radius; z <= radius; z++){
                for(int y = -radius; y <= radius; y++){
                    BlockPos currentBlockPos = originBlockPos.offset(x, y, z);
                    float distance = distanceBetween(currentBlockPos, originBlockPos);

                    float noiseWarpValue = boulderShapeNoise.GetNoise(-currentBlockPos.getX(), -currentBlockPos.getY(), -currentBlockPos.getZ());
                    float noiseValue = boulderShapeNoise.GetNoise(currentBlockPos.getX() + noiseWarpValue, currentBlockPos.getY() + noiseWarpValue, currentBlockPos.getZ() + noiseWarpValue);
                    distance += ((noiseValue * 0.5F) + 0.5F) * 2;

                    if(distance <= radius){
                        BlockState blockState = boulderFeatureConfiguration.rockProvider.getState(random, currentBlockPos);
                        worldGenLevel.setBlock(currentBlockPos, blockState, Block.UPDATE_CLIENTS);
                    }
                }
                for(int y = radius; y >= -radius; y--){
                    BlockPos currentBlockPos = originBlockPos.offset(x, y, z);
                    BlockState rockBlockState = boulderFeatureConfiguration.rockProvider.getState(random, currentBlockPos);
                    BlockState altBlockState = boulderFeatureConfiguration.mossProvider.getState(random, currentBlockPos);

                    if(worldGenLevel.getBlockState(currentBlockPos) == rockBlockState && worldGenLevel.getBlockState(currentBlockPos.above()).isAir()){
                        int randomDepth = random.nextInt(3);
                        if(randomDepth != 0){
                            for(int y2 = 0; y2 >= -randomDepth; y2--){
                                BlockPos currentDepthBlockPos = currentBlockPos.offset(0, y2, 0);
                                if(worldGenLevel.getBlockState(currentDepthBlockPos) == rockBlockState){
                                    worldGenLevel.setBlock(currentDepthBlockPos, altBlockState, Block.UPDATE_CLIENTS);
                                }
                            }
                        }
                    }
                }
            }
        }

                /*
        for(int y = -verticalRadius; y <= verticalRadius; y++){

            float verticalGradient = y / (float) verticalRadius;
            verticalGradient = 1 - (Mth.abs(verticalGradient));
            verticalGradient = (float) Math.cbrt(verticalGradient);
            BlockPos centerBlockPos = originBlockPos.offset(0, y, 0);

            for(int x = -horizontalRadius; x <= horizontalRadius; x++){
                for(int z = -horizontalRadius; z <= horizontalRadius; z++){
                    BlockPos currentBlockPos = originBlockPos.offset(x, y, z);
                    float distanceToCenter = distanceBetween(centerBlockPos, currentBlockPos);

                    float noiseValue = boulderRandomnessNoise.GetNoise(currentBlockPos.getX(), currentBlockPos.getY(), currentBlockPos.getZ());
                    noiseValue = noiseValue / 2 - 0.5F;

                    if(distanceToCenter <= (horizontalRadius * (verticalGradient - (noiseValue * 0.8F))) + (noiseValue * 1)){
                        BlockState blockState = boulderFeatureConfiguration.outerProvider.getState(random, currentBlockPos);
                        worldGenLevel.setBlock(currentBlockPos, blockState, 2);
                    }
                }
            }
        }

                 */

        return false;
    }

    private float distanceBetween(BlockPos blockPos1, BlockPos blockPos2){
        return
                Mth.sqrt(
                (float) Math.pow(Math.abs(blockPos1.getX() - blockPos2.getX()), 2) +
                (float) Math.pow(Math.abs(blockPos1.getY() - blockPos2.getY()), 2) +
                (float) Math.pow(Math.abs(blockPos1.getZ() - blockPos2.getZ()), 2)
        );
    }
}
