package com.projectzed.mod.block.generator;

import java.util.Random;

import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import com.projectzed.api.block.AbstractBlockGenerator;
import com.projectzed.api.energy.source.EnumType;
import com.projectzed.mod.ProjectZed;
import com.projectzed.mod.registry.TileEntityRegistry;
import com.projectzed.mod.tileentity.generator.TileEntityNuclear;

import cpw.mods.fml.common.network.internal.FMLNetworkHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * Class containing code for nuclear controller, main hub for multi-block structure and used as main source of 'power generation', particle effects,
 * etc.
 * 
 * @author hockeyhurd
 * @version Nov 24, 2014
 */
public class BlockNuclearController extends AbstractBlockGenerator {

	/** Variable tracking whether to use fusion or fission. */
	private final boolean FUSION_MODE; 
	
	/**
	 * @param material
	 * @param name
	 */
	public BlockNuclearController(Material material, boolean fusion) {
		super(material, "nuclearController");
		this.setBlockName("nuclearController" + (fusion ? "Fusion" : "Fission"));
		this.setCreativeTab(ProjectZed.modCreativeTab);
		this.setHardness(1.0f);
		this.FUSION_MODE = fusion;
	}

	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister reg) {
		blockIcon = reg.registerIcon(ProjectZed.assetDir + "generic_side");
		this.top = this.base = reg.registerIcon(ProjectZed.assetDir + "generic_base");
		this.front = reg.registerIcon(ProjectZed.assetDir + "nuclearController_front");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.projectzed.api.block.AbstractBlockGenerator#createNewTileEntity(net.minecraft.world.World, int)
	 */
	public TileEntity createNewTileEntity(World world, int id) {
		TileEntityNuclear te = new TileEntityNuclear();
		if (this.FUSION_MODE) te.setSource(EnumType.FUSION);
		return te;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.projectzed.api.block.AbstractBlockGenerator#onBlockActivated(net.minecraft.world.World, int, int, int,
	 * net.minecraft.entity.player.EntityPlayer, int, float, float, float)
	 */
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
		if (world.isRemote) return true;

		else {
			TileEntityNuclear te = (TileEntityNuclear) world.getTileEntity(x, y, z);
			if (te != null) FMLNetworkHandler.openGui(player, ProjectZed.instance, TileEntityRegistry.instance().getID(TileEntityNuclear.class), world, x, y, z);
			return true;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.projectzed.api.block.AbstractBlockGenerator#doBreakBlock(net.minecraft.world.World, int, int, int)
	 */
	protected void doBreakBlock(World world, int x, int y, int z) {
		TileEntityNuclear te = (TileEntityNuclear) world.getTileEntity(x, y, z);
		ProjectZed.logHelper.info("Stored:", te.getEnergyStored());
	}

	/**
	 * A randomly called display update to be able to add particles or other items for display
	 */
	@SideOnly(Side.CLIENT)
	public void randomDisplayTick(World world, int x, int y, int z, Random random) {
		// If active.
		// if (this.field_149932_b) {
		int meta = world.getBlockMetadata(x, y, z);
		float xx = (float) x + 0.5F;
		float yy = (float) y + 0.0F + random.nextFloat() * 6.0F / 16.0F;
		float zz = (float) z + 0.5F;
		
		float yOffset = 2f;
		double vel = 0.25d;

		if (meta == 4) {
			world.spawnParticle("smoke", (double) xx, (double) (yy + yOffset), (double) zz, vel, 0.0D, -vel);
			world.spawnParticle("flame", (double) xx, (double) (yy + yOffset), (double) zz, vel, 0.0D, -vel);
		}
		else if (meta == 5) {
			world.spawnParticle("smoke", (double) xx, (double) (yy + yOffset), (double) zz, -vel, 0.0D, vel);
			world.spawnParticle("flame", (double) xx, (double) (yy + yOffset), (double) zz, -vel, 0.0D, vel);
		}
		else if (meta == 2) {
			world.spawnParticle("smoke", (double) xx, (double) (yy + yOffset), (double) zz, vel, 0.0D, vel);
			world.spawnParticle("flame", (double) xx, (double) (yy + yOffset), (double) zz, vel, 0.0D, vel);
		}
		else if (meta == 3) {
			world.spawnParticle("smoke", (double) xx, (double) (yy + yOffset), (double) zz, -vel, 0.0D, -vel);
			world.spawnParticle("flame", (double) xx, (double) (yy + yOffset), (double) zz, -vel, 0.0D, -vel);
		}
		// }
	}

}
