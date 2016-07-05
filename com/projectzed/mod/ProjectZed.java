/* This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under the terms of the GNU General Public 
* License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Project-Zed is 
* distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
* PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along 
* with Project-Zed. If not, see <http://www.gnu.org/licenses/>
*/
package com.projectzed.mod;

import com.hockeyhurd.hcorelib.api.creativetab.AbstractCreativeTab;
import com.hockeyhurd.hcorelib.api.math.TimeLapse;
import com.hockeyhurd.hcorelib.api.util.FluidFactory;
import com.hockeyhurd.hcorelib.api.util.LogHelper;
import com.hockeyhurd.hcorelib.api.util.SidedHelper;
import com.hockeyhurd.hcorelib.api.util.enums.EnumArmorType;
import com.hockeyhurd.hcorelib.api.util.interfaces.IForgeMod;
import com.hockeyhurd.hcorelib.api.worldgen.HCWorldGenFluid;
import com.projectzed.api.energy.source.EnumColor;
import com.projectzed.mod.block.*;
import com.projectzed.mod.block.container.*;
import com.projectzed.mod.block.container.digger.BlockIndustrialQuarry;
import com.projectzed.mod.block.fluids.BlockFluidOil;
import com.projectzed.mod.block.fluids.BlockFluidPetrol;
import com.projectzed.mod.block.generator.*;
import com.projectzed.mod.block.machines.*;
import com.projectzed.mod.block.ore.*;
import com.projectzed.mod.creativetabs.ProjectZedCreativeTab;
import com.projectzed.mod.handler.ConfigHandler;
import com.projectzed.mod.handler.SortingConfigHandler;
import com.projectzed.mod.item.*;
import com.projectzed.mod.item.armor.ArmorSetZPlated;
import com.projectzed.mod.item.metals.*;
import com.projectzed.mod.item.tools.ItemBlockExchanger;
import com.projectzed.mod.item.tools.ItemToolChainsaw;
import com.projectzed.mod.item.tools.ItemToolMiningDrill;
import com.projectzed.mod.item.tools.ItemWrench;
import com.projectzed.mod.item.upgrades.ItemUpgradeEfficiency;
import com.projectzed.mod.item.upgrades.ItemUpgradeOverclocker;
import com.projectzed.mod.item.upgrades.ItemUpgradeSilkTouch;
import com.projectzed.mod.proxy.CommonProxy;
import com.projectzed.mod.util.ModsLoadedHelper;
import com.projectzed.mod.util.ProjectZedMetadata;
import com.projectzed.mod.util.Reference;
import com.projectzed.mod.worldgen.OreWorldgen;
import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialLiquid;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.item.ItemArmor.ArmorMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.*;
import net.minecraftforge.fml.relauncher.Side;

/**
 * Project-Zed's main class covering all initializations.
 * 
 * @author hockeyhurd
 * @version Oct 19, 2014
 */
@Mod(modid = Reference.MOD_NAME, acceptedMinecraftVersions = Reference.MINECRAFT_VERSION, name = Reference.MOD_NAME,
		version = Reference.VERSION, dependencies = "required-after:HCoreLib", guiFactory = "com.projectzed.mod.gui.config.PZGuiFactory")
public final class ProjectZed implements IForgeMod {

	@SidedProxy(clientSide = "com.projectzed.mod.proxy.ClientProxy", serverSide = "com.projectzed.mod.proxy.CommonProxy")
	public static CommonProxy proxy;
	
	@Instance(Reference.MOD_NAME)
	public static ProjectZed instance;
	
	public static LogHelper logHelper;
	public static ConfigHandler configHandler;
	public static SortingConfigHandler sortingConfigHandler;
	public static final String assetDir = Reference.MOD_NAME.toLowerCase(); // + ":";
	public static final String modID = Reference.MOD_NAME;
	private TimeLapse tl;
	
	// Creative Tabs:
	public static final AbstractCreativeTab modCreativeTab = new ProjectZedCreativeTab(CreativeTabs.getNextID(), "Project-Zed");
	
	// Blocks:
	public static Block machineContainer;
	public static Block thickenedGlass;
	public static Block wickedClearGlass;
	public static Block quarryMarker;
	public static Block atomicBomb;

