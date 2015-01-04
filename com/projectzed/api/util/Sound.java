package com.projectzed.api.util;

import com.projectzed.mod.ProjectZed;

/**
 * Class containing code for creating a sound for sound handler.
 * <br><br>Feel free to use any of sounds I made below but note you must
 * <br>have full mod for sound to work as the sound file is in my sounds resource folder! 
 * 
 * @author hockeyhurd
 * @version Dec 30, 2014
 */
public class Sound {
	
	/** Name of file (NOTE: Already includes assetDir) */
	public final String NAME;
	
	/** Volume of sound to play */
	public final float VOLUME;
	
	/** Pitch of sound to play */
	public final float PITCH;
	
	/** Approximate length of sound in whole seconds (best to round up). */
	public final int LENGTH;

	public static Sound METAL_PRESS = new Sound("industrialMetalPressSound", 1.0f, 1.0f, 2);
	
	/**
	 * @param name = name of sound.
	 * @param volume = volume of sound.
	 * @param pitch = pitch of sound.
	 * @param length = length of sound.
	 */
	private Sound(String name, float volume, float pitch, int length) {
		this.NAME = ProjectZed.assetDir + name;
		this.VOLUME = volume;
		this.PITCH = pitch;
		this.LENGTH = length;
	}
	
	/**
	 * NOTE: Should create object in own sound handler similar to my own (see link below).
	 * <br>Create objects there resembling above.
	 * <br>Although not recommended, you could create sound object in TileEntity class
	 * <br>should the class extend AbstractTileEntityMachine for instance.
	 * @see com.projectzed.mod.handler.SoundHandler
	 * 
	 * @param assetDir = usually "<your modid (in lowercase)>:"; ex. ' "projectzed:"; '
	 * @param name = name of sound.
	 * @param volume = volume of sound.
	 * @param pitch = pitch of sound.
	 * @param length = length of sound.
	 */
	public Sound(String assetDir, String name, float volume, float pitch, int length) {
		this.NAME = assetDir + name;
		this.VOLUME = volume;
		this.PITCH = pitch;
		this.LENGTH = length;
	}
	
}
