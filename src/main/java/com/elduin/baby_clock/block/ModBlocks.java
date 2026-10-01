package com.elduin.baby_clock.block;

import com.elduin.baby_clock.BabyClock;
import com.elduin.baby_clock.Compat;

import java.util.function.Function;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;

public final class ModBlocks {

	public static final Block BABY_CLOCK = register("baby_clock", BabyClockBlock::new,
			BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_PINK).strength(0.5f).sound(SoundType.METAL)
					.noOcclusion());

	public static final Item BABY_CLOCK_ITEM = registerBlockItem("baby_clock", BABY_CLOCK);

	/** Only the picture on the Baby tab: a baby face. Not in any tab and not craftable. */
	public static final Item BABY_FACE = registerItem("baby_face");

	/** The "Baby" creative tab. */
	public static final CreativeModeTab TAB = Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB,
			ResourceKey.create(Registries.CREATIVE_MODE_TAB, BabyClock.id("baby")),
			Compat.tabBuilder()
					.icon(() -> new ItemStack(BABY_FACE))
					.title(Component.translatable("itemGroup.baby_clock"))
					.displayItems((params, output) -> output.accept(BABY_CLOCK_ITEM))
					.build());

	private ModBlocks() {
	}

	/** Loads this class, which registers everything above. */
	public static void init() {
	}

	private static Block register(String name, Function<BlockBehaviour.Properties, Block> factory,
			BlockBehaviour.Properties properties) {
		ResourceKey<Block> key = ResourceKey.create(Registries.BLOCK, BabyClock.id(name));
		return Registry.register(BuiltInRegistries.BLOCK, key, factory.apply(properties.setId(key)));
	}

	private static Item registerBlockItem(String name, Block block) {
		ResourceKey<Item> key = ResourceKey.create(Registries.ITEM, BabyClock.id(name));
		return Registry.register(BuiltInRegistries.ITEM, key,
				new BlockItem(block, new Item.Properties().setId(key).useBlockDescriptionPrefix()));
	}

	private static Item registerItem(String name) {
		ResourceKey<Item> key = ResourceKey.create(Registries.ITEM, BabyClock.id(name));
		return Registry.register(BuiltInRegistries.ITEM, key, new Item(new Item.Properties().setId(key)));
	}
}
