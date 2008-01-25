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
 * TestService.java
 *
 * Created on 12 March 2007, 12.00 by Giovanni Novelli
 *
 ****************************************************************************************
 *
 * $Revision$
 * $Id$
 * $HeadURL$
 *
 *****************************************************************************************
 */

package net.sf.gap.agents.services.impl;

import net.sf.gap.agents.middleware.AbstractAgentPlatform;
import net.sf.gap.agents.services.PlatformService;
import net.sf.gap.constants.Tags;
import net.sf.gap.messages.impl.AgentRequest;
import eduni.simjava.Sim_event;

/**
 * This class is responsible for a sample test subclass of PlatformService
 * 
 * @author Giovanni Novelli
 * @see net.p2pgrid.agents.services.PlatformService
 */
public class TestService extends PlatformService {

	/**
	 * @param ap
	 * @param name
	 * @param trace_flag
	 * @throws Exception
	 */
	public TestService(AbstractAgentPlatform ap, boolean trace_flag) throws Exception {
		super(ap, "TestService", trace_flag);
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
		// TODO Auto-generated method stub
		switch (ev.get_tag()) {
		case Tags.TEST_SERVICE_REQ:
			AgentRequest agentRequest = AgentRequest.get_data(ev);
			super.sendACK(ev, agentRequest);
			break;

		default:
			break;
		}
	}
}
