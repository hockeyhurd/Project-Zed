package com.projectzed.mod.handler;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import com.projectzed.mod.container.ContainerEnergyContainer;
import com.projectzed.mod.container.ContainerFabricationTable;
import com.projectzed.mod.container.ContainerGenerator;
import com.projectzed.mod.container.ContainerMachine;
import com.projectzed.mod.container.ContainerRFBridge;
import com.projectzed.mod.gui.GuiEnergyContainer;
import com.projectzed.mod.gui.GuiFabricationTable;
import com.projectzed.mod.gui.GuiGenerator;
import com.projectzed.mod.gui.GuiMachine;
import com.projectzed.mod.gui.GuiRFBridge;
import com.projectzed.mod.tileentity.TileEntityFabricationTable;
import com.projectzed.mod.tileentity.container.TileEntityEnergyBankBase;
import com.projectzed.mod.tileentity.generator.TileEntityFurnaceGenerator;
import com.projectzed.mod.tileentity.generator.TileEntityNuclear;
import com.projectzed.mod.tileentity.generator.TileEntitySolarArray;
import com.projectzed.mod.tileentity.machine.TileEntityIndustrialCrusher;
import com.projectzed.mod.tileentity.machine.TileEntityIndustrialFurnace;
import com.projectzed.mod.tileentity.machine.TileEntityIndustrialLumberMill;
import com.projectzed.mod.tileentity.machine.TileEntityRFBridge;

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
		else if (te instanceof TileEntityNuclear) return new ContainerGenerator(player.inventory, (TileEntityNuclear) te);
		else if (te instanceof TileEntityIndustrialFurnace) return new ContainerMachine(player.inventory, (TileEntityIndustrialFurnace) te);
		else if (te instanceof TileEntityIndustrialCrusher) return new ContainerMachine(player.inventory, (TileEntityIndustrialCrusher) te);
		else if (te instanceof TileEntityIndustrialLumberMill) return new ContainerMachine(player.inventory, (TileEntityIndustrialLumberMill) te);
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
		else if (te instanceof TileEntityNuclear) return new GuiGenerator(player.inventory, (TileEntityNuclear) te);
		else if (te instanceof TileEntityIndustrialFurnace) return new GuiMachine(player.inventory, (TileEntityIndustrialFurnace) te);
		else if (te instanceof TileEntityIndustrialCrusher) return new GuiMachine(player.inventory, (TileEntityIndustrialCrusher) te);
		else if (te instanceof TileEntityIndustrialLumberMill) return new GuiMachine(player.inventory, (TileEntityIndustrialLumberMill) te);
		else if (te instanceof TileEntityFabricationTable) return new GuiFabricationTable(player.inventory, (TileEntityFabricationTable) te);
		else if (te instanceof TileEntityRFBridge) return new GuiRFBridge(player.inventory, (TileEntityRFBridge) te);
		else if (te instanceof TileEntityEnergyBankBase) return new GuiEnergyContainer(player.inventory, (TileEntityEnergyBankBase) te);

		return null;
	}

}
