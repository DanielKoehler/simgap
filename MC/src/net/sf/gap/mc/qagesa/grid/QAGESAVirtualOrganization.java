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
 * QAGESAVirtualOrganization.java
 *
 * Created on 7 August 2006, 14.40 by Giovanni Novelli
 *
 * $Id$
 *
 */

package net.sf.gap.mc.qagesa.grid;

import eduni.simjava.Sim_system;
import gridsim.net.FIFOScheduler;
import gridsim.net.Link;
import gridsim.net.RIPRouter;

import java.util.HashMap;
import java.util.Vector;

import net.sf.gap.agents.middleware.AgentMiddleware;
import net.sf.gap.distributions.Uniform_int;
import net.sf.gap.factories.LinkFactory;
import net.sf.gap.grid.AbstractVirtualOrganization;
import net.sf.gap.grid.RingsChain;
import net.sf.gap.grid.StaticTopology;
import net.sf.gap.grid.components.AbstractGridElement;
import net.sf.gap.grid.components.GridElement;
import net.sf.gap.mc.qagesa.agents.TranscodingAgent;
import net.sf.gap.mc.qagesa.agents.middleware.QAGESAPlatform;
import net.sf.gap.mc.qagesa.factories.QAGESAGEFactory;
import net.sf.gap.mc.qagesa.multimedia.TranscodingSet;
import net.sf.gap.mc.qagesa.users.impl.Submitter;
import net.sf.gap.mc.qagesa.users.impl.User;

/**
 *
 * @author Giovanni Novelli
 */
public class QAGESAVirtualOrganization extends AbstractVirtualOrganization {
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
    
    private QAGESAGEFactory seFactory;
    
    private int numUsers;
    
    private boolean cachingEnabled;
    
    public static final int RMR = 0;
    public static final int MR = 1;
    public static final int RMS = 2;
    public static final int MS = 3;
    public static final int RMF = 4;
    public static final int MF = 5;
    
    private int whichMeasure;
    
    private TranscodingSet transcodingSet;
    
    private int maxRequests;
    
    public QAGESAVirtualOrganization(boolean traceFlag, int numCE, int MIPS,
            int PEMax, int MMin, int MMax, int numSE, int GBMin,
            int GBMax, int routersPerCloud, int clouds, boolean fixedInfrastructure, double factor, int numUsers, boolean cachingEnabled, int whichMeasure, int maxRequests) throws Exception {
        this.setFactor(factor);
        this.initParameters(traceFlag,
                numCE, MIPS, PEMax, MMin, MMax, numSE, GBMin, GBMax, 
                routersPerCloud, clouds, fixedInfrastructure, factor, numUsers, cachingEnabled, whichMeasure, maxRequests);
        
        this.createEntities();
    }
    
