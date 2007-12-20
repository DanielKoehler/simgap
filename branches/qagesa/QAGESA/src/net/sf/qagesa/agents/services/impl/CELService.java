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
 * CELService.java
 *
 * Created on 15 March 2007, 18:19 by Giovanni Novelli
 *
 * $Id: CELService.java 1142 2007-07-18 20:49:58Z gnovelli $
 *
 */

package net.sf.qagesa.agents.services.impl;

import eduni.simjava.Sim_event;

import gridsim.GridSimTags;
import gridsim.IO_data;

import net.sf.gap.agents.middleware.AgentPlatform;
import net.sf.gap.agents.services.impl.GISService;
import net.sf.gap.messages.impl.GISReply;
import net.sf.gap.messages.impl.GISRequest;

import net.sf.qagesa.constants.QAGESATags;

/**
 * CEL service
 * 
 * 
 * @author Giovanni Novelli
 * @see net.p2pgrid.gap.agents.services.PlatformService
 */
public class CELService extends GISService {

	/**
	 * @param ap
	 * @param trace_flag
	 * @throws Exception
	 */
	public CELService(AgentPlatform ap, boolean trace_flag) throws Exception {
		super(ap, "CEL", trace_flag);
	}

	@Override
	public void initialize() throws Exception {
            super.initialize();
	}

	@Override
	protected void dispose() {
	}

	@Override
	public void processOtherEvent(Sim_event ev) {
		switch (ev.get_tag()) {
		case QAGESATags.CEL_SEARCH_REQ:
			this.processGIS();
			this.sendCEL_SEARCH_REPLY(ev);
			break;

		default:
			break;
		}
	}

	protected void sendCEL_SEARCH_REPLY(Sim_event ev) {
		int SIZE = 500;
		GISRequest request = GISRequest.get_data(ev);
		GISReply reply = new GISReply(ev.get_tag(), true, request, this
				.getGisRepository());
		super.send(super.output, GridSimTags.SCHEDULE_NOW,
				QAGESATags.CEL_SEARCH_REP, new IO_data(reply, SIZE, request
						.getSrc_ID()));
	}

}
