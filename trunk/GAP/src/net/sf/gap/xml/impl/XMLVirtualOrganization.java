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

import gridsim.net.RIPRouter;

import java.util.HashMap;
import java.util.Vector;

import net.sf.gap.agents.middleware.AgentMiddleware;
import net.sf.gap.grid.AbstractVirtualOrganization;
import net.sf.gap.grid.components.AbstractGridElement;
import net.sf.gap.xml.types.ScenarioType;

/**
 *
 * @author Giovanni Novelli
 */
public abstract class XMLVirtualOrganization extends AbstractVirtualOrganization {
    private ScenarioType scenario;
    
    public XMLVirtualOrganization(ScenarioType scenario) {
        this.setScenario(scenario);
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
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void createAndAttachAgentPlatform() throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void createAndAttachAgents() throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void createAndAttachCEs() throws Exception {
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
