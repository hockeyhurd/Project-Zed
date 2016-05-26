/* This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under the terms of the GNU General Public 
* License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Project-Zed is 
* distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
* PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along 
* with Project-Zed. If not, see <http://www.gnu.org/licenses/>
*/
package com.projectzed.mod.item;

import com.hockeyhurd.hcorelib.api.item.AbstractHCoreItem;
import com.hockeyhurd.hcorelib.api.util.ChatUtils;
import com.hockeyhurd.hcorelib.api.util.NumberFormatter;
import com.projectzed.api.energy.storage.IEnergyContainer;
import com.projectzed.api.fluid.container.IFluidContainer;
import com.projectzed.mod.ProjectZed;
import com.projectzed.mod.tileentity.container.TileEntityEnergyBankBase;
import com.projectzed.mod.tileentity.container.pipe.TileEntityEnergyPipeBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import static net.minecraft.util.text.TextFormatting.*;

/**
 * Class containing code for item mcu reader
 * that when used, can read the amount of energy stored
 * in given {@link IEnergyContainer}.
 * 
 * @author hockeyhurd
 * @version Dec 6, 2014
 */
public class ItemMcUReader extends AbstractHCoreItem {

	public ItemMcUReader() {
		super(ProjectZed.modCreativeTab, "mcuReader", ProjectZed.assetDir);
		this.setCreativeTab(ProjectZed.modCreativeTab);
		this.setMaxStackSize(1);
	}
	
	@Override
	public EnumActionResult onItemUseFirst(ItemStack stack, EntityPlayer player, World world, BlockPos blockPos, EnumFacing side,
			float hitX, float hitY, float hitZ, EnumHand hand) {

		final TileEntity tileEntity = world.getTileEntity(blockPos);
		if (tileEntity instanceof IEnergyContainer) {
			IEnergyContainer cont = (IEnergyContainer) tileEntity;
			boolean full = cont.getEnergyStored() == cont.getMaxStorage();
			
			if (world.isRemote) {
				player.addChatComponentMessage(
						ChatUtils.createComponent(false,
								GOLD + "Stored: " + WHITE + NumberFormatter.format(cont.getEnergyStored()) + " McU" + (full ? " (full)" : "")));
				player.addChatComponentMessage(ChatUtils.createComponent(false,
						GOLD + "Max Storage: " + WHITE + NumberFormatter.format(cont.getMaxStorage()) + " McU"));
				if (cont instanceof TileEntityEnergyBankBase) player.addChatComponentMessage(ChatUtils.createComponent(false,
						GOLD + "Tier: " + WHITE + (((TileEntityEnergyBankBase) cont).getTier() + 1)));
				else if (cont instanceof TileEntityEnergyPipeBase) player.addChatComponentMessage(ChatUtils.createComponent(false,
						GOLD + "Last Received Direction: " + WHITE + ((TileEntityEnergyPipeBase) cont).getLastReceivedDirection()));
			}
			
			else {
				ProjectZed.logHelper.info("Stored: " + NumberFormatter.format(cont.getEnergyStored()) + " McU" + (full ? " (full)" : ""));
				ProjectZed.logHelper.info("Max Storage: "+ NumberFormatter.format(cont.getMaxStorage()) + "McU");
				if (cont instanceof TileEntityEnergyBankBase) ProjectZed.logHelper.info("Tier: " + (((TileEntityEnergyBankBase) cont).getTier() + 1));
				else if (cont instanceof TileEntityEnergyPipeBase) ProjectZed.logHelper.info("Last Received Direction: " + ((TileEntityEnergyPipeBase) cont).getLastReceivedDirection());
			}

		}
		
		else if (tileEntity instanceof IFluidContainer) {
			IFluidContainer cont = (IFluidContainer) tileEntity;
			if (world.isRemote) {
				player.addChatComponentMessage(ChatUtils.createComponent(false, AQUA + "Stored: " + cont.getTank().getFluidAmount()));
			}
			
			else ProjectZed.logHelper.info("Stored: " + cont.getTank().getFluidAmount());
		}
		
		return EnumActionResult.SUCCESS;
	}

}
