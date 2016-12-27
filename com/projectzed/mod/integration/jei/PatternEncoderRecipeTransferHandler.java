package com.projectzed.mod.integration.jei;

import com.projectzed.mod.ProjectZed;
import com.projectzed.mod.container.ContainerPatternEncoder;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.gui.IGuiIngredient;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.recipe.VanillaRecipeCategoryUid;
import mezz.jei.api.recipe.transfer.IRecipeTransferError;
import mezz.jei.api.recipe.transfer.IRecipeTransferHandler;
import mezz.jei.api.recipe.transfer.IRecipeTransferRegistry;
import mezz.jei.transfer.BasicRecipeTransferInfo;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;

/**
 * @author hockeyhurd
 * @version 12/26/2016.
 */
public class PatternEncoderRecipeTransferHandler implements IRecipeTransferHandler {

	private final IModRegistry registry;
	private final @Nonnull BasicRecipeTransferInfo<ContainerPatternEncoder> transferInfo;

	private PatternEncoderRecipeTransferHandler(IModRegistry registry) {
		this.registry = registry;
		transferInfo = new BasicRecipeTransferInfo<ContainerPatternEncoder>(ContainerPatternEncoder.class, VanillaRecipeCategoryUid.CRAFTING,
				0, 9, 10, 0);
	}

	public static void register(IModRegistry registry) {
		final IRecipeTransferRegistry recipeTransferHandler = registry.getRecipeTransferRegistry();
		recipeTransferHandler.addRecipeTransferHandler(new PatternEncoderRecipeTransferHandler(registry));
		registry.addRecipeCategoryCraftingItem(new ItemStack(ProjectZed.patternEncoder), VanillaRecipeCategoryUid.CRAFTING);
	}

	@Override
	public Class getContainerClass() {
		return ContainerPatternEncoder.class;
	}

	@Override
	public String getRecipeCategoryUid() {
		return VanillaRecipeCategoryUid.CRAFTING;
	}

	@Nullable
	@Override
	public IRecipeTransferError transferRecipe(Container container, IRecipeLayout recipeLayout, EntityPlayer player, boolean maxTransfer,
			boolean doTransfer) {

		if (!(container instanceof ContainerPatternEncoder))
			return registry.getJeiHelpers().recipeTransferHandlerHelper().createInternalError();

		final ContainerPatternEncoder containerPatternEncoder = (ContainerPatternEncoder) container;
		ItemStack[][] ingredients = new ItemStack[9][];
		IGuiItemStackGroup itemStacks = recipeLayout.getItemStacks();
		Map<Integer, ? extends IGuiIngredient<ItemStack>> guiIngredients = itemStacks.getGuiIngredients();

		for (int i = 0; i < 9; i++) {
			final int slotOffset = i + 1;

			if (guiIngredients.containsKey(slotOffset)) {
				final List<ItemStack> allIngredients = guiIngredients.get(slotOffset).getAllIngredients();

				if (!allIngredients.isEmpty())
					ingredients[i] = allIngredients.toArray(new ItemStack[allIngredients.size()]);
			}
		}

		if (doTransfer) {
			containerPatternEncoder.clearSlots();
			containerPatternEncoder.fillCraftingGrid(ingredients);
		}

		return null;
	}

}
