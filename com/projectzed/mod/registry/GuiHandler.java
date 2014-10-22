package com.projectzed.mod.registry;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import com.projectzed.mod.gui.ContainerGenerator;
import com.projectzed.mod.gui.GuiGenerator;
import com.projectzed.mod.tileentity.generator.TileEntitySolarArray;

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

		return null;
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		TileEntity te = world.getTileEntity(x, y, z);

		if (te instanceof TileEntitySolarArray) return new GuiGenerator(player.inventory, (TileEntitySolarArray) te);
		
		return null;
	}

}
