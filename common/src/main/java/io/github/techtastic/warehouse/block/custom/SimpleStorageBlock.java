package io.github.techtastic.warehouse.block.custom;

import io.github.techtastic.warehouse.block.entity.SimpleStorageBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class SimpleStorageBlock extends BaseEntityBlock {
    private final int size;

    protected SimpleStorageBlock(Properties properties, int size) {
        super(properties);
        this.size = size;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new SimpleStorageBlockEntity(blockPos, blockState, size);
    }

    @Override
    public RenderShape getRenderShape(BlockState blockState) {
        return RenderShape.MODEL;
    }
}
