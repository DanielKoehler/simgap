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
 * Service.java
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

import net.sf.gap.agents.GridAgent;
import net.sf.gap.grid.components.AbstractGridElement;
import eduni.simjava.Sim_event;

/**
 * This class is responsible for abstraction of services
 * 
 * @author Giovanni Novelli
 * @see net.p2pgrid.agents.GridAgent
 */

public class Service extends GridAgent {
    private ServiceInfo serviceInfo;
	public Service(AbstractGridElement ge, String name, boolean trace_flag)
			throws Exception {
		super(ge, name, 0, trace_flag);
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

	}

    public ServiceInfo getServiceInfo() {
        return serviceInfo;
    }

    public void setServiceInfo(ServiceInfo serviceInfo) {
        this.serviceInfo = serviceInfo;
    }

}
