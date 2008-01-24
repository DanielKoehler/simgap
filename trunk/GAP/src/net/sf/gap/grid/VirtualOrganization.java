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
 * VirtualOrganization.java
 *
 * Created on 7 August 2006, 14.40 by Giovanni Novelli
 *
 * $Id$
 *
 */

package net.sf.gap.grid;

import eduni.simjava.Sim_system;

import gridsim.net.FIFOScheduler;
import gridsim.net.Link;
import gridsim.net.RIPRouter;

import java.util.HashMap;
import java.util.Vector;

import net.sf.gap.agents.middleware.AgentMiddleware;
import net.sf.gap.distributions.Uniform_int;
import net.sf.gap.grid.AbstractVirtualOrganization;
import net.sf.gap.grid.components.AbstractGridElement;

import net.sf.gap.factories.COREGEFactory;
import net.sf.gap.factories.LinkFactory;
import net.sf.gap.impl.grid.components.COREGridElement;

/**
 *
 * @author Giovanni Novelli
 */
public abstract class VirtualOrganization extends AbstractVirtualOrganization {
    /**
     * links delay factor
     */
    private double factor;
    
    private boolean fixedInfrastructure;
    
    private int networkType;
    
    private boolean traceFlag;
    
    private int routersPerCloud;
    
    private int clouds;
    
    private int basicMIPS;
    
    private int PEMax;
    
    private int MMin;
    
    private int MMax;
    
    private int GBMin;
    
    private int GBMax;
    
    private COREGEFactory seFactory;
    
    private int numUsers;
    
    private int experimentID;

    public VirtualOrganization(boolean traceFlag, int numCE, int MIPS,
            int PEMax, int MMin, int MMax, int numSE, int GBMin,
            int GBMax, int routersPerCloud, int clouds, boolean fixedInfrastructure, double factor, int numUsers, int experimentID) throws Exception {
        this.setFactor(factor);
        this.initParameters(traceFlag,
                numCE, MIPS, PEMax, MMin, MMax, numSE, GBMin, GBMax, 
                routersPerCloud, clouds, fixedInfrastructure, factor, numUsers, experimentID);
        
        this.createEntities();
    }
    
    public static final int NT_RINGSCHAIN = 1;
    public static final int NT_STATIC = 2;
    public VirtualOrganization(boolean traceFlag, int numCE, int MIPS,
            int PEMax, int MMin, int MMax, int numSE, int GBMin,
            int GBMax, int numUsers, double factor) throws Exception {
        this.setFactor(factor);
        this.initParameters(traceFlag,
                numCE, MIPS, PEMax, MMin, MMax, 
                numSE, GBMin, GBMax, numUsers);
        
        this.createEntities();
    }
    
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
    
    private void initializeCEs() {
        int N = this.getTopology().getNumRouters();
        for (int i = 0; i < this.getNumCEs(); i++) {
            COREGridElement computingElement = (COREGridElement) Sim_system.get_entity("CE_"+i);
            this.mapCEs.put(computingElement.get_id(), computingElement.getExternalRouter());
            this.getCEs().add(computingElement);
        }
    }
    
    private void initializeSEs() {
        for (int i = 0; i < this.getNumSEs(); i++) {
            COREGridElement storageElement = (COREGridElement) Sim_system.get_entity("SE_"+i);
            this.mapSEs.put(storageElement.get_id(), storageElement.getExternalRouter());
            this.getSEs().add(storageElement);
        }
    }
    
    abstract protected void initializeAgents();
    
    private void initParameters(boolean traceFlag, int numCE, int MIPS,
            int PEMax, int MMin, int MMax, int numSE, int GBMin,
            int GBMax, int routersPerCloud, int clouds, boolean fixedInfrastructure, 
            double factor, int numUsers, int experimentID) {
        this.setTraceFlag(traceFlag);
        this.setNumCEs(numCE);
        this.setNumSEs(numSE);
        this.setNumUsers(numUsers);
        this.setRoutersPerCloud(routersPerCloud);
        this.setClouds(clouds);
        this.setBasicMIPS(MIPS);
        this.setPEMax(PEMax);
        this.setMMin(MMin);
        this.setMMax(MMax);
        this.setGBMin(GBMin);
        this.setGBMax(GBMax);
        this.setFixedInfrastructure(fixedInfrastructure);
        this.setFactor(factor);
        this.setExperimentID(experimentID);
        this.setNetworkType(VirtualOrganization.NT_RINGSCHAIN);
    }
    
    private void initParameters(boolean traceFlag, int numCE, int MIPS,
            int PEMax, int MMin, int MMax, int numSE, int GBMin,
            int GBMax, int numUsers) {
        this.setTraceFlag(traceFlag);
        this.setNumCEs(numCE);
        this.setNumSEs(numSE);
        this.setNumUsers(numUsers*numSE);
        this.setBasicMIPS(MIPS);
        this.setPEMax(PEMax);
        this.setMMin(MMin);
        this.setMMax(MMax);
        this.setGBMin(GBMin);
        this.setGBMax(GBMax);
        this.setNetworkType(VirtualOrganization.NT_STATIC);
    }
    
