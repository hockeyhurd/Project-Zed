package com.projectzed.mod.proxy;

import net.minecraft.item.Item;
import net.minecraftforge.client.MinecraftForgeClient;

import com.projectzed.api.energy.source.EnumColor;
import com.projectzed.mod.ProjectZed;
import com.projectzed.mod.renderer.EnergyBankItemRenderer;
import com.projectzed.mod.renderer.EnergyBankRenderer;
import com.projectzed.mod.renderer.EnergyPipeItemRenderer;
import com.projectzed.mod.renderer.EnergyPipeRenderer;
import com.projectzed.mod.tileentity.container.TileEntityEnergyBankBase;
import com.projectzed.mod.tileentity.container.pipe.TileEntityEnergyPipeClear;
import com.projectzed.mod.tileentity.container.pipe.TileEntityEnergyPipeOrange;
import com.projectzed.mod.tileentity.container.pipe.TileEntityEnergyPipeRed;

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
	public static int energyPipeRed, energyPipeOrange, energyPipeClear;
	public static int energyCell;
	
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
		energyPipeRed = RenderingRegistry.getNextAvailableRenderId();
		energyPipeOrange = RenderingRegistry.getNextAvailableRenderId();
		energyPipeClear = RenderingRegistry.getNextAvailableRenderId();
		energyCell = RenderingRegistry.getNextAvailableRenderId();
		
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityEnergyPipeRed.class, new EnergyPipeRenderer(EnumColor.RED));
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityEnergyPipeOrange.class, new EnergyPipeRenderer(EnumColor.ORANGE));
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityEnergyPipeClear.class, new EnergyPipeRenderer(EnumColor.CLEAR));
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityEnergyBankBase.class, new EnergyBankRenderer((byte) 0));
		
		MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(ProjectZed.energyPipeRed), new EnergyPipeItemRenderer(ProjectZed.energyPipeRed.getBlockTextureFromSide(0)));
		MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(ProjectZed.energyPipeOrange), new EnergyPipeItemRenderer(ProjectZed.energyPipeOrange.getBlockTextureFromSide(0)));
		MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(ProjectZed.energyPipeClear), new EnergyPipeItemRenderer(ProjectZed.energyPipeClear.getBlockTextureFromSide(0), true));
		MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(ProjectZed.energyCellTier0), new EnergyBankItemRenderer(ProjectZed.energyCellTier0.getBlockTextureFromSide(0)));
		MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(ProjectZed.energyCellTier1), new EnergyBankItemRenderer(ProjectZed.energyCellTier1.getBlockTextureFromSide(0)));
		MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(ProjectZed.energyCellTier2), new EnergyBankItemRenderer(ProjectZed.energyCellTier2.getBlockTextureFromSide(0)));
		MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(ProjectZed.energyCellTier3), new EnergyBankItemRenderer(ProjectZed.energyCellTier3.getBlockTextureFromSide(0)));
	}

}
