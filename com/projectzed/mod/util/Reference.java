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
	public static final short BUILD = 1;
	
	/** Current version with included build number. */
	public static final String VERSION = "v1.1." + BUILD;
	
	/** Current mod name. */
	public static final String MOD_NAME = "ProjectZed";
	
	/** Not required but is available. NOTE: if not using it, set to null! */
	public static final String MOD_URL = "http://75.68.113.97:8080/downloads/" + MOD_NAME.toLowerCase() + "/versions/Project-Zed-alpha-1.1.";
	// public static final String MOD_URL = "https://dl.dropboxusercontent.com/u/276611945/Minecraft/mods/" + MOD_NAME.toLowerCase() + "/versions/Project-Zed-alpha-1.1.";
	
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
	public static class Constants {

		// Kept old in case of reverting or need of comparison.
		
		/** Unit represented as string. */
		public static final String ENERGY_UNIT = "McU";
		
		/** Base unit for machine's energy consumption. */
		// public static final int BASE_MACH_USAGE = 40; 
		public static final int BASE_MACH_USAGE = 160;
		
		/** Base unit for transfer unit for basic pipe. */
		// public static final int BASE_PIPE_TRANSFER_RATE = 320;
		// public static final int BASE_PIPE_TRANSFER_RATE = 20;
		public static final int BASE_PIPE_TRANSFER_RATE = 320;
		
		/**
		 * Conversion rate from mcu to rf.
		 * <br>MCU to EU = MCU / 10.
		 * <br>EU to RF = EU * 2.5
		 * <br>Therefore 1f / 10f * 2.5f
		 */
		public static final float MCU_TO_RF = 1f / 10f * 2.5f; 
		
		public static final float RF_TO_MCU = 1f * 10f / 2.5f;
		
		/**
		 * Function used to quickly convert any amount of energy in McU to RF.
		 * @param mcu = energy in McU
		 * @return energy value in RF.
		 */
		public static int getRFFromMcU(int mcu) {
			float val = mcu * MCU_TO_RF;
			int retVal = (int) Math.floor(val);
			return retVal;
		}
		
		/**
		 * Function used to quickly convert any amount of energy in RF to McU.
		 * @param rf = energy in RF.
		 * @return energy value in McU
		 */
		public static int getMcUFromRF(int rf) {
			float val = rf * RF_TO_MCU;
			int retVal = (int) Math.floor(val);
			return retVal;
		}
		
		private Constants() {
		}

	}

}
