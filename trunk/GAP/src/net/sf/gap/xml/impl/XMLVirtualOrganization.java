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
 * XMLVirtualOrganization.java
 *
 * Created on 26 Janauary 2008, 19.30 by Giovanni Novelli
 *
 * $Id$
 *
 */
package net.sf.gap.xml.impl;

import eduni.simjava.Sim_system;
import gridsim.Machine;
import gridsim.MachineList;
import gridsim.PE;
import gridsim.PEList;
import gridsim.ResourceCalendar;
import gridsim.ResourceCharacteristics;
import gridsim.datagrid.SimpleReplicaManager;
import gridsim.datagrid.index.TopRegionalRC;
import gridsim.datagrid.storage.HarddriveStorage;
import gridsim.datagrid.storage.Storage;
import gridsim.net.FIFOScheduler;
import gridsim.net.Link;
import gridsim.net.RIPRouter;
import gridsim.net.SimpleLink;

import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Vector;

import net.sf.gap.agents.middleware.AgentMiddleware;
import net.sf.gap.grid.AbstractVirtualOrganization;
import net.sf.gap.grid.components.AbstractGridElement;
import net.sf.gap.grid.components.GridElement;
import net.sf.gap.xml.types.GridElementType;
import net.sf.gap.xml.types.HardDiskType;
import net.sf.gap.xml.types.LinkType;
import net.sf.gap.xml.types.MachineListType;
import net.sf.gap.xml.types.MachineType;
import net.sf.gap.xml.types.PEType;
import net.sf.gap.xml.types.ScenarioType;

/**
 * 
 * @author Giovanni Novelli
 */
