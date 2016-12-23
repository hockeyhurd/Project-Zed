package com.projectzed.mod.integration.jei;

import com.projectzed.mod.ProjectZed;
import com.projectzed.mod.container.ContainerStoneCraftingTable;
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
import net.minecraft.inventory.ContainerPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author hockeyhurd
 * @version 10/26/2016.
 */
public final class StoneCraftingTableRecipeTransferHandler implements IRecipeTransferHandler {

	private final IModRegistry registry;
	private final @Nonnull BasicRecipeTransferInfo transferInfo;

	private StoneCraftingTableRecipeTransferHandler(IModRegistry registry) {
		this.registry = registry;
		// transferInfo = new BasicRecipeTransferInfo<ContainerStoneCraftingTable>(ContainerStoneCraftingTable.class, VanillaRecipeCategoryUid.CRAFTING, 0, 9, 10, 36);
		transferInfo = new BasicRecipeTransferInfo<ContainerPlayer>(ContainerPlayer.class, VanillaRecipeCategoryUid.CRAFTING, 0, 9, 10, 36);
	}

	public static void register(IModRegistry registry) {
		final IRecipeTransferRegistry recipeTransferRegistry = registry.getRecipeTransferRegistry();
		recipeTransferRegistry.addRecipeTransferHandler(new StoneCraftingTableRecipeTransferHandler(registry));
		registry.addRecipeCategoryCraftingItem(new ItemStack(ProjectZed.stoneCraftingTable), VanillaRecipeCategoryUid.CRAFTING);
	}

	@Override
	public Class getContainerClass() {
		return ContainerStoneCraftingTable.class;
	}

	@Override
	public String getRecipeCategoryUid() {
		return VanillaRecipeCategoryUid.CRAFTING;
	}

	@Nullable
	@Override
	public IRecipeTransferError transferRecipe(Container container, IRecipeLayout recipeLayout, EntityPlayer player, boolean maxTransfer,
			boolean doTransfer) {

		if (!(container instanceof ContainerStoneCraftingTable))
			return registry.getJeiHelpers().recipeTransferHandlerHelper().createInternalError();

		final ContainerStoneCraftingTable containerStoneCraftingTable = (ContainerStoneCraftingTable) container;
		if (doTransfer) containerStoneCraftingTable.clearCraftingGrid();

		List<Integer> missingItemSlots = new ArrayList<Integer>();
		ItemStack[][] ingredients = new ItemStack[9][];
		IGuiItemStackGroup itemStacks = recipeLayout.getItemStacks();
		Map<Integer, ? extends IGuiIngredient<ItemStack>> guiIngredients = itemStacks.getGuiIngredients();

		for (int i = 0; i < 9; i++) {
			final int slotOffset = i + 1;

			if (guiIngredients.containsKey(slotOffset)) {
				final List<ItemStack> allIngredients = guiIngredients.get(slotOffset).getAllIngredients();
				if (!allIngredients.isEmpty()) {
					if (containerContainsIngredient(containerStoneCraftingTable, allIngredients))
						ingredients[i] = allIngredients.toArray(new ItemStack[allIngredients.size()]);
					else missingItemSlots.add(slotOffset);
				}
			}
		}

		if (missingItemSlots.isEmpty()) {
			if (doTransfer) {
				containerStoneCraftingTable.fillCraftingGrid(ingredients, maxTransfer ? 0x40 : 1);

				/*for (int i = 0; i < player.inventory.getSizeInventory(); i++) {
					ItemStack stackInSlot = player.inventory.getStackInSlot(i);
					if (stackInSlot == null) continue;

					boolean canEscape = false;

					for (int index = 0; index < ingredients.length; index++) {
						if (ingredients[index] == null) continue;

						for (int type = 0; type < ingredients[index].length; type++) {
							if (stackInSlot.isItemEqual(ingredients[index][type])) {
								stackInSlot.stackSize -= ingredients[index][type].stackSize;

								if (stackInSlot.stackSize == 0) {
									player.inventory.setInventorySlotContents(i, null);
									canEscape = true;
									break;
								}

								player.inventory.setInventorySlotContents(i, stackInSlot);
							}
						}

						if (canEscape) break;
					}
				}

				player.inventory.markDirty();*/
			}
			return null;
		}

		return registry.getJeiHelpers().recipeTransferHandlerHelper().createUserErrorForSlots("Recipe transfer error!", missingItemSlots);
	}

