package io.github.techtastic.warehouse.block;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import io.github.techtastic.warehouse.WarehouseMod;
import io.github.techtastic.warehouse.block.custom.SimpleStorageBlock;
import io.github.techtastic.warehouse.block.custom.WarehouseControllerBlock;
import net.minecraft.core.Registry;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;

public class WarehouseBlocks {
    private static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(WarehouseMod.MOD_ID, Registry.BLOCK_REGISTRY);
    private static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(WarehouseMod.MOD_ID, Registry.ITEM_REGISTRY);

    public static RegistrySupplier<Block> TEST_BLOCK = BLOCKS.register("test_block", () ->
            new Block(BlockBehaviour.Properties.of(Material.AMETHYST)));

    public static RegistrySupplier<Block> SMALL_STORAGE_BLOCK =
            BLOCKS.register("small_storage_block", () ->
                    new SimpleStorageBlock(BlockBehaviour.Properties.copy(Blocks.CHEST), 16));
    public static RegistrySupplier<Block> MEDIUM_STORAGE_BLOCK =
            BLOCKS.register("medium_storage_block", () ->
                    new SimpleStorageBlock(BlockBehaviour.Properties.copy(Blocks.CHEST), 32));
    public static RegistrySupplier<Block> LARGE_STORAGE_BLOCK =
            BLOCKS.register("large_storage_block", () ->
                    new SimpleStorageBlock(BlockBehaviour.Properties.copy(Blocks.CHEST), 64));
    public static RegistrySupplier<Block> WAREHOUSE_CONTROLLER =
            BLOCKS.register("wareohuse_controller", () ->
                    new WarehouseControllerBlock(BlockBehaviour.Properties.copy(Blocks.CHEST)));

    public static void register() {
        BLOCKS.register();

        BLOCKS.forEach(supp -> ITEMS.register(supp.getId(),
                () -> new BlockItem(supp.get(), new Item.Properties())));

        ITEMS.register();
    }
}
