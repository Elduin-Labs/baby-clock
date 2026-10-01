package com.elduin.baby_clock.client;

import com.elduin.baby_clock.BabyClock;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.player.PlayerModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.resources.Identifier;

/** Draws the open mouth and drool on any player who has been turned into a baby. */
public class BabyMouthLayer extends RenderLayer<AvatarRenderState, PlayerModel> {

	private static final Identifier TEXTURE = BabyClock.id("textures/entity/baby_mouth.png");

	/** Babies are half size. Anything this small or smaller gets the mouth. */
	private static final float BABY_SIZE = 0.6F;

	private final BabyMouthModel model;

	public BabyMouthLayer(RenderLayerParent<AvatarRenderState, PlayerModel> parent, EntityModelSet models) {
		super(parent);
		this.model = new BabyMouthModel(models.bakeLayer(BabyClockClient.BABY_MOUTH));
	}

	@Override
	public void submit(PoseStack poseStack, SubmitNodeCollector collector, int light, AvatarRenderState state, float yRot, float xRot) {
		if (state.scale > BABY_SIZE || state.isInvisible) {
			return;
		}
		int overlay = LivingEntityRenderer.getOverlayCoords(state, 0.0F);
		collector.submitModel(this.model, state, poseStack, RenderTypes.entityCutout(TEXTURE), light, overlay, state.outlineColor, null);
	}
}
