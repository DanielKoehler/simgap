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

import net.sf.gap.GAP;
import net.sf.gap.constants.AgentStates;
import net.sf.gap.constants.Tags;
import net.sf.gap.agents.gridlets.Gridlets;
import net.sf.gap.messages.impl.GridletReply;
import net.sf.gap.messages.impl.GridletRequest;
import net.sf.gap.messages.impl.AgentReply;
import net.sf.gap.messages.impl.AgentRequest;
import net.sf.gap.grid.components.GridElement;
import eduni.simjava.Sim_event;
import eduni.simjava.Sim_system;
import gridsim.GridSim;
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
         * This field keeps track of information related to
         * gridlets management
         */
	protected Gridlets gapGridlets;

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
	    this.setGapGridlets(new Gridlets());
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
			if (this.getGapGridlets().hasGridlets()) {
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
		GridletReply gridletReply;
        
                gridletRequest = GridletRequest.get_data(ev);
		switch (ev.get_tag()) {

                // Cancels a Gridlet submitted in the GridResource entity
		case GridSimTags.GRIDLET_CANCEL:
			gridlet = gridletRequest.getGridlet();
			if (this.getAgentState() == AgentStates.RUNNING) {
				gridlet.setUserID(this.get_id());
				Gridlet canceledGridlet = super.gridletCancel(gridlet, this
						.getResourceID(), GridSimTags.SCHEDULE_NOW);
				if (canceledGridlet == null) {
					this.getGapGridlets().addCanceled(gridlet);
				} else {
					this.getGapGridlets().addCanceled(canceledGridlet);
				}
				this.sendACK(ev, gridletRequest, gridlet);
			} else {
				this.sendNACK(ev, gridletRequest, gridlet);
			}
			break;
                case GridSimTags.GRIDLET_MOVE:
                    break;
                case GridSimTags.GRIDLET_MOVE_ACK:
                    break;
                // Pauses a Gridlet submitted in the GridResource entity
		case GridSimTags.GRIDLET_PAUSE:
			gridlet = gridletRequest.getGridlet();
			if (this.getAgentState() == AgentStates.RUNNING) {
				gridlet.setUserID(this.get_id());
				boolean paused = super.gridletPause(gridlet, this
						.getResourceID(), GridSimTags.SCHEDULE_NOW);
				if (paused) {
					this.getGapGridlets().addPaused(gridlet);
					this.sendACK(ev, gridletRequest, gridlet);
				} else {
					this.sendNACK(ev, gridletRequest, gridlet);
				}

			} else {
				this.sendNACK(ev, gridletRequest, gridlet);
			}
			break;
                case GridSimTags.GRIDLET_PAUSE_ACK:
                    break;
                case GridSimTags.GRIDLET_RESUME:
                    break;
                case GridSimTags.GRIDLET_RESUME_ACK:
                    break;
                //  Denotes the return of a Gridlet back to sender
		case GridSimTags.GRIDLET_RETURN:
			// Receiving gridlet back
                        // @TODO Check this way to extract gridlet from event
			gridlet = (Gridlet) ev.get_data();
			String indent = "    ";
			System.out.println();
			System.out.println("========== OUTPUT ==========");
			System.out.println("Gridlet ID" + indent + "STATUS");
			System.out.print(indent + gridlet.getGridletID() + indent + indent);
			if (gridlet.getGridletStatus() == Gridlet.SUCCESS) {
				System.out.println("SUCCESS" + " " + GridSim.clock());
				this.getGapGridlets().addSuccesses(gridlet);
			}
			gridletRequest = this.getGapGridlets().getMapGR().get(
					gridlet.getGridletID());
			if (!this.getGapGridlets().hasGridlets()
					&& this.getGapGridlets().isWaitingGridlets()) {
				this.getGapGridlets().offWaitingGridlets();
				this
						.sendACK(ev, this.getGapGridlets()
								.getWaitingAgentRequest());
			}
			break;
                case GridSimTags.GRIDLET_STATUS:
                    break;
                case GridSimTags.GRIDLET_SUBMIT:
                    break;
                case GridSimTags.GRIDLET_SUBMIT_ACK:
                    break;
		case Tags.GRIDLET_STATUS_REQ:
			gridlet = gridletRequest.getGridlet();
			if (this.getAgentState() == AgentStates.RUNNING) {
				gridlet.setUserID(this.get_id());
				super.gridletStatus(gridlet, this.getResourceID());
				this.sendSTATUSACK(ev, gridletRequest, gridlet);
			} else {
				this.sendSTATUSNACK(ev, gridletRequest, gridlet);
			}
			break;
		case Tags.GRIDLET_SUBMIT_REQ:
			gridlet = gridletRequest.getGridlet();
			if (this.getAgentState() == AgentStates.RUNNING
					&& !this.getGapGridlets().isWaitingGridlets()) {
				gridlet.setUserID(this.get_id());
				this.getGapGridlets().addRequest(gridletRequest);
				boolean submitted = super.gridletSubmit(gridlet, this
						.getResourceID(), GridSimTags.SCHEDULE_NOW, true);
				if (submitted) {
					this.getGapGridlets().addSubmitted(gridlet);
					this.sendACK(ev, gridletRequest, gridlet);
				} else {
					System.out
							.println("Problems in submission of last gridlet");
					this.sendNACK(ev, gridletRequest, gridlet);
				}
			} else {
				this.sendNACK(ev, gridletRequest, gridlet);
			}
			break;
                case Tags.GRIDLET_SUBMIT_REP:
			gridletReply = GridletReply.get_data(ev);
			gridlet = gridletRequest.getGridlet();
			this.getGapGridlets().getMapGR().remove(gridlet.getGridletID());
			System.out.println("Received back gridlet "
					+ gridletReply.getReceivedGridlet());
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

	protected Gridlets getGapGridlets() {
		return gapGridlets;
	}

	protected void setGapGridlets(Gridlets gapGridlets) {
		this.gapGridlets = gapGridlets;
	}

	@Override
	protected boolean hasGridlets() {
		return this.getGapGridlets().hasGridlets();
	}

        /**
         * @TODO Fix semanthics
         * @TODO Comments to be completed
         * @param agentRequest
         */
	@Override
	protected void onWaitingGridlets(AgentRequest agentRequest) {
		this.getGapGridlets().onWaitingGridlets(agentRequest);
	}

        /**
         * @TODO Fix semanthics
         * @TODO Comments to be completed
         */
	@Override
	protected void offWaitingGridlets() {
		this.getGapGridlets().offWaitingGridlets();
	}
}
