package io.github.techtastic.warehouse.network.packet;

import dev.architectury.fluid.FluidStack;
import dev.architectury.networking.NetworkManager;
import io.github.techtastic.warehouse.block.entity.WarehouseControllerBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.AABB;

import java.util.function.Supplier;

public class AABBSyncS2CPacket {
    private final AABB aabb;
    private final BlockPos pos;

    public AABBSyncS2CPacket(AABB aabb, BlockPos pos) {
        this.aabb = aabb;
        this.pos = pos;
    }

    public AABBSyncS2CPacket(FriendlyByteBuf buf) {
        this(new AABB(buf.readDouble(), buf.readDouble(), buf.readDouble(), buf.readDouble(), buf.readDouble(), buf.readDouble()), buf.readBlockPos());
    }

    public AABBSyncS2CPacket(FriendlyByteBuf buf, NetworkManager.PacketContext context) {
        this(buf);
        this.apply(() -> context);
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeDouble(aabb.minX);
        buf.writeDouble(aabb.minY);
        buf.writeDouble(aabb.minZ);
        buf.writeDouble(aabb.maxX);
        buf.writeDouble(aabb.maxY);
        buf.writeDouble(aabb.maxZ);
        buf.writeBlockPos(pos);
    }

    public void apply(Supplier<NetworkManager.PacketContext> contextSupplier) {
        contextSupplier.get().queue(() -> {
            assert Minecraft.getInstance().level != null;
            BlockEntity be = Minecraft.getInstance().level.getBlockEntity(pos);
            if (be instanceof WarehouseControllerBlockEntity wh)
                wh.setBoundingBox(this.aabb);
        });
    }
}
