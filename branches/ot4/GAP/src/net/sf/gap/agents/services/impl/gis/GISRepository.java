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
 * $Revision$
 * $Id$
 * $HeadURL$
 *
 *****************************************************************************************
 */

package net.sf.gap.agents.services.impl.gis;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;

import net.sf.gap.agents.middleware.AbstractAgentPlatform;
import net.sf.gap.grid.components.AbstractGridElement;
import eduni.simjava.Sim_system;

/**
 * 
 * @author Giovanni Novelli
 */
public class GISRepository extends HashMap<Integer, GISEntry> {
	private double lastRequestTime;

	private AbstractAgentPlatform agentPlatform;

	private LinkedList<AbstractGridElement> listGEs; // list of GEs hosting

	/**
	 * 
	 */
	private static final long serialVersionUID = -3390353402842833504L;

	/**
	 * Creates a new instance of GISRepository
	 */
	public GISRepository(AbstractAgentPlatform agentPlatform) {
		super();
		this.setAgentPlatform(agentPlatform);

		this.setListGEs(new LinkedList<AbstractGridElement>());

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

	public void addGE(AbstractGridElement ge) {
		this.getListGEs().add(ge);
	}

	public void removeGE(AbstractGridElement ge) {
		this.getListGEs().remove(ge);
	}

	public LinkedList<AbstractGridElement> getListGEs() {
		return this.listGEs;
	}

	public void setListGEs(LinkedList<AbstractGridElement> listGEs) {
		this.listGEs = listGEs;
	}

	public AbstractAgentPlatform getAgentPlatform() {
		return agentPlatform;
	}

	public void setAgentPlatform(AbstractAgentPlatform agentPlatform) {
		this.agentPlatform = agentPlatform;
	}

	public double getLastRequestTime() {
		return lastRequestTime;
	}

	public void setLastRequestTime(double lastRequestTime) {
		this.lastRequestTime = lastRequestTime;
	}
}
