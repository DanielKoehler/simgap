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
	private final int MIPS = 1;
	private final int INPUTSIZE = 2;
	private final int OUTPUTSIZE = 3;
	private final int PROCESSINGCOST = 4;
	private final int ACTUALCPUTIME = 5;
	private final int WAITINGTIME = 6;
	private final int WALLCLOCKTIME = 7;

	private final int SUBMITTED = 11;
	private final int CANCELED = 12;
	private final int PAUSED = 13;
	private final int SUCCESSES = 14;
	private final int FAILURES = 15;

	/**
	 * Map of GridletRequest instances received by agent Maps gridlets IDs to
	 * GridletRequests
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
		this.getGridletRequestsMap().put(
				gridletRequest.getGridlet().getGridletID(), gridletRequest);
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
		this.getGridletSubmitted().incMIPS(gridlet.getGridletLength());
		this.getGridletSubmitted().incInputSize(gridlet.getGridletFileSize());
		this.getGridletSubmitted()
				.incOutputSize(gridlet.getGridletOutputSize());
	}

	@SuppressWarnings("unchecked")
	public void addCanceled(Gridlet gridlet) {
		this.getGridletSubmitted().remove(gridlet);
		this.getGridletCanceled().add(gridlet);
		this.getGridletCanceled().incMIPS(gridlet.getGridletLength());
		this.getGridletCanceled().incInputSize(gridlet.getGridletFileSize());
		this.getGridletCanceled().incOutputSize(gridlet.getGridletOutputSize());
		this.getGridletCanceled().incActualCPUTime(gridlet.getActualCPUTime());
		this.getGridletCanceled()
				.incProcessingCost(gridlet.getProcessingCost());
		this.getGridletCanceled().incWaitingTime(gridlet.getWaitingTime());
		this.getGridletCanceled().incWallClockTime(gridlet.getWallClockTime());
	}

	@SuppressWarnings("unchecked")
	public void addPaused(Gridlet gridlet) {
		this.getGridletPaused().add(gridlet);
		this.getGridletPaused().incMIPS(gridlet.getGridletLength());
		this.getGridletPaused().incInputSize(gridlet.getGridletFileSize());
		this.getGridletPaused().incOutputSize(gridlet.getGridletOutputSize());
		this.getGridletPaused().incActualCPUTime(gridlet.getActualCPUTime());
		this.getGridletPaused().incProcessingCost(gridlet.getProcessingCost());
		this.getGridletPaused().incWaitingTime(gridlet.getWaitingTime());
		this.getGridletPaused().incWallClockTime(gridlet.getWallClockTime());
	}

	@SuppressWarnings("unchecked")
	public void removePaused(Gridlet gridlet) {
		this.getGridletPaused().remove(gridlet);
		this.getGridletPaused().decMIPS(gridlet.getGridletLength());
		this.getGridletPaused().decInputSize(gridlet.getGridletFileSize());
		this.getGridletPaused().decOutputSize(gridlet.getGridletOutputSize());
		this.getGridletPaused().decActualCPUTime(gridlet.getActualCPUTime());
		this.getGridletPaused().decProcessingCost(gridlet.getProcessingCost());
		this.getGridletPaused().decWaitingTime(gridlet.getWaitingTime());
		this.getGridletPaused().decWallClockTime(gridlet.getWallClockTime());
	}

	@SuppressWarnings("unchecked")
	public void addSuccesses(Gridlet gridlet) {
		this.getGridletSubmitted().remove(gridlet);
		this.getGridletSuccesses().add(gridlet);
		this.getGridletSuccesses().incMIPS(gridlet.getGridletLength());
		this.getGridletSuccesses().incInputSize(gridlet.getGridletFileSize());
		this.getGridletSuccesses()
				.incOutputSize(gridlet.getGridletOutputSize());
		this.getGridletSuccesses().incActualCPUTime(gridlet.getActualCPUTime());
		this.getGridletSuccesses().incProcessingCost(
				gridlet.getProcessingCost());
		this.getGridletSuccesses().incWaitingTime(gridlet.getWaitingTime());
		this.getGridletSuccesses().incWallClockTime(gridlet.getWallClockTime());
	}

	@SuppressWarnings("unchecked")
	public void addFailures(Gridlet gridlet) {
		this.getGridletSubmitted().remove(gridlet);
		this.getGridletFailures().add(gridlet);
		this.getGridletFailures().incMIPS(gridlet.getGridletLength());
		this.getGridletFailures().incInputSize(gridlet.getGridletFileSize());
		this.getGridletFailures().incOutputSize(gridlet.getGridletOutputSize());
		this.getGridletFailures().incActualCPUTime(gridlet.getActualCPUTime());
		this.getGridletFailures()
				.incProcessingCost(gridlet.getProcessingCost());
		this.getGridletFailures().incWaitingTime(gridlet.getWaitingTime());
		this.getGridletFailures().incWallClockTime(gridlet.getWallClockTime());
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

	private GAPGridletList getList(int list) {
		GAPGridletList result = null;
		switch (list) {
		case SUBMITTED:
			result = this.getGridletSubmitted();
			break;
		case CANCELED:
			result = this.getGridletCanceled();
			break;
		case PAUSED:
			result = this.getGridletCanceled();
			break;
		case SUCCESSES:
			result = this.getGridletSuccesses();
			break;
		case FAILURES:
			result = this.getGridletFailures();
			break;
		default:
			break;
		}
		return result;
	}

	public double getTotal(int total, int list) {
		GAPGridletList theList = this.getList(list);
		double result = 0.0;
		if (theList != null) {
			switch (total) {
			case MIPS:
				result = theList.getMIPS();
				break;
			case INPUTSIZE:
				result = theList.getInputSize();
				break;
			case OUTPUTSIZE:
				result = theList.getOutputSize();
				break;
			case ACTUALCPUTIME:
				result = theList.getActualCPUTime();
				break;
			case PROCESSINGCOST:
				result = theList.getProcessingCost();
				break;
			case WAITINGTIME:
				result = theList.getWaitingTime();
				break;
			case WALLCLOCKTIME:
				result = theList.getWallClockTime();
				break;
			default:
				break;
			}
		}
		return result;
	}
}
