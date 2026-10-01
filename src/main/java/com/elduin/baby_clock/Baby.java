package com.elduin.baby_clock;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.Identifier;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.phys.Vec3;

/**
 * Turning players into babies and back. Being a baby is a permanent modifier on the player's
 * scale attribute, so it is saved with the player and every client sees it. The clock changes it
 * one tick at a time; while it's all the way down, the player drools.
 */
public final class Baby {

	private static final Identifier MODIFIER = BabyClock.id("baby");

	/** How small a baby is: half size. */
	private static final double BABY = -0.5;

	/** The clock ticks this many times on the way down (or up)... */
	private static final int STEPS = 8;
	/** ...this many game ticks apart. */
	private static final int TICK_EVERY = 10;

	/** Players whose clock is ticking right now: where they're going, and how far they've got. */
	private static final Map<UUID, Change> CHANGING = new HashMap<>();

	private record Change(double from, double to, int step) {
	}

	private Baby() {
	}

	public static void register() {
		ServerTickEvents.END_SERVER_TICK.register(Baby::tick);
	}

	public static boolean isBaby(ServerPlayer player) {
		return amount(player) <= BABY + 0.01;
	}

	/** Tick down into a baby. */
	public static void shrink(ServerPlayer player) {
		CHANGING.put(player.getUUID(), new Change(amount(player), BABY, 0));
	}

	/** Tick back up into a grown-up. */
	public static void grow(ServerPlayer player) {
		CHANGING.put(player.getUUID(), new Change(amount(player), 0.0, 0));
	}

	/** Baby -> grown-up, anything else -> baby. */
	public static void toggle(ServerPlayer player) {
		Change going = CHANGING.get(player.getUUID());
		boolean headingToBaby = going != null ? going.to() == BABY : isBaby(player);
		if (headingToBaby) {
			grow(player);
		} else {
			shrink(player);
		}
	}

	private static void tick(MinecraftServer server) {
		long time = server.getTickCount();

		Iterator<Map.Entry<UUID, Change>> it = CHANGING.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<UUID, Change> entry = it.next();
			ServerPlayer player = server.getPlayerList().getPlayer(entry.getKey());
			if (player == null) {
				it.remove();
				continue;
			}
			if (time % TICK_EVERY != 0) {
				continue;
			}
			Change change = entry.getValue();
			int step = change.step() + 1;
			setAmount(player, change.from() + (change.to() - change.from()) * step / STEPS);
			// tick... tock...
			player.level().playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.NOTE_BLOCK_HAT,
					SoundSource.PLAYERS, 1.0F, step % 2 == 0 ? 1.2F : 1.6F);
			if (step >= STEPS) {
				it.remove();
			} else {
				entry.setValue(new Change(change.from(), change.to(), step));
			}
		}

		if (time % 4 == 0) {
			for (ServerPlayer player : server.getPlayerList().getPlayers()) {
				if (isBaby(player) && !player.isSpectator()) {
					drool(player);
				}
			}
		}
	}

	/** Drips out of the open mouth and down the front. */
	private static void drool(ServerPlayer player) {
		ServerLevel level = (ServerLevel) player.level();
		double scale = player.getScale();
		Vec3 forward = Vec3.directionFromRotation(0.0F, player.getYHeadRot());
		Vec3 mouth = player.getEyePosition().add(forward.scale(0.28 * scale)).subtract(0.0, 0.22 * scale, 0.0);
		level.sendParticles(ParticleTypes.DRIPPING_WATER, mouth.x, mouth.y, mouth.z, 1, 0.03, 0.0, 0.03, 0.0);
		if (player.getRandom().nextInt(3) == 0) {
			level.sendParticles(ParticleTypes.FALLING_WATER, mouth.x, mouth.y - 0.05, mouth.z, 2, 0.05, 0.0, 0.05, 0.0);
		}
		if (player.getRandom().nextInt(10) == 0) {
			level.sendParticles(ParticleTypes.SPLASH, player.getX(), player.getY() + 0.4 * scale, player.getZ(),
					4, 0.15 * scale, 0.1, 0.15 * scale, 0.0);
		}
	}

	private static double amount(ServerPlayer player) {
		AttributeInstance scale = player.getAttribute(Attributes.SCALE);
		AttributeModifier modifier = scale == null ? null : scale.getModifier(MODIFIER);
		return modifier == null ? 0.0 : modifier.amount();
	}

	private static void setAmount(ServerPlayer player, double amount) {
		AttributeInstance scale = player.getAttribute(Attributes.SCALE);
		if (scale == null) {
			return;
		}
		scale.removeModifier(MODIFIER);
		if (amount < -0.001) {
			scale.addPermanentModifier(new AttributeModifier(MODIFIER, amount, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));
		}
	}
}
