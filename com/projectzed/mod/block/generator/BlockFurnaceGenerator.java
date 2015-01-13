package com.projectzed.mod.block.generator;

import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import com.projectzed.api.block.AbstractBlockGenerator;
import com.projectzed.api.tileentity.generator.AbstractTileEntityGenerator;
import com.projectzed.mod.ProjectZed;
import com.projectzed.mod.registry.TileEntityRegistry;
import com.projectzed.mod.tileentity.generator.TileEntityFurnaceGenerator;
import com.projectzed.mod.tileentity.generator.TileEntitySolarArray;
import com.projectzed.mod.util.WorldUtils;

import cpw.mods.fml.common.network.internal.FMLNetworkHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * 
 * @author hockeyhurd
 * @version Nov 18, 2014
 */
public class BlockFurnaceGenerator extends AbstractBlockGenerator {
	
	public BlockFurnaceGenerator(Material material) {
		super(material, "furnaceGen");
		this.setBlockName("furnaceGen");
		this.setCreativeTab(ProjectZed.modCreativeTab);
		this.setHardness(1.0f);
	}

	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister reg) {
		blockIcon = reg.registerIcon(ProjectZed.assetDir + "generic_side");
		this.top = this.base = reg.registerIcon(ProjectZed.assetDir + "generic_base");
		this.front = reg.registerIcon(ProjectZed.assetDir + this.name + "_front");
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.block.AbstractBlockGenerator#getTileEntity()
	 */
	@Override
	public AbstractTileEntityGenerator getTileEntity() {
		return new TileEntityFurnaceGenerator();
	}

	/* (non-Javadoc)
	 * @see com.projectzed.api.block.AbstractBlockGenerator#onBlockActivated(net.minecraft.world.World, int, int, int, net.minecraft.entity.player.EntityPlayer, int, float, float, float)
	 */
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
		if (world.isRemote) return true;

		else {
			TileEntityFurnaceGenerator te = (TileEntityFurnaceGenerator) world.getTileEntity(x, y, z);
			if (te != null) FMLNetworkHandler.openGui(player, ProjectZed.instance, TileEntityRegistry.instance().getID(TileEntitySolarArray.class), world, x, y, z);
			return true;
		}
	}

	/* (non-Javadoc)
	 * @see com.projectzed.api.block.AbstractBlockGenerator#doBreakBlock(net.minecraft.world.World, int, int, int)
	 */
	protected void doBreakBlock(World world, int x, int y, int z) {
		TileEntityFurnaceGenerator te = (TileEntityFurnaceGenerator) world.getTileEntity(x, y, z);
		
		WorldUtils.dropItemsFromContainerOnBreak(te);
		
		ProjectZed.logHelper.info("Stored:", te.getEnergyStored());
	}

}
