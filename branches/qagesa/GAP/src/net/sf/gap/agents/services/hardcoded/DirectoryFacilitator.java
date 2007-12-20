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
 * DirectoryFacilitator.java
 *
 * Created on 11 March 2007, 12.00 by Giovanni Novelli
 *
 ****************************************************************************************
 *
 * $Revision: 1141 $
 * $Id: DirectoryFacilitator.java 1141 2007-07-18 18:22:15Z gnovelli $
 * $HeadURL: file:///var/svn/grid/trunk/GAP/src/net/sf/gap/agents/services/hardcoded/DirectoryFacilitator.java $
 *
 *****************************************************************************************
 */

package net.sf.gap.agents.services.hardcoded;

import java.util.HashMap;

import eduni.simjava.Sim_event;
import eduni.simjava.Sim_port;

import gridsim.GridSimTags;
import gridsim.IO_data;

import net.sf.gap.GAP;
import net.sf.gap.agents.AbstractAgent;
import net.sf.gap.agents.middleware.AgentPlatform;
import net.sf.gap.agents.services.hardcoded.df.AgentsMap;
import net.sf.gap.agents.services.hardcoded.df.DFMap;
import net.sf.gap.constants.AgentStates;
import net.sf.gap.constants.EntityTypes;
import net.sf.gap.constants.Tags;
import net.sf.gap.messages.impl.AgentReply;
import net.sf.gap.messages.impl.AgentRequest;
import net.sf.gap.grid.components.GridElement;

/**
 * 
 * @author Giovanni Novelli
 */
public class DirectoryFacilitator {
	private AgentPlatform agentPlatform;

	private DFMap directory; // GridAgent

	// ,
	// entity
	// type,
	// agents
	// maps

	// Agents maps are maps of Integer to Integer
	// mapping agent entity IDs to their GE IDs

	private HashMap<Integer, Integer> mapAgentStates; // GridAgent entity ID,

	// GridAgent state

	private HashMap<Integer, Integer> mapEntityTypes; // GridAgent entity ID,

	// GridAgent entity type

	private HashMap<Integer, Integer> mapAIDs; // AID (unique GridAgent

	// IDentity), GridAgent entity
	// ID

	/** Creates a new instance of DirectoryFacilitator */
	public DirectoryFacilitator(AgentPlatform agentPlatform) {
		this.setAgentPlatform(agentPlatform);

		this.setDFMap(new DFMap());

		this.registerAgentsMap(EntityTypes.AGENT_ZOMBIE);

		this.setMapAgentStates(new HashMap<Integer, Integer>());

		this.setMapEntityTypes(new HashMap<Integer, Integer>());

		this.setMapAIDs(new HashMap<Integer, Integer>());
	}

	public void processEvent(Sim_event ev) {
		switch (ev.get_tag()) {
		case Tags.DF_REGISTER_REQ:
			this.sendDF_ACK(ev, this.registerAgent(ev));
			break;
		case Tags.DF_DEREGISTER_REQ:
			this.sendDF_ACK(ev, this.deregisterAgent(ev));
			break;
		case Tags.DF_PAUSED_REQ:
		case Tags.DF_RESUMED_REQ:
			this.sendDF_ACK(ev, this.modifyAgent(ev));
			break;

		default:
			break;
		}
	}

	protected void sendDF_ACK(Sim_event ev, AgentRequest agentRequest) {
		int SIZE = 500;
		AgentReply agentReply = new AgentReply(ev.get_tag(), true, agentRequest);
		this.send(this.getOutput(), GridSimTags.SCHEDULE_NOW, ev.get_tag() + 1,
				new IO_data(agentReply, SIZE, agentRequest.getDst_agentID()));
	}

	private void registerAgentsMap(int entityType) {
		this.getDFMap().registerAgentsMap(entityType);
	}

	public void addAgent(AbstractAgent agent, GridElement ge) {
		this.getMapZombieAgents().put(agent.get_id(), ge.get_id());
		ge.getLocalDirectory().addFreeAgent(agent);
		this.getMapAgentStates().put(agent.get_id(), AgentStates.ZOMBIE);
	}

	protected int getAgentResourceID(int entityType, int aid) {
		return this.getDFMap().getAgentResourceID(entityType, aid);
	}

