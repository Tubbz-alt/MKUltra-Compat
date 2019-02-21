package com.chaosbuffalo.mkultrax.integrations;

import com.chaosbuffalo.mkultra.core.PlayerAttributes;
import com.chaosbuffalo.mkultra.spawn.AttributeRange;
import com.chaosbuffalo.mkultra.spawn.AttributeSetter;
import com.chaosbuffalo.mkultra.spawn.CustomModifier;
import com.chaosbuffalo.mkultra.spawn.CustomSetter;
import com.chaosbuffalo.mkultra.utils.MathUtils;
import com.chaosbuffalo.mkultrax.Log;
import com.chaosbuffalo.mkultrax.MKUltraX;
import com.chaosbuffalo.mkultrax.MKXWorldListener;
import com.chaosbuffalo.mkultrax.custom_modifiers.lycanites.SubspeciesModifier;
import com.chaosbuffalo.mkultrax.init.MKXSpawnRegistry;
import com.chaosbuffalo.targeting_api.Targeting;
import com.google.gson.JsonObject;
import com.lycanitesmobs.core.entity.EntityCreatureBase;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.Loader;

import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;

/**
 * Created by Jacob on 7/21/2018.
 */
public class LycanitesIntegration implements IIntegration {
    private static double DISTANCE_SCALING = 1500.0;
    private static int LEVEL_SCALING = 4;
    private static int MAX_SCALE_ZONES = 8;

    @Override
    public boolean isLoaded() {
        return Loader.isModLoaded("lycanitesmobs");
    }

    @Override
    public void init_attribute_setters_phase(){
        BiConsumer<EntityLivingBase, AttributeRange> level_setter = (entity, range) -> {
            double creatureLevel = MathUtils.lerp_double(range.start, range.stop, range.level, range.maxLevel);
            if (entity instanceof EntityCreatureBase){
                EntityCreatureBase creature = (EntityCreatureBase)entity;
                creature.applyLevel((int) creatureLevel);
            }
        };
        AttributeSetter lycanites_level = new AttributeSetter(MKUltraX.MODID, "lycanites_level", level_setter);
        MKXSpawnRegistry.regInternalAttributeSetter(lycanites_level);
    }

    @Override
    public void init_custom_setters_phase(){
        BiFunction<JsonObject, CustomSetter, CustomModifier> subspecies_deserializer = (obj, setter) ->{
            if (obj.has("subspecies_index")){
                int speciesIndex = obj.get("subspecies_index").getAsInt();
                SubspeciesModifier mod = new SubspeciesModifier(setter.getApplier(), speciesIndex);
                return mod;
            }
            Log.info("Error deserializing subspecies setter. %s", obj.toString());
            return null;
        };
        BiConsumer<EntityLivingBase, CustomModifier> subspecies_modifier = (entity, modifier) ->{
            if (entity instanceof EntityCreatureBase && modifier instanceof SubspeciesModifier){
                SubspeciesModifier subModifier = (SubspeciesModifier) modifier;
                EntityCreatureBase creature = (EntityCreatureBase) entity;
                creature.setSubspecies(subModifier.subspeciesIndex);
            } else {
                Log.info("Skipping apply subspecies modifier either entitiy is" +
                        " not EntityCreatureBase or modifier is not right type.");
            }
        };
        CustomSetter setSubspecies = new CustomSetter(
                new ResourceLocation(MKUltraX.MODID, "set_subspecies"),
                subspecies_deserializer, subspecies_modifier);
        MKXSpawnRegistry.regInternalSetter(setSubspecies);
    }

    @Override
    public void mod_init() {
        Targeting.registerFriendlyEntity("com.lycanitesmobs.elementalmobs.entity.EntityNymph");
        Targeting.registerFriendlyEntity("com.lycanitesmobs.elementalmobs.entity.EntityWisp");
        BiFunction<Entity, Entity, Boolean> lycanitesWrapper = (caster, target) -> {
            return Targeting.isValidTarget(Targeting.TargetType.ENEMY, caster, target, true);
        };
        com.lycanitesmobs.api.Targeting.registerCallback(lycanitesWrapper);
        MKXWorldListener.registerEntityLoadedCallback(LycanitesIntegration::on_entity_added);
    }

    public static void on_entity_added(Entity entityIn) {
        if (entityIn instanceof EntityCreatureBase){
            EntityCreatureBase creature = (EntityCreatureBase) entityIn;
            double scale = creature.getRenderScale();
            if (scale > 1.0){
                AttributeModifier modifier = creature.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).getModifier(UUID.fromString("749d6722-b566-472d-b33c-d3c1b8cd0b8d"));
                if (modifier == null){
                    creature.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH)
                            .applyModifier(new AttributeModifier(
                                    UUID.fromString("749d6722-b566-472d-b33c-d3c1b8cd0b8d"),
                                    "Size Health Bonus",
                                    scale + 2.0, PlayerAttributes.OP_SCALE_MULTIPLICATIVE));
                }
            }
            double distance2 = creature.getDistanceSq(BlockPos.ORIGIN);

            double distanceOut = distance2 / (DISTANCE_SCALING * DISTANCE_SCALING);
            if (distanceOut > 1.0){
                int scaleFactor = Math.min((int) distanceOut, MAX_SCALE_ZONES);
                if (creature.getLevel() < (scaleFactor+2)*LEVEL_SCALING){
                    creature.addLevel((creature.getRNG().nextInt(scaleFactor*LEVEL_SCALING)));


                }

            }
        }

    }
}
