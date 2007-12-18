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
 * GridAgent.java
 *
 * Created on 11 March 2007, 12.00 by Giovanni Novelli
 *
 ****************************************************************************************
 *
 * $Revision: 1141 $
 * $Id: GridAgent.java 1141 2007-07-18 18:22:15Z gnovelli $
 * $HeadURL: file:///var/svn/grid/trunk/GAP/src/net/sf/gap/agents/GridAgent.java $
 *
 *****************************************************************************************
 */

package net.sf.gap.agents;

import junit.framework.Assert;
import net.sf.gap.GAP;
import net.sf.gap.constants.AgentStates;
import net.sf.gap.constants.Tags;
import net.sf.gap.agents.gridlets.scheduling.RRScheduler;
import net.sf.gap.messages.impl.GridletReply;
import net.sf.gap.messages.impl.GridletRequest;
import net.sf.gap.messages.impl.AgentReply;
import net.sf.gap.messages.impl.AgentRequest;
import net.sf.gap.grid.components.GridElement;
import eduni.simjava.Sim_event;
import eduni.simjava.Sim_system;
import gridsim.GridSimTags;
import gridsim.Gridlet;
import gridsim.IO_data;

/**
 * This abstract class is mainly responsible in simulating basic behaviour 
 * of agents in relation to:
 * <p>
 * <ul>
 * <li> Gridlets submission and monitoring by agents
 * <ul/>
 * 
 * @author Giovanni Novelli
 */
public abstract class GridAgent extends Agent {
        /**
         * This field incorporates gridlets' scheduler
         */
	private RRScheduler scheduler;

