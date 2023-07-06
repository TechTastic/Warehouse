package io.github.techtastic.warehouse.fabric;

import io.github.techtastic.warehouse.WarehouseMod;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.api.ModInitializer;

public class WarehouseModFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        WarehouseMod.init();
    }
}
