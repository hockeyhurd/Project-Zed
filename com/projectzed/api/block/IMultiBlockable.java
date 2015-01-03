package com.projectzed.api.block;

import java.util.List;

import net.minecraft.block.Block;

/**
 * Interface for multiblock structures to be implemented in the block's class.
 * 
 * @author hockeyhurd
 * @version Jan 2, 2015
 */
public interface IMultiBlockable<T extends Block> {

	/** Get the block's object. */
	public T getInstance();
	
	/** Whether we can have more than one of these in structure. */
	public boolean isUnique();
	
	/** Whether this block can sub-able. */
	public boolean isSubstituable();
	
	/** If has a substituable block, put in this list, else leave null. */
	public List<IMultiBlockable> getSubList();
	
	/** Get the amount of blocks should have  */
	public int getAmountFromSize(int size);
	
}
