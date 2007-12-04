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
 * Agent.java
 *
 * Created on 9 March 2007, 12.00 by Giovanni Novelli
 *
 ****************************************************************************************
 *
 * $Revision: 1141 $
 * $Id: Agent.java 1141 2007-07-18 18:22:15Z gnovelli $
 * $HeadURL: file:///var/svn/grid/trunk/GAP/src/net/sf/gap/agents/Agent.java $
 *
 *****************************************************************************************
 */

package net.sf.gap.agents;

import junit.framework.Assert;
import net.sf.gap.constants.AgentStates;
import net.sf.gap.constants.EntityTypes;
import net.sf.gap.constants.Tags;
import net.sf.gap.agents.history.AgentHistory;
import net.sf.gap.agents.history.AgentHistoryEntry;
import net.sf.gap.messages.impl.AgentReply;
import net.sf.gap.messages.impl.AgentRequest;
import net.sf.gap.grid.components.GridElement;
import eduni.simjava.Sim_event;
import eduni.simjava.Sim_type_p;
import gridsim.GridSim;
import gridsim.GridSimTags;
import gridsim.IO_data;

public abstract class Agent extends AbstractAgent {

	public Agent(GridElement ge, String name, int agentSizeInBytes,
			boolean trace_flag) throws Exception {
		super(ge, name, agentSizeInBytes, trace_flag);
	}

    @Override
	public void initialize() throws Exception { // Agen's Initialization
            super.initialize();
        }
        
	abstract protected boolean hasGridlets();

	abstract protected void onWaitingGridlets(AgentRequest agentRequest);

	abstract protected void offWaitingGridlets();

	abstract protected void manageGridlets(Sim_event ev);

	/**
	 * @param ev
	 *            Sim_event to be processed by any child class
	 */
	@Override
	abstract public void processOtherEvent(Sim_event ev);

	/**
	 * Updates GridAgent's LifeCycle
	 * <p>
	 * It receives requests of the following types, checking if it is allowed
	 * according to permitted transition table:
	 * <p>
	 * <ul>
	 * <li> agent's activation ZOMBIE-->RUNNING
	 * <li> agent's killing RUNNING-->ZOMBIE
	 * <li> agent's pausing RUNNING-->PAUSED
	 * <li> agent's resuming PAUSED-->RUNNING <ul/>
	 * <p>
	 * Notifies the Directory Facilitator on the GridAgent Platform about state
	 * transition
	 * <p>
	 * Sends an ACK or a NACK about the success or failure of the request to the
	 * originator of it.
	 * 
	 * 
	 * @param ev
	 *            Sim_event containing the request
	 * @see eduni.simjava.Sim_event
	 * @see net.p2pgrid.gap.agents.messages.AgentRequest
	 */
	@Override
	protected void manageLifecycle(Sim_event ev) {
		AgentRequest agentRequest = AgentRequest.get_data(ev);
		if (this.hasGridlets() && ev.get_tag() == Tags.AGENT_KILL_REQ) {
			this.sendNACK(ev, agentRequest);
		} else if (this.hasGridlets()
				&& ev.get_tag() == Tags.AGENT_KILLAWAIT_REQ) {
			if (this.isGoodState(ev)) {
				this.update(ev, agentRequest);
				this.notifyDF(ev, agentRequest);
				this.onWaitingGridlets(agentRequest);
			}
		} else {
			if (this.isGoodState(ev)) {
				this.update(ev, agentRequest);
				this.notifyDF(ev, agentRequest);
				this.sendACK(ev, agentRequest);
			} else {
				this.sendNACK(ev, agentRequest);
			}
		}
	}

