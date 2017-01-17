package com.projectzed.mod.item.upgrades;

import com.projectzed.api.item.AbstractItemUpgrade;
import com.projectzed.api.tileentity.digger.AbstractTileEntityDigger;
import com.projectzed.api.tileentity.generator.AbstractTileEntityGenerator;
import com.projectzed.api.tileentity.machine.AbstractTileEntityMachine;
import com.projectzed.mod.ProjectZed;
import com.projectzed.mod.tileentity.machine.TileEntityIndustrialHarvester;
import com.projectzed.mod.tileentity.machine.TileEntityIndustrialPlanter;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

/**
 * @author hockeyhurd
 * @version 1/13/2017.
 */
public class ItemRadialUpgrade extends AbstractItemUpgrade {

	public static final int MAX_SIZE = 9;
	private final ResourceLocation[] locations;

	/**
	 * @param name name of upgrade component.
	 */
	public ItemRadialUpgrade(String name) {
		super(name);

		setMaxStackSize(1);

		locations = new ResourceLocation[MAX_SIZE];
		for (int i = 0; i < locations.length; i++)
			locations[i] = new ResourceLocation(ProjectZed.assetDir, getMetaName(name, i));

		resourceLocation = locations[0];

		setHasSubtypes(true);
	}

	private static String getMetaName(String name, int meta) {
		return name + '_' + meta;
	}

	@Override
	public ResourceLocation getResourceLocation(int meta) {
		return locations[meta];
	}

	@Override
	public int getSizeOfSubItems() {
		return locations.length;
	}

	@Override
	public String getUnlocalizedName(ItemStack stack) {
		final int meta = stack.getMetadata();
		return super.getUnlocalizedName() + '_' + meta;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void getSubItems(Item item, CreativeTabs tab, List<ItemStack> subItems) {
		for (int i = 0; i < getSizeOfSubItems(); i++) {
			subItems.add(new ItemStack(item, 1, i));
		}
	}

	@Override
	public boolean effectOnMachines(AbstractTileEntityMachine te, boolean simulate) {
		return te instanceof TileEntityIndustrialHarvester || te instanceof TileEntityIndustrialPlanter;
	}

	@Override
	public boolean effectOnGenerators(AbstractTileEntityGenerator te, boolean simulate) {
		return false;
	}

	@Override
	public boolean effectOnDiggers(AbstractTileEntityDigger te, boolean simulate) {
		return false;
	}

	@Override
	@SideOnly(Side.CLIENT)
	protected void addInfo(ItemStack stack, EntityPlayer player, List list) {
		list.add(TextFormatting.GREEN + "Increases radius by: " + TextFormatting.WHITE + stack.getMetadata());
	}

	@Override
	@SideOnly(Side.CLIENT)
	protected int addShiftInfo(ItemStack stack, EntityPlayer player, List list, boolean simulate) {
		return 0;
	}

}
