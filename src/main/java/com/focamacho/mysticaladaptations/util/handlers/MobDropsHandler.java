package com.focamacho.mysticaladaptations.util.handlers;

import java.util.List;
import java.util.Random;

import javax.annotation.Nullable;

import com.blakebr0.mysticalagriculture.lib.CropType;
import com.blakebr0.mysticalagriculture.lib.CropType.Type;
import com.blakebr0.mysticalagriculture.util.ModChecker;
import com.focamacho.mysticaladaptations.config.ModConfig;
import com.focamacho.mysticaladaptations.items.SeedExtractor;
import com.focamacho.mysticaladaptations.lib.SeedExtractorRecipe;
import com.focamacho.mysticaladaptations.lib.SeedExtractorRecipes;
import com.focamacho.mysticaladaptations.util.ModCheck;

import cofh.thermalfoundation.entity.monster.EntityBasalz;
import cofh.thermalfoundation.entity.monster.EntityBlitz;
import cofh.thermalfoundation.entity.monster.EntityBlizz;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.AbstractSkeleton;
import net.minecraft.entity.monster.EntityBlaze;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.monster.EntityGhast;
import net.minecraft.entity.monster.EntityGuardian;
import net.minecraft.entity.monster.EntityPigZombie;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.entity.monster.EntityWitherSkeleton;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.entity.passive.EntityRabbit;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class MobDropsHandler{

    @SubscribeEvent
    public void onMobDrops(LivingDropsEvent event){
    	if(event.isRecentlyHit() && event.getSource().getTrueSource() != null && event.getSource().getTrueSource() instanceof EntityPlayer) {
    		EntityLivingBase player = ((EntityPlayer) event.getSource().getTrueSource());
			ItemStack weapon = ((EntityPlayer) event.getSource().getTrueSource()).getHeldItemMainhand();
			if(!weapon.isEmpty() && weapon.getItem() instanceof SeedExtractor) {
				if(ModConfig.MOB_SEED_DROP) {
					for(SeedExtractorRecipe recipe : SeedExtractorRecipes.allRecipes) {
						if(checkRecipe(event.getEntityLiving(), recipe, weapon)) dropItem(event, recipe.getSeed(), weapon, player);
					}
				}
			}
    	}
    }
    
    public boolean checkRecipe(EntityLivingBase entity, SeedExtractorRecipe recipe, ItemStack extractor) {
    	if(checkEntity(entity, recipe.getEntitiesList()) && checkTier(recipe.getTier(), extractor)) return true;
    	return false;
    }
    public boolean checkEntity(EntityLivingBase entity, List<ResourceLocation> resourceList) {
		if(!(resourceList == null || resourceList.isEmpty())) {
	    	for(ResourceLocation r : resourceList) {
	    		if(EntityList.getKey(entity).equals(r)) {
	    			return true;
	    		}
	    	}
		}
    	return false;
    }
    
    public void dropItem(LivingDropsEvent event, ItemStack drop, ItemStack extractor, EntityLivingBase player) {
    	EntityItem entityItem = new EntityItem(event.getEntity().world, event.getEntity().posX, event.getEntity().posY, event.getEntity().posZ, drop);
    	event.getDrops().add(entityItem);
    	extractor.damageItem(1, player);
    }
	
	public static boolean checkTier(int tier, ItemStack extractor) {
		if(!ModConfig.EXTRACTOR_ANY_TIER) {
			if(ModConfig.EXTRACTOR_LOWER_TIER) {
				if(extractor.getTagCompound().getInteger("tier") >= tier) return true;
				else return false;
			} else {
				if(extractor.getTagCompound().getInteger("tier") == tier) return true;
				else return false;
			}
		} else return true;
	}

}