package io.github.techtastic.warehouse.network;

import dev.architectury.networking.NetworkChannel;
import dev.architectury.networking.NetworkManager;
import io.github.techtastic.warehouse.WarehouseMod;
import io.github.techtastic.warehouse.network.packet.AABBSyncS2CPacket;
import net.minecraft.resources.ResourceLocation;

public class WarehouseNetworking {
    public static final NetworkChannel CHANNEL = NetworkChannel.create(new ResourceLocation(WarehouseMod.MOD_ID, "networking_channel"));

    public static final ResourceLocation AABB_SYNC_S2C_PACKET_ID = new ResourceLocation(WarehouseMod.MOD_ID, "aabb_sync_s2c_packet");

    public static void register() {
        NetworkManager.registerReceiver(NetworkManager.Side.S2C, AABB_SYNC_S2C_PACKET_ID,
                AABBSyncS2CPacket::new);
        CHANNEL.register(
                AABBSyncS2CPacket.class,
                AABBSyncS2CPacket::toBytes,
                AABBSyncS2CPacket::new,
                AABBSyncS2CPacket::apply
        );
    }
}
