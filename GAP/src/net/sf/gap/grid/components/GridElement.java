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
 * GridElement.java
 *
 * Created on 19 August 2006, 12.00 by Giovanni Novelli
 *
 ****************************************************************************************
 *
 * $Revision: 1141 $
 * $Id: GridElement.java 1141 2007-07-18 18:22:15Z gnovelli $
 * $HeadURL: file:///var/svn/grid/trunk/GAP/src/net/sf/gap/grid/components/GridElement.java $
 *
 *****************************************************************************************
 */

package net.sf.gap.grid.components;

import eduni.simjava.Sim_event;
import eduni.simjava.Sim_type_p;
import gridsim.Accumulator;
import gridsim.AllocPolicy;
import gridsim.GridSim;
import gridsim.GridSimTags;
import gridsim.IO_data;
import gridsim.ResourceCalendar;
import gridsim.ResourceCharacteristics;
import gridsim.datagrid.DataGridResource;
import gridsim.datagrid.ReplicaManager;
import gridsim.net.FIFOScheduler;
import gridsim.net.InfoPacket;
import gridsim.net.Link;
import gridsim.net.RIPRouter;
import gridsim.net.Router;

import java.util.HashMap;

import net.sf.gap.agents.AbstractAgent;
import net.sf.gap.constants.Tags;
import net.sf.gap.messages.impl.AgentReply;
import net.sf.gap.messages.impl.AgentRequest;
import net.sf.gap.agents.middleware.AgentPlatform;
import net.sf.gap.agents.services.Service;
import net.sf.gap.grid.LocalDirectory;
import net.sf.gap.grid.factories.LinkFactory;
import net.sf.gap.messages.impl.PingReply;
import net.sf.gap.messages.impl.PingRequest;

/**
 * 
 * @author Giovanni Novelli
 */
public abstract class GridElement extends DataGridResource {
	private AgentPlatform agentPlatform;

	private int numPE;

	private int numWN;

	private Link externalLink;

	private RIPRouter internalRouter;
        
        private RIPRouter externalRouter;

	private boolean SE; // to know if this is an SE or a CE

	private LocalDirectory localDirectory; // Lists of agents running on this

	// Grid Element (CE/SE)

	/**
	 * Creates a new instance of GridElement
	 */
	public GridElement(String name, Link link,
			ResourceCharacteristics resourceCharacteristics,
			ResourceCalendar resourceCalendar, ReplicaManager replicaManager)
			throws Exception {
		super(name, LinkFactory.getGELink(name), resourceCharacteristics,
				resourceCalendar, replicaManager);
		RIPRouter grrouter = new RIPRouter("gr_router_" + name);
		FIFOScheduler sched = new FIFOScheduler("gr_sched_" + name);
		grrouter.attachHost(this, sched);
		this.setExternalLink(link);
		this.setInternalRouter(grrouter);
		this.setLocalDirectory(new LocalDirectory(this));
	}

        protected void registerOtherEntity() {
        }
        
	public AllocPolicy getAllocPolicy() {
		return this.policy_;
	}

	public Accumulator getTotalLoad() {
		return this.policy_.getTotalLoad();
	}

	/**
	 * Handles ping request
	 * 
	 * @param ev
	 *            a Sim_event object
	 */
	private void pingRequest(Sim_event ev) {
		InfoPacket pkt = (InfoPacket) ev.get_data();
		pkt.setTag(GridSimTags.INFOPKT_RETURN);
		pkt.setDestID(pkt.getSrcID());

		// sends back to the sender
		super.send(super.output, GridSimTags.SCHEDULE_NOW,
				GridSimTags.INFOPKT_RETURN, new IO_data(pkt, pkt.getSize(), pkt
						.getSrcID()));
	}

	/*
	 * 
	 * To process other events in concrete simulations TODO: Divide
	 * responsibilities between GridElement class and a new indipendent
	 * Sim_entity class for Agent Middleware
	 * 
	 */
	protected abstract void processCustomEvents(Sim_event ev);

