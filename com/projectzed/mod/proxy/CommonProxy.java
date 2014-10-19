package com.projectzed.mod.proxy;

import java.util.HashMap;

import net.minecraftforge.common.MinecraftForge;

import com.hockeyhurd.api.handler.NotifyPlayerOnJoinHandler;
import com.hockeyhurd.api.handler.UpdateHandler;
import com.projectzed.mod.ProjectZed;
import com.projectzed.mod.util.Reference;

/**
 * Common proxy for both client and server.
 * 
 * @author hockeyhurd
 * @version Oct 19, 2014
 */
public class CommonProxy {

	protected UpdateHandler updateHandler;
	protected HashMap<Short, String> map;
	public boolean updateFlag = false;
	
	/**
	 * Default Constructor
	 */
	public CommonProxy() {
	}
	 
	/**
	 * To be used in the ClientProxy and overriden.
	 */
	public void registerRenderInformation() {
	}
	
	/**
	 * Method used for init everything: blocks, items, handlers, etc.
	 */
	public void init() {
	}
	
	public void registerUpdateHandler() {
		updateHandler = new UpdateHandler(Reference.class);
		updateHandler.check();
		this.map = updateHandler.getMap();
		this.updateFlag = updateHandler.getUpToDate();
		
		// TODO: Temporarily set to false until set-up is done on the update-server first.
		MinecraftForge.EVENT_BUS.register(new NotifyPlayerOnJoinHandler(updateHandler, this.map, Reference.class, this.updateFlag, true, false /*ProjectZed.configHandler.allowUpdating()*/));
	}

}
