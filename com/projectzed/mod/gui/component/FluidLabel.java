package com.projectzed.mod.gui.component;

import java.util.ArrayList;
import java.util.List;

import com.hockeyhurd.api.math.Vector4Helper;
import com.projectzed.mod.util.Reference;

/**
 * Class containing code for custom fluid info label.
 * 
 * @author hockeyhurd
 * @version Jan 1, 2015
 */
public class FluidLabel<N> implements IInfoLabel<N> {

	private Vector4Helper<Integer> mouseVec, pos, minMax;
	private N stored, max;
	private List<String> list;
	private boolean visible;
	
	/**
	 * @param pos = x, y coordinate of label boundary.
	 * @param minMax = width, height of label boundary.
	 * @param stored = amount stored at start.
	 * @param max = max stored at start.
	 */
	public FluidLabel(Vector4Helper<Integer> pos, Vector4Helper<Integer> minMax, N stored, N max) {
		this.pos = pos;
		this.minMax = minMax;
		this.stored = stored;
		this.max = max;
		
		this.mouseVec = Vector4Helper.zero;
		this.list = new ArrayList<String>();
	}

	/* (non-Javadoc)
	 * @see com.projectzed.mod.gui.component.IInfoLabel#getLabel()
	 */
	@Override
	public List<String> getLabel() {
		String text0 = "Stored: " + this.stored + " / " + this.max + " " + Reference.Constants.FLUID_UNIT;
		
		if (list.size() == 0) list.add(text0);
		else list.set(0, text0);
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
	public boolean isVisible(boolean ignoreMouse) {
		if (ignoreMouse) return (visible = ignoreMouse);
		else {
			if (mouseVec.x >= pos.x && mouseVec.x <= pos.x + minMax.x && mouseVec.y >= pos.y && mouseVec.y <= pos.y + minMax.y) return (visible = true);
			else return (visible = false);
		}
	}
	
	/* (non-Javadoc)
	 * @see com.projectzed.mod.gui.component.IInfoLabel#update()
	 */
	@Override
	public void update(Vector4Helper<Integer> mouseVec, N stored, N max) {
		this.mouseVec = mouseVec;
		this.stored = stored;
		this.max = max;
	}

}
