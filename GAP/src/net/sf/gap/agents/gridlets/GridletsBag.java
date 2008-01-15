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
 * GridletsBag.java
 *
 * Created on 11 March 2007, 12.00 by Giovanni Novelli
 *
 ****************************************************************************************
 *
 * $Revision$
 * $Id$
 * $HeadURL$
 *
 *****************************************************************************************
 */

package net.sf.gap.agents.gridlets;

import gridsim.Gridlet;

import net.sf.gap.messages.impl.GridletRequest;

/**
 * @TODO Fix GridletsBag lists as queues and vectors when needed
 * @author Giovanni Novelli
 */
public class GridletsBag {
        /**
         * Map of GridletRequest instances received by agent
         * Maps gridlets IDs to GridletRequests
         */
	private GridletRequestsMap gridletRequestsMap; 

        /**
         * List of gridlets submitted to GE/GEs
         */
	private GAPGridletList gridletSubmitted; 

        /**
         * List of canceled gridlets
         */
	private GAPGridletList gridletCanceled; 

        /**
         * List of paused gridlets
         */
	private GAPGridletList gridletPaused; 

        /**
         * List of successful gridlets
         */
	private GAPGridletList gridletSuccesses;

        /**
         * List of failed gridlets
         */
	private GAPGridletList gridletFailures; 

        /**
	 * Creates a new instance of GridletsBag
	 */
	public GridletsBag() {
		this.gridletRequestsMap = new GridletRequestsMap();
		this.gridletSubmitted = new GAPGridletList();
		this.gridletCanceled = new GAPGridletList();
		this.gridletPaused = new GAPGridletList();
		this.gridletSuccesses = new GAPGridletList();
		this.gridletFailures = new GAPGridletList();
	}

	public void addRequest(GridletRequest gridletRequest) {
		this.getGridletRequestsMap().put(gridletRequest.getGridlet().getGridletID(),
				gridletRequest);
	}
        
        private GridletRequest getRequest(int gridletID) {
            return this.getGridletRequestsMap().get(gridletID);
        }
        
        public Gridlet getGridlet(int gridletID) {
            return this.getRequest(gridletID).getGridlet();
        }

	@SuppressWarnings("unchecked")
	public void addSubmitted(Gridlet gridlet) {
		this.getGridletSubmitted().add(gridlet);
	}

	@SuppressWarnings("unchecked")
	public void addCanceled(Gridlet gridlet) {
		this.getGridletSubmitted().remove(gridlet);
		this.getGridletCanceled().add(gridlet);
	}

	@SuppressWarnings("unchecked")
	public void addPaused(Gridlet gridlet) {
		this.getGridletPaused().add(gridlet);
	}

	@SuppressWarnings("unchecked")
	public void addSuccesses(Gridlet gridlet) {
		this.getGridletSubmitted().remove(gridlet);
		this.getGridletSuccesses().add(gridlet);
	}

	@SuppressWarnings("unchecked")
	public void addFailures(Gridlet gridlet) {
		this.getGridletSubmitted().remove(gridlet);
		this.getGridletFailures().add(gridlet);
	}

	public GridletRequestsMap getGridletRequestsMap() {
		return this.gridletRequestsMap;
	}

	public GAPGridletList getGridletSuccesses() {
		return gridletSuccesses;
	}

	public GAPGridletList getGridletFailures() {
		return gridletFailures;
	}

	public GAPGridletList getGridletSubmitted() {
		return gridletSubmitted;
	}

	public GAPGridletList getGridletCanceled() {
		return gridletCanceled;
	}

        public GAPGridletList getGridletPaused() {
		return gridletPaused;
	}
}
