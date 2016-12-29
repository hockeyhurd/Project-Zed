package com.projectzed.mod.item.circuitry;

import com.hockeyhurd.hcorelib.api.creativetab.AbstractCreativeTab;
import com.hockeyhurd.hcorelib.api.item.AbstractHCoreItem;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import java.util.List;

/**
 * @author hockeyhurd
 * @version 12/29/2016.
 */
public class ItemMemCell extends AbstractHCoreItem {

	private static final int maxMeta = 4;
	private static final String[] naming = { "1k", "4k", "16k", "64k" };
	private final ResourceLocation[] locations;

	/**
	 * @param creativeTab Creative tab to place item in.
	 * @param name        Name of Item.
	 * @param assetDir    Asset directory for item texture.
	 */
	public ItemMemCell(AbstractCreativeTab creativeTab, String name, String assetDir) {
		super(creativeTab, assetDir, name);

		locations = new ResourceLocation[maxMeta];
		for (int i = 0; i < locations.length; i++)
			locations[i] = new ResourceLocation(assetDir, getMetaName(name, i));

		resourceLocation = locations[0];

		setHasSubtypes(true);
	}

	private static String getMetaName(String name, int meta) {
		return name + '_' + naming[meta];
	}

	@Override
	public ResourceLocation getResourceLocation(int meta) {
		return locations[meta];
	}

	@Override
	public int getSizeOfSubItems() {
		return maxMeta;
	}

	@Override
	public String getUnlocalizedName(ItemStack stack) {
		final int meta = stack.getItemDamage();
		return super.getUnlocalizedName() + '_' + naming[meta];
	}

	@Override
	public void getSubItems(Item item, CreativeTabs tab, List<ItemStack> subItems) {
		for (int i = 0; i < getSizeOfSubItems(); i++)
			subItems.add(new ItemStack(item, 1, i));
	}

}
