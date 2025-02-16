package net.qsef.coolmodremastered.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;

public class RocketModel<T extends Entity> extends HierarchicalModel<T> {
	private final ModelPart bb_main;

	public RocketModel(ModelPart root) {
		this.bb_main = root.getChild("bb_main");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition bb_main = partdefinition.addOrReplaceChild("bb_main", CubeListBuilder.create().texOffs(0, 0).addBox(-2.0F, -9.0F, -6.0F, 4.0F, 4.0F, 13.0F, new CubeDeformation(0.0F))
		.texOffs(48, 46).addBox(-1.0F, -8.0F, 8.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(0, 17).addBox(-1.0F, -5.0F, -6.0F, 2.0F, 1.0F, 13.0F, new CubeDeformation(0.0F))
		.texOffs(0, 31).addBox(-3.0F, -8.0F, -6.0F, 1.0F, 2.0F, 13.0F, new CubeDeformation(0.0F))
		.texOffs(30, 17).addBox(-1.0F, -10.0F, -6.0F, 2.0F, 1.0F, 13.0F, new CubeDeformation(0.0F))
		.texOffs(28, 31).addBox(2.0F, -8.0F, -6.0F, 1.0F, 2.0F, 13.0F, new CubeDeformation(0.0F))
		.texOffs(34, 14).addBox(-2.0F, -8.0F, 7.0F, 4.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(48, 50).addBox(-1.0F, -9.0F, 7.0F, 2.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 7F, 0.0F));

		PartDefinition bottom_flap_r1 = bb_main.addOrReplaceChild("bottom_flap_r1", CubeListBuilder.create().texOffs(24, 46).addBox(-1.0F, -2.0F, -6.0F, 5.0F, 0.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.0F, -7.0F, 0.0F, 0.0F, -0.3927F, 1.5708F));

		PartDefinition top_flap_r1 = bb_main.addOrReplaceChild("top_flap_r1", CubeListBuilder.create().texOffs(0, 46).addBox(-1.0F, -2.0F, -6.0F, 5.0F, 0.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.0F, -10.0F, 1.0F, 0.0F, 0.3927F, 1.5708F));

		PartDefinition right_flap_r1 = bb_main.addOrReplaceChild("right_flap_r1", CubeListBuilder.create().texOffs(34, 7).addBox(-1.0F, -2.0F, -6.0F, 5.0F, 0.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -5.0F, 0.0F, 0.0F, -0.4363F, 0.0F));

		PartDefinition left_flap_r1 = bb_main.addOrReplaceChild("left_flap_r1", CubeListBuilder.create().texOffs(34, 0).addBox(-1.0F, -2.0F, -6.0F, 5.0F, 0.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.0F, -5.0F, 1.0F, 0.0F, 0.4363F, 0.0F));

		return LayerDefinition.create(meshdefinition, 64, 64);
	}

	@Override
	public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		bb_main.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}

	@Override
	public ModelPart root() {
		return bb_main;
	}
}