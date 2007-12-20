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
 * $Revision: 1141 $
 * $Id: GridletsBag.java 1141 2007-07-18 18:22:15Z gnovelli $
 * $HeadURL: file:///var/svn/grid/trunk/GAP/src/net/sf/gap/agents/gridlets/GridletsBag.java $
 *
 *****************************************************************************************
 */

package net.sf.gap.agents.gridlets;

import gridsim.Gridlet;
import gridsim.GridletList;
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
	private GridletList gridletSubmitted; 

        /**
         * List of canceled gridlets
         */
	private GridletList gridletCanceled; 

        /**
         * List of paused gridlets
         */
	private GridletList gridletPaused; 

        /**
         * List of successful gridlets
         */
	private GridletList gridletSuccesses;

        /**
         * List of failed gridlets
         */
	private GridletList gridletFailures; 

        /**
	 * Creates a new instance of GridletsBag
	 */
	public GridletsBag() {
		this.gridletRequestsMap = new GridletRequestsMap();
		this.gridletSubmitted = new GridletList();
		this.gridletCanceled = new GridletList();
		this.gridletPaused = new GridletList();
		this.gridletSuccesses = new GridletList();
		this.gridletFailures = new GridletList();
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

	public GridletList getGridletSuccesses() {
		return gridletSuccesses;
	}

	public GridletList getGridletFailures() {
		return gridletFailures;
	}

	public GridletList getGridletSubmitted() {
		return gridletSubmitted;
	}

	public GridletList getGridletCanceled() {
		return gridletCanceled;
	}

        public GridletList getGridletPaused() {
		return gridletPaused;
	}
}
