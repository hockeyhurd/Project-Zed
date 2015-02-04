/* This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under the terms of the GNU General Public 
* License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Project-Zed is 
* distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
* PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along 
* with Project-Zed. If not, see <http://www.gnu.org/licenses/>
*/
package com.projectzed.mod.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;

import com.hockeyhurd.api.math.Vector4Helper;
import com.projectzed.mod.ProjectZed;
import com.projectzed.mod.tileentity.generator.TileEntityNuclear;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * Class containing block code for nuclearChamberLock.
 * 
 * @author hockeyhurd
 * @version Dec 14, 2014
 */
public class BlockNuclearChamberLock extends Block {

	private boolean mulitBlockMode;
	
	public BlockNuclearChamberLock() {
		super(Material.rock);
		this.setBlockName("nuclearChamberLock");
		this.setHardness(1.0f);
		this.setCreativeTab(ProjectZed.modCreativeTab);
	}

	@SideOnly(Side.CLIENT) 
	public void registerBlockIcons(IIconRegister reg) {
		blockIcon = reg.registerIcon(ProjectZed.assetDir + "nuclearChamberLock");
	}
	
	/*
	 * (non-Javadoc)
	 * @see net.minecraft.block.Block#onBlockAdded(net.minecraft.world.World, int, int, int)
	 */
	@Override
	public void onBlockAdded(World world, int x, int y, int z) {
		updateMultiBlock(world, x, y, z);
	}
	
	/*
	 * (non-Javadoc)
	 * @see net.minecraft.block.Block#onNeighborBlockChange(net.minecraft.world.World, int, int, int, net.minecraft.block.Block)
	 */
	@Override
	public void onNeighborBlockChange(World world, int x, int y, int z, Block neighborBlock) {
		updateMultiBlock(world, x, y, z);
	}
	
	/**
	 * Method used to update the multiblock structure if applicable.
	 * 
	 * @param world = world object as reference.
	 * @param x = x-position.
	 * @param y = y-position.
	 * @param z = z-position.
	 */
	public void updateMultiBlock(World world, int x, int y, int z) { 
		this.mulitBlockMode = isMultiBlockStructureCheck(world, x, y, z);
	}
	
	/**
	 * @param b = block as reference to compare to.
	 * @return true if valid, else returns false.
	 */
	private boolean isBlockValid(Block b) {
		return b != null && b != Blocks.air && (b == ProjectZed.nuclearChamberWall || b == ProjectZed.thickenedGlass); 
	}
	
	/**
	 * @return calculated result of whether is multiblock or nah.
	 */
	public boolean isMultiBlockStructure() {
		return this.mulitBlockMode;
	}
	
	/**
	 * Function to check if multiblock structure is created.
	 * @return true if multiblock structure, else returns false.
	 */
	private boolean isMultiBlockStructureCheck(World world, int x, int y, int z) {
		
		// Tracking variables.
		boolean flag = false;
		boolean containsOneController;
		boolean containsOneCore;
		int counterContoller = 0;
		int counterCore = 0;
		Vector4Helper<Integer> controllerVec = null;
		Vector4Helper<Integer> coreVec = null;
		
		// Quick check to make sure how many controllers and cores are in the 'system'.
		for (int yy = 0; yy < 4; yy++) {
			for (int xx = 0; xx < 4; xx++) {
				for (int zz = 0; zz < 4; zz++) {
					if (world.getBlock(x - xx, y + yy, z - zz) == ProjectZed.fissionController || world.getBlock(x - xx, y + yy, z - zz) == ProjectZed.fusionController) {
						counterContoller++;
						controllerVec = new Vector4Helper<Integer>(x - xx, y + yy, z - zz);
					}
					
					else if (world.getBlock(x - xx, y + yy, z - zz) == ProjectZed.nuclearReactantCore) {
						counterCore++;
						coreVec = new Vector4Helper<Integer>(x - xx, y + yy, z - zz);
					}
				}
			}
		}
		
		// Set boolean variables appropriately.
		containsOneController = counterContoller == 1;
		containsOneCore = counterCore == 1;
		
		// If the system doesn't have atleast 1 core and 1 controller, return false.
		if (!containsOneController || !containsOneCore || controllerVec == null) return false;
		
		// Else multiblock check.
		else {
			
			// Tracking variable for whether a part of the check had failed.
			boolean current = true;

			// Tracking current vector coordinate.
			Vector4Helper<Integer> currentVec = null;
			
			// Size of system which we get from our controller.
			int size = ((TileEntityNuclear) world.getTileEntity(controllerVec.x, controllerVec.y, controllerVec.z)).getChamberSize();
			size--;
			for (int yy = 0; yy < size; yy++) {
				for (int xx = 0; xx < size; xx++) {
					for (int zz = 0; zz < size; zz++) {
						
						// If top or bottom of multiblock, make sure these locks are in correct place.
						if (yy == 0 || yy == size) {
							
							// Check each corner.
							if ((xx == 0 && zz == 0) || (xx == 0 && zz == size) || (xx == size && zz == 0) || (xx == size && zz == size)) {
								if (world.getBlock(x - xx, y + yy, z - zz) != this) {
									current = false;
									break;
								}
							}
							
							// Repeat of above in a sense but is cleaner looking.
							else if (!isBlockValid(world.getBlock(x - xx, y + yy, z - zz))) {
								System.out.println("Block " + world.getBlock(x - xx, y + yy, z - zz).getUnlocalizedName() + " failed @:");
								System.out.println((x - xx) + ", " + (y + yy) + ", " + (z - zz));
								current = false;
								break;
							}
						}

						// Check all other y-levels for valid blocks.
						else {
							currentVec = new Vector4Helper<Integer>(x - xx, y + yy, z - zz);
							Block b = world.getBlock(currentVec.x, currentVec.y, currentVec.z);
							if (controllerVec != null && currentVec.equals(controllerVec) && b != null) continue;
							
							// Find which rows and coloums, x and z we need to check.
							else if (((xx == 0 || xx == size) && (zz == 0 || zz == size)) && !isBlockValid(b)) {
								current = false;
								break;
							}
						}
					}
				}
			}
			
			// If all blocks were valid including controllers and cores for redundancy are true, return true (yay!).
			flag = current && containsOneController && containsOneCore;
		}
		
		return flag;
	}
	
}
