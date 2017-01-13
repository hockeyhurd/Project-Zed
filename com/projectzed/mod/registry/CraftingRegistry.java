/* This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under the terms of the GNU General Public 
* License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Project-Zed is 
* distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
* PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along 
* with Project-Zed. If not, see <http://www.gnu.org/licenses/>
*/
package com.projectzed.mod.registry;

import com.projectzed.mod.item.upgrades.ItemRadialUpgrade;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

import java.util.LinkedList;
import java.util.List;

import static com.projectzed.mod.ProjectZed.*;

/**
 * Registry for all crafting recipes. 
 * 
 * @author hockeyhurd
 * @version Nov 9, 2014
 */
public final class CraftingRegistry {

	private List<ShapelessOreRecipe> shapelessList;
	private List<ShapedOreRecipe> shapedList;
	private static CraftingRegistry reg = new CraftingRegistry();
	
	private CraftingRegistry() {
		shapelessList = new LinkedList<ShapelessOreRecipe>();
		shapedList = new LinkedList<ShapedOreRecipe>();
	}
	
	/**
	 * Get the instance of this class in its static way.
	 * @return static instance of this class.
	 */
	public static CraftingRegistry instance() {
		return reg;
	}
	
	/**
	 * Get the shapeless crafting list created during init method call.
	 * @return shapeless crafting list.
	 */
	public List<ShapelessOreRecipe> getShapelessList() {
		return reg.shapelessList;
	}
	
	/**
	 * Get the shaped crafting list created during the init method call.
	 * @return shaped crafting list.
	 */
	public List<ShapedOreRecipe> getShapedList() {
		return reg.shapedList;
	}
	
	/**
	 * Method once called, will handle the initializing of all crafting recipes.
	 */
	public void init() {
		initShapeless();
		initShaped();
	}
	
	/**
	 * Sub-init method for init'ing shapeless crafting recipes into list.
	 */
	private void initShapeless() {
		reg.shapelessList.add(createShapelessRecipe(ingotTitanium, 9, 0, "blockTitanium"));
		reg.shapelessList.add(createShapelessRecipe(ingotCopper, 9, 0, "blockCopper"));
		reg.shapelessList.add(createShapelessRecipe(ingotNickel, 9, 0, "blockNickel"));
		reg.shapelessList.add(createShapelessRecipe(ingotAluminium, 9, 0, "blockAluminium"));
		reg.shapelessList.add(createShapelessRecipe(ingotUranium, 9, 0, "blockUranium"));

		reg.shapelessList.add(createShapelessRecipe(nuggetIron, 9, 0, "ingotIron"));
		reg.shapelessList.add(createShapelessRecipe(nuggetAluminium, 9, 0, "ingotAluminium"));
		reg.shapelessList.add(createShapelessRecipe(nuggetCopper, 9, 0, "ingotCopper"));
		reg.shapelessList.add(createShapelessRecipe(nuggetNickel, 9, 0, "ingotNickel"));
		reg.shapelessList.add(createShapelessRecipe(nuggetTitanium, 9, 0, "ingotTitanium"));
		reg.shapelessList.add(createShapelessRecipe(nuggetUranium, 9, 0, "ingotUranium"));

		reg.shapelessList.add(createShapelessRecipe(screw, 27, 0, "ingotIron", "ingotIron", "ingotIron"));
		reg.shapelessList.add(createShapelessRecipe(plateAluminium, 1, 0, "ingotAluminium", new ItemStack(forgingHammer, 1, OreDictionary.WILDCARD_VALUE)));
		reg.shapelessList.add(createShapelessRecipe(mixedAlloy, 2, 0, "ingotIron", "ingotCopper", "ingotTitanium", new ItemStack(forgingHammer, 1, OreDictionary.WILDCARD_VALUE)));

		reg.shapelessList.add(createShapelessRecipe(stoneBricksWide, 2, "stoneBricks", "stoneBricks"));
		reg.shapelessList.add(createShapelessRecipe(stoneBricksDefault, 1, "stoneBricks", Items.WATER_BUCKET));
		reg.shapelessList.add(createShapelessRecipe(stoneBricksDefaultStairs, 1, "stoneBricksStairs", Items.WATER_BUCKET));
		reg.shapelessList.add(createShapelessRecipe(stoneBricksRedStairs, 1, "stoneBricksStairs", "dyeRed"));
		reg.shapelessList.add(createShapelessRecipe(stoneBricksBlueStairs, 1, "stoneBricksStairs", "dyeBlue"));
		reg.shapelessList.add(createShapelessRecipe(stoneBricksGreenStairs, 1, "stoneBricksStairs", "dyeGreen"));
		reg.shapelessList.add(createShapelessRecipe(stoneBricksPurpleStairs, 1, "stoneBricksStairs", "dyePurple"));

		reg.shapelessList.add(createShapelessRecipe(blankCraftingPattern, 1, 0, craftingPattern));

		// Radial Upgrades:
		for (int i = 2; i <= ItemRadialUpgrade.MAX_SIZE; i++) {
			for (int y = 0 ; y < i; y++) {
				for (int x = 0; x <= y; x++) {
					if (x + y == i) {
						reg.shapelessList.add(createShapelessRecipe(radialUpgrade, 1, i - 1, new ItemStack(radialUpgrade, 1, x - 1),
								new ItemStack(radialUpgrade, 1, y - 1)));
					}
				}
			}
		}
	}
	
