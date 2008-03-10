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
 * GISService.java
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

import eduni.simjava.*;
import net.sf.gap.GAP;
import net.sf.gap.agents.middleware.AbstractAgentPlatform;
import net.sf.gap.agents.services.PlatformService;
import net.sf.gap.agents.services.impl.gis.GISEntry;
import net.sf.gap.agents.services.impl.gis.GISRepository;
import net.sf.gap.constants.Tags;
import net.sf.gap.grid.components.AbstractGridElement;
import net.sf.gap.messages.impl.GISReply;
import net.sf.gap.messages.impl.GISRequest;
import net.sf.gap.distributions.Uniform_int;
import eduni.simjava.Sim_event;
import eduni.simjava.Sim_system;
import gridsim.*;

/**
 * This class is responsible for a sample test subclass of PlatformService
 * 
 * @author Giovanni Novelli
 * @see net.p2pgrid.agents.services.PlatformService
 */
public class GISService extends PlatformService {

    private GISRepository gisRepository;
    private Uniform_int randgis;
    private double entryCacheTime;
    private double gisCacheTime;

    /**
     * @param ap
     * @param name
     * @param trace_flag
     * @throws Exception
     */
    public GISService(AbstractAgentPlatform ap, boolean trace_flag, double gisCacheTime, double entryCacheTime)
            throws Exception {
        super(ap, "GISService", trace_flag);
        this.setGisCacheTime(gisCacheTime);
        this.setEntryCacheTime(entryCacheTime);
        randgis = new Uniform_int("rand_gis");
    }

    public GISService(AbstractAgentPlatform ap, String name, boolean trace_flag)
            throws Exception {
        super(ap, name, trace_flag);
        randgis = new Uniform_int("rand_gis");
    }

    @Override
    public void initialize() throws Exception {
        super.initialize();
        this.setGisRepository(new GISRepository(this.getAgentPlatform()));

        if (this.getAgentPlatform().getVirtualOrganization().getScenario() == null) {
            for (int i = 0; i < this.getAgentPlatform().getVirtualOrganization().getNumCEs(); i++) {
                AbstractGridElement ce = (AbstractGridElement) Sim_system.get_entity("CE_" + i);
                this.getGisRepository().addGE(ce);
            }
            for (int i = 0; i < this.getAgentPlatform().getVirtualOrganization().getNumSEs(); i++) {
                AbstractGridElement se = (AbstractGridElement) Sim_system.get_entity("SE_" + i);
                this.getGisRepository().addGE(se);
            }
        } else {
            for (int i = 0; i < this.getAgentPlatform().getVirtualOrganization().getNumCEs() + this.getAgentPlatform().getVirtualOrganization().getNumSEs(); i++) {
                String gename =
                        this.getAgentPlatform().getVirtualOrganization().getScenario().getGrid().getGridElements().get(i).getName();
                AbstractGridElement ge = (AbstractGridElement) Sim_system.get_entity(gename);
                this.getGisRepository().addGE(ge);
            }
        }
    }

    @Override
    protected void dispose() {
    }

    @Override
    public void processOtherEvent(Sim_event ev) {
        // TODO Auto-generated method stub
        switch (ev.get_tag()) {
            case Tags.GIS_REQUEST:
                this.processGIS();
                this.sendGIS_REPLY(ev);
                break;

            default:
                break;
        }
    }
    
    @Override
    protected void doSomething() {
        double currentTime = GISService.clock();
        if ((currentTime-this.getGisRepository().getLastRequestTime())>this.getGisCacheTime()) {
            this.processGIS();
        }
    }

    protected void sendGIS_REPLY(Sim_event ev) {
        int SIZE = 500;
        GISRequest request = GISRequest.get_data(ev);
        GISReply reply = new GISReply(ev.get_tag(), true, request, this.getGisRepository());
        super.send(super.output, GridSimTags.SCHEDULE_NOW, Tags.GIS_REPLY,
                new IO_data(reply, SIZE, request.getSrc_ID()));
    }

    public GISRepository getGisRepository() {
        return gisRepository;
    }

    public void setGisRepository(GISRepository gisRepository) {
        this.gisRepository = gisRepository;
    }

