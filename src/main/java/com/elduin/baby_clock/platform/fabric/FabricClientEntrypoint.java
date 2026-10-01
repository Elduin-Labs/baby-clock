package com.elduin.baby_clock.platform.fabric;

//? fabric {

import com.elduin.baby_clock.BabyClock;
import dev.kikugie.fletching_table.annotation.fabric.Entrypoint;
import net.fabricmc.api.ClientModInitializer;

@Entrypoint("client")
public class FabricClientEntrypoint implements ClientModInitializer {

	@Override
	public void onInitializeClient() {
		BabyClock.onInitializeClient();
	}

}
//?}
