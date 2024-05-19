package net.qsef.coolmodremastered.datagen;

import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;
import net.qsef.coolmodremastered.CoolModRemastered;
import net.qsef.coolmodremastered.item.ModItems;

public class ModItemModelProvider extends ItemModelProvider {
    public ModItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, CoolModRemastered.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        simpleItem(ModItems.PigRelic);
        simpleItem(ModItems.PigSoul);
        simpleItem(ModItems.PorkBurger);
        simpleItem(ModItems.Porkchopyonite);
        simpleItem(ModItems.RoastedPorkchop);
    }

    // creates a basic 2d item
    private ItemModelBuilder simpleItem(RegistryObject<Item> item) {
        return withExistingParent(item.getId().getPath(),
                new ResourceLocation("item/generated")).texture("layer0", // parent: item/generated
                new ResourceLocation(CoolModRemastered.MOD_ID, "item/" + item.getId().getPath())); // layer0: coolmodremastered:item/ItemName
    }
}
