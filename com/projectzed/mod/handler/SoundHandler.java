package com.projectzed.mod.handler;

import net.minecraft.entity.Entity;
import net.minecraft.world.World;

import com.hockeyhurd.api.math.Vector4Helper;
import com.projectzed.mod.ProjectZed;
import com.projectzed.mod.util.Sound;

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
	public static void playEffect(String name, World world, Vector4Helper<Integer> pos, float volume, float pitch) {
		world.playSoundEffect(pos.x, pos.y, pos.z, name, volume, pitch);
	}
	
	/**
	 * Plays sound effect in world.
	 * 
	 * @param sound = sound object to play.
	 * @param world = world object as reference.
	 * @param pos = position in world to play.
	 */
	public static void playEffect(Sound sound, World world, Vector4Helper<Integer> pos) {
		playEffect(sound.NAME, world, pos, sound.VOLUME, sound.PITCH);
	}

}
