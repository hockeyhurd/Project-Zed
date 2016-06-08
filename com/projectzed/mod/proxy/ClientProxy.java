/* This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under the terms of the GNU General Public 
* License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Project-Zed is 
* distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
* PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along 
* with Project-Zed. If not, see <http://www.gnu.org/licenses/>
*/
package com.projectzed.mod.proxy;

import com.hockeyhurd.hcorelib.api.block.IHBlock;
import com.hockeyhurd.hcorelib.api.client.util.ModelRegistry;
import com.hockeyhurd.hcorelib.api.handler.config.ConfigChangedEventHandler;
import com.hockeyhurd.hcorelib.api.handler.input.KeyBindingHandler;
import com.hockeyhurd.hcorelib.api.item.IHItem;
import com.projectzed.mod.ProjectZed;
import com.projectzed.mod.handler.DrawBlockSelectionHandler;
import com.projectzed.mod.handler.input.ItemAdjusterHandler;
import com.projectzed.mod.registry.BlockRegistry;
import com.projectzed.mod.registry.ItemRegistry;
import com.projectzed.mod.renderer.EnergyBankRenderer;
import com.projectzed.mod.tileentity.container.TileEntityEnergyBankBase;
import com.projectzed.mod.util.Reference;
import net.minecraft.item.Item;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;

/**
 * Client proxy for client related registering only!
 * 
 * @author hockeyhurd
 * @version Oct 19, 2014
 */
@SideOnly(Side.CLIENT)
public class ClientProxy extends CommonProxy {

	/** Stating variable for tracking the current render pass in special renderers. */
	public static int renderPass;
	public static int energyPipeRed, energyPipeOrange, energyPipeClear;
	public static int energyCell;
	public static int refinery;
	public static int fluidTankTier0, fluidTankTier1, fluidTankTier2, fluidTankTier3;
	public static int liquiductBlue;
	public static int liquiductClear;
	public static int itemPipeGreen, itemPipeGreenOpaque;
	public static int thickenedGlass;
	public static int wickedClearGlass;
	public static int reactorGlass;

	// private static final RenderWorldHandler renderWorldHandler = RenderWorldHandler.instance();
	// private static final ChunkLoaderWorldRenderer chunkLoaderWorldRenderer = ChunkLoaderWorldRenderer.instance();
	private static KeyBindingHandler keyBindingHandler;

	/**
	 * Default Constructor.
	 */
	public ClientProxy() {
	}

	/**
	 * Gets the KeyBindingHandler from ClientProxy.
	 *
	 * @return KeyBindingHandler instance.
	 */
	/*public static KeyBindingHandler getKeyBindingHandler() {
		return keyBindingHandler;
	}*/
	@Override
	protected void registerBlocks() {
		BlockRegistry.instance().init(ProjectZed.class);
		for (IHBlock b : BlockRegistry.instance().getBlocks()) {
			if (b != null) {
				GameRegistry.register(b.getBlock());
				GameRegistry.register(b.getItemBlock().setRegistryName(b.getBlock().getRegistryName()));
				ModelRegistry.registerBlock(b);
			}
		}
	}

	@Override
	protected void registerItems() {
		ItemRegistry.instance().init(ProjectZed.class);
		for (Item item : ItemRegistry.instance().getItems()) {
			if (item != null) {
				GameRegistry.register(item);

				if (item instanceof IHItem) ModelRegistry.registerItem((IHItem) item);
				else ProjectZed.logHelper.warn("Item:", item.getUnlocalizedName(), "is not an IHItem!");
			}
		}
	}

	@Override
	protected void registerEventHandlers() {
		super.registerEventHandlers();

		FMLCommonHandler.instance().bus().register(new ConfigChangedEventHandler(ProjectZed.modID, ProjectZed.configHandler));
	}

