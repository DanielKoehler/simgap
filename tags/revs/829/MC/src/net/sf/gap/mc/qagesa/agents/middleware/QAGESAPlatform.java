/*
 ****************************************************************************************
 * Copyright © Giovanni Novelli                                             
 * All Rights Reserved.                                                                 
 ****************************************************************************************
 *
 * License:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * AgentPlatform.java
 *
 * Created on 10 August 2006, 9.44 by Giovanni Novelli
 *
 * $Id$
 *
 */



package net.sf.gap.mc.qagesa.agents.middleware;

import net.sf.gap.agents.middleware.AgentPlatform;
import net.sf.gap.mc.qagesa.agents.TranscodingAgent;
import net.sf.gap.mc.qagesa.agents.services.impl.CELService;
import net.sf.gap.mc.qagesa.agents.services.impl.MuMService;
import net.sf.gap.mc.qagesa.agents.services.impl.ReFService;
import net.sf.gap.mc.qagesa.stats.QAGESAStat;
import eduni.simjava.Sim_event;
import eduni.simjava.Sim_system;
import java.util.Iterator;
import net.sf.gap.grid.components.AbstractGridElement;

/**
 *
 * @author Giovanni Novelli
 */
public class QAGESAPlatform extends AgentPlatform {
    private CELService serviceCEL;
    private MuMService serviceMuM;
    private ReFService serviceReF;
    /** Creates a new instance of QAGESA AgentPlatform */
    public QAGESAPlatform(boolean trace) throws Exception {
            super("QAGESA", trace);
    }

    @Override
    public void createServices() throws Exception {
        super.createServices();
        this.setServiceMuM(new MuMService(this, false));
        this.setServiceCEL(new CELService(this, false));
        this.setServiceReF(new ReFService(this, false, 0.0, 0.0));
    }

    public void initializeServices() throws Exception {
        this.getServiceMuM().initialize();
        this.getServiceCEL().initialize();
        this.getServiceReF().initialize();
    }
    
    private void asyncProcessNetworkMap() {
        this.getNetworkMonitor().asyncProcessNetworkMap();
    }
    
    private void asyncProcessNetworkMap(AbstractGridElement ge1) {
        this.getNetworkMonitor().asyncProcessNetworkMap(ge1);
    }
    
    public void preprocess() {
        QAGESAStat.reset(this.getVirtualOrganization().getNumCEs());
        Iterator<AbstractGridElement> it1 = this.getGisService().getGisRepository().getListGEs().iterator();
        while (it1.hasNext()) {
                AbstractGridElement ge1 = it1.next();
                super.gridSimHold(2.0);
                this.asyncProcessNetworkMap(ge1);
                super.gridSimHold(2.0);
        }
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

    public MuMService getServiceMuM() {
        return serviceMuM;
    }

    public void setServiceMuM(MuMService serviceMuM) {
        this.serviceMuM = serviceMuM;
    }

    public ReFService getServiceReF() {
        return serviceReF;
    }

    public void setServiceReF(ReFService serviceReF) {
        this.serviceReF = serviceReF;
    }

    public void initAgents() throws Exception {
        int totalAgents = this.getTotalAgents();
        for (int i = 0; i < totalAgents; i++) {
            TranscodingAgent agent = (TranscodingAgent) Sim_system.get_entity("AGENT_" + i);
            agent.initialize();
        }
    }

    public CELService getServiceCEL() {
        return serviceCEL;
    }

    public void setServiceCEL(CELService serviceCEL) {
        this.serviceCEL = serviceCEL;
    }
}
