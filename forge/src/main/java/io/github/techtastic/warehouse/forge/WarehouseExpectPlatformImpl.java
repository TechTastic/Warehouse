package io.github.techtastic.warehouse.forge;

import io.github.techtastic.warehouse.WarehouseExpectPlatform;
import net.minecraftforge.fml.loading.FMLPaths;

import java.nio.file.Path;

public class WarehouseExpectPlatformImpl {
    /**
     * This is our actual method to {@link WarehouseExpectPlatform#getConfigDirectory()}.
     */
    public static Path getConfigDirectory() {
        return FMLPaths.CONFIGDIR.get();
    }
}
