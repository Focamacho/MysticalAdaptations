package com.focamacho.mysticaladaptations.block;

import com.blakebr0.mysticalagriculture.lib.ModTooltips;
import com.focamacho.mysticaladaptations.tiles.InsaniumFurnaceTileEntity;
import net.minecraft.block.AbstractFurnaceBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.Stats;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.List;

public class InsaniumFurnaceBlock extends AbstractFurnaceBlock {

    public InsaniumFurnaceBlock() {
        super(Properties.copy(Blocks.FURNACE));
    }

    @Override
    protected void openContainer(World world, BlockPos pos, PlayerEntity player) {
        TileEntity tile = world.getBlockEntity(pos);
        if (tile instanceof InsaniumFurnaceTileEntity) {
            player.openMenu((InsaniumFurnaceTileEntity) tile);
            player.awardStat(Stats.INTERACT_WITH_FURNACE);
        }
    }

    @Override
    public TileEntity newBlockEntity(IBlockReader world) {
        return new InsaniumFurnaceTileEntity();
    }

    @Override
    public void setPlacedBy(World world, BlockPos pos, BlockState state, LivingEntity entity, ItemStack stack) {}

    @Override
    public void onRemove(BlockState state, World world, BlockPos pos, BlockState newState, boolean isMoving) {
        if (state.getBlock() != newState.getBlock()) {
            TileEntity tile = world.getBlockEntity(pos);
            if (tile instanceof InsaniumFurnaceTileEntity) {
                InsaniumFurnaceTileEntity furnace = (InsaniumFurnaceTileEntity) tile;
                InventoryHelper.dropContents(world, pos, furnace);
            }
        }

        super.onRemove(state, world, pos, newState, isMoving);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void appendHoverText(ItemStack stack, IBlockReader world, List<ITextComponent> tooltip, ITooltipFlag flag) {
        double cookingSpeedDifference = 200D * 0.01D;
        double cookingSpeedValue = Math.ceil(((200D - cookingSpeedDifference) / cookingSpeedDifference) * 100D) + 100D;
        ITextComponent cookingSpeed = new StringTextComponent(String.valueOf((int) cookingSpeedValue)).append("%");
        double burnTimeDifference = (1600D * 0.1D) / cookingSpeedDifference;
        double burnTimeValue = Math.ceil(((burnTimeDifference - 8D) / 8D) * 100D) + 100D;
        ITextComponent fuelEfficiency = new StringTextComponent(String.valueOf((int) burnTimeValue)).append("%");

        tooltip.add(ModTooltips.COOKING_SPEED.args(cookingSpeed).build());
        tooltip.add(ModTooltips.FUEL_EFFICIENCY.args(fuelEfficiency).build());
    }

}