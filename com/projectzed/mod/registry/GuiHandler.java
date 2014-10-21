package com.projectzed.mod.registry;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import cpw.mods.fml.common.network.IGuiHandler;

/**
 * Class container for handling all gui or container requests.
 * 
 * @author hockeyhurd
 * @version Oct 21, 2014
 */
public class GuiHandler implements IGuiHandler {

	
	
	public GuiHandler() {
	}

	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		return null;
	}

	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		return null;
	}

}