	/**
	 * Sub-init method for init'ing shapeless crafting recipes into list.
	 */
	private void initShaped() {
		// Items:
		reg.shapedList.add(createShapedRecipe(forgingHammer, 1, 0, "xxx", "xyx", " y ", 'x', "ingotTitanium", 'y', "stickWood"));
		reg.shapedList.add(createShapedRecipe(conductiveCoil, 1, 0, " yx", "yxy", "xy ", 'x', "ingotCopper", 'y', Items.REDSTONE));
		reg.shapedList.add(createShapedRecipe(electricMotor, 1, 0, "bab", "cac", "dad", 'a', conductiveCoil, 'b', Items.REDSTONE, 'c', "plateAluminium",
				'd', gearIron));
		reg.shapedList.add(createShapedRecipe(photoviolicCell, 3, 0, "gdg", "nrn", 'g', "blockGlass", 'd', "gemDiamond", 'r', Items.REDSTONE, 'n',
				"nuggetIron"));
		// reg.shapedList.add(createShapedRecipe(emptyFuelRod, 1, "xyx", "y y", "xyx", 'x', "ingotIron", 'y', "blockGlass"));
		reg.shapedList.add(createShapedRecipe(fuelRod, 1, 0, "xyx", "y y", "xyx", 'x', "ingotIron", 'y', "blockGlass"));
		reg.shapedList.add(createShapedRecipe(mcuReader, 1, 0, " a ", "bcb", "   ", 'a', "blockGlass", 'b', "dustRedstone", 'c', energyPipeRed));
		reg.shapedList.add(createShapedRecipe(dustMixedAlloy, 2, 0, "xxx", "yyy", "zzz", 'x', "dustIron", 'y', "dustCopper", 'z', "dustTitanium"));
		reg.shapedList.add(createShapedRecipe(silkTouchUpgrade, 1, 0, "sds", "rar", 'a', "plateAluminium", 'd', Items.DIAMOND_PICKAXE, 'r', Items.REDSTONE, 's',
				Items.STRING));
		reg.shapedList.add(createShapedRecipe(overclockerUpgrade, 2, 0, "mrm", "rar", "mcm", 'a', "plateAluminium", 'c', conductiveCoil, 'm', "mixedAlloy", 'r',
				Items.REDSTONE));

		reg.shapedList.add(createShapedRecipe(efficiencyUpgrade, 1, 0, "oco", "rpr", 'c', conductiveCoil, 'p', "plateAluminium", 'r', Items.REDSTONE, 'o', overclockerUpgrade));
		reg.shapedList.add(createShapedRecipe(radialUpgrade, 2, 0, "nrn", "rpr", "nrn", 'n', "nuggetIron", 'r', Items.REDSTONE, 'p', "plateAluminium"));
		
		// tools:
		reg.shapedList.add(createShapedRecipe(wrench, 1, 0, "x x", "xxx", " x ", 'x', "ingotIron"));
		// reg.shapedList.add(createShapedRecipeWithMeta(titaniumDrill, 1, titaniumDrill.getMaxDamage(), "cdc", "dad", "ebe", 'a', Blocks.piston, 'b', conductiveCoil, 'c', Items.redstone, 'd', plateReinforced, 'e', gearTitanium));
		reg.shapedList.add(createShapedRecipeWithMeta(titaniumDrill, 1, titaniumDrill.getMaxDamage(), "cdc", "dad", "ebe", 'a', electricMotor, 'b', conductiveCoil, 'c', Items.REDSTONE, 'd',
				plateReinforced, 'e', gearTitanium));
		reg.shapedList.add(createShapedRecipeWithMeta(titaniumChainsaw, 1, titaniumChainsaw.getMaxDamage(), "dcd", "dad", "ebe", 'a', electricMotor,
				'b', conductiveCoil, 'c', Items.REDSTONE, 'd', plateReinforced, 'e', gearTitanium));
		
		// Blocks:
		reg.shapedList.add(createShapedRecipe(blockTitanium, 1, 0, "xxx", "xxx", "xxx", 'x', "ingotTitanium"));
		reg.shapedList.add(createShapedRecipe(blockCopper, 1, 0, "xxx", "xxx", "xxx", 'x', "ingotCopper"));
		reg.shapedList.add(createShapedRecipe(blockNickel, 1, 0, "xxx", "xxx", "xxx", 'x', "ingotNickel"));
		reg.shapedList.add(createShapedRecipe(blockAluminium, 1, 0, "xxx", "xxx", "xxx", 'x', "ingotAluminium"));
		reg.shapedList.add(createShapedRecipe(blockUranium, 1, 0, "xxx", "xxx", "xxx", 'x', "ingotUranium"));

		reg.shapedList.add(createShapedRecipe(Items.IRON_INGOT, 1, 0, "xxx", "xxx", "xxx", 'x', "nuggetIron"));
		reg.shapedList.add(createShapedRecipe(ingotAluminium, 1, 0, "xxx", "xxx", "xxx", 'x', "nuggetAluminium"));
		reg.shapedList.add(createShapedRecipe(ingotCopper, 1, 0, "xxx", "xxx", "xxx", 'x', "nuggetCopper"));
		reg.shapedList.add(createShapedRecipe(ingotNickel, 1, 0, "xxx", "xxx", "xxx", 'x', "nuggetNickel"));
		reg.shapedList.add(createShapedRecipe(ingotTitanium, 1, 0, "xxx", "xxx", "xxx", 'x', "nuggetTitanium"));
		reg.shapedList.add(createShapedRecipe(ingotUranium, 1, 0, "xxx", "xxx", "xxx", 'x', "nuggetUranium"));

		reg.shapedList.add(createShapedRecipe(stoneCraftingTable, 1, 0, "xx", "xx", 'x', "stoneBricks"));
		reg.shapedList.add(createShapedRecipe(fabricationTable, 1, 0, "tst", "xyx", "tst", 'x', Blocks.CHEST, 'y', stoneCraftingTable, 's', screw, 't', "stone"));
		reg.shapedList.add(createShapedRecipe(fabricationTable, 1, 0, "tst", "xyx", "tst", 'x', Blocks.CHEST, 'y', stoneCraftingTable, 's', screw, 't', "stoneBricks"));
		reg.shapedList.add(createShapedRecipe(thickenedGlass, 4, 0, "xyx", 'x', "ingotTitanium", 'y', "blockGlass"));
		reg.shapedList.add(createShapedRecipe(wickedClearGlass, 8, 0, "xxx", "x x", "xxx", 'x', Blocks.GLASS));
		reg.shapedList.add(createShapedRecipe(quarryMarker, 1, 0, "a", "x", 'a', "ingotAluminium", 'x', Blocks.REDSTONE_TORCH));
		reg.shapedList.add(createShapedRecipe(quarryMarker, 1, 0, "a", "x", 'a', "ingotAluminum", 'x', Blocks.REDSTONE_TORCH));

		reg.shapedList.add(createShapedRecipe(stoneBricksDefault, 8, 0, "xxx", "x x", "xxx", 'x', Blocks.STONEBRICK));
		// reg.shapedList.add(createShapedRecipe(stoneBricksWide, 8, "xx", 'x', stoneBricksDefault));
		reg.shapedList.add(createShapedRecipe(stoneBricksRed, 8, 0, "xxx", "xyx", "xxx", 'x', "stoneBricks", 'y', "dyeRed"));
		reg.shapedList.add(createShapedRecipe(stoneBricksBlue, 8, 0, "xxx", "xyx", "xxx", 'x', "stoneBricks", 'y', "dyeBlue"));
		reg.shapedList.add(createShapedRecipe(stoneBricksGreen, 8, 0, "xxx", "xyx", "xxx", 'x', "stoneBricks", 'y', "dyeGreen"));
		reg.shapedList.add(createShapedRecipe(stoneBricksPurple, 8, 0, "xxx", "xyx", "xxx", 'x', "stoneBricks", 'y', "dyePurple"));

		reg.shapedList.add(createShapedRecipe(stoneBricksDefaultStairs, 4, 0, "x  ", "xx ", "xxx", 'x', stoneBricksDefault));
		reg.shapedList.add(createShapedRecipe(stoneBricksWideStairs, 4, 0, "x  ", "xx ", "xxx", 'x', stoneBricksWide));
		reg.shapedList.add(createShapedRecipe(stoneBricksRedStairs, 4, 0, "x  ", "xx ", "xxx", 'x', stoneBricksRed));
		reg.shapedList.add(createShapedRecipe(stoneBricksBlueStairs, 4, 0, "x  ", "xx ", "xxx", 'x', stoneBricksBlue));
		reg.shapedList.add(createShapedRecipe(stoneBricksGreenStairs, 4, 0, "x  ", "xx ", "xxx", 'x', stoneBricksGreen));
		reg.shapedList.add(createShapedRecipe(stoneBricksPurpleStairs, 4, 0, "x  ", "xx ", "xxx", 'x', stoneBricksPurple));

		// non-machine tileentities:
		reg.shapedList.add(createShapedRecipe(energyPipeRed, 3, 0, " z ", "xyx", " z ", 'x', "ingotNickel", 'y', Items.REDSTONE, 'z', "blockGlass"));
		reg.shapedList.add(createShapedRecipe(energyPipeOrange, 3, 0, "xyx", "zzz", "xyx", 'x', "ingotIron", 'y', "dustCopper", 'z', energyPipeRed));
		reg.shapedList.add(createShapedRecipe(energyPipeClear, 3, 0, "xyx", "zzz", "xyx", 'x', "ingotTitanium", 'y', thickenedGlass, 'z', energyPipeOrange));
		reg.shapedList.add(createShapedRecipe(energyCellTier0, 1, 0, "xyx", "yzy", "xyx", 'x', "ingotNickel", 'y', "blockRedstone", 'z', machineContainer));
		reg.shapedList.add(createShapedRecipe(energyCellTier1, 1, 0, "xxx", "x x", "xxx", 'x', energyCellTier0));
		reg.shapedList.add(createShapedRecipe(energyCellTier2, 1, 0, "xxx", "x x", "xxx", 'x', energyCellTier1));
		reg.shapedList.add(createShapedRecipe(energyCellTier3, 1, 0, "xxx", "x x", "xxx", 'x', energyCellTier2));
		reg.shapedList.add(createShapedRecipe(fluidTankTier0, 1, 0, "xyx", "y y", "xyx", 'x', "ingotCopper", 'y', "paneGlassColorless"));
		reg.shapedList.add(createShapedRecipe(fluidTankTier1, 1, 0, "xxx", "xyx", "xxx", 'x', "ingotIron", 'y', fluidTankTier0));
		reg.shapedList.add(createShapedRecipe(fluidTankTier2, 1, 0, "xxx", "xyx", "xxx", 'x', "ingotGold", 'y', fluidTankTier1));
		reg.shapedList.add(createShapedRecipe(fluidTankTier3, 1, 0, "xxx", "xyx", "xxx", 'x', "ingotTitanium", 'y', fluidTankTier2));
		reg.shapedList.add(createShapedRecipe(liquiductBlue, 4, 0, " z ", "xyx", " z ", 'x', "ingotNickel", 'y', Items.REDSTONE, 'z', "gemLapis"));
		
		// nuclear stuffs:
		reg.shapedList.add(createShapedRecipe(nuclearReactorGlass, 1, 0, "xyx", 'x', thickenedGlass, 'y', nuclearChamberWall));
		reg.shapedList.add(createShapedRecipe(nuclearChamberWall, 4, 0, "xyx", "y y", "xyx", 'x', "ingotTitanium", 'y', "stone"));
		reg.shapedList.add(createShapedRecipe(nuclearChamberLock, 1, 0, "x x", " y ", "x x", 'x', screw, 'y', nuclearChamberWall));
		reg.shapedList.add(createShapedRecipe(nuclearReactantCore, 1, 0, "xyx", "yzy", "xyx", 'x', screw, 'y', "plateReinforced", 'z', "blockDiamond"));
		reg.shapedList.add(createShapedRecipe(nuclearPowerPort, 1, 0, "xzx", "yay", "xyx", 'x', nuclearChamberWall, 'y', "dustRedstone", 'z', energyCellTier0, 'a', machineContainer));
		reg.shapedList.add(createShapedRecipe(nuclearControlPort, 1, 0, "xzx", "yay", "xyx", 'x', nuclearChamberWall, 'y', "dustRedstone", 'z', Blocks.LEVER, 'a', machineContainer));
		reg.shapedList.add(createShapedRecipe(nuclearIOPort, 1, 0, "xzx", "yay", "xbx", 'x', nuclearChamberWall, 'y', "dustRedstone", 'z', Blocks.LEVER, 'a', machineContainer, 'b', Blocks.PISTON));
		
		// Machine stuff:
		reg.shapedList.add(createShapedRecipe(furnaceGen, 1, 0, "aba", "aca", "ada", 'a', "cobblestone", 'b', Blocks.FURNACE, 'c', machineContainer, 'd', screw));
		reg.shapedList.add(createShapedRecipe(solarArray, 1, 0, "bbb", "cdc", "aea", 'a', "ingotTitanium", 'b', photoviolicCell, 'c', screw, 'd', furnaceGen, 'e', conductiveCoil));
		reg.shapedList.add(createShapedRecipe(solarArrayLV, 1, 0, "xxx", "x x", "xxx", 'x', solarArray));
		reg.shapedList.add(createShapedRecipe(solarArrayMV, 1, 0, "xxx", "x x", "xxx", 'x', solarArrayLV));
		reg.shapedList.add(createShapedRecipe(solarArrayHV, 1, 0, "xxx", "x x", "xxx", 'x', solarArrayMV));
		reg.shapedList.add(createShapedRecipe(lavaGen, 1, 0, "xbx", "sgs", "tct", 'b', Items.LAVA_BUCKET, 'x', Blocks.BRICK_BLOCK, 'g', furnaceGen, 's', screw, 't', "ingotTitanium", 'c', conductiveCoil));
		reg.shapedList.add(createShapedRecipe(petrolGen, 1, 0, "rbr", "sgs", "xcx", 'b', bucketPetrol, 'c', conductiveCoil, 'g', furnaceGen, 'r', Items.REDSTONE, 's', screw, 'x', gearIron));
		reg.shapedList.add(createShapedRecipe(fissionController, 1, 0, "xyx", "yzy", "xyx", 'x', "gemDiamond", 'y', "plateAluminium", 'z', furnaceGen));
		reg.shapedList.add(createShapedRecipe(machineContainer, 1, 0, "xyx", "yzy", "xyx", 'x', screw, 'y', "plateAluminium", 'z', "ingotNickel"));
		reg.shapedList.add(createShapedRecipe(gearWooden, 1, 0, "xyx", "y y", "xyx", 'x', "stickWood", 'y', "plankWood"));
		reg.shapedList.add(createShapedRecipe(gearStone, 1, 0, " x ", "xyx", " x ", 'x', "cobblestone", 'y', gearWooden));
		reg.shapedList.add(createShapedRecipe(gearIron, 1, 0, " x ", "xyx", " x ", 'x', "ingotIron", 'y', gearStone));
		reg.shapedList.add(createShapedRecipe(gearCopper, 1, 0, " x ", "xyx", " x ", 'x', "ingotCopper", 'y', gearStone));
		reg.shapedList.add(createShapedRecipe(gearAluminium, 1, 0, " x ", "xyx", " x ", 'x', "ingotAluminium", 'y', gearIron));
		reg.shapedList.add(createShapedRecipe(gearTitanium, 1, 0, " x ", "xyx", " x ", 'x', "ingotTitanium", 'y', gearIron));
		reg.shapedList.add(createShapedRecipe(gearGold, 1, 0, " x ", "xyx", " x ", 'x', "ingotGold", 'y', gearIron));
		reg.shapedList.add(createShapedRecipe(gearDiamond, 1, 0, " x ", "xyx", " x ", 'x', "gemDiamond", 'y', gearGold));
		reg.shapedList.add(createShapedRecipe(industrialFurnace, 1, 0, "bab", "cdc", "efe", 'a', Blocks.FURNACE, 'b', "ingotTitanium", 'c', screw, 'd', machineContainer, 'e', gearIron, 'f', conductiveCoil));
		reg.shapedList.add(createShapedRecipe(industrialCrusher, 1, 0, "bab", "cdc", "efe", 'a', Items.IRON_PICKAXE, 'b', "ingotTitanium", 'c', screw, 'd', machineContainer, 'e', gearIron, 'f', conductiveCoil));
		reg.shapedList.add(createShapedRecipe(industrialLumberMill, 1, 0, "bab", "cdc", "efe", 'a', Items.IRON_AXE, 'b', "ingotTitanium", 'c', screw, 'd', machineContainer, 'e', gearIron, 'f', conductiveCoil));
		reg.shapedList.add(createShapedRecipe(industrialMetalPress, 1, 0, "bab", "cdc", "efe", 'a', forgingHammer, 'b', "ingotTitanium", 'c', screw, 'd', machineContainer, 'e', gearIron, 'f', conductiveCoil));
		reg.shapedList.add(createShapedRecipe(industrialCentrifuge, 1, 0, "bab", "cdc", "efe", 'a', fuelRod, 'b', "ingotTitanium", 'c', screw, 'd', machineContainer, 'e', gearIron, 'f', conductiveCoil));
		reg.shapedList.add(createShapedRecipe(industrialEnergizer, 1, 0, "bab", "cdc", "efe", 'a', energyCellTier0, 'b', "ingotTitanium", 'c', screw, 'd', machineContainer, 'e', gearCopper, 'f', conductiveCoil));
		reg.shapedList.add(createShapedRecipe(industrialLoader, 1, 0, "xyx", "yzy", "xyx", 'x', Blocks.OBSIDIAN, 'y', "plateReinforced", 'z', machineContainer));
		reg.shapedList.add(createShapedRecipe(industrialQuarry, 1, 0, "eae", "sbs", "dcd", 'a', titaniumDrill, 'b', machineContainer, 'c', conductiveCoil, 'd', gearDiamond, 'e', energyPipeClear, 's', screw));
		reg.shapedList.add(createShapedRecipe(industrialHarvester, 1, 0, "sas", "rmr", "gcg", 'm', machineContainer, 'c', conductiveCoil, 'g', gearIron, 's', screw, 'a', Items.DIAMOND_AXE, 'r', Items.REDSTONE));
		reg.shapedList.add(createShapedRecipe(industrialPlanter, 1, 0, "shs", "rmr", "gcg", 'm', machineContainer, 'c', conductiveCoil, 'g', gearIron, 's', screw, 'h', Items.DIAMOND_HOE, 'r', Items.REDSTONE));

		reg.shapedList.add(createShapedRecipe(liquidNode, 1, 0, "wyw", "xzx", "vuv", 'u', conductiveCoil, 'v', "dustRedstone", 'w', "gemLapis", 'x', liquiductBlue, 'y', Items.BUCKET, 'z', machineContainer));
		reg.shapedList.add(createShapedRecipe(refinery, 1, 0, "gtg", "sfs", "gcg", 'c', conductiveCoil, 'f', industrialFurnace, 'g', thickenedGlass, 's', screw, 't', fluidTankTier3));
	}
	
