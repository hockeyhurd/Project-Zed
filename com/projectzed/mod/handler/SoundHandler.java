/* This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under the terms of the GNU General Public 
* License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Project-Zed is 
* distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
* PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along 
* with Project-Zed. If not, see <http://www.gnu.org/licenses/>
*/
package com.projectzed.mod.handler;

import net.minecraft.entity.Entity;
import net.minecraft.world.World;

import com.hockeyhurd.api.math.Vector4;
import com.projectzed.api.util.Sound;
import com.projectzed.mod.ProjectZed;

/**
 * Class containing code for playing a given sound.
 * 
 * @author hockeyhurd
 * @version Dec 30, 2014
 */
public class SoundHandler {

	private SoundHandler() {
	}
	
	/**
	 * Plays sound at entity.
	 * 
	 * @param name = name of sound.
	 * @param world = world object as reference.
	 * @param entity = entity to play at as reference.
	 * @param volume = volume of sound to play.
	 * @param pitch = pitch of sound to play.
	 */
	public static void playAtEntity(String name, World world, Entity entity, float volume, float pitch) {
		world.playSoundAtEntity(entity, ProjectZed.assetDir + name, volume, pitch);
	}
	
	/**
	 * Plays sound at entity.
	 * 
	 * @param sound = sound to play.
	 * @param world = world object as reference.
	 * @param entity = entity to play at as reference.
	 */
	public static void playAtEntity(Sound sound, World world, Entity entity) {
		playAtEntity(sound.NAME, world, entity, sound.VOLUME, sound.PITCH);
	}
	
	/**
	 * Plays sound effect in world.
	 * 
	 * @param name = name of sound.
	 * @param world = world object as reference.
	 * @param pos = position in world to play.
	 * @param volume = volume to play at.
	 * @param pitch = pitch to play at.
	 */
	public static void playEffect(String name, World world, Vector4<Integer> pos, float volume, float pitch) {
		world.playSoundEffect(pos.x, pos.y, pos.z, name, volume, pitch);
	}
	
	/**
	 * Plays sound effect in world.
	 * 
	 * @param sound = sound object to play.
	 * @param world = world object as reference.
	 * @param pos = position in world to play.
	 */
	public static void playEffect(Sound sound, World world, Vector4<Integer> pos) {
		playEffect(sound.NAME, world, pos, sound.VOLUME, sound.PITCH);
	}

}
