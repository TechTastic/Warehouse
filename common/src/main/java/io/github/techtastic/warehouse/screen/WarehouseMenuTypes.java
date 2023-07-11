package io.github.techtastic.warehouse.screen;

import dev.architectury.registry.menu.MenuRegistry;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import io.github.techtastic.warehouse.WarehouseMod;
import io.github.techtastic.warehouse.screen.menu.SimpleStorageMenu;
import net.minecraft.core.Registry;
import net.minecraft.world.inventory.MenuType;

public class WarehouseMenuTypes {
    private static final DeferredRegister<MenuType<?>> MENU_TYPES = DeferredRegister.create(WarehouseMod.MOD_ID, Registry.MENU_REGISTRY);

    public static final RegistrySupplier<MenuType<SimpleStorageMenu>> SIMPLE_STORAGE_MENU_TYPE =
            MENU_TYPES.register("simple_storage", () -> MenuRegistry.ofExtended(SimpleStorageMenu::new));

    public static void register() {
        MENU_TYPES.register();
    }
}
