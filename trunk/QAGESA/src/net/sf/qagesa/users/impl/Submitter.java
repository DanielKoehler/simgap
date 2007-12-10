/*
 ****************************************************************************************
 * Copyright © Giovanni Novelli                                             
 * All Rights Reserved.                                                                 
 ****************************************************************************************
 *
 * Title:        QAGESA Simulator 
 * Description:  QAGESA (QoS Aware Grid Enabled Streaming Architecture) Simulator
 *               of a QoS-Aware Architecture for Multimedia Content Provisioning in a GRID Environment
 * License:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * Submitter.java
 *
 * Created on 6 March 2007, 14.23 by Giovanni Novelli
 *
 * $Id: Submitter.java 1142 2007-07-18 20:49:58Z gnovelli $
 *
 */

package net.sf.qagesa.users.impl;

import eduni.simjava.Sim_event;

import gridsim.Gridlet;
import gridsim.net.Link;

import net.sf.gap.distributions.Uniform_int;
import net.sf.gap.messages.impl.GridletReply;
import net.sf.gap.messages.impl.AgentReply;
import net.sf.gap.util.EntitiesCounter;
import net.sf.qagesa.constants.QAGESAEntityTypes;
import net.sf.qagesa.grid.components.QAGESAGridElement;
import net.sf.qagesa.users.QAGESAUser;

/**
 * @author Giovanni Novelli
 */
public class Submitter extends QAGESAUser {
	/**
	 * 
	 * Creates a new instance of Submitter
	 * 
	 * @param name
	 *            AgentMiddleware entity name
	 * @param trace_flag
	 *            GridSim trace flag
	 * @throws Exception
	 *             This happens when name is null or haven't initialized
	 *             GridSim.
	 */
	public Submitter(String name, Link link, boolean trace_flag)
			throws Exception {
		super(name, link, QAGESAEntityTypes.USER_SUBMITTER, trace_flag);
	}

        public void printStats() {
        }
        
        @Override
	public void processOtherEvent(Sim_event ev) {
	}

        @Override
	public void initWork() {
		this.DoIt();
	}

	private void DoIt() {
		this.DoGridlets();
	}

	private void DoGridlets() {
		Uniform_int r = new Uniform_int("nextaege");
		AgentReply agentReply = null;
		QAGESAGridElement agentsEnabledGridElement = null;
		QAGESAGridElement next_agentsEnabledGridElement = null;
		int aegeResourceID;
		int next_aegeResourceID;
		int i = 0;
		int ii = 0;
		for (int j = 0; j < 10; j++) {
			i = r.sample(this.getVirtualOrganization().getCEs().size());
			agentsEnabledGridElement = (QAGESAGridElement) this.getVirtualOrganization()
					.getCEs().get(i);
			aegeResourceID = agentsEnabledGridElement.get_id();
			agentReply = this.submitAgent(QAGESAEntityTypes.SERVER_PROXY,
					aegeResourceID, 10000);
			if (agentReply.isOk()) {
				GridletReply gridletReply = null;
				gridletReply = this.newGridlet(agentReply);
				if (gridletReply.isOk()) {
					do {
						agentReply = this.hasGridletsAgent(agentReply
								.getRequest(), false);
					} while (agentReply.isOk());
				}
				do {
					ii = r.sample(this.getVirtualOrganization().getCEs().size());
				} while (i == ii);
				next_agentsEnabledGridElement = (QAGESAGridElement) this
						.getVirtualOrganization().getCEs().get(ii);
				next_aegeResourceID = next_agentsEnabledGridElement.get_id();
				agentReply = this.killWaitAgent(agentReply.getRequest());
                                /*
				agentReply = this.killAgent(agentReply.getRequest());
                                 */
			}
		}
	}

	private GridletReply newGridlet(AgentReply agentReply) {
		if (!EntitiesCounter.contains("Gridlet")) {
			EntitiesCounter.create("Gridlet");
		}
		double length = 5000.0;
		long file_size = 300;
		long output_size = 300;
		Gridlet g = new Gridlet(EntitiesCounter.inc("Gridlet"), length,
				file_size, output_size);
		GridletReply gridletReply = null;
		gridletReply = this.submitGridletToAgent(agentReply.getRequest()
				.getDst_agentID(), agentReply.getRequest().getDst_resID(), g);
		return gridletReply;
	}
}