    public void processGIS() {
        double currentTime;
        int numCEs = this.getAgentPlatform().getVirtualOrganization().getNumCEs();
        AbstractGridElement ge;
        int geid;
        boolean SE;
        int numFreeAgents;
        double load;
        double inputLoad;
        double outputLoad;
        double ioLoad;
        GISEntry entry;
        int i;
        boolean update;
        int count;
        update = false;
        count = 0;
        while (!update && (count<numCEs*2)) {
            i = randgis.sample(numCEs);
            count++;
            ge = this.getAgentPlatform().getVirtualOrganization().getCEs().get(i);
            geid = ge.get_id();
            currentTime = GISService.clock();
            entry = this.gisRepository.get(geid);
            if (entry != null) {
                double lastUpdateTime = entry.getLastUpdateTime();
                if ((currentTime - lastUpdateTime) > this.getEntryCacheTime()) {
                    update = true;
                }
            } else {
                update = true;
            }
            if (update) {
                SE = ge.isSE();
                numFreeAgents = ge.getLocalDirectory().getFreeAgents();
                load = this.askLoad(geid);
                inputLoad = this.askInputLoad(geid);
                outputLoad = this.askOutputLoad(geid);
                ioLoad = inputLoad + outputLoad;
                entry = this.addEntry(geid, 0, 0, numFreeAgents, 0,
                        SE, 0, load, ioLoad);
            }
        }

        int numSEs = this.getAgentPlatform().getVirtualOrganization().getNumSEs();
        update = false;
        count = 0;
        while (!update && (count<numSEs*2)) {
            i = randgis.sample(numSEs);
            count++;
            ge = this.getAgentPlatform().getVirtualOrganization().getSEs().get(i);
            geid = ge.get_id();
            currentTime = GISService.clock();
            entry = this.gisRepository.get(geid);
            if (entry != null) {
                double lastUpdateTime = entry.getLastUpdateTime();
                if ((currentTime - lastUpdateTime) > this.getEntryCacheTime()) {
                    update = true;
                }
            } else {
                update = true;
            }
            if (update) {
                SE = ge.isSE();
                numFreeAgents = ge.getLocalDirectory().getFreeAgents();
                load = this.askLoad(geid);
                inputLoad = this.askInputLoad(geid);
                outputLoad = this.askOutputLoad(geid);
                ioLoad = inputLoad + outputLoad;
                entry = this.addEntry(geid, 0, 0, numFreeAgents, 0,
                        SE, 0, load, ioLoad);
            }
        }

        this.getGisRepository().setLastRequestTime(PlatformService.clock());
    }

    public double getEntryCacheTime() {
        return entryCacheTime;
    }

    public void setEntryCacheTime(double entryCacheTime) {
        this.entryCacheTime = entryCacheTime;
    }
    //double delay = rand.sample()*0.1;
    //super.sim_process(delay);
    private double askLoad(int geid) {
        // Get Resource Dynamic information
        send(super.output, 0.0, GridSimTags.RESOURCE_DYNAMICS,
                new IO_data(new Integer(super.get_id()), 4, this.get_id()));
        double load;
        try {
            // waiting for a response from system GIS
            Sim_type_p tag = new Sim_type_p(GridSimTags.RESOURCE_DYNAMICS);

            // only look for this type of ack
            Sim_event ev = new Sim_event();
            super.sim_get_next(tag, ev);
            Accumulator accLoad = (Accumulator) ev.get_data();
            load =
                    accLoad.getMean();
        } catch (Exception e) {
            load = 0.5;
        }

        return load;
    }

    private double askInputLoad(int geid) {
        // Get Resource Dynamic information
        send(super.output, 0.0, Tags.INPUT_DYNAMICS,
                new IO_data(new Integer(super.get_id()), 4, this.get_id()));
        double load;
        try {
            // waiting for a response from system GIS
            Sim_type_p tag = new Sim_type_p(Tags.INPUT_DYNAMICS);

            // only look for this type of ack
            Sim_event ev = new Sim_event();
            super.sim_wait_for(tag, this.getGisCacheTime()*0.5, ev);
            if (ev!=null) {
            Accumulator accLoad = (Accumulator) ev.get_data();
            load =
                    accLoad.getMean();
            } else {
              load = 1.0;
            }
        } catch (Exception e) {
            load = 0.5;
        }

        return load;
    }

    private double askOutputLoad(int geid) {
        // Get Resource Dynamic information
        send(super.output, 0.0, Tags.OUTPUT_DYNAMICS,
                new IO_data(new Integer(super.get_id()), 4, this.get_id()));
        double load;
        try {
            // waiting for a response from system GIS
            Sim_type_p tag = new Sim_type_p(Tags.OUTPUT_DYNAMICS);

            // only look for this type of ack
            Sim_event ev = new Sim_event();
            super.sim_wait_for(tag, this.getGisCacheTime()*0.5, ev);
            Accumulator accLoad = (Accumulator) ev.get_data();
            load =
                    accLoad.getMean();
        } catch (Exception e) {
            load = 0.5;
        }

        return load;
    }

    public GISEntry addEntry(
            int geid, int numPEs, int numFreePEs,
            int numFreeAgents, int totalMIPS, boolean SE, double MB_size,
            double load, double ioLoad) {
        double time = GISService.clock();
        GISEntry entry = new GISEntry(time, numPEs, numFreePEs, numFreeAgents,
                totalMIPS, SE, MB_size, load, ioLoad);
        if (this.getGisRepository() == null) {
            this.setGisRepository(new GISRepository(this.getAgentPlatform()));

            for (int i = 0; i <
                    this.getAgentPlatform().getVirtualOrganization().getNumCEs(); i++) {
                AbstractGridElement ce = this.getAgentPlatform().getVirtualOrganization().getCEs().get(i);
                this.getGisRepository().addGE(ce);
            }

            for (int i = 0; i <
                    this.getAgentPlatform().getVirtualOrganization().getNumSEs(); i++) {
                AbstractGridElement se = this.getAgentPlatform().getVirtualOrganization().getSEs().get(i);
                this.getGisRepository().addGE(se);
            }

        }
        this.getGisRepository().put(geid, entry);
        return entry;
    }

    public GISEntry removeEntry(
            int geid) {
        return this.getGisRepository().remove(geid);
    }

    public double getGisCacheTime() {
        return gisCacheTime;
    }

    public void setGisCacheTime(double gisCacheTime) {
        this.gisCacheTime = gisCacheTime;
    }
}
