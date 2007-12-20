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
 * ReFPlayReply.java
 *
 * Created on 24 March 2007, 17.54 by Giovanni Novelli
 *
 * $Id: ReFPlayReply.java 1142 2007-07-18 20:49:58Z gnovelli $
 *
 */

package net.sf.qagesa.messages;

import net.sf.gap.messages.Message;
import net.sf.gap.messages.Reply;
import eduni.simjava.Sim_event;

/**
 * 
 * @author Giovanni Novelli
 */
public class ReFPlayReply extends Reply {

	/**
	 * Creates a new instance of ReFPlayRequest
	 */
	public ReFPlayReply(int requestTAG, boolean ok, ReFPlayRequest request) {
		super(requestTAG, ok, request);
	}

	@Override
	public ReFPlayReply clone() {
		return new ReFPlayReply(this.getRequestTAG(), this.isOk(), this
				.getRequest().clone());
	}

	public static ReFPlayReply get_data(Sim_event ev) {
		Message<ReFPlayReply> message = new Message<ReFPlayReply>();
		return message.get_data(ev);
	}

	@Override
	public ReFPlayRequest getRequest() {
		return (ReFPlayRequest) this.request;
	}

	public void setRequest(ReFPlayRequest request) {
		this.request = request;
	}
}
