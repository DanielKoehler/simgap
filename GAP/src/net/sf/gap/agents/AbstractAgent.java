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
 * AbstractAgent.java
 *
 * Created on 10 March 2007, 12.00 by Giovanni Novelli
 *
 ****************************************************************************************
 *
 * $Revision: 1141 $
 * $Id: AbstractAgent.java 1141 2007-07-18 18:22:15Z gnovelli $
 * $HeadURL: file:///var/svn/grid/trunk/GAP/src/net/sf/gap/agents/AbstractAgent.java $
 *
 *****************************************************************************************
 */
package net.sf.gap.agents;

import eduni.simjava.Sim_event;
import eduni.simjava.Sim_system;
import gridsim.datagrid.DataGridUser;
import gridsim.util.SimReport;
import net.sf.gap.GAP;
import net.sf.gap.constants.AgentStates;
import net.sf.gap.constants.EntityTypes;
import net.sf.gap.constants.Tags;
import net.sf.gap.agents.history.AgentHistory;
import net.sf.gap.agents.middleware.AgentPlatform;
import net.sf.gap.grid.components.GridElement;
import net.sf.gap.grid.factories.LinkFactory;
import net.sf.gap.util.EntitiesCounter;

public abstract class AbstractAgent extends DataGridUser {
        private boolean traceFlag;

	private SimReport report_;

	private int entityType;

	private int agentSizeInBytes;

	private int agentState;

	private int resourceID;

	protected GridElement gridElement;

	private AgentPlatform agentPlatform;

	private int AID;

	private AgentHistory agentHistory;

	public AbstractAgent(GridElement ge, String name, int agentSizeInBytes,
			boolean trace_flag) throws Exception {
		super(name, LinkFactory.getAgentLink(name));
		this.setGridElement(ge);
		this.setAgentPlatform(this.getGridElement().getAgentPlatform());
		this.setResourceID(this.getGridElement().get_id());
		this.setEntityType(EntityTypes.AGENT_ZOMBIE);
		this.setAgentState(AgentStates.ZOMBIE);
		this.setAID(EntityTypes.NOBODY);

                EntitiesCounter.create("AID");
	}

	public void initialize() throws Exception { // Agen's Initialization
                // creates a report file
		if (this.isTraceFlag() == true) {
			this.setReport_(new SimReport(this.get_name()));
		}
		this.setAgentHistory(new AgentHistory());
        }

	abstract protected void dispose(); // Agent's Disposal

	abstract protected void manageLifecycle(Sim_event ev);

	abstract protected void manageMigration(Sim_event ev);

	abstract public void processOtherEvent(Sim_event ev);

	/**
	 * @param ev
	 *            Sim_event to be processed
	 */
	public void processEvent(Sim_event ev) {
		switch (ev.get_tag()) {
		case Tags.AGENT_RUN_REQ:
		case Tags.AGENT_KILL_REQ:
		case Tags.AGENT_KILLAWAIT_REQ:
		case Tags.AGENT_PAUSE_REQ:
		case Tags.AGENT_RESUME_REQ:
			this.manageLifecycle(ev);
			break;

		case Tags.AGENT_MOVE_REQ:
			this.manageMigration(ev);
			break;

		default:
			break;
		}
	}

	protected void processEvents() {
		Sim_event ev = new Sim_event();

		super.sim_get_next(ev);
		this.processEvent(ev);
	}

	/**
	 * Assigns an AID to agent
	 * 
	 * @return assigned AID
	 */
	protected int assignAID() {
		int newAID = EntitiesCounter.inc("AID");
		this.setAID(newAID);
		return newAID;
	}

	/**
	 * detach AID from agent
	 */
	protected void detachAID() {
		this.setAID(EntityTypes.NOBODY);
	}

	/**
	 * Gets agent platform
	 * 
	 * 
	 * @return
	 * @see AgentPlatform
	 */
	protected AgentPlatform getAgentPlatform() {
		return this.agentPlatform;
	}

	/**
	 * @return agent size in bytes
	 */
	protected int getAgentSizeInBytes() {
		return this.agentSizeInBytes;
	}

	/**
	 * @return agent state
	 */
	protected int getAgentState() {
		return this.agentState;
	}

	/**
	 * @return agent entity type
	 */
	protected int getEntityType() {
		return this.entityType;
	}

	/**
	 * @return agent report
	 */
	protected SimReport getReport_() {
		return this.report_;
	}

	/**
	 * @return agent grid element
	 */
	protected int getResourceID() {
		return this.resourceID;
	}

	protected void setAgentPlatform(AgentPlatform agentPlatform) {
		this.agentPlatform = agentPlatform;
	}

	protected void setAgentSizeInBytes(int agentSizeInBytes) {
		this.agentSizeInBytes = agentSizeInBytes;
	}

	protected void setAgentState(int agentState) {
		this.agentState = agentState;
	}

	protected void setEntityType(int entityType) {
		this.entityType = entityType;
	}

	protected void setReport_(SimReport report_) {
		this.report_ = report_;
	}

	protected void setResourceID(int resourceID) {
		this.resourceID = resourceID;
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

	protected void setGridElement(GridElement gridElement) {
		this.gridElement = gridElement;
	}

	protected int getAID() {
		return this.AID;
	}

	protected void setAID(int AID) {
		this.AID = AID;
	}

	protected AgentHistory getAgentHistory() {
		return this.agentHistory;
	}

	protected void setAgentHistory(AgentHistory agentHistory) {
		this.agentHistory = agentHistory;
	}

	protected GridElement getGridElement() {
		return gridElement;
	}

	protected void writeHistory() {
		int ne = this.getAgentHistory().size();
		for (int i = 0; i < ne; i++) {
			String msg = this.getAgentHistory().get(i)
					.toString(this.get_name());
			this.getReport_().write(msg);
		}
	}

    public boolean isTraceFlag() {
        return traceFlag;
    }

    public void setTraceFlag(boolean traceFlag) {
        this.traceFlag = traceFlag;
    }

}