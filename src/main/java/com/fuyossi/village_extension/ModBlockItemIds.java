package com.fuyossi.village_extension;

import net.minecraft.references.BlockItemId;
import net.minecraft.resources.Identifier;

public class ModBlockItemIds {
    public static final BlockItemId TRAIN_STATION_TICKET_GATE = create("train_station_ticket_gate");

    public static final BlockItemId TRAIN_STATION_TICKET_VENDING_MACHINE = create("train_station_ticket_vending_machine");

    public static final BlockItemId IC_CARD_VENDING_MACHINE = create("ic_card_vending_machine");

    private static BlockItemId create(String name) {
        Identifier id = Identifier.fromNamespaceAndPath(VillageExtension.MOD_ID, name);
        return BlockItemId.create(id, id);
    }
}
