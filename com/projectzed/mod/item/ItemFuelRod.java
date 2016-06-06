/* This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under the terms of the GNU General Public 
* License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Project-Zed is 
* distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
* PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along 
* with Project-Zed. If not, see <http://www.gnu.org/licenses/>
*/
package com.projectzed.mod.item;

import com.hockeyhurd.hcorelib.api.item.AbstractHCoreItem;
import com.projectzed.mod.ProjectZed;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

/**
 * Class containing code for nuclear fuel rods.
 * 
 * @author hockeyhurd
 * @version Dec 18, 2014
 */
public class ItemFuelRod extends AbstractHCoreItem {

	private ResourceLocation[] resourceLocations;

	/**
	 * @param name
	 * @param assetDir
	 */
	public ItemFuelRod(String name, String assetDir) {
		super(ProjectZed.modCreativeTab, assetDir, name);
		this.setHasSubtypes(true);
		// this.setMaxDamage(10);
		this.maxStackSize = 1;
		resourceLocations = new ResourceLocation[11];

		for (int i = 0; i < resourceLocations.length; i++)
			resourceLocations[i] = new ResourceLocation(assetDir, name + '_' + i);
	}

	@Override
	public String getUnlocalizedName(ItemStack stack) {
		String name = super.getUnlocalizedName();
		final int meta = stack.getMetadata();

		if (meta == 0) name += "_Empty";
		else if (meta == resourceLocations.length - 1) name += "_Full";

		return name;
	}

	@Override
	public ResourceLocation getResourceLocation(int meta) {
		return resourceLocations[meta];
	}

	@Override
	public int getSizeOfSubItems() {
		return resourceLocations.length;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void getSubItems(Item itemIn, CreativeTabs tab, List<ItemStack> subItems) {
		for (int i = 0; i < getSizeOfSubItems(); i++) {
			subItems.add(new ItemStack(itemIn, 1, i));
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, EntityPlayer player, List<String> list, boolean flag) {
		final int damage = stack.getItemDamage();

		if (/*damage > 0 && */damage < resourceLocations.length) {
			// int left = 10 - stack.getItemDamage();

			list.add(TextFormatting.GREEN + "Uses left: " + TextFormatting.WHITE + damage);
		}
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn, EnumHand hand) {
		if (!worldIn.isRemote) {
			ProjectZed.logHelper.info("name:", itemStackIn.getUnlocalizedName(), "meta:", itemStackIn.getMetadata());
		}

		return new ActionResult<ItemStack>(EnumActionResult.PASS, itemStackIn);
	}
	
}
