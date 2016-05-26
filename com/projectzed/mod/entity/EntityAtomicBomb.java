/* This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under the terms of the GNU General Public 
* License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Project-Zed is 
* distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
* PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along 
* with Project-Zed. If not, see <http://www.gnu.org/licenses/>
*/
package com.projectzed.mod.entity;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;

/**
 * 
 * 
 * @author hockeyhurd
 * @version Mar 18, 2015
 */
public class EntityAtomicBomb extends EntityTNTPrimed {

	/**
	 * @param world
	 */
	public EntityAtomicBomb(World world) {
		super(world);
	}

	/**
	 * @param world
	 * @param x
	 * @param y
	 * @param z
	 * @param entity
	 */
	public EntityAtomicBomb(World world, double x, double y, double z, EntityLivingBase entity) {
		super(world, x, y, z, entity);
	}
	
	/**
     * Called to update the entity's position/logic.
     */
	@Override
	public void onUpdate() {
		this.prevPosX = this.posX;
		this.prevPosY = this.posY;
		this.prevPosZ = this.posZ;
		this.motionY -= 0.03999999910593033D;
		this.moveEntity(this.motionX, this.motionY, this.motionZ);
		this.motionX *= 0.9800000190734863D;
		this.motionY *= 0.9800000190734863D;
		this.motionZ *= 0.9800000190734863D;

		if (this.onGround) {
			this.motionX *= 0.699999988079071D;
			this.motionZ *= 0.699999988079071D;
			this.motionY *= -0.5D;
		}

		int fuse = getFuse();
		if (fuse-- <= 0) {
			setFuse(fuse);
			this.setDead();

			if (!this.worldObj.isRemote) this.explode();
		}

		else this.worldObj.spawnParticle(EnumParticleTypes.SMOKE_LARGE, this.posX, this.posY + 0.5D, this.posZ, 0.0D, 0.0D, 0.0D);
	}
	
	private void explode() {
		float f = 1500.0f;
		// float f = Float.MAX_VALUE;
        this.worldObj.createExplosion(this, this.posX, this.posY, this.posZ, f, true);
	}
	
	/**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
	@Override
    protected void writeEntityToNBT(NBTTagCompound comp) {
        comp.setInteger("AtomicBombFuse", this.getFuse());
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
	@Override
    protected void readEntityFromNBT(NBTTagCompound comp) {
		setFuse(comp.getInteger("AtomicBombFuse"));
    }

}
