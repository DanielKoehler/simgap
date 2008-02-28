/*
 *******************************************************************************
 * Copyright © Giovanni Novelli                                             
 * All Rights Reserved.                                                                 
 *******************************************************************************
 *
 * Title:        GAP Simulator
 * Description:  GAP (Grid Agents Platform) Toolkit for Modeling and Simulation
 *               of Mobile Agents on Grids
 * License:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * XMLNetworkTopology.java
 *
 * Created on 26 January 2008, 19.00 by Giovanni Novelli
 *
 *******************************************************************************
 *
 * $Revision$
 * $Id$
 * $HeadURL$
 *
 *******************************************************************************
 */
package net.sf.gap.xml.impl;

import eduni.simjava.Sim_system;
import gridsim.net.FIFOScheduler;
import gridsim.net.Link;
import gridsim.net.RIPRouter;
import gridsim.net.SimpleLink;

import java.util.LinkedList;
import java.util.Vector;

import net.sf.gap.grid.NetworkTopology;
import net.sf.gap.xml.types.LinkType;
import net.sf.gap.xml.types.RouterType;
import net.sf.gap.xml.types.RouterListType;
import net.sf.gap.xml.types.ScenarioType;

/**
 * 
 * @author Giovanni Novelli
 */
public class XMLNetworkTopology extends NetworkTopology {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8875327992608003476L;
	private ScenarioType scenario;
        private boolean trace;

	public XMLNetworkTopology(ScenarioType scenario, boolean trace_flag)
			throws Exception {
		super(scenario.getNumRouters());
		this.setScenario(scenario);
                this.setTrace(trace_flag);
		this.create(trace_flag);
	}

	@Override
	public void initialize() {
		int N = this.getNumRouters();
		this.setRouters(new Vector<RIPRouter>(N));
		RouterListType routerItems = 
                        this.getScenario().getTopology().getRouters();
		for (int i = 0; i < N; i++) {
			String routerName = routerItems.get(i).getName();
			RIPRouter router = 
                                new RIPRouter(routerName,this.isTrace());
			this.addElement(router);
		}
	}

	@Override
	public void create(boolean trace) throws Exception {
		int N = this.getNumRouters();
		this.setRouters(new Vector<RIPRouter>(N));
		RouterListType routerItems = 
                        this.getScenario().getTopology().getRouters();
		for (int i = 0; i < N; i++) {
			String routerName = routerItems.get(i).getName();
                        System.out.println(i + " " + routerName);
			RIPRouter router = new RIPRouter(routerName, trace);
			this.addElement(router);
		}

		LinkedList<LinkType> linkItems = 
                        this.getScenario().getTopology().getLinks();
		for (int i = 0; i < linkItems.size(); i++) {
			LinkType linkItem = linkItems.get(i);
			String from = linkItem.getFromEntity();
			String to = linkItem.getToEntity();
			boolean isNetworkLink = 
                                (
                                    routerItems.contains(new RouterType(from)) 
                                    && 
                                    routerItems.contains(new RouterType(to)) 
                                );
			if (isNetworkLink) {
				RIPRouter r1 = 
                                        (RIPRouter) Sim_system.get_entity(from);
				RIPRouter r2 = 
                                        (RIPRouter) Sim_system.get_entity(to);
				if (linkItem.isBidirectional()) {
					FIFOScheduler resSched1 = 
                                                new FIFOScheduler(
                                                "sched_" + 
                                                from + 
                                                "_" + 
                                                to);
					FIFOScheduler resSched2 = 
                                                new FIFOScheduler(
                                                "sched_" + 
                                                to + 
                                                "_" + 
                                                from);
					Link link = 
                                                new SimpleLink(
                                                linkItem.getName(), 
                                                linkItem.getBaudrate(), 
                                                linkItem.getDelay(), 
                                                linkItem.getMTU());
					r1.attachRouter(
                                                r2, 
                                                link, 
                                                resSched1, 
                                                resSched2);
				} else {
					/*
					 * @TODO check this part of code
					 */
					FIFOScheduler resSched1 = 
                                                new FIFOScheduler(
                                                "sched_" + 
                                                from + 
                                                "_" + 
                                                to);
					Link link = 
                                                new SimpleLink(
                                                linkItem.getName(), 
                                                linkItem.getBaudrate(), 
                                                linkItem.getDelay(), 
                                                linkItem.getMTU());
					r1.attachRouter(r2, link, resSched1);
				}
			}
		}
	}

	private ScenarioType getScenario() {
		return scenario;
	}

	private void setScenario(ScenarioType scenario) {
		this.scenario = scenario;
	}

    public boolean isTrace() {
        return trace;
    }

    public void setTrace(boolean trace) {
        this.trace = trace;
    }
}
