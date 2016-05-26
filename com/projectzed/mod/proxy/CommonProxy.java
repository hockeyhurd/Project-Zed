/* This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under the terms of the GNU General Public 
* License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Project-Zed is 
* distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
* PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along 
* with Project-Zed. If not, see <http://www.gnu.org/licenses/>
*/
package com.projectzed.mod.proxy;

import com.hockeyhurd.hcorelib.api.handler.NotifyPlayerOnJoinHandler;
import com.hockeyhurd.hcorelib.api.handler.UpdateHandler;
import com.projectzed.api.util.Sound;
import com.projectzed.mod.ProjectZed;
import com.projectzed.mod.handler.*;
import com.projectzed.mod.registry.*;
import com.projectzed.mod.registry.tools.ChainsawSetRegistry;
import com.projectzed.mod.registry.tools.DrillSetRegistry;
import com.projectzed.mod.util.OutputUtil;
import com.projectzed.mod.util.Reference;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

/**
 * Common proxy for both client and server.
 * 
 * @author hockeyhurd
 * @version Oct 19, 2014
 */
public class CommonProxy {

	protected UpdateHandler updateHandler;
	protected HashMap<String, String> map;
	public boolean updateFlag = false;
	protected static GuiHandler guiHandler;
	
	/**
	 * Default Constructor
	 */
	public CommonProxy() {
	}
	 
	/**
	 * To be used in the ClientProxy and overridden.
	 */
	public void registerRenderInformation() {
	}

	/**
	 * To be used in the ClientProxy and overridden.
	 */
	public void registerInputHandlers() {
	}
	
	/**
	 * Method used for init everything: blocks, items, handlers, etc.
	 */
	public void init() {
		registerBlocks();
		registerItems();
		registerOreDictionaryEntries();
		registerWorldgen();
		registerEntities();
		registerTileEntities();
		registerGuiHandler();
		registerSounds();
		registerRegisters();
		registerEventHandlers();
	}
	
	private void registerBlocks() {
		BlockRegistry.instance().init(ProjectZed.class);
		for (Block b : BlockRegistry.instance().getBlocks()) {
			if (b != null) GameRegistry.registerBlock(b, b.getUnlocalizedName());
		}
	}
	
	private void registerItems() {
		ItemRegistry.instance().init(ProjectZed.class);
		for (Item i : ItemRegistry.instance().getItems()) {
			if (i != null) GameRegistry.registerItem(i, i.getUnlocalizedName());
		}
	}
	
	private void registerOreDictionaryEntries() {
		for (Block b : BlockRegistry.instance().getOreBlocks()) {
			if (b != null) OreDictionary.registerOre(BlockRegistry.getBlockName(b), b);
		}
		
		for (Item i : ItemRegistry.instance().getItemOres()) {
			if (i != null) OreDictionary.registerOre(ItemRegistry.getBlockName(i), i);
		}

		OreDictionary.registerOre("stoneBricks", ProjectZed.stoneBricksDefault);
		OreDictionary.registerOre("stoneBricks", ProjectZed.stoneBricksWide);
		OreDictionary.registerOre("stoneBricks", ProjectZed.stoneBricksRed);
		OreDictionary.registerOre("stoneBricks", ProjectZed.stoneBricksBlue);
		OreDictionary.registerOre("stoneBricks", ProjectZed.stoneBricksGreen);
		OreDictionary.registerOre("stoneBricks", ProjectZed.stoneBricksPurple);
		OreDictionary.registerOre("stoneBricksStairs", ProjectZed.stoneBricksDefaultStairs);
		OreDictionary.registerOre("stoneBricksStairs", ProjectZed.stoneBricksWideStairs);
		OreDictionary.registerOre("stoneBricksStairs", ProjectZed.stoneBricksRedStairs);
		OreDictionary.registerOre("stoneBricksStairs", ProjectZed.stoneBricksBlueStairs);
		OreDictionary.registerOre("stoneBricksStairs", ProjectZed.stoneBricksGreenStairs);
		OreDictionary.registerOre("stoneBricksStairs", ProjectZed.stoneBricksPurpleStairs);

		// OreDictionary.registerOre("plateAluminium", ProjectZed.plateAluminium);
		OreDictionary.registerOre("plateAluminum", ProjectZed.plateAluminium);
		// OreDictionary.registerOre("plateReinforced", ProjectZed.plateReinforced);
		OreDictionary.registerOre("mixedAlloy", ProjectZed.mixedAlloy);
		OreDictionary.registerOre("dustMixedAlloy", ProjectZed.dustMixedAlloy);
		OreDictionary.registerOre("dustCoal", ProjectZed.dustCoal);
	}
	