	/*@Nullable
	@Override
	public IRecipeTransferError transferRecipe(ContainerPlayer container, IRecipeLayout recipeLayout, EntityPlayer player, boolean maxTransfer, boolean doTransfer) {
		if (!SessionData.isJeiOnServer()) {
			String tooltipMessage = Translator.translateToLocal("jei.tooltip.error.recipe.transfer.no.server");
			return handlerHelper.createUserErrorWithTooltip(tooltipMessage);
		}

		if (!transferHelper.canHandle(container)) {
			return handlerHelper.createInternalError();
		}

		Map<Integer, Slot> inventorySlots = new HashMap<Integer, Slot>();
		for (Slot slot : transferHelper.getInventorySlots(container)) {
			inventorySlots.put(slot.slotNumber, slot);
		}

		Map<Integer, Slot> craftingSlots = new HashMap<Integer, Slot>();
		for (Slot slot : transferHelper.getRecipeSlots(container)) {
			craftingSlots.put(slot.slotNumber, slot);
		}

		IGuiItemStackGroup itemStackGroup = recipeLayout.getItemStacks();
		int inputCount = 0;
		{
			// indexes that do not fit into the player crafting grid
			Set<Integer> badIndexes = ImmutableSet.of(2, 5, 6, 7, 8);

			int inputIndex = 0;
			for (IGuiIngredient<ItemStack> ingredient : itemStackGroup.getGuiIngredients().values()) {
				if (ingredient.isInput()) {
					if (!ingredient.getAllIngredients().isEmpty()) {
						inputCount++;
						if (badIndexes.contains(inputIndex)) {
							String tooltipMessage = Translator.translateToLocal("jei.tooltip.error.recipe.transfer.too.large.player.inventory");
							return handlerHelper.createUserErrorWithTooltip(tooltipMessage);
						}
					}
					inputIndex++;
				}
			}
		}

		// compact the crafting grid into a 2x2 area
		List<IGuiIngredient<ItemStack>> guiIngredients = new ArrayList<IGuiIngredient<ItemStack>>();
		for (IGuiIngredient<ItemStack> guiIngredient : itemStackGroup.getGuiIngredients().values()) {
			if (guiIngredient.isInput()) {
				guiIngredients.add(guiIngredient);
			}
		}
		IGuiItemStackGroup playerInvItemStackGroup = new GuiItemStackGroup(null);
		int[] playerGridIndexes = {0, 1, 3, 4};
		for (int i = 0; i < 4; i++) {
			IGuiIngredient<ItemStack> ingredient = guiIngredients.get(playerGridIndexes[i]);
			playerInvItemStackGroup.init(i, true, 0, 0);
			playerInvItemStackGroup.set(i, ingredient.getAllIngredients());
		}

		Map<Integer, ItemStack> availableItemStacks = new HashMap<Integer, ItemStack>();
		int filledCraftSlotCount = 0;
		int emptySlotCount = 0;

		for (Slot slot : craftingSlots.values()) {
			final ItemStack stack = slot.getStack();
			if (!stack.isEmpty()) {
				if (!slot.canTakeStack(player)) {
					ProjectZed.logHelper.severe("Recipe Transfer helper {} does not work for container {}. Player can't move item out of Crafting Slot number {}", transferHelper.getClass(), container.getClass(), slot.slotNumber);
					return handlerHelper.createInternalError();
				}
				filledCraftSlotCount++;
				availableItemStacks.put(slot.slotNumber, stack.copy());
			}
		}

		for (Slot slot : inventorySlots.values()) {
			final ItemStack stack = slot.getStack();
			if (!stack.isEmpty()) {
				availableItemStacks.put(slot.slotNumber, stack.copy());
			} else {
				emptySlotCount++;
			}
		}

		// check if we have enough inventory space to shuffle items around to their final locations
		if (filledCraftSlotCount - inputCount > emptySlotCount) {
			String message = Translator.translateToLocal("jei.tooltip.error.recipe.transfer.inventory.full");
			return handlerHelper.createUserErrorWithTooltip(message);
		}

		StackHelper.MatchingItemsResult matchingItemsResult = stackHelper.getMatchingItems(availableItemStacks, playerInvItemStackGroup.getGuiIngredients());

		if (matchingItemsResult.missingItems.size() > 0) {
			String message = Translator.translateToLocal("jei.tooltip.error.recipe.transfer.missing");
			matchingItemsResult = stackHelper.getMatchingItems(availableItemStacks, itemStackGroup.getGuiIngredients());
			return handlerHelper.createUserErrorForSlots(message, matchingItemsResult.missingItems);
		}

		List<Integer> craftingSlotIndexes = new ArrayList<Integer>(craftingSlots.keySet());
		Collections.sort(craftingSlotIndexes);

		List<Integer> inventorySlotIndexes = new ArrayList<Integer>(inventorySlots.keySet());
		Collections.sort(inventorySlotIndexes);

		// check that the slots exist and can be altered
		for (Map.Entry<Integer, Integer> entry : matchingItemsResult.matchingItems.entrySet()) {
			int craftNumber = entry.getKey();
			int slotNumber = craftingSlotIndexes.get(craftNumber);
			if (slotNumber < 0 || slotNumber >= container.inventorySlots.size()) {
				ProjectZed.logHelper.severe("Recipes Transfer Helper {} references slot {} outside of the inventory's size {}", transferHelper.getClass(), slotNumber, container.inventorySlots.size());
				return handlerHelper.createInternalError();
			}
		}

		if (doTransfer) {
			PacketRecipeTransfer packet = new PacketRecipeTransfer(matchingItemsResult.matchingItems, craftingSlotIndexes, inventorySlotIndexes, maxTransfer);
			JustEnoughItems.getProxy().sendPacketToServer(packet);
		}

		return null;
	}*/

	private boolean containerContainsIngredient(ContainerStoneCraftingTable container, List<ItemStack> ingredientList) {
		List<Slot> slots = transferInfo.getInventorySlots(container);
		List<ItemStack> availableItems = new ArrayList<ItemStack>(slots.size() << 1);

		for (Slot slot : slots) {
			if (slot.getHasStack()) availableItems.add(slot.getStack());
		}

		final StackHelper stackHelper = (StackHelper) registry.getJeiHelpers().getStackHelper();
		return stackHelper.containsAnyStack(availableItems, ingredientList) != null;
	}
	
}
