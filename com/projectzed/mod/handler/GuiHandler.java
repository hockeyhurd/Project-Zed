/* This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under the terms of the GNU General Public 
* License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Project-Zed is 
* distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
* PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along 
* with Project-Zed. If not, see <http://www.gnu.org/licenses/>
*/
package com.projectzed.mod.handler;

import com.hockeyhurd.hcorelib.api.math.VectorHelper;
import com.projectzed.mod.container.*;
import com.projectzed.mod.gui.*;
import com.projectzed.mod.tileentity.TileEntityFabricationTable;
import com.projectzed.mod.tileentity.container.TileEntityEnergyBankBase;
import com.projectzed.mod.tileentity.container.TileEntityNuclearIOPort;
import com.projectzed.mod.tileentity.container.TileEntityRFBridge;
import com.projectzed.mod.tileentity.digger.TileEntityIndustrialQuarry;
import com.projectzed.mod.tileentity.generator.*;
import com.projectzed.mod.tileentity.machine.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

/**
 * Class container for handling all gui or container requests.
 * 
 * @author hockeyhurd
 * @version Oct 21, 2014
 */
public class GuiHandler implements IGuiHandler {

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		TileEntity te = world.getTileEntity(VectorHelper.toBlockPos(x, y, z));

		if (te instanceof TileEntitySolarArray) return new ContainerGenerator(player.inventory, (TileEntitySolarArray) te);
		else if (te instanceof TileEntityFurnaceGenerator) return new ContainerGenerator(player.inventory, (TileEntityFurnaceGenerator) te);
		else if (te instanceof TileEntityLavaGenerator) return new ContainerGenerator(player.inventory, (TileEntityLavaGenerator) te);
		else if (te instanceof TileEntityPetrolGenerator) return new ContainerGenerator(player.inventory, (TileEntityPetrolGenerator) te);
		else if (te instanceof TileEntityNuclearController) return new ContainerGenerator(player.inventory, (TileEntityNuclearController) te);
		else if (te instanceof TileEntityIndustrialFurnace) return new ContainerMachine(player.inventory, (TileEntityIndustrialFurnace) te);
		else if (te instanceof TileEntityIndustrialCrusher) return new ContainerMachine(player.inventory, (TileEntityIndustrialCrusher) te);
		else if (te instanceof TileEntityIndustrialLumberMill) return new ContainerMachine(player.inventory, (TileEntityIndustrialLumberMill) te);
		else if (te instanceof TileEntityIndustrialMetalPress) return new ContainerMachine(player.inventory, (TileEntityIndustrialMetalPress) te);
		else if (te instanceof TileEntityIndustrialCentrifuge) return new ContainerCentrifuge(player.inventory, (TileEntityIndustrialCentrifuge) te);
		else if (te instanceof TileEntityIndustrialEnergizer) return new ContainerMachine(player.inventory, (TileEntityIndustrialEnergizer) te);
		else if (te instanceof TileEntityIndustrialLoader) return new ContainerLoader(player.inventory, (TileEntityIndustrialLoader) te);
		else if (te instanceof TileEntityIndustrialPlanter) return new ContainerPlanter(player.inventory, (TileEntityIndustrialPlanter) te);
		else if (te instanceof TileEntityIndustrialHarvester) return new ContainerHarvester(player.inventory, (TileEntityIndustrialHarvester) te);
		else if (te instanceof TileEntityIndustrialStorageUnit) return new ContainerMachine(player.inventory, (TileEntityIndustrialStorageUnit) te);
		else if (te instanceof TileEntityFabricationTable) return new ContainerFabricationTable(player.inventory, (TileEntityFabricationTable) te);
		else if (te instanceof TileEntityStoneCraftingTable) return new ContainerStoneCraftingTable(player.inventory, (TileEntityStoneCraftingTable) te);
		else if (te instanceof TileEntityPatternEncoder) return new ContainerPatternEncoder(player.inventory, (TileEntityPatternEncoder) te);
		else if (te instanceof TileEntityRFBridge) return new ContainerRFBridge(player.inventory, (TileEntityRFBridge) te);
		else if (te instanceof TileEntityEnergyBankBase) return new ContainerEnergyContainer(player.inventory, (TileEntityEnergyBankBase) te);