	/**
	 * Move an agent from a GE to another GE
	 * <p>
	 * Notifies the Directory Facilitator on the GridAgent Platform about
	 * movement
	 * <p>
	 * Keep AID on new GE
	 * <p>
	 * Manage failures of migration
	 * <p>
	 * Sends an ACK or a NACK about the success or failure of the request to the
	 * originator of it.
	 * 
	 * 
	 * 
	 * @param ev
	 *            Sim_event containing the request
	 * @see eduni.simjava.Sim_event
	 * @see net.p2pgrid.gap.agents.messages.AgentRequest
	 * @see net.p2pgrid.gap.agents.messages.AgentReply
	 */
	@Override
	protected void manageMigration(Sim_event ev) {
		AgentRequest agentRequest = AgentRequest.get_data(ev);
		if (this.isGoodState(ev)) {
			int SIZE = agentRequest.getDst_agentSize();
			AgentReply agentReply = this.submitAgent(agentRequest
					.getDst_entityType(), agentRequest.getDst_moveToresID(),
					SIZE, this.getAID(), this.getAgentHistory());
			if (agentReply.isOk()) {
				Assert.assertEquals(this.gridElement.getLocalDirectory()
						.getZombieAgents().contains(this.get_id()), false);

				/*
				 * @TODO Fix it This is a TRICK Missing message exchange with
				 * GridElement
				 */
				this.gridElement.getLocalDirectory().update(agentRequest,
						Tags.AGENT_MOVE_REQ);
				Assert.assertEquals(this.gridElement.getLocalDirectory()
						.getZombieAgents().contains(this.get_id()), true);

				this.notifyDFService(agentRequest, Tags.DF_DEREGISTER_REQ);
				this.setAgentState(AgentStates.ZOMBIE);
				this.setEntityType(EntityTypes.AGENT_ZOMBIE);
				this.setAgentSizeInBytes(0);
				this.detachAID();
				AgentHistoryEntry agentHistoryEntry = new AgentHistoryEntry(
						AbstractAgent.clock(), this.get_name(), this.getAgentState(),
						this.get_id(), this.getAID(), this.getEntityType(),
						this.getResourceID());
				this.getAgentHistory().add(agentHistoryEntry);
				int newaid = agentReply.getRequest().getDst_agentID();
				agentRequest.setDst_agentID(newaid);
				agentRequest.setDst_resID(agentRequest.getDst_moveToresID());
				this.sendACK(ev, agentRequest);
			} else {
				this.sendNACK(ev, agentRequest);
			}
		} else {
			this.sendNACK(ev, agentRequest);
		}
	}

	/**
	 * Submits an agent to a grid element
	 * 
	 * 
	 * @param agentEntityType
	 *            agent entity type
	 * @param agentResourceID
	 *            grid element id
	 * @param SIZE
	 *            agent size in bytes
	 * @return
	 * @see AgentReply
	 */
	protected AgentReply submitAgent(int agentEntityType, int agentResourceID,
			int SIZE) {
		AgentRequest agentRequest = null;
		agentRequest = new AgentRequest(this.get_id(), this.get_id(), null,
				agentResourceID, -1, agentEntityType, SIZE, agentResourceID,
				EntityTypes.NOBODY);
		return this.runAgent(agentRequest);	}

	/**
	 * Submits an agent to a grid element keeping AID and agent history
	 * 
	 * 
	 * @param agentEntityType
	 *            agent entity type
	 * @param agentResourceID
	 *            grid element id
	 * @param SIZE
	 *            agent size in bytes
	 * @param AID
	 *            AID
	 * @param agentHistory
	 *            agent history
	 * @return
	 * @see AgentReply
	 */
	private AgentReply submitAgent(int agentEntityType, int agentResourceID,
			int SIZE, int AID, AgentHistory agentHistory) {
		AgentRequest agentRequest = null;
		agentRequest = new AgentRequest(this.get_id(), this.getResourceID(),
				agentHistory, agentResourceID, -1, agentEntityType, SIZE,
				agentResourceID, AID);
		return this.runAgent(agentRequest);
	}

	/**
	 * Checks agent state
	 * 
	 * @param ev
	 *            event received
	 * @return true if agent state is compatible with event received
	 */
	private boolean isGoodState(Sim_event ev) {
		int agentState = this.getAgentState();
		switch (ev.get_tag()) {
		case Tags.AGENT_RUN_REQ:
			return agentState == AgentStates.ZOMBIE;
		case Tags.AGENT_KILL_REQ:
		case Tags.AGENT_KILLAWAIT_REQ:
			return (agentState == AgentStates.RUNNING);
		case Tags.AGENT_PAUSE_REQ:
			return agentState == AgentStates.RUNNING;
		case Tags.AGENT_RESUME_REQ:
			return agentState == AgentStates.PAUSED;

		case Tags.AGENT_MOVE_REQ:
			return ((agentState == AgentStates.RUNNING) || (agentState == AgentStates.PAUSED))
					&& (!this.hasGridlets());

		default:
			return false;
		}
	}

