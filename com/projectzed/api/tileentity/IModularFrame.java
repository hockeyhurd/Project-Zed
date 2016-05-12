/* This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under the terms of the GNU General Public 
* License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Project-Zed is 
* distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
* PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along 
* with Project-Zed. If not, see <http://www.gnu.org/licenses/>
*/
package com.projectzed.api.tileentity;

import com.projectzed.api.util.EnumFrameType;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraftforge.common.util.ForgeDirection;

/**
 * Interface used for in tile entities to make it sided and changeable.
 * <br><br><bold>NOTE:</bold> This interface should only be implemented in said TE class.
 * <br>Also make sure you create an appropritate array for TE (see example below).
 * <code>
 * 	<pre>
 * 
 * 	public class MyTileEntity extends TileEntity implements IModularFrame {
 *		
 *		private byte[] openSides = new byte[ForgeDirection.VALID_DIRECTIONS.length];
 * 		
 * 		public MyTileEntity() {
 * 			super();
 * 		}
 * 
 * 		public void setSideValve(ForgeDirection dir, byte value) {
 * 			openSides[dir.ordinal()] = value;		
 * 		}
 * 
 * 		// NOTE: This example will have 3 possible values (relative to this TE):
 * 		// -1 for input, 0 for neither, 1 for output.
 * 		public void setSideValveAndRotate(ForgeDirection dir) {
 * 			openSides[dir.ordinal()] = (byte) (openSides[dir.ordinal()] == -1 ? 0 : (openSides[dir.ordinal()] == 0 ? 1 : -1));
 * 		}
 * 
 * 		public byte getSideValve(ForgeDirection dir) {
 * 			return openSides[dir.ordinal()];		
 * 		}
 * 
 * 		public byte getSideValve(int dir) {
 * 			return openSides[dir];		
 * 		}
 * 
 * 		public byte[] getSidedArray() {
 * 			return openSides;
 * 		}
 * 
 * 		public void onDataPacket(NetworkManager manager, S35PacketUpdateTileEntity packet) {
 * 			&ltYourStaticPacketHandler&gt.INSTANCE.getPacketFrom(new &ltYourMessageTileEntityClass&gt(this));
 * 		}
 * 
 * 		// insert other necessary code here:
 * 	}
 * 
 * 	</pre>
 * </code>
 * 
 * @author hockeyhurd
 * @version Jan 4, 2015
 */
public interface IModularFrame {

	/**
	 * @return type of modular frame; power, item, fluid, etc.
	 */
	EnumFrameType getType();
	
	/**
	 * Method used to set value to given side.
	 * @param dir direction to change.
	 * @param value value to set.
	 */
	void setSideValve(ForgeDirection dir, byte value);
	
	/**
	 * Method used to change the value of given sides value to next available value.
	 * @param dir direction to set.
	 */
	void setSideValveAndRotate(ForgeDirection dir);
	
	/**
	 * Function used to get value of said direction.
	 * @param dir direction to get value from.
	 * @return value of side.
	 */
	byte getSideValve(ForgeDirection dir);
	
	/**
	 * Function used to get value of said direction.
	 * @param dir direction represented by an ordinal of a ForgeDirection, direction. (0, 1, 2, 3, 4, 5).
	 * @return value of side.
	 */
	byte getSideValve(int dir);
	
	/**
	 * @return sided byte array.
	 */
	byte[] getSidedArray();
	
	/**
	 * Method used in TE's and is to make sure it is implemented so we can receive data from client (gui's primarily).
	 * @param manager network manager reference.
	 * @param packet packet receiving.
	 */
	void onDataPacket(NetworkManager manager, S35PacketUpdateTileEntity packet);
	
}
