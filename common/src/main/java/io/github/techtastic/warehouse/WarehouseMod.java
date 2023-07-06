package io.github.techtastic.warehouse;

import io.github.techtastic.warehouse.block.WarehouseBlocks;

public class WarehouseMod {
    public static final String MOD_ID = "warehouse";
    
    public static void init() {
        WarehouseBlocks.register();
    }

    public static void initClient() {
    }
}
