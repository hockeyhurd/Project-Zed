/* This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under the terms of the GNU General Public 
* License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Project-Zed is 
* distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
* PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along 
* with Project-Zed. If not, see <http://www.gnu.org/licenses/>
*/
package com.projectzed.mod.proxy;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import net.minecraft.block.Block;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

import com.hockeyhurd.api.handler.NotifyPlayerOnJoinHandler;
import com.hockeyhurd.api.handler.UpdateHandler;
import com.projectzed.mod.ProjectZed;
import com.projectzed.mod.handler.CraftingEventHandler;
import com.projectzed.mod.handler.GuiHandler;
import com.projectzed.mod.handler.ItemHoverEventHandler;
import com.projectzed.mod.handler.PacketHandler;
import com.projectzed.mod.handler.PlayerEventHandler;
import com.projectzed.mod.registry.BlockRegistry;
import com.projectzed.mod.registry.CentrifugeRecipeRegistry;
import com.projectzed.mod.registry.CraftingRegistry;
import com.projectzed.mod.registry.CrusherRecipesRegistry;
import com.projectzed.mod.registry.ItemRegistry;
import com.projectzed.mod.registry.LumberMillRecipesRegistry;
import com.projectzed.mod.registry.MetalPressRecipesRegistry;
import com.projectzed.mod.registry.TileEntityRegistry;
import com.projectzed.mod.util.Reference;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;

/**
 * Common proxy for both client and server.
 * 
 * @author hockeyhurd
 * @version Oct 19, 2014
 */
public class CommonProxy {

	protected UpdateHandler updateHandler;
	protected HashMap<Short, String> map;
	public boolean updateFlag = false;
	protected static GuiHandler guiHandler;
	
	/**
	 * Default Constructor
	 */
	public CommonProxy() {
	}
	 
	/**
	 * To be used in the ClientProxy and overriden.
	 */
	public void registerRenderInformation() {
	}
	
	/**
	 * Method used for init everything: blocks, items, handlers, etc.
	 */
	public void init() {
		registerBlocks();
		registerItems();
		registerOreDictionaryEntries();
		registerWorldgen();
		registerTileEntities();
		registerGuiHandler();
		registerFurnaceRecipes();
		registerRegisters();
		registerEventHandlers();
	}

	private void registerBlocks() {
		BlockRegistry.instance().init(ProjectZed.instance.getClass());
		for (Block b : BlockRegistry.instance().getBlocks()) {
			if (b != null) GameRegistry.registerBlock(b, b.getUnlocalizedName());
		}
	}
	
	private void registerItems() {
		ItemRegistry.instance().init(ProjectZed.instance.getClass());
		for (Item i : ItemRegistry.instance().getItems()) {
			if (i != null) GameRegistry.registerItem(i, i.getUnlocalizedName());
		}
	}
	
	private void registerOreDictionaryEntries() {
		for (Block b : BlockRegistry.instance().getOreBlocks()) {
			if (b != null) OreDictionary.registerOre(BlockRegistry.instance().getBlockName(b), b);
		}
		
		for (Item i : ItemRegistry.instance().getItemOres()) {
			if (i != null) OreDictionary.registerOre(ItemRegistry.instance().getBlockName(i), i);
		}
		
		OreDictionary.registerOre("plateAluminium", ProjectZed.sheetAluminium);
		OreDictionary.registerOre("plateReinforced", ProjectZed.sheetReinforced);
		OreDictionary.registerOre("mixedAlloy", ProjectZed.mixedAlloy);
		OreDictionary.registerOre("dustCoal", ProjectZed.dustCoal);
	}
	
	private void registerWorldgen() {
		GameRegistry.registerWorldGenerator(ProjectZed.worldgenTitanium, 1);
		GameRegistry.registerWorldGenerator(ProjectZed.worldgenNickel, 1);
		GameRegistry.registerWorldGenerator(ProjectZed.worldgenAluminium, 1);
		GameRegistry.registerWorldGenerator(ProjectZed.worldgenCopper, 1);
		GameRegistry.registerWorldGenerator(ProjectZed.worldgenUranium, 1);
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
		
		CrusherRecipesRegistry.init();
		LumberMillRecipesRegistry.init();
		MetalPressRecipesRegistry.init();
		CentrifugeRecipeRegistry.init();
	}
	
	private void registerEventHandlers() {
		PacketHandler.init();
		FMLCommonHandler.instance().bus().register(CraftingEventHandler.instance());
		MinecraftForge.EVENT_BUS.register(PlayerEventHandler.instance());
		MinecraftForge.EVENT_BUS.register(ItemHoverEventHandler.instance());
	}
	
	protected void registerFurnaceRecipes() {
		GameRegistry.addSmelting(ProjectZed.dustIron, new ItemStack(Items.iron_ingot), 25f);
		GameRegistry.addSmelting(ProjectZed.dustGold, new ItemStack(Items.gold_ingot), 25f);
		
		GameRegistry.addSmelting(ProjectZed.oreTitanium, new ItemStack(ProjectZed.ingotTitanium, 1), 50f);
		GameRegistry.addSmelting(ProjectZed.dustTitanium, new ItemStack(ProjectZed.ingotTitanium, 1), 50f);
		
		GameRegistry.addSmelting(ProjectZed.oreCopper, new ItemStack(ProjectZed.ingotCopper, 1), 50f);
		GameRegistry.addSmelting(ProjectZed.dustCopper, new ItemStack(ProjectZed.ingotCopper, 1), 50f);
		
		GameRegistry.addSmelting(ProjectZed.oreNickel, new ItemStack(ProjectZed.ingotNickel, 1), 50f);
		GameRegistry.addSmelting(ProjectZed.dustNickel, new ItemStack(ProjectZed.ingotNickel, 1), 50f);
		
		GameRegistry.addSmelting(ProjectZed.oreAluminium, new ItemStack(ProjectZed.ingotAluminium, 1), 50f);
		GameRegistry.addSmelting(ProjectZed.dustAluminium, new ItemStack(ProjectZed.ingotAluminium, 1), 50f);
	}
	
	public void registerUpdateHandler() {
		updateHandler = new UpdateHandler(Reference.class);
		updateHandler.check();
		this.map = updateHandler.getMap();
		this.updateFlag = updateHandler.getUpToDate();
		
		MinecraftForge.EVENT_BUS.register(new NotifyPlayerOnJoinHandler(updateHandler, this.map, Reference.class, this.updateFlag, true, ProjectZed.configHandler.allowUpdating()));
	}

}
