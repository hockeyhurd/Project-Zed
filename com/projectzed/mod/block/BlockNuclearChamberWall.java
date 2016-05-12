/* This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under the terms of the GNU General Public 
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Project-Zed is 
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along 
 * with Project-Zed. If not, see <http://www.gnu.org/licenses/>
 */
package com.projectzed.mod.block;

import com.hockeyhurd.api.math.Vector3;
import com.hockeyhurd.api.math.Vector4;
import com.projectzed.api.block.AbstractBlockNuclearComponent;
import com.projectzed.api.block.IMetaUpdate;
import com.projectzed.api.tileentity.container.AbstractTileEntityNuclearComponent;
import com.projectzed.mod.ProjectZed;
import com.projectzed.mod.tileentity.container.TileEntityNuclearChamberWall;
import com.projectzed.mod.util.MultiblockHelper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

import static net.minecraftforge.common.util.ForgeDirection.*;

/**
 * Class containing code for nuclearChamberWall.
 * 
 * @author hockeyhurd
 * @version Dec 12, 2014
 */
public class BlockNuclearChamberWall extends AbstractBlockNuclearComponent implements IMetaUpdate {

	private Block[] blockWhitelist;
	
	@SideOnly(Side.CLIENT)
	private IIcon vert, horiz, both;

	public BlockNuclearChamberWall() {
		super("nuclearChamberWall");
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * net.minecraft.block.Block#registerBlockIcons(net.minecraft.client.renderer
	 * .texture.IIconRegister)
	 */
	@SideOnly(Side.CLIENT)
	@Override
	public void registerBlockIcons(IIconRegister reg) {
		blockIcon = reg.registerIcon(ProjectZed.assetDir + "nuclearChamberWall_normal"); 
		vert = reg.registerIcon(ProjectZed.assetDir + "nuclearChamberWall_vert");
		horiz = reg.registerIcon(ProjectZed.assetDir + "nuclearChamberWall_horiz");
		both = reg.registerIcon(ProjectZed.assetDir + "nuclearChamberWall_both");
	}

