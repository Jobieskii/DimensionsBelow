package jobieskii.dimensionsbelow;

import com.oroarmor.config.ArrayConfigItem;
import com.oroarmor.config.Config;
import eu.pb4.polymer.blocks.api.BlockModelType;
import eu.pb4.polymer.blocks.api.PolymerBlockModel;
import eu.pb4.polymer.blocks.api.PolymerBlockResourceUtils;
import eu.pb4.polymer.core.api.item.PolymerBlockItem;
import eu.pb4.polymer.resourcepack.api.PolymerResourcePackUtils;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.*;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Items;
import net.minecraft.registry.BuiltinRegistries;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.YOffset;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.placementmodifier.CountPlacementModifier;
import net.minecraft.world.gen.placementmodifier.HeightRangePlacementModifier;
import net.minecraft.world.gen.placementmodifier.SquarePlacementModifier;

import java.util.Arrays;
import java.util.Set;

public class Dimensionsbelow implements ModInitializer {
    public static final String ModID = "dimensionsbelow";
    public static final Identifier CrackedBedrockID = new Identifier("dimensionsbelow", "cracked_bedrock");
    public static final Block CrackedBedrockBlock = new CrackedBedrock(FabricBlockSettings.of(Material.STONE, MapColor.BLACK).requiresTool().strength(45.0f, 1200.0f).dropsNothing());
    public static final BlockItem CrackedBedrockItem = new PolymerBlockItem(CrackedBedrockBlock, new FabricItemSettings(), Items.BEDROCK);

    public static final Identifier BedrockPortalID = new Identifier("dimensionsbelow", "bedrock_portal");
    public static final Block BedrockPortalBlock = new BedrockPortal(FabricBlockSettings.of(Material.PORTAL)
            .strength(-1.0F, 3600000.0F)
            .dropsNothing()
            .mapColor(MapColor.BLACK)
            .noCollision()
    );

    //TODO: This config library is not supported, so the bug with arrays probably won't be fixed
    // Need to find a new one...
    public static Config CONFIG = new DimensionsBelowConfig();


    @Override
    public void onInitialize() {
        CONFIG.readConfigFromFile();
        CONFIG.saveConfigToFile();
        ServerLifecycleEvents.SERVER_STARTED.register(
                (handler) -> {
                    DimensionsBelowUtil.loadFromConfig(CONFIG, handler.getWorlds());
                });

        Registry.register(Registries.BLOCK, CrackedBedrockID, CrackedBedrockBlock);
        Registry.register(Registries.ITEM, CrackedBedrockID, CrackedBedrockItem);

        Registry.register(Registries.BLOCK, BedrockPortalID, BedrockPortalBlock);
        {
            PolymerBlockModel pbm = PolymerBlockModel.of(new Identifier(ModID, "block/cracked_bedrock"));
            BlockState bs = PolymerBlockResourceUtils.requestBlock(BlockModelType.FULL_BLOCK, pbm);
            CrackedBedrock.setBs(bs);
        }
        {
            // This block is transparent to enable beacons and stuff
            PolymerBlockModel pbm = PolymerBlockModel.of(new Identifier(ModID, "block/bedrock_portal"));
            BlockState bs = PolymerBlockResourceUtils.requestBlock(BlockModelType.TRANSPARENT_BLOCK, pbm);
            BedrockPortal.setBs(bs);
        }

        PolymerResourcePackUtils.addModAssets("dimensionsbelow");
    }


}
