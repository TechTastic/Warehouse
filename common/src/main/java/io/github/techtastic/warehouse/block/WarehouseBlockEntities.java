package io.github.techtastic.warehouse.block;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import io.github.techtastic.warehouse.WarehouseMod;
import io.github.techtastic.warehouse.block.entity.SimpleStorageBlockEntity;
import io.github.techtastic.warehouse.block.entity.WarehouseControllerBlockEntity;
import net.minecraft.core.Registry;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class WarehouseBlockEntities {
    private static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(WarehouseMod.MOD_ID, Registry.BLOCK_ENTITY_TYPE_REGISTRY);

    public static final RegistrySupplier<BlockEntityType<SimpleStorageBlockEntity>> SIMPLE_STORAGE_BLOCK_ENTITY =
            BLOCK_ENTITIES.register("simple_storage", () ->
                    BlockEntityType.Builder.of(SimpleStorageBlockEntity::new,
                            WarehouseBlocks.SMALL_STORAGE_BLOCK.get(),
                            WarehouseBlocks.MEDIUM_STORAGE_BLOCK.get(),
                            WarehouseBlocks.LARGE_STORAGE_BLOCK.get())
                            .build(null));

    public static final RegistrySupplier<BlockEntityType<WarehouseControllerBlockEntity>> WAREHOUSE_CONTROLLER_BLOCK_ENTITY =
            BLOCK_ENTITIES.register("warehouse_controller", () ->
                    BlockEntityType.Builder.of(WarehouseControllerBlockEntity::new,
                            WarehouseBlocks.WAREHOUSE_CONTROLLER.get())
                            .build(null));

    public static void register() {
        BLOCK_ENTITIES.register();
    }
}
