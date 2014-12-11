package com.projectzed.api.energy;

import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import com.projectzed.api.energy.storage.IEnergyContainer;
import com.projectzed.api.tileentity.machine.AbstractTileEntityMachine;
import com.projectzed.mod.tileentity.container.TileEntityEnergyBankBase;
import com.projectzed.mod.tileentity.container.pipe.TileEntityEnergyPipeBase;

/**
 * Class containing code for how energy is to transfer from 
 * <br>one te object to another across the power network system.
 * 
 * @author hockeyhurd
 * @version Dec 2, 2014
 */
public class EnergyNet {

	private EnergyNet() {
	}
	
	// TODO: Make this class not need 'TileEntityEnergyPipeBase' class (not in the API).
	/**
	 * Main static method used to move energy from one container to another
	 * from one place (here).
	 * @param sourceCont = container requesting power.
	 * @param world = world object as reference.
	 * @param x = x-pos as reference.
	 * @param y = y-pos as reference.
	 * @param z = z-pos as reference.
	 * @param lastDir = last direction received from (prevent continuous looping).
	 */
	public static void importEnergyFromNeighbors(IEnergyContainer sourceCont, World world, int x, int y, int z, ForgeDirection lastDir) {
		if (world == null || world.isRemote) return;
		else {
			if (sourceCont.getEnergyStored() == sourceCont.getMaxStorage()) return;
			
			boolean colorDep = sourceCont instanceof TileEntityEnergyPipeBase;
			boolean sideDep = sourceCont instanceof TileEntityEnergyBankBase;
			boolean[] sides = new boolean[ForgeDirection.VALID_DIRECTIONS.length];
			int count = 0;
			
			for (ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS) {
				if (world.getTileEntity(x + dir.offsetX, y + dir.offsetY, z + dir.offsetZ) instanceof IEnergyContainer) {
					IEnergyContainer cont = (IEnergyContainer) world.getTileEntity(x + dir.offsetX, y + dir.offsetY, z + dir.offsetZ);
					if (colorDep && cont instanceof TileEntityEnergyPipeBase && ((TileEntityEnergyPipeBase)cont).getColor() != ((TileEntityEnergyPipeBase)sourceCont).getColor()) continue;
					else if (cont instanceof AbstractTileEntityMachine) continue;
					else if (sideDep && cont instanceof TileEntityEnergyBankBase && ((TileEntityEnergyBankBase) cont).getSideValve(dir) != 1) continue;
					else if (cont.getEnergyStored() > 0 && lastDir != dir) {
						sides[dir.ordinal()] = true;
						count++;
					}
				}
			}
			
			for (ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS) {
				if (sourceCont.getEnergyStored() >= sourceCont.getMaxStorage()) break;
				
				if (sides[dir.ordinal()] && dir != lastDir) {
					IEnergyContainer cont = (IEnergyContainer) world.getTileEntity(x + dir.offsetX, y + dir.offsetY, z + dir.offsetZ);
					
					// Shouldn't need below code from pre-determination.
					// if (cont instanceof TileEntityEnergyBankBase && ((TileEntityEnergyBankBase) cont).getSideValve(dir) != 1) continue;
					if (sideDep && ((TileEntityEnergyBankBase) sourceCont).getSideValve(dir) != -1) continue;
					
					int amount = Math.min(cont.getMaxExportRate(), sourceCont.getMaxImportRate()) /*/ count*/;
					amount = Math.min(amount, sourceCont.getMaxStorage() - sourceCont.getEnergyStored());
					if (amount > 0 && cont.getEnergyStored() > 0) {
						if (colorDep && cont instanceof TileEntityEnergyPipeBase && cont.getEnergyStored() <= sourceCont.getEnergyStored()) continue;

						sourceCont.addPower(cont, cont.requestPower(sourceCont, amount));
						cont.setLastReceivedDirection(ForgeDirection.VALID_DIRECTIONS[ForgeDirection.OPPOSITES[dir.ordinal()]]);
					}
					
				}
			}
			
		}
	}
	
	/**
	 * Method once called will attempt to resolve the 'last received direction' if applicable.
	 * 
	 * @param sourceCont = container requesting power.
	 * @param world = world object as reference.
	 * @param x = x-pos as reference.
	 * @param y = y-pos as reference.
	 * @param z = z-pos as reference.
	 * @param lastDir = last direction received from (prevent continuous looping).
	 */
	public static void tryClearDirectionalTraffic(IEnergyContainer sourceCont, World world, int x, int y, int z, ForgeDirection lastDir) {
		boolean shouldSend = false;
		
		IEnergyContainer te = (IEnergyContainer) world.getTileEntity(x + lastDir.offsetX, y + lastDir.offsetY, z + lastDir.offsetZ);
		if (sourceCont.getEnergyStored() >= sourceCont.getMaxStorage() || te == null) {
			shouldSend = true;
			clearDirectionalTraffic(sourceCont);
		}
	}
	
	/**
	 * Method when called will forcibly clear 'last received direction' regardless 
	 * of current process state.
	 * 
	 * @param sourceCont = container to send data to.
	 */
	public static void clearDirectionalTraffic(IEnergyContainer sourceCont) {
		sourceCont.setLastReceivedDirection(ForgeDirection.UNKNOWN);
	}

 }
