/* This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under the terms of the GNU General Public 
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Project-Zed is 
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along 
 * with Project-Zed. If not, see <http://www.gnu.org/licenses/>
 */
package com.projectzed.mod.block;

import com.hockeyhurd.hcorelib.api.math.Vector3;
import com.hockeyhurd.hcorelib.api.math.VectorHelper;
import com.hockeyhurd.hcorelib.api.util.BlockUtils;
import com.projectzed.api.block.AbstractBlockNuclearComponent;
import com.projectzed.api.block.IMetaUpdate;
import com.projectzed.api.tileentity.container.AbstractTileEntityNuclearComponent;
import com.projectzed.mod.ProjectZed;
import com.projectzed.mod.tileentity.container.TileEntityNuclearChamberWall;
import com.projectzed.mod.util.MultiblockHelper;
import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import static net.minecraft.util.EnumFacing.*;

/**
 * Class containing code for nuclearChamberWall.
 * 
 * @author hockeyhurd
 * @version Dec 12, 2014
 */
public class BlockNuclearChamberWall extends AbstractBlockNuclearComponent implements IMetaUpdate {

	private Block[] blockWhitelist;
	
	public BlockNuclearChamberWall() {
		super("nuclearChamberWall");
	}

	/*@SideOnly(Side.CLIENT)
	@Override
	public void registerBlockIcons(IIconRegister reg) {
		blockIcon = reg.registerIcon(ProjectZed.assetDir + "nuclearChamberWall_normal"); 
		vert = reg.registerIcon(ProjectZed.assetDir + "nuclearChamberWall_vert");
		horiz = reg.registerIcon(ProjectZed.assetDir + "nuclearChamberWall_horiz");
		both = reg.registerIcon(ProjectZed.assetDir + "nuclearChamberWall_both");
	}*/

	@Override
	public void updateMeta(boolean isActive, World world, Vector3<Integer> vec) {
		final BlockPos blockPos = VectorHelper.toBlockPos(vec);
		TileEntity te = world.getTileEntity(blockPos);
		
		if (te != null && te instanceof TileEntityNuclearChamberWall) {
			int ret = isActive ? isBlockAdjacent(world, vec) : 0;
			updateMeta(ret, world, vec);
		}
	}
	
	@Override
	public void updateMeta(int meta, World world, Vector3<Integer> vec) {
		final BlockPos blockPos = VectorHelper.toBlockPos(vec);
		TileEntity te = world.getTileEntity(blockPos);
		
		if (te != null && te instanceof TileEntityNuclearChamberWall) {
			Block currentBlock = ((TileEntityNuclearChamberWall) te).getBlock();
			// world.setBlockMetadataWithNotify(vec.x, vec.y, vec.z, meta, 2);
			// world.markBlockForUpdate(vec.x, vec.y, vec.z);
			BlockUtils.setBlock(world, vec, currentBlock.getStateFromMeta(meta));
			world.notifyBlockOfStateChange(blockPos, currentBlock);
			world.notifyNeighborsOfStateChange(blockPos, currentBlock);
		}
	}

	/**
	 * Function to check and get the approrpriate metadata for this block.
	 * 
	 * @param world world object as reference.
	 * @param vec Vec3i.
	 * @return metadata for given block.
	 */
	private int isBlockAdjacent(World world, Vector3<Integer> vec) {
		if (blockWhitelist == null) blockWhitelist = new Block[] {
					this, ProjectZed.nuclearChamberLock, ProjectZed.nuclearReactorGlass, ProjectZed.nuclearPowerPort, ProjectZed.nuclearControlPort,
					ProjectZed.nuclearIOPort, ProjectZed.fissionController, ProjectZed.fusionController
		};
		
		MultiblockHelper mb = new MultiblockHelper(world, vec.copy(), blockWhitelist);
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

	@Override
	public AbstractTileEntityNuclearComponent getTileEntity() {
		return new TileEntityNuclearChamberWall();
	}

}
