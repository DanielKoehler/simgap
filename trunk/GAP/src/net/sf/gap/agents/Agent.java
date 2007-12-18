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

/**
 * This abstract class is mainly responsible in simulating basic 
 * behaviour of agents in relation to:
 * <p>
 * <ul>
 * <li> GridAgent Platform (Directory Facilitator)
 * 
 * <li> GridAgent LifeCycle over Grid's CEs/SEs
 * 
 * <li> GridAgent's Mobility
 * <ul/>
 * 
 * @author Giovanni Novelli
 */
public abstract class Agent extends AbstractAgent {

    /**
     * Agent constructor
     * @param ge GridElement associated to an agent
     * @param name Agent's name
     * @param agentSizeInBytes Agent's transfer size in bytes
     * @param trace_flag Flag used to enable(true)/disable(false) tracing
     * @throws java.lang.Exception 
     * 
     * @see net.sf.gap.grid.components.GridElement
     */
    public Agent(
            GridElement ge, 
            String name, 
            int agentSizeInBytes,
            boolean trace_flag) 
            throws Exception 
    {
        super(ge, name, agentSizeInBytes, trace_flag);
    }

    /**
     * Agent's Initialization
     * @throws java.lang.Exception
     */
    @Override
    public void initialize() 
            throws Exception {
        super.initialize();
    }

    /**
     * @return true if there are gridlets carried by agent, false otherwise
     */
    abstract protected boolean hasGridlets();

    /**
     * @param agentRequest
     */
    abstract protected void onWaitingGridlets(AgentRequest agentRequest);

    /**
     * @param ev
     */
    abstract protected void manageGridlets(Sim_event ev);

    /**
     * @param ev Sim_event to be processed by any child class
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
     * <li> agent's activation  ZOMBIE-->RUNNING
     * <li> agent's killing     RUNNING-->ZOMBIE
     * <li> agent's pausing     RUNNING-->PAUSED
     * <li> agent's resuming    PAUSED-->RUNNING 
     * <ul/>
     * <p>
     * Notifies the Directory Facilitator on the GridAgent Platform about state
     * transition
     * <p>
     * Sends an ACK or a NACK about the success or failure of the request to the
     * originator of it.
     * 
     * 
     * @param ev Sim_event containing the request
     * @see eduni.simjava.Sim_event
     * @see net.p2pgrid.gap.agents.messages.AgentRequest
     */
    @Override
    protected void manageLifecycle(Sim_event ev) 
    {
        AgentRequest agentRequest = AgentRequest.get_data(ev);

        // If this agent has gridlets and has received a KILL request
        // then send a NACK and do nothing
        if (this.hasGridlets() && ev.get_tag() == Tags.AGENT_KILL_REQ) {
            this.sendNACK(ev, agentRequest);

        // If this agent has gridlets and has received a delayed
        // KILL request then
        } else if (this.hasGridlets() && ev.get_tag() == Tags.AGENT_KILLAWAIT_REQ) {
            // if agent's state is congruent with the request
            if (this.isGoodState(ev)) {
                // update agent's internals
                this.update(ev, agentRequest);
                // notify DF about agent's state change
                this.notifyDF(ev, agentRequest);
                // wait until the end of current gridlets
                // Waiting current gridlets is done at last because agent
                // won't be available anyway as delayed KILL keeps same
                // semanthics of KILL even if with greater latency
                this.onWaitingGridlets(agentRequest);
            }
        // otherwise, if this agent has NOT gridlets then
        } else {
            // if agent's state is congruent with the request
            if (this.isGoodState(ev)) {
                // update agent's internals
                this.update(ev, agentRequest);
                // notify DF about agent's state change
                this.notifyDF(ev, agentRequest);
                // send ACK
                this.sendACK(ev, agentRequest);
            // otherwise, if agent's state is NOT congruent with the
            // request
            } else {
                // send NACK
                this.sendNACK(ev, agentRequest);
            }
        }
    }

