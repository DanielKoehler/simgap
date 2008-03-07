/*
 ****************************************************************************************
 * Copyright © Giovanni Novelli                                             
 * All Rights Reserved.                                                                 
 ****************************************************************************************
 *
 * Title:        GAP Simulator
 * Description:  GAP (Grid Agents Platform) Toolkit for Modeling and Simulation
 *               of Mobile Agents on Grids
 * License:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * GISEntry.java
 *
 * Created on 25 August 2006, 12.00 by Giovanni Novelli
 *
 ****************************************************************************************
 *
 * $Revision$
 * $Id$
 * $HeadURL$
 *
 *****************************************************************************************
 */

package net.sf.gap.agents.services.impl.gis;

import gridsim.Accumulator;

/**
 * 
 * @author Giovanni Novelli
 */
public class GISEntry {
	private int numPEs;

	private int numFreePEs;

	private int totalMIPS;

	private int numFreeAgents;

	private boolean SE;

	private double MB_size;

	private double load;

	/**
	 * Creates a new instance of GISEntry
	 */
	public GISEntry(int numPEs, int numFreePEs, int numFreeAgents,
			int totalMIPS, boolean SE, double MB_size, double load) {
		this.setNumPEs(numPEs);
		this.setNumFreeAgents(numFreeAgents);
		this.setNumFreePEs(numFreePEs);
		this.setTotalMIPS(totalMIPS);
		this.setSE(SE);
		this.setMB_size(MB_size);
		this.setLoad(load);
	}

	public int getNumPEs() {
		return this.numPEs;
	}

	public void setNumPEs(int numPEs) {
		this.numPEs = numPEs;
	}

	public int getNumFreePEs() {
		return this.numFreePEs;
	}

	public void setNumFreePEs(int numFreePEs) {
		this.numFreePEs = numFreePEs;
	}

	public boolean isSE() {
		return this.SE;
	}

	public void setSE(boolean SE) {
		this.SE = SE;
	}

	public double getMB_size() {
		return this.MB_size;
	}

	public void setMB_size(double MB_size) {
		this.MB_size = MB_size;
	}

	public double getLoad() {
		return this.load;
	}

	public void setLoad(double load) {
		this.load = load;
	}

	public int getNumFreeAgents() {
		return numFreeAgents;
	}

	public void setNumFreeAgents(int numFreeAgents) {
		this.numFreeAgents = numFreeAgents;
	}

	public int getTotalMIPS() {
		return totalMIPS;
	}

	public void setTotalMIPS(int totalMIPS) {
		this.totalMIPS = totalMIPS;
	}

	public String toString() {
		String msg = "";
		msg = msg + "GIS[" + "PEs(" + this.getNumPEs() + ", "
				+ this.getNumFreePEs() + ") " + "AGENTs(" + this.getNumPEs()
				+ ", " + this.getNumFreeAgents() + ") " + "MIPS("
				+ this.getTotalMIPS() + ") " + "LOAD("
				+ this.getLoad() + ") " + "SE(" + this.isSE()
				+ ", " + this.getMB_size() + ")" + "]";

		return msg;
	}
}