public abstract class XMLVirtualOrganization extends
		AbstractVirtualOrganization {

	private ScenarioType scenario;

	public XMLVirtualOrganization(ScenarioType scenario, boolean trace_flag)
			throws Exception {
		this.setScenario(scenario);
		int numGEs = scenario.getGrid().getGridElements().size();
		int countSEs = 0;
		int countCEs = 0;
		for (int i = 0; i < numGEs; i++) {
			GridElementType ge = scenario.getGrid().getGridElements().get(i);
			if (ge.isSE()) {
				countSEs++;
			} else {
				countCEs++;
			}
		}
		this.setTraceFlag(trace_flag);
		this.setNumCEs(countCEs);
		this.setNumSEs(countSEs);
		this.createEntities();
	}

	@Override
	public void initialize() {
		this.getTopology().initialize();
		this.mapCEs = new HashMap<Integer, RIPRouter>(this.getNumCEs());
		this.mapSEs = new HashMap<Integer, RIPRouter>(this.getNumSEs());
		this.setCEs(new Vector<AbstractGridElement>(this.getNumCEs()));
		this.setSEs(new Vector<AbstractGridElement>(this.getNumSEs()));
		this.setAMs(new Vector<AgentMiddleware>(this.getNumAMs()));
		this.initializeCEs();
		this.initializeSEs();
		this.initializeAgents();
	}

	@Override
	protected void initializeCEs() {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	protected void initializeSEs() {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	abstract protected void initializeAgents();

	@Override
	protected void createEntities() throws Exception {
		this.setDataGIS(this.createDataGIS());
		this.setTopRegionalRC(this.createTopRegionalRC());
		this.setTopology(new XMLNetworkTopology(this.getScenario(), this
				.isTraceFlag()));
		FIFOScheduler rcSched = new FIFOScheduler("trrc_sched");
		RIPRouter router = this.getTopology().get(0);
		router.attachHost(this.getTopRegionalRC(), rcSched);
		this.createAndAttachCEs();
		this.createAndAttachSEs();
		this.createAndAttachAgentPlatform();
		this.createAndAttachAgents();
		this.createAndAttachUsers();
	}

	@Override
	public void createAndAttachAgentPlatform() throws Exception {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void createAndAttachAgents() throws Exception {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@SuppressWarnings("unchecked")
	@Override
	public void createAndAttachCEs() throws Exception {
		for (int i = 0; i < this.getNumCEs(); i++) {
			GridElementType geItem = scenario.getGrid().getGridElements()
					.get(i);
			String linkName = geItem.getLink();
			if (scenario.getTopology().getMapLinks().containsKey(linkName)) {
				LinkType linkItem = scenario.getTopology().getMapLinks().get(
						linkName);
				if (linkItem.isBidirectional()) {
					Link link = new SimpleLink(linkItem.getName(), linkItem
							.getBaudrate(), linkItem.getDelay(), linkItem
							.getMTU());

					String arch = "System Architecture"; // system
															// architecture
					String os = "Operating System"; // operating system
					double time_zone = 12.0; // time zone this resource
												// located
					double cost = 1.0; // the cost of using this resource
					MachineListType mListType = geItem.getMachineList();
					MachineList mList = new MachineList();
					int np = 0;
					int m = mListType.getItems().size();
					for (int j = 0; i < m; j++) {
						MachineType mType = mListType.getItems().get(j);
						PEList peList = new PEList();
						for (int k = 0; k < mType.getPeList().getItems().size(); k++) {
							PEType peType = mType.getPeList().getItems().get(k);
							peList.add(new PE(k, peType.getMIPS()));
							np++;
						}
						mList.add(new Machine(j, peList));
					}
					ResourceCharacteristics resConfig = new ResourceCharacteristics(
							arch, os, mList,
							ResourceCharacteristics.SPACE_SHARED, time_zone,
							cost);
					String geName = geItem.getName();
					long seed = 11L * 13 * 17 * 19 * 23 + 1;
					double peakLoad = 0.0; // the resource load during peak
											// hour
					double offPeakLoad = 0.0; // the resource load during
												// off-peak hr
					double holidayLoad = 0.0; // the resource load during
												// holiday
					// incorporates weekends so the grid resource is on 7 days a
					// week
					LinkedList<Integer> Weekends = new LinkedList<Integer>();
					Weekends.add(new Integer(Calendar.SATURDAY));
					Weekends.add(new Integer(Calendar.SUNDAY));
					// incorporates holidays. However, no holidays are set in
					// this example
					LinkedList Holidays = new LinkedList();
					GridElement ge = null;
					try {
						// create the resource calendar
						ResourceCalendar cal = new ResourceCalendar(time_zone,
								peakLoad, offPeakLoad, holidayLoad, Weekends,
								Holidays, seed);

						// create the replica manager

						SimpleReplicaManager rm = new SimpleReplicaManager(
								"RM_" + geName, geName); // create a storage
						ge = new GridElement(geName, link, resConfig, cal, rm);
						if (geItem.isSE()) {
							ge.setSE(true);
							for (int ihd = 0; ihd < geItem.getStorage()
									.getHardDiskList().getItems().size(); ihd++) {
								HardDiskType hdType = geItem.getStorage()
										.getHardDiskList().getItems().get(ihd);
								Storage storage = new HarddriveStorage(hdType
										.getName(),
										hdType.getCapacity() * 1000000000);
								ge.addStorage(storage);
							}
						} else {
							ge.setSE(false);
						}
						ge.createLocalRC();
						ge
								.setHigherReplicaCatalogue(TopRegionalRC.DEFAULT_NAME);
						ge.setNumPE(np);
						ge.setNumWN(m);
					} catch (Exception e) {
						e.printStackTrace();
					}
					RIPRouter router = (RIPRouter) Sim_system
							.get_entity(linkItem.getToEntity());
					ge.attachRouter(router);
				} else {
					throw new Exception("Missing link to network for GE "
							+ geItem.getName());
				}
			}
		}
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void createAndAttachSEs() throws Exception {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void createAndAttachUsers() throws Exception {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	private ScenarioType getScenario() {
		return scenario;
	}

	private void setScenario(ScenarioType scenario) {
		this.scenario = scenario;
	}
}