	protected int locateAID(int AID) {
		int resID = GAP.NOWHERE;
		if (this.getMapAIDs().containsKey(AID)) {
			int aid = this.getMapAIDs().get(AID);
			if (this.getMapZombieAgents().containsKey(aid)) {
				resID = GAP.NOWHERE * this.getMapZombieAgents().get(aid);
			} else {
				int entityType = this.getMapEntityTypes().get(aid);
				resID = this.getAgentResourceID(entityType, aid);
			}
		}
		return resID;
	}

	public void addAgent(int entityType, int aid, int resID) {
		this.getDFMap().addAgent(entityType, aid, resID);
	}

	public AgentRequest registerAgent(Sim_event ev) {
		AgentRequest agentRequest = AgentRequest.get_data(ev);

		this.addAgent(agentRequest.getDst_entityType(), agentRequest
				.getDst_agentID(), agentRequest.getDst_resID());

		this.getMapAgentStates().put(agentRequest.getDst_agentID(),
				AgentStates.RUNNING);
		this.getMapZombieAgents().remove(agentRequest.getDst_agentID());
		this.getMapEntityTypes().put(agentRequest.getDst_agentID(),
				agentRequest.getDst_entityType());
		this.getMapAIDs().put(agentRequest.getDst_AID(),
				agentRequest.getDst_agentID());

		return agentRequest;
	}

	public AgentRequest deregisterAgent(Sim_event ev) {
		AgentRequest agentRequest = AgentRequest.get_data(ev);

		this.removeAgent(agentRequest.getDst_entityType(), agentRequest
				.getDst_agentID());

		this.getMapAgentStates().put(agentRequest.getDst_agentID(),
				AgentStates.ZOMBIE);
		this.getMapZombieAgents().put(agentRequest.getDst_agentID(),
				agentRequest.getDst_resID());
		this.getMapEntityTypes().put(agentRequest.getDst_agentID(),
				EntityTypes.AGENT_ZOMBIE);
		this.getMapAIDs().remove(agentRequest.getDst_AID());

		return agentRequest;
	}

	public AgentRequest modifyAgent(Sim_event ev) {
		AgentRequest agentRequest = AgentRequest.get_data(ev);

		this.getMapAgentStates().put(agentRequest.getDst_agentID(),
				ev.get_tag());

		return agentRequest;
	}

	protected void removeAgent(int entityType, int aid) {
		this.getDFMap().removeAgent(entityType, aid);
	}

	public AgentsMap getAgentsMap(int entityType) {
		return this.getDFMap().getAgentsMap(entityType);
	}

	public AgentsMap getMapZombieAgents() {
		return this.getDFMap().getMapZombieAgents();
	}

	public void setMapZombieAgents(AgentsMap mapZombieAgents) {
		this.getDFMap().setMapZombieAgents(mapZombieAgents);
	}

	/**
	 * @param agentPlatform
	 *            the agentPlatform to set
	 */
	public void setAgentPlatform(AgentPlatform agentPlatform) {
		this.agentPlatform = agentPlatform;
	}

	/**
	 * @return the agentPlatform
	 */
	public AgentPlatform getAgentPlatform() {
		return agentPlatform;
	}

	public DFMap getDFMap() {
		return directory;
	}

	public void setDFMap(DFMap directory) {
		this.directory = directory;
	}

	public HashMap<Integer, Integer> getMapAgentStates() {
		return mapAgentStates;
	}

	public void setMapAgentStates(HashMap<Integer, Integer> mapAgentStates) {
		this.mapAgentStates = mapAgentStates;
	}

	public HashMap<Integer, Integer> getMapEntityTypes() {
		return mapEntityTypes;
	}

	public void setMapEntityTypes(HashMap<Integer, Integer> mapEntityTypes) {
		this.mapEntityTypes = mapEntityTypes;
	}

	public HashMap<Integer, Integer> getMapAIDs() {
		return mapAIDs;
	}

	public void setMapAIDs(HashMap<Integer, Integer> mapAIDs) {
		this.mapAIDs = mapAIDs;
	}

	private Sim_port getOutput() {
		return this.getAgentPlatform().getOutput();
	}

	private void send(Sim_port destPort, double delay, int gridSimTag,
			Object data) {
		this.getAgentPlatform().send(destPort, delay, gridSimTag, data);
	}
}
