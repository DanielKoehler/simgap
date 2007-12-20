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
 * GISRepository.java
 *
 * Created on 25 August 2006, 12.00 by Giovanni Novelli
 *
 ****************************************************************************************
 *
 * $Revision: 1141 $
 * $Id: GISRepository.java 1141 2007-07-18 18:22:15Z gnovelli $
 * $HeadURL: file:///var/svn/grid/trunk/GAP/src/net/sf/gap/agents/services/impl/gis/GISRepository.java $
 *
 *****************************************************************************************
 */

package net.sf.gap.agents.services.impl.gis;

import eduni.simjava.Sim_system;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;

import net.sf.gap.agents.services.impl.gis.GISEntry;
import net.sf.gap.agents.middleware.AgentPlatform;
import net.sf.gap.grid.components.GridElement;

/**
 * 
 * @author Giovanni Novelli
 */
public class GISRepository extends HashMap<Integer, GISEntry> {
	private double lastRequestTime;

	private AgentPlatform agentPlatform;

	private LinkedList<GridElement> listGEs; // list of GEs hosting

	/**
	 * 
	 */
	private static final long serialVersionUID = -3390353402842833504L;

	/**
	 * Creates a new instance of GISRepository
	 */
	public GISRepository(AgentPlatform agentPlatform) {
		super();
		this.setAgentPlatform(agentPlatform);

		this.setListGEs(new LinkedList<GridElement>());

		this.setLastRequestTime(0);
	}

	@Override
	public String toString() {
		String msg = "";
		Set<Integer> set = this.keySet();
		Iterator<Integer> it = set.iterator();
		while (it.hasNext()) {
			Integer geid = it.next();
			GISEntry entry = this.get(geid);
			msg = msg + "{" + Sim_system.get_entity(geid).get_name() + ", "
					+ entry + "}\n";
		}
		return msg;
	}

	public void addGE(GridElement ge) {
		this.getListGEs().add(ge);
	}

	public void removeGE(GridElement ge) {
		this.getListGEs().remove(ge);
	}

	public LinkedList<GridElement> getListGEs() {
		return this.listGEs;
	}

	public void setListGEs(LinkedList<GridElement> listGEs) {
		this.listGEs = listGEs;
	}

	public AgentPlatform getAgentPlatform() {
		return agentPlatform;
	}

	public void setAgentPlatform(AgentPlatform agentPlatform) {
		this.agentPlatform = agentPlatform;
	}

	public double getLastRequestTime() {
		return lastRequestTime;
	}

	public void setLastRequestTime(double lastRequestTime) {
		this.lastRequestTime = lastRequestTime;
	}
}
