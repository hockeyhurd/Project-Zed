package com.projectzed.mod.handler;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;

import com.projectzed.mod.ProjectZed;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;

/**
 * Class containing event code for when user is doing something i should know about.
 * 
 * @author hockeyhurd
 * @version Dec 30, 2014
 */
public class PlayerEventHandler {

	/** Only static instance of this class. */
	private static final PlayerEventHandler HANLDER = new PlayerEventHandler();
	
	/** Effect player gets when holding enriched (radioactive) uranium. */
	private static final PotionEffect POISON = new PotionEffect(Potion.poison.id, 100, 1);

	private PlayerEventHandler() {
	}
	
	/**
	 * @return static instance of this class.
	 */
	public static PlayerEventHandler instance() {
		return HANLDER;
	}

	/**
	 * Method used to mostly give user POISON when handling enriched uranium.
	 * 
	 * @param event = event called.
	 */
	@SubscribeEvent
	public void onPlayerUpdate(LivingUpdateEvent event) {
		if (!(event.entityLiving instanceof EntityPlayer) || event.entityLiving.worldObj.isRemote) return;
		else {
			EntityPlayer player = (EntityPlayer) event.entityLiving;
			if (player.inventory.hasItem(ProjectZed.enrichedUranium)) {
				// System.out.println(player.getActivePotionEffect(Potion.poison) != null);
				if (player.getActivePotionEffect(Potion.poison) != null) return;
				else player.addPotionEffect(POISON);
			}
		}
	}
	
}
