package com.projectzed.mod.block.container;

import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

import com.projectzed.api.block.AbstractBlockContainer;
import com.projectzed.api.tileentity.container.AbstractTileEntityEnergyContainer;
import com.projectzed.mod.ProjectZed;
import com.projectzed.mod.registry.TileEntityRegistry;
import com.projectzed.mod.tileentity.machine.TileEntityRFBridge;

import cpw.mods.fml.common.network.internal.FMLNetworkHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * Class containing block code for RF bridge.
 * 
 * @author hockeyhurd
 * @version Nov 29, 2014
 */
public class BlockRFBridge extends AbstractBlockContainer {

	private boolean flip;

	public BlockRFBridge(Material material, boolean flip) {
		super(material, ProjectZed.assetDir, "bridgeMcUToRF");
		this.flip = flip;
		this.setBlockName(!flip ? "bridgeMcUToRF" : "bridgeRFToMcU");
	}

	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister reg) {
		blockIcon = reg.registerIcon(ProjectZed.assetDir + (!flip ? "bridgeMcUToRF" : "bridgeRFToMcU"));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.projectzed.api.block.AbstractBlockContainer#getTileEntity()
	 */
	protected AbstractTileEntityEnergyContainer getTileEntity() {
		TileEntityRFBridge te = new TileEntityRFBridge();
		te.setFlip(this.flip);
		return te;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.projectzed.api.block.AbstractBlockContainer#doBreakBlock(net.minecraft.world.World, int, int, int)
	 */
	protected void doBreakBlock(World world, int x, int y, int z) {
		TileEntityRFBridge te = (TileEntityRFBridge) world.getTileEntity(x, y, z);
		ProjectZed.logHelper.info("Stored:", te.getEnergyStored());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.projectzed.api.block.AbstractBlockContainer#onBlockActivated(net.minecraft.world.World, int, int, int,
	 * net.minecraft.entity.player.EntityPlayer, int, float, float, float)
	 */
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
		if (world.isRemote) return true;

		else {
			TileEntityRFBridge te = (TileEntityRFBridge) world.getTileEntity(x, y, z);
			if (te != null) FMLNetworkHandler.openGui(player, ProjectZed.instance, TileEntityRegistry.instance().getID(TileEntityRFBridge.class), world, x, y, z);
			return true;
		}
	}

}
