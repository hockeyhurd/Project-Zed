/* This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under the terms of the GNU General Public 
* License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Project-Zed is 
* distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
* PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along 
* with Project-Zed. If not, see <http://www.gnu.org/licenses/>
*/
package com.projectzed.api.util;

import com.projectzed.mod.ProjectZed;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

import java.util.LinkedList;
import java.util.List;

/**
 * Class containing code for creating a sound for sound handler.
 * <br><br>Feel free to use any of sounds I made below but note you must
 * <br>have full mod for sound to work as the sound file is in my sounds resource folder! 
 * 
 * @author hockeyhurd
 * @version Dec 30, 2014
 */
public final class Sound {

	// Registry code:
	private static final List<Sound> registryList = new LinkedList<Sound>();
	private static boolean isRegistered = false;

	/** Name of file (NOTE: Already includes assetDir) */
	public final String NAME;
	
	/** Volume of sound to play */
	public final float VOLUME;
	
	/** Pitch of sound to play */
	public final float PITCH;
	
	/** Approximate length of sound in whole seconds (best to round up). */
	public final int LENGTH;

	/**
	 * The registered sound event.
	 */
	public final SoundEvent SOUND_EVENT;

	/**
	 * ResourceLocation contained
	 */
	public final ResourceLocation RESOURCE;

	public static final Sound METAL_PRESS = new Sound("industrialMetalPressSound", 1.0f, 1.0f, 2);
	public static final Sound ENERGIZER = new Sound("industrialEnergizerSound", 1.0f, 1.0f, 2);

	/**
	 * @param name name of sound.
	 * @param volume volume of sound.
	 * @param pitch pitch of sound.
	 * @param length length of sound.
	 */
	private Sound(String name, float volume, float pitch, int length) {
		this.NAME = ProjectZed.assetDir + name;
		this.VOLUME = volume;
		this.PITCH = pitch;
		this.LENGTH = length;

		this.RESOURCE = new ResourceLocation(ProjectZed.assetDir, name);
		this.SOUND_EVENT = new SoundEvent(RESOURCE);
	}
	
	/**
	 * NOTE: Should create object in own sound handler similar to my own (see link below).
	 * <br>Create objects there resembling above.
	 * <br>Although not recommended, you could create sound object in TileEntity class
	 * <br>should the class extend AbstractTileEntityMachine for instance.
	 * @see com.projectzed.mod.handler.SoundHandler
	 *
	 * @param assetDir usually "<your modid (in lowercase)>:"; ex. ' "projectzed:"; '
	 * @param name name of sound.
	 * @param volume volume of sound.
	 * @param pitch pitch of sound.
	 * @param length length of sound.
	 */
	private Sound(String assetDir, String name, float volume, float pitch, int length) {
		this.NAME = assetDir + name;
		this.VOLUME = volume;
		this.PITCH = pitch;
		this.LENGTH = length;

		this.RESOURCE = new ResourceLocation(ProjectZed.assetDir, name);
		this.SOUND_EVENT = new SoundEvent(RESOURCE);
	}

	/**
	 * Handles registering of new sound.
	 *
	 * @param soundEvent ResourceLocation.
	 */
	private static void registerSound(SoundEvent soundEvent) {
		GameRegistry.register(soundEvent, soundEvent.getRegistryName());
	}

	/**
	 * Handles initialization of sounds.
	 */
	public static void init() {
		if (isRegistered) return;
		isRegistered = true;

		registryList.add(METAL_PRESS);
		registryList.add(ENERGIZER);

		for (Sound sound : registryList) {
			GameRegistry.register(sound.SOUND_EVENT, sound.RESOURCE);
		}
	}
	
}