	public static Block stoneBricksDefault, stoneBricksDefaultStairs;
	public static Block stoneBricksWide, stoneBricksWideStairs;
	public static Block stoneBricksRed, stoneBricksRedStairs;
	public static Block stoneBricksBlue, stoneBricksBlueStairs;
	public static Block stoneBricksGreen, stoneBricksGreenStairs;
	public static Block stoneBricksPurple, stoneBricksPurpleStairs;

	// Nuclear Blocks:
	public static Block nuclearReactorGlass;
	public static Block nuclearChamberWall;
	public static Block nuclearChamberLock;
	public static Block nuclearReactantCore;
	public static Block nuclearPowerPort;
	public static Block nuclearControlPort;
	public static Block nuclearIOPort;

	// Fluids:
	public static Block blockFluidOil;
	public static Block blockFluidPetrol;

	public static Fluid fluidOil;
	public static Fluid fluidPetrol;

	public static Item bucketOil;
	public static Item bucketPetrol;

	// Machines: 
	public static Block solarArray, solarArrayLV, solarArrayMV, solarArrayHV;
	public static Block furnaceGen;
	public static Block fusionController, fissionController;
	public static Block lavaGen;
	public static Block petrolGen;
	public static Block handGen;
	public static Block fabricationTable;
	public static Block stoneCraftingTable;
	public static Block refinery;
	public static Block patternEncoder;
	public static Block industrialFurnace;
	public static Block industrialCrusher;
	public static Block industrialLumberMill;
	public static Block industrialMetalPress;
	public static Block industrialCentrifuge;
	public static Block industrialEnergizer;
	public static Block industrialLoader;
	public static Block industrialPlanter;
	public static Block industrialHarvester;

	// Containers:
	public static Block energyPipeRed;
	public static Block energyPipeOrange;
	public static Block energyPipeClear;
	public static Block energyCellTier0, energyCellTier1, energyCellTier2, energyCellTier3;
	public static Block liquidNode;
	public static Block liquiductBlue;
	public static Block liquiductClear;
	public static Block fluidTankTier0, fluidTankTier1, fluidTankTier2, fluidTankTier3;
	public static Block itemPipeGreen;
	public static Block itemPipeGreenOpaque;
	
	// Diggers:
	public static Block industrialQuarry;
	
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
	public static Item conductiveCoil;
	public static Item electricMotor;
	public static Item photoviolicCell;
	public static Item plateAluminium;
	public static Item plateCopper;
	public static Item plateNickel;
	public static Item plateIron;
	public static Item plateGold;
	public static Item plateLapis;
	public static Item plateTitanium;
	public static Item plateUranium;
	public static Item plateReinforced;
	public static Item gearWooden;
	public static Item gearStone;
	public static Item gearIron;
	public static Item gearCopper;
	public static Item gearAluminium;
	public static Item gearGold;
	public static Item gearDiamond;
	public static Item gearTitanium;
	public static Item overclockerUpgrade;
	public static Item silkTouchUpgrade;
	public static Item efficiencyUpgrade;
	public static Item blankCraftingPattern;
	public static Item craftingPattern;

	// Material:
	public static final Material MATERIAL_OIL = new MaterialLiquid(MapColor.BLACK);
	public static final Material MATERIAL_PETROL = new MaterialLiquid(MapColor.AIR);
	public static final ArmorMaterial zPlatedMat = EnumHelper.addArmorMaterial("ZPLATEDARMOR", assetDir + ":ZPlated", 100,
			new int[] { 3, 8, 6, 3 }, 25, SoundEvents.ITEM_ARMOR_EQUIP_IRON, 25.0f);

	// Armor:
	public static Item zPlatedHelm, zPlatedChest, zPlatedLeg, zPlatedBoot;

	// Tools:
	public static final ToolMaterial pzToolMat = EnumHelper.addToolMaterial("PZTOOLS", 5, 1000 + 1, 20.0f, 4.0f, 0);
	
	public static Item wrench;
	public static Item blockExchanger;
	public static Item titaniumDrill;
	public static Item titaniumChainsaw;
	
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
	public static Item dustMixedAlloy;
	
