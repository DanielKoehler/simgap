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
 * LocalDirectory.java
 *
 * Created on 25 August 2006, 12.00 by Giovanni Novelli
 *
 ****************************************************************************************
 *
 * $Revision: 1141 $
 * $Id: LocalDirectory.java 1141 2007-07-18 18:22:15Z gnovelli $
 * $HeadURL: file:///var/svn/grid/trunk/GAP/src/net/sf/gap/grid/LocalDirectory.java $
 *
 *****************************************************************************************
 */

package net.sf.gap.grid;

import java.util.LinkedList;

import net.sf.gap.agents.AbstractAgent;
import net.sf.gap.constants.Tags;
import net.sf.gap.grid.components.GridElement;
import net.sf.gap.messages.impl.AgentRequest;

/**
 * This class is responsible to track agents running on a Grid Element (CE/SE)
 * with the following lists:
 * <p>
 * <ul>
 * <li> list of zombie agents
 * 
 * <li> list of running agents
 * 
 * <li> list of paused agents
 * 
 * <ul/>
 * 
 * @author Giovanni Novelli
 */
public class LocalDirectory {

	private GridElement gridElement;

	private LinkedList<Integer> zombieAgents;

	private LinkedList<Integer> runningAgents;

	private LinkedList<Integer> pausedAgents;

	/**
	 * Creates a new instance of LocalDirectory
	 */
	public LocalDirectory(GridElement gridElement) {
		this.setGridElement(gridElement);

		this.setZombieAgents(new LinkedList<Integer>());
		this.setRunningAgents(new LinkedList<Integer>());
		this.setPausedAgents(new LinkedList<Integer>());
	}

	public LinkedList<Integer> getZombieAgents() {
		return zombieAgents;
	}

	public void setZombieAgents(LinkedList<Integer> zombieAgents) {
		this.zombieAgents = zombieAgents;
	}

	public LinkedList<Integer> getRunningAgents() {
		return runningAgents;
	}

	public void setRunningAgents(LinkedList<Integer> runningAgents) {
		this.runningAgents = runningAgents;
	}

	public LinkedList<Integer> getPausedAgents() {
		return pausedAgents;
	}

	public void setPausedAgents(LinkedList<Integer> pausedAgents) {
		this.pausedAgents = pausedAgents;
	}

	public int getFreeAgents() {
		return this.getZombieAgents().size();
	}

	public boolean addFreeAgent(AbstractAgent agent) {
		return this.getZombieAgents().add(agent.get_id());
	}

	public boolean update(AgentRequest agentRequest, int tag) {
		int aid = 0;
		if (this.getGridElement().get_id() == agentRequest.getDst_resID()) {
			switch (tag) {
			case Tags.AGENT_RUN_REQ:
				int numZA = this.getZombieAgents().size();
				if (numZA > 0) {
					aid = this.getZombieAgents().removeFirst();
					this.getRunningAgents().add(Integer.valueOf(aid));
					agentRequest.setDst_agentID(aid);
					return true;
				} else {
					return false;
				}
			case Tags.AGENT_KILL_REQ:
				aid = agentRequest.getDst_agentID();
				if (this.getRunningAgents().contains(Integer.valueOf(aid))) {
					this.getZombieAgents().add(Integer.valueOf(aid));
					this.getRunningAgents().remove(Integer.valueOf(aid));
					return true;
				} else {
					return false;
				}
			case Tags.AGENT_KILLAWAIT_REQ:
				aid = agentRequest.getDst_agentID();
				if (this.getRunningAgents().contains(Integer.valueOf(aid))) {
					this.getZombieAgents().add(Integer.valueOf(aid));
					this.getRunningAgents().remove(Integer.valueOf(aid));
					return true;
				} else {
					return false;
				}
			case Tags.AGENT_PAUSE_REQ:
				aid = agentRequest.getDst_agentID();
				if (this.getRunningAgents().contains(Integer.valueOf(aid))) {
					this.getRunningAgents().remove(Integer.valueOf(aid));
					this.getPausedAgents().add(Integer.valueOf(aid));
					return true;
				} else {
					return false;
				}
			case Tags.AGENT_RESUME_REQ:
				aid = agentRequest.getDst_agentID();
				if (this.getPausedAgents().contains(Integer.valueOf(aid))) {
					this.getPausedAgents().remove(Integer.valueOf(aid));
					this.getRunningAgents().add(Integer.valueOf(aid));
					return true;
				} else {
					return false;
				}

			case Tags.AGENT_MOVE_REQ:
				aid = agentRequest.getDst_agentID();
				if (this.getRunningAgents().contains(Integer.valueOf(aid))) {
					this.getZombieAgents().add(Integer.valueOf(aid));
					this.getRunningAgents().remove(Integer.valueOf(aid));
					return true;
				} else {
					return false;
				}

			default:
				return false;
			}
		} else {
			return false;
		}
	}

	@Override
	public String toString() {
		String msg = "LOCAL DIRECTORY STATUS:\n" + "  ZOMBIE  AGENTS:"
				+ this.getZombieAgents().size() + "\n" + "  RUNNING AGENTS:"
				+ this.getRunningAgents().size() + "\n" + "  PAUSED  AGENTS:"
				+ this.getPausedAgents().size();
		return msg;
	}

	public GridElement getGridElement() {
		return gridElement;
	}

	public void setGridElement(GridElement gridElement) {
		this.gridElement = gridElement;
	}
}
