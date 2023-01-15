package jobieskii.dimensionsbelow;

import net.minecraft.registry.RegistryKey;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class DimensionsBelowUtil {


    //TODO: Make it read a config file for an arbitrary number of dimensions (and their heights)
    public static RegistryKey<World> nextDimension(RegistryKey<World> prev, Boolean down) {
        if (prev == World.OVERWORLD && down) return World.NETHER;
        else if (prev == World.NETHER && !down) return World.OVERWORLD;
        else return null;
    }

    //TODO: Make it read a config file for an arbitrary number of dimensions (and their heights)
    public static Boolean shouldTeleportDown(ServerWorld world, BlockPos pos) {
        Boolean res = true;
        if (world.getRegistryKey() == World.OVERWORLD) {
            res = true;
        } else if (world.getRegistryKey() == World.NETHER && pos.getY() > 64) {
            res = false;
        }
        return res;
    }
}
