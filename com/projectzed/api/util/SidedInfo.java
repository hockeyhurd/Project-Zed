/*
 * This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software Foundation, either version 3
 *  of the License, or (at your option) any later version. Project-Zed is distributed in the hope that it will be
 *  useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 *  FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details. You should
 *  have received a copy of the GNU General Public License along with Project-Zed. If not, see <http://www.gnu
 *  .org/licenses/>
 */

package com.projectzed.api.util;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.NetworkRegistry.TargetPoint;
import net.minecraftforge.fml.relauncher.Side;

/**
 * Class for storing and handling sided based information.
 *
 * @author hockeyhurd
 * @version 5/3/2016.
 */
public final class SidedInfo {

	public final Side side;
	public final EnumClientPacket packet;

	/**
	 * @param side Side to send to.
	 */
	public SidedInfo(Side side) {
		this(side, EnumClientPacket.DEFAULT, null, null);
	}

	/**
	 * @param side Side to send to.
	 * @param packet EnumClientPacket containing info on where to send packets.
	 */
	public SidedInfo(Side side, EnumClientPacket packet, TargetPoint targetPoint, EntityPlayer player) {
		this.side = side;
		this.packet = packet;
		packet.targetPoint = targetPoint;
		packet.player = player;
	}

	public boolean isSideClient() {
		return side == Side.CLIENT;
	}

	public boolean isSideServer() {
		return side == Side.SERVER;
	}

	public enum EnumClientPacket {
		ALL, ALL_AROUND, PLAYER, DEFAULT;

		TargetPoint targetPoint;
		EntityPlayer player;

		public TargetPoint getTargetPoint() {
			return targetPoint;
		}

		public EntityPlayer getPlayer() {
			return player;
		}
	}

}
