package com.projectzed.api.source;

/**
 * Enum for types of generation.
 * 
 * @author hockeyhurd
 * @version Oct 19, 2014
 */
public enum EnumType {

	SOLAR("Sun", 10), BURNABLE("Burnable", 20), LAVA("Lava", 50), WATER("Water", 5), OTHER("Other", 1);
	
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