	/**
	 * Updates agent according to event received and embedded request
	 * 
	 * @param ev
	 *            event received
	 * @param agentRequest
	 *            request embedded in such event
	 */
	private void update(Sim_event ev, AgentRequest agentRequest) {
		AgentHistoryEntry agentHistoryEntry = null;
		switch (ev.get_tag()) {
		case Tags.AGENT_RUN_REQ:
			Assert.assertEquals(this.getAgentState(), AgentStates.ZOMBIE);
			this.setAgentState(AgentStates.RUNNING);

			this.setEntityType(agentRequest.getDst_entityType());
			this.setAgentSizeInBytes(agentRequest.getDst_agentSize());

			Assert.assertEquals(this.getAID() == EntityTypes.NOBODY, true);
			if (agentRequest.getDst_AID() == EntityTypes.NOBODY) {
				this.assignAID();
			} else {
				this.setAID(agentRequest.getDst_AID());
			}

			agentHistoryEntry = new AgentHistoryEntry(AbstractAgent.clock(), this
					.get_name(), this.getAgentState(), this.get_id(), this
					.getAID(), this.getEntityType(), this.getResourceID());
			if (agentRequest.getDst_AID() != EntityTypes.NOBODY) {
				this.getAgentHistory().addAll(
						agentRequest.getSrc_agentHistory());
			}
			this.getAgentHistory().add(agentHistoryEntry);

			agentRequest.setDst_AID(this.getAID());
			break;
		case Tags.AGENT_KILL_REQ:
		case Tags.AGENT_KILLAWAIT_REQ:
			Assert.assertEquals(this.getAgentState(), AgentStates.RUNNING);
			this.setAgentState(AgentStates.ZOMBIE);

			Assert.assertEquals(
					this.getEntityType() == EntityTypes.AGENT_ZOMBIE, false);
			this.setEntityType(EntityTypes.AGENT_ZOMBIE);

			this.setAgentSizeInBytes(0);

			Assert.assertEquals(this.getAID() == EntityTypes.NOBODY, false);
			this.detachAID();

			agentHistoryEntry = new AgentHistoryEntry(AbstractAgent.clock(), this
					.get_name(), this.getAgentState(), this.get_id(), this
					.getAID(), this.getEntityType(), this.getResourceID());
			this.getAgentHistory().add(agentHistoryEntry);
			agentRequest.setDst_AID(EntityTypes.NOBODY);
			break;
		case Tags.AGENT_PAUSE_REQ:
			Assert.assertEquals(this.getAgentState(), AgentStates.RUNNING);
			this.setAgentState(AgentStates.PAUSED);
			break;
		case Tags.AGENT_RESUME_REQ:
			Assert.assertEquals(this.getAgentState(), AgentStates.PAUSED);
			this.setAgentState(AgentStates.RUNNING);
			break;

		default:
			break;
		}
	}

	/**
	 * Notify DF service about request
	 * 
	 * @param ev
	 *            event received
	 * @param agentRequest
	 *            request embedded in such request
	 */
	private void notifyDF(Sim_event ev, AgentRequest agentRequest) {
		switch (ev.get_tag()) {
		case Tags.AGENT_RUN_REQ:
			this.notifyDFService(agentRequest, Tags.DF_REGISTER_REQ);
			break;
		case Tags.AGENT_KILL_REQ:
		case Tags.AGENT_KILLAWAIT_REQ:
			this.notifyDFService(agentRequest, Tags.DF_DEREGISTER_REQ);
			break;
		case Tags.AGENT_PAUSE_REQ:
			this.notifyDFService(agentRequest, Tags.DF_PAUSED_REQ);
			break;
		case Tags.AGENT_RESUME_REQ:
			this.notifyDFService(agentRequest, Tags.DF_RESUMED_REQ);
			break;
		default:
			break;
		}
	}

	protected AgentReply runAgent(AgentRequest agentRequest) {
		AgentReply agentReply = this.requestToAgent(agentRequest,
				Tags.AGENT_RUN_REQ);
		return agentReply;
	}

	protected AgentReply pauseAgent(AgentRequest agentRequest) {
		return this.requestToAgent(agentRequest, Tags.AGENT_PAUSE_REQ);
	}

	protected AgentReply resumeAgent(AgentRequest agentRequest) {
		return this.requestToAgent(agentRequest, Tags.AGENT_RESUME_REQ);
	}

	protected AgentReply killAgent(AgentRequest agentRequest) {
		return this.requestToAgent(agentRequest, Tags.AGENT_KILL_REQ);
	}

	protected AgentReply killWaitAgent(AgentRequest agentRequest) {
		return this.requestToAgent(agentRequest, Tags.AGENT_KILLAWAIT_REQ);
	}

