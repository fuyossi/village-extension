package com.fuyossi.village_extension;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;

public class ModItemIds {
    public static final ResourceKey<Item> TRAIN_TICKET = create("train_ticket");

    private static ResourceKey<Item> create(String name) {
        return ResourceKey.create(Registries.ITEM, VillageExtension.id(name));
    }
}
