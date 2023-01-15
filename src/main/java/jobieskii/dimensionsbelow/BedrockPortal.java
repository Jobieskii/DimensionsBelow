package jobieskii.dimensionsbelow;

import com.mojang.brigadier.Message;
import eu.pb4.polymer.blocks.api.PolymerTexturedBlock;
import net.fabricmc.fabric.api.dimension.v1.FabricDimensions;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.TeleportTarget;
import net.minecraft.world.World;

import static jobieskii.dimensionsbelow.DimensionsBelowUtil.nextDimension;

public class BedrockPortal extends Block implements PolymerTexturedBlock {
    public static BlockState bs;
    public static final BooleanProperty TELEPORT_DOWN = BooleanProperty.of("teleport_down");
    public BedrockPortal(AbstractBlock.Settings settings) {
        super(settings);
        setDefaultState(getDefaultState().with(TELEPORT_DOWN, true));
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (world instanceof ServerWorld && !player.hasVehicle() && !player.hasPassengers() && player.canUsePortals()) {
            if (player instanceof ServerPlayerEntity) {
                player.sendMessage(Text.literal("Going in!"));
                goThrough(state, (ServerWorld) world, pos, player);
            }
        }
        return super.onUse(state, world, pos, player, hand, hit);
    }
    protected void goThrough(BlockState bs, ServerWorld world, BlockPos pos, Entity entity) {
        boolean down = bs.get(TELEPORT_DOWN);
        BlockPos new_pos = DimensionsBelowUtil.getSisterPortalPos(world, pos, down);
        if (new_pos == null) {
            entity.sendMessage(Text.literal("No more dimensions!"));
            return;
        }
        FabricDimensions.teleport(
                entity,
                world.getServer().getWorld(nextDimension(world.getRegistryKey(), down)),
                new TeleportTarget(new_pos.toCenterPos(), Vec3d.ZERO, 0.f, 0.f)
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
    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(TELEPORT_DOWN);
    }
}
