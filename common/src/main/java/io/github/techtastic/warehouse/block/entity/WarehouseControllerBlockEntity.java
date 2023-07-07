package io.github.techtastic.warehouse.block.entity;

import dev.architectury.networking.NetworkManager;
import io.github.techtastic.warehouse.block.WarehouseBlockEntities;
import io.github.techtastic.warehouse.network.WarehouseNetworking;
import io.netty.buffer.Unpooled;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.phys.AABB;

public class WarehouseControllerBlockEntity extends BlockEntity {
    private AABB aabb;

    public WarehouseControllerBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(WarehouseBlockEntities.WAREHOUSE_CONTROLLER_BLOCK_ENTITY.get(), blockPos, blockState);
        this.aabb = getDefaultOrientation(blockState.getValue(BlockStateProperties.HORIZONTAL_FACING).getOpposite());
    }

    @Override
    protected void saveAdditional(CompoundTag compoundTag) {
        compoundTag.putDouble("Warehouse$maxX", this.aabb.maxX);
        compoundTag.putDouble("Warehouse$minX", this.aabb.minX);
        compoundTag.putDouble("Warehouse$maxY", this.aabb.maxY);
        compoundTag.putDouble("Warehouse$minY", this.aabb.minY);
        compoundTag.putDouble("Warehouse$maxZ", this.aabb.maxZ);
        compoundTag.putDouble("Warehouse$minZ", this.aabb.minZ);

        super.saveAdditional(compoundTag);
    }

    @Override
    public void load(CompoundTag compoundTag) {
        super.load(compoundTag);

        this.aabb = new AABB(
                compoundTag.getDouble("Warehouse$minX"),
                compoundTag.getDouble("Warehouse$minY"),
                compoundTag.getDouble("Warehouse$minZ"),
                compoundTag.getDouble("Warehouse$maxX"),
                compoundTag.getDouble("Warehouse$maxY"),
                compoundTag.getDouble("Warehouse$maxZ")
        );
    }

    @Override
    public void setChanged() {
        super.setChanged();

        assert level != null;
        if (level.isClientSide) return;

        FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
        buf.writeDouble(this.aabb.minX);
        buf.writeDouble(this.aabb.minY);
        buf.writeDouble(this.aabb.minZ);
        buf.writeDouble(this.aabb.maxX);
        buf.writeDouble(this.aabb.maxY);
        buf.writeDouble(this.aabb.maxZ);
        buf.writeBlockPos(this.worldPosition);

        NetworkManager.sendToPlayers(level.getServer().getPlayerList().getPlayers(), WarehouseNetworking.AABB_SYNC_S2C_PACKET_ID, buf);
    }

    public AABB getBoundingBox() {
        return this.aabb;
    }

    public void setBoundingBox(AABB aabb) {
        this.aabb = aabb;
        this.setChanged();
    }

    public static AABB moveInDirection(AABB aabb, Direction direction) {
        return switch (direction) {
            case DOWN -> aabb.move(0, -0.5, 0);
            case UP -> aabb.move(0, 0.5, 0);
            case NORTH -> aabb.move(0, 0, -0.5);
            case SOUTH -> aabb.move(0,0,0.5);
            case WEST -> aabb.move(-0.5, 0, 0);
            case EAST -> aabb.move(0.5, 0, 0);
        };
    }

    public static AABB getDefaultOrientation(Direction direction) {
        return switch (direction) {
            case SOUTH -> new AABB(-1, 0, 1, 2, 3, 4);
            case WEST -> new AABB(-3, 0, -1, 0, 3, 2);
            case EAST -> new AABB(1, 0, -1, 4, 3, 2);
            default -> new AABB(-1, 0, -3, 2, 3, 0);
        };
    }
}
