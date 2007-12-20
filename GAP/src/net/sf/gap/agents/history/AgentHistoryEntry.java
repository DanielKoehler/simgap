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
 * AgentHistoryEntry.java
 *
 * Created on 20 August 2006, 16.32 by Giovanni Novelli
 *
 ****************************************************************************************
 *
 * $Revision: 1141 $
 * $Id: AgentHistoryEntry.java 1141 2007-07-18 18:22:15Z gnovelli $
 * $HeadURL: file:///var/svn/grid/trunk/GAP/src/net/sf/gap/agents/history/AgentHistoryEntry.java $
 *
 *****************************************************************************************
 */

package net.sf.gap.agents.history;

import net.sf.gap.constants.AgentStates;
import net.sf.gap.constants.EntityTypes;
import eduni.simjava.Sim_system;

/**
 * This class is mainly responsible for maintaing information about each agent's
 * lifecycle step.
 * 
 * @author Giovanni Novelli
 * @see net.p2pgrid.gap.agents.history.AgentHistory
 */
public class AgentHistoryEntry {
	private double time; // Time of event

	private String name; // Name

	private int state; // State (RUNNING, PAUSED, ZOMBIE)

	private int entityID; // Entity ID

	private int AID; // GridAgent IDentity

	private int entityType; // Entity Type

	private int resourceID; // Entity ID of the Resource (CE/SE) hosting the

	// agent

	/**
	 * Creates a new instance of AgentHistoryEntry
	 */
	public AgentHistoryEntry(double time, String name, int state, int entityID,
			int AID, int entityType, int resourceID) {
		this.setTime(time);
		this.setName(name);
		this.setState(state);
		this.setEntityID(entityID);
		this.setAID(AID);
		this.setEntityType(entityType);
		this.setResourceID(resourceID);
	}

	@Override
	public String toString() {
		return String.format("%1$f %2$s HISTORY %3$s AID%4$s %5$s AM_%6$s",
				this.time, this.name, AgentStates.toString(this.getState()),
				this.AID, EntityTypes.toString(this.entityType), Sim_system
						.get_entity(this.resourceID).get_name());
	}

	public String toString(String agentReport) {
		return String.format(
				"%1$f %2$s HISTORY %3$s %4$s AID%5$s %6$s AM_%7$s", this.time,
				agentReport, this.name, AgentStates.toString(this.getState()),
				this.AID, EntityTypes.toString(this.entityType), Sim_system
						.get_entity(this.resourceID).get_name());
	}

	public double getTime() {
		return this.time;
	}

	public void setTime(double time) {
		this.time = time;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getEntityID() {
		return this.entityID;
	}

	public void setEntityID(int entityID) {
		this.entityID = entityID;
	}

	public int getAID() {
		return this.AID;
	}

	public void setAID(int AID) {
		this.AID = AID;
	}

	public int getEntityType() {
		return this.entityType;
	}

	public void setEntityType(int entityType) {
		this.entityType = entityType;
	}

	public int getResourceID() {
		return this.resourceID;
	}

	public void setResourceID(int resourceID) {
		this.resourceID = resourceID;
	}

	public int getState() {
		return this.state;
	}

	public void setState(int state) {
		this.state = state;
	}

}
