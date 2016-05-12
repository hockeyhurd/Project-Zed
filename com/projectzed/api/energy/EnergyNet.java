/* This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under the terms of the GNU General Public 
* License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Project-Zed is 
* distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
* PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along 
* with Project-Zed. If not, see <http://www.gnu.org/licenses/>
*/
package com.projectzed.api.energy;

import com.projectzed.api.energy.machine.IEnergyMachine;
import com.projectzed.api.energy.source.IColorComponent;
import com.projectzed.api.energy.storage.IEnergyContainer;
import com.projectzed.api.tileentity.IModularFrame;
import com.projectzed.api.util.EnumFrameType;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

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
	public static void importEnergyFromNeighbors(IEnergyContainer sourceCont, World world, int x, int y, int z, EnumFacing lastDir) {
		if (world == null || world.isRemote) return;
		else {
			if (sourceCont.getEnergyStored() == sourceCont.getMaxStorage()) return;

			boolean colorDep = sourceCont instanceof IColorComponent;
			boolean sideDep = sourceCont instanceof IModularFrame && ((IModularFrame) sourceCont).getType() == EnumFrameType.POWER;
			boolean[] sides = new boolean[EnumFacing.VALUES.length];
			int count = 0;
			
			// Check surrounding blocks.
			for (EnumFacing dir : EnumFacing.VALUES) {
				final BlockPos blockPos = new BlockPos(x + dir.getFrontOffsetX(), y + dir.getFrontOffsetY(), z + dir.getFrontOffsetZ());

				if (world.getTileEntity(blockPos) instanceof IEnergyContainer) {
					IEnergyContainer cont = (IEnergyContainer) world.getTileEntity(blockPos);
					if (colorDep && cont instanceof IColorComponent && ((IColorComponent) cont).getColor() != ((IColorComponent) sourceCont).getColor()) continue;
					else if (cont instanceof IEnergyMachine) continue;
					else if (/*sideDep &&*/ cont instanceof IModularFrame && ((IModularFrame) cont).getType() == EnumFrameType.POWER && ((IModularFrame) cont).getSideValve(dir.getOpposite()) != 1) continue;
					else if (cont.getEnergyStored() > 0 && lastDir != dir) {
						sides[dir.ordinal()] = true;
						count++;
					}
				}
			}
			
			// Check this block relative to neighbors.
			for (EnumFacing dir : EnumFacing.VALUES) {
				if (sourceCont.getEnergyStored() >= sourceCont.getMaxStorage()) break;
				
				if (sides[dir.ordinal()] && dir != lastDir) {
					final BlockPos blockPos = new BlockPos(x + dir.getFrontOffsetX(), y + dir.getFrontOffsetY(), z + dir.getFrontOffsetZ());

					IEnergyContainer cont = (IEnergyContainer) world.getTileEntity(blockPos);
					
					if (sideDep && ((IModularFrame) sourceCont).getType() == EnumFrameType.POWER && ((IModularFrame) sourceCont).getSideValve(dir) != -1) continue;
					if (cont.getMaxExportRate() == 0 || sourceCont.getMaxImportRate() == 0) {
						if (count > 0) count--;
						continue;
					}

					int amount = Math.min(cont.getMaxExportRate(), sourceCont.getMaxImportRate());
					amount = Math.min(amount, sourceCont.getMaxStorage() - sourceCont.getEnergyStored());

					if (amount > 0 && count > 1) amount /= count;

					if (amount > 0 && cont.getEnergyStored() > 0) {
						if (colorDep && cont instanceof IColorComponent && cont.getEnergyStored() <= sourceCont.getEnergyStored()) continue;

						sourceCont.addPower(cont, cont.requestPower(sourceCont, amount));
						// cont.setLastReceivedDirection(dir.getOpposite());
						// sourceCont.setLastReceivedDirection(dir);
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
	public static void tryClearDirectionalTraffic(IEnergyContainer sourceCont, World world, int x, int y, int z, EnumFacing lastDir) {
		// boolean shouldSend = false;

		final BlockPos blockPos = new BlockPos(x + lastDir.getFrontOffsetX(), y + lastDir.getFrontOffsetY(), z + lastDir.getFrontOffsetZ());

		TileEntity te = world.getTileEntity(blockPos);
		IEnergyContainer cont = null;
		if (te != null && te instanceof IEnergyContainer) cont = (IEnergyContainer) te;
		// if (/*sourceCont.getEnergyStored() >= sourceCont.getMaxStorage() || */ sourceCont.getEnergyStored() == 0 || cont == null) {
		if (cont == null || sourceCont.getEnergyStored() == 0) {
			// shouldSend = true;
			clearDirectionalTraffic(sourceCont);
		}
		
		else if (cont != null && lastDir == cont.getLastReceivedDirection().getOpposite() && sourceCont.getEnergyStored() >= cont.getEnergyStored()) {
			// shouldSend = true;
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
		sourceCont.setLastReceivedDirection(null);
	}

 }
