/*
 ****************************************************************************************
 * Copyright © Giovanni Novelli                                             
 * All Rights Reserved.                                                                 
 ****************************************************************************************
 *
 * License:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * ExperimentsPlatform.java
 *
 * Created on 8 January 2008, 12.15 by Giovanni Novelli
 *
 * $Id$
 *
 */



package net.sf.gap.mc.experiments.agents.middleware;

import net.sf.gap.agents.middleware.AgentPlatform;
import net.sf.gap.mc.experiments.agents.TESTAgent;
import eduni.simjava.Sim_event;
import eduni.simjava.Sim_system;

/**
 *
 * @author Giovanni Novelli
 */
public class ExperimentsPlatform extends AgentPlatform {
    /** Creates a new instance of AgentPlatform */
    public ExperimentsPlatform(boolean trace) throws Exception {
            super("EXPERIMENTS", trace);
    }

    @Override
    public void createServices() throws Exception {
        super.createServices();
    }

    public void initializeServices() throws Exception {
    }
    
    public void preprocess() {
    }
    public void postprocess() {
    }

    @Override
    public void body() {
        this.init();
        this.preprocess();
        this.process();
        this.postprocess();
        this.end();
    }

    @Override
    public void processOtherEvent(Sim_event ev) {
    }

    public void initAgents() throws Exception {
        int totalAgents = this.getTotalAgents();
        for (int i = 0; i < totalAgents; i++) {
            TESTAgent agent = (TESTAgent) Sim_system.get_entity("AGENT_" + i);
            agent.initialize();
        }
    }
}
