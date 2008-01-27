/*
 ****************************************************************************************
 * Copyright © Giovanni Novelli                                             
 * All Rights Reserved.                                                                 
 ****************************************************************************************
 *
 * License:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * MachineListType.java
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
public class MachineListType {
	private LinkedList<MachineType> items;
	private int MIPS;

	public MachineListType() {
		this.setItems(new LinkedList<MachineType>());
		this.setMIPS(0);
	}

	public MachineListType(LinkedList<MachineType> items) {
		this.setItems(items);
		for (int i = 0; i < items.size(); i++) {
			this.MIPS += items.get(i).getMIPS();
		}
	}

	public boolean addMachine(MachineType machine) {
		this.MIPS += machine.getMIPS();
		return this.getItems().add(machine);
	}

	public LinkedList<MachineType> getItems() {
		return items;
	}

	public void setItems(LinkedList<MachineType> items) {
		this.items = items;
	}

	public int getMIPS() {
		return MIPS;
	}

	public void setMIPS(int MIPS) {
		this.MIPS = MIPS;
	}
}
