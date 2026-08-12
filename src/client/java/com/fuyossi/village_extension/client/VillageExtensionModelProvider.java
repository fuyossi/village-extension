package com.fuyossi.village_extension.client;

import com.fuyossi.village_extension.VillageExtension;
import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricModelProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.resources.Identifier;

public class VillageExtensionModelProvider extends FabricModelProvider {
    public VillageExtensionModelProvider(FabricPackOutput output) {
        super(output);
    }
    @Override
    public void generateBlockStateModels(BlockModelGenerators blockStateModelGenerator) {

    }
    @Override
    public void generateItemModels(ItemModelGenerators itemModelGenerator) {
    }
    @Override
    public String getName() {
        return "VillageExtensionModelProvider";
    }
}
