/* This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under the terms of the GNU General Public 
* License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Project-Zed is 
* distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
* PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along 
* with Project-Zed. If not, see <http://www.gnu.org/licenses/>
*/
package com.projectzed.mod.handler;

import com.hockeyhurd.hcorelib.api.math.Vector3;
import com.projectzed.api.util.Sound;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;

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
	 * @param soundEvent sound event.
	 * @param soundCategory sound's category.
	 * @param world world object as reference.
	 * @param entity entity to play at as reference.
	 * @param volume volume of sound to play.
	 * @param pitch pitch of sound to play.
	 */
	public static void playAtEntity(SoundEvent soundEvent, SoundCategory soundCategory, World world, EntityPlayer entity, float volume, float pitch) {
		world.playSound(entity, entity.getPosition(), soundEvent, soundCategory, volume, pitch);
	}
	
	/**
	 * Plays sound at entity.
	 *
	 * @param sound sound to play.
	 * @param world world object as reference.
	 * @param entity entity to play at as reference.
	 */
	public static void playAtEntity(Sound sound, World world, EntityPlayer entity) {
		playAtEntity(sound.SOUND_EVENT, SoundCategory.AMBIENT, world, entity, sound.VOLUME, sound.PITCH);
	}
	
	/**
	 * Plays sound effect in world.
	 *
	 * @param soundEvent sound event.
	 * @param soundCategory sound's category.
	 * @param world world object as reference.
	 * @param pos position in world to play.
	 * @param volume volume to play at.
	 * @param pitch pitch to play at.
	 */
	public static void playEffect(SoundEvent soundEvent, SoundCategory soundCategory, World world, Vector3<Integer> pos, float volume, float pitch) {
		// world.playSoundEffect(pos.x, pos.y, pos.z, name, volume, pitch);
		final Vector3<Double> vec3d = pos.getVector3d();
		world.playSound(null, vec3d.x, vec3d.y, vec3d.z, soundEvent, soundCategory, volume, pitch);
	}
	
	/**
	 * Plays sound effect in world.
	 * 
	 * @param sound = sound object to play.
	 * @param world = world object as reference.
	 * @param pos = position in world to play.
	 */
	public static void playEffect(Sound sound, World world, Vector3<Integer> pos) {
		playEffect(sound.SOUND_EVENT, SoundCategory.AMBIENT, world, pos, sound.VOLUME, sound.PITCH);
	}

}
