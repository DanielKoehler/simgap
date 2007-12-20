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
 * AbstractUser.java
 *
 * Created on 17 August 2006, 12.00 by Giovanni Novelli
 *
 ****************************************************************************************
 *
 * $Revision: 1141 $
 * $Id: AbstractUser.java 1141 2007-07-18 18:22:15Z gnovelli $
 * $HeadURL: file:///var/svn/grid/trunk/GAP/src/net/sf/gap/users/AbstractUser.java $
 *
 *****************************************************************************************
 */


package net.sf.gap.users;

import eduni.simjava.Sim_event;
import eduni.simjava.Sim_system;
import eduni.simjava.Sim_type_p;
import gridsim.GridSim;
import gridsim.GridSimTags;
import gridsim.Gridlet;
import gridsim.IO_data;
import gridsim.datagrid.DataGridUser;
import gridsim.net.FIFOScheduler;
import gridsim.net.Link;
import gridsim.util.SimReport;
import junit.framework.Assert;
import net.sf.gap.GAP;
import net.sf.gap.constants.EntityTypes;
import net.sf.gap.constants.Tags;
import net.sf.gap.messages.impl.GISReply;
import net.sf.gap.messages.impl.GISRequest;
import net.sf.gap.messages.impl.GridletReply;
import net.sf.gap.messages.impl.GridletRequest;
import net.sf.gap.messages.impl.AgentReply;
import net.sf.gap.messages.impl.AgentRequest;
import net.sf.gap.messages.impl.ServicesListReply;
import net.sf.gap.messages.impl.ServicesListRequest;
import net.sf.gap.agents.predicates.Predicate;
import net.sf.gap.grid.AbstractVirtualOrganization;
import net.sf.gap.messages.impl.PingReply;
import net.sf.gap.messages.impl.PingRequest;

/**
 * 
 * @author Giovanni Novelli
 */
public abstract class AbstractUser extends DataGridUser {
        private boolean traceFlag;
    
	private SimReport report_; // logs every events

	private FIFOScheduler userSched;

	private AbstractVirtualOrganization virtualOrganization;

	private int entityType;

	/**
	 * Creates a new instance of AbstractUser
	 */
	public AbstractUser(String name, Link link, int entityType,
			boolean trace_flag) throws Exception {
		super(name, link);
		this.setEntityType(entityType);
		this.setUserSched(new FIFOScheduler("sched_" + name));
	}

	protected GISReply requestToGIS() {
		int SIZE = 500;
		double evsend_time = 0;
		GISRequest request = new GISRequest(this.get_id(), this.get_id());
		int requestID = request.getRequestID();
		int reqrepID = request.getReqrepID();
		super.send(super.output, GridSimTags.SCHEDULE_NOW, Tags.GIS_REQUEST,
				new IO_data(request, SIZE, super.getEntityId("GISService")));
		evsend_time = GridSim.clock();
		String msg = String.format("%1$f %2$d %3$s --> GAP GIS_REQUEST",
				evsend_time, reqrepID, this.get_name());
		this.write(msg);

		Sim_event ev = new Sim_event();
		Predicate predicate = new Predicate(Tags.GIS_REPLY);
		super.sim_get_next(predicate, ev); // only look for this type of ack
		GISReply reply = GISReply.get_data(ev);
		Assert.assertEquals(requestID, reply.getRequestID());
		Assert.assertEquals(Tags.GIS_REQUEST, reply.getRequestTAG());
		Assert.assertEquals(Tags.GIS_REPLY, ev.get_tag());
		double evrecv_time = GridSim.clock();
		msg = String.format("%1$f %2$d %3$s <-- GAP GIS_REPLY\n", evrecv_time,
				reqrepID, this.get_name(), reply.getGIS());
		this.write(msg);
		return reply;
	}

