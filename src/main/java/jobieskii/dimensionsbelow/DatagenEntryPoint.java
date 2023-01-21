package jobieskii.dimensionsbelow;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.minecraft.block.Blocks;
import net.minecraft.registry.RegistryBuilder;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.world.gen.YOffset;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.EmeraldOreFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.PlacedFeature;
import net.minecraft.world.gen.placementmodifier.CountPlacementModifier;
import net.minecraft.world.gen.placementmodifier.HeightRangePlacementModifier;
import net.minecraft.world.gen.placementmodifier.SquarePlacementModifier;

import java.util.Arrays;

import static jobieskii.dimensionsbelow.Dimensionsbelow.CrackedBedrockBlock;

public class DatagenEntryPoint implements DataGeneratorEntrypoint {

    private static ConfiguredFeature<?, ?> cracked_feature = new ConfiguredFeature<>(Feature.REPLACE_SINGLE_BLOCK, new EmeraldOreFeatureConfig(
            Blocks.BEDROCK.getDefaultState(),
            CrackedBedrockBlock.getDefaultState()
    ));
    public static PlacedFeature cracked_placed = new PlacedFeature(
            RegistryEntry.of(cracked_feature),
            Arrays.asList(
                    CountPlacementModifier.of(8), // number of veins per chunk
                    SquarePlacementModifier.of(),
                    HeightRangePlacementModifier.uniform(YOffset.getBottom(), YOffset.getBottom())
            )
    );
    @Override
    public void buildRegistry(RegistryBuilder registryBuilder) {
        registryBuilder.addRegistry(
                RegistryKeys.CONFIGURED_FEATURE,

                )
    }

    @Override
    public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {

    }
}