    protected void createEntities() throws Exception {
        this.setDataGIS(this.createDataGIS());
        this.setTopRegionalRC(this.createTopRegionalRC());
        if (this.getNetworkType() == VirtualOrganization.NT_RINGSCHAIN) {
            this.setTopology(new RingsChain(this.getRoutersPerCloud(), this.getClouds(), this.getFactor(), this.isTraceFlag()));
        } else if (this.getNetworkType() == VirtualOrganization.NT_STATIC) {
            this.setTopology(new StaticTopology(this.isTraceFlag(),this.getFactor()));
        }
        FIFOScheduler rcSched = new FIFOScheduler("trrc_sched");
        RIPRouter router = (RIPRouter) Sim_system.get_entity("ROUTER_0");
        router.attachHost(this.getTopRegionalRC(), rcSched);
        COREGEFactory sefactory = new COREGEFactory(this.getTopRegionalRC(), this.getBasicMIPS(),
                getPEMax(), getMMin(), getMMax(), getGBMin(), getGBMax());
        this.setSeFactory(sefactory);
        this.createAndAttachCEs();
        this.createAndAttachSEs();
        this.createAndAttachAgentPlatform();
        this.createAndAttachAgents();
        this.createAndAttachUsers();
    }
    
    abstract public void createAndAttachAgentPlatform() throws Exception;
    
    abstract public void createAndAttachAgents() throws Exception;
    
    abstract public void createAndAttachUsers() throws Exception;
    
    public void createAndAttachCEs() throws Exception {
        if (this.getNetworkType()==VirtualOrganization.NT_STATIC) {
            int N = this.getTopology().getNumRouters();
            int index;
            for (int i = 0; i < this.getNumCEs(); i++) {
                index = i % N;
                RIPRouter router = (RIPRouter) Sim_system.get_entity("ROUTER_"+index);
                Link link = LinkFactory.GELink(false);
                COREGridElement computingElement = this.seFactory.create(this.isFixedInfrastructure(), i,link, false);
                computingElement.attachRouter(router);
            }
        } else {
            Uniform_int r = new Uniform_int("createAndAttachCEs");
            int N = this.getTopology().getNumRouters();
            int index;
            for (int i = 0; i < this.getNumCEs(); i++) {
                index = i % N;
                RIPRouter router = (RIPRouter) Sim_system.get_entity("ROUTER_"+index);
                Link link = LinkFactory.GELink(false);
                COREGridElement computingElement = this.seFactory.create(this.isFixedInfrastructure(), i,link, false);
                computingElement.attachRouter(router);
            }
        }
    }
    
    public void createAndAttachSEs() throws Exception {
        if (this.getNetworkType()==VirtualOrganization.NT_STATIC) {
        int N = this.getTopology().getNumRouters();
        int index;
        for (int i = 0; i < this.getNumSEs(); i++) {
            index = i % N;
            RIPRouter router = (RIPRouter) Sim_system.get_entity("ROUTER_"+index);
            Link link = LinkFactory.GELink(false);
            COREGridElement storageElement = this.seFactory.create(this.isFixedInfrastructure(), i, link, true);
            
            storageElement.attachRouter(router);
        }
        } else {
        Uniform_int r = new Uniform_int("createAndAttachSEs");
        int N = this.getTopology().getNumRouters();
        int index;
        for (int i = 0; i < this.getNumSEs(); i++) {
            index = i % N;
            RIPRouter router = (RIPRouter) Sim_system.get_entity("ROUTER_"+index);
            Link link = LinkFactory.GELink(false);
            COREGridElement storageElement = this.seFactory.create(this.isFixedInfrastructure(), i, link, true);
            
            storageElement.attachRouter(router);
        }
        }
    }
    
    public COREGEFactory getSeFactory() {
        return this.seFactory;
    }
    
    public void setSeFactory(COREGEFactory seFactory) {
        this.seFactory = seFactory;
    }
    
    public int getNumAMs() {
        return this.getNumCEs() + this.getNumSEs();
    }
    
    public int getNumUsers() {
        return numUsers;
    }
    
    public void setNumUsers(int numUsers) {
        this.numUsers = numUsers;
    }
    
    public boolean isTraceFlag() {
        return traceFlag;
    }
    
    public void setTraceFlag(boolean traceFlag) {
        this.traceFlag = traceFlag;
    }
    
    public int getRoutersPerCloud() {
        return routersPerCloud;
    }
    
    public void setRoutersPerCloud(int routersPerCloud) {
        this.routersPerCloud = routersPerCloud;
    }
    
    public int getClouds() {
        return clouds;
    }
    
    public void setClouds(int clouds) {
        this.clouds = clouds;
    }
    
    public int getBasicMIPS() {
        return basicMIPS;
    }
    
    public void setBasicMIPS(int basicMIPS) {
        this.basicMIPS = basicMIPS;
    }
    
    public int getPEMax() {
        return PEMax;
    }
    
    public void setPEMax(int PEMax) {
        this.PEMax = PEMax;
    }
    
    public int getGBMin() {
        return GBMin;
    }
    
    public void setGBMin(int GBMin) {
        this.GBMin = GBMin;
    }
    
    public int getGBMax() {
        return GBMax;
    }
    
    public void setGBMax(int GBMax) {
        this.GBMax = GBMax;
    }
    
    public int getMMin() {
        return MMin;
    }
    
    public void setMMin(int MMin) {
        this.MMin = MMin;
    }
    
    public int getMMax() {
        return MMax;
    }
    
    public void setMMax(int MMax) {
        this.MMax = MMax;
    }

    public int getNetworkType() {
        return networkType;
    }

    public void setNetworkType(int networkType) {
        this.networkType = networkType;
    }

    public boolean isFixedInfrastructure() {
        return fixedInfrastructure;
    }

    public void setFixedInfrastructure(boolean fixedInfrastructure) {
        this.fixedInfrastructure = fixedInfrastructure;
    }

    public double getFactor() {
        return factor;
    }

    public void setFactor(double factor) {
        this.factor = factor;
    }

    public int getExperimentID() {
        return experimentID;
    }

    public void setExperimentID(int experimentID) {
        this.experimentID = experimentID;
    }
}
