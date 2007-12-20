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
 * PingReply.java
 *
 * Created on 26 August 2006, 12.00 by Giovanni Novelli
 *
 ****************************************************************************************
 *
 * $Revision: 1141 $
 * $Id: PingReply.java 1141 2007-07-18 18:22:15Z gnovelli $
 * $HeadURL: file:///var/svn/grid/trunk/GAP/src/net/sf/gap/messages/impl/PingReply.java $
 *
 *****************************************************************************************
 */


package net.sf.gap.messages.impl;

import eduni.simjava.Sim_event;
import gridsim.net.InfoPacket;
import net.sf.gap.messages.Message;
import net.sf.gap.messages.Reply;

/**
 * 
 * @author Giovanni Novelli
 */
public class PingReply extends Reply {

	/**
	 * Creates a new instance of PingReply
	 */
	public PingReply(int requestTAG, boolean ok, PingRequest request,
			InfoPacket pkt) {
		super(requestTAG, ok, request);
		this.setPkt(pkt);
	}

	@Override
	public PingReply clone() {
		return new PingReply(this.getRequestTAG(), this.isOk(), this
				.getRequest().clone(), this.getPkt());
	}

	public static PingReply get_data(Sim_event ev) {
		Message<PingReply> message = new Message<PingReply>();
		return message.get_data(ev);
	}

	@Override
	public PingRequest getRequest() {
		return (PingRequest) this.request;
	}

	public void setRequest(PingRequest request) {
		this.request = request;
	}

	public InfoPacket getPkt() {
		return this.getRequest().getPkt();
	}

	public void setPkt(InfoPacket pkt) {
		this.getRequest().setPkt(pkt);
	}
}
