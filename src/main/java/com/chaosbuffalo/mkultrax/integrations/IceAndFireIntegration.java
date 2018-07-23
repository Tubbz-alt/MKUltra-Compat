package com.chaosbuffalo.mkultrax.integrations;

import com.chaosbuffalo.mkultra.core.ArmorClass;
import com.chaosbuffalo.mkultra.event.ItemRestrictionHandler;
import com.chaosbuffalo.mkultra.utils.ItemUtils;
import com.github.alexthe666.iceandfire.core.ModItems;
import com.github.alexthe666.iceandfire.enums.EnumDragonArmor;
import com.github.alexthe666.iceandfire.item.ItemAlchemySword;
import com.github.alexthe666.iceandfire.item.ItemModAxe;
import com.github.alexthe666.iceandfire.item.ItemTrollWeapon;
import net.minecraft.entity.Entity;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Loader;

/**
 * Created by Jacob on 7/21/2018.
 */
public class IceAndFireIntegration implements IIntegration {

    @Override
    public boolean isLoaded() {
        return Loader.isModLoaded("iceandfire");
    }

    @Override
    public void mod_init() {
        ArmorClass.HEAVY
                .register(EnumDragonArmor.armor_blue.armorMaterial)
                .register(EnumDragonArmor.armor_bronze.armorMaterial)
                .register(EnumDragonArmor.armor_gray.armorMaterial)
                .register(EnumDragonArmor.armor_green.armorMaterial)
                .register(EnumDragonArmor.armor_red.armorMaterial)
                .register(EnumDragonArmor.armor_sapphire.armorMaterial)
                .register(EnumDragonArmor.armor_silver.armorMaterial)
                .register(EnumDragonArmor.armor_white.armorMaterial)
                .register(ModItems.silverMetal);

        ArmorClass.MEDIUM
                .register(ModItems.red_deathworm)
                .register(ModItems.white_deathworm)
                .register(ModItems.yellow_deathworm);

        ArmorClass.LIGHT
                .register(ModItems.troll_forest)
                .register(ModItems.troll_frost)
                .register(ModItems.troll_mountain);

        ArmorClass.ROBES
                .register(ModItems.blindfoldArmor)
                .register(ModItems.earplugsArmor)
                .register(ModItems.sheep);

        ItemUtils.addCriticalStats(ItemAlchemySword.class, 1, .1f, 2.25f);
        ItemUtils.addCriticalStats(ItemTrollWeapon.class, 1, .05f, 3.0f);
        ItemUtils.addCriticalStats(ItemModAxe.class, 1, .15f, 2.0f);
        ItemRestrictionHandler.addShieldRestrictedItem(ItemAlchemySword.class, 0);
        ItemRestrictionHandler.addShieldRestrictedItem(ItemTrollWeapon.class, 0);

    }

    @Override
    public void crafting_register(RegistryEvent.Register<IRecipe> event) {

    }

    @Override
    public void on_entity_added(Entity entityIn) {

    }

    @Override
    public void init_items_phase() {

    }

    @Override
    public void register_tile_entities() {

    }

    @Override
    public void init_blocks_phase() {

    }
}
