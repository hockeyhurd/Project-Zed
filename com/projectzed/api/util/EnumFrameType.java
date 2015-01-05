package com.projectzed.api.util;

/**
 * Enumeration for type of modular frame. 
 * 
 * @author hockeyhurd
 * @version Jan 4, 2015
 */
public enum EnumFrameType {

	POWER("mcu"), POWER_RF("rf"), ITEM("item"), FLUID("fluid");
	
	private final String name;
	
	/**
	 * @param name = name of enumeration.
	 */
	private EnumFrameType(String name) {
		this.name = name;
	}
	
	/**
	 * @return toString() method.
	 */
	public String getName() {
		return toString();
	}
	
	@Override
	public String toString() {
		return this.name;
	}
	
}
