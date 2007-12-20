/*
 ****************************************************************************************
 * Copyright © Giovanni Novelli                                             
 * All Rights Reserved.                                                                 
 ****************************************************************************************
 *
 * License:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * Platform.java
 *
 * Created on 10 August 2006, 9.44 by Giovanni Novelli
 *
 * $Id: Platform.java 1142 2007-07-18 20:49:58Z gnovelli $
 *
 */



package net.sf.gap.mc.qagesa.agents.middleware;

import eduni.simjava.Sim_event;
import eduni.simjava.Sim_system;
import net.sf.gap.mc.agents.middleware.Platform;
import net.sf.gap.mc.qagesa.agents.TranscodingAgent;
import net.sf.gap.mc.qagesa.agents.services.impl.MuMService;
import net.sf.gap.mc.qagesa.agents.services.impl.ReFService;

/**
 *
 * @author Giovanni Novelli
 */
public class QAGESAPlatform extends Platform {
    private MuMService serviceMuM;
    private ReFService serviceReF;
    /** Creates a new instance of QAGESA Platform */
    public QAGESAPlatform(boolean trace) throws Exception {
            super("QAGESA", trace);
    }

    @Override
    public void createServices() throws Exception {
        super.createServices();
        this.setServiceMuM(new MuMService(this, false));
        this.setServiceReF(new ReFService(this, false, 0.0, 0.0));
    }

    public void initializeServices() throws Exception {
        this.getServiceMuM().initialize();
        this.getServiceReF().initialize();
    }
    
    private void asyncProcessNetworkMap() {
        this.getNetworkMonitor().asyncProcessNetworkMap();
    }
    public void preprocess() {
        this.asyncProcessNetworkMap();
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
}
