/*
 ****************************************************************************************
 * Copyright © Giovanni Novelli                                             
 * All Rights Reserved.                                                                 
 ****************************************************************************************
 *
 * Title:        QAGESA Simulator
 * Description:  QAGESA (QoS Aware Grid Enabled Streaming Architecture) Simulator
 *               of a QoS-Aware Architecture for Multimedia Content Provisioning in a GRID Environment
 * License:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * MuMSearchReply.java
 *
 * Created on 16 March 2007, 16.16 by Giovanni Novelli
 *
 * $Id: MuMSearchReply.java 1142 2007-07-18 20:49:58Z gnovelli $
 *
 */

package net.sf.qagesa.messages;

import eduni.simjava.Sim_event;

import net.sf.gap.messages.Message;
import net.sf.gap.messages.Reply;

import net.sf.qagesa.agents.services.impl.mum.GEList;

/**
 * 
 * @author Giovanni Novelli
 */
public class MuMSearchReply extends Reply {

	/**
	 * Creates a new instance of MuMSearchRequest
	 */
	public MuMSearchReply(int requestTAG, boolean ok, MuMSearchRequest request,
			GEList gelist) {
		super(requestTAG, ok, request);
		this.setGelist(gelist);
	}

	@Override
	public MuMSearchReply clone() {
		return new MuMSearchReply(this.getRequestTAG(), this.isOk(), this
				.getRequest().clone(), this.getGelist());
	}

	public static MuMSearchReply get_data(Sim_event ev) {
		Message<MuMSearchReply> message = new Message<MuMSearchReply>();
		return message.get_data(ev);
	}

	@Override
	public MuMSearchRequest getRequest() {
		return (MuMSearchRequest) this.request;
	}

	public void setRequest(MuMSearchRequest request) {
		this.request = request;
	}

	public GEList getGelist() {
		return this.getRequest().getGelist();
	}

	public void setGelist(GEList gelist) {
		this.getRequest().setGelist(gelist);
	}
}
