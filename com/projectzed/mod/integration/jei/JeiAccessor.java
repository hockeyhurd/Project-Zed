package com.projectzed.mod.integration.jei;

import javax.annotation.Nonnull;

/**
 * @author hockeyhurd
 * @version 10/25/2016.
 */
public class JeiAccessor {

	static boolean jeiRuntimeAvailable = false;

	public static boolean isJeiRuntimeAvailable() {
		return jeiRuntimeAvailable;
	}

	public static void setFilterText(@Nonnull String filterText) {
		if (jeiRuntimeAvailable) {
			JeiPlugin.setFilterText(filterText);
		}
	}

	public static @Nonnull String getFilterText() {
		if (jeiRuntimeAvailable) {
			return JeiPlugin.getFilterText();
		}
		return "";
	}

}
