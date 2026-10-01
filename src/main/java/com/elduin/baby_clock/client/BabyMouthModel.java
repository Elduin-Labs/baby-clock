package com.elduin.baby_clock.client;

import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.model.player.PlayerModel;

/**
 * Just a mouth and a dribble of drool, stuck to the front of the player's head. Built like the
 * game's own deadmau5 ears: a player model with everything cleared except bits on the head, so the
 * head turns and nods with the real one.
 */
public class BabyMouthModel extends PlayerModel {

	public BabyMouthModel(ModelPart root) {
		super(root, false);
	}

	public static LayerDefinition createLayer() {
		MeshDefinition mesh = PlayerModel.createMesh(CubeDeformation.NONE, false);
		PartDefinition head = mesh.getRoot().clearRecursively().getChild("head");
		// The face is at z = -4. These sit a hair in front of it so they don't flicker.
		head.addOrReplaceChild("mouth", CubeListBuilder.create()
				.texOffs(0, 0).addBox(-2.0F, -3.0F, -4.05F, 4.0F, 2.0F, 0.0F), PartPose.ZERO);
		head.addOrReplaceChild("drool", CubeListBuilder.create()
				.texOffs(0, 4).addBox(0.0F, -1.0F, -4.06F, 1.0F, 2.0F, 0.0F), PartPose.ZERO);
		return LayerDefinition.create(mesh, 16, 16);
	}
}
