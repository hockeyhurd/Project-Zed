package com.projectzed.mod.handler;

import net.minecraft.entity.Entity;
import net.minecraft.world.World;

import com.projectzed.mod.ProjectZed;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * Class containing code for playing a given sound.
 * 
 * @author hockeyhurd
 * @version Dec 30, 2014
 */
@SideOnly(Side.CLIENT)
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

}
