package com.elduin.baby_clock;

//? if >=26 {
/*import net.fabricmc.fabric.api.creativetab.v1.FabricCreativeModeTab;
*///? } else {
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
//? }
import net.minecraft.world.item.CreativeModeTab;

/**
 * The handful of things Fabric moved between the versions this mod supports. The client-side ones
 * live in {@code client.BabyClockClient}, which only loads on the client.
 */
public final class Compat {

	private Compat() {
	}

	/** Fabric renamed its creative-tab builder in 26. */
	public static CreativeModeTab.Builder tabBuilder() {
		//? if >=26 {
		/*return FabricCreativeModeTab.builder();
		*///? } else {
		return FabricItemGroup.builder();
		//? }
	}
}
