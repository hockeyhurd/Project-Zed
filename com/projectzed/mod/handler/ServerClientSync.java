package com.projectzed.mod.handler;

import cpw.mods.fml.relauncher.Side;

/**
 * Class used to try and fix sync issues with guis.
 * <br>NOTE: This is a temporary fix until further investigated.
 * 
 * @author hockeyhurd
 * @version Oct 21, 2014
 */
@Deprecated
public class ServerClientSync {

	private int serverPacket, clientPacket;
	private boolean fixClient;
	
	public ServerClientSync(boolean fixClient) {
		this.fixClient = fixClient;
	}
	
	public void resolve() {
		if (this.clientPacket == this.serverPacket) return;
		if (fixClient) this.clientPacket = this.serverPacket;
		else this.serverPacket = this.clientPacket;
	}
	
	public void setServerPacket(int packet) {
		this.serverPacket = packet;
	}
	
	public int getServerPacket() {
		return this.serverPacket;
	}
	
	public void setClientPacket(int packet) {
		this.clientPacket = packet;
	}
	
	public int getClientPacket() {
		return this.clientPacket;
	}
	
}
