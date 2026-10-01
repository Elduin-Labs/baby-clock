package com.elduin.baby_clock.client;

import com.elduin.baby_clock.BabyClock;

//? if >=26 {
/*import net.fabricmc.fabric.api.client.rendering.v1.LivingEntityRenderLayerRegistrationCallback;
import net.fabricmc.fabric.api.client.rendering.v1.ModelLayerRegistry;
*///? } else {
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.LivingEntityFeatureRendererRegistrationCallback;
//? }
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.player.PlayerModel;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.player.AvatarRenderer;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;

public final class BabyClockClient {

	public static final ModelLayerLocation BABY_MOUTH = new ModelLayerLocation(BabyClock.id("baby_mouth"), "main");

	private BabyClockClient() {
	}

	@SuppressWarnings("unchecked")
	public static void register() {
		// Fabric renamed both of these in 26: "entity model layer" -> "model layer",
		// "feature renderer" -> "render layer".
		//? if >=26 {
		/*ModelLayerRegistry.registerModelLayer(BABY_MOUTH, BabyMouthModel::createLayer);
		LivingEntityRenderLayerRegistrationCallback.EVENT.register((type, renderer, helper, context) -> {
		*///? } else {
		EntityModelLayerRegistry.registerModelLayer(BABY_MOUTH, BabyMouthModel::createLayer);
		LivingEntityFeatureRendererRegistrationCallback.EVENT.register((type, renderer, helper, context) -> {
		//? }
			if (renderer instanceof AvatarRenderer<?> avatar) {
				helper.register(new BabyMouthLayer(
						(RenderLayerParent<AvatarRenderState, PlayerModel>) (Object) avatar, context.getModelSet()));
			}
		});
	}
}
