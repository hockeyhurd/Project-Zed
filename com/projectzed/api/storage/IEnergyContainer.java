package com.projectzed.api.storage;

import com.hockeyhurd.api.math.Vector4Helper;

/**
 * Interface for an object (TileEntity, tool, etc.) that contains power.
 * 
 * @author hockeyhurd
 * @version Oct 19, 2014
 */
public interface IEnergyContainer {

	/** Max allowed capacity */
	public void setMaxStorage(int max);

	/** Get the max capacity */
	public int getMaxStorage();

	/** Set the amount of energy stored. */
	public void setEnergyStored(int amount);

	/** Get the amount currently stored. */
	public int getEnergyStored();

	/** Function used to get the max import rate */
	public int getMaxImportRate();

	/** Function used to get the max export rate */
	public int getMaxExportRate();

	/**
	 * Function used to request power from one container to another with given amount
	 * @param cont = te reference.
	 * @param amount = amount of energy requested.
	 * @return amount of energy able to obtain.
	 */
	public int requestPower(IEnergyContainer cont, int amount);

	/** Gets and stored the vector co-ordinates of this te. */
	public Vector4Helper<Integer> worldVec();

}