		else if (te instanceof TileEntityNuclearIOPort) return new ContainerNuclearIOPort(player.inventory, (TileEntityNuclearIOPort) te);
		else if (te instanceof TileEntityIndustrialQuarry) return new ContainerDigger(player.inventory, (TileEntityIndustrialQuarry) te);
		
		return null;
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		TileEntity te = world.getTileEntity(VectorHelper.toBlockPos(x, y, z));

		if (te instanceof TileEntitySolarArray) return new GuiGenerator(player.inventory, (TileEntitySolarArray) te);
		else if (te instanceof TileEntityFurnaceGenerator) return new GuiGenerator(player.inventory, (TileEntityFurnaceGenerator) te);
		else if (te instanceof TileEntityLavaGenerator) return new GuiLavaGen(player.inventory, (TileEntityLavaGenerator) te);
		else if (te instanceof TileEntityPetrolGenerator) return new GuiPetrolGen(player.inventory, (TileEntityPetrolGenerator) te);
		else if (te instanceof TileEntityNuclearController) return new GuiNuclearController(player.inventory, (TileEntityNuclearController) te);
		else if (te instanceof TileEntityIndustrialFurnace) return new GuiMachine(player.inventory, (TileEntityIndustrialFurnace) te);
		else if (te instanceof TileEntityIndustrialCrusher) return new GuiMachine(player.inventory, (TileEntityIndustrialCrusher) te);
		else if (te instanceof TileEntityIndustrialLumberMill) return new GuiMachine(player.inventory, (TileEntityIndustrialLumberMill) te);
		else if (te instanceof TileEntityIndustrialMetalPress) return new GuiMachine(player.inventory, (TileEntityIndustrialMetalPress) te);
		else if (te instanceof TileEntityIndustrialCentrifuge) return new GuiCentrifuge(player.inventory, (TileEntityIndustrialCentrifuge) te);
		else if (te instanceof TileEntityIndustrialEnergizer) return new GuiMachine(player.inventory, (TileEntityIndustrialEnergizer) te);
		else if (te instanceof TileEntityIndustrialLoader) return new GuiLoader(player.inventory, (TileEntityIndustrialLoader) te);
		else if (te instanceof TileEntityIndustrialPlanter) return new GuiPlanter(player.inventory, (TileEntityIndustrialPlanter) te);
		else if (te instanceof TileEntityIndustrialHarvester) return new GuiHarvester(player.inventory, (TileEntityIndustrialHarvester) te);
		else if (te instanceof TileEntityIndustrialStorageUnit) return new GuiStorageUnit(player.inventory, (TileEntityIndustrialStorageUnit) te);
		else if (te instanceof TileEntityFabricationTable) return new GuiFabricationTable(player.inventory, (TileEntityFabricationTable) te);
		else if (te instanceof TileEntityStoneCraftingTable) return new GuiStoneCraftingTable(player.inventory, (TileEntityStoneCraftingTable) te);
		else if (te instanceof TileEntityPatternEncoder) return new GuiPatternEncoder(player.inventory, (TileEntityPatternEncoder) te);
		else if (te instanceof TileEntityRFBridge) return new GuiRFBridge(player.inventory, (TileEntityRFBridge) te);
		else if (te instanceof TileEntityEnergyBankBase) return new GuiEnergyContainer(player.inventory, (TileEntityEnergyBankBase) te);
		
		else if (te instanceof TileEntityNuclearIOPort) return new GuiNuclearIOPort(player.inventory, (TileEntityNuclearIOPort) te);
		else if (te instanceof TileEntityIndustrialQuarry) return new GuiQuarry(player.inventory, (TileEntityIndustrialQuarry) te);

		return null;
	}

}
