/* This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under the terms of the GNU General Public 
* License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Project-Zed is 
* distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
* PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along 
* with Project-Zed. If not, see <http://www.gnu.org/licenses/>
*/
package com.projectzed.mod.handler;

import com.projectzed.mod.container.*;
import com.projectzed.mod.gui.*;
import com.projectzed.mod.tileentity.TileEntityFabricationTable;
import com.projectzed.mod.tileentity.container.TileEntityEnergyBankBase;
import com.projectzed.mod.tileentity.generator.TileEntityFurnaceGenerator;
import com.projectzed.mod.tileentity.generator.TileEntityNuclearController;
import com.projectzed.mod.tileentity.generator.TileEntitySolarArray;
import com.projectzed.mod.tileentity.machine.*;
import cpw.mods.fml.common.network.IGuiHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

/**
 * Class container for handling all gui or container requests.
 * 
 * @author hockeyhurd
 * @version Oct 21, 2014
 */
public class GuiHandler implements IGuiHandler {

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		TileEntity te = world.getTileEntity(x, y, z);

		if (te instanceof TileEntitySolarArray) return new ContainerGenerator(player.inventory, (TileEntitySolarArray) te);
		else if (te instanceof TileEntityFurnaceGenerator) return new ContainerGenerator(player.inventory, (TileEntityFurnaceGenerator) te);
		else if (te instanceof TileEntityNuclearController) return new ContainerGenerator(player.inventory, (TileEntityNuclearController) te);
		else if (te instanceof TileEntityIndustrialFurnace) return new ContainerMachine(player.inventory, (TileEntityIndustrialFurnace) te);
		else if (te instanceof TileEntityIndustrialCrusher) return new ContainerMachine(player.inventory, (TileEntityIndustrialCrusher) te);
		else if (te instanceof TileEntityIndustrialLumberMill) return new ContainerMachine(player.inventory, (TileEntityIndustrialLumberMill) te);
		else if (te instanceof TileEntityIndustrialMetalPress) return new ContainerMachine(player.inventory, (TileEntityIndustrialMetalPress) te);
		else if (te instanceof TileEntityIndustrialCentrifuge) return new ContainerCentrifuge(player.inventory, (TileEntityIndustrialCentrifuge) te);
		else if (te instanceof TileEntityFabricationTable) return new ContainerFabricationTable(player.inventory, (TileEntityFabricationTable) te);
		else if (te instanceof TileEntityRFBridge) return new ContainerRFBridge(player.inventory, (TileEntityRFBridge) te);
		else if (te instanceof TileEntityEnergyBankBase) return new ContainerEnergyContainer(player.inventory, (TileEntityEnergyBankBase) te);

		return null;
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		TileEntity te = world.getTileEntity(x, y, z);

		if (te instanceof TileEntitySolarArray) return new GuiGenerator(player.inventory, (TileEntitySolarArray) te);
		else if (te instanceof TileEntityFurnaceGenerator) return new GuiGenerator(player.inventory, (TileEntityFurnaceGenerator) te);
		else if (te instanceof TileEntityNuclearController) return new GuiGenerator(player.inventory, (TileEntityNuclearController) te);
		else if (te instanceof TileEntityIndustrialFurnace) return new GuiMachine(player.inventory, (TileEntityIndustrialFurnace) te);
		else if (te instanceof TileEntityIndustrialCrusher) return new GuiMachine(player.inventory, (TileEntityIndustrialCrusher) te);
		else if (te instanceof TileEntityIndustrialLumberMill) return new GuiMachine(player.inventory, (TileEntityIndustrialLumberMill) te);
		else if (te instanceof TileEntityIndustrialMetalPress) return new GuiMachine(player.inventory, (TileEntityIndustrialMetalPress) te);
		else if (te instanceof TileEntityIndustrialCentrifuge) return new GuiCentrifuge(player.inventory, (TileEntityIndustrialCentrifuge) te);
		else if (te instanceof TileEntityFabricationTable) return new GuiFabricationTable(player.inventory, (TileEntityFabricationTable) te);
		else if (te instanceof TileEntityRFBridge) return new GuiRFBridge(player.inventory, (TileEntityRFBridge) te);
		else if (te instanceof TileEntityEnergyBankBase) return new GuiEnergyContainer(player.inventory, (TileEntityEnergyBankBase) te);

		return null;
	}

}
