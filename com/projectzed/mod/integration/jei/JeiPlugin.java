package com.projectzed.mod.integration.jei;

import mezz.jei.api.*;

import javax.annotation.Nonnull;

/**
 * @author hockeyhurd
 * @version 10/25/2016.
 */
@JEIPlugin
public class JeiPlugin extends BlankModPlugin {

	private static IJeiRuntime jeiRuntime = null;

	@Override
	public void register(@Nonnull IModRegistry registry) {
		IJeiHelpers jeiHelpers = registry.getJeiHelpers();
		IGuiHelper guiHelper = jeiHelpers.getGuiHelper();

		FabricationTableRecipeTransferHandler.register(registry);
		StoneCraftingTableRecipeTransferHandler.register(registry);
	}

	@Override
	public void onRuntimeAvailable(@Nonnull IJeiRuntime jeiRuntime) {
		JeiPlugin.jeiRuntime = jeiRuntime;
		JeiAccessor.jeiRuntimeAvailable = true;
	}

	public static void setFilterText(@Nonnull String filterText) {
		jeiRuntime.getItemListOverlay().setFilterText(filterText);
	}

	public static @Nonnull String getFilterText() {
		return jeiRuntime.getItemListOverlay().getFilterText();
	}

}
