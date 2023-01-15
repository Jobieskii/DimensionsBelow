package jobieskii.dimensionsbelow;

import eu.pb4.polymer.blocks.api.PolymerTexturedBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import static jobieskii.dimensionsbelow.BedrockPortal.TELEPORT_DOWN;
import static jobieskii.dimensionsbelow.DimensionsBelowUtil.getSisterPortalPos;
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
        // change this block to a portal
        world.setBlockState(
                pos,
                BedrockPortalBlock.getDefaultState().with(TELEPORT_DOWN, isDown)
        );
        // and its sister block on the other side
        BlockPos sister_portal_pos = getSisterPortalPos((ServerWorld) world, pos, isDown);
        world.setBlockState(
                sister_portal_pos,
                BedrockPortalBlock.getDefaultState().with(TELEPORT_DOWN, !isDown)
        );
        // and make some air
        if (isDown) {
            world.breakBlock(sister_portal_pos.down(1), true, player);
            world.breakBlock(sister_portal_pos.down(2), true, player);
        } else {
            world.breakBlock(sister_portal_pos.up(1), true, player);
            world.breakBlock(sister_portal_pos.up(2), true, player);
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
