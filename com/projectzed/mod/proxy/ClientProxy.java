package com.projectzed.mod.proxy;

import net.minecraft.item.Item;
import net.minecraftforge.client.MinecraftForgeClient;

import com.projectzed.mod.ProjectZed;
import com.projectzed.mod.renderer.EnergyPipeItemRenderer;
import com.projectzed.mod.renderer.EnergyPipeRenderer;
import com.projectzed.mod.tileentity.container.pipe.TileEntityEnergyPipe;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;

/**
 * Client proxy for client related registering only!
 * 
 * @author hockeyhurd
 * @version Oct 19, 2014
 */
public class ClientProxy extends CommonProxy {

	/** Stating variable for tracking the current render pass in special renderers. */
	public static int renderPass;
	public static int energyPipe;
	
	/**
	 * Default Constructor.
	 */
	public ClientProxy() {
	}

	/**
	 * Method used to overwrite CommonProxy's method
	 * and to register any special renderer's we may have.
	 */
	public void registerRenderInformation() {
		energyPipe = RenderingRegistry.getNextAvailableRenderId();
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityEnergyPipe.class, new EnergyPipeRenderer());
		MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(ProjectZed.energyPipe), new EnergyPipeItemRenderer(ProjectZed.energyPipe.getBlockTextureFromSide(0)));
	}

}
