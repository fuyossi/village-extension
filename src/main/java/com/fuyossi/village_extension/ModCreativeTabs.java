package com.fuyossi.village_extension;

import net.fabricmc.fabric.api.creativetab.v1.FabricCreativeModeTab;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public final class ModCreativeTabs {
    public static final ResourceKey<CreativeModeTab> VILLAGE_TAB_KEY = ResourceKey.create(
            BuiltInRegistries.CREATIVE_MODE_TAB.key(),
            VillageExtension.id("village_tab")
    );

    public static final CreativeModeTab VILLAGE_TAB = FabricCreativeModeTab.builder()
            .icon(() -> new ItemStack(Items.EMERALD))   // タブのアイコン
            .title(Component.translatable("creativeTab.village-extension"))
            .displayItems((params, output) -> {
                output.accept(ModBlocks.TRAIN_STATION_TICKET_GATE);
            })
            .build();

    public static void initialize() {
        Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, VILLAGE_TAB_KEY, VILLAGE_TAB);
    }

    private ModCreativeTabs() {}
}