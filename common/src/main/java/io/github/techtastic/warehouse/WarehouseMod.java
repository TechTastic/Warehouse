package io.github.techtastic.warehouse;

import dev.architectury.registry.client.rendering.BlockEntityRendererRegistry;
import dev.architectury.registry.client.rendering.RenderTypeRegistry;
import dev.architectury.registry.menu.MenuRegistry;
import io.github.techtastic.warehouse.block.WarehouseBlockEntities;
import io.github.techtastic.warehouse.block.WarehouseBlocks;
import io.github.techtastic.warehouse.block.custom.WarehouseControllerBlock;
import io.github.techtastic.warehouse.network.WarehouseNetworking;
import io.github.techtastic.warehouse.render.WarehouseControllerBlockEntityRenderer;
import io.github.techtastic.warehouse.screen.WarehouseMenuTypes;
import io.github.techtastic.warehouse.screen.menu.SimpleStorageScreen;
import net.minecraft.client.renderer.RenderType;

public class WarehouseMod {
    public static final String MOD_ID = "warehouse";
    
    public static void init() {
        WarehouseBlocks.register();
        WarehouseBlockEntities.register();
        WarehouseMenuTypes.register();

        WarehouseNetworking.register();
    }

    public static void initClient() {
        RenderTypeRegistry.register(RenderType.translucent(), WarehouseBlocks.WAREHOUSE_CONTROLLER.get());

        BlockEntityRendererRegistry.register(
                WarehouseBlockEntities.WAREHOUSE_CONTROLLER_BLOCK_ENTITY.get(),
                WarehouseControllerBlockEntityRenderer::new);

        MenuRegistry.registerScreenFactory(WarehouseMenuTypes.SIMPLE_STORAGE_MENU_TYPE.get(), SimpleStorageScreen::new);
    }
}