	protected ServicesListReply requestServicesList() {
		int SIZE = 500;
		double evsend_time = 0;
		ServicesListRequest request = new ServicesListRequest(this.get_id(),
				this.get_id());
		int requestID = request.getRequestID();
		int reqrepID = request.getReqrepID();
		super.send(super.output, GridSimTags.SCHEDULE_NOW,
				Tags.AP_SERVICES_LIST_REQ, new IO_data(request, SIZE, this
						.getVirtualOrganization().getAgentPlatform().get_id()));
		evsend_time = GridSim.clock();
		String msg = String.format(
				"%1$f %2$d %3$s --> %4$s SERVICES_LIST_REQUEST", evsend_time,
				reqrepID, this.get_name(), this.getVirtualOrganization().getAgentPlatform()
						.get_name());
		this.write(msg);

		Sim_event ev = new Sim_event();
		Predicate predicate = new Predicate(Tags.AP_SERVICES_LIST_REP);
		super.sim_get_next(predicate, ev); // only look for this type of ack
		ServicesListReply reply = ServicesListReply.get_data(ev);
		Assert.assertEquals(requestID, reply.getRequestID());
		Assert.assertEquals(Tags.AP_SERVICES_LIST_REQ, reply.getRequestTAG());
		Assert.assertEquals(Tags.AP_SERVICES_LIST_REP, ev.get_tag());
		double evrecv_time = GridSim.clock();
		msg = String
				.format("%1$f %2$d %3$s <-- %4$s SERVICES_LIST_REPLY %5$s",
						evrecv_time, reqrepID, this.get_name(), this.getVirtualOrganization()
								.getAgentPlatform().get_name(), reply
								.getServicesList());
		this.write(msg);
		return reply;
	}

	protected PingReply requestPing(int src_id, int dst_id) {
		int SIZE = 500;
		double evsend_time = 0;
		PingRequest request = new PingRequest(this.get_id(), this.get_id(),
				src_id, dst_id);
		int requestID = request.getRequestID();
		int reqrepID = request.getReqrepID();
		super.send(super.output, GridSimTags.SCHEDULE_NOW, Tags.PING_REQ,
				new IO_data(request, SIZE, src_id));
		evsend_time = GridSim.clock();
		String msg = String.format(
				"%1$f %2$d %3$s --> AM_%4$s PING_REQUEST AM_%5$s", evsend_time,
				reqrepID, this.get_name(), super.getEntityName(src_id), super
						.getEntityName(dst_id));
		this.write(msg);

		Sim_event ev = new Sim_event();
		Predicate predicate = new Predicate(Tags.PING_REP);
		super.sim_get_next(predicate, ev); // only look for this type of ack
		PingReply reply = PingReply.get_data(ev);
		Assert.assertEquals(requestID, reply.getRequestID());
		Assert.assertEquals(Tags.PING_REQ, reply.getRequestTAG());
		Assert.assertEquals(Tags.PING_REP, ev.get_tag());
		double evrecv_time = GridSim.clock();
		msg = String.format("%1$f %2$d %3$s <-- AM_%4$s PING_REQUEST AM_%5$s",
				evrecv_time, reqrepID, this.get_name(), super
						.getEntityName(src_id), super.getEntityName(dst_id));
		this.write(msg);
		return reply;
	}

	public AgentReply submitAgent(int agentEntityType, int agentResourceID,
			int SIZE) {
		AgentRequest agentRequest = null;
		agentRequest = new AgentRequest(this.get_id(), this.get_id(), null,
				agentResourceID, -1, agentEntityType, SIZE, agentResourceID,
				EntityTypes.NOBODY);
		return this.runAgent(agentRequest);
	}

	private AgentReply runAgent(AgentRequest agentRequest) {
		return this.requestToAgent(agentRequest, Tags.AGENT_RUN_REQ);
	}

	public AgentReply pauseAgent(AgentRequest agentRequest) {
		return this.requestToAgent(agentRequest, Tags.AGENT_PAUSE_REQ);
	}

	public AgentReply resumeAgent(AgentRequest agentRequest) {
		return this.requestToAgent(agentRequest, Tags.AGENT_RESUME_REQ);
	}

	public AgentReply killAgent(AgentRequest agentRequest) {
		return this.requestToAgent(agentRequest, Tags.AGENT_KILL_REQ);
	}

	public AgentReply killWaitAgent(AgentRequest agentRequest) {
		return this.requestToAgent(agentRequest, Tags.AGENT_KILLAWAIT_REQ);
	}

	public AgentReply moveAgent(AgentRequest agentRequest, int moveToResourceID) {
		agentRequest.setDst_moveToresID(moveToResourceID);
		return this.requestToAgent(agentRequest, Tags.AGENT_MOVE_REQ);
	}

	public abstract void processOtherEvent(Sim_event ev);

	protected void processEvent(Sim_event ev) {
		int tag = ev.get_tag();
		switch (tag) {
		default:
			this.processOtherEvent(ev);
			break;
		}
	}

