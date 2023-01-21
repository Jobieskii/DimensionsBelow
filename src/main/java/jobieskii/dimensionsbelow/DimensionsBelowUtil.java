package jobieskii.dimensionsbelow;

import com.oroarmor.config.Config;
import com.oroarmor.config.ConfigItem;
import net.minecraft.block.BlockState;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static jobieskii.dimensionsbelow.BedrockPortal.TELEPORT_DOWN;

public class DimensionsBelowUtil {

    private static class DimensionPositionalConfig {
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
        @Override
        public String toString() {
            return "above: " + (above != null ? above.toString() : "null") +
                    " below: " + (below != null ? below.toString() : "null") +
                    " topLayer: " + topLayer + " bottomLayer: " + bottomLayer;
        }
    }
    public static void loadFromConfig(Config config, Iterable<ServerWorld> worldsIter) {
        ConfigItem<String[]> c = (ConfigItem<String[]>) config.getConfigs().get(0).getConfigs().get(0);

        List<ServerWorld> selected_ordered_worlds = Arrays.stream(c.getValue())
                .map((String value) -> {
                    for (ServerWorld world : worldsIter) {
                        if (world.getRegistryKey().getValue().toString().equals(value)) {
                            return world;
                        }
                    }
                    throw new NoSuchElementException("No matching world in the registry!");
                }).toList();
        for (int i = 0; i < selected_ordered_worlds.size(); i++) {
            ServerWorld world = selected_ordered_worlds.get(i);
            dimensionConfigs.put(
                    world.getRegistryKey(),
                    new DimensionPositionalConfig(
                            world.getBottomY() + world.getLogicalHeight() - 1,
                            world.getBottomY(),
                            i > 0 ? selected_ordered_worlds.get(i-1).getRegistryKey() : null,
                            i < selected_ordered_worlds.size()-1 ? selected_ordered_worlds.get(i+1).getRegistryKey() : null
                    ));
            System.out.println(dimensionConfigs.get(world.getRegistryKey()));
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
        return pos.withY(y);
    }

    public static Boolean shouldTeleportDown(ServerWorld world, BlockPos pos) {
        DimensionPositionalConfig c = dimensionConfigs.get(world.getRegistryKey());
        if (pos.getY() < (c.topLayer + c.bottomLayer) / 2) {
            return true;
        }
        return false;
    }
}