	public static Item ingotTitanium;
	public static Item ingotCopper;
	public static Item ingotNickel;
	public static Item ingotAluminium;
	public static Item ingotUranium;
	public static Item mixedAlloy;

	public static Item nuggetIron;
	public static Item nuggetAluminium;
	public static Item nuggetCopper;
	public static Item nuggetNickel;
	public static Item nuggetTitanium;
	public static Item nuggetUranium;

	public static Block blockTitanium;
	public static Block blockCopper;
	public static Block blockNickel;
	public static Block blockAluminium;
	public static Block blockUranium;
	
	// Fuels
	// public static Item emptyFuelRod;
	public static Item fuelRod;
	
	// Worldgen stuff:
	public static OreWorldgen worldgenTitanium;
	public static OreWorldgen worldgenNickel;
	public static OreWorldgen worldgenAluminium;
	public static OreWorldgen worldgenCopper;
	public static OreWorldgen worldgenUranium;
	public static HCWorldGenFluid worldgenOil;
	
	/**
	 * Default constructor.
	 */
	public ProjectZed() {
	}

	@Override
	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		tl = new TimeLapse();
		logHelper = new LogHelper(modID);

		final Side side = FMLCommonHandler.instance().getEffectiveSide();

		if (side == Side.CLIENT) {
			logHelper.info("Injecting mcmod.info information");

			final ProjectZedMetadata metadata = new ProjectZedMetadata(event);

			if (metadata.getResult()) logHelper.info("Injection was successful!");
			else logHelper.warn("Injection was un-successful! mcmod.info is a liar!");
		}

		logHelper.info("Pre-init started, looking for config info!");
		configHandler = new ConfigHandler(event, modID);
		configHandler.handleConfiguration();
		logHelper.info("Config loaded successfully! Patching mod now!"); 
		
		logHelper.info("Pre-init started, looking for sorting config info!");
		sortingConfigHandler = new SortingConfigHandler(event, modID, "sorting.cfg");
		sortingConfigHandler.handleConfiguration();
		logHelper.info("Sorting config loaded successfully! Patching mod now!");
		
		ModsLoadedHelper.instance().init();
		ModsLoadedHelper.instance().logFindings(logHelper);

		logHelper.info("Generating blocks, items, various objects and tinkering tools...");
		loadObj();

		logHelper.info("Initializing up proxy on side:", SidedHelper.getSide().name());
		proxy.init();
		
