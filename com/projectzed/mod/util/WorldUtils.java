/* This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under the terms of the GNU General Public 
* License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Project-Zed is 
* distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
* PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along 
* with Project-Zed. If not, see <http://www.gnu.org/licenses/>
*/
package com.projectzed.mod.util;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import com.projectzed.api.block.AbstractBlockContainer;
import com.projectzed.api.block.AbstractBlockNuclearComponent;
import com.projectzed.api.tileentity.AbstractTileEntityGeneric;
import com.projectzed.api.tileentity.IMultiBlockable;
import com.projectzed.mod.ProjectZed;

/**
 * Class containing code for general purpose code in the mc world. <br>
 * <bold>NOTE:</bold> This class should/will be mostly static.
 * 
 * @author hockeyhurd
 * @version Dec 22, 2014
 */
public class WorldUtils {

	private static Random random = new Random();
	
	private WorldUtils() {
	}

	/**
	 * Creates an EntityItem object from given parameters.
	 * 
	 * @param world = world object as reference.
	 * @param x = x-pos.
	 * @param y = y-pos.
	 * @param z = z-pos
	 * @return EntityItem object.
	 */
	public static EntityItem createEntityItem(World world, int x, int y, int z) {
		return new EntityItem(world, x, y, z);
	}

	/**
	 * Creates an EntityItem object from given parameters.
	 * 
	 * @param world = world object as reference.
	 * @param x = x-pos.
	 * @param y = y-pos.
	 * @param z = z-pos
	 * @param stack = Itemstack to create from.
	 * @return EntityItem object.
	 */
	public static EntityItem createEntityItemStack(World world, int x, int y, int z, ItemStack stack) {
		return new EntityItem(world, x, y, z, stack);
	}

	/**
	 * Method used to add drop items to world more efficiently.
	 * 
	 * @param stacks = stack array object to drop.
	 * @param world = world object as reference.
	 * @param x = x-pos.
	 * @param y = y-pos.
	 * @param z = z-pos
	 */
	public static void addItemDrop(ItemStack stack, World world, int x, int y, int z) {
		addItemDrop(stack, world, x, y, z, null);
	}

	/**
	 * Method used to add drop items to world more efficiently.
	 * 
	 * @param stack = stack object to drop.
	 * @param world = world object as reference.
	 * @param x = x-pos.
	 * @param y = y-pos.
	 * @param z = z-pos
	 * @param random = random object to use.
	 */
	public static void addItemDrop(ItemStack stack, World world, int x, int y, int z, Random random) {
		addItemDrop(new ItemStack[] {
			stack
		}, world, x, y, z, random);
	}

	/**
	 * Method used to add drop items to world more efficiently.
	 * @param stacks = stack array object to drop.
	 * @param world = world object as reference.
	 * @param x = x-pos.
	 * @param y = y-pos.
	 * @param z = z-pos
	 */
	public static void addItemDrop(ItemStack[] stacks, World world, int x, int y, int z) {
		addItemDrop(stacks, world, x, y, z, null);
	}

	/**
	 * Method used to add drop items to world more efficiently.
	 * 
	 * @param stacks = stack array object to drop.
	 * @param world = world object as reference.
	 * @param x = x-pos.
	 * @param y = y-pos.
	 * @param z = z-pos
	 * @param random = random object to use.
	 */
	public static void addItemDrop(ItemStack[] stacks, World world, int x, int y, int z, Random random) {
		if (stacks == null || stacks.length == 0 || world == null || world.isRemote) {
			ProjectZed.logHelper.severe("Error attempting to add item drops to world!");
			return;
		}

		for (ItemStack stack : stacks) {
			if (stack != null) {
				if (random == null) world.spawnEntityInWorld(createEntityItemStack(world, x, y, z, stack));
				else world.spawnEntityInWorld(createEntityItemStack(world, x + random.nextInt(3), y + random.nextInt(3), z + random.nextInt(3), stack));
			}
		}
	}

	/**
	 * Method used to drop all contents in container on break.
	 * 
	 * @param te = te object to perform item drop on.
	 */
	public static void dropItemsFromContainerOnBreak(AbstractTileEntityGeneric te) {
		
		if (te != null && te.getSizeInvenotry() > 0) {
			ItemStack[] drops = new ItemStack[te.getSizeInvenotry()];
			
			for (int i = 0; i < drops.length; i++) {
				drops[i] = te.getStackInSlot(i);
			}
			
			addItemDrop(drops, te.getWorldObj(), te.xCoord, te.yCoord, te.zCoord, random);
		}
	}
	
	/**
	 * Function to create a fake instance of IMultiBlockable TE.
	 * @param block block to reference.
	 * @return object if valid, else returns false.
	 */
	public static IMultiBlockable<?> createFakeTE(Block block) {
		IMultiBlockable<?> mb = null;
		
		if (block != null && block != Blocks.air) {
			if (block instanceof AbstractBlockNuclearComponent && ((AbstractBlockNuclearComponent) block).getTileEntity() instanceof IMultiBlockable<?>) {
				mb = (IMultiBlockable<?>) ((AbstractBlockNuclearComponent) block).getTileEntity();
			}
			
			else if (block instanceof AbstractBlockContainer && ((AbstractBlockContainer) block).getTileEntity() instanceof IMultiBlockable<?>) {
				mb = (IMultiBlockable<?>) ((AbstractBlockContainer) block).getTileEntity();
			}
		}
		
		return mb;
	}

}
