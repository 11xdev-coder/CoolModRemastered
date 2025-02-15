package net.qsef.coolmodremastered.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.qsef.coolmodremastered.CoolModRemastered;
import net.qsef.coolmodremastered.entity.custom.RocketEntity;

public class RocketRenderer extends EntityRenderer<RocketEntity> {
    private final RocketModel<RocketEntity> model;

    public RocketRenderer(EntityRendererProvider.Context context) {
        super(context);
        // bake the model
        this.model = new RocketModel<>(context.bakeLayer(ModModelLayers.ROCKET_LAYER));
    }

    @Override
    public ResourceLocation getTextureLocation(RocketEntity rocketEntity) {
        return new ResourceLocation(CoolModRemastered.MOD_ID, "textures/entity/rocket.png");
    }

    @Override
    public void render(RocketEntity rocket, float entityYaw, float partialTicks, PoseStack poseStack,
                       MultiBufferSource bufferSource, int packedLight) {
        poseStack.pushPose();

        // Rotate so the rocket faces its travel direction.
        // Adjust the yaw by -90 degrees if your model's default orientation is different.
        poseStack.mulPose(new org.joml.Quaternionf().rotateY((rocket.getYRot() - 90) * ((float)Math.PI / 180F)));
        poseStack.mulPose(new org.joml.Quaternionf().rotateX(rocket.getXRot() * ((float)Math.PI / 180F)));

        VertexConsumer vertexConsumer = bufferSource.getBuffer(model.renderType(getTextureLocation(rocket)));
        model.renderToBuffer(poseStack, vertexConsumer, packedLight, OverlayTexture.NO_OVERLAY,
                1.0F, 1.0F, 1.0F, 1.0F);
        poseStack.popPose();
    }

}
