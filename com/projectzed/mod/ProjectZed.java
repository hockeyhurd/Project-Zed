package com.projectzed.mod;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

import com.hockeyhurd.api.math.TimeLapse;
import com.hockeyhurd.api.util.LogHelper;
import com.projectzed.api.source.EnumColor;
import com.projectzed.mod.block.container.BlockEnergyPipe;
import com.projectzed.mod.block.generator.BlockSolarArray;
import com.projectzed.mod.block.machines.BlockIndustrialCrusher;
import com.projectzed.mod.block.machines.BlockIndustrialFurnace;
import com.projectzed.mod.block.ore.BlockCopperOre;
import com.projectzed.mod.block.ore.BlockNickelOre;
import com.projectzed.mod.block.ore.BlockTitaniumOre;
import com.projectzed.mod.creativetabs.ProjectZedCreativeTab;
import com.projectzed.mod.handler.ConfigHandler;
import com.projectzed.mod.item.ItemDustGold;
import com.projectzed.mod.item.ItemDustIron;
import com.projectzed.mod.item.metals.ItemDustCopper;
import com.projectzed.mod.item.metals.ItemDustNickel;
import com.projectzed.mod.item.metals.ItemDustTitanium;
import com.projectzed.mod.item.metals.ItemIngotCopper;
import com.projectzed.mod.item.metals.ItemIngotNickel;
import com.projectzed.mod.item.metals.ItemIngotTitanium;
import com.projectzed.mod.proxy.CommonProxy;
import com.projectzed.mod.registry.BlockRegistry;
import com.projectzed.mod.registry.ItemRegistry;
import com.projectzed.mod.registry.TileEntityRegistry;
import com.projectzed.mod.util.Reference;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

/**
 * Project-Zed's main class covering all initializations.
 * 
 * @author hockeyhurd
 * @version Oct 19, 2014
 */
@Mod(modid = Reference.MOD_NAME, name = Reference.MOD_NAME, version = Reference.VERSION, dependencies = "required-after:HCoreLib")
public class ProjectZed {

	@SidedProxy(clientSide = "com.projectzed.mod.proxy.ClientProxy", serverSide = "com.projectzed.mod.proxy.CommonProxy")
	public static CommonProxy proxy;
	
	@Instance(Reference.MOD_NAME)
	public static ProjectZed instance;
	
	public static LogHelper logHelper;
	public static ConfigHandler configHandler;
	public static final String assetDir = Reference.MOD_NAME.toLowerCase() + ":";
	public static final String modID = Reference.MOD_NAME;
	
	// Creative Tabs:
	public static CreativeTabs modCreativeTab = new ProjectZedCreativeTab(CreativeTabs.getNextID(), "Project-Zed");
	
	// Blocks: 
	public static Block solarArray;
	public static Block industrialFurnace;
	public static Block industrialCrusher;
	public static Block energyPipe;
	
	// Ores
	public static Block oreTitanium;
	public static Block oreCopper;
	public static Block oreNickel;
	
	// Items:
	
	// Metals:
	public static Item dustGold;
	public static Item dustIron;
	public static Item dustTitanium;
	public static Item dustCopper;
	public static Item dustNickel;
	
	public static Item ingotTitanium;
	public static Item ingotCopper;
	public static Item ingotNickel;
	
	/**
	 * Default constructor.
	 */
	public ProjectZed() {
	}
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		TimeLapse tl = new TimeLapse();
		logHelper = new LogHelper(Reference.class);
		
		logHelper.info("Pre-init started, looking for config info!");
		configHandler = new ConfigHandler(event, Reference.class);
		configHandler.handleConfiguration();
		logHelper.info("Config loaded successfully! Patching mod now!"); 
		
		/*lh.info("Detecting other soft-dependent mods.");
		ModsLoadedHelper.init();
		
		Iterator iter = ModsLoadedHelper.getEntries().iterator();
		do {
			Entry<String, Boolean> current = (Entry<String, Boolean>) iter.next();
			if (current.getValue()) lh.info(current.getKey(), "detected! Wrapping into mod!");
			else lh.warn(current.getKey(), "not detected!");
		}
		while (iter.hasNext());*/
		
		logHelper.info("Pre-init finished succesfully after", tl.getEffectiveTimeSince(), "ms!");
	}
	
	@EventHandler
	public void init(FMLInitializationEvent event) {
		TimeLapse tl = new TimeLapse();
		logHelper.info("Init started");
		
		loadObj();
		BlockRegistry.instance().init(instance.getClass());
		ItemRegistry.instance().init(instance.getClass());
		TileEntityRegistry.instance().init();
		proxy.init();
		proxy.registerRenderInformation();
		
		logHelper.info("Init finished successfully after", tl.getEffectiveTimeSince(), "ms!");
	}
	
	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		TimeLapse tl = new TimeLapse();
		logHelper.info("Post-Init started");
		
		/*if (configHandler.allowUpdating()) {
			proxy.registerUpdateHandler();
			if (!proxy.updateFlag) lh.warn("Found an update!");
			else lh.info("Everything is up to date!");
		}
		else lh.warn("Skipping checking for updates. WARNING: bugs may exist!");*/
		
		logHelper.info("Post-Init finished successfully after", tl.getEffectiveTimeSince(), "ms!");
	}
	
	/**
	 * Method used to instantiate objects: blocks, items, etc.
	 */
	private void loadObj() {
		// Blocks:
		solarArray = new BlockSolarArray(Material.rock);
		industrialFurnace = new BlockIndustrialFurnace();
		industrialCrusher = new BlockIndustrialCrusher();
		energyPipe = new BlockEnergyPipe(Material.rock, "energyPipe", EnumColor.RED);
		
		// Ores:
		oreTitanium = new BlockTitaniumOre(Material.rock, assetDir, "oreTitanium");
		oreCopper = new BlockCopperOre(Material.rock, assetDir, "oreCopper");
		oreNickel = new BlockNickelOre(Material.rock, assetDir, "oreNickel");
		
		// Items:
		
		// Metals:
		dustGold = new ItemDustGold("dustGold", assetDir);
		dustIron = new ItemDustIron("dustIron", assetDir);
		dustTitanium = new ItemDustTitanium("dustTitanium", assetDir);
		dustCopper = new ItemDustCopper("dustCopper", assetDir);
		dustNickel = new ItemDustNickel("dustNickel", assetDir);
		
		ingotTitanium = new ItemIngotTitanium("ingotTitanium", assetDir);
		ingotCopper = new ItemIngotCopper("ingotCopper", assetDir);
		ingotNickel = new ItemIngotNickel("ingotNickel", assetDir);
	}

}
