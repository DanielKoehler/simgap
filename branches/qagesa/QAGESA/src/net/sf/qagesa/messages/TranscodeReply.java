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
 * TranscodeReply.java
 *
 * Created on 24 March 2007, 20.06 by Giovanni Novelli
 *
 * $Id: TranscodeReply.java 1142 2007-07-18 20:49:58Z gnovelli $
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
public class TranscodeReply extends Reply {

	/**
	 * Creates a new instance of TranscodeRequest
	 */
	public TranscodeReply(int requestTAG, boolean ok, TranscodeRequest request, int agentID) {
		super(requestTAG, ok, request);
                this.setAgentID(agentID);
	}


    public int getAgentID() {
        return this.getRequest().getAgentID();
    }

    public void setAgentID(int agentID) {
        this.getRequest().setAgentID(agentID);
    }
	@Override
	public TranscodeReply clone() {
		return new TranscodeReply(this.getRequestTAG(), this.isOk(), this
				.getRequest().clone(), this.getAgentID());
	}

	public static TranscodeReply get_data(Sim_event ev) {
		Message<TranscodeReply> message = new Message<TranscodeReply>();
		return message.get_data(ev);
	}

	@Override
	public TranscodeRequest getRequest() {
		return (TranscodeRequest) this.request;
	}

	public void setRequest(TranscodeRequest request) {
		this.request = request;
	}
}