    /**
     * Moves an agent from a Grid Element to another Grid Element
     * <p>
     * Notifies the Directory Facilitator on the GridAgent Platform about
     * migration
     * <p>
     * Keeps AID on new Grid Element
     * <p>
     * Manages migration failures
     * <p>
     * Sends an ACK or a NACK about the success or failure of the request
     * 
     * @param ev Sim_event containing the request
     * 
     * @see eduni.simjava.Sim_event
     * @see net.p2pgrid.gap.agents.messages.AgentRequest
     * @see net.p2pgrid.gap.agents.messages.AgentReply
     * @see net.sf.gap.agents.history.AgentHistory
     * @see net.sf.gap.agents.history.AgentHistoryEntry
     */
    @Override
    protected void manageMigration(Sim_event ev) 
    {
        // Extracts AgentRequest from ev
        AgentRequest agentRequest = AgentRequest.get_data(ev);
        // if agent's state is congruent with the request
        if (this.isGoodState(ev)) {
            // Stores in local variable SIZE the transfer size needed by migration
            int SIZE = agentRequest.getDst_agentSize();
            // Stores in local variable oldaid agent's AID
            int oldaid = this.getAID();
            // Submits an agent to the destination Grid Element; such agent has:
            // - same entity type
            // - same AID
            // - current agent's history
            // - same SIZE
            // Submission could be successful or not
            AgentReply agentReply = this.submitAgent(agentRequest.getDst_entityType(), agentRequest.getDst_moveToresID(),
                    SIZE, this.getAID(), this.getAgentHistory());
            // If agent's submission is successful then
            if (agentReply.isOk()) {
                // Putting agent in local zombies list
                // 1. Checks if agent is NOT in the zombies list of its original Grid Element
                Assert.assertEquals(this.gridElement.getLocalDirectory().getZombieAgents().contains(this.get_id()), false);
                /*
                 * @TODO Fix it This is a TRICK Missing message exchange with GridElement
                 */
                // 2. Updates local zombies list adding agent to zombies
                this.gridElement.getLocalDirectory().update(agentRequest,Tags.AGENT_MOVE_REQ);
                // 3. Checks if agent is in the zombies list of its original Grid Element
                Assert.assertEquals(this.gridElement.getLocalDirectory().getZombieAgents().contains(this.get_id()), true);

                // Notifies Directory Facilitator that original agent must be DEREGISTERED
                this.notifyDFService(agentRequest, Tags.DF_DEREGISTER_REQ);
                // Sets agent's state to ZOMBIE
                this.setAgentState(AgentStates.ZOMBIE);
                // Sets agent's entity type to AGENT_ZOMBIE
                this.setEntityType(EntityTypes.AGENT_ZOMBIE);
                // Resets agent's transfer SIZE to 0 bytes
                this.setAgentSizeInBytes(0);
                // Detach AID from zombied agent
                this.detachAID();
                // Creates an empty agent's history entry
                AgentHistoryEntry agentHistoryEntry = new AgentHistoryEntry(
                        AbstractAgent.clock(), this.get_name(), this.getAgentState(),
                        this.get_id(), this.getAID(), this.getEntityType(),
                        this.getResourceID());
                // Attach such history entry to zombied agent's history
                this.getAgentHistory().add(agentHistoryEntry);
                // Stores new (should be the same of the original) AID in local
                // variable newaid
                int newaid = agentReply.getRequest().getDst_agentID();
                // Checks that AID hasn't changed with migration
                Assert.assertEquals(oldaid==newaid,true);
                // Puts new AID in original request
                agentRequest.setDst_agentID(newaid);
                // Puts new resource ID in original request
                agentRequest.setDst_resID(agentRequest.getDst_moveToresID());
                // Sends ACK and updated request to its originator
                this.sendACK(ev, agentRequest);
            // otherwise, if agent's submission was NOT successful
            } else {
                // Sends NACK and original request to its originator
                this.sendNACK(ev, agentRequest);
            }
        // otherwise, if agent's state is congruent with the request
        } else {
            // Sends NACK and original request to its originator
            this.sendNACK(ev, agentRequest);
        }
    }

