package com.projectzed.mod.container;

import com.projectzed.mod.tileentity.machine.TileEntityIndustrialStorageUnit;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.ItemStack;

import javax.annotation.Nullable;

/**
 * @author hockeyhurd
 * @version 1/22/2017.
 */
public class ContainerStorageUnit extends ContainerMachine {

    public ContainerStorageUnit(InventoryPlayer inventory, TileEntityIndustrialStorageUnit te) {
        super(inventory, te);

        addPlayerInventorySlots(inventory);
    }

    @Nullable
    @Override
    public ItemStack slotClick(int slotId, int dragType, ClickType clickTypeIn, EntityPlayer player) {
        if (slotId == 1) {
            if (te.getStackInSlot(slotId) == null)
                super.slotClick(slotId, dragType, clickTypeIn, player);
            else {
                final TileEntityIndustrialStorageUnit tileEntity = (TileEntityIndustrialStorageUnit) te;

                ItemStack slotStack = tileEntity.getStackInSlot(slotId);

                if (dragType == 0) {
                    tileEntity.getBigItemStack().removeAmount(slotStack.stackSize);
                }

                else if (dragType == 1) {
                    slotStack.stackSize >>>= 1;
                    tileEntity.getBigItemStack().removeAmount(slotStack.stackSize);
                }

                super.slotClick(slotId, dragType, clickTypeIn, player);
            }
        }

        return super.slotClick(slotId, dragType, clickTypeIn, player);
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer player, int index) {
        final ItemStack ret = super.transferStackInSlot(player, index);

        if (index > 1 && ret != null) {
            final TileEntityIndustrialStorageUnit tileEntity = (TileEntityIndustrialStorageUnit) te;

            if (tileEntity.getBigItemStack().getAmount() < ret.getMaxStackSize()) {
                if (!tileEntity.getBigItemStack().isEmpty())
                    tileEntity.getBigItemStack().addAmount(ret.stackSize);
                else
                    tileEntity.getBigItemStack().setItemStack(ret, ret.stackSize);
            }
        }

        return ret;
    }
}
