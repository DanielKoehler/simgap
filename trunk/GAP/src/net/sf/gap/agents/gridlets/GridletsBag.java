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
import net.sf.gap.messages.impl.AgentRequest;

/**
 * @TODO Fix GridletsBag lists as queues and vectors when needed
 * @author Giovanni Novelli
 */
public class GridletsBag {
	private GridletsMap mapGR; // gridlets IDs --> GridletRequests

	private GridletList gridletSubmitted; // GridletsBag submitted

	private GridletList gridletCanceled; // GridletsBag canceled

	private GridletList gridletPaused; // GridletsBag paused

	private GridletList gridletSuccesses; // GridletsBag successfull

	private GridletList gridletFailures; // GridletsBag failed

	private boolean waitingGridlets; // Flag for waiting gridlets ending

	// before KILL_AGENT

	private AgentRequest waitingAgentRequest; // GridAgent Request for

	// KILL_AGENT waiting gridlets
	// ending

	/**
	 * Creates a new instance of GridletsBag
	 */
	public GridletsBag() {
		this.setMapGR(new GridletsMap());
		this.setGridletSubmitted(new GridletList());
		this.setGridletCanceled(new GridletList());
		this.setGridletPaused(new GridletList());
		this.setGridletSuccesses(new GridletList());
		this.setGridletFailures(new GridletList());

		this.setWaitingGridlets(false);
		this.setWaitingAgentRequest(null);
	}

	public boolean hasGridlets() {
		return (this.getGridletSubmitted().size() > 0);
	}

	public void addRequest(GridletRequest gridletRequest) {
		this.getMapGR().put(gridletRequest.getGridlet().getGridletID(),
				gridletRequest);
	}
        
        private GridletRequest getRequest(int gridletID) {
            return this.getMapGR().get(gridletID);
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

	public GridletsMap getMapGR() {
		return this.mapGR;
	}

	public void setMapGR(GridletsMap mapGridletRequests) {
		this.mapGR = mapGridletRequests;
	}

	public GridletList getGridletSuccesses() {
		return gridletSuccesses;
	}

	public void setGridletSuccesses(GridletList gridletSuccesses) {
		this.gridletSuccesses = gridletSuccesses;
	}

	public GridletList getGridletFailures() {
		return gridletFailures;
	}

	public void setGridletFailures(GridletList gridletFailures) {
		this.gridletFailures = gridletFailures;
	}

	public GridletList getGridletSubmitted() {
		return gridletSubmitted;
	}

	public void setGridletSubmitted(GridletList gridletSubmitted) {
		this.gridletSubmitted = gridletSubmitted;
	}

	public GridletList getGridletCanceled() {
		return gridletCanceled;
	}

	public void setGridletCanceled(GridletList gridletCanceled) {
		this.gridletCanceled = gridletCanceled;
	}

	public GridletList getGridletPaused() {
		return gridletPaused;
	}

	public void setGridletPaused(GridletList gridletPaused) {
		this.gridletPaused = gridletPaused;
	}

	public boolean isWaitingGridlets() {
		return waitingGridlets;
	}

	public void setWaitingGridlets(boolean waitingGridlets) {
		this.waitingGridlets = waitingGridlets;
	}

	public AgentRequest getWaitingAgentRequest() {
		return waitingAgentRequest;
	}

	public void setWaitingAgentRequest(AgentRequest waitingAgentRequest) {
		this.waitingAgentRequest = waitingAgentRequest;
	}

	public void offWaitingGridlets() {
		this.setWaitingGridlets(false);
	}

	public void onWaitingGridlets(AgentRequest agentRequest) {
		this.setWaitingGridlets(true);
		this.setWaitingAgentRequest(agentRequest);
	}
}
