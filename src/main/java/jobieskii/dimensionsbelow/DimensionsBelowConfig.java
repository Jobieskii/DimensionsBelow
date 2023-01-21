package jobieskii.dimensionsbelow;


import com.oroarmor.config.ArrayConfigItem;
import com.oroarmor.config.Config;
import com.oroarmor.config.ConfigItem;
import com.oroarmor.config.ConfigItemGroup;
import net.fabricmc.loader.api.FabricLoader;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class DimensionsBelowConfig extends Config {
    public static final ConfigItemGroup mainGroup = new MainGroup();
    public static final List<ConfigItemGroup> configs = List.of(mainGroup);

    public DimensionsBelowConfig() {
        super(configs, new File(FabricLoader.getInstance().getConfigDir().toFile(), Dimensionsbelow.ModID+".json"), Dimensionsbelow.ModID);
    }


    public static class MainGroup extends ConfigItemGroup {
        public static final ArrayConfigItem<String> dimArr = new ArrayConfigItem<String>(
                "list_of_dimensions",
                new String[]{"minecraft:overworld", "deep:deep", "minecraft:the_nether"},
                "Ordered list of dimensions that should be connected by bedrock."
        );
        public MainGroup() {
            super(List.of(dimArr), "dimensions");
        }
    }
}
