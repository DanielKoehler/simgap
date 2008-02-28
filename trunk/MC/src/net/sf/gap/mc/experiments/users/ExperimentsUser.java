/*
 ****************************************************************************************
 * Copyright © Giovanni Novelli                                             
 * All Rights Reserved.                                                                 
 ****************************************************************************************
 *
 * License:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * ExperimentsUser.java
 *
 * Created on 8 January 2008, 12:45 by Giovanni Novelli
 *
 * $Id$
 *
 */
package net.sf.gap.mc.experiments.users;

import eduni.simjava.Sim_event;
import eduni.simjava.Sim_system;
import gridsim.Gridlet;
import gridsim.GridSim;
import gridsim.GridSimTags;
import gridsim.IO_data;
import gridsim.net.Link;
import net.sf.gap.agents.predicates.Predicate;
import net.sf.gap.distributions.Uniform_int;
import net.sf.gap.grid.components.GridElement;
import net.sf.gap.mc.experiments.constants.ExperimentsEntityTypes;
import net.sf.gap.mc.experiments.constants.ExperimentsTags;
import net.sf.gap.mc.experiments.users.impl.Measure;
import net.sf.gap.messages.impl.AgentReply;
import net.sf.gap.messages.impl.GridletReply;
import net.sf.gap.users.User;
import net.sf.gap.util.EntitiesCounter;

/**
 * @author Giovanni Novelli
 */
public class ExperimentsUser extends User {

    private int experimentID;
    private Measure measure;

    /**
     * 
     * Creates a new instance of ExperimentsUser
     * 
     * @param name
     *            AgentMiddleware entity name
     * @param trace_flag
     *            GridSim trace flag
     * @throws Exception
     *             This happens when name is null or haven't initialized
     *             GridSim.
     */
    public ExperimentsUser(int experimentID, String name, Link link, boolean trace_flag)
            throws Exception {
        super(name, link, ExperimentsEntityTypes.USER_USER, trace_flag);
        this.setExperimentID(experimentID);
        this.setMeasure(new Measure(experimentID));
        this.set_stat(this.getMeasure().getStat());
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
        GridElement agentsEnabledGridElement = null;
        int aegeResourceID;
        int i = 0;
        i = r.sample(this.getVirtualOrganization().getCEs().size());
        agentsEnabledGridElement = (GridElement) this.getVirtualOrganization().getCEs().get(i);
        aegeResourceID = agentsEnabledGridElement.get_id();
        agentReply = this.submitAgent(ExperimentsEntityTypes.AGENT_AGENT,
                aegeResourceID, 10000);
        /*
        int agentid = agentReply.getRequest().getDst_agentID();
        super.send(super.output, GridSimTags.SCHEDULE_NOW,
                ExperimentsTags.TEST_REQ, new IO_data(ExperimentsTags.TEST_REQ, 10, agentid));
        double evsend_time = GridSim.clock();
        String msg = String.format(
                "%1$f TEST_REQ %2$d --> %3$d", evsend_time,
               this.get_id(), agentid);
        this.write(msg);

        Sim_event ev = new Sim_event();
        Predicate predicate = new Predicate(ExperimentsTags.TEST_REP);
        super.sim_get_next(predicate, ev); // only look for this type of ack
        double evrecv_time = GridSim.clock();
        msg = String.format(
                "%1$f %2$d <-- TEST_REP %3$d",
                evrecv_time, 
                ev.get_dest(),
                ev.get_src());
        this.write(msg);
        */
        
        if (agentReply.isOk()) {
            for (int j = 0; j < 1; j++) {
                @SuppressWarnings("unused")
				GridletReply gridletReply = null;
                gridletReply = this.newGridlet(agentReply);
            }
            /*
            if (gridletReply.isOk()) {
            do {
            agentReply = this.hasGridletsAgent(agentReply
            .getRequest(), false);
            } while (agentReply.isOk());
            }*/
            agentReply = this.killWaitAgent(agentReply.getRequest());
        /*
        agentReply = this.killAgent(agentReply.getRequest());
         */
        }
    }

    private GridletReply newGridlet(AgentReply agentReply) {
        if (!EntitiesCounter.contains("Gridlet")) {
            EntitiesCounter.create("Gridlet");
        }
        double length = 1000.0;
        long file_size = 3000;
        long output_size = 1500;
        Gridlet g = new Gridlet(EntitiesCounter.inc("Gridlet"), length,
                file_size, output_size);
        GridletReply gridletReply = null;
        //gridletReply = this.submitGridletToAgent(agentReply.getRequest().getDst_agentID(), agentReply.getRequest().getDst_resID(), g);
        Uniform_int r = new Uniform_int("nextaege");
        GridElement agentsEnabledGridElement = null;
        int aegeResourceID;
        int i = 0;
        i = r.sample(this.getVirtualOrganization().getCEs().size());
        agentsEnabledGridElement = (GridElement) this.getVirtualOrganization().getCEs().get(i);
        aegeResourceID = agentsEnabledGridElement.get_id();
        gridletReply = this.submitGridletToAgent(agentReply.getRequest().getDst_agentID(), aegeResourceID, g);
        return gridletReply;
    }

    public Measure getMeasure() {
        return measure;
    }

    public void setMeasure(Measure measure) {
        this.measure = measure;
    }

    public int getExperimentID() {
        return experimentID;
    }

    public void setExperimentID(int experimentID) {
        this.experimentID = experimentID;
    }
}
