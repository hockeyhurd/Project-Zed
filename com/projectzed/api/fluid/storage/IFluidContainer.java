package com.projectzed.api.fluid.storage;

import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;

import com.hockeyhurd.api.math.Vector4Helper;

/**
 * Interface for all fluid containers. 
 * <br><bold>NOTE:</bold> This interface closely follows IEnergyContainer (see link below for more info).
 * @see com.projectzed.api.energy.storage.IEnergyContainer
 * 
 * @author hockeyhurd
 * @version Jan 9, 2015
 */
public interface IFluidContainer {

	/** Max allowed capacity */
	public void setMaxStorage(int max);

	/** Get the max capacity */
	public int getMaxStorage();

	/** Set the amount of fluid stored. */
	public void setFluidStored(int amount);

	/** Get the amount currently stored. */
	public int getFluidStored();
	
	/** Get the fluid associated with this container. */
	public Fluid getFluidType();

	/** Function used to get the max import rate */
	public int getMaxImportRate();

	/** Function used to get the max export rate */
	public int getMaxExportRate();

	/**
	 * Function used to request fluid from one container to another with given amount
	 * @param cont = te reference.
	 * @param amount = amount of energy requested.
	 * @return amount of fluid able to obtain.
	 */
	public int requestFluid(IFluidContainer cont, int amount);
	
	/**
	 * Function used to add fluid to this container from another.
	 * @param cont = container from as reference.
	 * @param amount = amount of fluid able to add.
	 * @return
	 */
	public int addFluid(IFluidContainer cont, Fluid fluid, int amount);
	
	/**
	 * Sets the last received direction.
	 * @param dir = direction received from.
	 */
	public void setLastReceivedDirection(ForgeDirection dir);
	
	/**
	 * @return the last received direction.
	 */
	public ForgeDirection getLastReceivedDirection();

	/** Gets and stored the vector co-ordinates of this te. */
	public Vector4Helper<Integer> worldVec();
	
}
