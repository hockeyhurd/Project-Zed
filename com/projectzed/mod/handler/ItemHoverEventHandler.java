package com.projectzed.mod.handler;

import static com.hockeyhurd.api.util.NumberFormatter.format;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;

import com.projectzed.api.block.AbstractBlockContainer;
import com.projectzed.api.block.AbstractBlockGenerator;
import com.projectzed.api.block.AbstractBlockPipe;
import com.projectzed.mod.block.container.AbstractBlockEnergyPipeBase;
import com.projectzed.mod.block.container.BlockEnergyCell;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;

/**
 * Class containing code for all tooltip info related to blocks.
 * 
 * @author hockeyhurd
 * @version Jan 12, 2015
 */
public class ItemHoverEventHandler {

	/** Only static instance of this class. */
	private static final ItemHoverEventHandler HANLDER = new ItemHoverEventHandler();

	private ItemHoverEventHandler() {
	}
	
	/**
	 * @return static instance of this class.
	 */
	public static ItemHoverEventHandler instance() {
		return HANLDER;
	}

	/**
	 * Method used to add relavent information to user tooltips.
	 * 
	 * @param event = event passed.
	 */
	// @SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void onItemHover(ItemTooltipEvent event) {
		ItemStack stack = event.itemStack;
		if (stack != null) {
			int amount = 0;
			int type = 0;
			Block b = Block.getBlockFromItem(stack.getItem());
			if (b != null) {
				if (b instanceof AbstractBlockContainer) {
					type = 0;
					if (b instanceof BlockEnergyCell) event.toolTip.add(EnumChatFormatting.GREEN + "Capacity: " + EnumChatFormatting.WHITE + format(((BlockEnergyCell) b).getTileEntity().getMaxStorage()) + " McU");
					amount = ((AbstractBlockContainer) b).getTileEntity().getMaxExportRate();
				}
				
				else if (b instanceof AbstractBlockPipe) {
					if (b instanceof AbstractBlockEnergyPipeBase) {
						type = 0;
						amount = ((AbstractBlockEnergyPipeBase) b).getTileEntity().getMaxExportRate();
					}

					else {
						type = 2;
						amount = ((AbstractBlockPipe) b).getTileEntity().getMaxExportRate();
					}
				}
				
				else if (b instanceof AbstractBlockGenerator) {
					type = 1;
					amount = ((AbstractBlockGenerator) b).getTileEntity().getSource().getEffectiveSize();
				}
			}
			
			if (amount > 0) {
				String prefix = type == 0 ? "Transfer Rate: " : "Generation Rate: ";
				String suffix = type < 2 ? " McU/t" : "TO_BE_DEFINED";
				event.toolTip.add(EnumChatFormatting.GREEN + prefix + EnumChatFormatting.WHITE + format(amount) + suffix);
			}
		}
	}
	
}
