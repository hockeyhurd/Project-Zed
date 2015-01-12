package com.projectzed.api.energy.source;

/**
 * Enum for types of generation.
 * 
 * @author hockeyhurd
 * @version Oct 19, 2014
 */
public enum EnumType {

	SOLAR("Sun", 20), BURNABLE("Burnable", 100), LAVA("Lava", 200), WATER("Water", 5), FISSION("Fission", 10000), FUSION("Fusion", 50000), OTHER("Other", 1);
	
	private String name;
	private int size;
	
	private EnumType(String name, int size) {
		this.name = name;
		this.size = size;
	}
	
	@Override
	public String toString() {
		return this.name;
	}
	
	public void setName(String newName) {
		this.name = newName;
	}

	public void setSize(int newSize) {
		this.size = newSize;
	}
	
	public final int getSize() {
		return this.size;
	}
	
}
