package com.fuyossi.village_extension;

import net.fabricmc.fabric.api.creativetab.v1.CreativeModeTabEvents;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;

import java.util.function.Function;

public class ModItems {
    public static final Item TRAIN_TICKET = register(
            ModItemIds.TRAIN_TICKET,
            Item::new,
            new Item.Properties().stacksTo(16)
    );

    public static final Item IC_CARD = register(
            ModItemIds.IC_CARD,
            Item::new,
            new Item.Properties().stacksTo(16)
    );

    private static Item register(ResourceKey<Item> itemKey, Function<Item.Properties, Item> itemFactory, Item.Properties settings) {
        Item item = itemFactory.apply(settings.setId(itemKey));
        Registry.register(BuiltInRegistries.ITEM, itemKey, item);
        return item;
    }

    public static void initialize() {
        CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.TOOLS_AND_UTILITIES).register(tab -> {
            tab.accept(TRAIN_TICKET);
            tab.accept(IC_CARD);
        });
    }
}