    public static final int NT_RINGSCHAIN = 1;
    public static final int NT_STATIC = 2;
    public QAGESAVirtualOrganization(boolean traceFlag, int numCE, int MIPS,
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
    
    protected void initializeCEs() {
        @SuppressWarnings("unused")
		int N = this.getTopology().getNumRouters();
        for (int i = 0; i < this.getNumCEs(); i++) {
            GridElement computingElement = (GridElement) Sim_system.get_entity("CE_"+i);
            this.mapCEs.put(computingElement.get_id(), computingElement.getExternalRouter());
            this.getCEs().add(computingElement);
        }
    }
    
    protected void initializeSEs() {
        for (int i = 0; i < this.getNumSEs(); i++) {
            GridElement storageElement = (GridElement) Sim_system.get_entity("SE_"+i);
            this.mapSEs.put(storageElement.get_id(), storageElement.getExternalRouter());
            this.getSEs().add(storageElement);
        }
    }
    
    protected void initializeAgents() {
        int totalAgents = 0;
        for (int i = 0; i < this.getNumCEs(); i++) {
            GridElement computingElement = (GridElement) Sim_system.get_entity("CE_"+i);
            int numAgents = computingElement.getNumPE();
            for (int j = 0; j < numAgents; j++) {
                TranscodingAgent agent = (TranscodingAgent) Sim_system.get_entity("AGENT_"+totalAgents);
                this.getPlatform().addAgent(agent, computingElement);
                totalAgents++;
            }
        }
    }
    
    private void initParameters(boolean traceFlag, int numCE, int MIPS,
            int PEMax, int MMin, int MMax, int numSE, int GBMin,
            int GBMax, int routersPerCloud, int clouds, boolean fixedInfrastructure, 
            double factor, int numUsers, boolean cachingEnabled, int whichMeasure, int maxRequests) {
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
        this.setCachingEnabled(cachingEnabled);
        this.setWhichMeasure(whichMeasure);
        this.setNetworkType(QAGESAVirtualOrganization.NT_RINGSCHAIN);
        this.setMaxRequests(maxRequests);
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
        this.setNetworkType(QAGESAVirtualOrganization.NT_STATIC);
    }
    
    protected void createEntities() throws Exception {
        this.setDataGIS(this.createDataGIS());
        this.setTopRegionalRC(this.createTopRegionalRC());
        if (this.getNetworkType() == QAGESAVirtualOrganization.NT_RINGSCHAIN) {
            this.setTopology(new RingsChain(this.getRoutersPerCloud(), this.getClouds(), this.getFactor(), this.isTraceFlag()));
        } else if (this.getNetworkType() == QAGESAVirtualOrganization.NT_STATIC) {
            this.setTopology(new StaticTopology(this.isTraceFlag(),this.getFactor()));
        }
        FIFOScheduler rcSched = new FIFOScheduler("trrc_sched");
        RIPRouter router = (RIPRouter) Sim_system.get_entity("ROUTER_0");
        router.attachHost(this.getTopRegionalRC(), rcSched);
        QAGESAGEFactory sefactory = new QAGESAGEFactory(this.getTopRegionalRC(), this.getBasicMIPS(),
                getPEMax(), getMMin(), getMMax(), getGBMin(), getGBMax());
        this.setSeFactory(sefactory);
        this.createAndAttachCEs();
        this.createAndAttachSEs();
        this.createAndAttachAgentPlatform();
        this.createAndAttachAgents();
        this.createAndAttachUsers();
    }
    
    public void createAndAttachAgentPlatform() throws Exception {
        @SuppressWarnings("unused")
		Uniform_int r = new Uniform_int("createAndAttachAgentPlatform");
        int index = 0;
        GridElement ce = (GridElement) Sim_system.get_entity("CE_"+index);
        this.setPlatform(new QAGESAPlatform(false));
        QAGESAPlatform agent = (QAGESAPlatform) this.getPlatform();
        
        agent.setGridElement(ce);
        agent.setVirtualOrganization(this);
        
        ce.attachPlatform(agent);
        
        for (int i = 0; i < this.getNumCEs(); i++) {
            ce = (GridElement) Sim_system.get_entity("CE_"+i);
            ce.setAgentPlatform(agent);
        }
        for (int i = 0; i < this.getNumSEs(); i++) {
            ce = (GridElement) Sim_system.get_entity("SE_"+i);
            ce.setAgentPlatform(agent);
        }
        
        agent.createServices();
    }
    
    public void createAndAttachAgents() throws Exception {
        int totalAgents = 0;
        for (int i = 0; i < this.getNumCEs(); i++) {
            GridElement se = (GridElement) Sim_system.get_entity("CE_"+i);
            int numAgents = se.getNumPE();
            for (int j = 0; j < numAgents; j++) {
                TranscodingAgent agent = new TranscodingAgent(se, "AGENT_"
                        + totalAgents, 0, false, this.isCachingEnabled());
                totalAgents++;
                se.attachAgent(agent);
            }
        }
        this.getPlatform().setTotalAgents(totalAgents);
    }
    
    public void createAndAttachSubmitters() throws Exception {
        @SuppressWarnings("unused")
		Uniform_int r = new Uniform_int("createAndAttachSubmitters");
        int N = this.getTopology().getNumRouters();
        int index;
        RIPRouter router = null;
        Link link = null;
        for (int i = 0; i < this.getNumUsers(); i++) {
                index = i % N;
                router = (RIPRouter) Sim_system.get_entity("ROUTER_"+index);
                link = LinkFactory.UserLink(640000, 20);
                Submitter submitter = new Submitter("SUBMITTER_" + i, link,false);
                router.attachHost(submitter, submitter.getUserSched());
                submitter.setVirtualOrganization(this);
        }
    }

    public void createAndAttachUsers() throws Exception {
        double baudrate = 20971520;
        double delay = 10.0;
        this.setTranscodingSet(new TranscodingSet("measures/videos.csv",
                "measures/chunks.csv"));
        @SuppressWarnings("unused")
		Uniform_int r = new Uniform_int("createAndAttachUsers");
        int N = this.getTopology().getNumRouters();
        int index;
        RIPRouter router = null;
        Link link = null;
        String movieTag = "aa06f7ddedc7460bd439298494c1e968";
        int numRequests = this.getMaxRequests();
        boolean repeated = true;
        for (int i = 0; i < this.getNumUsers(); i++) {
                index = i % N;
                switch (this.getWhichMeasure()) {
                    case RMR:
                        router = (RIPRouter) Sim_system.get_entity("ROUTER_"+index);
                        link = LinkFactory.UserLink(baudrate, delay);
                        User rmrUser = new User("RMRUSER_" + i, link,false,
                                true,numRequests,repeated, movieTag,User.MEASURE_RESPONSE);
                        router.attachHost(rmrUser, rmrUser.getUserSched());
                        rmrUser.setVirtualOrganization(this);
                        rmrUser.setUid(i);
                        break;
                    case  MR:
                        router = (RIPRouter) Sim_system.get_entity("ROUTER_"+index);
                        link = LinkFactory.UserLink(baudrate, delay);
                        User mruser = new User("MRUSER_" + i, link,false,
                                false,numRequests,repeated, movieTag,User.MEASURE_RESPONSE);
                        router.attachHost(mruser, mruser.getUserSched());
                        mruser.setVirtualOrganization(this);
                        mruser.setUid(i);
                        break;
                    case RMS:
                        router = (RIPRouter) Sim_system.get_entity("ROUTER_"+index);
                        link = LinkFactory.UserLink(baudrate, delay);
                        User rmsUser = new User("RMSUSER_" + i, link,false,
                                true,numRequests,repeated, movieTag,User.MEASURE_STREAMING);
                        router.attachHost(rmsUser, rmsUser.getUserSched());
                        rmsUser.setVirtualOrganization(this);
                        rmsUser.setUid(i);
                        break;
                    case  MS:
                        router = (RIPRouter) Sim_system.get_entity("ROUTER_"+index);
                        link = LinkFactory.UserLink(baudrate, delay);
                        User msuser = new User("MSUSER_" + i, link,false,
                                false,numRequests,repeated, movieTag,User.MEASURE_STREAMING);
                        router.attachHost(msuser, msuser.getUserSched());
                        msuser.setVirtualOrganization(this);
                        msuser.setUid(i);
                        break;
                    case RMF:
                        router = (RIPRouter) Sim_system.get_entity("ROUTER_"+index);
                        link = LinkFactory.UserLink(baudrate, delay);
                        User rmfUser = new User("RMFUSER_" + i, link,false,
                                true,numRequests,repeated, movieTag,User.MEASURE_FIRST);
                        router.attachHost(rmfUser, rmfUser.getUserSched());
                        rmfUser.setVirtualOrganization(this);
                        rmfUser.setUid(i);
                        break;
                    case MF:
                        router = (RIPRouter) Sim_system.get_entity("ROUTER_"+index);
                        link = LinkFactory.UserLink(baudrate, delay);
                        User mfUser = new User("MFUSER_" + i, link,false,
                                false,numRequests,repeated, movieTag,User.MEASURE_FIRST);
                        router.attachHost(mfUser, mfUser.getUserSched());
                        mfUser.setVirtualOrganization(this);
                        mfUser.setUid(i);
                        break;
                    default:
                        break;
                }
        }
    }
    
    public void createAndAttachCEs() throws Exception {
        this.setMIPS(0);
        if (this.getNetworkType()==QAGESAVirtualOrganization.NT_STATIC) {
            int N = this.getTopology().getNumRouters();
            int index;
            for (int i = 0; i < this.getNumCEs(); i++) {
                index = i % N;
                RIPRouter router = (RIPRouter) Sim_system.get_entity("ROUTER_"+index);
                Link link = LinkFactory.GELink(false);
                GridElement computingElement = this.seFactory.create(this.isFixedInfrastructure(), i,link, false);
                computingElement.attachRouter(router);
                this.setMIPS(this.getMIPS()+computingElement.getTotalMIPS());
            }
        } else {
            @SuppressWarnings("unused")
			Uniform_int r = new Uniform_int("createAndAttachCEs");
            int N = this.getTopology().getNumRouters();
            int index;
            for (int i = 0; i < this.getNumCEs(); i++) {
                index = i % N;
                RIPRouter router = (RIPRouter) Sim_system.get_entity("ROUTER_"+index);
                Link link = LinkFactory.GELink(false);
                GridElement computingElement = this.seFactory.create(this.isFixedInfrastructure(), i,link, false);
                computingElement.attachRouter(router);
                this.setMIPS(this.getMIPS()+computingElement.getTotalMIPS());
            }
        }
    }
    
    public void createAndAttachSEs() throws Exception {
        if (this.getNetworkType()==QAGESAVirtualOrganization.NT_STATIC) {
        int N = this.getTopology().getNumRouters();
        int index;
        for (int i = 0; i < this.getNumSEs(); i++) {
            index = i % N;
            RIPRouter router = (RIPRouter) Sim_system.get_entity("ROUTER_"+index);
            Link link = LinkFactory.GELink(false);
            GridElement storageElement = this.seFactory.create(this.isFixedInfrastructure(), i, link, true);
            
            storageElement.attachRouter(router);
        }
        } else {
        @SuppressWarnings("unused")
		Uniform_int r = new Uniform_int("createAndAttachSEs");
        int N = this.getTopology().getNumRouters();
        int index;
        for (int i = 0; i < this.getNumSEs(); i++) {
            index = i % N;
            RIPRouter router = (RIPRouter) Sim_system.get_entity("ROUTER_"+index);
            Link link = LinkFactory.GELink(false);
            GridElement storageElement = this.seFactory.create(this.isFixedInfrastructure(), i, link, true);
            
            storageElement.attachRouter(router);
        }
        }
    }
    
    public QAGESAGEFactory getSeFactory() {
        return this.seFactory;
    }
    
    public void setSeFactory(QAGESAGEFactory seFactory) {
        this.seFactory = seFactory;
    }
    
    public int getNumAMs() {
        return this.getNumCEs() + this.getNumSEs();
    }
    
    public TranscodingSet getTranscodingSet() {
        return transcodingSet;
    }
    
    public void setTranscodingSet(TranscodingSet transcodingSet) {
        this.transcodingSet = transcodingSet;
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

    public boolean isCachingEnabled() {
        return cachingEnabled;
    }

    public void setCachingEnabled(boolean cachingEnabled) {
        this.cachingEnabled = cachingEnabled;
    }

    public int getWhichMeasure() {
        return whichMeasure;
    }

    public void setWhichMeasure(int whichMeasure) {
        this.whichMeasure = whichMeasure;
    }

    public int getMaxRequests() {
        return maxRequests;
    }

    public void setMaxRequests(int maxRequests) {
        this.maxRequests = maxRequests;
    }
}