		logHelper.info("Pre-init finished successfully after", tl.getEffectiveTimeSince(), "ms!");
	}

	@Override
	@EventHandler
	public void init(FMLInitializationEvent event) {
		tl.resetStartTime();
		logHelper.info("Init started");

		/*logHelper.info("Generating blocks, items, various objects and tinkering tools...");
		loadObj();

		logHelper.info("Setting up proxy on side: " + FMLCommonHandler.instance().getEffectiveSide().name());

		proxy.init();*/

		logHelper.info("Setting up additional proxy stuffs on side:", SidedHelper.getSide().name());

		proxy.registerRenderInformation();
		proxy.registerInputHandlers();
		
		logHelper.info("Init finished successfully after", tl.getEffectiveTimeSince(), "ms!");
	}

	@Override
	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		tl.resetStartTime();
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
		quarryMarker = new BlockQuarryMarker();
		nuclearReactorGlass = new BlockNuclearReactorGlass();
		atomicBomb = new BlockAtomicBomb();

		stoneBricksDefault = new BlockStoneBricks("stoneBricksDefault");
		stoneBricksWide = new BlockStoneBricks("stoneBricksWide");
		stoneBricksRed = new BlockStoneBricks("stoneBricksRed");
		stoneBricksBlue = new BlockStoneBricks("stoneBricksBlue");
		stoneBricksGreen = new BlockStoneBricks("stoneBricksGreen");
		stoneBricksPurple = new BlockStoneBricks("stoneBricksPurple");

		stoneBricksDefaultStairs = new BlockStairsMaker(stoneBricksDefault);
		stoneBricksWideStairs = new BlockStairsMaker(stoneBricksWide);
		stoneBricksRedStairs = new BlockStairsMaker(stoneBricksRed);
		stoneBricksBlueStairs = new BlockStairsMaker(stoneBricksBlue);
		stoneBricksGreenStairs = new BlockStairsMaker(stoneBricksGreen);
		stoneBricksPurpleStairs = new BlockStairsMaker(stoneBricksPurple);

		nuclearChamberWall = new BlockNuclearChamberWall();
		nuclearChamberLock = new BlockNuclearChamberLock();
		nuclearReactantCore = new BlockNuclearReactantCore();
		nuclearPowerPort = new BlockNuclearPowerPort();
		nuclearControlPort = new BlockNuclearControlPort();
		nuclearIOPort = new BlockNuclearIOPort();

		// Fluids:
		if (!FluidRegistry.isFluidRegistered("oil")) {
			// fluidOil = FluidFactory.createNewFluid("oil");
			fluidOil = FluidFactory.createNewFluid("oil", new ResourceLocation(assetDir, "blockFluidOil.sill"),
					new ResourceLocation(assetDir, "blockFluidOil.flowing"));
			fluidOil.setDensity(800);
			fluidOil.setViscosity(10000);

			// TODO: MOVE REGISTERING OF FLUID SOMEWHERE OUT OF THIS MAIN CLASS!!!
			FluidRegistry.registerFluid(fluidOil);
		}

		else fluidOil = FluidRegistry.getFluid("oil");

		if (!FluidRegistry.isFluidRegistered("fuel")) {
			// fluidPetrol = FluidFactory.createNewFluid("fuel");
			fluidPetrol = FluidFactory.createNewFluid("fuel", new ResourceLocation(assetDir, "blockFluidPetrol.still"),
					new ResourceLocation(assetDir, "blockFluidPetrol.flowing"));
			FluidRegistry.registerFluid(fluidPetrol);
		}

		else fluidPetrol = FluidRegistry.getFluid("fuel");

		blockFluidOil = new BlockFluidOil("blockFluidOil", fluidOil);
		blockFluidPetrol = new BlockFluidPetrol("blockFluidPetrol", fluidPetrol);

		bucketOil = new ItemBucketOil("bucketOil", blockFluidOil);
		bucketPetrol = new ItemBucketPetrol("bucketPetrol", blockFluidPetrol);

		// TODO: MOVE REGISTERING OF FLUID Bucket SOMEWHERE OUT OF THIS MAIN CLASS!!!
		FluidContainerRegistry.registerFluidContainer(fluidOil, new ItemStack(bucketOil), new ItemStack(Items.BUCKET));
		FluidContainerRegistry.registerFluidContainer(fluidPetrol, new ItemStack(bucketPetrol), new ItemStack(Items.BUCKET));

		// Generators:
		solarArray = new BlockSolarArray(Material.ROCK, (byte) 0);
		solarArrayLV = new BlockSolarArray(Material.ROCK, (byte) 1);
		solarArrayMV = new BlockSolarArray(Material.ROCK, (byte) 2);
		solarArrayHV = new BlockSolarArray(Material.ROCK, (byte) 3);
		furnaceGen = new BlockFurnaceGenerator(Material.ROCK);
		lavaGen = new BlockLavaGenerator(Material.ROCK);
		petrolGen = new BlockPetrolGenerator(Material.ROCK);
		handGen = new BlockHandGenerator(Material.ROCK);
		fusionController = new BlockNuclearController(Material.IRON, true);
		fissionController = new BlockNuclearController(Material.IRON, false);
		
		// Machines:
		fabricationTable = new BlockFabricationTable(Material.ROCK);
		stoneCraftingTable = new BlockStoneCraftingTable(Material.ROCK);
		refinery = new BlockRefinery(Material.ROCK);
		patternEncoder = new BlockPatternEncoder("patternEncoder");
		industrialFurnace = new BlockIndustrialFurnace();
		industrialCrusher = new BlockIndustrialCrusher();
		industrialLumberMill = new BlockIndustrialLumberMill();
		industrialMetalPress = new BlockIndustrialMetalPress();
		industrialCentrifuge = new BlockIndustrialCentrifuge();
		industrialEnergizer = new BlockIndustrialEnergizer();
		industrialLoader = new BlockIndustrialLoader();
		industrialPlanter = new BlockIndustrialPlanter();
		industrialHarvester = new BlockIndustrialHarvester();

		// Containers:
		energyPipeRed = new BlockEnergyPipeRed(Material.ROCK, "energyPipeRed", EnumColor.RED);
		energyPipeOrange = new BlockEnergyPipeOrange(Material.ROCK, "energyPipeOrange", EnumColor.ORANGE);
		energyPipeClear = new BlockEnergyPipeClear(Material.ROCK, "energyPipeClear", EnumColor.CLEAR);
		energyCellTier0 = new BlockEnergyCell(Material.ROCK, "energyCellTier0");
		energyCellTier1 = new BlockEnergyCell(Material.ROCK, "energyCellTier1");
		energyCellTier2 = new BlockEnergyCell(Material.ROCK, "energyCellTier2");
		energyCellTier3 = new BlockEnergyCell(Material.ROCK, "energyCellTier3");
		liquidNode = new BlockLiquidNode(Material.ROCK);
		liquiductBlue = new BlockLiquiductBlue(Material.ROCK);
		liquiductClear = new BlockLiquiductClear(Material.ROCK);
		fluidTankTier0 = new BlockTankTier0(Material.ROCK);
		fluidTankTier1 = new BlockTankTier1(Material.ROCK);
		fluidTankTier2 = new BlockTankTier2(Material.ROCK);
		fluidTankTier3 = new BlockTankTier3(Material.ROCK);
		itemPipeGreen = new BlockItemPipeGreen(Material.ROCK);
		itemPipeGreenOpaque = new BlockItemPipeGreenOpaque(Material.ROCK);
		
		// Diggers:
		industrialQuarry = new BlockIndustrialQuarry(Material.ROCK);
		
		// RF STUFF:
		if (ModsLoadedHelper.instance().cofhCore) { 
			bridgeMcUToRF = new BlockRFBridge(Material.ROCK, false);
			bridgeRFToMcU = new BlockRFBridge(Material.ROCK, true);
		}
		
		// Ores:
		oreTitanium = new BlockTitaniumOre(Material.ROCK, assetDir, "oreTitanium");
		oreCopper = new BlockCopperOre(Material.ROCK, assetDir, "oreCopper");
		oreNickel = new BlockNickelOre(Material.ROCK, assetDir, "oreNickel"); 
		oreAluminium = new BlockAluminiumOre(Material.ROCK, assetDir, "oreAluminium");
		oreUranium = new BlockUraniumOre(Material.ROCK, assetDir, "oreUranium");
		
		// Items:
		itemDongle = new ItemDongle();
		forgingHammer = new ItemForgingHammer();
		mcuReader = new ItemMcUReader();
		screw = new ItemScrew("screw", assetDir);
		conductiveCoil = new ItemConductiveCoil("conductiveCoil", assetDir);
		electricMotor = new ItemElectricMotor("electricMotor", assetDir);
		photoviolicCell = new ItemPhotoviolicCell("photoviolicCell", assetDir);
		plateAluminium = new ItemPlateAluminium("plateAluminium", assetDir);
		plateCopper = new ItemPlateCopper("plateCopper", assetDir);
		plateNickel = new ItemPlateNickel("plateNickel", assetDir);
		plateIron = new ItemPlateIron("plateIron", assetDir);
		plateGold = new ItemPlateGold("plateGold", assetDir);
		plateLapis = new ItemPlateLapis("plateLapis", assetDir);
		plateTitanium = new ItemPlateTitanium("plateTitanium", assetDir);
		plateUranium = new ItemPlateUranium("plateUranium", assetDir);
		plateReinforced = new ItemPlateReinforced("plateReinforced", assetDir);
		gearWooden = new ItemGear("gearWooden", assetDir);
		gearStone = new ItemGear("gearStone", assetDir);
		gearIron = new ItemGear("gearIron", assetDir);
		gearCopper = new ItemGear("gearCopper", assetDir);
		gearGold = new ItemGear("gearGold", assetDir);
		gearDiamond = new ItemGear("gearDiamond", assetDir);
		gearAluminium = new ItemGear("gearAluminium", assetDir);
		gearTitanium = new ItemGear("gearTitanium", assetDir);
		gearDiamond = new ItemGear("gearDiamond", assetDir);
		overclockerUpgrade = new ItemUpgradeOverclocker("overclockerUpgrade");
		silkTouchUpgrade = new ItemUpgradeSilkTouch("silkTouchUpgrade");
		efficiencyUpgrade = new ItemUpgradeEfficiency("efficiencyUpgrade");
		blankCraftingPattern = new ItemCraftingPattern("blankCraftingPattern", false);
		craftingPattern = new ItemCraftingPattern("craftingPattern", true);

		// Armor:
		zPlatedHelm = new ArmorSetZPlated(zPlatedMat, 0, EnumArmorType.HELMET);
		zPlatedChest = new ArmorSetZPlated(zPlatedMat, 0, EnumArmorType.CHEST);
		zPlatedLeg = new ArmorSetZPlated(zPlatedMat, 0, EnumArmorType.LEGGINGS);
		zPlatedBoot = new ArmorSetZPlated(zPlatedMat, 0, EnumArmorType.BOOTS);
		
		// Tools:
		wrench = new ItemWrench("wrench");
		titaniumDrill = new ItemToolMiningDrill(pzToolMat, "titaniumDrill");
		titaniumChainsaw = new ItemToolChainsaw(pzToolMat, "titaniumChainsaw");
		blockExchanger = new ItemBlockExchanger("blockExchanger");

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
		dustMixedAlloy = new ItemDustMixedAlloy("dustMixedAlloy", assetDir);

		nuggetIron = new ItemNuggetIron("nuggetIron", assetDir);
		nuggetAluminium = new ItemNuggetAluminium("nuggetAluminium", assetDir);
		nuggetCopper = new ItemNuggetCopper("nuggetCopper", assetDir);
		nuggetNickel = new ItemNuggetNickel("nuggetNickel", assetDir);
		nuggetTitanium = new ItemNuggetTitanium("nuggetTitanium", assetDir);
		nuggetUranium = new ItemNuggetUranium("nuggetUranium", assetDir);

		ingotTitanium = new ItemIngotTitanium("ingotTitanium", assetDir);
		ingotCopper = new ItemIngotCopper("ingotCopper", assetDir);
		ingotNickel = new ItemIngotNickel("ingotNickel", assetDir);
		ingotAluminium = new ItemIngotAluminium("ingotAluminium", assetDir);
		ingotUranium = new ItemIngotUranium("ingotUranium", assetDir);
		mixedAlloy = new ItemMixedAlloy("mixedAlloy", assetDir);

		blockTitanium = new BlockTitanium(Material.IRON, "blockTitanium");
		blockCopper = new BlockCopper(Material.IRON, "blockCopper");
		blockNickel = new BlockNickel(Material.IRON, "blockNickel");
		blockAluminium = new BlockAluminium(Material.IRON, "blockAluminium");
		blockUranium = new BlockUranium(Material.IRON, "blockUranium");

		// Fuels:
		// emptyFuelRod = new ItemFuelRod("emptyFuelRod", assetDir, true);
		fuelRod = new ItemFuelRod("fuelRod", assetDir);
		
		// Worldgen:
		if (configHandler.genOreTitanium()) worldgenTitanium = new OreWorldgen(oreTitanium, 7, 4, 8, 8, 24);
		if (configHandler.genOreNickel()) worldgenNickel = new OreWorldgen(oreNickel, 6, 4, 6, 10, 30);
		if (configHandler.genOreAluminium()) worldgenAluminium = new OreWorldgen(oreAluminium, 8, 4, 8, 10, 64);
		if (configHandler.genOreCopper()) worldgenCopper = new OreWorldgen(oreCopper, 10, 5, 10, 40, 75);
		if (configHandler.genOreUranium()) worldgenUranium = new OreWorldgen(oreUranium, 5, 3, 5, 4, 16);
		if (configHandler.genFluidOil()) worldgenOil = new HCWorldGenFluid(blockFluidOil);
	}

	@Override
	@EventHandler
	public void serverStartingEvent(FMLServerStartingEvent event) {
	}

	@Override
	@EventHandler
	public void serverStartedEvent(FMLServerStartedEvent event) {
	}

}
