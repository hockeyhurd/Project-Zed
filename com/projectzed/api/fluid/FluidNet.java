/* This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under the terms of the GNU General Public 
* License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Project-Zed is 
* distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
* PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along 
* with Project-Zed. If not, see <http://www.gnu.org/licenses/>
*/
package com.projectzed.api.fluid;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidHandler;

import com.projectzed.api.energy.source.IColorComponent;
import com.projectzed.api.tileentity.IModularFrame;
import com.projectzed.api.util.EnumFrameType;
import com.projectzed.mod.tileentity.container.pipe.TileEntityLiquiductBase;

/**
 * Class containing code for how fluid is to transfer from
 * <br>one te object to another across the fluid network system.
 * 
 * @author hockeyhurd
 * @version Feb 13, 2015
 */
public class FluidNet {

	// Mostly static class, no need to instantiate outside of this class!
	private FluidNet() {
	}

	/**
	 * Main static method used to move fluid from one container to another
	 * from one place (here).
	 * 
	 * @param sourceCont container requesting fluid.
	 * @param world world object as reference.
	 * @param x x-pos as reference.
	 * @param y y-pos as reference.
	 * @param z z-pos as reference.
	 * @param lastDir last direction received from (prevent continuous looping).
	 */
	public static void importFluidFromNeighbors(IFluidHandler sourceCont, World world, int x, int y, int z, ForgeDirection lastDir) {
		if (sourceCont == null || world == null || world.isRemote) return;
		
		boolean colorDep = sourceCont instanceof IColorComponent;
		boolean sideDep = sourceCont instanceof IModularFrame;
		boolean[] sides = new boolean[ForgeDirection.VALID_DIRECTIONS.length];
		int counter = 0;
		int maxTransfer = sourceCont instanceof TileEntityLiquiductBase ? ((TileEntityLiquiductBase) sourceCont).getMaxExportRate() : 1000; 
		
		// Check surrounding blocks.
		for (ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS) {
			if (world.getTileEntity(x + dir.offsetX, y + dir.offsetY, z + dir.offsetZ) instanceof IFluidHandler && dir != lastDir) {
				IFluidHandler cont = (IFluidHandler) world.getTileEntity(x + dir.offsetX, y + dir.offsetY, z + dir.offsetZ);
				if (colorDep && cont instanceof IColorComponent && ((IColorComponent) cont).getColor() != ((IColorComponent) sourceCont).getColor()) continue;
				if (cont instanceof IModularFrame && ((IModularFrame) cont).getType() == EnumFrameType.FLUID && ((IModularFrame) cont).getSideValve(dir.getOpposite()) != 1) continue;
				if (cont.getTankInfo(dir)[0] != null && cont.getTankInfo(dir)[0].fluid != null && cont.getTankInfo(dir)[0].fluid.amount > 0) {
					sides[dir.ordinal()] = true;
					counter++;
				}
			}
		}
		
		// Check this block relative to neighbors.
		for (ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS) {
			if (sourceCont.getTankInfo(dir.getOpposite())[0] != null && sourceCont.getTankInfo(dir.getOpposite())[0].fluid != null && sourceCont.getTankInfo(dir.getOpposite())[0].fluid.amount == sourceCont.getTankInfo(dir.getOpposite())[0].capacity) break;

			if (sides[dir.ordinal()] && dir != lastDir) {
				IFluidHandler cont = (IFluidHandler) world.getTileEntity(x + dir.offsetX, y + dir.offsetY, z + dir.offsetZ);
				
				if (cont.getTankInfo(dir)[0] == null || cont.getTankInfo(dir)[0].fluid.getFluid() == null || cont.getTankInfo(dir)[0].fluid.amount == 0) continue;
				if (sideDep && ((IModularFrame) sourceCont).getSideValve(dir) != -1) continue;
				
				FluidStack stackSrc = sourceCont.getTankInfo(dir.getOpposite())[0].fluid;
				FluidStack stackCont = cont.getTankInfo(dir)[0].fluid;
				
				if (!FluidStack.areFluidStackTagsEqual(stackSrc, stackCont)) continue;
				
				int amount = Math.min(maxTransfer, 1000);
				amount = Math.min(amount, stackSrc.amount);
				amount = Math.min(amount, stackCont.amount);
				
				if (counter > 1) amount /= counter;
				
				if (amount > 0) {
					if (colorDep && cont instanceof IColorComponent && cont.getTankInfo(dir)[0].fluid.amount <= sourceCont.getTankInfo(dir.getOpposite())[0].fluid.amount) continue;
	
					FluidStack temp = stackSrc.copy();
					temp.amount = amount;
					
					sourceCont.fill(dir.getOpposite(), temp, true);
					cont.drain(dir, temp, true);
				}
			}
			
		}
		
	}
	
	/**
	 * Method once called will attempt to resolve the 'last received direction' if applicable.
	 * 
	 * @param sourceCont container requesting fluid.
	 * @param world world object as reference.
	 * @param x x-pos as reference.
	 * @param y y-pos as reference.
	 * @param z z-pos as reference.
	 * @param lastDir last direction received from (prevent continuous looping).
	 */
	public static void tryClearDirectionalTraffic(IFluidHandler sourceCont, World world, int x, int y, int z, ForgeDirection lastDir) {
		boolean shouldSend = false;
		
		TileEntity te = world.getTileEntity(x + lastDir.offsetX, y + lastDir.offsetY, z + lastDir.offsetZ);
		IFluidHandler cont = null;
		if (te != null && te instanceof IFluidHandler) cont = (IFluidHandler) te;
		if (cont == null || sourceCont.getTankInfo(lastDir)[0] == null || sourceCont.getTankInfo(lastDir)[0].fluid == null || sourceCont.getTankInfo(lastDir)[0].fluid.amount == 0) {
			shouldSend = true;
			clearDirectionalTraffic((TileEntityLiquiductBase) sourceCont);
		}
		
		else if (cont instanceof TileEntityLiquiductBase && ((TileEntityLiquiductBase) cont).getLastReceivedDirection().getOpposite() == lastDir) {
			shouldSend = true;
			clearDirectionalTraffic((TileEntityLiquiductBase) sourceCont);
		}
	}
	
	/**
	 * Method when called will forcibly clear 'last received direction' regardless 
	 * of current process state.
	 * 
	 * @param sourceCont container to send data to.
	 */
	public static void clearDirectionalTraffic(TileEntityLiquiductBase sourceCont) {
		sourceCont.setLastReceivedDirection(ForgeDirection.UNKNOWN);
	}
	
}
