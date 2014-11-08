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
	public static final String MOD_URL = "http://75.68.113.97:8080/downloads/" + MOD_NAME.toLowerCase() + "/versions/Project-Zed-1.1.";;
	
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
	public class Constants {

		/** Unit represented as string. */
		public static final String ENERGY_UNIT = "McU";
		
		/** Base unit for machine's energy consumption. */
		public static final int BASE_MACH_USAGE = 10;
		
		/** Base unit for transfer unit for basic pipe. */
		public static final int BASE_PIPE_TRANSFER_RATE = 20;
		
		private Constants() {
		}

	}

}
