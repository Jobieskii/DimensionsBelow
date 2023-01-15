package jobieskii.dimensionsbelow;

import eu.pb4.polymer.blocks.api.BlockModelType;
import eu.pb4.polymer.blocks.api.PolymerBlockModel;
import eu.pb4.polymer.blocks.api.PolymerBlockResourceUtils;
import eu.pb4.polymer.core.api.item.PolymerBlockItem;
import eu.pb4.polymer.resourcepack.api.PolymerResourcePackUtils;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.*;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

public class Dimensionsbelow implements ModInitializer {
    public static final String ModID = "dimensionsbelow";
    public static final Identifier CrackedBedrockID = new Identifier("dimensionsbelow", "cracked_bedrock");
    public static final Block CrackedBedrockBlock = new CrackedBedrock(FabricBlockSettings.of(Material.STONE, MapColor.BLACK).requiresTool().strength(45.0f, 1200.0f).dropsNothing());
    public static final BlockItem CrackedBedrockItem = new PolymerBlockItem(CrackedBedrockBlock, new FabricItemSettings(), Items.BEDROCK);

    public static final Identifier BedrockPortalDownID = new Identifier("dimensionsbelow", "bedrock_portal_down");
    public static final Block BedrockPortalDownBlock = new BedrockPortalDown(FabricBlockSettings.of(Material.PORTAL)
            .strength(-1.0F, 3600000.0F)
            .dropsNothing()
            .mapColor(MapColor.BLACK)
    );
    public static final Identifier BedrockPortalUpID = new Identifier("dimensionsbelow", "bedrock_portal_up");
    public static final Block BedrockPortalUpBlock = new BedrockPortalUp(FabricBlockSettings.of(Material.PORTAL)
            .strength(-1.0F, 3600000.0F)
            .dropsNothing()
            .mapColor(MapColor.BLACK)
            .noCollision()
    );

    @Override
    public void onInitialize() {
        Registry.register(Registries.BLOCK, CrackedBedrockID, CrackedBedrockBlock);
        Registry.register(Registries.ITEM, CrackedBedrockID, CrackedBedrockItem);

        Registry.register(Registries.BLOCK, BedrockPortalUpID, BedrockPortalUpBlock);
        Registry.register(Registries.BLOCK, BedrockPortalDownID, BedrockPortalDownBlock);
        {
            PolymerBlockModel pbm = PolymerBlockModel.of(new Identifier(ModID, "block/cracked_bedrock"));
            BlockState bs = PolymerBlockResourceUtils.requestBlock(BlockModelType.FULL_BLOCK, pbm);
            CrackedBedrock.setBs(bs);
        }
        {
            // This block is transparent to enable beacons and stuff
            PolymerBlockModel pbm = PolymerBlockModel.of(new Identifier(ModID, "block/bedrock_portal"));
            BlockState bs = PolymerBlockResourceUtils.requestBlock(BlockModelType.TRANSPARENT_BLOCK, pbm);
            BedrockPortalDown.setBs(bs);
            BedrockPortalUp.setBs(bs);
        }

        PolymerResourcePackUtils.addModAssets("dimensionsbelow");
    }


}
