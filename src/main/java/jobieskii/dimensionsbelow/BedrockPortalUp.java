package jobieskii.dimensionsbelow;

import eu.pb4.polymer.blocks.api.PolymerTexturedBlock;
import eu.pb4.polymer.core.api.block.PolymerBlock;
import eu.pb4.polymer.core.api.block.PolymerBlockUtils;
import net.fabricmc.fabric.api.dimension.v1.FabricDimensions;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.TeleportTarget;
import net.minecraft.world.World;

import static jobieskii.dimensionsbelow.DimensionsBelowUtil.nextDimension;

public class BedrockPortalUp extends Block implements PolymerTexturedBlock {
    public static BlockState bs;
    public BedrockPortalUp(AbstractBlock.Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (world instanceof ServerWorld && !player.hasVehicle() && !player.hasPassengers() && player.canUsePortals()) {
            if (player instanceof ServerPlayerEntity) {
                goThrough((ServerWorld) world, pos, player);
            }
        }
        return super.onUse(state, world, pos, player, hand, hit);
    }
    protected void goThrough(ServerWorld world, BlockPos pos, Entity entity) {
        Vec3d v = pos.toCenterPos();
        FabricDimensions.teleport(
                entity,
                world.getServer().getWorld(nextDimension(world.getRegistryKey(), false)),
                new TeleportTarget(new Vec3d(v.getX(), -63.,v.getZ()), Vec3d.ZERO, 0.f, 0.f)
        );
    }

    @Override
    public Block getPolymerBlock(BlockState state) {
        return Blocks.END_GATEWAY;
    }
    @Override
    public BlockState getPolymerBlockState(BlockState state) {
        return Blocks.END_GATEWAY.getDefaultState();
    }
    @Override
    public BlockState getPolymerBlockState(BlockState state, ServerPlayerEntity player) {
        if (player == null) return getPolymerBlockState(state);
        return bs;
    }
    public static void setBs(BlockState nbs) {
        bs = nbs;
    }
}
