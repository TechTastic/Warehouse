package io.github.techtastic.warehouse.screen.menu;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import io.github.techtastic.warehouse.WarehouseMod;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class SimpleStorageScreen extends AbstractContainerScreen<SimpleStorageMenu> {
    private static final ResourceLocation TEXTURE =
            new ResourceLocation(WarehouseMod.MOD_ID, "textures/gui/simple_storage_page_gui.png");

    public SimpleStorageScreen(SimpleStorageMenu abstractContainerMenu, Inventory inventory, Component title) {
        super(abstractContainerMenu, inventory, title);
    }

    @Override
    protected void renderBg(PoseStack poseStack, float partialTick, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, TEXTURE);
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        this.blit(poseStack, x, y, 0, 0, imageWidth, imageHeight);

        if (!menu.isLastPage())
            blit(poseStack, x + 173, y + 41, 176, 200, 24, 27);
        if (!menu.isFirstPage())
            blit(poseStack, x + 173, y + 14, 176, 0, 24, 27);
    }

    @Override
    public void render(PoseStack poseStack, int mouseX, int mouseY, float delta) {
        renderBackground(poseStack);
        super.render(poseStack, mouseX, mouseY, delta);
        renderTooltip(poseStack, mouseX, mouseY);
    }
}