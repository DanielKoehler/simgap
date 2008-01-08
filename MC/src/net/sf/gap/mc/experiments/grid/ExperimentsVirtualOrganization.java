/*
 ****************************************************************************************
 * Copyright © Giovanni Novelli                                             
 * All Rights Reserved.                                                                 
 ****************************************************************************************
 *
 * License:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * ExperimentsVirtualOrganization.java
 *
 * Created on 8 January 2008, 12.00 by Giovanni Novelli
 *
 * $Id$
 *
 */

package net.sf.gap.mc.experiments.grid;

import eduni.simjava.Sim_system;

import gridsim.net.Link;
import gridsim.net.RIPRouter;

import net.sf.gap.distributions.Uniform_int;

import net.sf.gap.mc.core.factories.LinkFactory;
import net.sf.gap.mc.core.grid.COREVirtualOrganization;
import net.sf.gap.mc.core.grid.components.COREGridElement;

import net.sf.gap.mc.experiments.agents.TESTAgent;
import net.sf.gap.mc.experiments.agents.middleware.ExperimentsPlatform;
import net.sf.gap.mc.experiments.users.User;

/**
 *
 * @author Giovanni Novelli
 */
public class ExperimentsVirtualOrganization extends COREVirtualOrganization {
    public ExperimentsVirtualOrganization(boolean traceFlag, int numCE, int MIPS,
            int PEMax, int MMin, int MMax, int numSE, int GBMin,
            int GBMax, int routersPerCloud, int clouds, 
            boolean fixedInfrastructure, double factor, int numUsers, int whichMeasure) 
            throws Exception {
        super(
                traceFlag, numCE, MIPS,
                PEMax, MMin, MMax, numSE, GBMin,
                GBMax, routersPerCloud, clouds,
                fixedInfrastructure, factor, numUsers, whichMeasure
                );
    }
    
    protected void initializeAgents() {
        int totalAgents = 0;
        for (int i = 0; i < this.getNumCEs(); i++) {
            COREGridElement computingElement = (COREGridElement) Sim_system.get_entity("CE_"+i);
            int numAgents = computingElement.getNumPE();
            for (int j = 0; j < numAgents; j++) {
                TESTAgent agent = (TESTAgent) Sim_system.get_entity("AGENT_"+totalAgents);
                this.getPlatform().addAgent(agent, computingElement);
                totalAgents++;
            }
        }
    }
    
    public void createAndAttachAgentPlatform() throws Exception {
        Uniform_int r = new Uniform_int("createAndAttachAgentPlatform");
        int index = 0;
        COREGridElement ce = (COREGridElement) Sim_system.get_entity("CE_"+index);
        this.setPlatform(new ExperimentsPlatform(false));
        ExperimentsPlatform agent = (ExperimentsPlatform) this.getPlatform();
        
        agent.setGridElement(ce);
        agent.setVirtualOrganization(this);
        
        ce.attachPlatform(agent);
        
        for (int i = 0; i < this.getNumCEs(); i++) {
            ce = (COREGridElement) Sim_system.get_entity("CE_"+i);
            ce.setAgentPlatform(agent);
        }
        for (int i = 0; i < this.getNumSEs(); i++) {
            ce = (COREGridElement) Sim_system.get_entity("SE_"+i);
            ce.setAgentPlatform(agent);
        }
        
        agent.createServices();
    }
    
    public void createAndAttachAgents() throws Exception {
        int totalAgents = 0;
        for (int i = 0; i < this.getNumCEs(); i++) {
            COREGridElement se = (COREGridElement) Sim_system.get_entity("CE_"+i);
            int numAgents = se.getNumPE();
            for (int j = 0; j < numAgents; j++) {
                TESTAgent agent = new TESTAgent(se, "AGENT_"
                        + totalAgents, 0, false);
                totalAgents++;
                se.attachAgent(agent);
            }
        }
        this.getPlatform().setTotalAgents(totalAgents);
    }
    
    public void createAndAttachUsers() throws Exception {
        Uniform_int r = new Uniform_int("createAndAttachUsers");
        int N = this.getTopology().getNumRouters();
        int index;
        RIPRouter router = null;
        Link link = null;
        for (int i = 0; i < this.getNumUsers(); i++) {
                index = i % N;
                router = (RIPRouter) Sim_system.get_entity("ROUTER_"+index);
                link = LinkFactory.UserLink(640000, 20);
                User rmrUser = new User("USER_" + i, link,false);
                router.attachHost(rmrUser, rmrUser.getUserSched());
                rmrUser.setVirtualOrganization(this);
        }
    }
}