    /**
     * This method allows an agent to submit a new agent to a Grid Element
     * 
     * @param agentEntityType new agent's entity type
     * @param agentResourceID destination Grid Element's resource ID
     * @param SIZE new agent's transfer size in bytes
     * @return an instance of class AgentReply
     * 
     * @see net.p2pgrid.gap.agents.messages.AgentRequest
     * @see net.p2pgrid.gap.agents.messages.AgentReply
     */
    protected AgentReply submitAgent(
            int agentEntityType, 
            int agentResourceID,
            int SIZE) 
    {
        AgentRequest agentRequest = null;
        // Creates a new instance of AgentRequest where:
        // - actor ID is the ID of this agent
        // - resource ID is the resource ID of this agent
        // - agent's history is null (will be instantiated on destination)
        // - new agent's destination resource ID is specified by actual 
        //   parameter for agentResourceID
        // - new agent's ID is unknown
        // - new agent's entity type is specified by actual parameter 
        //   for agentEntityType
        // - new agent's transfer size in bytes is specified by actual parameter
        //   for SIZE
        // - new agent's AID is actually unknown so it's set to NOBODY
        agentRequest = new AgentRequest(this.get_id(), this.get_id(), null,
                agentResourceID, -1, agentEntityType, SIZE, agentResourceID,
                EntityTypes.NOBODY);
        // Passes the new instance of AgentRequest to method runAgent
        // because method runAgent is more general than method submitAgent
        return this.runAgent(agentRequest);
    }

    /**
     * This method allows an agent to clone itself to another Grid Element
     * keeping AID and agent history
     * @TODO What happens to agent's gridlets?!?
     * 
     * @param agentEntityType agent entity type
     * @param agentResourceID Grid Element's resource ID
     * @param SIZE agent's transfer size in bytes
     * @param AID  agent's AID
     * @param agentHistory agent's history
     * @return an instance of class AgentReply
     * 
     * @see net.p2pgrid.gap.agents.messages.AgentRequest
     * @see net.p2pgrid.gap.agents.messages.AgentReply
     * @see net.sf.gap.agents.history.AgentHistoryEntry
     */
    private AgentReply submitAgent(
            int agentEntityType, 
            int agentResourceID,
            int SIZE, 
            int AID, 
            AgentHistory agentHistory) 
    {
        AgentRequest agentRequest = null;
        agentRequest = new AgentRequest(this.get_id(), this.getResourceID(),
                agentHistory, agentResourceID, -1, agentEntityType, SIZE,
                agentResourceID, AID);
        return this.runAgent(agentRequest);
    }

    /**
     * Checks if current agent's state is congruent with a request
     * 
     * @param ev event received
     * @return true if agent state is compatible with event received
     * 
     * @see eduni.simjava.Sim_event
     */
    private boolean isGoodState(Sim_event ev) 
    {
        int agentState = this.getAgentState();
        switch (ev.get_tag()) {
            // Allowed state's transitions
            // ZOMBIE  --> RUNNING
            case Tags.AGENT_RUN_REQ:
                return agentState == AgentStates.ZOMBIE;
            // RUNNING --> ZOMBIE
            case Tags.AGENT_KILL_REQ:
            case Tags.AGENT_KILLAWAIT_REQ:
                return (agentState == AgentStates.RUNNING);
            // RUNNING --> PAUSED
            case Tags.AGENT_PAUSE_REQ:
                return agentState == AgentStates.RUNNING;
            // PAUSED  --> RUNNING
            case Tags.AGENT_RESUME_REQ:
                return agentState == AgentStates.PAUSED;
            // @TODO Check what happens to agent's state with MOVE and what is preferable to happen
            // PAUSED  --> MOVE --> RUNNING(?)
            // RUNNING --> MOVE --> RUNNING
            case Tags.AGENT_MOVE_REQ:
                return ((agentState == AgentStates.RUNNING) || (agentState == AgentStates.PAUSED)) && (!this.hasGridlets());
            default:
                return false;
        }
    }

