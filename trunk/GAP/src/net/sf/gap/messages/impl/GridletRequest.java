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
 * GridletRequest.java
 *
 * Created on 20 August 2006, 12.00 by Giovanni Novelli
 *
 ****************************************************************************************
 *
 * $Revision: 1141 $
 * $Id: GridletRequest.java 1141 2007-07-18 18:22:15Z gnovelli $
 * $HeadURL: file:///var/svn/grid/trunk/GAP/src/net/sf/gap/messages/impl/GridletRequest.java $
 *
 *****************************************************************************************
 */


package net.sf.gap.messages.impl;

import eduni.simjava.Sim_event;
import gridsim.Gridlet;
import net.sf.gap.messages.Message;
import net.sf.gap.messages.Request;

/**
 * 
 * @author Giovanni Novelli
 */
public class GridletRequest extends Request {
        /**
         * ID of destination agent that should fulfill the request
         */
	private int dst_agentID;

        /**
         * Resource ID of the Grid Element of the destination agent
         */
	private int dst_resID;

        /**
         * Gridlet encapsulated in the request
         */
	private Gridlet gridlet;

        /**
         * Field used in reply for received gridlet
         */
	private Gridlet receivedGridlet; 

	/**
	 * Creates a new instance of GridletRequest
         * 
         * @param src_ID ID of request submitter
         * @param src_resID Resource ID of Grid Element of request submitter
         * @param dst_agentID ID of destination agent
         * @param dst_resID Resource ID of destination agent
         * @param gridlet Gridlet encapsulated in the request
         * 
         * @see gridsim.Gridlet
         */
	public GridletRequest(
                int src_ID, 
                int src_resID, 
                int dst_agentID,
                int dst_resID, 
                Gridlet gridlet) 
        {
		super(src_ID, src_resID);
		this.setDst_agentID(dst_agentID);
		this.setDst_resID(dst_resID);
		this.setGridlet(gridlet);
	}

	public int getDst_agentID() {
		return this.dst_agentID;
	}

	public void setDst_agentID(int dst_agentID) {
		this.dst_agentID = dst_agentID;
	}

	public int getDst_resID() {
		return this.dst_resID;
	}

	public void setDst_resID(int dst_resID) {
		this.dst_resID = dst_resID;
	}

	public Gridlet getGridlet() {
		return this.gridlet;
	}

	public void setGridlet(Gridlet gridlet) {
		this.gridlet = gridlet;
	}

	@Override
	public GridletRequest clone() {
		return new GridletRequest(
                        this.getSrc_ID(), 
                        this.getSrc_resID(), 
                        this.getDst_agentID(), 
                        this.getDst_resID(), 
                        this.getGridlet());
	}

	public static GridletRequest get_data(Sim_event ev) {
		Message<GridletRequest> message = new Message<GridletRequest>();
		return message.get_data(ev);
	}

	public Gridlet getReceivedGridlet() {
		return receivedGridlet;
	}

	public void setReceivedGridlet(Gridlet receivedGridlet) {
		this.receivedGridlet = receivedGridlet;
	}
}
