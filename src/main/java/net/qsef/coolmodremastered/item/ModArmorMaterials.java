package net.qsef.coolmodremastered.item;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.crafting.Ingredient;
import net.qsef.coolmodremastered.CoolModRemastered;

import java.util.function.Supplier;

public enum ModArmorMaterials implements ArmorMaterial {
    STEEL("steel", 41, new int[] { 2, 6, 5, 2 }, 22, SoundEvents.ARMOR_EQUIP_IRON,
            1f, 0f, 0.3f, 0.2f, () -> Ingredient.of(ModItems.SteelIngot.get()));

    private final String name;
    private final int durabilityMultiplier;
    private final int[] protectionAmounts;
    private final int enchantmentValue;
    private final SoundEvent equipSound;
    private final float toughness;
    private final float knockbackResistance;
    private final float fireResistance;
    private final float blastResistance;
    private final Supplier<Ingredient> repairItem;

    private static final int[] BASE_DURABILITY = { 11, 16, 15, 13 };

    ModArmorMaterials(String name, int durabilityMultiplier, int[] protectionAmounts, int enchantmentValue,
                             SoundEvent equipSound, float toughness, float knockbackResistance, float fireResistance,
                             float blastResistance, Supplier<Ingredient> repairItem) {
        this.name = name;
        this.durabilityMultiplier = durabilityMultiplier;
        this.protectionAmounts = protectionAmounts;
        this.enchantmentValue = enchantmentValue;
        this.equipSound = equipSound;
        this.toughness = toughness;
        this.knockbackResistance = knockbackResistance;
        this.fireResistance = fireResistance;
        this.blastResistance = blastResistance;
        this.repairItem = repairItem;
    }

    @Override
    public int getDurabilityForType(ArmorItem.Type pType) {
        return BASE_DURABILITY[pType.ordinal()] * durabilityMultiplier;
    }

    @Override
    public int getDefenseForType(ArmorItem.Type pType) {
        return protectionAmounts[pType.ordinal()];
    }

    @Override
    public int getEnchantmentValue() {
        return enchantmentValue;
    }

    @Override
    public SoundEvent getEquipSound() {
        return equipSound;
    }

    @Override
    public Ingredient getRepairIngredient() {
        return repairItem.get();
    }

    @Override
    public String getName() {
        return CoolModRemastered.MOD_ID + ":" + name;
    }

    @Override
    public float getToughness() {
        return toughness;
    }

    @Override
    public float getKnockbackResistance() {
        return knockbackResistance;
    }

    public float getFireResistance() {
        return fireResistance;
    }

    public float getBlastResistance() {
        return blastResistance;
    }
}
