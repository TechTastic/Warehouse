package io.github.techtastic.warehouse.render;

import com.mojang.blaze3d.vertex.PoseStack;
import io.github.techtastic.warehouse.block.entity.WarehouseControllerBlockEntity;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.phys.AABB;

public class WarehouseControllerBlockEntityRenderer extends BaseBlockEntityRenderer<WarehouseControllerBlockEntity> {
    public WarehouseControllerBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void render(WarehouseControllerBlockEntity wh, float f, PoseStack poseStack, MultiBufferSource multiBufferSource, int i, int j) {
        AABB bb = wh.getBoundingBox();

        poseStack.pushPose();
        LevelRenderer.renderLineBox(poseStack, multiBufferSource.getBuffer(RenderType.lines()),
                bb, 255f,  0f, 0f, 0.8F);
        poseStack.popPose();
    }
}
