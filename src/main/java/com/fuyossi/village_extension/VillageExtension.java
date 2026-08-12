package com.fuyossi.village_extension;

import net.fabricmc.api.ModInitializer;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.references.BlockItemId;
import net.minecraft.resources.Identifier;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Function;

public class VillageExtension implements ModInitializer {
	public static final String MOD_ID = "village-extension";

	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.

		ModBlocks.initialize();
	}

	public class ModBlocks {
		public static final Block TRAIN_STATION_TICKET_GATE = register(
				ModBlockItemIds.TRAIN_STATION_TICKET_GATE,
				Block::new,
				BlockBehaviour.Properties.ofFullCopy(Blocks.STONE).noOcclusion().noCollision()
		);

		private static Block register(ResourceKey<Block> id, Function<BlockBehaviour.Properties, Block> blockFactory, BlockBehaviour.Properties properties) {
			// Create the block instance
			Block block = blockFactory.apply(properties.setId(id));

			return Registry.register(BuiltInRegistries.BLOCK, id, block);
		}

		private static Block register(BlockItemId id, Function<BlockBehaviour.Properties, Block> blockFactory, BlockBehaviour.Properties properties) {
			// Create the block instance
			Block block = register(id.block(), blockFactory, properties);

			// Create the block item instance
			BlockItem blockItem = new BlockItem(block, new Item.Properties().useBlockDescriptionPrefix().setId(id.item()));
			Registry.register(BuiltInRegistries.ITEM, id.item(), blockItem);

			return block;
		}

		public static void initialize() {
		}
	}

	public static Identifier id(String path) {
		return Identifier.fromNamespaceAndPath(MOD_ID, path);
	}

	public class ModBlockIds {
		private static ResourceKey<Block> create(String name) {
			Identifier id = Identifier.fromNamespaceAndPath(VillageExtension.MOD_ID, name);
			return ResourceKey.create(Registries.BLOCK, id);
		}
	}

	public class ModBlockItemIds {
		public static final BlockItemId TRAIN_STATION_TICKET_GATE = create(
				"train_station_ticket_gate"
		);

		private static BlockItemId create(String name) {
			Identifier id = Identifier.fromNamespaceAndPath(VillageExtension.MOD_ID, name);
			return BlockItemId.create(id, id);
		}
	}
}
