package jobieskii.dimensionsbelow;

import eu.pb4.polymer.blocks.api.BlockModelType;
import eu.pb4.polymer.blocks.api.PolymerBlockModel;
import eu.pb4.polymer.blocks.api.PolymerBlockResourceUtils;
import eu.pb4.polymer.blocks.api.PolymerTexturedBlock;
import eu.pb4.polymer.core.api.block.PolymerBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import static jobieskii.dimensionsbelow.Dimensionsbelow.*;
import static net.minecraft.block.Blocks.COBBLED_DEEPSLATE;

public class CrackedBedrock extends Block implements PolymerTexturedBlock {
    public static BlockState bs;

    public CrackedBedrock(Settings settings) {
        super(settings);
    }

    @Override
    public void afterBreak(World world, PlayerEntity player, BlockPos pos, BlockState state, @Nullable BlockEntity blockEntity, ItemStack stack) {
        super.afterBreak(world, player, pos, state, blockEntity, stack);
        Boolean isDown = DimensionsBelowUtil.shouldTeleportDown((ServerWorld) world, pos);
        if (isDown) {
            world.setBlockState(pos, BedrockPortalDownBlock.getDefaultState());
        } else {
            world.setBlockState(pos, BedrockPortalUpBlock.getDefaultState());
        }

    }

    @Override
    public Block getPolymerBlock(BlockState state) {
        return COBBLED_DEEPSLATE;
    }

    @Override
    public BlockState getPolymerBlockState(BlockState state) {
        return bs;
    }

    public static void setBs(BlockState nbs) {
        bs = nbs;
    }
}
