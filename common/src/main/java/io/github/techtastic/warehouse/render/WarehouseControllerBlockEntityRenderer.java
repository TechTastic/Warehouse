package io.github.techtastic.warehouse.render;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.vertex.PoseStack;
import io.github.techtastic.warehouse.block.entity.WarehouseControllerBlockEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.phys.AABB;
import org.lwjgl.opengl.GL11;

public class WarehouseControllerBlockEntityRenderer extends BaseBlockEntityRenderer<WarehouseControllerBlockEntity> {
    public WarehouseControllerBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void render(WarehouseControllerBlockEntity blockEntity, float f, PoseStack poseStack, MultiBufferSource multiBufferSource, int i, int j) {
        drawOutlinedBoundingBox2(blockEntity.getBoundingBox(), 1.f, 1.f, 1.f, 0.0625f);
    }



    /*
     * render a BB as a set of enlarged cuboids.
     */
    public static void drawOutlinedBoundingBox2(AABB bb, float r, float g, float b, float width) {
        GlStateManager._enableBlend();
        GlStateManager._blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GlStateManager._clearColor(r, g, b, 0.4F);
        GlStateManager._bindTexture(0);
        float hw = width / 2;
        drawCuboid((float) bb.minX, (float) bb.minY - hw, (float) bb.minZ - hw, (float) bb.maxX, (float) bb.minY + hw, (float) bb.minZ + hw);
        drawCuboid((float) bb.minX, (float) bb.maxY - hw, (float) bb.minZ - hw, (float) bb.maxX, (float) bb.maxY + hw, (float) bb.minZ + hw);
        drawCuboid((float) bb.minX, (float) bb.minY - hw, (float) bb.maxZ - hw, (float) bb.maxX, (float) bb.minY + hw, (float) bb.maxZ + hw);
        drawCuboid((float) bb.minX, (float) bb.maxY - hw, (float) bb.maxZ - hw, (float) bb.maxX, (float) bb.maxY + hw, (float) bb.maxZ + hw);

        drawCuboid((float) bb.minX - hw, (float) bb.minY, (float) bb.minZ - hw, (float) bb.minX + hw, (float) bb.maxY, (float) bb.minZ + hw);
        drawCuboid((float) bb.maxX - hw, (float) bb.minY, (float) bb.minZ - hw, (float) bb.maxX + hw, (float) bb.maxY, (float) bb.minZ + hw);
        drawCuboid((float) bb.minX - hw, (float) bb.minY, (float) bb.maxZ - hw, (float) bb.minX + hw, (float) bb.maxY, (float) bb.maxZ + hw);
        drawCuboid((float) bb.maxX - hw, (float) bb.minY, (float) bb.maxZ - hw, (float) bb.maxX + hw, (float) bb.maxY, (float) bb.maxZ + hw);

        drawCuboid((float) bb.minX - hw, (float) bb.minY - hw, (float) bb.minZ, (float) bb.minX + hw, (float) bb.minY + hw, (float) bb.maxZ);
        drawCuboid((float) bb.minX - hw, (float) bb.maxY - hw, (float) bb.minZ, (float) bb.minX + hw, (float) bb.maxY + hw, (float) bb.maxZ);
        drawCuboid((float) bb.maxX - hw, (float) bb.minY - hw, (float) bb.minZ, (float) bb.maxX + hw, (float) bb.minY + hw, (float) bb.maxZ);
        drawCuboid((float) bb.maxX - hw, (float) bb.maxY - hw, (float) bb.minZ, (float) bb.maxX + hw, (float) bb.maxY + hw, (float) bb.maxZ);
        GlStateManager._disableBlend();
    }

    private static void drawCuboid(float x, float y, float z, float mx, float my, float mz) {
        GlStateManager.glBegin(GL11.GL_QUADS);
        //z+ side
        GlStateManager.glNormal3f(0, 0, 1);
        GlStateManager.glVertex3f(x, my, mz);
        GlStateManager.glVertex3f(x, y, mz);
        GlStateManager.glVertex3f(mx, y, mz);
        GlStateManager.glVertex3f(mx, my, mz);

        //x+ side
        GlStateManager.glNormal3f(1, 0, 0);
        GlStateManager.glVertex3f(mx, my, mz);
        GlStateManager.glVertex3f(mx, y, mz);
        GlStateManager.glVertex3f(mx, y, z);
        GlStateManager.glVertex3f(mx, my, z);

        //y+ side
        GlStateManager.glNormal3f(0, 1, 0);
        GlStateManager.glVertex3f(x, my, z);
        GlStateManager.glVertex3f(x, my, mz);
        GlStateManager.glVertex3f(mx, my, mz);
        GlStateManager.glVertex3f(mx, my, z);

        //z- side
        GlStateManager.glNormal3f(0, 0, -1);
        GlStateManager.glVertex3f(x, my, z);
        GlStateManager.glVertex3f(mx, my, z);
        GlStateManager.glVertex3f(mx, y, z);
        GlStateManager.glVertex3f(x, y, z);

        //x-side
        GlStateManager.glNormal3f(-1, 0, 0);
        GlStateManager.glVertex3f(x, y, mz);
        GlStateManager.glVertex3f(x, my, mz);
        GlStateManager.glVertex3f(x, my, z);
        GlStateManager.glVertex3f(x, y, z);

        //y- side
        GlStateManager.glNormal3f(0, -1, 0);
        GlStateManager.glVertex3f(x, y, z);
        GlStateManager.glVertex3f(mx, y, z);
        GlStateManager.glVertex3f(mx, y, mz);
        GlStateManager.glVertex3f(x, y, mz);

        GlStateManager.glEnd();
    }
}