    /**
     * Updates agent according to event received and embedded request
     * 
     * @param ev event received
     * @param agentRequest the request embedded in such event
     * 
     * @see eduni.simjava.Sim_event
     * @see net.p2pgrid.gap.agents.messages.AgentRequest
     * @see net.sf.gap.agents.history.AgentHistory
     * @see net.sf.gap.agents.history.AgentHistoryEntry
     */
    private void update(
            Sim_event ev, 
            AgentRequest agentRequest) 
    {
        AgentHistoryEntry agentHistoryEntry = null;
        switch (ev.get_tag()) {
            // Agent has received a RUN request
            case Tags.AGENT_RUN_REQ:
                // Checks that current agent's state is ZOMBIE
                Assert.assertEquals(this.getAgentState(), AgentStates.ZOMBIE);
                // Sets new agent's state to RUNNING
                this.setAgentState(AgentStates.RUNNING);
                // Updates agent's entity type
                this.setEntityType(agentRequest.getDst_entityType());
                // Updates agent's transfer size
                this.setAgentSizeInBytes(agentRequest.getDst_agentSize());
                // Checks that new agent has NOT yet an AID
                Assert.assertEquals(this.getAID() == EntityTypes.NOBODY, true);
                // if the new agent has NOT yet an AID
                if (agentRequest.getDst_AID() == EntityTypes.NOBODY) {
                    // assigns a new AID 
                    this.assignAID();
                // otherwise, if the new agent has an old AID from a migration
                } else {
                    // Sets agent's AID to the AID in 
                    // agentRequest's destination AID
                    this.setAID(agentRequest.getDst_AID());
                }

                // Update agent's history with the following information:
                // - current simulation's clock
                // - new agent's name
                // - new agent's state
                // - new agent's ID
                // - new agent's AID
                // - new agent's entity type
                // - new agent's resource ID
                agentHistoryEntry = 
                        new AgentHistoryEntry(
                        AbstractAgent.clock(), 
                        this.get_name(), 
                        this.getAgentState(), 
                        this.get_id(), 
                        this.getAID(), 
                        this.getEntityType(), 
                        this.getResourceID());
                // if agent is NOT really new (AID!=NOBODY) but the result
                // of a migration of an existing agent
                if (agentRequest.getDst_AID() != EntityTypes.NOBODY) {
                    // carry with migrated agent its old agent's history
                    this.getAgentHistory().addAll(agentRequest.getSrc_agentHistory());
                }
                // Updates agent's history with the new history entry
                this.getAgentHistory().add(agentHistoryEntry);
                // Updates agentRequest with current agent's AID
                agentRequest.setDst_AID(this.getAID());
                break;
            // Agent has received a KILL request
            case Tags.AGENT_KILL_REQ:
            // Agent has received a delayed KILL request
            case Tags.AGENT_KILLAWAIT_REQ:
                // Checks that current agent's state is RUNNING
                Assert.assertEquals(this.getAgentState(), AgentStates.RUNNING);
                // Sets agent's state to ZOMBIE
                this.setAgentState(AgentStates.ZOMBIE);
                // Checks that agent's entity type is NOT AGENT_ZOMBIE before
                // fulfilling current KILL request
                Assert.assertEquals(this.getEntityType() == EntityTypes.AGENT_ZOMBIE, 
                                    false);
                // Sets agent's entity type to AGENT_ZOMBIE
                this.setEntityType(EntityTypes.AGENT_ZOMBIE);
                // Resets agent's transfer size to 0 bytes
                this.setAgentSizeInBytes(0);
                // Checks that agent had an AID before fulfilling 
                // current KILL request
                Assert.assertEquals(this.getAID() == EntityTypes.NOBODY, false);
                // Detach AID from agent
                this.detachAID();

                // Update agent's history with the following information:
                // - current simulation's clock
                // - agent's name
                // - agent's state
                // - agent's ID
                // - agent's AID
                // - agent's entity type
                // - agent's resource ID
                agentHistoryEntry = 
                        new AgentHistoryEntry(
                        AbstractAgent.clock(), 
                        this.get_name(), 
                        this.getAgentState(), 
                        this.get_id(), 
                        this.getAID(), 
                        this.getEntityType(), 
                        this.getResourceID());
                // Updates agent's history with the new history entry
                this.getAgentHistory().add(agentHistoryEntry);
                // Sets agent's AID to NOBODY
                agentRequest.setDst_AID(EntityTypes.NOBODY);
                break;
            // Agent has received a PAUSE request
            case Tags.AGENT_PAUSE_REQ:
                // Checks that agent's state is RUNNING
                Assert.assertEquals(this.getAgentState(), AgentStates.RUNNING);
                // Sets agent's state to PAUSED
                this.setAgentState(AgentStates.PAUSED);
                break;
            // Agent has received a RESUME request
            case Tags.AGENT_RESUME_REQ:
                // Checks that agent's state is PAUSED
                Assert.assertEquals(this.getAgentState(), AgentStates.PAUSED);
                // Sets agent's state to RUNNING
                this.setAgentState(AgentStates.RUNNING);
                break;
            default:
                break;
        }
    }

