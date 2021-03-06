/* This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under the terms of the GNU General Public 
* License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Project-Zed is 
* distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
* PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along 
* with Project-Zed. If not, see <http://www.gnu.org/licenses/>
*/
package com.projectzed.mod.handler.message;

import com.hockeyhurd.hcorelib.api.math.Vector3;
import com.hockeyhurd.hcorelib.api.math.VectorHelper;
import com.projectzed.mod.container.ContainerStoneCraftingTable;
import com.projectzed.mod.tileentity.machine.TileEntityStoneCraftingTable;
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
 * 
 * 
 * @author hockeyhurd
 * @version Apr 1, 2015
 */
public class MessageTileEntityStoneCraftingTable implements IMessage, IMessageHandler<MessageTileEntityStoneCraftingTable, IMessage> {

	private TileEntityStoneCraftingTable te;
	private Vector3<Integer> vec;
	private int numSlots;
	private ItemStack[] slots;
	private byte buttonHit;

	@Deprecated
	public MessageTileEntityStoneCraftingTable() {
	}
	
	public MessageTileEntityStoneCraftingTable(TileEntityStoneCraftingTable te) {
		this(te, (byte) -1);
	}
	
	public MessageTileEntityStoneCraftingTable(TileEntityStoneCraftingTable te, byte buttonHit) {
		this.te = te;
		this.vec = te.worldVec();
		this.buttonHit = buttonHit;
		this.numSlots = this.te.getSizeInventory();
		this.slots = new ItemStack[numSlots];
		
		syncStacks();
	}

	private void syncStacks() {
		if (this.te != null && this.numSlots > 0) {
			if (this.slots == null) this.slots = new ItemStack[numSlots];
			
			for (int i = 0; i < this.slots.length; i++) {
				this.slots[i] = this.te.getStackInSlot(i);
			}
		}
	}
	
	private boolean isArrayValid() {
		return this.slots != null && this.slots.length > 0;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		if (this.vec == null) this.vec = new Vector3<Integer>();
		this.vec.x = buf.readInt();
		this.vec.y = buf.readInt();
		this.vec.z = buf.readInt();

		this.buttonHit = buf.readByte();
		
		this.numSlots = buf.readInt();
		this.slots = new ItemStack[this.numSlots];
		for (int i = 0; i < this.slots.length; i++) {
			this.slots[i] = ByteBufUtils.readItemStack(buf);
		}
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(this.vec.x);
		buf.writeInt(this.vec.y);
		buf.writeInt(this.vec.z);
		buf.writeByte(this.buttonHit);
		buf.writeInt(this.numSlots);
		
		if (!isArrayValid()) syncStacks();
		for (int i = 0; i < this.slots.length; i++) {
			ByteBufUtils.writeItemStack(buf, this.slots[i]);
		}
	}

	@Override
	public IMessage onMessage(MessageTileEntityStoneCraftingTable message, MessageContext ctx) {
		if (ctx.side == Side.SERVER) {
			World world = ctx.getServerHandler().playerEntity.worldObj;
			TileEntity te = world.getTileEntity(VectorHelper.toBlockPos(message.vec));
			
			if (te != null && te instanceof TileEntityStoneCraftingTable) {
				TileEntityStoneCraftingTable te2 = (TileEntityStoneCraftingTable) te;
				
				if (message.slots != null && message.slots.length > 0) {
					EntityPlayer player = (EntityPlayer) ctx.getServerHandler().playerEntity;
					ContainerStoneCraftingTable cont = null;
					
					if (player != null && player.openContainer != null && player.openContainer instanceof ContainerStoneCraftingTable) {

						cont = (ContainerStoneCraftingTable) player.openContainer;

						if (message.buttonHit == 1) cont.clearCraftingGrid();

						else if (message.buttonHit == 2) {
							int max = Integer.MAX_VALUE;
							ItemStack[][] stacks = new ItemStack[9][];
							for (int i = 0; i < stacks.length; i++) {
								stacks[i] = new ItemStack[1];
								stacks[i][0] = message.slots[i];

								if (message.slots[i] != null) max = Math.max(max, message.slots[i].stackSize);
							}

							cont.fillCraftingGrid(stacks, max);
						}
					}
					
					for (int i = 0; i < message.slots.length; i++) {
						te2.setInventorySlotContents(i, message.slots[i]);
					}
					
					if (cont != null) cont.onCraftMatrixChanged(cont.craftMatrix);
					
					te2.markDirty();
				}
			}
		}
		
		else if (ctx.side == Side.CLIENT) {
			TileEntity te = FMLClientHandler.instance().getClient().theWorld.getTileEntity(VectorHelper.toBlockPos(message.vec));
			
			if (te != null && te instanceof TileEntityStoneCraftingTable) {
				TileEntityStoneCraftingTable te2 = (TileEntityStoneCraftingTable) te;
				
				if (message.slots != null && message.slots.length > 0) {
					for (int i = 0; i < message.slots.length; i++) {
						te2.setInventorySlotContents(i, message.slots[i]);
					}
				}
				
				te2.markDirty();
			}
		}
		
		return null;
	}
	
}
