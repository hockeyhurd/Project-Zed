package com.projectzed.mod;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

import com.hockeyhurd.api.math.TimeLapse;
import com.hockeyhurd.api.util.LogHelper;
import com.projectzed.api.energy.source.EnumColor;
import com.projectzed.mod.block.BlockFabricationTable;
import com.projectzed.mod.block.BlockNuclearChamberLock;
import com.projectzed.mod.block.BlockNuclearChamberWall;
import com.projectzed.mod.block.BlockNuclearReactantCore;
import com.projectzed.mod.block.BlockThickenedGlass;
import com.projectzed.mod.block.container.BlockEnergyCell;
import com.projectzed.mod.block.container.BlockEnergyPipeClear;
import com.projectzed.mod.block.container.BlockEnergyPipeOrange;
import com.projectzed.mod.block.container.BlockEnergyPipeRed;
import com.projectzed.mod.block.container.BlockRFBridge;
import com.projectzed.mod.block.generator.BlockFurnaceGenerator;
import com.projectzed.mod.block.generator.BlockNuclearController;
import com.projectzed.mod.block.generator.BlockSolarArray;
import com.projectzed.mod.block.machines.BlockIndustrialCentrifuge;
import com.projectzed.mod.block.machines.BlockIndustrialCrusher;
import com.projectzed.mod.block.machines.BlockIndustrialFurnace;
import com.projectzed.mod.block.machines.BlockIndustrialLumberMill;
import com.projectzed.mod.block.machines.BlockIndustrialMetalPress;
import com.projectzed.mod.block.machines.BlockMachineContainer;
import com.projectzed.mod.block.ore.BlockAluminiumOre;
import com.projectzed.mod.block.ore.BlockCopperOre;
import com.projectzed.mod.block.ore.BlockNickelOre;
import com.projectzed.mod.block.ore.BlockTitaniumOre;
import com.projectzed.mod.block.ore.BlockUraniumOre;
import com.projectzed.mod.creativetabs.ProjectZedCreativeTab;
import com.projectzed.mod.handler.ConfigHandler;
import com.projectzed.mod.item.ItemConductiveCoil;
import com.projectzed.mod.item.ItemDongle;
import com.projectzed.mod.item.ItemDustGold;
import com.projectzed.mod.item.ItemDustIron;
import com.projectzed.mod.item.ItemForgingHammer;
import com.projectzed.mod.item.ItemFuelRod;
import com.projectzed.mod.item.ItemGearAluminium;
import com.projectzed.mod.item.ItemMcUReader;
import com.projectzed.mod.item.ItemScrew;
import com.projectzed.mod.item.ItemSheetAluminium;
import com.projectzed.mod.item.metals.ItemDustAluminium;
import com.projectzed.mod.item.metals.ItemDustCopper;
import com.projectzed.mod.item.metals.ItemDustNickel;
import com.projectzed.mod.item.metals.ItemDustTitanium;
import com.projectzed.mod.item.metals.ItemDustUranium;
import com.projectzed.mod.item.metals.ItemIngotAluminium;
import com.projectzed.mod.item.metals.ItemIngotCopper;
import com.projectzed.mod.item.metals.ItemIngotNickel;
import com.projectzed.mod.item.metals.ItemIngotTitanium;
import com.projectzed.mod.item.metals.ItemIngotUranium;
import com.projectzed.mod.proxy.CommonProxy;
import com.projectzed.mod.util.ModsLoadedHelper;
import com.projectzed.mod.util.Reference;
import com.projectzed.mod.worldgen.OreWorldgen;

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
	public static Block machineContainer;
	public static Block thickenedGlass;
	public static Block nuclearChamberWall;
	public static Block nuclearChamberLock;
	public static Block nuclearReactantCore;
	
	// Machines: 
	public static Block solarArray, solarArrayLV, solarArrayMV, solarArrayHV;
	public static Block furnaceGen;
	public static Block fusionController, fissionController;
	public static Block fabricationTable;
	public static Block industrialFurnace;
	public static Block industrialCrusher;
	public static Block industrialLumberMill;
	public static Block industrialMetalPress;
	public static Block industrialCentrifuge;
	
	// Containers:
	public static Block energyPipeRed;
	public static Block energyPipeOrange;
	public static Block energyPipeClear;
	public static Block energyCellTier0, energyCellTier1, energyCellTier2, energyCellTier3;
	
	// RF STUFF:
	public static Block bridgeMcUToRF, bridgeRFToMcU;
	
	// Ores
	public static Block oreTitanium;
	public static Block oreCopper;
	public static Block oreNickel;
	public static Block oreAluminium;
	public static Block oreUranium;
	
	// Items:
	public static Item itemDongle;
	public static Item forgingHammer;
	public static Item mcuReader;
	public static Item screw;
	public static Item sheetAluminium;
	public static Item gearAluminium;
	public static Item conductiveCoil;
	
	// Metals:
	public static Item dustGold;
	public static Item dustIron;
	public static Item dustTitanium;
	public static Item dustCopper;
	public static Item dustNickel;
	public static Item dustAluminium;
	public static Item dustUranium;
	public static Item enrichedUranium;
	
	public static Item ingotTitanium;
	public static Item ingotCopper;
	public static Item ingotNickel;
	public static Item ingotAluminium;
	public static Item ingotUranium;
	
	// Fuels
	public static Item emptyFuelRod;
	public static Item fullFuelRod;
	
	// Worldgen stuff:
	public static OreWorldgen worldgenTitanium;
	public static OreWorldgen worldgenNickel;
	public static OreWorldgen worldgenAluminium;
	public static OreWorldgen worldgenCopper;
	public static OreWorldgen worldgenUranium;
	
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
		
		ModsLoadedHelper.instance().init();
		ModsLoadedHelper.instance().logFindings(logHelper);
		
		logHelper.info("Pre-init finished succesfully after", tl.getEffectiveTimeSince(), "ms!");
	}
	
	@EventHandler
	public void init(FMLInitializationEvent event) {
		TimeLapse tl = new TimeLapse();
		logHelper.info("Init started");
		
		loadObj();
		proxy.init();
		proxy.registerRenderInformation();
		
		logHelper.info("Init finished successfully after", tl.getEffectiveTimeSince(), "ms!");
	}
	
	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		TimeLapse tl = new TimeLapse();
		logHelper.info("Post-Init started");
		
		if (configHandler.allowUpdating()) {
			proxy.registerUpdateHandler();
			if (!proxy.updateFlag) logHelper.warn("Found an update!");
			else logHelper.info("Everything is up to date!");
		}
		else logHelper.warn("Skipping checking for updates. WARNING: bugs may exist!");
		
		logHelper.info("Post-Init finished successfully after", tl.getEffectiveTimeSince(), "ms!");
	}
	
	/**
	 * Method used to instantiate objects: blocks, items, etc.
	 */
	private void loadObj() {
		// Blocks:
		machineContainer = new BlockMachineContainer();
		thickenedGlass = new BlockThickenedGlass();
		nuclearChamberWall = new BlockNuclearChamberWall();
		nuclearChamberLock = new BlockNuclearChamberLock();
		nuclearReactantCore = new BlockNuclearReactantCore();

		// Generators:
		solarArray = new BlockSolarArray(Material.rock, (byte) 0);
		solarArrayLV = new BlockSolarArray(Material.rock, (byte) 1);
		solarArrayMV = new BlockSolarArray(Material.rock, (byte) 2);
		solarArrayHV = new BlockSolarArray(Material.rock, (byte) 3);
		furnaceGen = new BlockFurnaceGenerator(Material.rock);
		fusionController = new BlockNuclearController(Material.rock, true);
		fissionController = new BlockNuclearController(Material.rock, false);
		
		// Machines:
		fabricationTable = new BlockFabricationTable(Material.rock);
		industrialFurnace = new BlockIndustrialFurnace();
		industrialCrusher = new BlockIndustrialCrusher();
		industrialLumberMill = new BlockIndustrialLumberMill();
		industrialMetalPress = new BlockIndustrialMetalPress();
		industrialCentrifuge = new BlockIndustrialCentrifuge();
		
		// Containers:
		energyPipeRed = new BlockEnergyPipeRed(Material.rock, "energyPipeRed", EnumColor.RED);
		energyPipeOrange = new BlockEnergyPipeOrange(Material.rock, "energyPipeOrange", EnumColor.ORANGE);
		energyPipeClear = new BlockEnergyPipeClear(Material.rock, "energyPipeClear", EnumColor.CLEAR);
		energyCellTier0 = new BlockEnergyCell(Material.rock, "energyCellTier0");
		energyCellTier1 = new BlockEnergyCell(Material.rock, "energyCellTier1");
		energyCellTier2 = new BlockEnergyCell(Material.rock, "energyCellTier2");
		energyCellTier3 = new BlockEnergyCell(Material.rock, "energyCellTier3");
		
		// RF STUFF:
		if (ModsLoadedHelper.instance().cofhCore) { 
			bridgeMcUToRF = new BlockRFBridge(Material.rock, false);
			bridgeRFToMcU = new BlockRFBridge(Material.rock, true);
		}
		
		// Ores:
		oreTitanium = new BlockTitaniumOre(Material.rock, assetDir, "oreTitanium");
		oreCopper = new BlockCopperOre(Material.rock, assetDir, "oreCopper");
		oreNickel = new BlockNickelOre(Material.rock, assetDir, "oreNickel"); 
		oreAluminium = new BlockAluminiumOre(Material.rock, assetDir, "oreAluminium");
		oreUranium = new BlockUraniumOre(Material.rock, assetDir, "oreUranium");
		
		// Items:
		itemDongle = new ItemDongle();
		forgingHammer = new ItemForgingHammer();
		mcuReader = new ItemMcUReader();
		screw = new ItemScrew("screw", assetDir);
		sheetAluminium = new ItemSheetAluminium("sheetAluminium", assetDir);
		gearAluminium = new ItemGearAluminium("gearAluminium", assetDir);
		conductiveCoil = new ItemConductiveCoil("conductiveCoil", assetDir);
		
		// Metals:
		dustGold = new ItemDustGold("dustGold", assetDir);
		dustIron = new ItemDustIron("dustIron", assetDir);
		dustTitanium = new ItemDustTitanium("dustTitanium", assetDir);
		dustCopper = new ItemDustCopper("dustCopper", assetDir);
		dustNickel = new ItemDustNickel("dustNickel", assetDir);
		dustAluminium = new ItemDustAluminium("dustAluminium", assetDir);
		dustUranium = new ItemDustUranium("dustUranium", assetDir, false);
		enrichedUranium = new ItemDustUranium("enrichedUranium", assetDir, true);
		
		ingotTitanium = new ItemIngotTitanium("ingotTitanium", assetDir);
		ingotCopper = new ItemIngotCopper("ingotCopper", assetDir);
		ingotNickel = new ItemIngotNickel("ingotNickel", assetDir);
		ingotAluminium = new ItemIngotAluminium("ingotAluminium", assetDir);
		ingotUranium = new ItemIngotUranium("ingotUranium", assetDir);
		
		// Fuels:
		emptyFuelRod = new ItemFuelRod("emptyFuelRod", assetDir, true);
		fullFuelRod = new ItemFuelRod("fuelRod", assetDir, false);
		
		// Worldgen:
		worldgenTitanium = new OreWorldgen(oreTitanium, 6, 3, 6, 8, 24);
		worldgenNickel = new OreWorldgen(oreNickel, 6, 4, 6, 10, 30);
		worldgenAluminium = new OreWorldgen(oreAluminium, 8, 3, 7, 10, 64);
		worldgenCopper = new OreWorldgen(oreCopper, 10, 5, 10, 40, 75);
		worldgenUranium = new OreWorldgen(oreUranium, 4, 2, 4, 4, 16);
	}

}