	protected void processEvents() {
		Sim_event ev = new Sim_event();

		super.sim_get_next(ev);
		this.processEvent(ev);
	}

	public GridletReply submitGridletToAgent(int dst_agentID, int dst_resID,
			Gridlet gridlet) {
		GridletRequest request = new GridletRequest(this.get_id(), this
				.get_id(), dst_agentID, dst_resID, gridlet);
		int requestID = request.getRequestID();
		int reqrepID = request.getReqrepID();
		super.send(super.output, GridSimTags.SCHEDULE_NOW,
				Tags.GRIDLET_SUBMIT_REQ, new IO_data(request, gridlet
						.getGridletFileSize(), request.getDst_agentID()));

		Sim_type_p ptag = new Sim_type_p(Tags.GRIDLET_SUBMIT_REP);
		Sim_event ev = new Sim_event();
		super.sim_get_next(ptag, ev); // only look for this type of ack
		GridletReply reply = GridletReply.get_data(ev);

		Assert.assertEquals(requestID, reply.getRequestID());
		Assert.assertEquals(Tags.GRIDLET_SUBMIT_REQ, reply.getRequestTAG());
		Assert.assertEquals(Tags.GRIDLET_SUBMIT_REP, ev.get_tag());
		double evrecv_time = GridSim.clock();
		if (reply.isOk()) {
			String msg = String
					.format(
							"%1$f %2$d %3$s <-- GOT GRIDLET_REPLY for Gridlet %4$d of Gridlet %5$d with result TRUE on AM_%6$s",
							evrecv_time, reqrepID, this.get_name(), reply
									.getReceivedGridlet().getGridletID(), reply
									.getRequest().getGridlet().getGridletID(),
							super.getEntityName(reply.getRequest()
									.getDst_resID()));
			this.write(msg);
		} else {
			String msg = String
					.format(
							"%1$f $2$d %3$s <-- GOT GRIDLET_REPLY for Gridlet %4$d of Gridlet %5$d with result FALSE on AM_%6$s",
							evrecv_time, reqrepID, this.get_name(), reply
									.getReceivedGridlet().getGridletID(), reply
									.getRequest().getGridlet().getGridletID(),
							super.getEntityName(reply.getRequest()
									.getDst_resID()));
			this.write(msg);
		}
		return reply;
	}

	public GridletReply askGridletStatusToAgent(int dst_agentID, int dst_resID,
			Gridlet gridlet) {
		GridletRequest request = new GridletRequest(this.get_id(), this
				.get_id(), dst_agentID, dst_resID, gridlet);
		int requestID = request.getRequestID();
		int reqrepID = request.getReqrepID();
		super.send(super.output, GridSimTags.SCHEDULE_NOW,
				Tags.GRIDLET_STATUS_REQ, new IO_data(request, gridlet
						.getGridletFileSize(), request.getDst_agentID()));

		Sim_type_p ptag = new Sim_type_p(Tags.GRIDLET_STATUS_REP);
		Sim_event ev = new Sim_event();
		super.sim_get_next(ptag, ev); // only look for this type of ack
		GridletReply gridletReply = GridletReply.get_data(ev);

		Assert.assertEquals(requestID, gridletReply.getRequestID());
		Assert.assertEquals(Tags.GRIDLET_STATUS_REQ, gridletReply
				.getRequestTAG());
		Assert.assertEquals(Tags.GRIDLET_STATUS_REP, ev.get_tag());
		double evrecv_time = GridSim.clock();
		String msg = String
				.format(
						"%1$f %2$d %3$s <-- GOT GRIDLET_STATUS_REP for Gridlet %4$d of Gridlet %5$d with status %6$d",
						evrecv_time, reqrepID, this.get_name(), gridletReply
								.getReceivedGridlet().getGridletID(),
						gridletReply.getRequest().getGridlet().getGridletID(),
						gridletReply.getRequest().getGridlet()
								.getGridletStatus());
		this.write(msg);
		return gridletReply;
	}

