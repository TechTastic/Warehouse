package io.github.techtastic.warehouse.fabric;

import io.github.techtastic.warehouse.WarehouseExpectPlatform;
import net.fabricmc.loader.api.FabricLoader;

import java.nio.file.Path;

public class WarehouseExpectPlatformImpl {
    /**
     * This is our actual method to {@link WarehouseExpectPlatform#getConfigDirectory()}.
     */
    public static Path getConfigDirectory() {
        return FabricLoader.getInstance().getConfigDir();
    }
}
