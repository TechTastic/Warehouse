package io.github.techtastic.warehouse.forge;

import dev.architectury.platform.forge.EventBuses;
import io.github.techtastic.warehouse.WarehouseMod;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(WarehouseMod.MOD_ID)
public class WarehouseModForge {
    public WarehouseModForge() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        // Submit our event bus to let architectury register our content on the right time
        EventBuses.registerModEventBus(WarehouseMod.MOD_ID, bus);
        bus.addListener(this::clientSetup);

        WarehouseMod.init();
    }

    void clientSetup(final FMLClientSetupEvent event) {
        WarehouseMod.initClient();
    }
}
