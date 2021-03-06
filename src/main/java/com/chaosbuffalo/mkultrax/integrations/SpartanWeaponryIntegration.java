package com.chaosbuffalo.mkultrax.integrations;

import com.chaosbuffalo.mkultra.event.ItemEventHandler;
import com.chaosbuffalo.mkultra.item.ItemHelper;
import com.chaosbuffalo.mkultra.item.RangedWeaponry;
import com.chaosbuffalo.mkultra.utils.EntityUtils;
import com.chaosbuffalo.mkultra.utils.ItemUtils;
import com.oblivioussp.spartanweaponry.api.weaponproperty.WeaponPropertyTwoHanded;
import com.oblivioussp.spartanweaponry.entity.projectile.*;
import com.oblivioussp.spartanweaponry.item.*;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntityTippedArrow;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionUtils;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Loader;

import static com.oblivioussp.spartanweaponry.api.WeaponProperties.TWO_HANDED_1;
import static com.oblivioussp.spartanweaponry.api.WeaponProperties.TWO_HANDED_2;

/**
 * Created by Jacob on 7/22/2018.
 */
public class SpartanWeaponryIntegration implements IIntegration {
    @Override
    public boolean isLoaded() {
        return Loader.isModLoaded("spartanweaponry");
    }

    @Override
    public void mod_init() {
        ItemEventHandler.addShieldRestrictedItem(ItemLongbow.class, 0);
        ItemEventHandler.addShieldRestrictedItem(ItemKatana.class, 0);
        ItemEventHandler.addShieldRestrictedItem(ItemCrossbow.class, 0);
        ItemEventHandler.addShieldRestrictedItem(ItemHalberd.class, 0);
        ItemEventHandler.addShieldRestrictedItem(ItemGreatsword.class, 0);
        ItemEventHandler.addShieldRestrictedItem(ItemPike.class, 0);
        ItemEventHandler.addShieldRestrictedItem(ItemGlaive.class, 0);
        ItemUtils.addTwoHandedCallback((Item item) -> {
            if (item instanceof ItemSwordBase){
                ItemSwordBase itemSword = (ItemSwordBase) item;
                return itemSword.hasWeaponProperty(TWO_HANDED_1)
                        || itemSword.hasWeaponProperty(TWO_HANDED_2);
            } else {
                return false;
            }
        });
        ItemUtils.addCriticalStats(ItemKatana.class, 1, .1f, 3.0f);
        ItemUtils.addCriticalStats(ItemRapier.class, 1, .1f, 2.5f);
        ItemUtils.addCriticalStats(ItemLongsword.class, 1, .05f, 2.5f);
        ItemUtils.addCriticalStats(ItemSaber.class, 1, .05f, 2.5f);
        ItemUtils.addCriticalStats(ItemHammer.class, 1, .15f, 3.0f);
        ItemUtils.addCriticalStats(ItemWarhammer.class, 1, .15f, 3.0f);
        ItemUtils.addCriticalStats(ItemCaestus.class, 1, .2f, 2.0f);
        ItemUtils.addCriticalStats(ItemSpear.class, 1, .05f, 2.5f);
        ItemUtils.addCriticalStats(ItemHalberd.class, 1, .1f, 2.0f);
        ItemUtils.addCriticalStats(ItemPike.class, 1, .05f, 2.0f);
        ItemUtils.addCriticalStats(ItemLance.class, 1, .05f, 2.5f);
        ItemUtils.addCriticalStats(ItemBattleaxe.class, 1, .05f, 3.0f);
        ItemUtils.addCriticalStats(ItemMace.class, 1, .15f, 2.5f);
        ItemUtils.addCriticalStats(ItemQuarterstaff.class, 1, .10f, 3.0f);
        ItemUtils.addCriticalStats(ItemGlaive.class, 1, .1f, 2.75f);

        EntityUtils.addCriticalStats(EntityThrownWeapon.class, 1, .05f, 3.0f);
        EntityUtils.addCriticalStats(EntityThrownJavelin.class, 2, .15f, 3.0f);
        EntityUtils.addCriticalStats(EntityThrowingAxe.class, 2, .1f, 3.5f);
        EntityUtils.addCriticalStats(EntityThrowingKnife.class, 2, .2f, 2.5f);
        EntityUtils.addCriticalStats(EntityBolt.class, 1, .05f, 3.0f);
        EntityUtils.addCriticalStats(EntityBoltTipped.class, 2, .05f, 3.0f);
        EntityUtils.addCriticalStats(EntityBoltSpectral.class, 2, .1f, 3.0f);
        EntityUtils.addCriticalStats(EntityBoomerang.class, 2, .1f, 3.0f);
        RangedWeaponry.registerWeapon(new SpartanCrossbowWeapon());
    }

    static class SpartanCrossbowWeapon implements RangedWeaponry.IRangedWeapon {

        @Override
        public boolean isRangedWeapon(ItemStack itemStack) {
            return itemStack.getItem() instanceof ItemCrossbow;
        }

        @Override
        public ItemStack findAmmo(EntityPlayer entityPlayer) {
            return ItemHelper.find(entityPlayer, i -> i.getItem() instanceof ItemBolt);
        }

        @Override
        public void applyEffects(EntityArrow newArrow, ItemStack shooter, ItemStack ammo) {
            if (newArrow instanceof EntityBoltTipped) {
                for (PotionEffect e : PotionUtils.getEffectsFromStack(ammo)) {
                    ((EntityBoltTipped) newArrow).addEffect(e);
                }
            }
            if (newArrow instanceof EntityTippedArrow) {
                for (PotionEffect e : PotionUtils.getEffectsFromStack(ammo)) {
                    ((EntityTippedArrow) newArrow).addEffect(e);
                }
            }
        }

        @Override
        public EntityArrow createAmmoEntity(World world, ItemStack itemStack, EntityLivingBase entityLivingBase) {
            return ((ItemBolt) itemStack.getItem()).createBolt(world, itemStack, entityLivingBase);
        }
    }
}
