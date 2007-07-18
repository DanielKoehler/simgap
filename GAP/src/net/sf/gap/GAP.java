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
 * GAP.java
 *
 * Created on 16 August 2006, 21.47 by Giovanni Novelli
 *
 ****************************************************************************************
 *
 * $Revision: 1141 $
 * $Id: GAP.java 1141 2007-07-18 18:22:15Z gnovelli $
 * $HeadURL: file:///var/svn/grid/trunk/GAP/src/net/sf/gap/GAP.java $
 *
 *****************************************************************************************
 */

package net.sf.gap;

import eduni.simjava.Sim_system;

import gridsim.GridSim;

/**
 * This class is mainly responsible in initialization, running and stopping of
 * the overall simulation, adding a simple unifying abstraction for such actions
 * above of GridSim and SimJava2.
 * 
 * @author Giovanni Novelli
 * @see gridsim.GridSim
 *
 */
public abstract class GAP {
	public static final int NOWHERE = -1;

	/**
	 * Used for no resource ID associated to an agent (its AID isn't alive or
	 * never lived)
	 */

	private static double platformStartTime;

	private static double startTime;

	private static double endTime;

	/**
	 * Creates a new instance of GAP
	 */
	public GAP() {
	}

	/**
	 * Static initialization
	 */
	static {
		GAP.setPlatformStartTime(100.0);
		GAP.setStartTime(200.0);
		GAP.setEndTime(500.0);
	}

	protected static void startSimulation() {
                Sim_system.generate_graphs(true);
                GridSim.startGridSimulation();
	}

	protected static void stopSimulation() {
		GridSim.stopGridSimulation();
	}

	/**
	 * @return true if a GAP simulation is running
	 */
	public static final boolean isRunning() {
		return (GridSim.clock() < GAP.getEndTime());
	}

	/**
	 * @return GAP Simulation start time
	 */
	public static final double getStartTime() {
		return startTime;
	}

	/**
	 * @param aStartTime
	 *            sets GAP Simulation start time
	 */
	public static final void setStartTime(double aStartTime) {
		startTime = aStartTime;
	}

	/**
	 * @return GAP Simulation end time
	 */
	public static final double getEndTime() {
		return endTime;
	}

	/**
	 * @param aEndTime
	 *            sets GAP Simulation end time
	 */
	public static final void setEndTime(double aEndTime) {
		endTime = aEndTime;
	}

	/**
	 * @return GAP Simulation platform start time
	 */
	public static double getPlatformStartTime() {
		return platformStartTime;
	}

	/**
	 * @param aPlatformStartTime
	 *            sets GAP Simulation platform start time
	 */
	public static void setPlatformStartTime(double aPlatformStartTime) {
		platformStartTime = aPlatformStartTime;
	}
}