	@Override
	protected void processOtherEvent(Sim_event ev) {
		int SIZE = 500;
		switch (ev.get_tag()) {
		case Tags.AGENT_KILLAWAIT_REP:
		case Tags.AGENT_KILL_REP:
		case Tags.AGENT_MOVE_REP:
		case Tags.AGENT_PAUSE_REP:
		case Tags.AGENT_RESUME_REP:
		case Tags.AGENT_RUN_REP:
			int replyToID = 0;
			AgentReply agentReply = AgentReply.get_data(ev);
			replyToID = agentReply.getRequest().getSrc_ID();
			super.send(super.output, GridSimTags.SCHEDULE_NOW, ev.get_tag(),
					new IO_data(agentReply, SIZE, replyToID));
			break;
		case GridSimTags.INFOPKT_SUBMIT:
			this.pingRequest(ev);
			break;
		case Tags.PING_REQ:
			this.pingReply(ev);
			break;
		case Tags.AGENT_RUN_REQ:
		case Tags.AGENT_KILL_REQ:
		case Tags.AGENT_KILLAWAIT_REQ:
		case Tags.AGENT_PAUSE_REQ:
		case Tags.AGENT_RESUME_REQ:
			this.agentsLife(ev);
			break;

		default:
			this.processCustomEvents(ev);
			break;
		}
	}

	private void sendACKNACK(Sim_event ev, AgentRequest agentRequest,
			boolean flag) {
		AgentReply agentReply = null;

		int SIZE = 500;

		int replyToID = 0;
		replyToID = agentRequest.getSrc_ID();
		agentReply = new AgentReply(ev.get_tag(), flag, agentRequest);
		super.send(super.output, GridSimTags.SCHEDULE_NOW, ev.get_tag() + 1,
				new IO_data(agentReply, SIZE, replyToID));
	}

	private void agentsLife(Sim_event ev) {
		AgentRequest agentRequest = AgentRequest.get_data(ev);
		if (ev.get_tag() != Tags.AGENT_KILL_REQ) {
			if (this.updateLocalDirectory(ev)) {
				int aid = agentRequest.getDst_agentID();
				super.send(super.output, GridSimTags.SCHEDULE_NOW,
						ev.get_tag(), new IO_data(agentRequest, SIZE, aid));
			} else {
				this.sendACKNACK(ev, agentRequest, false);
			}
		} else if (ev.get_tag() == Tags.AGENT_KILL_REQ) {
			AgentRequest agentRequestGridlets = agentRequest.clone();
			agentRequestGridlets.setSrc_ID(this.get_id());
			agentRequestGridlets.setSrc_resID(this.get_id());
			AgentReply agentReply = this.hasGridletsAgent(agentRequestGridlets);
			if (!agentReply.isOk()) {
				if (this.updateLocalDirectory(ev)) {
					int aid = agentRequest.getDst_agentID();
					super.send(super.output, GridSimTags.SCHEDULE_NOW, ev
							.get_tag(), new IO_data(agentRequest, SIZE, aid));
				}
			} else {
				this.sendACKNACK(ev, agentRequest, false);
			}
		} else {
			AgentReply agentReply = new AgentReply(ev.get_tag(), false,
					agentRequest);
			if (agentRequest.getSrc_ID() == agentRequest.getSrc_resID()) { // Replying
				// to
				// an
				// user's
				// request
				int uid = agentRequest.getSrc_ID();
				super.send(super.output, GridSimTags.SCHEDULE_NOW,
						ev.get_tag() + 1, new IO_data(agentReply, SIZE, uid));
			} else { // Replying to an agent's request
				int aid = agentRequest.getSrc_ID();
				super.send(super.output, GridSimTags.SCHEDULE_NOW,
						ev.get_tag() + 1, new IO_data(agentReply, SIZE, aid));
			}
		}
	}