    /**
     * Notify DF service about agent's request
     * 
     * @param ev event received
     * @param agentRequest request embedded in such request
     * 
     * @see eduni.simjava.Sim_event
     * @see net.p2pgrid.gap.agents.messages.AgentRequest
     */
    private void notifyDF(
            Sim_event ev, 
            AgentRequest agentRequest) 
    {
        switch (ev.get_tag()) {
            // Agent has received a RUN request
            case Tags.AGENT_RUN_REQ:
                // Notifies DF to REGISTER agent
                this.notifyDFService(agentRequest, Tags.DF_REGISTER_REQ);
                break;
            // Agent has received a KILL request
            case Tags.AGENT_KILL_REQ:
             // Agent has received a delayed KILL request
             case Tags.AGENT_KILLAWAIT_REQ:
                // Notifies DF to DEREGISTER agent
                this.notifyDFService(agentRequest, Tags.DF_DEREGISTER_REQ);
                break;
            // Agent has received a PAUSE request
            case Tags.AGENT_PAUSE_REQ:
                // Notifies DF to PAUSE agent
                this.notifyDFService(agentRequest, Tags.DF_PAUSED_REQ);
                break;
            // Agent has received a RESUME request
            case Tags.AGENT_RESUME_REQ:
                // Notifies DF to RESUME agent
                this.notifyDFService(agentRequest, Tags.DF_RESUMED_REQ);
                break;
            default:
                break;
        }
    }

    /**
     * This method is responsible for all submissions of new or
     * migrated agents
     * 
     * @param agentRequest an instance of AgentRequest class specifying parameters of new or migrated agent
     * @return an instance of AgentReply class
     *
     * @see net.p2pgrid.gap.agents.messages.AgentRequest
     * @see net.p2pgrid.gap.agents.messages.AgentReply
     */
    protected AgentReply runAgent(AgentRequest agentRequest) 
    {
        AgentReply agentReply = 
                this.requestToAgent(
                agentRequest,
                Tags.AGENT_RUN_REQ);
        return agentReply;
    }

    /**
     * This method is responsible to send a PAUSE request to an existing agent
     * 
     * @param agentRequest an instance of AgentRequest class specifying parameters the agent to pause
     * @return an instance of AgentReply class
     * 
     * @see net.p2pgrid.gap.agents.messages.AgentRequest
     * @see net.p2pgrid.gap.agents.messages.AgentReply
     */
    protected AgentReply pauseAgent(AgentRequest agentRequest) {
        return this.requestToAgent(agentRequest, Tags.AGENT_PAUSE_REQ);
    }

