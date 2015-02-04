/* This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under the terms of the GNU General Public 
* License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Project-Zed is 
* distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
* PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along 
* with Project-Zed. If not, see <http://www.gnu.org/licenses/>
*/
package com.projectzed.mod.handler;

import static com.hockeyhurd.api.util.NumberFormatter.format;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;

import com.projectzed.api.block.AbstractBlockContainer;
import com.projectzed.api.block.AbstractBlockFluidContainer;
import com.projectzed.api.block.AbstractBlockGenerator;
import com.projectzed.api.block.AbstractBlockMachine;
import com.projectzed.api.block.AbstractBlockPipe;
import com.projectzed.api.tileentity.container.AbstractTileEntityFluidContainer;
import com.projectzed.mod.block.container.AbstractBlockEnergyPipeBase;
import com.projectzed.mod.block.container.BlockEnergyCell;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;

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
		ItemStack stack = event.itemStack;
		if (stack != null) {
			int amount = 0;
			int type = 0;
			Block b = Block.getBlockFromItem(stack.getItem());
			if (b != null) {
				if (b instanceof AbstractBlockContainer) {
					type = 0;
					if (b instanceof BlockEnergyCell && ((BlockEnergyCell) b).getTileEntity() != null) event.toolTip.add(EnumChatFormatting.GREEN + "Capacity: " + EnumChatFormatting.WHITE + format(((BlockEnergyCell) b).getTileEntity().getMaxStorage()) + " McU");
					amount = ((AbstractBlockContainer) b).getTileEntity().getMaxExportRate();
				}
				
				else if (b instanceof AbstractBlockPipe) {
					if (b instanceof AbstractBlockEnergyPipeBase) {
						type = 0;
						amount = ((AbstractBlockEnergyPipeBase) b).getTileEntity().getMaxExportRate();
					}

					else {
						type = 3;
						amount = ((AbstractBlockPipe) b).getTileEntity().getMaxExportRate();
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
					if (te.getTank().getFluidAmount() > 0 && te.getTank().getFluid() != null) {
						event.toolTip.add(EnumChatFormatting.GREEN + "Stored: " + EnumChatFormatting.WHITE + format(te.getTank().getFluidAmount()) + " mb");
						event.toolTip.add(EnumChatFormatting.GREEN + "Fluid: " + EnumChatFormatting.WHITE + te.getTank().getFluid().getLocalizedName() + " mb");
					}
					
					amount = te.getTank().getCapacity();
				}
			}
			
			if (amount > 0) {
				String prefix = type == 0 ? "Transfer Rate: " : (type == 1 ? "Generation Rate: " : (type == 4 ? "Stored: " : "Burn Rate: "));
				String suffix = type < 3 ? " McU/t" : (type == 4 ? " mb" : "TO_BE_DEFINED");
				event.toolTip.add(EnumChatFormatting.GREEN + prefix + EnumChatFormatting.WHITE + format(amount) + suffix);
			}
		}
	}
	
}