	private void pingReply(Sim_event ev) {
		PingRequest request = PingRequest.get_data(ev);
		InfoPacket pkt = super.pingBlockingCall(request.getPing_dst_ID(),
				50);
		PingReply reply = new PingReply(ev.get_tag(), true, request, pkt);
		if (pkt != null) {
			super.send(super.output, GridSimTags.SCHEDULE_NOW, Tags.PING_REP,
					new IO_data(reply, 10, request.getSrc_ID()));
		} else {
			reply.setOk(false);
			super.send(super.output, GridSimTags.SCHEDULE_NOW, Tags.PING_REP,
					new IO_data(reply, 10, request.getSrc_ID()));
		}
	}

	public AgentReply hasGridletsAgent(AgentRequest agentRequest) {
		int SIZE = 500;
		super.send(super.output, GridSimTags.SCHEDULE_NOW,
				Tags.HASGRIDLETS_REQUEST, new IO_data(agentRequest, SIZE,
						agentRequest.getDst_agentID()));

		Sim_type_p ptag = new Sim_type_p(Tags.HASGRIDLETS_REPLY);
		Sim_event ev = new Sim_event();
		super.sim_get_next(ptag, ev); // only look for this type of ack
		AgentReply agentReply = AgentReply.get_data(ev);
		return agentReply;
	}

	private boolean updateLocalDirectory(Sim_event ev) {
		AgentRequest agentRequest = AgentRequest.get_data(ev);
		return this.getLocalDirectory().update(agentRequest, ev.get_tag());
	}

	public int getNumPE() {
		return this.numPE;
	}

	public int getNumWN() {
		return this.numWN;
	}

	public void setNumPE(int numPE) {
		this.numPE = numPE;
	}

	public void setNumWN(int numWN) {
		this.numWN = numWN;
	}

	protected void shutdownUserEntity() {
		int shutdownID = GridSim.getGridSimShutdownEntityId();
		if (shutdownID != -1) {
			this.send(shutdownID, 0.0, GridSimTags.END_OF_SIMULATION);
		}
	}

	public int getPlatformID() {
		return this.getAgentPlatform().get_id();
	}

	public Link getExternalLink() {
		return this.externalLink;
	}

	public void setExternalLink(Link externalLink) {
		this.externalLink = externalLink;
	}

	public RIPRouter getInternalRouter() {
		return this.internalRouter;
	}

	public void setInternalRouter(RIPRouter internalRouter) {
		this.internalRouter = internalRouter;
	}

	public boolean isSE() {
		return this.SE;
	}

	public void setSE(boolean SE) {
		this.SE = SE;
	}

	public AgentPlatform getAgentPlatform() {
		return this.agentPlatform;
	}

	public void setAgentPlatform(AgentPlatform platform) {
		this.agentPlatform = platform;
	}

	private void attachHost(GridSim host) {
		try {
			FIFOScheduler sched = new FIFOScheduler(this.get_name() + "_"
					+ host.get_name() + "_sched");
			this.getInternalRouter().attachHost(host, sched);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void attachPlatform(AgentPlatform platform) {
		this.attachHost(platform);
	}

	public void attachAgent(AbstractAgent agent) {
		this.attachHost(agent);
	}

	public void attachService(Service service) {
		this.attachHost(service);
	}

	public void attachRouter(RIPRouter router) {
		try {
			FIFOScheduler sched_in = new FIFOScheduler(this.get_name()
					+ "_sched_in");
			FIFOScheduler sched_out = new FIFOScheduler(this.get_name()
					+ "_sched_out");
			this.getInternalRouter().attachRouter(router,
					this.getExternalLink(), sched_in, sched_out);
                        this.setExternalRouter(router);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public LocalDirectory getLocalDirectory() {
		return localDirectory;
	}

	public void setLocalDirectory(LocalDirectory localDirectory) {
		this.localDirectory = localDirectory;
	}

	public int getTotalMIPS() {
		return this.resource_.getMIPSRating();
	}

    public RIPRouter getExternalRouter() {
        return externalRouter;
    }

    public void setExternalRouter(RIPRouter externalRouter) {
        this.externalRouter = externalRouter;
    }
}
