package com.projectzed.mod.block.container;

import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemBucket;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import com.projectzed.api.block.AbstractBlockFluidContainer;
import com.projectzed.api.tileentity.container.AbstractTileEntityFluidContainer;
import com.projectzed.mod.ProjectZed;
import com.projectzed.mod.tileentity.container.TileEntityFluidTank;

/**
 * Class containing code for generic tank container.
 * 
 * @author hockeyhurd
 * @version Jan 7, 2015
 */
public class BlockTank extends AbstractBlockFluidContainer {

	/** Tier of block tank. */
	private final byte TIER;
	
	/**
	 * @param material
	 * @param assetDir
	 * @param name
	 */
	public BlockTank(Material material, String name, byte tier) {
		super(material, ProjectZed.assetDir, name);
		this.TIER = tier;
	}

	/* (non-Javadoc)
	 * @see com.projectzed.api.block.AbstractBlockContainer#getTileEntity()
	 */
	@Override
	public AbstractTileEntityFluidContainer getTileEntity() {
		TileEntityFluidTank te = new TileEntityFluidTank();
		if (this.TIER > 0) te.setTier(this.TIER);
		return te;
	}

	/* (non-Javadoc)
	 * @see com.projectzed.api.block.AbstractBlockContainer#onBlockActivated(net.minecraft.world.World, int, int, int, net.minecraft.entity.player.EntityPlayer, int, float, float, float)
	 */
	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
		if (world.isRemote) return true;
		
		else {
			AbstractTileEntityFluidContainer te = (AbstractTileEntityFluidContainer) world.getTileEntity(x, y, z);
			
			if (te != null && player != null) {
				if (player.getHeldItem() != null && player.getHeldItem().getItem() != null && player.getHeldItem().getItem() instanceof ItemBucket && te.canAddDefaultFluid()) {
					((TileEntityFluidTank) te).setFluidStored(((TileEntityFluidTank) te).getFluidStored() + 1000);
					System.out.println(((TileEntityFluidTank) te).getMaxStorage());
					player.inventory.setInventorySlotContents(player.inventory.currentItem, new ItemStack(Items.bucket, 1));
				}
			}
		}
		
		return true;
	}

	/* (non-Javadoc)
	 * @see com.projectzed.api.block.AbstractBlockContainer#doBreakBlock(net.minecraft.world.World, int, int, int)
	 */
	@Override
	protected void doBreakBlock(World world, int x, int y, int z) {
		TileEntityFluidTank te = (TileEntityFluidTank) world.getTileEntity(x, y, z);
		if (te != null) {
			String fluidName = te.getFluidType() == null ? "<empty>" : te.getFluidType().getUnlocalizedName(); 
			ProjectZed.logHelper.info("Destroyed fluid container w/fluid: " + fluidName + ", stored: " + te.getFluidStored());
		}
	}

}
