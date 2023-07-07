package io.github.techtastic.warehouse.block.entity;

import io.github.techtastic.warehouse.block.WarehouseBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.AABB;

public class WarehouseControllerBlockEntity extends BlockEntity {
    private AABB aabb = new AABB(
            this.worldPosition.relative(
                    this.getBlockState().getValue(
                            BlockStateProperties.HORIZONTAL_FACING)));

    public WarehouseControllerBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(WarehouseBlockEntities.WAREHOUSE_CONTROLLER_BLOCK_ENTITY.get(), blockPos, blockState);
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

        this.aabb.setMaxX(compoundTag.getDouble("Warehouse$maxX"));
        this.aabb.setMinX(compoundTag.getDouble("Warehouse$minX"));
        this.aabb.setMaxY(compoundTag.getDouble("Warehouse$maxY"));
        this.aabb.setMinY(compoundTag.getDouble("Warehouse$minY"));
        this.aabb.setMaxZ(compoundTag.getDouble("Warehouse$maxZ"));
        this.aabb.setMinZ(compoundTag.getDouble("Warehouse$minZ"));
    }

    public AABB getBoundingBox() {
        return this.aabb;
    }

    public void setBoundingBox(AABB aabb) {
        this.aabb = aabb;
    }
}
