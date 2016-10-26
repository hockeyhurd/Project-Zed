package com.projectzed.mod.integration.jei;

import com.projectzed.mod.ProjectZed;
import com.projectzed.mod.container.ContainerFabricationTable;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.gui.IGuiIngredient;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.recipe.VanillaRecipeCategoryUid;
import mezz.jei.api.recipe.transfer.IRecipeTransferError;
import mezz.jei.api.recipe.transfer.IRecipeTransferHandler;
import mezz.jei.api.recipe.transfer.IRecipeTransferRegistry;
import mezz.jei.transfer.BasicRecipeTransferInfo;
import mezz.jei.util.StackHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author hockeyhurd
 * @version 10/25/2016.
 */
public class FabricationTableRecipeTransferHandler implements IRecipeTransferHandler {

	private final IModRegistry registry;
	private final @Nonnull
	BasicRecipeTransferInfo transferInfo;

	private FabricationTableRecipeTransferHandler(IModRegistry registry) {
		this.registry = registry;
		transferInfo = new BasicRecipeTransferInfo(ContainerFabricationTable.class, VanillaRecipeCategoryUid.CRAFTING, 0, 9, 10, 6 * 12);
	}

	public static void register(IModRegistry registry) {
		final IRecipeTransferRegistry recipeTransferRegistry = registry.getRecipeTransferRegistry();
		recipeTransferRegistry.addRecipeTransferHandler(new FabricationTableRecipeTransferHandler(registry));
		registry.addRecipeCategoryCraftingItem(new ItemStack(ProjectZed.fabricationTable), VanillaRecipeCategoryUid.CRAFTING);
	}

	@Override
	public Class getContainerClass() {
		return ContainerFabricationTable.class;
	}

	@Override
	public String getRecipeCategoryUid() {
		return VanillaRecipeCategoryUid.CRAFTING;
	}

	@Nullable
	@Override
	public IRecipeTransferError transferRecipe(Container container, IRecipeLayout recipeLayout, EntityPlayer player, boolean maxTransfer,
			boolean doTransfer) {

		if (!(container instanceof ContainerFabricationTable))
			return registry.getJeiHelpers().recipeTransferHandlerHelper().createInternalError();

		final ContainerFabricationTable containerFabTable = (ContainerFabricationTable) container;
		if (doTransfer) containerFabTable.clearCraftingGrid();

		List<Integer> missingItemSlots = new ArrayList<Integer>();
		ItemStack[][] ingrediants = new ItemStack[9][];
		IGuiItemStackGroup itemStacks = recipeLayout.getItemStacks();
		Map<Integer, ? extends IGuiIngredient<ItemStack>> guiIngrediants = itemStacks.getGuiIngredients();

		for (int i = 0; i < 9; i++) {
			final int slotOffset = i + 1;

			if (guiIngrediants.containsKey(slotOffset)) {
				final List<ItemStack> allIngredients = guiIngrediants.get(slotOffset).getAllIngredients();
				if (!allIngredients.isEmpty()) {
					if (containerContainsIngrediant(containerFabTable, allIngredients))
						ingrediants[i] = allIngredients.toArray(new ItemStack[allIngredients.size()]);
					else missingItemSlots.add(slotOffset);
				}
			}
		}

		if (missingItemSlots.isEmpty()) {
			if (doTransfer) containerFabTable.fillCraftingGrid(ingrediants, maxTransfer ? 0x40 : 1);
			return null;
		}

		return registry.getJeiHelpers().recipeTransferHandlerHelper().createUserErrorForSlots("Recipe transfer error!", missingItemSlots);
	}

	private boolean containerContainsIngrediant(ContainerFabricationTable container, List<ItemStack> ingrediantList) {
		List<Slot> slots = transferInfo.getInventorySlots(container);
		List<ItemStack> availabeItems = new ArrayList<ItemStack>(slots.size() << 1);

		for (Slot slot : slots) {
			if (slot.getHasStack()) availabeItems.add(slot.getStack());
		}

		final StackHelper stackHelper = (StackHelper) registry.getJeiHelpers().getStackHelper();
		return stackHelper.containsAnyStack(availabeItems, ingrediantList) != null;
	}

}