	/**
	 * Method used for creating a new shapeless crafting recipes.
	 * @param item = item output from said recipe.
	 * @param amount = amount of items to receive.
	 * @param damage  = damage of item.
	 * @param objects = array of objects for how crafting recipe essentially should look.
	 * @return created shapeless recipe object if successful, else return null.
	 */
	private static ShapelessOreRecipe createShapelessRecipe(Item item, int amount, int damage, Object... objects) {
		return item == null || objects == null || objects.length < 1 ? null : new ShapelessOreRecipe(new ItemStack(item, amount, damage), objects);
	}
	
	/**
	 * Method used for creating a new shapeless crafting recipes.
	 * @param block = block output from said recipe.
	 * @param amount = amount of items to receive.
	 * @param objects = array of objects for how crafting recipe essentially should look.
	 * @return created shapeless recipe object if successful, else return null.
	 */
	private static ShapelessOreRecipe createShapelessRecipe(Block block, int amount, Object... objects) {
		return block == null || objects == null || objects.length < 1 ? null : new ShapelessOreRecipe(new ItemStack(block, amount), objects);
	}
	
	/**
	 * Method used for creating a new shaped crafting recipe.
	 * @param item item output from said recipe.
	 * @param amount amount of items to receive.
	 * @param objects array of objects for how crafting recipe essentially should look.
	 * @param damage damage the itemstack should have.
	 * @return created shaped recipe object if successful, else return null.
	 */
	private static ShapedOreRecipe createShapedRecipeWithMeta(Item item, int amount, int damage, Object... objects) {
		return item == null || objects == null || objects.length < 4 ? null : new ShapedOreRecipe(new ItemStack(item, amount, damage), objects);
	}
	
	/**
	 * Method used for creating a new shaped crafting recipe.
	 * @param item item output from said recipe.
	 * @param amount amount of items to receive.
	 * @param damage damage of itemstack.
	 * @param objects array of objects for how crafting recipe essentially should look.
	 * @return created shaped recipe object if successful, else return null.
	 */
	private static ShapedOreRecipe createShapedRecipe(Item item, int amount, int damage, Object... objects) {
		return item == null || objects == null || objects.length < 4 ? null : new ShapedOreRecipe(new ItemStack(item, amount, damage), objects);
	}
	
	/**
	 * Method used for creating a new shaped crafting recipe.
	 * @param block = block output from said recipe.
	 * @param amount = amount of items to receive.
	 * @param damage damage of itemstack.
	 * @param objects = array of objects for how crafting recipe essentially should look.
	 * @return created shaped recipe object if successful, else return null.
	 */
	private static ShapedOreRecipe createShapedRecipe(Block block, int amount, int damage, Object... objects) {
		return block == null || objects == null || objects.length < 4 ? null : new ShapedOreRecipe(new ItemStack(block, amount, damage), objects);
	}

}
