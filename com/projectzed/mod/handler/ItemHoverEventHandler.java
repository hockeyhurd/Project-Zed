/* This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under the terms of the GNU General Public 
* License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Project-Zed is 
* distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
* PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along 
* with Project-Zed. If not, see <http://www.gnu.org/licenses/>
*/
package com.projectzed.mod.handler;

import com.projectzed.api.block.*;
import com.projectzed.api.energy.storage.IEnergyContainer;
import com.projectzed.api.fluid.container.IFluidContainer;
import com.projectzed.api.tileentity.container.AbstractTileEntityFluidContainer;
import com.projectzed.mod.block.BlockAtomicBomb;
import com.projectzed.mod.block.container.AbstractBlockEnergyPipe;
import com.projectzed.mod.block.container.AbstractBlockItemPipe;
import com.projectzed.mod.block.container.AbstractBlockLiquiduct;
import com.projectzed.mod.block.container.BlockEnergyCell;
import com.projectzed.mod.tileentity.container.TileEntityEnergyBankBase;
import com.projectzed.mod.tileentity.container.pipe.TileEntityLiquiductBase;
import net.minecraft.block.Block;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import static com.hockeyhurd.hcorelib.api.util.NumberFormatter.format;

/**
 * Class containing code for all tooltip info related to blocks.
 * 
 * @author hockeyhurd
 * @version Jan 12, 2015
 */
public class ItemHoverEventHandler {

	/** Only static instance of this class. */
	private static final ItemHoverEventHandler HANLDER = new ItemHoverEventHandler();

	private ItemHoverEventHandler() {
	}
	
	/**
	 * @return static instance of this class.
	 */
	public static ItemHoverEventHandler instance() {
		return HANLDER;
	}

	/**
	 * Method used to add relavent information to user tooltips.
	 * 
	 * @param event = event passed.
	 */
	// @SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void onItemHover(ItemTooltipEvent event) {
		ItemStack stack = event.getItemStack();
		if (stack != null) {
			insertTileEntityInfo(event, stack);
			
			Block b = Block.getBlockFromItem(stack.getItem());
			
			if (b instanceof BlockAtomicBomb) {
				event.getToolTip().add(TextFormatting.GOLD + "WARNING: " + TextFormatting.WHITE + "Highly Destructive!");
				event.getToolTip().add(TextFormatting.GREEN + "Unbreakable");
			}
		}
	}
	
	private void insertTileEntityInfo(ItemTooltipEvent event, ItemStack stack) {
		int amount = 0;
		int type = 0;
		Block b = Block.getBlockFromItem(stack.getItem());
		if (b != null) {
			if (b instanceof AbstractBlockContainer) {
				type = 0;
				if (b instanceof BlockEnergyCell && ((BlockEnergyCell) b).getTileEntity() != null) {
					TileEntityEnergyBankBase bank = (TileEntityEnergyBankBase) ((BlockEnergyCell) b).getTileEntity();
					if (stack.hasTagCompound() && stack.getTagCompound().getInteger("ProjectZedPowerStored") > 0) {
						event.getToolTip().add(TextFormatting.GREEN + "Stored: " + TextFormatting.WHITE + format(
								stack.getTagCompound().getInteger("ProjectZedPowerStored")) + " McU");
					}

					event.getToolTip().add(TextFormatting.GREEN + "Capacity: " + TextFormatting.WHITE + format(bank.getMaxStorage()) + " McU");
				}

				amount = ((AbstractBlockContainer) b).getTileEntity().getMaxExportRate();
			}
			
			else if (b instanceof AbstractBlockPipe) {
				IEnergyContainer contE = null;
				IFluidContainer contF = null;
				if (b instanceof AbstractBlockEnergyPipe && ((AbstractBlockEnergyPipe) b).getTileEntity() instanceof IEnergyContainer) {
					type = 0;
					contE = (IEnergyContainer) ((AbstractBlockEnergyPipe) b).getTileEntity();
					amount = contE.getMaxExportRate();
				}

				else if (b instanceof AbstractBlockLiquiduct && ((AbstractBlockLiquiduct) b).getTileEntity() instanceof IFluidContainer) {
					type = 4;
					contF = (TileEntityLiquiductBase) ((AbstractBlockLiquiduct) b).getTileEntity();
					// amount = ((TileEntityLiquiductBase) ((AbstractBlockLiquiduct) b).getTileEntity()).getMaxFluidExportRate();
					amount = contF.getMaxFluidExportRate();
				}
				
				else if (b instanceof AbstractBlockItemPipe && ((AbstractBlockItemPipe) b).getTileEntity() instanceof IInventory) {
					// TODO: Add item info?
				}
				
				else {
					type = 3;
					contE = (IEnergyContainer) ((AbstractBlockEnergyPipe) b).getTileEntity();
					amount = contE.getMaxExportRate();
				}
			}
			
			else if (b instanceof AbstractBlockGenerator) {
				type = 1;
				amount = ((AbstractBlockGenerator) b).getTileEntity().getSource().getEffectiveSize();
			}
			
			else if (b instanceof AbstractBlockMachine) {
				type = 2;
				amount = ((AbstractBlockMachine) b).getTileEntity().getEnergyBurnRate();
			}
			
			else if (b instanceof AbstractBlockFluidContainer) {
				type = 4;
				AbstractTileEntityFluidContainer te = ((AbstractBlockFluidContainer) b).getTileEntity();

				if (stack.hasTagCompound() && stack.getTagCompound().getFloat("Fluid ID") >= 0 && stack.getTagCompound().getFloat("Fluid Amount") > 0) {
					Fluid fluidStack = FluidRegistry.getFluid((int) stack.getTagCompound().getFloat("Fluid ID"));
					int fluidAmount = (int) stack.getTagCompound().getFloat("Fluid Amount");

					event.getToolTip().add(TextFormatting.GREEN + "Stored: " + TextFormatting.WHITE + format(fluidAmount) + " mb");
					event.getToolTip().add(TextFormatting.GREEN + "Fluid: " + TextFormatting.WHITE +
							fluidStack.getLocalizedName(new FluidStack(fluidStack, fluidAmount)));
				}
				
				amount = te.getTank().getCapacity();
			}
		}
		
		if (amount > 0) {
			// TODO: make this applicable for fluid pipes!
			String prefix = type == 0 ? "Transfer Rate: " : (type == 1 ? "Generation Rate: " : (type == 4 ? "Max Storage: " : "Burn Rate: "));
			String suffix = type < 3 ? " McU/t" : (type == 4 ? " mb" : "TO_BE_DEFINED");
			event.getToolTip().add(TextFormatting.GREEN + prefix + TextFormatting.WHITE + format(amount) + suffix);
		}
	}
	
}
