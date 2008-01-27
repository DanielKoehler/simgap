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
 * AbstractGAP.java
 *
 * Created on 16 August 2006, 21.47 by Giovanni Novelli
 *
 ****************************************************************************************
 *
 * $Revision$
 * $Id$
 * $HeadURL$
 *
 *****************************************************************************************
 */
package net.sf.gap;

import org.junit.Assert;

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
public abstract class AbstractGAP {

	/**
	 * Used for no resource ID associated to an agent (its AID isn't alive or
	 * never lived)
	 */
	public static final int NOWHERE = -1;
	/**
	 * Platform's Start Time
	 */
	private static double platformStartTime;
	/**
	 * Simulation's Start Time
	 */
	private static double startTime;
	/**
	 * Simulation's End Time
	 */
	private static double endTime;
	/**
	 * Flag about generating Graphs
	 */
	private static boolean graphing;

	protected static boolean isGraphing() {
		return graphing;
	}

	protected static void setGraphing(boolean aGraphing) {
		graphing = aGraphing;
	}

	/**
	 * Creates a new instance of AbstractGAP
	 */
	public AbstractGAP() {
		AbstractGAP.setGraphing(true);
	}

	/**
	 * Creates a new instance of AbstractGAP indicating preference about
	 * graphing
	 */
	public AbstractGAP(boolean aGraphing) {
		AbstractGAP.setGraphing(aGraphing);
	}

	/**
	 * Static initialization
	 */
	static {
		AbstractGAP.setPlatformStartTime(100.0);
		AbstractGAP.setStartTime(200.0);
		AbstractGAP.setEndTime(500.0);
		Assert.assertFalse(AbstractGAP.getPlatformStartTime() > AbstractGAP
				.getStartTime());
		Assert.assertFalse(AbstractGAP.getStartTime() > AbstractGAP
				.getEndTime());
	}

	protected static void startSimulation() {
		Sim_system.set_report_detail(true, true);
		Sim_system.generate_graphs(AbstractGAP.isGraphing());
		GridSim.startGridSimulation();
	}

	protected static void stopSimulation() {
		GridSim.stopGridSimulation();
	}

	/**
	 * @return true if a AbstractGAP simulation is running
	 */
	public static final boolean isRunning() {
		return (GridSim.clock() < AbstractGAP.getEndTime());
	}

	/**
	 * @return AbstractGAP Simulation start time
	 */
	public static final double getStartTime() {
		return startTime;
	}

	/**
	 * @param aStartTime
	 *            sets AbstractGAP Simulation start time
	 */
	public static final void setStartTime(double aStartTime) {
		startTime = aStartTime;
	}

	/**
	 * @return AbstractGAP Simulation end time
	 */
	public static final double getEndTime() {
		return endTime;
	}

	/**
	 * @param aEndTime
	 *            sets AbstractGAP Simulation end time
	 */
	public static final void setEndTime(double aEndTime) {
		endTime = aEndTime;
	}

	/**
	 * @return AbstractGAP Simulation platform start time
	 */
	public static double getPlatformStartTime() {
		return platformStartTime;
	}

	/**
	 * @param aPlatformStartTime
	 *            sets AbstractGAP Simulation platform start time
	 */
	public static void setPlatformStartTime(double aPlatformStartTime) {
		platformStartTime = aPlatformStartTime;
	}
}