    /**
     * This method is responsible to send a RESUME request to an existing agent
     * 
     * @param agentRequest an instance of AgentRequest class specifying parameters the agent to resume
     * @return an instance of AgentReply class
     * 
     * @see net.p2pgrid.gap.agents.messages.AgentRequest
     * @see net.p2pgrid.gap.agents.messages.AgentReply
     */
    protected AgentReply resumeAgent(AgentRequest agentRequest) 
    {
        return this.requestToAgent(agentRequest, Tags.AGENT_RESUME_REQ);
    }

    /**
     * This method is responsible to send a KILL request to an existing agent
     * 
     * @param agentRequest an instance of AgentRequest class specifying parameters the agent to kill
     * @return an instance of AgentReply class
     * 
     * @see net.p2pgrid.gap.agents.messages.AgentRequest
     * @see net.p2pgrid.gap.agents.messages.AgentReply
     */
    protected AgentReply killAgent(AgentRequest agentRequest) 
    {
        return this.requestToAgent(agentRequest, Tags.AGENT_KILL_REQ);
    }

    /**
     * This method is responsible to send a delayed KILL request to an existing agent
     * 
     * @param agentRequest an instance of AgentRequest class specifying parameters the agent to kill while waiting for its gridlets
     * @return an instance of AgentReply class
     * 
     * @see net.p2pgrid.gap.agents.messages.AgentRequest
     * @see net.p2pgrid.gap.agents.messages.AgentReply
     */
    protected AgentReply killWaitAgent(AgentRequest agentRequest) 
    {
        return this.requestToAgent(agentRequest, Tags.AGENT_KILLAWAIT_REQ);
    }

    /**
     * This method is responsible to send a MOVE request to an existing agent
     * 
     * @param agentRequest an instance of AgentRequest class specifying parameters the agent to move
     * @param moveToResourceID the destination resource ID for the migration
     * @return an instance of AgentReply class
     * 
     * @see net.p2pgrid.gap.agents.messages.AgentRequest
     * @see net.p2pgrid.gap.agents.messages.AgentReply
     */
    protected AgentReply moveAgent(
            AgentRequest agentRequest,
            int moveToResourceID) 
    {
        agentRequest.setDst_moveToresID(moveToResourceID);
        return this.requestToAgent(agentRequest, Tags.AGENT_MOVE_REQ);
    }