	private void registerWorldgen() {
		if (ProjectZed.worldgenTitanium != null) GameRegistry.registerWorldGenerator(ProjectZed.worldgenTitanium, 1);
		if (ProjectZed.worldgenNickel != null) GameRegistry.registerWorldGenerator(ProjectZed.worldgenNickel, 1);
		if (ProjectZed.worldgenAluminium != null) GameRegistry.registerWorldGenerator(ProjectZed.worldgenAluminium, 1);
		if (ProjectZed.worldgenCopper != null) GameRegistry.registerWorldGenerator(ProjectZed.worldgenCopper, 1);
		if (ProjectZed.worldgenUranium != null) GameRegistry.registerWorldGenerator(ProjectZed.worldgenUranium, 1);
		if (ProjectZed.worldgenOil != null) GameRegistry.registerWorldGenerator(ProjectZed.worldgenOil, 1);
	}
	
	private void registerEntities() {
		// EntityRegistry.instance().registerGlobalEntityID(EntityAtomicBomb.class, "entityAtomicBomb", 0);
		
		PZEntityRegistry.instance().init();

		Iterator iter = PZEntityRegistry.instance().getMap().entrySet().iterator();
		while (iter.hasNext()) {
			Entry<Class<? extends Entity>, String> entry = (Entry<Class<? extends Entity>, String>) iter.next();
			if (entry.getKey() != null && entry.getValue() != null && entry.getValue().length() > 0) 
				// EntityRegistry.instance().registerGlobalEntityID(entry.getKey(), entry.getValue(), PZEntityRegistry.instance().getNextID());
				EntityRegistry.instance().registerGlobalEntityID(entry.getKey(), entry.getValue(), EntityRegistry.findGlobalUniqueEntityId());
		}
	}
	
	private void registerTileEntities() {
		TileEntityRegistry.instance().init();
		
		Iterator iter = TileEntityRegistry.instance().getMapping().entrySet().iterator();
		while (iter.hasNext()) {
			Entry<Class<? extends TileEntity>, String> entry = (Entry<Class<? extends TileEntity>, String>) iter.next();
			if (entry.getKey() != null && entry.getValue() != null) GameRegistry.registerTileEntity(entry.getKey(), entry.getValue());
		}
	}
	
	private void registerGuiHandler() {
		if (guiHandler != null) NetworkRegistry.INSTANCE.registerGuiHandler(ProjectZed.instance, guiHandler);
		else {
			guiHandler = new GuiHandler();
			NetworkRegistry.INSTANCE.registerGuiHandler(ProjectZed.instance, guiHandler);
		}
	}

	private void registerSounds() {
		Sound.init();
	}

	protected void registerRegisters() {
		CraftingRegistry.instance().init();
		
		List<ShapelessOreRecipe> shapelessList = CraftingRegistry.instance().getShapelessList();
		if (shapelessList != null && shapelessList.size() > 0) {
			for (int i = 0; i < shapelessList.size(); i++) {
				ShapelessOreRecipe rec = shapelessList.get(i);
				if (rec != null) GameRegistry.addRecipe(rec);
				else ProjectZed.logHelper.severe("Error registering shapeless recipe at index", i);
			}
		}
		
		List<ShapedOreRecipe> shapedList = CraftingRegistry.instance().getShapedList();
		if (shapedList != null && shapedList.size() > 0) {
			for (int i = 0; i < shapedList.size(); i++) {
				ShapedOreRecipe rec = shapedList.get(i);
				if (rec != null) GameRegistry.addRecipe(rec);
				else ProjectZed.logHelper.severe("Error registering shaped recipe at index", i);
			}
		}
		
		FurnaceRecipeRegistry furn = FurnaceRecipeRegistry.instance();
		furn.init();
		OutputUtil out;
		
		for (Object obj : furn.getMap().keySet()) {
			if (obj != null && furn.getMap().containsKey(obj)) {
				out = furn.getMap().get(obj);
				if (out == null || !out.isValid()) continue;
				
				if (obj instanceof Block) GameRegistry.addSmelting((Block) obj, out.stack, out.xp);
				else if (obj instanceof Item) GameRegistry.addSmelting((Item) obj, out.stack, out.xp);
			}
		}
		
		CrusherRecipesRegistry.init();
		LumberMillRecipesRegistry.init();
		MetalPressRecipesRegistry.init();
		CentrifugeRecipeRegistry.init();
		DrillSetRegistry.instance().init();
		ChainsawSetRegistry.instance().init();
	}
	
	protected void registerEventHandlers() {
		PacketHandler.init();
		// FMLCommonHandler.instance().bus().register(CraftingEventHandler.instance());
		MinecraftForge.EVENT_BUS.register(PlayerEventHandler.instance());
		MinecraftForge.EVENT_BUS.register(ItemHoverEventHandler.instance());
		
		WorldChunkHandler.instance().registerMod(ProjectZed.instance);
	}
	
	public void registerUpdateHandler() {
		updateHandler = new UpdateHandler(Reference.class);
		updateHandler.check();
		this.map = updateHandler.getMap();
		this.updateFlag = updateHandler.getUpToDate();
		
		MinecraftForge.EVENT_BUS.register(new NotifyPlayerOnJoinHandler(updateHandler, this.map, Reference.class, this.updateFlag, true, ProjectZed.configHandler.allowUpdating()));
	}

}
