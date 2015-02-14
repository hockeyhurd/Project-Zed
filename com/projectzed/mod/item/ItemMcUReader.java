/* This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under the terms of the GNU General Public 
* License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Project-Zed is 
* distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
* PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along 
* with Project-Zed. If not, see <http://www.gnu.org/licenses/>
*/
package com.projectzed.mod.item;

import static net.minecraft.util.EnumChatFormatting.GOLD;
import static net.minecraft.util.EnumChatFormatting.WHITE;
import static net.minecraft.util.EnumChatFormatting.AQUA;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import com.hockeyhurd.api.item.AbstractItemMetalic;
import com.hockeyhurd.api.util.ChatHelper;
import com.hockeyhurd.api.util.NumberFormatter;
import com.projectzed.api.energy.storage.IEnergyContainer;
import com.projectzed.mod.ProjectZed;
import com.projectzed.mod.tileentity.container.TileEntityEnergyBankBase;
import com.projectzed.mod.tileentity.container.pipe.TileEntityEnergyPipeBase;
import com.projectzed.mod.tileentity.container.pipe.TileEntityLiquiductBase;

/**
 * Class containing code for item mcu reader
 * that when used, can read the amount of energy stored
 * in given {@link IEnergyContainer}.
 * 
 * @author hockeyhurd
 * @version Dec 6, 2014
 */
public class ItemMcUReader extends AbstractItemMetalic {

	private final ChatHelper chatHelper;
	
	/**
	 * @param name
	 * @param assetDir
	 */
	public ItemMcUReader() {
		super("mcuReader", ProjectZed.assetDir);
		this.setCreativeTab(ProjectZed.modCreativeTab);
		chatHelper = new ChatHelper();
	}
	
	@Override
	public boolean onItemUseFirst(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
		if (world.getTileEntity(x, y, z) instanceof IEnergyContainer) {
			IEnergyContainer cont = (IEnergyContainer) world.getTileEntity(x, y, z);
			boolean full = cont.getEnergyStored() == cont.getMaxStorage();
			
			if (world.isRemote) {
				player.addChatComponentMessage(chatHelper.comp(GOLD + "Stored: " + WHITE + NumberFormatter.format(cont.getEnergyStored()) + " McU" + (full ? " (full)" : "")));
				player.addChatComponentMessage(chatHelper.comp(GOLD + "Max Storage: " + WHITE + NumberFormatter.format(cont.getMaxStorage()) + "McU"));
				if (cont instanceof TileEntityEnergyBankBase) player.addChatComponentMessage(chatHelper.comp(GOLD + "Tier: " + WHITE + (((TileEntityEnergyBankBase) cont).getTier() + 1)));
				else if (cont instanceof TileEntityEnergyPipeBase) player.addChatComponentMessage(chatHelper.comp(GOLD + "Last Received Direction: " + WHITE + ((TileEntityEnergyPipeBase) cont).getLastReceivedDirection()));
				else if (cont instanceof TileEntityLiquiductBase) player.addChatComponentMessage(chatHelper.comp(AQUA + "Stored: " + ((TileEntityLiquiductBase) cont).getTank().getFluidAmount()));
			}
			
			else {
				ProjectZed.logHelper.info("Stored: " + NumberFormatter.format(cont.getEnergyStored()) + " McU" + (full ? " (full)" : ""));
				ProjectZed.logHelper.info("Max Storage: "+ NumberFormatter.format(cont.getMaxStorage()) + "McU");
				if (cont instanceof TileEntityEnergyBankBase) ProjectZed.logHelper.info("Tier: " + (((TileEntityEnergyBankBase) cont).getTier() + 1));
				else if (cont instanceof TileEntityEnergyPipeBase) ProjectZed.logHelper.info("Last Received Direction: " + ((TileEntityEnergyPipeBase) cont).getLastReceivedDirection());
			}

		}
		
		return true;
	}

}