	/**
	 * Method used to overwrite CommonProxy's method
	 * and to register any special renderer's we may have.
	 */
	@Override
	public void registerRenderInformation() {
		
		/*energyPipeRed = RenderingRegistry.getNextAvailableRenderId();
		energyPipeOrange = RenderingRegistry.getNextAvailableRenderId();
		energyPipeClear = RenderingRegistry.getNextAvailableRenderId();
		energyCell = RenderingRegistry.getNextAvailableRenderId();
		refinery = RenderingRegistry.getNextAvailableRenderId();
		fluidTankTier0 = RenderingRegistry.getNextAvailableRenderId();
		fluidTankTier1 = RenderingRegistry.getNextAvailableRenderId();
		fluidTankTier2 = RenderingRegistry.getNextAvailableRenderId();
		fluidTankTier3 = RenderingRegistry.getNextAvailableRenderId();
		liquiductBlue = RenderingRegistry.getNextAvailableRenderId();
		liquiductClear = RenderingRegistry.getNextAvailableRenderId();
		itemPipeGreen = RenderingRegistry.getNextAvailableRenderId();
		itemPipeGreenOpaque = RenderingRegistry.getNextAvailableRenderId();
		thickenedGlass = RenderingRegistry.getNextAvailableRenderId();
		wickedClearGlass = RenderingRegistry.getNextAvailableRenderId();
		reactorGlass = RenderingRegistry.getNextAvailableRenderId();

		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityEnergyPipeRed.class, new EnergyPipeRenderer(EnumColor.RED));
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityEnergyPipeOrange.class, new EnergyPipeRenderer(EnumColor.ORANGE));
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityEnergyPipeClear.class, new EnergyPipeRenderer(EnumColor.CLEAR));
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityEnergyBankBase.class, new EnergyBankRenderer((byte) 0));
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityRefinery.class, new RefineryRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityFluidTankTier0.class, new FluidTankRenderer((byte) 0));
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityFluidTankTier1.class, new FluidTankRenderer((byte) 1));
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityFluidTankTier2.class, new FluidTankRenderer((byte) 2));
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityFluidTankTier3.class, new FluidTankRenderer((byte) 3));
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityLiquiductBlue.class, new FluidPipeRenderer(EnumColor.BLUE));
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityLiquiductClear.class, new FluidPipeRenderer(EnumColor.CLEAR));
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityItemPipeGreen.class, new ItemPipeRenderer(EnumColor.GREEN));
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityItemPipeGreenOpaque.class, new ItemPipeRenderer(EnumColor.GREEN_OPAQUE));
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityReactorGlass.class, new ReactorGlassRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityWickedClearGlass.class, new WickedClearGlassRenderer());

		MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(ProjectZed.energyPipeRed), new EnergyPipeItemRenderer(ProjectZed.energyPipeRed.getBlockTextureFromSide(0)));
		MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(ProjectZed.energyPipeOrange), new EnergyPipeItemRenderer(ProjectZed.energyPipeOrange.getBlockTextureFromSide(0)));
		MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(ProjectZed.energyPipeClear), new EnergyPipeItemRenderer(ProjectZed.energyPipeClear.getBlockTextureFromSide(0), true));
		MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(ProjectZed.energyCellTier0), new EnergyBankItemRenderer(ProjectZed.energyCellTier0.getBlockTextureFromSide(0)));
		MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(ProjectZed.energyCellTier1), new EnergyBankItemRenderer(ProjectZed.energyCellTier1.getBlockTextureFromSide(0)));
		MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(ProjectZed.energyCellTier2), new EnergyBankItemRenderer(ProjectZed.energyCellTier2.getBlockTextureFromSide(0)));
		MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(ProjectZed.energyCellTier3), new EnergyBankItemRenderer(ProjectZed.energyCellTier3.getBlockTextureFromSide(0)));
		MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(ProjectZed.refinery), new RefineryItemRenderer(ProjectZed.refinery.getBlockTextureFromSide(0)));
		MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(ProjectZed.fluidTankTier0), new FluidTankItemRenderer(ProjectZed.fluidTankTier0.getBlockTextureFromSide(0), (byte) 0));
		MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(ProjectZed.fluidTankTier1), new FluidTankItemRenderer(ProjectZed.fluidTankTier1.getBlockTextureFromSide(0), (byte) 1));
		MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(ProjectZed.fluidTankTier2), new FluidTankItemRenderer(ProjectZed.fluidTankTier2.getBlockTextureFromSide(0), (byte) 2));
		MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(ProjectZed.fluidTankTier3), new FluidTankItemRenderer(ProjectZed.fluidTankTier3.getBlockTextureFromSide(0), (byte) 3));
		MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(ProjectZed.liquiductBlue), new FluidPipeItemRenderer(ProjectZed.liquiductBlue.getBlockTextureFromSide(0)));
		MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(ProjectZed.liquiductClear), new FluidPipeItemRenderer(ProjectZed.liquiductClear.getBlockTextureFromSide(0)));
		MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(ProjectZed.itemPipeGreen), new ItemPipeItemRenderer(ProjectZed.itemPipeGreen.getBlockTextureFromSide(0)));
		MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(ProjectZed.itemPipeGreenOpaque), new ItemPipeItemRenderer(ProjectZed.itemPipeGreenOpaque.getBlockTextureFromSide(0)));
		MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(ProjectZed.nuclearReactorGlass), new ReactorGlassItemRenderer(ProjectZed.nuclearReactorGlass.getBlockTextureFromSide(0)));
		MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(ProjectZed.thickenedGlass), new ThickenedGlassItemRenderer(ProjectZed.thickenedGlass.getBlockTextureFromSide(0)));
		MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(ProjectZed.wickedClearGlass), new WickedClearGlassItemRenderer(ProjectZed.wickedClearGlass.getBlockTextureFromSide(0)));
		
		RenderingRegistry.registerBlockHandler(new ThickenedGlassRenderer());*/

		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityEnergyBankBase.class, new EnergyBankRenderer());

		// MinecraftForge.EVENT_BUS.register(renderWorldHandler);
		MinecraftForge.EVENT_BUS.register(new DrawBlockSelectionHandler());
	}

	/**
	 * Method used to overwrite CommonProxy's method
	 * and to register any input handler(s) we may have.
	 */
	@Override
	public void registerInputHandlers() {
		// keyBindingHandler = new KeyBindingHandler(new ChunkToggleKeyBind(chunkLoaderWorldRenderer));
		// FMLCommonHandler.instance().bus().register(keyBindingHandler);

		keyBindingHandler = new KeyBindingHandler(new ItemAdjusterHandler.ItemAdjusterIncrementorHandler("radii increase", Keyboard.KEY_ADD, Reference.MOD_NAME),
				new ItemAdjusterHandler.ItemAdjusterDecrementorHandler("radii decrease", Keyboard.KEY_SUBTRACT, Reference.MOD_NAME));
		FMLCommonHandler.instance().bus().register(keyBindingHandler);
	}

}
