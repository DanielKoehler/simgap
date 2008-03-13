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
 * NMService.java
 *
 * Created on 12 March 2007, 12.00 by Giovanni Novelli
 *
 ****************************************************************************************
 *
 * $Revision$
 * $Id$
 * $HeadURL$
 *
 *****************************************************************************************
 */

package net.sf.gap.agents.services.impl;

import eduni.simjava.Sim_event;
import gridsim.GridSimTags;
import gridsim.IO_data;
import gridsim.net.InfoPacket;

import java.util.Iterator;

import junit.framework.Assert;
import net.sf.gap.agents.middleware.AbstractAgentPlatform;
import net.sf.gap.agents.predicates.Predicate;
import net.sf.gap.agents.services.PlatformService;
import net.sf.gap.agents.services.impl.nm.NetworkMap;
import net.sf.gap.constants.Tags;
import net.sf.gap.distributions.Uniform_int;
import net.sf.gap.grid.components.AbstractGridElement;
import net.sf.gap.messages.impl.*;

/**
 * This class is responsible for Network Monitor Service
 * 
 * @author Giovanni Novelli
 * @see net.p2pgrid.agents.services.PlatformService
 */
public class NMService extends PlatformService {

	private NetworkMap networkMap;
        private Uniform_int randgis;
        private double nmCacheTime;

	/**
	 * @param ap
	 * @param name
	 * @param trace_flag
	 * @throws Exception
	 */
	public NMService(AbstractAgentPlatform ap, boolean trace_flag, double nmCacheTime)
			throws Exception {
		super(ap, "NMService", trace_flag);
                this.setNmCacheTime(nmCacheTime);
                randgis = new Uniform_int("rand_gis");
	}

	@Override
	public void initialize() throws Exception {
		super.initialize();
		this.setNetworkMap(new NetworkMap());
	}

        @Override
        protected void doSomething() {
            double currentTime = NMService.clock();
            if ((currentTime-this.getNetworkMap().getLastRequestTime())>this.getNmCacheTime()) {
                this.processNM();
            }
        }

    public void processNM() {
        double currentTime;
        int numCEs = this.getAgentPlatform().getVirtualOrganization().getNumCEs();
        int numSEs = this.getAgentPlatform().getVirtualOrganization().getNumCEs();
        int ceidx = randgis.sample(numCEs);
        int seidx = randgis.sample(numSEs);
        AbstractGridElement ce = this.getAgentPlatform().getVirtualOrganization().getCEs().get(ceidx);
        AbstractGridElement se = this.getAgentPlatform().getVirtualOrganization().getSEs().get(seidx);
        int ceid = ce.get_id();
        int seid = se.get_id();
        currentTime = NMService.clock();
        this.asyncRequestPing(ceid, seid);
        this.getNetworkMap().setLastRequestTime(currentTime);
    }
        
    private void asyncRequestPing(int src_id, int dst_id) {
        int SIZE = 10;
        PingRequest request = new PingRequest(this.get_id(), this.get_id(),
                src_id, dst_id);
        int requestID = request.getRequestID();
        this.send(super.output, GridSimTags.SCHEDULE_NOW, Tags.PING_REQ,
                new IO_data(request, SIZE, src_id));

        try {
            Sim_event ev = new Sim_event();
            Predicate predicate = new Predicate(Tags.PING_REP);
            this.sim_get_next(predicate, ev); // only look for this type of ack
            PingReply reply = PingReply.get_data(ev);
            InfoPacket pkt = reply.getPkt();
            if (pkt != null) {
                this.addRTT(src_id, dst_id, pkt);
            }
        } catch ( Exception e) {
        }
    }
    
        
        
	@Override
	protected void dispose() {
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

	public void syncRandomRequestPing() {
		Uniform_int r = new Uniform_int("asyncRandomRequestPing");
		int size = this.getAgentPlatform().getGisService().getGisRepository()
				.getListGEs().size();
		int i1 = r.sample(size);
		int i2 = r.sample(size);
		int ge1id = this.getAgentPlatform().getGisService().getGisRepository()
				.getListGEs().get(i1).get_id();
		int ge2id = this.getAgentPlatform().getGisService().getGisRepository()
				.getListGEs().get(i2).get_id();
		if (ge1id != ge2id) {
			this.requestPing(ge1id, ge2id);
		}
	}

	public void asyncProcessNetworkMap() {
		Iterator<AbstractGridElement> it1 = this.getAgentPlatform()
				.getGisService().getGisRepository().getListGEs().iterator();
		while (it1.hasNext()) {
			Iterator<AbstractGridElement> it2 = this.getAgentPlatform()
					.getGisService().getGisRepository().getListGEs().iterator();
			AbstractGridElement ge1 = it1.next();
			int ge1id = ge1.get_id();
			while (it2.hasNext()) {
				AbstractGridElement ge2 = it2.next();
				int ge2id = ge2.get_id();
				if (ge1id != ge2id) {
					this.asyncRequestPing(ge1id, ge2id);
				}
			}
		}
	}

	public void processNetworkMap() {
		Iterator<AbstractGridElement> it1 = this.getAgentPlatform()
				.getGisService().getGisRepository().getListGEs().iterator();
		while (it1.hasNext()) {
			Iterator<AbstractGridElement> it2 = this.getAgentPlatform()
					.getGisService().getGisRepository().getListGEs().iterator();
			AbstractGridElement ge1 = it1.next();
			int ge1id = ge1.get_id();
			while (it2.hasNext()) {
				AbstractGridElement ge2 = it2.next();
				int ge2id = ge2.get_id();
				if (ge1id != ge2id) {
					this.requestPing(ge1id, ge2id);
				}
			}
		}
	}

	@Override
	public void processOtherEvent(Sim_event ev) {
		switch (ev.get_tag()) {
            case Tags.NM_NETWORKMAP_REQ:
                int SIZE = 500;
                NetworkMapRequest nmRequest = NetworkMapRequest.get_data(ev);
                NetworkMapReply nmReply = new NetworkMapReply(ev.get_tag(), true,
                        nmRequest, this.getNetworkMap());
                this.send(super.output, GridSimTags.SCHEDULE_NOW,
                        Tags.NM_NETWORKMAP_REP, new IO_data(nmReply, SIZE,
                        nmRequest.getSrc_ID()));
                break;

                    case Tags.PING_REP:
			PingReply reply = PingReply.get_data(ev);
			InfoPacket pkt = reply.getPkt();
			if (pkt != null) {
				this.addRTT(pkt.getSrcID(), pkt.getDestID(), pkt);
			}
			break;

		default:
			break;
		}
	}

	private PingReply oldasyncRequestPing(int src_id, int dst_id) {
		int SIZE = 500;
		PingRequest request = new PingRequest(this.get_id(), this.get_id(),
				src_id, dst_id);
		int requestID = request.getRequestID();
		@SuppressWarnings("unused")
		int reqrepID = request.getReqrepID();
		super.send(super.output, GridSimTags.SCHEDULE_NOW, Tags.PING_REQ,
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
		int SIZE = 500;
		PingRequest request = new PingRequest(this.get_id(), this.get_id(),
				src_id, dst_id);
		int requestID = request.getRequestID();
		@SuppressWarnings("unused")
		int reqrepID = request.getReqrepID();
		super.send(super.output, GridSimTags.SCHEDULE_NOW, Tags.PING_REQ,
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

    public double getNmCacheTime() {
        return nmCacheTime;
    }

    public void setNmCacheTime(double nmCacheTime) {
        this.nmCacheTime = nmCacheTime;
    }
}
