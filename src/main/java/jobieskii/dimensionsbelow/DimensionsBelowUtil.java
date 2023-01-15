package jobieskii.dimensionsbelow;

import net.minecraft.block.BlockState;
import net.minecraft.registry.RegistryKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.*;

import static jobieskii.dimensionsbelow.BedrockPortal.TELEPORT_DOWN;

public class DimensionsBelowUtil {

    private class DimensionPositionalConfig {
        @Nullable RegistryKey<World> above;
        @Nullable RegistryKey<World> below;
        int topLayer;
        int bottomLayer;
        DimensionPositionalConfig(int topLayer, int bottomLayer, RegistryKey<World> above, RegistryKey<World> below) {
            this.topLayer = topLayer;
            this.bottomLayer = bottomLayer;
            this.above = above;
            this.below = below;
        }
    }

    public static HashMap<RegistryKey<World>, DimensionPositionalConfig> dimensionConfigs = new HashMap<>();

    @Nullable public static RegistryKey<World> nextDimension(RegistryKey<World> prev, Boolean down) {
        if (down) {
            return dimensionConfigs.get(prev).below;
        } else {
            return dimensionConfigs.get(prev).above;
        }
    }
    @Nullable public static BlockPos getSisterPortalPos(ServerWorld world, BlockPos pos, boolean down) {
        int y;
        if (down) {
            RegistryKey<World> next = dimensionConfigs.get(world.getRegistryKey()).below;
            if (next == null) return null;
            y = dimensionConfigs.get(next).topLayer;

        } else {
            RegistryKey<World> next = dimensionConfigs.get(world.getRegistryKey()).above;
            if (next == null) return null;
            y = dimensionConfigs.get(next).bottomLayer;
        }
        return new BlockPos(pos.getX(), y, pos.getZ());
    }

    public static Boolean shouldTeleportDown(ServerWorld world, BlockPos pos) {
        DimensionPositionalConfig c = dimensionConfigs.get(world.getRegistryKey());
        if (pos.getY() > (c.topLayer + c.bottomLayer) / 2) {
            return true;
        }
        return false;
    }
}