	/**
	 * Creates a new instance of GridAgent class
	 * 
	 * @param name agent name
	 * @param agentSizeInBytes agent size in bytes
	 * @param trace_flag trace flag for GridSim
	 * @throws Exception
	 */
	public GridAgent(
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
         * 
         * @throws java.lang.Exception
         */
    @Override
	public void initialize() 
        throws Exception 
    { 
            super.initialize();
            this.setScheduler(new RRScheduler(this,16));
        }

	/**
	 * The core method that handles communications among entities
	 */
	@Override
	public void body() {
		// Wait for a little while for about 3 seconds.
		// This to give a time for GridResource entities to 
                // registerAgent their services to GIS 
                // (GridInformationService) entity.
		super.gridSimHold(GAP.getStartTime());

		Sim_event ev = new Sim_event();
		while (GAP.isRunning()) {
			super.sim_wait_for(Sim_system.SIM_ANY, 1.0, ev);
			this.processEvent(ev);
			while (super.sim_waiting() > 0) {
				this.processEvents();
			}
		}

		// Dispose Agent
		this.dispose();

		// //////////////////////////////////////////////////////
		// shut down I/O ports
		this.shutdownUserEntity();
		this.terminateIOEntities();

		// don't forget to close the file
		if (this.getReport_() != null) {
			this.writeHistory();
			this.getReport_().finalWrite();
		}
	}

        
        /**
         * Processes single event related to:
         * - gridlets management
         * - replying to requests related to the presence of gridlets
         * - other
         * 
	 * @param ev Sim_event to be processed
	 */
	@Override
	public void processEvent(Sim_event ev) {
		super.processEvent(ev);

		switch (ev.get_tag()) {
                // Gridlets Management
		case GridSimTags.GRIDLET_PAUSE:
		case GridSimTags.GRIDLET_CANCEL:
		case GridSimTags.GRIDLET_STATUS:
		case Tags.GRIDLET_SUBMIT_REQ:
		case GridSimTags.GRIDLET_RETURN:
		case Tags.GRIDLET_SUBMIT_REP:
			this.manageGridlets(ev);
			break;

                // Replying to requests related to the presence of gridlets
		case Tags.HASGRIDLETS_REQUEST:
		case Tags.HASGRIDLETS_REPLY:
			AgentRequest agentRequest = AgentRequest.get_data(ev);
			if (this.getScheduler().hasGridlets()) {
				this.sendHASGRIDLETSACK(ev, agentRequest);
			} else {
				this.sendHASGRIDLETSNACK(ev, agentRequest);
			}
			break;

                // other
		default:
			this.processOtherEvent(ev);
			break;
		}
	}

        /**
         * @TODO Gridlets management to be fixed!!!!!
         * @TODO Comments to be completed
         * @param ev
         */
	@Override
	protected void manageGridlets(Sim_event ev) {
                Gridlet gridlet;
                
		GridletRequest gridletRequest;
        
                gridletRequest = GridletRequest.get_data(ev);
		switch (ev.get_tag()) {

                // Cancels a Gridlet submitted in the GridResource entity
		case GridSimTags.GRIDLET_CANCEL:
			gridlet = gridletRequest.getGridlet();
			if (this.getAgentState() == AgentStates.RUNNING) {
                                Gridlet canceledGridlet = this.getScheduler().gridletCancel(gridlet);
                                if (canceledGridlet!=null) {
				this.sendACK(ev, gridletRequest, gridlet);
                                } else {
				this.sendNACK(ev, gridletRequest, gridlet);
                                }
			} else {
				this.sendNACK(ev, gridletRequest, gridlet);
			}
			break;
                case GridSimTags.GRIDLET_MOVE:
                    Assert.fail();
                    break;
                case GridSimTags.GRIDLET_MOVE_ACK:
                    Assert.fail();
                    break;
                // Pauses a Gridlet submitted in the GridResource entity
		case GridSimTags.GRIDLET_PAUSE:
			gridlet = gridletRequest.getGridlet();
			if (this.getAgentState() == AgentStates.RUNNING) {
                                boolean paused = this.getScheduler().gridletPause(gridlet);
                                if (paused) {
				this.sendACK(ev, gridletRequest, gridlet);
                                } else {
				this.sendNACK(ev, gridletRequest, gridlet);
                                }
			} else {
				this.sendNACK(ev, gridletRequest, gridlet);
			}
			break;
                case GridSimTags.GRIDLET_PAUSE_ACK:
                    Assert.fail();
                    break;
                case GridSimTags.GRIDLET_RESUME:
                    Assert.fail();
                    break;
                case GridSimTags.GRIDLET_RESUME_ACK:
                    Assert.fail();
                    break;
                //  Denotes the return of a Gridlet back to sender
		case GridSimTags.GRIDLET_RETURN:
                    Assert.fail();
			Gridlet receivedGridlet = (Gridlet) ev.get_data();
                        this.getScheduler().gridletReceive(receivedGridlet);
			break;
                case GridSimTags.GRIDLET_STATUS:
                    Assert.fail();
                    break;
                case GridSimTags.GRIDLET_SUBMIT:
                    Assert.fail();
                    break;
                case GridSimTags.GRIDLET_SUBMIT_ACK:
                    Assert.fail();
                    break;
		case Tags.GRIDLET_STATUS_REQ:
                    Assert.fail();
                    /*
			gridlet = gridletRequest.getGridlet();
			if (this.getAgentState() == AgentStates.RUNNING) {
				gridlet.setUserID(this.get_id());
				super.gridletStatus(gridlet, this.getResourceID());
				this.sendSTATUSACK(ev, gridletRequest, gridlet);
			} else {
				this.sendSTATUSNACK(ev, gridletRequest, gridlet);
			}
                     */
			break;
		case Tags.GRIDLET_SUBMIT_REQ:
                    Assert.fail();
			gridlet = gridletRequest.getGridlet();
			if (this.getAgentState() == AgentStates.RUNNING) {
				gridlet.setUserID(this.get_id());
				this.getScheduler().getGridletsBag().addRequest(gridletRequest);
                                boolean submitted = this.getScheduler().enque(gridlet);
				if (submitted) {
					this.sendACK(ev, gridletRequest, gridlet);
				} else {
					System.out.println("Problems in queueing of last gridlet on agent " + this.get_name());
					this.sendNACK(ev, gridletRequest, gridlet);
				}
			} else {
				this.sendNACK(ev, gridletRequest, gridlet);
			}
			break;
                case Tags.GRIDLET_SUBMIT_REP:
                    Assert.fail();
			break;
		default:
			break;
		}
	}

        /**
         * @TODO Fix semanthics
         * @TODO Comments to be completed
         * Sends ACK/NACK about presence of gridlets in an agent
         * @param ev
         * @param agentRequest
         * @param flag
         */
	private void sendHASGRIDLETSACKNACK(Sim_event ev,
			AgentRequest agentRequest, boolean flag) {
		AgentReply agentReply = null;

		int SIZE = 500;

		int replyToID = 0;

		replyToID = agentRequest.getSrc_ID();
		agentReply = new AgentReply(ev.get_tag(), flag, agentRequest);
		super.send(super.output, GridSimTags.SCHEDULE_NOW,
				Tags.HASGRIDLETS_REPLY,
				new IO_data(agentReply, SIZE, replyToID));
	}

        /**
         * @TODO Fix semanthics
         * @TODO Comments to be completed
         * Sends ACK/NACK related to gridlets in an agent
         * @param ev
         * @param gridletRequest
         * @param flag
         * @param gridlet
         */
	private void sendSTATUSACKNACK(Sim_event ev, GridletRequest gridletRequest,
			boolean flag, Gridlet gridlet) {
		GridletReply gridletReply = null;

		int SIZE = 500;

		int replyToID = 0;

		replyToID = gridletRequest.getSrc_ID();
		gridletReply = new GridletReply(ev.get_tag(), flag, gridletRequest,
				gridlet);
		super.send(super.output, GridSimTags.SCHEDULE_NOW,
				Tags.GRIDLET_STATUS_REP, new IO_data(gridletReply, SIZE,
						replyToID));
	}

        /**
         * @TODO Fix semanthics
         * Sends ACK related to gridlets in an agent
         * @param ev
         * @param gridletRequest
         * @param gridlet
         */
	private void sendSTATUSACK(Sim_event ev, GridletRequest gridletRequest,
			Gridlet gridlet) {
		this.sendSTATUSACKNACK(ev, gridletRequest, true, gridlet);
	}

        /**
         * @TODO Fix semanthics
         * @TODO Comments to be completed
         * Sends NACK related to gridlets in an agent
         * @param ev
         * @param gridletRequest
         * @param gridlet
         */
	private void sendSTATUSNACK(Sim_event ev, GridletRequest gridletRequest,
			Gridlet gridlet) {
		this.sendSTATUSACKNACK(ev, gridletRequest, false, gridlet);
	}

        /**
         * @TODO Fix semanthics
         * @TODO Comments to be completed
         * @param ev
         * @param gridletRequest
         * @param flag
         * @param gridlet
         */
	private void sendACKNACK(Sim_event ev, GridletRequest gridletRequest,
			boolean flag, Gridlet gridlet) {
		GridletReply gridletReply = null;

		int SIZE = 500;

		int replyToID = 0;

		replyToID = gridletRequest.getSrc_ID();
		gridletReply = new GridletReply(ev.get_tag(), flag, gridletRequest,
				gridlet);
		super.send(super.output, GridSimTags.SCHEDULE_NOW,
				Tags.GRIDLET_SUBMIT_REP, new IO_data(gridletReply, SIZE,
						replyToID));
	}

        /**
         * @TODO Fix semanthics
         * @TODO Comments to be completed
         * Sends ACK related to gridlets presence in an agent
         * @param ev
         * @param agentRequest
         */
	private void sendHASGRIDLETSACK(Sim_event ev, AgentRequest agentRequest) {
		this.sendHASGRIDLETSACKNACK(ev, agentRequest, true);
	}

        /**
         * @TODO Fix semanthics
         * @TODO Comments to be completed
         * Sends NACK related to gridlets presence in an agent
         * @param ev
         * @param agentRequest
         */
	private void sendHASGRIDLETSNACK(Sim_event ev, AgentRequest agentRequest) {
		this.sendHASGRIDLETSACKNACK(ev, agentRequest, false);
	}

        /**
         * @TODO Fix semanthics
         * @TODO Comments to be completed
         * Sends ACK related to gridlets in an agent
         * @param ev
         * @param agentRequest
         */
	private void sendACK(Sim_event ev, GridletRequest gridletRequest,
			Gridlet gridlet) {
		this.sendACKNACK(ev, gridletRequest, true, gridlet);
	}

        /**
         * @TODO Fix semanthics
         * @TODO Comments to be completed
         * Sends NACK related to gridlets in an agent
         * @param ev
         * @param agentRequest
         */
	private void sendNACK(Sim_event ev, GridletRequest gridletRequest,
			Gridlet gridlet) {
		this.sendACKNACK(ev, gridletRequest, false, gridlet);
	}



	@Override
	protected boolean hasGridlets() {
		return this.getScheduler().hasGridlets();
	}

        /**
         * @TODO Fix semanthics
         * @TODO Comments to be completed
         * @param agentRequest
         */
	@Override
	protected void onWaitingGridlets(AgentRequest agentRequest) {
		Assert.fail();
	}

        /**
         * Sends a Gridlet to the Grid Element of this agent 
         * without any delay. An acknowledgement to denote the success
         * of this method is by default off or false
         * @param gl the gridlet submitted
         * @return <tt>true</tt> for successful submission <tt>false</tt> otherwise
         */
        public boolean gridletSubmit(Gridlet gl) 
        {
            return super.gridletSubmit(gl,this.getResourceID());
        }
        
        /**
         * Syncronously receives a gridlet from Grid Element
         * 
         * @return <tt>true</tt> for successful receival <tt>false</tt> otherwise
         */
    @Override
        public Gridlet gridletReceive() {
            return super.gridletReceive();
        }

        /**
         * Cancels a Gridlet on the Grid Element of this agent 
         * without any delay. An acknowledgement to denote the success 
         * of this method is by default off or false
         * @param gl the gridlet to be canceled
         * @return canceled gridlet
         */
    public Gridlet gridletCancel(Gridlet gl) {
        return super.gridletCancel(gl, this.getResourceID(), 0.0);
    }
    
        /**
         * Pauses a Gridlet on the Grid Element of this agent 
         * without any delay. An acknowledgement to denote the success 
         * of this method is by default off or false
         * @param gl the gridlet to be paused
         * @return <tt>true</tt> for successful pause <tt>false</tt> otherwise
         */
    public boolean gridletPause(Gridlet gl) {
        return super.gridletPause(gl, this.getResourceID(), 0.0);
    }
    
        /**
         * Resumes a Gridlet on the Grid Element of this agent 
         * without any delay. An acknowledgement to denote the success
         * of this method is by default off or false
         * @param gl the gridlet to be resumed
         * @return <tt>true</tt> for successful resume <tt>false</tt> otherwise
         */
    public boolean gridletResume(Gridlet gl) {
        return super.gridletResume(gl, this.getResourceID(), 0.0);
    }
    
    /**
     * 
     * @param gl the gridlet of which to know the status
     * @return the current Gridlet status or <tt>-1</tt> if not found.
     *         The various Gridlet status can be found in Gridlet class.
     */
    public int gridletStatus(Gridlet gl) {
        return super.gridletStatus(gl, this.getResourceID());
    }


    /**
     * This method is responsible for moving a gridlet to another resource ID
     * @TODO Note: To complete migration it's needed to change the agent that manages
     * @TODO the gridlet using an existing agent or the migrated entity of this
     * 
     * @param gl the gridlet to move
     * @param destResID the destination resource ID 
     * @return <tt>true</tt> for successful move <tt>false</tt> otherwise
     */
    public boolean gridletMove(Gridlet gl,int destResID) {
        return super.gridletMove(gl, this.getResourceID(), destResID, 0.0);
    }
    
    public RRScheduler getScheduler() {
        return scheduler;
    }

    public void setScheduler(RRScheduler scheduler) {
        this.scheduler = scheduler;
    }
}
