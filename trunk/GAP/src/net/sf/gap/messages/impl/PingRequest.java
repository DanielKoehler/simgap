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
 * PingRequest.java
 *
 * Created on 26 August 2006, 12.00 by Giovanni Novelli
 *
 ****************************************************************************************
 *
 * $Revision: 1141 $
 * $Id: PingRequest.java 1141 2007-07-18 18:22:15Z gnovelli $
 * $HeadURL: file:///var/svn/grid/trunk/GAP/src/net/sf/gap/messages/impl/PingRequest.java $
 *
 *****************************************************************************************
 */


package net.sf.gap.messages.impl;

import eduni.simjava.Sim_event;
import gridsim.net.InfoPacket;
import net.sf.gap.messages.Message;
import net.sf.gap.messages.Request;

/**
 * 
 * @author Giovanni Novelli
 */
public class PingRequest extends Request {
	private int ping_src_ID;

	private int ping_dst_ID;

	private InfoPacket pkt; // Used in reply

	/**
	 * Creates a new instance of PingRequest
	 */
	public PingRequest(int src_ID, int src_resID, int ping_src_ID,
			int ping_dst_ID) {
		super(src_ID, src_resID);
		this.setPing_src_ID(ping_src_ID);
		this.setPing_dst_ID(ping_dst_ID);
	}

	public static PingRequest get_data(Sim_event ev) {
		Message<PingRequest> message = new Message<PingRequest>();
		return message.get_data(ev);
	}

	@Override
	public PingRequest clone() {
		return new PingRequest(this.getSrc_ID(), this.getSrc_resID(), this
				.getPing_src_ID(), this.getPing_dst_ID());
	}

	public int getPing_src_ID() {
		return this.ping_src_ID;
	}

	public void setPing_src_ID(int ping_src_ID) {
		this.ping_src_ID = ping_src_ID;
	}

	public int getPing_dst_ID() {
		return this.ping_dst_ID;
	}

	public void setPing_dst_ID(int ping_dst_ID) {
		this.ping_dst_ID = ping_dst_ID;
	}

	public InfoPacket getPkt() {
		return pkt;
	}

	public void setPkt(InfoPacket pkt) {
		this.pkt = pkt;
	}
}