    /**
     * This method is responsible to send any request related to agents lifecycle to an existing agent
     * Allowed requests: RUN, PAUSE, RESUME, KILL, delayed KILL, MOVE
     * 
     * @param request an instance of AgentRequest specifying parameters for any request related to agent's lifecycle
     * @param tag the tag specifying the requested action
     * @return an instance of AgentReply class
     * 
     * @see eduni.simjava.Sim_type_p
     * @see eduni.simjava.Sim_event
     * @see net.p2pgrid.gap.agents.messages.AgentRequest
     * @see net.p2pgrid.gap.agents.messages.AgentReply
     */
    private AgentReply requestToAgent(
            AgentRequest request, 
            int tag) 
    {
        // Unique request ID
        int requestID = request.getRequestID();
        // Unique conversation (request/reply) ID
        int reqrepID = request.getReqrepID();
        // Event send time
        double evsend_time;
        
        // Destination agent's entity type
        int agentType = request.getDst_entityType();
        
        // Sets agent request transfer size
        int SIZE;
        if (tag == Tags.AGENT_RUN_REQ) {
            SIZE = request.getDst_agentSize();
        } else {
            //@TODO tune fixed SIZE for messages between agents 
            SIZE = 500;
        }

        // if requested action is RUN, PAUSE, RESUME, KILL, delayed KILL
        if ((tag == Tags.AGENT_RUN_REQ) || (tag == Tags.AGENT_KILL_REQ) || (tag == Tags.AGENT_KILLAWAIT_REQ) || (tag == Tags.AGENT_PAUSE_REQ) || (tag == Tags.AGENT_RESUME_REQ)) {
            // Sends agent's request to destination agent indirectly
            // through its Grid Element resource ID that acts as
            // Agent Middleware
            // @TODO for a better SoC Agent Middleware should be separated from Grid Element in order to use GridSim entities without requiring a class like GridElement
            super.send(super.output, GridSimTags.SCHEDULE_NOW, tag,
                    new IO_data(request, SIZE, request.getDst_resID()));
        } else {
            //@TODO verify possible alternative tags (should be only Tags.AGENT_MOVE_REQ, added an Assert for this goal
            Assert.assertEquals(tag==Tags.AGENT_MOVE_REQ, true);
            // Sends agent's request directly to destination agent
            super.send(super.output, GridSimTags.SCHEDULE_NOW, tag,
                    new IO_data(request, SIZE, request.getDst_agentID()));
        }
        // Sets event's send time to current simulation's clock
        evsend_time = GridSim.clock();
        // Prepares a message for simulation's output
        // @TODO Note that an improvement of current GAP library should pass through a global revision of its simulation's output that, i.e., could rely upon existing libraries like log4j
        String msg = String.format(
                "%1$f %2$d %3$s --> AM_%4$s::REQUEST %6$s (%7$s AID %9$d) %5$s AM_%8$s",
                evsend_time, reqrepID, this.get_name(), AbstractAgent.getEntityName(request.getDst_resID()),
                EntityTypes.toString(request.getDst_entityType()), Tags.toString(tag), AbstractAgent.getEntityName(request.getDst_agentID()), AbstractAgent.getEntityName(request.getDst_moveToresID()), request.getDst_AID());
        this.write(msg);

        // This segment of the code is responsible for 
        // - syncronously receiving the reply to the agent's request 
        // - parsing such reply
        // - return such reply as method's result
        // Note: As a convention in GAP library all tags' replies have a value
        // equal to tag's request value plus one
        
        // Create an instance of Sim_type_p class
        // It's a predicate to select events with specific tags
        // In this case the reply tag related to the request tag
        Sim_type_p ptag = new Sim_type_p(tag + 1);
        // Creates a new instance of Sim_event class
        Sim_event ev = new Sim_event();
        // Get the first event waiting in the entity's deferred queue
        // or if there are none, wait for an event to arrive
        // Event tag is that of related agent's reply that 
        // the agent is syncronously waiting for
        super.sim_get_next(ptag, ev); 
        // Extracts an instance of AgentReply class from the received event
        AgentReply agentReply = AgentReply.get_data(ev);

        // Checks that:
        // - the request ID, encapsulated in agent's reply is the
        //   same of that agent's request
        // - the request tag, encapsulated in agent's reply is the
        //   same of that agent's request
        // - the reply tag is really associated to the request tag
        Assert.assertEquals(requestID, agentReply.getRequestID());
        Assert.assertEquals(tag, agentReply.getRequestTAG());
        Assert.assertEquals(ev.get_tag(), tag + 1);

        // Sets event's receive time to current simulation's clock
        double evrecv_time = GridSim.clock();
        // Prepares a message for simulation's output
        msg = String.format(
                "%1$f %2$d %3$s <-- AM_%4$s::%7$s %8$s (%6$s AID %10$d) %5$s AM_%9$s",
                evrecv_time,
                reqrepID,
                this.get_name(), AbstractAgent.getEntityName(agentReply.getRequest().getDst_resID()), EntityTypes.toString(agentType), AbstractAgent.getEntityName(request.getDst_agentID()),
                agentReply.isOk(), Tags.toString(tag), AbstractAgent.getEntityName(request.getDst_moveToresID()),
                request.getDst_AID());
        this.write(msg);
        return agentReply;
    }

