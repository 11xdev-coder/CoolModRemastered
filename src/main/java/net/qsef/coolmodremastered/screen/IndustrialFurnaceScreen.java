package net.qsef.coolmodremastered.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import net.qsef.coolmodremastered.CoolModRemastered;

import javax.swing.*;

public class IndustrialFurnaceScreen extends AbstractContainerScreen<IndustrialFurnaceMenu> {
    private static final ResourceLocation GUI_TEXTURE = new ResourceLocation(CoolModRemastered.MOD_ID, "textures/gui/industrial_furnace.png");

    public IndustrialFurnaceScreen(IndustrialFurnaceMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
    }

    @Override
    protected void renderBg(GuiGraphics pGuiGraphics, float pPartialTick, int pMouseX, int pMouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
        RenderSystem.setShaderTexture(0, GUI_TEXTURE);

        pGuiGraphics.blit(GUI_TEXTURE, leftPos, topPos, 0, 0, imageWidth, imageHeight);

        // render cook progress
        int progress = Mth.ceil(menu.getCookProgress());
        pGuiGraphics.blit(GUI_TEXTURE, leftPos + 79, topPos + 34, 176, 0, progress, 16);

        // render fuel progress
        if (menu.isBurning()) {
            int totalHeight = 13;
            int burnProgress = Mth.ceil(menu.getBurnProgress());
            int yOffset = totalHeight - burnProgress;

            pGuiGraphics.blit(GUI_TEXTURE, leftPos + 56, topPos + 36 + yOffset, 176, 16 + yOffset,  14, burnProgress);
        }
    }

    @Override
    public void render(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        renderBackground(pGuiGraphics, pMouseX, pMouseY, pPartialTick);

        super.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);

        renderTooltip(pGuiGraphics, pMouseX, pMouseY);
    }
}
