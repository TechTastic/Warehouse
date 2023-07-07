package io.github.techtastic.warehouse.render;

import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.level.block.entity.BlockEntity;

public abstract class BaseBlockEntityRenderer<T extends BlockEntity> implements BlockEntityRenderer<T> {
    public BaseBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
    }
}