    /**
     * This methos is responsible for sending ACK/NACK
     * through requesting agent's Agent Middleware
     * 
     * @param ev received event
     * @param agentRequest agent's request
     * @param flag boolean flag indicating success or failure of the request
     * 
     * @see eduni.simjava.Sim_event
     * @see net.p2pgrid.gap.agents.messages.AgentRequest
     * @see net.p2pgrid.gap.agents.messages.AgentReply
     */
    private void sendACKNACK(
            Sim_event ev, 
            AgentRequest agentRequest,
            boolean flag) 
    {
        AgentReply agentReply;

        int SIZE = 500;

        int replyToID = 0;
        replyToID = agentRequest.getDst_resID();
        agentReply = new AgentReply(ev.get_tag(), flag, agentRequest);
        super.send(this.output, GridSimTags.SCHEDULE_NOW, ev.get_tag() + 1,
                new IO_data(agentReply, SIZE, replyToID));
    }

    /**
     * This methos is responsible for sending ACK 
     * through requesting agent's Agent Middleware
     * 
     * @param ev
     * @param agentRequest
     * 
     * @see eduni.simjava.Sim_event
     * @see net.p2pgrid.gap.agents.messages.AgentRequest
     */
    protected void sendACK(
            Sim_event ev, 
            AgentRequest agentRequest) 
    {
        this.sendACKNACK(ev, agentRequest, true);
    }

    /**
     * This methos is responsible for sending NACK 
     * through requesting agent's resource ID (its agent middleware)
     * 
     * @param ev
     * @param agentRequest
     * 
     * @see eduni.simjava.Sim_event
     * @see net.p2pgrid.gap.agents.messages.AgentRequest
     */
    private void sendNACK(
            Sim_event ev, 
            AgentRequest agentRequest) 
    {
        this.sendACKNACK(ev, agentRequest, false);
    }

    /**
     * This method is responsible for syncronously notifying to DF service
     * which is located on Agent Platform, all agent's requests related to:
     * - REGISTER
     * - DEREGISTER
     * - PAUSE
     * - RESUME
     * 
     * @param originalRequest
     * @param dftag
     * 
     * @see eduni.simjava.Sim_type_p
     * @see eduni.simjava.Sim_event
     * @see net.p2pgrid.gap.agents.messages.AgentRequest
     * @see net.p2pgrid.gap.agents.messages.AgentReply
     */
    private void notifyDFService(
            AgentRequest originalRequest, 
            int dftag) 
    {
        AgentRequest agentRequest;
        AgentReply agentReply;
        
        int SIZE = 500;
        
        agentRequest = new AgentRequest(
                originalRequest.getSrc_ID(),
                originalRequest.getSrc_resID(), 
                originalRequest.getSrc_agentHistory(), 
                originalRequest.getDst_resID(),
                this.get_id(), 
                this.getEntityType(),
                this.getAgentSizeInBytes(), 
                originalRequest.getDst_resID(),
                originalRequest.getDst_AID());
        
        int requestID = agentRequest.getRequestID();
        int reqrepID = agentRequest.getReqrepID();
        
        super.send(this.output, GridSimTags.SCHEDULE_NOW, dftag, new IO_data(
                agentRequest, SIZE, this.getAgentPlatform().get_id()));
        
        double evsend_time = GridSim.clock();
        String msg = String.format(
                "%1$f %2$d %3$s --> DFService::REQUEST %6$s (%7$s AID %8$d) %4$s AM_%5$s",
                evsend_time, reqrepID, this.get_name(), EntityTypes.toString(originalRequest.getDst_entityType()),
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
        msg = String.format(
                "%1$f %2$d %3$s <-- DFService::DFSERVICE_ACK %6$s (%7$s AID %8$d) %4$s AM_%5$s",
                evrecv_time, reqrepID, this.get_name(), EntityTypes.toString(originalRequest.getDst_entityType()),
                AbstractAgent.getEntityName(originalRequest.getDst_resID()),
                Tags.toString(dftag),
                AbstractAgent.getEntityName(originalRequest.getDst_agentID()),
                originalRequest.getDst_AID());
        this.write(msg);
    }
}
