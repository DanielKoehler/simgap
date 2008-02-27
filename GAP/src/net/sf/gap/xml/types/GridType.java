/*
 ****************************************************************************************
 * Copyright © Giovanni Novelli                                             
 * All Rights Reserved.                                                                 
 ****************************************************************************************
 *
 * License:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * GridType.java
 *
 * Created on 22 January 2008, 09.00 by Giovanni Novelli
 *
 * $Id$
 *
 */

package net.sf.gap.xml.types;

import java.util.LinkedList;

/**
 * 
 * @author Giovanni Novelli
 */
public class GridType {
        private int MIPS;
	private LinkedList<GridElementType> gridElements;

	public GridType() {
		this.setGridElements(new LinkedList<GridElementType>());
                this.setMIPS(0);
	}

	public int size() {
		return this.getGridElements().size();
	}

	public boolean addGridElement(GridElementType gridElement) {
		boolean result = this.getGridElements().add(gridElement);
                this.MIPS+=gridElement.getMIPS();
                return result;
	}

	public LinkedList<GridElementType> getGridElements() {
		return gridElements;
	}

	public void setGridElements(LinkedList<GridElementType> gridElements) {
		this.gridElements = gridElements;
	}

    public int getMIPS() {
        return MIPS;
    }

    public void setMIPS(int MIPS) {
        this.MIPS = MIPS;
    }

}
