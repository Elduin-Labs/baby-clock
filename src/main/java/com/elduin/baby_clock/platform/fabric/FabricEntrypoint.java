package com.elduin.baby_clock.platform.fabric;

//? fabric {

import com.elduin.baby_clock.BabyClock;
import dev.kikugie.fletching_table.annotation.fabric.Entrypoint;
import net.fabricmc.api.ModInitializer;

@Entrypoint("main")
public class FabricEntrypoint implements ModInitializer {

	@Override
	public void onInitialize() {
		BabyClock.onInitialize();
		FabricEventSubscriber.registerEvents();
	}
}
//?}
