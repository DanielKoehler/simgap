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
 * GISService.java
 *
 * Created on 12 March 2007, 12.00 by Giovanni Novelli
 *
 ****************************************************************************************
 *
 * $Revision: 1141 $
 * $Id: GISService.java 1141 2007-07-18 18:22:15Z gnovelli $
 * $HeadURL: file:///var/svn/grid/trunk/GAP/src/net/sf/gap/agents/services/impl/GISService.java $
 *
 *****************************************************************************************
 */

package net.sf.gap.agents.services.impl;

import eduni.simjava.Sim_event;
import eduni.simjava.Sim_system;

import gridsim.Accumulator;
import gridsim.GridSimTags;
import gridsim.IO_data;

import java.util.Iterator;

import net.sf.gap.constants.Tags;
import net.sf.gap.agents.services.impl.gis.GISEntry;
import net.sf.gap.agents.middleware.AgentPlatform;
import net.sf.gap.agents.services.PlatformService;
import net.sf.gap.agents.services.impl.gis.GISRepository;
import net.sf.gap.grid.components.GridElement;
import net.sf.gap.messages.impl.GISReply;
import net.sf.gap.messages.impl.GISRequest;

/**
 * This class is responsible for a sample test subclass of PlatformService
 * 
 * @author Giovanni Novelli
 * @see net.p2pgrid.agents.services.PlatformService
 */
public class GISService extends PlatformService {

	private GISRepository gisRepository;

	/**
	 * @param ap
	 * @param name
	 * @param trace_flag
	 * @throws Exception
	 */
	public GISService(AgentPlatform ap, boolean trace_flag) throws Exception {
		super(ap, "GISService", trace_flag);
	}

	public GISService(AgentPlatform ap, String name, boolean trace_flag)
			throws Exception {
		super(ap, name, trace_flag);
	}

	@Override
	public void initialize() throws Exception {
            super.initialize();
            this.setGisRepository(new GISRepository(this.getAgentPlatform()));

		for (int i = 0; i < this.getAgentPlatform().getVirtualOrganization().getNumCEs(); i++) {
			GridElement ce = (GridElement) Sim_system.get_entity("CE_"+i);
			this.getGisRepository().addGE(ce);
		}
		for (int i = 0; i < this.getAgentPlatform().getVirtualOrganization().getNumSEs(); i++) {
			GridElement se = (GridElement) Sim_system.get_entity("SE_"+i);
			this.getGisRepository().addGE(se);
		}
	}

	@Override
	protected void dispose() {
	}

	@Override
	public void processOtherEvent(Sim_event ev) {
		// TODO Auto-generated method stub
		switch (ev.get_tag()) {
		case Tags.GIS_REQUEST:
			this.processGIS();
			this.sendGIS_REPLY(ev);
			break;

		default:
			break;
		}
	}

	protected void sendGIS_REPLY(Sim_event ev) {
		int SIZE = 500;
		GISRequest request = GISRequest.get_data(ev);
		GISReply reply = new GISReply(ev.get_tag(), true, request, this
				.getGisRepository());
		super.send(super.output, GridSimTags.SCHEDULE_NOW, Tags.GIS_REPLY,
				new IO_data(reply, SIZE, request.getSrc_ID()));
	}

	public GISRepository getGisRepository() {
		return gisRepository;
	}

	public void setGisRepository(GISRepository gisRepository) {
		this.gisRepository = gisRepository;
	}

	public void processGIS() {
            int numCEs = this.getAgentPlatform().getVirtualOrganization().getNumCEs();
            for (int i=0;i<numCEs;i++) {
                GridElement ge = (GridElement) Sim_system.get_entity("CE_"+i);
                int geid = ge.get_id();
                int numPEs = ge.getNumPE();
                int numFreePEs = super.getNumFreePE(ge.get_id());
                int numFreeAgents = ge.getLocalDirectory().getFreeAgents();
                int totalMIPS = ge.getTotalMIPS();
                boolean SE = ge.isSE();
                double MB_size = ge.getTotalStorageCapacity();
                this.addEntry(geid, numPEs, numFreePEs, numFreeAgents,
                                totalMIPS, SE, MB_size, ge.getTotalLoad());
            }
            this.getGisRepository().setLastRequestTime(super.clock());
	}

	public GISEntry addEntry(int geid, int numPEs, int numFreePEs,
			int numFreeAgents, int totalMIPS, boolean SE, double MB_size,
			Accumulator load) {
		GISEntry entry = new GISEntry(numPEs, numFreePEs, numFreeAgents,
				totalMIPS, SE, MB_size, load);
                if (this.getGisRepository()==null) {
                    this.setGisRepository(new GISRepository(this.getAgentPlatform()));

                    for (int i = 0; i < this.getAgentPlatform().getVirtualOrganization().getNumCEs(); i++) {
                            GridElement ce = (GridElement) Sim_system.get_entity("CE_"+i);
                            this.getGisRepository().addGE(ce);
                    }
                    for (int i = 0; i < this.getAgentPlatform().getVirtualOrganization().getNumSEs(); i++) {
                            GridElement se = (GridElement) Sim_system.get_entity("SE_"+i);
                            this.getGisRepository().addGE(se);
                    }
                }
		return this.getGisRepository().put(geid, entry);
	}

	public GISEntry removeEntry(int geid) {
		return this.getGisRepository().remove(geid);
	}

}
