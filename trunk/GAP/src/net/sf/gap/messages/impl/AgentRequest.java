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
 * AgentRequest.java
 *
 * Created on 16 August 2006, 12.00 by Giovanni Novelli
 *
 ****************************************************************************************
 *
 * $Revision: 1141 $
 * $Id: AgentRequest.java 1141 2007-07-18 18:22:15Z gnovelli $
 * $HeadURL: file:///var/svn/grid/trunk/GAP/src/net/sf/gap/messages/impl/AgentRequest.java $
 *
 *****************************************************************************************
 */

package net.sf.gap.messages.impl;

import net.sf.gap.agents.history.AgentHistory;
import eduni.simjava.Sim_event;
import net.sf.gap.messages.Message;
import net.sf.gap.messages.Request;

/**
 * 
 * @author Giovanni Novelli
 */
public class AgentRequest extends Request {
	private AgentHistory src_agentHistory; // and whose agentHistory, if any,

	// is in src_agentHistory

	private int dst_agentID; // Destination agent entity ID

	private int dst_resID; // that is hosted on GridElement with ID dst_resID,

	private int dst_agentSize; // has size in bytes dst_agentSize

	private int dst_moveToresID; // could move to GridElement with ID

	// dst_moveToresID

	private int dst_AID; // and has unique GridAgent IDentity dst_AID

	/**
	 * Creates a new instance of AgentRequest
	 */
	public AgentRequest(int src_ID, int src_resID,
			AgentHistory src_agentHistory, int dst_resID, int dst_agentID,
			int dst_entityType, int dst_agentSize, int dst_moveToresID,
			int dst_AID) {
		super(src_ID, src_resID);
		this.setDst_resID(dst_resID);
		this.setDst_agentID(dst_agentID);
		this.setDst_entityType(dst_entityType);
		this.setDst_agentSize(dst_agentSize);
		this.setDst_moveToresID(dst_moveToresID);
		this.setDst_AID(dst_AID);
		this.setSrc_agentHistory(src_agentHistory);
	}

	@Override
	public AgentRequest clone() {
		return new AgentRequest(this.getSrc_ID(), this.getSrc_resID(), this
				.getSrc_agentHistory(), this.getDst_resID(), this
				.getDst_agentID(), this.getDst_entityType(), this
				.getDst_agentSize(), this.getDst_moveToresID(), this
				.getDst_AID());
	}

	public static AgentRequest get_data(Sim_event ev) {
		Message<AgentRequest> message = new Message<AgentRequest>();
		return message.get_data(ev);
	}

	public int getDst_resID() {
		return this.dst_resID;
	}

	public void setDst_resID(int dst_resID) {
		this.dst_resID = dst_resID;
	}

	public int getDst_agentID() {
		return this.dst_agentID;
	}

	public void setDst_agentID(int dst_agentID) {
		this.dst_agentID = dst_agentID;
	}

	public int getDst_agentSize() {
		return this.dst_agentSize;
	}

	public void setDst_agentSize(int agentSizeInBytes) {
		this.dst_agentSize = agentSizeInBytes;
	}

	public int getDst_moveToresID() {
		return this.dst_moveToresID;
	}

	public void setDst_moveToresID(int moveToResourceID) {
		this.dst_moveToresID = moveToResourceID;
	}

	public boolean isFromUser() {
		return this.getSrc_ID() == this.getSrc_resID();
	}

	public int getDst_AID() {
		return this.dst_AID;
	}

	public void setDst_AID(int AID) {
		this.dst_AID = AID;
	}

	public AgentHistory getSrc_agentHistory() {
		return this.src_agentHistory;
	}

	public void setSrc_agentHistory(AgentHistory agentHistory) {
		this.src_agentHistory = agentHistory;
	}
}
