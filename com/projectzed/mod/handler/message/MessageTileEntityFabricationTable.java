/* This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under the terms of the GNU General Public 
* License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Project-Zed is 
* distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
* PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along 
* with Project-Zed. If not, see <http://www.gnu.org/licenses/>
*/
package com.projectzed.mod.handler.message;

import com.hockeyhurd.hcorelib.api.math.Vector3;
import com.hockeyhurd.hcorelib.api.math.VectorHelper;
import com.projectzed.mod.container.ContainerFabricationTable;
import com.projectzed.mod.tileentity.TileEntityFabricationTable;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

/**
 * Class containing code for messaging buttons and other various requests for
 * fabrication table between client and server.
 * 
 * @author hockeyhurd
 * @version Nov 24, 2014
 */
public class MessageTileEntityFabricationTable implements IMessage, IMessageHandler<MessageTileEntityFabricationTable, IMessage> {

	private TileEntityFabricationTable te;
	private Vector3<Integer> vec;
	private int buttonHit;
	private int numSlots;
	private ItemStack[] slots;

	@Deprecated
	public MessageTileEntityFabricationTable() {
	}
	
	public MessageTileEntityFabricationTable(TileEntityFabricationTable te) {
		this(te, 0);
	}

	/**
	 * @param te te object as reference.
	 * @param buttonHit button hit.
	 */
	public MessageTileEntityFabricationTable(TileEntityFabricationTable te, int buttonHit) {
		this.te = te;
		this.vec = te.worldVec();
		this.buttonHit = buttonHit;
		this.numSlots = this.te.getSizeInventory();
		this.slots = new ItemStack[numSlots];
		
		syncStacks();
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * cpw.mods.fml.common.network.simpleimpl.IMessage#fromBytes(io.netty.buffer
	 * .ByteBuf)
	 */
	@Override
	public void fromBytes(ByteBuf buf) {
		if (vec == null) vec = new Vector3<Integer>();
		vec.x = buf.readInt();
		vec.y = buf.readInt();
		vec.z = buf.readInt();

		this.buttonHit = buf.readInt();
		this.numSlots = buf.readInt();
		
		this.slots = new ItemStack[this.numSlots];
		for (int i = 0; i < this.slots.length; i++) {
			this.slots[i] = ByteBufUtils.readItemStack(buf);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * cpw.mods.fml.common.network.simpleimpl.IMessage#toBytes(io.netty.buffer
	 * .ByteBuf)
	 */
	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(vec.x);
		buf.writeInt(vec.y);
		buf.writeInt(vec.z);
		buf.writeInt(this.buttonHit);
		buf.writeInt(this.numSlots);

		if (!isArrayValid()) syncStacks();
		for (int i = 0; i < this.slots.length; i++) {
			ByteBufUtils.writeItemStack(buf, this.slots[i]);
		}
	}
	
	private void syncStacks() {
		if (this.te != null) {
			if (this.slots == null) this.slots = new ItemStack[numSlots];
			
			for (int i = 0; i < this.slots.length; i++) {
				this.slots[i] = this.te.getStackInSlot(i);
			}
		}
	}
	
	private boolean isArrayValid() {
		return this.slots != null && this.slots.length > 0;
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * cpw.mods.fml.common.network.simpleimpl.IMessageHandler#onMessage(cpw.
	 * mods.fml.common.network.simpleimpl.IMessage,
	 * cpw.mods.fml.common.network.simpleimpl.MessageContext)
	 */
	@Override
	public IMessage onMessage(MessageTileEntityFabricationTable message, MessageContext ctx) {
		// TileEntity te =
		// FMLClientHandler.instance().getClient().theWorld.getTileEntity(message.x,
		// message.y, message.z);

		if (ctx.side == Side.SERVER) {
			World world = ctx.getServerHandler().playerEntity.worldObj;
			TileEntity te = world.getTileEntity(VectorHelper.toBlockPos(message.vec));
	
			if (world != null && te != null && te instanceof TileEntityFabricationTable) {
				TileEntityFabricationTable te2 = (TileEntityFabricationTable) te;
	
				if (message.slots != null && message.slots.length > 0) {
					
					if (message.buttonHit == 1) {
						EntityPlayer player = (EntityPlayer) ctx.getServerHandler().playerEntity;
						if (player != null && player.openContainer != null && player.openContainer instanceof ContainerFabricationTable) {
							ContainerFabricationTable cont = (ContainerFabricationTable) player.openContainer;
							cont.clearCraftingGrid();
						}
					}
					
					for (int i = 0; i < message.slots.length; i++) {
						te2.setInventorySlotContents(i, message.slots[i]);
					}
					
					te2.markDirty();
				}
			}
		}
		
		else if (ctx.side == Side.CLIENT) {
			TileEntity te = FMLClientHandler.instance().getClient().theWorld.getTileEntity(VectorHelper.toBlockPos(message.vec));
			
			if (te != null && te instanceof TileEntityFabricationTable) {
				TileEntityFabricationTable te2 = (TileEntityFabricationTable) te;
				
				if (message.slots != null && message.slots.length > 0) {
					for (int i = 0; i < message.slots.length; i++) {
						te2.setInventorySlotContents(i, message.slots[i]);
					}
				}
			}
 		}

		return null;
	}

}
