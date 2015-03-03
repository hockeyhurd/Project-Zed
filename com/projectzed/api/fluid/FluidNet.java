/* This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under the terms of the GNU General Public 
* License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Project-Zed is 
* distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
* PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along 
* with Project-Zed. If not, see <http://www.gnu.org/licenses/>
*/
package com.projectzed.api.fluid;

import com.projectzed.api.energy.source.IColorComponent;
import com.projectzed.api.fluid.container.IFluidContainer;
import com.projectzed.api.tileentity.IModularFrame;
import com.projectzed.api.util.EnumFrameType;
import com.projectzed.mod.tileentity.container.pipe.TileEntityLiquiductBase;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidHandler;

import java.util.HashMap;

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

	// TODO: Make this class fully safe in the API (make sure only API things are in here).

	/**
	 * Main static method used to move fluid from one container to another
	 * from one place (here).
	 * <br><bold>NOTE: </bold>Liquiducts use this for transport to one another.
	 *
	 * @param sourceCont container requesting fluid.
	 * @param world world object as reference.
	 * @param x x-pos as reference.
	 * @param y y-pos as reference.
	 * @param z z-pos as reference.
	 * @param lastDir last direction received from (prevent continuous looping).
	 */
	public static void importFluidFromNeighbors(IFluidContainer sourceCont, World world, int x, int y, int z, ForgeDirection lastDir) {
		if (sourceCont == null || world == null || world.isRemote) return;

		boolean colorDep = sourceCont instanceof IColorComponent;
		boolean sideDep = sourceCont instanceof IModularFrame;
		boolean[] sides = new boolean[ForgeDirection.VALID_DIRECTIONS.length];
		int counter = 0;
		int maxTransfer = sourceCont instanceof TileEntityLiquiductBase ? sourceCont.getMaxFluidImportRate() : 1000;

		HashMap<ForgeDirection, Integer> map = new HashMap<ForgeDirection, Integer>();

		// Check surrounding blocks.
		for (ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS) {
			if (world.getTileEntity(x + dir.offsetX, y + dir.offsetY, z + dir.offsetZ) instanceof IFluidHandler && dir != lastDir) {
				IFluidHandler cont = (IFluidHandler) world.getTileEntity(x + dir.offsetX, y + dir.offsetY, z + dir.offsetZ);
				if (colorDep && cont instanceof IColorComponent && ((IColorComponent) cont).getColor() != ((IColorComponent) sourceCont).getColor()) continue;
				if (cont instanceof IModularFrame && ((IModularFrame) cont).getType() == EnumFrameType.FLUID && ((IModularFrame) cont).getSideValve(
						dir.getOpposite()) != 1) continue;
				if (!(cont instanceof IFluidContainer) && sourceCont.isPipe()) continue;
				/*if (cont.getTankInfo(dir)[0] != null && cont.getTankInfo(dir)[0].fluid != null && cont.getTankInfo(dir)[0].fluid.amount > 0) {
					sides[dir.ordinal()] = true;
					counter++;
				}*/

				if (cont.getTankInfo(dir) != null && cont.getTankInfo(dir).length > 0) {

					for (int i = 0; i < cont.getTankInfo(dir).length; i++) {
						if (cont.getTankInfo(dir)[i].fluid != null && cont.getTankInfo(dir)[i].fluid.isFluidEqual(sourceCont.getTank().getFluid())) {
							map.put(dir, i);
							sides[dir.ordinal()] = true;
							counter++;
							break;
						}
					}
					
					/*if (!sides[dir.ordinal()]) {
						map.put(dir, -1);
						sides[dir.ordinal()] = true;
						counter++;
					}*/

				}
			}
		}

		// Check this block relative to neighbors.
		if (!map.isEmpty()) {
			for (ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS) {
				// if (sourceCont.getTankInfo(dir.getOpposite())[0] != null && sourceCont.getTankInfo(dir.getOpposite())[0].fluid != null && sourceCont.getTankInfo(dir.getOpposite())[0].fluid.amount == sourceCont.getTankInfo(dir.getOpposite())[0].capacity) break;

				if (sides[dir.ordinal()] && dir != lastDir && map.containsKey(dir)) {
					IFluidHandler cont = (IFluidHandler) world.getTileEntity(x + dir.offsetX, y + dir.offsetY, z + dir.offsetZ);

					int value = map.get(dir);

					if (value == -1 || cont.getTankInfo(dir)[value] == null || cont.getTankInfo(dir)[value].fluid.getFluid() == null || cont.getTankInfo(dir)[value].fluid.amount == 0) {
						counter--;
						continue;
					}

					if (sideDep && ((IModularFrame) sourceCont).getSideValve(dir) != -1) {
						counter--;
						continue;
					}

					FluidStack stackSrc = sourceCont.getTankInfo(dir.getOpposite())[value].fluid;
					FluidStack stackCont = cont.getTankInfo(dir)[value].fluid;

					if (!FluidStack.areFluidStackTagsEqual(stackSrc, stackCont)) {
						counter--;
						continue;
					}

					int amount = Math.min(maxTransfer, 1000);
					// amount = Math.min(amount, stackSrc.amount);
					// amount = Math.min(amount, stackCont.amount);
					amount = Math.min(amount, sourceCont.getTank().getCapacity() - sourceCont.getTank().getFluidAmount());

					if (counter > 1 && amount > 1) {
						if (amount / counter > 0) amount /= counter;
					}

					if (amount > 0) {
						if (colorDep && cont instanceof IColorComponent && cont.getTankInfo(dir)[value].fluid.amount > 0 && cont.getTankInfo(dir)[value].fluid.amount <= sourceCont.getTankInfo(dir.getOpposite())[value].fluid.amount) continue;

						FluidStack temp = stackSrc.copy();
						temp.amount = amount;

						sourceCont.fill(dir.getOpposite(), temp, true);
						cont.drain(dir, temp, true);

						sourceCont.setLastReceivedDirection(dir);
					}
				}

			}
		}

	}

	/**
	 * Method used to transfer fluid to all fluid tanks that aren't of my own. (tanks, liquiducts, etc)
	 * <br><bold>NOTE: </bold>Liquiducts <bold>DO NOT </bold>use this for transport to one another.
	 *
	 * @param sourceCont container requesting fluid.
	 * @param world world object as reference.
	 * @param x x-pos as reference.
	 * @param y y-pos as reference.
	 * @param z z-pos as reference.
	 */
	public static void exportFluidToNeighbors(IFluidContainer sourceCont, World world, int x, int y, int z) {
		if (world == null || world.isRemote || sourceCont == null || sourceCont.getTank().getFluid() == null) return;

		boolean colorDep = sourceCont instanceof IColorComponent;
		boolean sideDep = sourceCont instanceof IModularFrame;
		boolean[] sides = new boolean[ForgeDirection.VALID_DIRECTIONS.length];
		int counter = 0;
		int maxTransfer = sourceCont instanceof IFluidContainer ? sourceCont.getMaxFluidExportRate() : 1000;

		HashMap<ForgeDirection, Integer> map = new HashMap<ForgeDirection, Integer>();

		for (ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS) {
			IFluidHandler cont = (IFluidHandler) world.getTileEntity(x + dir.offsetX, y + dir.offsetY, z + dir.offsetZ);
			if (cont == null) continue;
			// if (cont instanceof IFluidContainer && ((IFluidContainer) cont).isPipe()) continue;
			if (cont instanceof IFluidContainer && !sourceCont.isPipe() && !((IFluidContainer) cont).isPipe()) continue;
			// if (cont instanceof IFluidContainer && sourceCont.isPipe() && ((IFluidContainer) cont).isPipe()) continue;
			if (cont.getTankInfo(dir) != null && cont.getTankInfo(dir).length > 0) {

				for (int i = 0; i < cont.getTankInfo(dir).length; i++) {
					if (cont.getTankInfo(dir)[i].fluid != null && cont.getTankInfo(dir)[i].fluid.isFluidEqual(sourceCont.getTank().getFluid())) {
						map.put(dir, i);
						sides[dir.ordinal()] = true;
						counter++;
						break;
					}
				}

				if (!sides[dir.ordinal()]) {
					map.put(dir, -1);
					sides[dir.ordinal()] = true;
					counter++;
				}
			}
		}

		if (!map.isEmpty()) {
			for (ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS) {
				if (sides[dir.ordinal()] && map.containsKey(dir)) {
					IFluidHandler cont = (IFluidHandler) world.getTileEntity(x + dir.offsetX, y + dir.offsetY, z + dir.offsetZ);
					FluidStack contStack = null;

					if (map.get(dir) > 0) contStack = cont.getTankInfo(dir.getOpposite())[map.get(dir)].fluid;
					else if (isContainerValid(sourceCont, world)) contStack = sourceCont.getTank().getFluid().copy();
					else break;

					int amount = Math.min(maxTransfer, contStack.amount);

					FluidStack temp = contStack.copy();

					if (!sourceCont.canDrain(dir, temp.getFluid()) || !cont.canFill(dir.getOpposite(), temp.getFluid())) {
						if (counter > 1) counter--;
						continue;
					}

					temp.amount = amount;
					amount = Math.min(amount, cont.fill(dir.getOpposite(), temp, false));

					if (counter > 1 && amount > 1) {
						if (amount / counter > 0) amount /= counter;
					}

					temp.amount = amount;

					if (amount > 0) {
						temp.amount = cont.fill(dir.getOpposite(), temp, true);
						sourceCont.drain(dir, temp, true);
					}

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
		if (te != null && te instanceof IFluidContainer) cont = (IFluidContainer) te;
		if (cont == null || sourceCont.getTankInfo(lastDir)[0] == null || sourceCont.getTankInfo(lastDir)[0].fluid == null || sourceCont.getTankInfo(lastDir)[0].fluid.amount == 0) {
			shouldSend = true;
			clearDirectionalTraffic((IFluidContainer) sourceCont);
		}

		else if (cont instanceof TileEntityLiquiductBase && ((TileEntityLiquiductBase) cont).getLastReceivedDirection().getOpposite() == lastDir) {
			shouldSend = true;
			clearDirectionalTraffic((IFluidContainer) sourceCont);
		}
	}

	/**
	 * Method when called will forcibly clear 'last received direction' regardless 
	 * of current process state.
	 *
	 * @param sourceCont container to send data to.
	 */
	public static void clearDirectionalTraffic(IFluidContainer sourceCont) {
		sourceCont.setLastReceivedDirection(ForgeDirection.UNKNOWN);
	}

	/**
	 * Simple function to determine whether a fluid container is valid (has something) or invalid (has nothing).
	 *
	 * @param cont container to reference.
	 * @param world world as reference of existance.
	 * @return true if has something relavent, else returns false.
	 */
	public static boolean isContainerValid(IFluidHandler cont, World world) {
		boolean flag = false;

		if (cont != null) {
			for (ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS) {
				if (cont.getTankInfo(dir) != null && cont.getTankInfo(dir).length > 0) {
					for (int i = 0; i < cont.getTankInfo(dir).length; i++) {
						if (cont.getTankInfo(dir)[i] != null && cont.getTankInfo(dir)[i].fluid != null && cont.getTankInfo(dir)[i].fluid.amount > 0) {
							flag = true;
							break;
						}
					}

					if (flag) break;
				}
			}
		}

		return flag;
	}

}
