/* This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under the terms of the GNU General Public 
* License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Project-Zed is 
* distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
* PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along 
* with Project-Zed. If not, see <http://www.gnu.org/licenses/>
*/
package com.projectzed.mod.util;

import com.hockeyhurd.api.util.AbstractReference;

/**
 * Reference class containing version numbers, modid, and update url. 
 * 
 * @author hockeyhurd
 * @version Oct 19, 2014
 */
public class Reference extends AbstractReference {

	/** Current build number. */
	public static final short BUILD = 12;
	
	/** Current version with included build number. */
	public static final String VERSION = "v1.1." + BUILD;
	
	/** Current mod name. */
	public static final String MOD_NAME = "ProjectZed";
	
	/** Not required but is available. NOTE: if not using it, set to null! */
	// public static final String MOD_URL = "http://73.17.180.186:8080/downloads/" + MOD_NAME.toLowerCase() + "/versions/Project-Zed-alpha-1.1.";
	// public static final String MOD_URL = "https://dl.dropboxusercontent.com/u/276611945/Minecraft/mods/" + MOD_NAME.toLowerCase() + "/versions/Project-Zed-alpha-1.1.";
	public static final String MOD_URL = "https://dl.dropboxusercontent.com/u/276611945/minecraft/mods/" + MOD_NAME.toLowerCase() + "/version.txt";
	
	/**
	 * Closed constructor, may not be init outside of this class.
	 */
	private Reference() {
	}
	
	/**
	 * Class used as localization for most/all contants.
	 * 
	 * @author hockeyhurd
	 * @version Nov 8, 2014
	 */
	public static final class Constants {

		// Kept old in case of reverting or need of comparison.
		
		/** Unit represented as string. */
		public static final String ENERGY_UNIT = "McU";
		
		/** Supported RF unit represented as string. */
		public static final String RF_ENERGY_UNIT = "RF";
		
		/** Common fluid unit for mc standing for milibucket. */
		public static final String FLUID_UNIT = "mb";
		
		/** Base unit for machine's energy consumption. */
		public static final int BASE_MACH_USAGE = 20; 
		// public static final int BASE_MACH_USAGE = 160;
		
		/** Base unit for transfer unit for basic pipe. */
		// public static final int BASE_PIPE_TRANSFER_RATE = 320;
		// public static final int BASE_PIPE_TRANSFER_RATE = 20;
		public static final int BASE_PIPE_TRANSFER_RATE = 320 * 5;
		
		public static final int TIER2_ENERGY_PIPE_MULTIPLIER = 2;
		public static final int TIER3_ENERGY_PIPE_MULTIPLIER = 8;
		
		/** Base item use rate for powered items. */ 
		public static final int BASE_ITEM_USE_RATE = 10;
		
		/** Base capacity for powered items. */
		public static final int BASE_ITEM_Capacity_RATE = 10000;
		
		/** Base charge rate for powered items. */
		public static final int BASE_ITEM_CHARGE_RATE = 10;
		
		/**
		 * Conversion rate from mcu to rf.
		 * <br>MCU to EU = MCU / 10.
		 * <br>EU to RF = EU * 2.5
		 * <br>Therefore 1f / 10f * 2.5f
		 */
		public static final float MCU_TO_RF = 1f / 10f * 2.5f; 
		
		public static final float RF_TO_MCU = 1f * 10f / 2.5f;
		
		/** Base unit for transfer rate of all things fluid. */
		public static final int BASE_FLUID_TRANSFER_RATE = 100;

		private Constants() {
		}
		
		/**
		 * Function used to quickly convert any amount of energy in McU to RF.
		 * 
		 * @param mcu energy in McU
		 * @return energy value in RF.
		 */
		public static int getRFFromMcU(int mcu) {
			float val = mcu * MCU_TO_RF;
			int retVal = (int) Math.floor(val);
			return retVal;
		}
		
		/**
		 * Function used to quickly convert any amount of energy in McU to RF.
		 * 
		 * @param mcu energy
		 * @param percent penalty percent.
		 * @return energy value in RF.
		 */
		public static int getRFFromMcU(int mcu, int percent) {
			float val = mcu * MCU_TO_RF;
			
			if (percent > 0 && percent < 100) val *= 1f - percent / 100f;
			
			int retVal = (int) Math.floor(val);
			return retVal;
		}
		
		/**
		 * Function used to quickly convert any amount of energy in RF to McU.
		 * 
		 * @param rf energy in RF.
		 * @return energy value in McU
		 */
		public static int getMcUFromRF(int rf) {
			return getMcUFromRF(rf, 0);
		}
		
		/**
		 * Function used to qucikly convert any amount of energy in RF to McU.
		 * 
		 * @param rf rf energy.
		 * @param percent penalty percent.
		 * @return energy value in McU.
		 */
		public static int getMcUFromRF(int rf, int percent) {
			float val = rf * RF_TO_MCU;
			
			if (percent > 0 && percent < 100) val *= 1f - percent / 100f;
			
			int retVal = (int) Math.floor(val);
			return retVal;
		}
		
		/**
		 * Function used to convert values to a more readable string value.
		 * <br><bold>NOTE:</bold> This function should mostly be used in gui's. 
		 * 
		 * @param amount amount to 'convert'.
		 * @return formatted string.
		 */
		public static String convertToString(double amount) {
			String ret = "";
			
			if (amount >= 1e6 && amount < 1e9) {
				amount /= 1e6;
				ret = amount + " mil.";
			}
			
			else if (amount >= 1e9) {
				amount /= 1e9;
				ret = amount + " bil.";
			}
			
			return ret;
		}

	}

}
