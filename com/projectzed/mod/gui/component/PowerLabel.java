/* This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under the terms of the GNU General Public 
* License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Project-Zed is 
* distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
* PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along 
* with Project-Zed. If not, see <http://www.gnu.org/licenses/>
*/
package com.projectzed.mod.gui.component;

import com.hockeyhurd.hcorelib.api.math.Vector2;
import com.projectzed.mod.util.Reference;
import net.minecraft.util.text.TextFormatting;

import java.util.ArrayList;
import java.util.List;

import static com.hockeyhurd.hcorelib.api.util.NumberFormatter.format;

/**
 * Class containing code for custom power info label.
 * 
 * @author hockeyhurd
 * @version Dec 31, 2014
 */
public class PowerLabel<N> implements IInfoLabel<N> {

	private Vector2<Integer> mouseVec, pos, minMax;
	private boolean useMCU;
	private N stored, lastStored, max, burnGenRate;
	private boolean visible;
	private boolean hasUsage;
	private List<String> list;

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

	@Override
	public List<String> getLabel() {
		String text0 =
				TextFormatting.GREEN + "Power: " + TextFormatting.WHITE + format((Number) this.stored) + " / " + format((Number) this.max)
						+ " " + (this.useMCU ? Reference.Constants.ENERGY_UNIT : Reference.Constants.RF_ENERGY_UNIT);


		String text1 = "";

		if (hasUsage) {
			int lastStored = ((Number) this.lastStored).intValue();
			int stored = ((Number) this.stored).intValue();
			int max = ((Number) this.max).intValue();

			// int dif = lastStored != stored ? stored - lastStored : lastStored - max;
			int dif = stored - lastStored;

			boolean neg = dif < 0;

			int burnGenRate = Math.round(((Number) this.burnGenRate).floatValue());

			if (neg) dif = Math.min(Math.abs(dif), burnGenRate);
			else if (dif != 0) dif = Math.max(Math.abs(dif), burnGenRate);

			if (neg /*&& dif < 0*/) dif = -dif;

			text1 = TextFormatting.GREEN + "Usage: " + TextFormatting.WHITE + format(dif) + " McU / t";
		}

		float percent = ((Number)(this.stored)).floatValue() / ((Number)(this.max)).floatValue() * 100.0f;
		String text2 = String.format("%.2f%%", percent);
		
		if (list.size() == 0) {
			list.add(text0);
			if (hasUsage) list.add(text1);
			list.add(text2);
		}
		
		else {
			list.set(0, text0);
			// if (hasUsage) list.set(1, text1);
			// list.set(hasUsage ? 2 : 1, text2);
			if (hasUsage) {
				list.set(1, text1);

				if (list.size() > 2) list.set(2, text2);
				else list.add(text2);
			}

			else list.set(1, text2);
		}
		return list;
	}
	
	@Override
	public float getPercent() {
		return ((Number) this.stored).floatValue() / ((Number) this.max).floatValue() * 100.0f;
	}
	
	@Override
	public boolean isVisible(boolean ignoreMouse) {
		if (ignoreMouse) return (visible = ignoreMouse);
		else {
			// if (mouseVec.x >= pos.x && mouseVec.x <= pos.x + minMax.x && mouseVec.y >= pos.y && mouseVec.y <= pos.y + minMax.y) return (visible = true);
			if (mouseVec.x >= pos.x && mouseVec.x <= minMax.x && mouseVec.y >= pos.y && mouseVec.y <= minMax.y) return (visible = true);
			else return (visible = false);
		}
	}
	
	@Override
	public void update(Vector2<Integer> mouseVec, Vector2<Integer> pos, Vector2<Integer> minMax, N[] data) {
		this.mouseVec = mouseVec;
		this.pos = pos;
		this.minMax = minMax;

		this.lastStored = this.stored;
		this.stored = data[0];
		this.max = data[1];
		this.burnGenRate = data.length >= 3 ? data[2] : (N) (Number) 0;
		this.hasUsage = ((Number) this.burnGenRate).floatValue() != 0f;
	}

}
