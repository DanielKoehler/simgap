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
 * MC.java
 *
 * Created on 19 August 2006, 17.14 by Giovanni Novelli
 *
 * $Id$
 *
 */

package net.sf.gap.impl;

import net.sf.gap.AbstractGAP;

/**
 * 
 * @author Giovanni Novelli
 */
public abstract class MC extends AbstractGAP {

	/** Creates a new instance of MC */
	public MC() {
		super(true);
	}

	/** 
         * Creates a new instance of MC indicating preference about graphing
         */
	public MC(boolean aGraphing) {
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
