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
 * AgentReply.java
 *
 * Created on 16 August 2006, 12.00 by Giovanni Novelli
 *
 ****************************************************************************************
 *
 * $Revision: 1141 $
 * $Id: AgentReply.java 1141 2007-07-18 18:22:15Z gnovelli $
 * $HeadURL: file:///var/svn/grid/trunk/GAP/src/net/sf/gap/messages/impl/AgentReply.java $
 *
 *****************************************************************************************
 */

package net.sf.gap.messages.impl;

import eduni.simjava.Sim_event;
import net.sf.gap.messages.Message;
import net.sf.gap.messages.Reply;

/**
 * 
 * @author Giovanni Novelli
 */
public class AgentReply extends Reply {

	public AgentReply(int requestTAG, boolean ok, AgentRequest agentRequest) {
		super(requestTAG, ok, agentRequest);
	}

	@Override
	public AgentReply clone() {
		return new AgentReply(this.getRequestTAG(), this.isOk(), this
				.getRequest().clone());
	}

	public static AgentReply get_data(Sim_event ev) {
		Message<AgentReply> message = new Message<AgentReply>();
		return message.get_data(ev);
	}

	@Override
	public AgentRequest getRequest() {
		return (AgentRequest) this.request;
	}

	public void setRequest(AgentRequest agentRequest) {
		this.request = agentRequest;
	}
}
