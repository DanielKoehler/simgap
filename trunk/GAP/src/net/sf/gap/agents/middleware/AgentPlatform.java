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
 * AgentPlatform.java
 *
 * Created on 17 August 2006, 12.00 by Giovanni Novelli
 *
 ****************************************************************************************
 *
 * $Revision: 1141 $
 * $Id: AgentPlatform.java 1141 2007-07-18 18:22:15Z gnovelli $
 * $HeadURL: file:///var/svn/grid/trunk/GAP/src/net/sf/gap/agents/middleware/AgentPlatform.java $
 *
 *****************************************************************************************
 */

package net.sf.gap.agents.middleware;

import net.sf.gap.agents.AbstractAgent;
import net.sf.gap.constants.Tags;
import net.sf.gap.messages.impl.ServicesListReply;
import net.sf.gap.messages.impl.ServicesListRequest;
import net.sf.gap.agents.services.ServicesList;
import net.sf.gap.agents.services.hardcoded.DirectoryFacilitator;
import net.sf.gap.agents.services.hardcoded.NetworkMonitor;
import net.sf.gap.agents.services.impl.GISService;
import net.sf.gap.agents.services.impl.NMService;
import net.sf.gap.agents.services.impl.TestService;
import net.sf.gap.grid.components.GridElement;
import eduni.simjava.Sim_event;
import eduni.simjava.Sim_port;
import eduni.simjava.Sim_predicate;
import gridsim.GridSimTags;
import gridsim.IO_data;

/**
 * 
 * @author Giovanni Novelli
 */
public abstract class AgentPlatform extends AgentMiddleware {
        private int totalAgents;    // Total number of agent entities
    
	private ServicesList servicesList; // list of services

	private NetworkMonitor networkMonitor; // Network monitor

	private DirectoryFacilitator directoryFacilitator; // Directory Facilitator

	private GISService gisService; // GISService instance of GISService

	private NMService nmService; // NetworkMonitor Service

	private TestService testService; // TestService instance of TestService

	// class derivated from PlatformService
	// class

	/**
	 * Creates a new instance of AgentPlatform
	 */
	public AgentPlatform(String name, boolean trace) throws Exception {
		super(name, trace);
                this.setPlatform(true);
	}

        public void createServices() throws Exception {
                this.setTestService(new TestService(this, false));
		this.setGisService(new GISService(this, false));
		this.setNMService(new NMService(this, false));
        }
        
	public void addAgent(AbstractAgent agent, GridElement ge) {
		this.getDirectoryFacilitator().addAgent(agent, ge);
	}

	public abstract void processOtherEvent(Sim_event ev);

	protected void processEvent(Sim_event ev) {
		switch (ev.get_tag()) {
		case Tags.AP_SERVICES_LIST_REQ:
			ServicesListRequest servicesListRequest = ServicesListRequest
					.get_data(ev);
			int SIZE = 500;

			int replyToID = 0;

			replyToID = servicesListRequest.getSrc_ID();
			ServicesListReply servicesListReply = new ServicesListReply(ev
					.get_tag(), true, servicesListRequest, this
					.getServicesList());
			super.send(super.output, GridSimTags.SCHEDULE_NOW,
					Tags.AP_SERVICES_LIST_REP, new IO_data(servicesListReply,
							SIZE, replyToID));
			break;
		case Tags.DF_REGISTER_REQ:
		case Tags.DF_DEREGISTER_REQ:
		case Tags.DF_PAUSED_REQ:
		case Tags.DF_RESUMED_REQ:
			this.getDirectoryFacilitator().processEvent(ev);
			break;

		case Tags.NM_NETWORKMAP_REQ:
			this.getNetworkMonitor().processEvent(ev);
			break;

		default:
			this.processOtherEvent(ev);
			break;
		}
	}

	public void initPlatform() throws Exception {
                this.setNetworkMonitor(new NetworkMonitor(this));
		this.setDirectoryFacilitator(new DirectoryFacilitator(this));
                
		this.setServicesList(new ServicesList());

		this.getTestService().initialize();

		this.getGisService().initialize();

		this.getNMService().initialize();
	}

        public abstract void initAgents() throws Exception;

	public ServicesList getServicesList() {
		return servicesList;
	}

	public void setServicesList(ServicesList servicesList) {
		this.servicesList = servicesList;
	}

	public NetworkMonitor getNetworkMonitor() {
		return networkMonitor;
	}

	public void setNetworkMonitor(NetworkMonitor networkMonitor) {
		this.networkMonitor = networkMonitor;
	}

	public DirectoryFacilitator getDirectoryFacilitator() {
		return directoryFacilitator;
	}

	public void setDirectoryFacilitator(
			DirectoryFacilitator directoryFacilitator) {
		this.directoryFacilitator = directoryFacilitator;
	}

	public Sim_port getOutput() {
		return super.output;
	}

	@Override
	public void send(Sim_port destPort, double delay, int gridSimTag,
			Object data) {
		super.send(destPort, delay, gridSimTag, data);
	}

	@Override
	public void sim_get_next(Sim_predicate p, Sim_event ev) {
		super.sim_get_next(p, ev);
	}

	public TestService getTestService() {
		return testService;
	}

	public void setTestService(TestService testService) {
		this.testService = testService;
	}

	public GISService getGisService() {
		return gisService;
	}

	public void setGisService(GISService gisService) {
		this.gisService = gisService;
	}

	public NMService getNMService() {
		return nmService;
	}

	public void setNMService(NMService nmService) {
		this.nmService = nmService;
	}

    public int getTotalAgents() {
        return totalAgents;
    }

    public void setTotalAgents(int totalAgents) {
        this.totalAgents = totalAgents;
    }
}
