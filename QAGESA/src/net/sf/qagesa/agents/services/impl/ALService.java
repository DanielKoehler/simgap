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
 * ALService.java
 *
 * Created on 15 March 2007, 18:19 by Giovanni Novelli
 *
 * $Id: ALService.java 1142 2007-07-18 20:49:58Z gnovelli $
 *
 */

package net.sf.qagesa.agents.services.impl;

import eduni.simjava.Sim_event;

import gridsim.GridSimTags;
import gridsim.IO_data;

import net.sf.gap.agents.middleware.AgentPlatform;
import net.sf.gap.agents.services.hardcoded.df.AgentsMap;
import net.sf.gap.agents.services.PlatformService;
import net.sf.gap.agents.services.hardcoded.df.AgentsMap;

import net.sf.qagesa.constants.QAGESATags;
import net.sf.qagesa.constants.QAGESAEntityTypes;

/**
 * AL service
 * 
 * 
 * @author Giovanni Novelli
 * @see net.p2pgrid.gap.agents.services.PlatformService
 */
public class ALService extends PlatformService {
        private AgentsMap agentsMap;
	/**
	 * @param ap
	 * @param trace_flag
	 * @throws Exception
	 */
	public ALService(AgentPlatform ap, boolean trace_flag) throws Exception {
		super(ap, "AL", trace_flag);
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
                    case QAGESATags.AL_UPDATE_REQ:
                        break;
		case QAGESATags.AL_SEARCH_REQ:
			int SIZE = 1500;
			int replyToID = ev.get_src();
			AgentsMap taMap = this.getAgentPlatform().getDirectoryFacilitator()
					.getDFMap().getAgentsMap(QAGESAEntityTypes.SERVER_PROXY);
			super.send(super.output, GridSimTags.SCHEDULE_NOW,
					QAGESATags.AL_SEARCH_REP, new IO_data(taMap, SIZE,
							replyToID));
			break;

		default:
			break;
		}
	}

    public AgentsMap getAgentsMap() {
        return agentsMap;
    }

    public void setAgentsMap(AgentsMap agentsMap) {
        this.agentsMap = agentsMap;
    }
}
