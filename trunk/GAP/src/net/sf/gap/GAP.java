/*
 ****************************************************************************************
 * Copyright © Giovanni Novelli                                             
 * All Rights Reserved.                                                                 
 ****************************************************************************************
 *
 * Title:        AbstractGAP Simulator
 * Description:  AbstractGAP (Grid Agents Platform) Toolkit for Modeling and Simulation
 *               of Mobile Agents on Grids
 * License:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * GAP.java
 *
 * Created on 19 August 2006, 17.14 by Giovanni Novelli
 *
 * $Id: GAP.java 211 2008-01-24 12:04:25Z gnovelli $
 *
 */

package net.sf.gap;

/**
 * 
 * @author Giovanni Novelli
 */
public abstract class GAP extends AbstractGAP {

	/** Creates a new instance of GAP */
	public GAP() {
		super(true);
	}

	/**
	 * Creates a new instance of GAP indicating preference about graphing
	 */
	public GAP(boolean aGraphing) {
		super(aGraphing);
	}

	/**
	 * Starts simulation
	 */
	public static void startSimulation() {
		System.out.println("Starting simulation");
		AbstractGAP.startSimulation();
	}

	/**
	 * Stops simulation
	 */
	public static void stopSimulation() {
		System.out.println("Stopping simulation");
		AbstractGAP.stopSimulation();
	}
}
