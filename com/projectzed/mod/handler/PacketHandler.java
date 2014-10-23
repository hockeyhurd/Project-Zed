package com.projectzed.mod.handler;

import com.projectzed.mod.handler.message.MessageTileEntityGenerator;
import com.projectzed.mod.handler.message.MessageTileEntityMachine;
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
	}
	
}
