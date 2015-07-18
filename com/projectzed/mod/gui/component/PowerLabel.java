/* This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under the terms of the GNU General Public 
* License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Project-Zed is 
* distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
* PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along 
* with Project-Zed. If not, see <http://www.gnu.org/licenses/>
*/
package com.projectzed.mod.gui.component;

import com.hockeyhurd.api.math.Vector2;
import com.projectzed.mod.util.Reference;
import net.minecraft.util.EnumChatFormatting;

import java.util.ArrayList;
import java.util.List;

import static com.hockeyhurd.api.util.NumberFormatter.format;

/**
 * Class containing code for custom power info label.
 * 
 * @author hockeyhurd
 * @version Dec 31, 2014
 */
public class PowerLabel<N> implements IInfoLabel<N> {

	private Vector2<Integer> mouseVec, pos, minMax;
	private boolean useMCU;
	private N stored, lastStored, max;
	private List<String> list;
	private boolean visible;
	
	/**
	 * See constructor below for more info.
	 * @param pos = x, y coordinate of label boundary.
	 * @param minMax = width, height of label boundary.
	 * @param stored = amount stored at start.
	 * @param max = max stored at start.
	 */
	public PowerLabel(Vector2<Integer> pos, Vector2<Integer> minMax, N stored, N max) {
		this(pos, minMax, stored, max, true);
	}
	
	/**
	 * @param pos = x, y coordinate of label boundary.
	 * @param minMax = width, height of label boundary.
	 * @param stored = amount stored at start.
	 * @param max = max stored at start.
	 * @param useMCU = whether to use native energy unit (true), else use RF energy unit (false).
	 */
	public PowerLabel(Vector2<Integer> pos, Vector2<Integer> minMax, N stored, N max, boolean useMCU) {
		this.pos = pos;
		this.minMax = minMax;
		this.stored = lastStored = stored;
		this.max = max;
		this.useMCU = useMCU;
		
		this.mouseVec = Vector2.zero;
		this.list = new ArrayList<String>();
	}

	/* (non-Javadoc)
	 * @see com.projectzed.mod.gui.component.IInfoLabel#getLabel()
	 */
	@Override
	public List<String> getLabel() {
		String text0 =
				EnumChatFormatting.GREEN + "Power: " + EnumChatFormatting.WHITE + format((Number) this.stored) + " / " + format((Number) this.max)
						+ " " + (this.useMCU ? Reference.Constants.ENERGY_UNIT : Reference.Constants.RF_ENERGY_UNIT);

		int lastStored = ((Number) this.lastStored).intValue();
		int stored = ((Number) this.stored).intValue();
		int max = ((Number) this.max).intValue();

		int dif =  lastStored != stored ? stored - lastStored : lastStored - max;
		String text1 = EnumChatFormatting.GREEN + "Usage: " + EnumChatFormatting.WHITE + format((Number) dif) + " McU / t";
		
		float percent = ((Number)(this.stored)).floatValue() / ((Number)(this.max)).floatValue() * 100.0f;
		String text2 = String.format("%.2f%%", percent);
		
		if (list.size() == 0) {
			list.add(text0);
			list.add(text1);
			list.add(text2);
		}
		
		else {
			list.set(0, text0);
			list.set(1, text1);
			list.set(2, text2);
		}
		return list;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.projectzed.mod.gui.component.IInfoLabel#getPercent()
	 */
	@Override
	public float getPercent() {
		return ((Number) this.stored).floatValue() / ((Number) this.max).floatValue() * 100.0f;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.projectzed.mod.gui.component.IInfoLabel#isVisible(int, int, int, int, int, int, boolean)
	 */
	@Override
	public boolean isVisible(boolean ignoreMouse) {
		if (ignoreMouse) return (visible = ignoreMouse);
		else {
			// if (mouseVec.x >= pos.x && mouseVec.x <= pos.x + minMax.x && mouseVec.y >= pos.y && mouseVec.y <= pos.y + minMax.y) return (visible = true);
			if (mouseVec.x >= pos.x && mouseVec.x <= minMax.x && mouseVec.y >= pos.y && mouseVec.y <= minMax.y) return (visible = true);
			else return (visible = false);
		}
	}
	
	/* (non-Javadoc)
	 * @see com.projectzed.mod.gui.component.IInfoLabel#update()
	 */
	@Override
	public void update(Vector2<Integer> mouseVec, Vector2<Integer> pos, Vector2<Integer> minMax, N stored, N max) {
		this.mouseVec = mouseVec;
		this.pos = pos;
		this.minMax = minMax;

		this.lastStored = this.stored;
		this.stored = stored;
		this.max = max;
	}

}
