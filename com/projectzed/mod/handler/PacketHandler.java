/* This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under the terms of the GNU General Public 
* License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Project-Zed is 
* distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
* PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along 
* with Project-Zed. If not, see <http://www.gnu.org/licenses/>
*/
package com.projectzed.mod.handler;

import com.projectzed.mod.handler.message.*;
import com.projectzed.mod.util.Reference;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;

/**
 * Class used for syncing packets between server 
 * and client sides respectively.
 * 
 * @author hockeyhurd
 * @version Oct 22, 2014
 */
public class PacketHandler {

	public static final SimpleNetworkWrapper INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel(Reference.MOD_NAME);
	
	public static void init() {
		INSTANCE.registerMessage(MessageTileEntityGenerator.class, MessageTileEntityGenerator.class, 0, Side.CLIENT);
		INSTANCE.registerMessage(MessageTileEntityMachine.class, MessageTileEntityMachine.class, 1, Side.CLIENT);
		INSTANCE.registerMessage(MessageTileEntityFabricationTable.class, MessageTileEntityFabricationTable.class, 2, Side.SERVER);
		INSTANCE.registerMessage(MessageTileEntityFabricationTable.class, MessageTileEntityFabricationTable.class, 3, Side.CLIENT);
		INSTANCE.registerMessage(MessageTileEntityRFBridge.class, MessageTileEntityRFBridge.class, 4, Side.CLIENT);
		INSTANCE.registerMessage(MessageTileEntityEnergyContainer.class, MessageTileEntityEnergyContainer.class, 5, Side.CLIENT);
		INSTANCE.registerMessage(MessageTileEntityEnergyContainer.class, MessageTileEntityEnergyContainer.class, 6, Side.SERVER);
		INSTANCE.registerMessage(MessageTileEntityFluidTank.class, MessageTileEntityFluidTank.class, 7, Side.CLIENT);
	}
	
}
