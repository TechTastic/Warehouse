package io.github.techtastic.warehouse.fabric;

import io.github.techtastic.warehouse.WarehouseMod;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class WarehouseModClientFabric implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        WarehouseMod.initClient();
    }
}
