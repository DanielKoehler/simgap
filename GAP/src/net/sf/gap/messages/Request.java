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
 * Request.java
 *
 * Created on 29 March 2007, 12.00 by Giovanni Novelli
 *
 ****************************************************************************************
 *
 * $Revision: 1141 $
 * $Id: Request.java 1141 2007-07-18 18:22:15Z gnovelli $
 * $HeadURL: file:///var/svn/grid/trunk/GAP/src/net/sf/gap/messages/Request.java $
 *
 *****************************************************************************************
 */


package net.sf.gap.messages;

import net.sf.gap.constants.EntityTypes;
import eduni.simjava.Sim_event;
import net.sf.gap.messages.Message;
import net.sf.gap.util.EntitiesCounter;

/**
 * 
 * @author Giovanni Novelli
 */
public abstract class Request implements Cloneable {
	private int reqrepID; // Unique request/reply exchange ID

	private int requestID; // Unique request ID

	private int dst_entityType;

	protected int src_ID; // Source entity ID

	protected int src_resID; // that is hosted on Entity with ID src_resID

	/**
	 * Creates a new instance of Request
	 */
	public Request(int src_ID, int src_resID) {
		this.setDst_entityType(EntityTypes.NOBODY);

		this.setSrc_ID(src_ID);
		this.setSrc_resID(src_resID);

		EntitiesCounter.create("REQ");
		Integer ireqID = EntitiesCounter.inc("REQ");
		int reqID = ireqID;
		this.setRequestID(reqID);

		EntitiesCounter.create("REQREP");
		Integer ireqrepID = EntitiesCounter.inc("REQREP");
		int reqrepID = ireqrepID;
		this.setReqrepID(reqrepID);
	}

	public int getDst_entityType() {
		return this.dst_entityType;
	}

	public void setDst_entityType(int entityType) {
		this.dst_entityType = entityType;
	}

	public static Request get_data(Sim_event ev) {
		Message<Request> message = new Message<Request>();
		return message.get_data(ev);
	}

	public int getSrc_ID() {
		return this.src_ID;
	}

	public int getSrc_resID() {
		return this.src_resID;
	}

	public void setSrc_ID(int src_ID) {
		this.src_ID = src_ID;
	}

	public void setSrc_resID(int src_resID) {
		this.src_resID = src_resID;
	}

	public int getRequestID() {
		return requestID;
	}

	public void setRequestID(int requestID) {
		this.requestID = requestID;
	}

	public int getReqrepID() {
		return reqrepID;
	}

	public void setReqrepID(int reqrepID) {
		this.reqrepID = reqrepID;
	}
}