	protected AgentReply moveAgent(AgentRequest agentRequest,
			int moveToResourceID) {
		agentRequest.setDst_moveToresID(moveToResourceID);
		return this.requestToAgent(agentRequest, Tags.AGENT_MOVE_REQ);
	}

	private AgentReply requestToAgent(AgentRequest request, int tag) {
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
						evsend_time, reqrepID, this.get_name(), AbstractAgent
								.getEntityName(request.getDst_resID()),
						EntityTypes.toString(request.getDst_entityType()), Tags
								.toString(tag), AbstractAgent.getEntityName(request
								.getDst_agentID()), AbstractAgent.getEntityName(request
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
						evrecv_time, 
                                                reqrepID,
                                                this.get_name(), AbstractAgent
								.getEntityName(agentReply.getRequest()
										.getDst_resID()), EntityTypes
								.toString(agentType), AbstractAgent
								.getEntityName(request.getDst_agentID()),
						agentReply.isOk(), Tags.toString(tag), AbstractAgent
								.getEntityName(request.getDst_moveToresID()),
						request.getDst_AID());
		this.write(msg);
		return agentReply;
        }

	private void sendACKNACK(Sim_event ev, AgentRequest agentRequest,
			boolean flag) {
		AgentReply agentReply = null;

		int SIZE = 500;

		int replyToID = 0;
		replyToID = agentRequest.getDst_resID();
		agentReply = new AgentReply(ev.get_tag(), flag, agentRequest);
		super.send(this.output, GridSimTags.SCHEDULE_NOW, ev.get_tag() + 1,
				new IO_data(agentReply, SIZE, replyToID));
	}

	protected void sendACK(Sim_event ev, AgentRequest agentRequest) {
		this.sendACKNACK(ev, agentRequest, true);
	}

	private void sendNACK(Sim_event ev, AgentRequest agentRequest) {
		this.sendACKNACK(ev, agentRequest, false);
	}

	private void notifyDFService(AgentRequest originalRequest, int dftag) {
		AgentRequest agentRequest = null;
		AgentReply agentReply = null;
		int SIZE = 500;
		agentRequest = new AgentRequest(originalRequest.getSrc_ID(),
				originalRequest.getSrc_resID(), originalRequest
						.getSrc_agentHistory(), originalRequest.getDst_resID(),
				this.get_id(), this.getEntityType(),
				this.getAgentSizeInBytes(), originalRequest.getDst_resID(),
				originalRequest.getDst_AID());
		int requestID = agentRequest.getRequestID();
		int reqrepID = agentRequest.getReqrepID();
		super.send(this.output, GridSimTags.SCHEDULE_NOW, dftag, new IO_data(
				agentRequest, SIZE, this.getAgentPlatform().get_id()));
		double evsend_time = GridSim.clock();
		String msg = String
				.format(
						"%1$f %2$d %3$s --> DFService::REQUEST %6$s (%7$s AID %8$d) %4$s AM_%5$s",
						evsend_time, reqrepID, this.get_name(), EntityTypes
								.toString(originalRequest.getDst_entityType()),
						AbstractAgent.getEntityName(originalRequest.getDst_resID()),
						Tags.toString(dftag),
						AbstractAgent.getEntityName(originalRequest.getDst_agentID()),
						agentRequest.getDst_AID());
		this.write(msg);

		Sim_type_p tag = new Sim_type_p(dftag + 1);
		Sim_event ev = new Sim_event();
		super.sim_get_next(tag, ev); // only look for this type of ack
		agentReply = AgentReply.get_data(ev);
		Assert.assertEquals(requestID, agentReply.getRequestID());
		Assert.assertEquals(dftag, agentReply.getRequestTAG());
		Assert.assertEquals(dftag + 1, ev.get_tag());
		double evrecv_time = GridSim.clock();
		msg = String
				.format(
						"%1$f %2$d %3$s <-- DFService::DFSERVICE_ACK %6$s (%7$s AID %8$d) %4$s AM_%5$s",
						evrecv_time, reqrepID, this.get_name(), EntityTypes
								.toString(originalRequest.getDst_entityType()),
						AbstractAgent.getEntityName(originalRequest.getDst_resID()),
						Tags.toString(dftag),
						AbstractAgent.getEntityName(originalRequest.getDst_agentID()),
						originalRequest.getDst_AID());
		this.write(msg);
	}
}