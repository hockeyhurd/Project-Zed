package com.projectzed.mod.util;

import com.projectzed.mod.ProjectZed;

/**
 * Class containing code for creating a sound for sound handler.
 * 
 * @author hockeyhurd
 * @version Dec 30, 2014
 */
public class Sound {
	
	public static Sound METAL_PRESS = new Sound("industrialMetalPressSound", 1.0f, 1.0f);
	
	/** Name of file (NOTE: Already includes assetDir) */
	public final String NAME;
	
	/** Volume of sound to play */
	public final float VOLUME;
	
	/** Pitch of sound to play */
	public final float PITCH;
	
	/**
	 * @param name = name of sound.
	 * @param volume = volume of sound.
	 * @param pitch = pitch of sound.
	 */
	private Sound(String name, float volume, float pitch) {
		this.NAME = ProjectZed.assetDir + name;
		this.VOLUME = volume;
		this.PITCH = pitch;
	}
}
