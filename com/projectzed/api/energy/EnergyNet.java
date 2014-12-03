package com.projectzed.api.energy;

import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import com.projectzed.api.energy.storage.IEnergyContainer;
import com.projectzed.mod.tileentity.container.pipe.TileEntityEnergyPipeBase;

/**
 * 
 * @author hockeyhurd
 * @version Dec 2, 2014
 */
public class EnergyNet {

	private EnergyNet() {
	}
	
	// TODO: Make this class not need 'TileEntityEnergyPipeBase' class (not in the API).
	
	public static void importEnergyFromNeighbors(IEnergyContainer sourceCont, World world, int x, int y, int z, ForgeDirection lastDir) {
		if (world == null || world.isRemote) return;
		else {
			if (sourceCont.getEnergyStored() == sourceCont.getMaxStorage()) return;
			
			boolean colorDep = sourceCont instanceof TileEntityEnergyPipeBase;
			boolean[] sides = new boolean[ForgeDirection.VALID_DIRECTIONS.length];
			int count = 0;
			
			for (ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS) {
				if (world.getTileEntity(x + dir.offsetX, y + dir.offsetY, z + dir.offsetZ) instanceof IEnergyContainer) {
					IEnergyContainer cont = (IEnergyContainer) world.getTileEntity(x + dir.offsetX, y + dir.offsetY, z + dir.offsetZ);
					if (colorDep && cont instanceof TileEntityEnergyPipeBase && ((TileEntityEnergyPipeBase)cont).getColor() == ((TileEntityEnergyPipeBase)sourceCont).getColor()) continue;
					else if (cont.getEnergyStored() > 0 && lastDir != dir) {
						sides[dir.ordinal()] = true;
						count++;
					}
				}
			}
			
			for (ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS) {
				if (sides[dir.ordinal()] && dir != lastDir) {
					IEnergyContainer cont = (IEnergyContainer) world.getTileEntity(x + dir.offsetX, y + dir.offsetY, z + dir.offsetZ);
					
					int amount = Math.min(cont.getMaxExportRate(), sourceCont.getMaxImportRate()) / sides.length;
					if (amount >= 0 && cont.getEnergyStored() > 0 && sourceCont.getEnergyStored() + amount <= sourceCont.getMaxStorage()) {
						sourceCont.addPower(cont, cont.requestPower(sourceCont, amount));
					}
					
					cont.setLastReceivedDirection(ForgeDirection.VALID_DIRECTIONS[ForgeDirection.OPPOSITES[dir.ordinal()]]);
				}
			}
			
		}
	}

 }
