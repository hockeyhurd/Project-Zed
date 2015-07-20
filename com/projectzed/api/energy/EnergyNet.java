/* This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under the terms of the GNU General Public 
* License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Project-Zed is 
* distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
* PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along 
* with Project-Zed. If not, see <http://www.gnu.org/licenses/>
*/
package com.projectzed.api.energy;

import com.projectzed.api.energy.source.IColorComponent;
import com.projectzed.api.energy.storage.IEnergyContainer;
import com.projectzed.api.tileentity.IModularFrame;
import com.projectzed.api.tileentity.machine.AbstractTileEntityMachine;
import com.projectzed.api.util.EnumFrameType;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

/**
 * Class containing code for how energy is to transfer from 
 * <br>one te object to another across the power network system.
 * 
 * @author hockeyhurd
 * @version Dec 2, 2014
 */
public class EnergyNet {

	// Mostly static class, no need to instantiate outside of this class!
	private EnergyNet() {
	}
	
	/**
	 * Main static method used to move energy from one container to another
	 * from one place (here).
	 * 
	 * @param sourceCont container requesting power.
	 * @param world world object as reference.
	 * @param x x-pos as reference.
	 * @param y y-pos as reference.
	 * @param z z-pos as reference.
	 * @param lastDir last direction received from (prevent continuous looping).
	 */
	public static void importEnergyFromNeighbors(IEnergyContainer sourceCont, World world, int x, int y, int z, ForgeDirection lastDir) {
		if (world == null || world.isRemote) return;
		else {
			if (sourceCont.getEnergyStored() == sourceCont.getMaxStorage()) return;
			
			boolean colorDep = sourceCont instanceof IColorComponent;
			boolean sideDep = sourceCont instanceof IModularFrame && ((IModularFrame) sourceCont).getType() == EnumFrameType.POWER;
			boolean[] sides = new boolean[ForgeDirection.VALID_DIRECTIONS.length];
			int count = 0;
			
			// Check surrounding blocks.
			for (ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS) {
				if (world.getTileEntity(x + dir.offsetX, y + dir.offsetY, z + dir.offsetZ) instanceof IEnergyContainer) {
					IEnergyContainer cont = (IEnergyContainer) world.getTileEntity(x + dir.offsetX, y + dir.offsetY, z + dir.offsetZ);
					if (colorDep && cont instanceof IColorComponent && ((IColorComponent) cont).getColor() != ((IColorComponent) sourceCont).getColor()) continue;
					else if (cont instanceof AbstractTileEntityMachine) continue;
					else if (/*sideDep &&*/ cont instanceof IModularFrame && ((IModularFrame) cont).getType() == EnumFrameType.POWER && ((IModularFrame) cont).getSideValve(dir.getOpposite()) != 1) continue;
					else if (cont.getEnergyStored() > 0 && lastDir != dir) {
						sides[dir.ordinal()] = true;
						count++;
					}
				}
			}
			
			// Check this block relative to neighbors.
			for (ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS) {
				if (sourceCont.getEnergyStored() >= sourceCont.getMaxStorage()) break;
				
				if (sides[dir.ordinal()] && dir != lastDir) {
					IEnergyContainer cont = (IEnergyContainer) world.getTileEntity(x + dir.offsetX, y + dir.offsetY, z + dir.offsetZ);
					
					if (sideDep && ((IModularFrame) sourceCont).getType() == EnumFrameType.POWER && ((IModularFrame) sourceCont).getSideValve(dir) != -1) continue;
					
					int amount = Math.min(cont.getMaxExportRate(), sourceCont.getMaxImportRate());
					amount = Math.min(amount, sourceCont.getMaxStorage() - sourceCont.getEnergyStored());

					if (count > 1) amount /= count;

					if (amount > 0 && cont.getEnergyStored() > 0) {
						if (colorDep && cont instanceof IColorComponent && cont.getEnergyStored() <= sourceCont.getEnergyStored()) continue;

						sourceCont.addPower(cont, cont.requestPower(sourceCont, amount));
						// cont.setLastReceivedDirection(dir.getOpposite());
						sourceCont.setLastReceivedDirection(dir);
					}
					
				}
			}
			
		}
	}
	
	/**
	 * Method once called will attempt to resolve the 'last received direction' if applicable.
	 * 
	 * @param sourceCont container requesting power.
	 * @param world world object as reference.
	 * @param x x-pos as reference.
	 * @param y y-pos as reference.
	 * @param z z-pos as reference.
	 * @param lastDir last direction received from (prevent continuous looping).
	 */
	public static void tryClearDirectionalTraffic(IEnergyContainer sourceCont, World world, int x, int y, int z, ForgeDirection lastDir) {
		boolean shouldSend = false;
		
		TileEntity te = world.getTileEntity(x + lastDir.offsetX, y + lastDir.offsetY, z + lastDir.offsetZ);
		IEnergyContainer cont = null;
		if (te != null && te instanceof IEnergyContainer) cont = (IEnergyContainer) te;
		// if (/*sourceCont.getEnergyStored() >= sourceCont.getMaxStorage() || */ sourceCont.getEnergyStored() == 0 || cont == null) {
		if (cont == null || sourceCont.getEnergyStored() == 0) {
			shouldSend = true;
			clearDirectionalTraffic(sourceCont);
		}
		
		else if (cont != null && lastDir == cont.getLastReceivedDirection().getOpposite() && sourceCont.getEnergyStored() >= cont.getEnergyStored()) {
			shouldSend = true;
			clearDirectionalTraffic(sourceCont);
		}
	}
	
	/**
	 * Method when called will forcibly clear 'last received direction' regardless 
	 * of current process state.
	 * 
	 * @param sourceCont container to send data to.
	 */
	public static void clearDirectionalTraffic(IEnergyContainer sourceCont) {
		sourceCont.setLastReceivedDirection(ForgeDirection.UNKNOWN);
	}

 }
