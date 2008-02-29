/*
 ****************************************************************************************
 * Copyright © Giovanni Novelli                                             
 * All Rights Reserved.                                                                 
 ****************************************************************************************
 *
 * Title:        AbstractGAP Simulator
 * Description:  AbstractGAP (Grid Agents Platform) Toolkit for Modeling and Simulation
 *               of Mobile Agents on Grids
 * License:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * DFMap.java
 *
 * Created on 11 March 2007, 12.00 by Giovanni Novelli
 *
 ****************************************************************************************
 *
 * $Revision$
 * $Id$
 * $HeadURL$
 *
 *****************************************************************************************
 */

package net.sf.gap.agents.services.hardcoded.df;

import java.util.HashMap;

import net.sf.gap.AbstractGAP;
import net.sf.gap.constants.EntityTypes;

/**
 * 
 * @author Giovanni Novelli
 */
public class DFMap extends HashMap<Integer, AgentsMap> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 217466636888949413L;

	public int getAgentResourceID(int entityType, int aid) {
		int resID = AbstractGAP.NOWHERE;
		if (this.containsKey(entityType)) {
			AgentsMap agentsMap = this.get(entityType);
			resID = agentsMap.get(aid);
		}
		return resID;
	}

	public void registerAgentsMap(int entityType) {
		if (!this.containsKey(entityType)) {
			AgentsMap agentsMap = new AgentsMap();
			this.put(entityType, agentsMap);
		}
	}

	public void addAgent(int entityType, int aid, int resID) {
		this.registerAgentsMap(entityType);
		AgentsMap agentsMap = this.get(entityType);
		agentsMap.put(aid, resID);
	}

	public void removeAgent(int entityType, int aid) {
		AgentsMap agentsMap = this.get(entityType);
		agentsMap.remove(aid);
	}

	public AgentsMap getAgentsMap(int entityType) {
		return this.get(entityType);
	}

	public AgentsMap getMapZombieAgents() {
		return this.get(EntityTypes.AGENT_ZOMBIE);
	}

	public void setMapZombieAgents(AgentsMap mapZombieAgents) {
		this.put(EntityTypes.AGENT_ZOMBIE, mapZombieAgents);
	}
}