	/*
	 * (non-Javadoc)
	 * @see net.minecraft.block.Block#getIcon(int, int)
	 */
	@SideOnly(Side.CLIENT)
	@Override
	public IIcon getIcon(int side, int meta) {
		// if (side == 0 && meta == 0) return blockIcon;
		// return meta == 0 ? blockIcon : (meta == 1 ? vert : (meta == 2 ? horiz : both));
		
		if (meta == 0) return blockIcon;
		else if (meta == 1) return vert;
		else if (meta == 2) return horiz;
		else if (meta == 3) return both;
		else if (meta == 4) {
			if (side != 0 && side != 1) return horiz;
			else return vert;
		}
		else return blockIcon;
	}

	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.block.IMetaUpdate#updateMeta(boolean, net.minecraft.world.World, com.hockeyhurd.api.math.Vector4)
	 */
	@Override
	public void updateMeta(boolean isActive, World world, Vector3<Integer> vec) {
		TileEntity te = world.getTileEntity(vec.x, vec.y, vec.z);
		
		if (te != null && te instanceof TileEntityNuclearChamberWall) {
			int ret = isActive ? isBlockAdjacent(world, vec.x, vec.y, vec.z) : 0;
			updateMeta(ret, world, vec);
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.block.IMetaUpdate#updateMeta(int, net.minecraft.world.World, com.hockeyhurd.api.math.Vector4)
	 */
	@Override
	public void updateMeta(int meta, World world, Vector3<Integer> vec) {
		TileEntity te = world.getTileEntity(vec.x, vec.y, vec.z);
		
		if (te != null && te instanceof TileEntityNuclearChamberWall) {
			world.setBlockMetadataWithNotify(vec.x, vec.y, vec.z, meta, 2);
			world.markBlockForUpdate(vec.x, vec.y, vec.z);
		}
	}

	/**
	 * Function to check and get the approrpriate metadata for this block.
	 * 
	 * @param world world object as reference.
	 * @param x x-pos
	 * @param y y-pos
	 * @param z z-pos
	 * @return metadata for given block.
	 */
	private int isBlockAdjacent(World world, int x, int y, int z) {
		if (blockWhitelist == null) blockWhitelist = new Block[] {
					this, ProjectZed.nuclearChamberLock, ProjectZed.nuclearReactorGlass, ProjectZed.nuclearPowerPort, ProjectZed.nuclearControlPort,
					ProjectZed.nuclearIOPort, ProjectZed.fissionController, ProjectZed.fusionController
		};
		
		MultiblockHelper mb = new MultiblockHelper(world, new Vector4<Integer>(x, y, z), blockWhitelist);
		mb.calculateConnections();

		int counter = mb.getCounter();
		// ProjectZed.logHelper.info("Counter:\t", counter);
		int ret = 0;
		boolean[] connections = new boolean[mb.getConnections().length];
		
		for (int i = 0; i < connections.length; i++) {
			connections[i] = mb.getConnections()[i];
		}
		
		/* ProjectZed.logHelper.info(connections[DOWN.ordinal()], connections[UP.ordinal()], connections[WEST.ordinal()], connections[EAST.ordinal()],
				connections[NORTH.ordinal()], connections[SOUTH.ordinal()]);*/
		
		if (!connections[DOWN.ordinal()] && connections[UP.ordinal()] && connections[WEST.ordinal()] && connections[EAST.ordinal()]
				&& connections[NORTH.ordinal()] && !connections[SOUTH.ordinal()]) {
			ret = 2;
			// ProjectZed.logHelper.info("case 1");
		}
		
		else if (connections[DOWN.ordinal()] && !connections[UP.ordinal()] && connections[WEST.ordinal()] && connections[EAST.ordinal()]
				&& connections[NORTH.ordinal()] && !connections[SOUTH.ordinal()]) {
			ret = 2;
			// ProjectZed.logHelper.info("case 2");
		}
		
		else if (connections[DOWN.ordinal()] && connections[UP.ordinal()] && connections[WEST.ordinal()] && connections[EAST.ordinal()]
				&& !connections[NORTH.ordinal()] && !connections[SOUTH.ordinal()]) {
			ret = 3;
			// ProjectZed.logHelper.info("case 3");
		}
		
		else if (!connections[DOWN.ordinal()] && connections[UP.ordinal()] && connections[WEST.ordinal()] && !connections[EAST.ordinal()]
				&& connections[NORTH.ordinal()] && connections[SOUTH.ordinal()]) {
			ret = 4;
			// ProjectZed.logHelper.info("case 4");
		}
		
		else if (connections[DOWN.ordinal()] && !connections[UP.ordinal()] && connections[WEST.ordinal()] && !connections[EAST.ordinal()]
				&& connections[NORTH.ordinal()] && connections[SOUTH.ordinal()]) {
			ret = 4;
			// ProjectZed.logHelper.info("case 5");
		}
		
		else if (!connections[DOWN.ordinal()] && connections[UP.ordinal()] && connections[WEST.ordinal()] && connections[EAST.ordinal()]
				&& !connections[NORTH.ordinal()] && connections[SOUTH.ordinal()]) {
			ret = 2;
			// ProjectZed.logHelper.info("case 6");
		}
		
		else if (connections[DOWN.ordinal()] && !connections[UP.ordinal()] && connections[WEST.ordinal()] && connections[EAST.ordinal()]
				&& !connections[NORTH.ordinal()] && connections[SOUTH.ordinal()]) {
			ret = 2;
			// ProjectZed.logHelper.info("case 7");
		}
		
		else if (connections[DOWN.ordinal()] && !connections[UP.ordinal()] && !connections[WEST.ordinal()] && connections[EAST.ordinal()]
				&& connections[NORTH.ordinal()] && connections[SOUTH.ordinal()]) {
			ret = 4;
			// ProjectZed.logHelper.info("case 8");
		}
		
		else if (!connections[DOWN.ordinal()] && connections[UP.ordinal()] && !connections[WEST.ordinal()] && connections[EAST.ordinal()]
				&& connections[NORTH.ordinal()] && connections[SOUTH.ordinal()]) {
			ret = 4;
			// ProjectZed.logHelper.info("case 9");
		}
		
		else if (!connections[DOWN.ordinal()] && !connections[UP.ordinal()] && connections[WEST.ordinal()] && connections[EAST.ordinal()]
				&& connections[NORTH.ordinal()] && connections[SOUTH.ordinal()]) {
			ret = 3;
			// ProjectZed.logHelper.info("case 10");
		}
		
		else if (connections[DOWN.ordinal()] && connections[UP.ordinal()] && !connections[WEST.ordinal()] && !connections[EAST.ordinal()]
				&& connections[NORTH.ordinal()] && connections[SOUTH.ordinal()]) {
			ret = 3;
			// ProjectZed.logHelper.info("case 11");
		}
		
		// else if (counter > 3) ret = 1;
		else ret = 1;
		
		return ret;
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * com.projectzed.api.block.AbstractBlockNuclearComponent#getTileEntity()
	 */
	@Override
	public AbstractTileEntityNuclearComponent getTileEntity() {
		return new TileEntityNuclearChamberWall();
	}

}
