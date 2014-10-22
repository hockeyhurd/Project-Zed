package com.projectzed.mod.registry;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import com.projectzed.mod.gui.ContainerGenerator;
import com.projectzed.mod.gui.GuiGenerator;
import com.projectzed.mod.handler.ServerClientSync;
import com.projectzed.mod.tileentity.generator.TileEntitySolarArray;

import cpw.mods.fml.common.network.IGuiHandler;

/**
 * Class container for handling all gui or container requests.
 * 
 * @author hockeyhurd
 * @version Oct 21, 2014
 */
public class GuiHandler implements IGuiHandler {
	
	private ServerClientSync solarSync = new ServerClientSync(true);;
	
	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		TileEntity te = world.getTileEntity(x, y, z);
		
		if (te instanceof TileEntitySolarArray) {
			TileEntitySolarArray te2 = (TileEntitySolarArray) te;
			solarSync.setServerPacket(te2.getEnergyStored());
			solarSync.resolve();
			te2.setEnergyStored(solarSync.getServerPacket());
			return new ContainerGenerator(player.inventory, (TileEntitySolarArray) te2);
		}
		return null;
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		TileEntity te = world.getTileEntity(x, y, z);
		
		if (te instanceof TileEntitySolarArray) {
			TileEntitySolarArray te2 = (TileEntitySolarArray) te;
			solarSync.setClientPacket(te2.getEnergyStored());
			solarSync.resolve();
			te2.setEnergyStored(solarSync.getClientPacket());
			return new GuiGenerator(player.inventory, te2);
		}
		return null;
	}

}
