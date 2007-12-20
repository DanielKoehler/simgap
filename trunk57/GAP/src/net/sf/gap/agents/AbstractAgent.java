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
import gridsim.datagrid.DataGridUser;
import gridsim.util.SimReport;
import net.sf.gap.constants.AgentStates;
import net.sf.gap.constants.EntityTypes;
import net.sf.gap.constants.Tags;
import net.sf.gap.agents.history.AgentHistory;
import net.sf.gap.agents.middleware.AgentPlatform;
import net.sf.gap.grid.components.GridElement;
import net.sf.gap.grid.factories.LinkFactory;
import net.sf.gap.util.EntitiesCounter;

/**
 * This abstract class is responsible for simulating agent's basic behaviour.
 * 
 * @author Giovanni Novelli
 */
public abstract class AbstractAgent extends DataGridUser {
    /**
     * Boolean flag used to enable(true)/disable(false) entity's tracing
     */
    private boolean traceFlag;
    /**
     * Used for SimJava's reporting
     */
    private SimReport report_;
    /**
     * Used to identify entity's type
     */
    private int entityType;
    /**
     * Used to quantify agent's transfer size in bytes
     */
    private int agentSizeInBytes;
    /**
     * Used to track agent's state
     */
    private int agentState;
    /**
     * Used to map agent to a resource (Grid Element) with a SimJava's ID
     */
    private int resourceID;
    /**
     * Used to map agent to a Grid Element with a reference to an istance of
     * GridElement class
     */
    protected GridElement gridElement;
    /**
     * Used to map agent to a specific Agent Platform
     * Useful to have multiple Agent Platforms running in a simulation
     * Needed by an agent to refer to the correct SimJava's ID
     * for communications with its Agent Platform
     */
    private AgentPlatform agentPlatform;
    /**
     * Unique Agent ID which follows agent through its whole lifecycle in a
     * simulation and is independent from agent's localization
     */
    private int AID;
    /**
     * Agent's History useful to track agent's state, entity type and 
     * localizations through a whole simulation
     */
    private AgentHistory agentHistory;

    /**
     * AbstractAgent constructor
     * @param ge GridElement associated to an agent
     * @param name Agent's name
     * @param agentSizeInBytes Agent's transfer size in bytes
     * @param trace_flag Flag used to enable(true)/disable(false) tracing
     * @throws java.lang.Exception 
     */
    public AbstractAgent(GridElement ge, String name, int agentSizeInBytes,
            boolean trace_flag) throws Exception {
        super(name, LinkFactory.getAgentLink(name));
        this.setTraceFlag(trace_flag);
        this.setGridElement(ge);
        this.setAgentSizeInBytes(agentSizeInBytes);
        this.setAgentPlatform(this.getGridElement().getAgentPlatform());
        this.setResourceID(this.getGridElement().get_id());
        this.setEntityType(EntityTypes.AGENT_ZOMBIE);
        this.setAgentState(AgentStates.ZOMBIE);
        this.setAID(EntityTypes.NOBODY);

        EntitiesCounter.create("AID");
    }

    /**
     * Agent's initialization
     * @throws java.lang.Exception
     */
    public void initialize() throws Exception { 
        // creates a report file
        if (this.isTraceFlag() == true) {
            this.setReport_(new SimReport(this.get_name()));
        }
        this.setAgentHistory(new AgentHistory());
    }

    /**
     * Agent's disposal
     */
    abstract protected void dispose();

    /**
     * Manages agent's lifecycle
     * @param ev Event triggering agent's lifecycle transitions
     */
    abstract protected void manageLifecycle(Sim_event ev);

    /**
     * Manages agent's migration
     * @param ev Event triggering agent's migration
     */
    abstract protected void manageMigration(Sim_event ev);

    /**
     * Method used to extend agent's behaviour
     * @param ev Event triggering agent's actions
     */
    abstract public void processOtherEvent(Sim_event ev);

    /**
     * @param ev Sim_event to be processed
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

    /**
     * Method used to process all events received by an agent
     */
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
     * Detach AID from agent
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

    /**
     * 
     * @param agentPlatform
     */
    protected void setAgentPlatform(AgentPlatform agentPlatform) {
        this.agentPlatform = agentPlatform;
    }
    
    /**
     * 
     * @param agentSizeInBytes
     */
    protected void setAgentSizeInBytes(int agentSizeInBytes) {
        this.agentSizeInBytes = agentSizeInBytes;
    }

    /**
     * 
     * @param agentState
     */
    protected void setAgentState(int agentState) {
        this.agentState = agentState;
    }

    /**
     * 
     * @param entityType
     */
    protected void setEntityType(int entityType) {
        this.entityType = entityType;
    }
    
    /**
     * 
     * @param report_
     */
    protected void setReport_(SimReport report_) {
        this.report_ = report_;
    }

    /**
     * 
     * @param resourceID
     */
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

    /**
     * 
     * @param gridElement
     */
    protected void setGridElement(GridElement gridElement) {
        this.gridElement = gridElement;
    }

    /**
     * 
     * @return AID
     */
    protected int getAID() {
        return this.AID;
    }

    /**
     * 
     * @param AID
     */
    protected void setAID(int AID) {
        this.AID = AID;
    }

    /**
     * 
     * @return Agent's History
     */
    protected AgentHistory getAgentHistory() {
        return this.agentHistory;
    }

    /**
     * 
     * @param agentHistory
     */
    protected void setAgentHistory(AgentHistory agentHistory) {
        this.agentHistory = agentHistory;
    }

    /**
     * 
     * @return agent's GridElement
     */
    protected GridElement getGridElement() {
        return gridElement;
    }

    /**
     * Writes history to standard output
     */
    protected void writeHistory() {
        int ne = this.getAgentHistory().size();
        for (int i = 0; i < ne; i++) {
            String msg = this.getAgentHistory().get(i).toString(this.get_name());
            this.getReport_().write(msg);
        }
    }

    /**
     * 
     * @return
     */
    public boolean isTraceFlag() {
        return traceFlag;
    }

    /**
     * 
     * @param traceFlag
     */
    public void setTraceFlag(boolean traceFlag) {
        this.traceFlag = traceFlag;
    }
}
