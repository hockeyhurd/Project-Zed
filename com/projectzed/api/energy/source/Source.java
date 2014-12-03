package com.projectzed.api.energy.source;

/**
 * House keeping class to define the source of energy generation.
 * 
 * @author hockeyhurd
 * @version Oct 19, 2014
 */
public class Source {

	private EnumType type;
	private int packetSize;
	
	/**
	 * Default constructor.
	 * @param type = type of energy source.
	 * @param modifier = 
	 */
	public Source(EnumType type, float modifier) {
		this.type = type;
		this.packetSize = (int) Math.round(type.getSize() * modifier);
	}

	/**
	 * Alternative constructor with no modifier.
	 * @param type = type of energy source.
	 */
	public Source(EnumType type) {
		this.type = type;
		this.packetSize = type.getSize();
	}
	
	/**
	 * Alternative constructor allowing for custom/new sources of energy.
	 * @param sourceName = name of source.
	 * @param size = size of said source per tick.
	 * @param modifier = modifier value if applicable.
	 */
	public Source(String sourceName, int size, float modifier) {
		this.type = EnumType.OTHER;
		this.type.setName(sourceName);
		this.type.setSize(size);
		this.packetSize = (int) Math.round(this.type.getSize() * modifier);
	}
	
	/**
	 * Alternative constructor allowing for custom/new sources of energy.
	 * NOTE: modifier will be '1' or no change.
	 * @param sourceName = name of source.
	 * @param size = size of said source per tick.
	 */
	public Source(String sourceName, int size) {
		this(sourceName, size, 1);
	}
	
	/**
	 * Gets the max amount of energy allowed to produce per tick.
	 * @return max amount of energy.
	 */
	public int getEffectiveSize() {
		return this.packetSize;
	}

}
