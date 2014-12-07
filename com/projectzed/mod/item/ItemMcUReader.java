package com.projectzed.mod.item;

import java.text.DecimalFormat;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import com.hockeyhurd.api.item.AbstractItemMetalic;
import com.hockeyhurd.api.util.ChatHelper;
import com.projectzed.api.energy.storage.IEnergyContainer;
import com.projectzed.mod.ProjectZed;

/**
 * Class containing code for item mcu reader
 * that when used, can read the amount of energy stored
 * in given {@link IEnergyContainer}.
 * 
 * @author hockeyhurd
 * @version Dec 6, 2014
 */
public class ItemMcUReader extends AbstractItemMetalic {

	private final DecimalFormat DF = new DecimalFormat("###,###.##");
	
	/**
	 * @param name
	 * @param assetDir
	 */
	public ItemMcUReader() {
		super("mcuReader", ProjectZed.assetDir);
		this.setCreativeTab(ProjectZed.modCreativeTab);
	}
	
	@Override
	public boolean onItemUseFirst(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
		if (world.isRemote && world.getTileEntity(x, y, z) instanceof IEnergyContainer) {
			IEnergyContainer cont = (IEnergyContainer) world.getTileEntity(x, y, z);
			boolean full = cont.getEnergyStored() == cont.getMaxStorage();
			
			player.addChatComponentMessage(new ChatHelper().comp("Stored: " + DF.format(cont.getEnergyStored()) + " McU" + (full ? " (full)" : "")));
		}
		
		return true;
	}

}
