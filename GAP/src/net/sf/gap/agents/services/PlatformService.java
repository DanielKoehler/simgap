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
 * PlatformService.java
 *
 * Created on 10 March 2007, 12.00 by Giovanni Novelli
 *
 ****************************************************************************************
 *
 * $Revision$
 * $Id$
 * $HeadURL$
 *
 *****************************************************************************************
 */

package net.sf.gap.agents.services;

import net.sf.gap.agents.middleware.AgentPlatform;

/**
 * This class is responsible for abstraction of Platform services
 * 
 * @author Giovanni Novelli
 * @see net.p2pgrid.agents.services.Service
 */

public class PlatformService extends Service {
	public PlatformService(AgentPlatform ap, String name, boolean trace_flag)
			throws Exception {
		super(ap.getGridElement(), name, trace_flag);
                this.setServiceInfo(new ServiceInfo(name, ap.getGridElement()));
                this.attachToAgentPlatform();
	}

	public void attachToAgentPlatform() {
		this.getGridElement().attachService(this);
	}
        
	@Override
	public void initialize() throws Exception {
            super.initialize();
            this.getAgentPlatform().getServicesList().add(this.getServiceInfo());
	}
}