	public AgentReply testTestService() {
		double evsend_time = 0;
		int SIZE = 500;
		AgentRequest request = null;
		request = new AgentRequest(this.get_id(), this.get_id(), null, this
				.getVirtualOrganization().getAgentPlatform().getGridElement().get_id(),
				Sim_system.get_entity_id("TestService"),
				EntityTypes.AGENT_ZOMBIE, SIZE, this.getVirtualOrganization().getAgentPlatform()
						.getGridElement().get_id(), EntityTypes.NOBODY);
		int requestID = request.getRequestID();
		int reqrepID = request.getReqrepID();
		super.send(super.output, GridSimTags.SCHEDULE_NOW,
				Tags.TEST_SERVICE_REQ, new IO_data(request, SIZE, Sim_system
						.get_entity_id("TestService")));
		evsend_time = GridSim.clock();

		String msg = String.format("%1$f %2$d %3$s --> %4$s", evsend_time,
				reqrepID, this.get_name(), "TestService");
		this.write(msg);

		Sim_type_p ptag = new Sim_type_p(Tags.TEST_SERVICE_REP);
		Sim_event ev = new Sim_event();
		super.sim_get_next(ptag, ev); // only look for this type of ack
		AgentReply reply = AgentReply.get_data(ev);

		Assert.assertEquals(requestID, reply.getRequestID());
		Assert.assertEquals(Tags.TEST_SERVICE_REQ, reply.getRequestTAG());
		Assert.assertEquals(Tags.TEST_SERVICE_REP, ev.get_tag());

		double evrecv_time = GridSim.clock();

		msg = String.format("%1$f %2$d %3$s <-- %4$s", evrecv_time, reqrepID, this.get_name(),
				"TestService");
		this.write(msg);

		return reply;
	}

	AgentReply requestToAgent(AgentRequest request, int tag) {
		int requestID = request.getRequestID();
		int reqrepID = request.getReqrepID();
		double evsend_time = 0;
		int agentType = request.getDst_entityType();
		int SIZE;
		if (tag == Tags.AGENT_RUN_REQ) {
			SIZE = request.getDst_agentSize();
		} else {
			SIZE = 500;
		}
		if ((tag == Tags.AGENT_RUN_REQ) || (tag == Tags.AGENT_KILL_REQ)
				|| (tag == Tags.AGENT_KILLAWAIT_REQ)
				|| (tag == Tags.AGENT_PAUSE_REQ)
				|| (tag == Tags.AGENT_RESUME_REQ)) {
			super.send(super.output, GridSimTags.SCHEDULE_NOW, tag,
					new IO_data(request, SIZE, request.getDst_resID()));
		} else {
			super.send(super.output, GridSimTags.SCHEDULE_NOW, tag,
					new IO_data(request, SIZE, request.getDst_agentID()));
		}
		evsend_time = GridSim.clock();
		String msg = String
				.format(
						"%1$f %2$d %3$s --> AM_%4$s::REQUEST %6$s (%7$s AID %9$d) %5$s AM_%8$s",
						evsend_time, reqrepID, this.get_name(), super
								.getEntityName(request.getDst_resID()),
						EntityTypes.toString(request.getDst_entityType()), Tags
								.toString(tag), super.getEntityName(request
								.getDst_agentID()), super.getEntityName(request
								.getDst_moveToresID()), request.getDst_AID());
		this.write(msg);

		Sim_type_p ptag = new Sim_type_p(tag + 1);
		Sim_event ev = new Sim_event();
		super.sim_get_next(ptag, ev); // only look for this type of ack
		AgentReply agentReply = AgentReply.get_data(ev);

		Assert.assertEquals(requestID, agentReply.getRequestID());
		Assert.assertEquals(tag, agentReply.getRequestTAG());
		Assert.assertEquals(ev.get_tag(), tag + 1);

		double evrecv_time = GridSim.clock();
		msg = String
				.format(
						"%1$f %2$d %3$s <-- AM_%4$s::%7$s %8$s (%6$s AID %10$d) %5$s AM_%9$s",
						evrecv_time, reqrepID, this.get_name(), super
								.getEntityName(agentReply.getRequest()
										.getDst_resID()), EntityTypes
								.toString(agentType), super
								.getEntityName(request.getDst_agentID()),
						agentReply.isOk(), Tags.toString(tag), super
								.getEntityName(request.getDst_moveToresID()),
						request.getDst_AID());
		this.write(msg);
		return agentReply;
	}

