/* This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under the terms of the GNU General Public 
* License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Project-Zed is 
* distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
* PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along 
* with Project-Zed. If not, see <http://www.gnu.org/licenses/>
*/
package com.projectzed.mod.handler;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import com.projectzed.mod.container.ContainerCentrifuge;
import com.projectzed.mod.container.ContainerEnergyContainer;
import com.projectzed.mod.container.ContainerFabricationTable;
import com.projectzed.mod.container.ContainerGenerator;
import com.projectzed.mod.container.ContainerMachine;
import com.projectzed.mod.container.ContainerNuclearIOPort;
import com.projectzed.mod.container.ContainerRFBridge;
import com.projectzed.mod.container.ContainerStoneCraftingTable;
import com.projectzed.mod.gui.GuiCentrifuge;
import com.projectzed.mod.gui.GuiEnergyContainer;
import com.projectzed.mod.gui.GuiFabricationTable;
import com.projectzed.mod.gui.GuiGenerator;
import com.projectzed.mod.gui.GuiLoader;
import com.projectzed.mod.gui.GuiMachine;
import com.projectzed.mod.gui.GuiNuclearController;
import com.projectzed.mod.gui.GuiNuclearIOPort;
import com.projectzed.mod.gui.GuiRFBridge;
import com.projectzed.mod.gui.GuiStoneCraftingTable;
import com.projectzed.mod.tileentity.TileEntityFabricationTable;
import com.projectzed.mod.tileentity.container.TileEntityEnergyBankBase;
import com.projectzed.mod.tileentity.container.TileEntityNuclearIOPort;
import com.projectzed.mod.tileentity.generator.TileEntityFurnaceGenerator;
import com.projectzed.mod.tileentity.generator.TileEntityNuclearController;
import com.projectzed.mod.tileentity.generator.TileEntitySolarArray;
import com.projectzed.mod.tileentity.machine.TileEntityIndustrialCentrifuge;
import com.projectzed.mod.tileentity.machine.TileEntityIndustrialCrusher;
import com.projectzed.mod.tileentity.machine.TileEntityIndustrialEnergizer;
import com.projectzed.mod.tileentity.machine.TileEntityIndustrialFurnace;
import com.projectzed.mod.tileentity.machine.TileEntityIndustrialLoader;
import com.projectzed.mod.tileentity.machine.TileEntityIndustrialLumberMill;
import com.projectzed.mod.tileentity.machine.TileEntityIndustrialMetalPress;
import com.projectzed.mod.tileentity.machine.TileEntityRFBridge;
import com.projectzed.mod.tileentity.machine.TileEntityStoneCraftingTable;

import cpw.mods.fml.common.network.IGuiHandler;

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
		else if (te instanceof TileEntityIndustrialEnergizer) return new ContainerMachine(player.inventory, (TileEntityIndustrialEnergizer) te);
		else if (te instanceof TileEntityIndustrialLoader) return new ContainerMachine(player.inventory, (TileEntityIndustrialLoader) te);
		else if (te instanceof TileEntityFabricationTable) return new ContainerFabricationTable(player.inventory, (TileEntityFabricationTable) te);
		else if (te instanceof TileEntityStoneCraftingTable) return new ContainerStoneCraftingTable(player.inventory, (TileEntityStoneCraftingTable) te);
		else if (te instanceof TileEntityRFBridge) return new ContainerRFBridge(player.inventory, (TileEntityRFBridge) te);
		else if (te instanceof TileEntityEnergyBankBase) return new ContainerEnergyContainer(player.inventory, (TileEntityEnergyBankBase) te);

		else if (te instanceof TileEntityNuclearIOPort) return new ContainerNuclearIOPort(player.inventory, (TileEntityNuclearIOPort) te);
		
		return null;
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		TileEntity te = world.getTileEntity(x, y, z);

		if (te instanceof TileEntitySolarArray) return new GuiGenerator(player.inventory, (TileEntitySolarArray) te);
		else if (te instanceof TileEntityFurnaceGenerator) return new GuiGenerator(player.inventory, (TileEntityFurnaceGenerator) te);
		else if (te instanceof TileEntityNuclearController) return new GuiNuclearController(player.inventory, (TileEntityNuclearController) te);
		else if (te instanceof TileEntityIndustrialFurnace) return new GuiMachine(player.inventory, (TileEntityIndustrialFurnace) te);
		else if (te instanceof TileEntityIndustrialCrusher) return new GuiMachine(player.inventory, (TileEntityIndustrialCrusher) te);
		else if (te instanceof TileEntityIndustrialLumberMill) return new GuiMachine(player.inventory, (TileEntityIndustrialLumberMill) te);
		else if (te instanceof TileEntityIndustrialMetalPress) return new GuiMachine(player.inventory, (TileEntityIndustrialMetalPress) te);
		else if (te instanceof TileEntityIndustrialCentrifuge) return new GuiCentrifuge(player.inventory, (TileEntityIndustrialCentrifuge) te);
		else if (te instanceof TileEntityIndustrialEnergizer) return new GuiMachine(player.inventory, (TileEntityIndustrialEnergizer) te);
		else if (te instanceof TileEntityIndustrialLoader) return new GuiLoader(player.inventory, (TileEntityIndustrialLoader) te);
		else if (te instanceof TileEntityFabricationTable) return new GuiFabricationTable(player.inventory, (TileEntityFabricationTable) te);
		else if (te instanceof TileEntityStoneCraftingTable) return new GuiStoneCraftingTable(player.inventory, (TileEntityStoneCraftingTable) te);
		else if (te instanceof TileEntityRFBridge) return new GuiRFBridge(player.inventory, (TileEntityRFBridge) te);
		else if (te instanceof TileEntityEnergyBankBase) return new GuiEnergyContainer(player.inventory, (TileEntityEnergyBankBase) te);
		
		else if (te instanceof TileEntityNuclearIOPort) return new GuiNuclearIOPort(player.inventory, (TileEntityNuclearIOPort) te);

		return null;
	}

}
