package io.github.techtastic.warehouse;

import dev.architectury.registry.client.rendering.BlockEntityRendererRegistry;
import dev.architectury.registry.client.rendering.RenderTypeRegistry;
import io.github.techtastic.warehouse.block.WarehouseBlockEntities;
import io.github.techtastic.warehouse.block.WarehouseBlocks;
import io.github.techtastic.warehouse.block.custom.WarehouseControllerBlock;
import io.github.techtastic.warehouse.network.WarehouseNetworking;
import io.github.techtastic.warehouse.render.WarehouseControllerBlockEntityRenderer;
import net.minecraft.client.renderer.RenderType;

public class WarehouseMod {
    public static final String MOD_ID = "warehouse";
    
    public static void init() {
        WarehouseBlocks.register();
        WarehouseBlockEntities.register();

        WarehouseNetworking.register();
    }

    public static void initClient() {
        RenderTypeRegistry.register(RenderType.translucent(), WarehouseBlocks.WAREHOUSE_CONTROLLER.get());

        BlockEntityRendererRegistry.register(
                WarehouseBlockEntities.WAREHOUSE_CONTROLLER_BLOCK_ENTITY.get(),
                WarehouseControllerBlockEntityRenderer::new);
    }
}