	public AgentReply hasGridletsAgent(AgentRequest request, boolean trace) {
		int requestID = request.getRequestID();
		int reqrepID = request.getReqrepID();
		double evsend_time = 0;
		int agentType = request.getDst_entityType();
		int SIZE = 500;
		super.send(super.output, GridSimTags.SCHEDULE_NOW,
				Tags.HASGRIDLETS_REQUEST, new IO_data(request, SIZE, request
						.getDst_agentID()));
		evsend_time = GridSim.clock();
		String msg = String
				.format(
						"%1$f %2$d %3$s --> AM_%4$s::HASGRIDLETS_REQUEST %6$s (%7$s AID %9$d) %5$s AM_%8$s",
						evsend_time, reqrepID, this.get_name(), super
								.getEntityName(request.getDst_resID()),
						EntityTypes.toString(request.getDst_entityType()), Tags
								.toString(Tags.HASGRIDLETS_REQUEST), super
								.getEntityName(request.getDst_agentID()), super
								.getEntityName(request.getDst_moveToresID()),
						request.getDst_AID());
		if (trace) {
			this.write(msg);
		}

		Sim_type_p ptag = new Sim_type_p(Tags.HASGRIDLETS_REPLY);
		Sim_event ev = new Sim_event();
		super.sim_get_next(ptag, ev); // only look for this type of ack
		AgentReply agentReply = AgentReply.get_data(ev);

		Assert.assertEquals(requestID, agentReply.getRequestID());
		Assert.assertEquals(Tags.HASGRIDLETS_REQUEST, agentReply
				.getRequestTAG());
		Assert.assertEquals(ev.get_tag(), Tags.HASGRIDLETS_REPLY);

		double evrecv_time = GridSim.clock();
		msg = String
				.format(
						"%1$f %2$d %3$s <-- AM_%4$s::%7$s %8$s (%6$s AID %10$d) %5$s AM_%9$s",
						evrecv_time, reqrepID, this.get_name(), super
								.getEntityName(agentReply.getRequest()
										.getDst_resID()), EntityTypes
								.toString(agentType), super
								.getEntityName(request.getDst_agentID()),
						agentReply.isOk(), Tags
								.toString(Tags.HASGRIDLETS_REPLY), super
								.getEntityName(request.getDst_moveToresID()),
						request.getDst_AID());
		if (trace) {
			this.write(msg);
		}
		return agentReply;
	}

	public int getEntityType() {
		return this.entityType;
	}

	public SimReport getReport_() {
		return this.report_;
	}

	public FIFOScheduler getUserSched() {
		return this.userSched;
	}

	public AbstractVirtualOrganization getVirtualOrganization() {
		return this.virtualOrganization;
	}

	public void setEntityType(int entityType) {
		this.entityType = entityType;
	}

	public void setReport_(SimReport report_) {
		this.report_ = report_;
	}

	public void setUserSched(FIFOScheduler userSched) {
		this.userSched = userSched;
	}

	public void setVirtualOrganization(AbstractVirtualOrganization virtualOrganization) {
		this.virtualOrganization = virtualOrganization;
	}

	/**
	 * Prints out the given message into stdout. In addition, writes it into a
	 * file.
	 * 
	 * @param msg
	 *            a message
	 */
	protected void write(String msg) {
		System.out.println(msg);
		if (this.report_ != null) {
			this.report_.write(msg);
		}
	}

        public void initialize() throws Exception {
		// creates a report file
		if (this.isTraceFlag() == true) {
			this.setReport_(new SimReport(this.get_name()));
		}
        }
        
        public abstract void printStats();
        
	@Override
	public void body() {
		super.gridSimHold(GAP.getStartTime());
                try {
                this.initialize();
                } catch (Exception e) {
                    e.printStackTrace();
                }

		try {
			this.initWork();
		} catch (Exception e) {
			e.printStackTrace();
		}

		Sim_event ev = new Sim_event();
		while (GAP.isRunning()) {
			super.sim_wait_for(Sim_system.SIM_ANY, 1.0, ev);
			this.processEvent(ev);
			while (super.sim_waiting() > 0) {
				this.processEvents();
			}
		}

                this.printStats();
		// //////////////////////////////////////////////////////
		// shut down I/O ports
		this.shutdownUserEntity();
		this.terminateIOEntities();

		// don't forget to close the file
		if (this.getReport_() != null) {
			this.getReport_().finalWrite();
		}
	}

	public abstract void initWork();

    public boolean isTraceFlag() {
        return traceFlag;
    }

    public void setTraceFlag(boolean traceFlag) {
        this.traceFlag = traceFlag;
    }
}
