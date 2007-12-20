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
 * NetworkMonitor.java
 *
 * Created on 11 March 2007, 12.00 by Giovanni Novelli
 *
 ****************************************************************************************
 *
 * $Revision: 1141 $
 * $Id: NetworkMonitor.java 1141 2007-07-18 18:22:15Z gnovelli $
 * $HeadURL: file:///var/svn/grid/trunk/GAP/src/net/sf/gap/agents/services/hardcoded/NetworkMonitor.java $
 *
 *****************************************************************************************
 */

package net.sf.gap.agents.services.hardcoded;

import eduni.simjava.Sim_event;
import eduni.simjava.Sim_port;
import eduni.simjava.Sim_predicate;
import gridsim.GridSimTags;
import gridsim.IO_data;
import gridsim.net.InfoPacket;

import java.util.Iterator;

import junit.framework.Assert;

import net.sf.gap.constants.Tags;
import net.sf.gap.distributions.Uniform_int;
import net.sf.gap.agents.middleware.AgentPlatform;
import net.sf.gap.agents.predicates.Predicate;
import net.sf.gap.agents.services.impl.nm.NetworkMap;
import net.sf.gap.grid.components.GridElement;
import net.sf.gap.messages.impl.PingReply;
import net.sf.gap.messages.impl.PingRequest;
import net.sf.gap.messages.impl.NetworkMapRequest;
import net.sf.gap.messages.impl.NetworkMapReply;

/**
 * 
 * @author Giovanni Novelli
 */
public class NetworkMonitor {
	private AgentPlatform agentPlatform;

	private NetworkMap networkMap;

	/** Creates a new instance of NetworkMonitor */
	public NetworkMonitor(AgentPlatform agentPlatform) {
		this.setAgentPlatform(agentPlatform);
		this.setNetworkMap(new NetworkMap());
	}

	public NetworkMap getNetworkMap() {
		return this.networkMap;
	}

	public void setNetworkMap(NetworkMap networkMap) {
		this.networkMap = networkMap;
	}

	private void addRTT(Integer src, Integer dst, InfoPacket pkt) {
		this.getNetworkMap().addRTT(src, dst, pkt);
	}

	public void showNetworkMap() {
		this.getNetworkMap().show();
	}

	public void asyncProcessNetworkMap() {
		Iterator<GridElement> it1 = this.getAgentPlatform().getGisService().getGisRepository().getListGEs().iterator();
		while (it1.hasNext()) {
			Iterator<GridElement> it2 = this.getAgentPlatform().getGisService()
					.getGisRepository().getListGEs().iterator();
			GridElement ge1 = it1.next();
			int ge1id = ge1.get_id();
			while (it2.hasNext()) {
				GridElement ge2 = it2.next();
				int ge2id = ge2.get_id();
				if (ge1id != ge2id) {
					this.asyncRequestPing(ge1id, ge2id);
				}
			}
		}
	}

	public void processEvent(Sim_event ev) {
		switch (ev.get_tag()) {
		case Tags.NM_NETWORKMAP_REQ:
			int SIZE = 500;
			NetworkMapRequest nmRequest = NetworkMapRequest.get_data(ev);
			NetworkMapReply nmReply = new NetworkMapReply(ev.get_tag(), true,
					nmRequest, this.getNetworkMap());
			this.send(this.getOutput(), GridSimTags.SCHEDULE_NOW,
					Tags.NM_NETWORKMAP_REP, new IO_data(nmReply, SIZE,
							nmRequest.getSrc_ID()));
			break;

                    default:
			break;
		}
	}

	public AgentPlatform getAgentPlatform() {
		return agentPlatform;
	}

	public void setAgentPlatform(AgentPlatform agentPlatform) {
		this.agentPlatform = agentPlatform;
	}

	private PingReply asyncRequestPing(int src_id, int dst_id) {
		int SIZE = 10;
		PingRequest request = new PingRequest(this.get_id(), this.get_id(),
				src_id, dst_id);
		int requestID = request.getRequestID();
		int reqrepID = request.getReqrepID();
		this.send(this.getOutput(), GridSimTags.SCHEDULE_NOW, Tags.PING_REQ,
				new IO_data(request, SIZE, src_id));

		Sim_event ev = new Sim_event();
		Predicate predicate = new Predicate(Tags.PING_REP);
		this.sim_get_next(predicate, ev); // only look for this type of ack
		PingReply reply = PingReply.get_data(ev);
		Assert.assertEquals(requestID, reply.getRequestID());
		Assert.assertEquals(Tags.PING_REQ, reply.getRequestTAG());
		Assert.assertEquals(Tags.PING_REP, ev.get_tag());
		InfoPacket pkt = reply.getPkt();
		if (pkt != null) {
			this.addRTT(src_id, dst_id, pkt);
		}
		return reply;
	}

	private PingReply requestPing(int src_id, int dst_id) {
		int SIZE = 10;
		PingRequest request = new PingRequest(this.get_id(), this.get_id(),
				src_id, dst_id);
		int requestID = request.getRequestID();
		int reqrepID = request.getReqrepID();
		this.send(this.getOutput(), GridSimTags.SCHEDULE_NOW, Tags.PING_REQ,
				new IO_data(request, SIZE, src_id));

		Sim_event ev = new Sim_event();
		Predicate predicate = new Predicate(Tags.PING_REP);
		this.sim_get_next(predicate, ev); // only look for this type of ack
		PingReply reply = PingReply.get_data(ev);
		Assert.assertEquals(requestID, reply.getRequestID());
		Assert.assertEquals(Tags.PING_REQ, reply.getRequestTAG());
		Assert.assertEquals(Tags.PING_REP, ev.get_tag());
		InfoPacket pkt = reply.getPkt();
		if (pkt != null) {
			this.addRTT(src_id, dst_id, pkt);
		}
		return reply;
	}

	private int get_id() {
		return this.getAgentPlatform().get_id();
	}

	private Sim_port getOutput() {
		return this.getAgentPlatform().getOutput();
	}

	private void send(Sim_port destPort, double delay, int gridSimTag,
			Object data) {
		this.getAgentPlatform().send(destPort, delay, gridSimTag, data);
	}

	private void sim_get_next(Sim_predicate p, Sim_event ev) {
		this.getAgentPlatform().sim_get_next(p, ev);
	}
}
