/* This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under the terms of the GNU General Public 
* License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Project-Zed is 
* distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
* PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along 
* with Project-Zed. If not, see <http://www.gnu.org/licenses/>
*/
package com.projectzed.mod.handler;

import com.projectzed.mod.ProjectZed;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

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
		if (!(event.getEntityLiving() instanceof EntityPlayer) || event.getEntityLiving().worldObj.isRemote) return;
		else {
			EntityPlayer player = (EntityPlayer) event.getEntityLiving();
			if (player.inventory.hasItem(ProjectZed.enrichedUranium)) {
				// System.out.println(player.getActivePotionEffect(Potion.poison) != null);
				if (player.getActivePotionEffect(Potion.poison) != null) return;
				else player.addPotionEffect(POISON);
			}
		}
	}
	
}
