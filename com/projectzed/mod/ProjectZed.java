/* This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under the terms of the GNU General Public 
* License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Project-Zed is 
* distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
* PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along 
* with Project-Zed. If not, see <http://www.gnu.org/licenses/>
*/
package com.projectzed.mod;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraftforge.common.util.EnumHelper;

import com.hockeyhurd.api.math.TimeLapse;
import com.hockeyhurd.api.util.LogHelper;
import com.projectzed.api.energy.source.EnumColor;
import com.projectzed.mod.block.BlockAtomicBomb;
import com.projectzed.mod.block.BlockFabricationTable;
import com.projectzed.mod.block.BlockNuclearChamberLock;
import com.projectzed.mod.block.BlockNuclearChamberWall;
import com.projectzed.mod.block.BlockNuclearControlPort;
import com.projectzed.mod.block.BlockNuclearReactantCore;
import com.projectzed.mod.block.BlockNuclearReactorGlass;
import com.projectzed.mod.block.BlockThickenedGlass;
import com.projectzed.mod.block.BlockWickedClearGlass;
import com.projectzed.mod.block.container.BlockEnergyCell;
import com.projectzed.mod.block.container.BlockEnergyPipeClear;
import com.projectzed.mod.block.container.BlockEnergyPipeOrange;
import com.projectzed.mod.block.container.BlockEnergyPipeRed;
import com.projectzed.mod.block.container.BlockLiquidNode;
import com.projectzed.mod.block.container.BlockLiquiductBlue;
import com.projectzed.mod.block.container.BlockLiquiductClear;
import com.projectzed.mod.block.container.BlockNuclearIOPort;
import com.projectzed.mod.block.container.BlockNuclearPowerPort;
import com.projectzed.mod.block.container.BlockRFBridge;
import com.projectzed.mod.block.container.BlockTankTier0;
import com.projectzed.mod.block.container.BlockTankTier1;
import com.projectzed.mod.block.container.BlockTankTier2;
import com.projectzed.mod.block.container.BlockTankTier3;
import com.projectzed.mod.block.generator.BlockFurnaceGenerator;
import com.projectzed.mod.block.generator.BlockNuclearController;
import com.projectzed.mod.block.generator.BlockSolarArray;
import com.projectzed.mod.block.machines.BlockIndustrialCentrifuge;
import com.projectzed.mod.block.machines.BlockIndustrialCrusher;
import com.projectzed.mod.block.machines.BlockIndustrialEnergizer;
import com.projectzed.mod.block.machines.BlockIndustrialFurnace;
import com.projectzed.mod.block.machines.BlockIndustrialLoader;
import com.projectzed.mod.block.machines.BlockIndustrialLumberMill;
import com.projectzed.mod.block.machines.BlockIndustrialMetalPress;
import com.projectzed.mod.block.machines.BlockMachineContainer;
import com.projectzed.mod.block.machines.BlockStoneCraftingTable;
import com.projectzed.mod.block.ore.BlockAluminium;
import com.projectzed.mod.block.ore.BlockAluminiumOre;
import com.projectzed.mod.block.ore.BlockCopper;
import com.projectzed.mod.block.ore.BlockCopperOre;
import com.projectzed.mod.block.ore.BlockNickel;
import com.projectzed.mod.block.ore.BlockNickelOre;
import com.projectzed.mod.block.ore.BlockTitanium;
import com.projectzed.mod.block.ore.BlockTitaniumOre;
import com.projectzed.mod.block.ore.BlockUranium;
import com.projectzed.mod.block.ore.BlockUraniumOre;
import com.projectzed.mod.creativetabs.ProjectZedCreativeTab;
import com.projectzed.mod.handler.ConfigHandler;
import com.projectzed.mod.handler.SortingConfigHandler;
import com.projectzed.mod.item.ItemConductiveCoil;
import com.projectzed.mod.item.ItemDongle;
import com.projectzed.mod.item.ItemForgingHammer;
import com.projectzed.mod.item.ItemFuelRod;
import com.projectzed.mod.item.ItemGear;
import com.projectzed.mod.item.ItemMcUReader;
import com.projectzed.mod.item.ItemScrew;
import com.projectzed.mod.item.metals.ItemDustAluminium;
import com.projectzed.mod.item.metals.ItemDustCoal;
import com.projectzed.mod.item.metals.ItemDustCopper;
import com.projectzed.mod.item.metals.ItemDustGold;
import com.projectzed.mod.item.metals.ItemDustIron;
import com.projectzed.mod.item.metals.ItemDustNickel;
import com.projectzed.mod.item.metals.ItemDustTitanium;
import com.projectzed.mod.item.metals.ItemDustUranium;
import com.projectzed.mod.item.metals.ItemIngotAluminium;
import com.projectzed.mod.item.metals.ItemIngotCopper;
import com.projectzed.mod.item.metals.ItemIngotNickel;
import com.projectzed.mod.item.metals.ItemIngotTitanium;
import com.projectzed.mod.item.metals.ItemIngotUranium;
import com.projectzed.mod.item.metals.ItemMixedAlloy;
import com.projectzed.mod.item.metals.ItemSheetAluminium;
import com.projectzed.mod.item.metals.ItemSheetReinforced;
import com.projectzed.mod.item.tools.ItemMiningDrill;
import com.projectzed.mod.item.tools.ItemWrench;
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
	public static SortingConfigHandler sortingConfigHandler;
	public static final String assetDir = Reference.MOD_NAME.toLowerCase() + ":";
	public static final String modID = Reference.MOD_NAME;
	
	// Creative Tabs:
	public static CreativeTabs modCreativeTab = new ProjectZedCreativeTab(CreativeTabs.getNextID(), "Project-Zed");
	
	// Blocks:
	public static Block machineContainer;
	public static Block thickenedGlass;
	public static Block wickedClearGlass;
	public static Block atomicBomb;
	public static Block nuclearReactorGlass;
	public static Block nuclearChamberWall;
	public static Block nuclearChamberLock;
	public static Block nuclearReactantCore;
	public static Block nuclearPowerPort;
	public static Block nuclearControlPort;
	public static Block nuclearIOPort;
	
	// Machines: 
	public static Block solarArray, solarArrayLV, solarArrayMV, solarArrayHV;
	public static Block furnaceGen;
	public static Block fusionController, fissionController;
	public static Block fabricationTable;
	public static Block stoneCraftingTable;
	public static Block industrialFurnace;
	public static Block industrialCrusher;
	public static Block industrialLumberMill;
	public static Block industrialMetalPress;
	public static Block industrialCentrifuge;
	public static Block industrialEnergizer;
	public static Block industrialLoader;
	
	// Containers:
	public static Block energyPipeRed;
	public static Block energyPipeOrange;
	public static Block energyPipeClear;
	public static Block energyCellTier0, energyCellTier1, energyCellTier2, energyCellTier3;
	public static Block liquidNode;
	public static Block liquiductBlue;
	public static Block liquiductClear;
	public static Block fluidTankTier0, fluidTankTier1, fluidTankTier2, fluidTankTier3;
	
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
	public static Item sheetReinforced;
	public static Item conductiveCoil;
	public static Item gearWooden;
	public static Item gearStone;
	public static Item gearIron;
	public static Item gearCopper;
	public static Item gearAluminium;
	public static Item gearGold;
	public static Item gearDiamond;
	public static Item gearTitanium;
	
	// Tools:
	public static final ToolMaterial drillMat = EnumHelper.addToolMaterial("DRILLING", 4, 1000 + 1, 20.0f, 4.0f, 0);
	
	public static Item wrench;
	public static Item titaniumDrill;
	
	// Metals:
	public static Item dustGold;
	public static Item dustIron;
	public static Item dustCoal;
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
	public static Item mixedAlloy;

	public static Block blockTitanium;
	public static Block blockCopper;
	public static Block blockNickel;
	public static Block blockAluminium;
	public static Block blockUranium;
	
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
		
		logHelper.info("Pre-init started, looking for sorting config info!");
		sortingConfigHandler = new SortingConfigHandler(event, Reference.class, "sorting.cfg");
		sortingConfigHandler.handleConfiguration();
		logHelper.info("Sorting config loaded successfully! Patching mod now!");
		
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
		wickedClearGlass = new BlockWickedClearGlass();
		nuclearReactorGlass = new BlockNuclearReactorGlass();
		atomicBomb = new BlockAtomicBomb();
		nuclearChamberWall = new BlockNuclearChamberWall();
		nuclearChamberLock = new BlockNuclearChamberLock();
		nuclearReactantCore = new BlockNuclearReactantCore();
		nuclearPowerPort = new BlockNuclearPowerPort();
		nuclearControlPort = new BlockNuclearControlPort();
		nuclearIOPort = new BlockNuclearIOPort();

		// Generators:
		solarArray = new BlockSolarArray(Material.rock, (byte) 0);
		solarArrayLV = new BlockSolarArray(Material.rock, (byte) 1);
		solarArrayMV = new BlockSolarArray(Material.rock, (byte) 2);
		solarArrayHV = new BlockSolarArray(Material.rock, (byte) 3);
		furnaceGen = new BlockFurnaceGenerator(Material.rock);
		fusionController = new BlockNuclearController(Material.iron, true);
		fissionController = new BlockNuclearController(Material.iron, false);
		
		// Machines:
		fabricationTable = new BlockFabricationTable(Material.rock);
		stoneCraftingTable = new BlockStoneCraftingTable(Material.rock);
		industrialFurnace = new BlockIndustrialFurnace();
		industrialCrusher = new BlockIndustrialCrusher();
		industrialLumberMill = new BlockIndustrialLumberMill();
		industrialMetalPress = new BlockIndustrialMetalPress();
		industrialCentrifuge = new BlockIndustrialCentrifuge();
		industrialEnergizer = new BlockIndustrialEnergizer();
		industrialLoader = new BlockIndustrialLoader();
		
		// Containers:
		energyPipeRed = new BlockEnergyPipeRed(Material.rock, "energyPipeRed", EnumColor.RED);
		energyPipeOrange = new BlockEnergyPipeOrange(Material.rock, "energyPipeOrange", EnumColor.ORANGE);
		energyPipeClear = new BlockEnergyPipeClear(Material.rock, "energyPipeClear", EnumColor.CLEAR);
		energyCellTier0 = new BlockEnergyCell(Material.rock, "energyCellTier0");
		energyCellTier1 = new BlockEnergyCell(Material.rock, "energyCellTier1");
		energyCellTier2 = new BlockEnergyCell(Material.rock, "energyCellTier2");
		energyCellTier3 = new BlockEnergyCell(Material.rock, "energyCellTier3");
		liquidNode = new BlockLiquidNode(Material.rock);
		liquiductBlue = new BlockLiquiductBlue(Material.rock);
		liquiductClear = new BlockLiquiductClear(Material.rock);
		fluidTankTier0 = new BlockTankTier0(Material.rock);
		fluidTankTier1 = new BlockTankTier1(Material.rock);
		fluidTankTier2 = new BlockTankTier2(Material.rock);
		fluidTankTier3 = new BlockTankTier3(Material.rock);
		
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
		sheetReinforced = new ItemSheetReinforced("sheetReinforced", assetDir);
		conductiveCoil = new ItemConductiveCoil("conductiveCoil", assetDir);
		gearWooden = new ItemGear("gearWooden", assetDir);
		gearStone = new ItemGear("gearStone", assetDir);
		gearIron = new ItemGear("gearIron", assetDir);
		gearCopper = new ItemGear("gearCopper", assetDir);
		gearGold = new ItemGear("gearGold", assetDir);
		gearDiamond = new ItemGear("gearDiamond", assetDir);
		gearAluminium = new ItemGear("gearAluminium", assetDir);
		gearTitanium = new ItemGear("gearTitanium", assetDir);
		gearDiamond = new ItemGear("gearDiamond", assetDir);
		
		// Tools.
		wrench = new ItemWrench("wrench");
		titaniumDrill = new ItemMiningDrill(drillMat, "titaniumDrill");
		
		// Metals:
		dustGold = new ItemDustGold("dustGold", assetDir);
		dustIron = new ItemDustIron("dustIron", assetDir);
		dustCoal = new ItemDustCoal("dustCoal", assetDir);
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
		mixedAlloy = new ItemMixedAlloy("mixedAlloy", assetDir);

		blockTitanium = new BlockTitanium(Material.iron, "blockTitanium");
		blockCopper = new BlockCopper(Material.iron, "blockCopper");
		blockNickel = new BlockNickel(Material.iron, "blockNickel");
		blockAluminium = new BlockAluminium(Material.iron, "blockAluminium");
		blockUranium = new BlockUranium(Material.iron, "blockUranium");

		// Fuels:
		emptyFuelRod = new ItemFuelRod("emptyFuelRod", assetDir, true);
		fullFuelRod = new ItemFuelRod("fuelRod", assetDir, false);
		
		// Worldgen:
		worldgenTitanium = new OreWorldgen(oreTitanium, 7, 4, 8, 8, 24);
		worldgenNickel = new OreWorldgen(oreNickel, 6, 4, 6, 10, 30);
		worldgenAluminium = new OreWorldgen(oreAluminium, 8, 4, 8, 10, 64);
		worldgenCopper = new OreWorldgen(oreCopper, 10, 5, 10, 40, 75);
		worldgenUranium = new OreWorldgen(oreUranium, 5, 3, 5, 4, 16);
	}

}
