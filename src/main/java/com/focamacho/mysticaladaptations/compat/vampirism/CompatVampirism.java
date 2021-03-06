package com.focamacho.mysticaladaptations.compat.vampirism;

import de.teamlapen.vampirism.core.ModEffects;
import de.teamlapen.vampirism.player.vampire.VampirePlayer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.world.World;

public class CompatVampirism {

    public static void fillThirst(PlayerEntity player, World world) {
        if(!world.isClientSide) {
            VampirePlayer vampire = VampirePlayer.get(player);
            vampire.drinkBlood(vampire.getBloodStats().getMaxBlood(), 0, false);
        }
    }

    public static void applySunscreen(PlayerEntity player, World world) {
        EffectInstance effect = new EffectInstance(ModEffects.sunscreen, 20, 5, false, false);
        if(world.isClientSide) effect.setNoCounter(true);
        player.addEffect(effect);
    }

}
