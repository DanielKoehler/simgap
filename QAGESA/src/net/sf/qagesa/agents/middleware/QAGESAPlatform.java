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
 * QAGESAPlatform.java
 *
 * Created on 10 August 2006, 9.44 by Giovanni Novelli
 *
 * $Id: QAGESAPlatform.java 1142 2007-07-18 20:49:58Z gnovelli $
 *
 */

package net.sf.qagesa.agents.middleware;

import net.sf.gap.agents.middleware.AgentPlatform;
import net.sf.qagesa.QAGESA;
import net.sf.qagesa.agents.TranscodingAgent;
import net.sf.qagesa.agents.services.impl.MuMService;
import net.sf.qagesa.agents.services.impl.ReFService;
import net.sf.qagesa.stats.QAGESAStat;
import eduni.simjava.Sim_event;
import eduni.simjava.Sim_system;

/**
 * 
 * @author Giovanni Novelli
 */
public class QAGESAPlatform extends AgentPlatform {
	private MuMService serviceMuM; // MuM Service

	private ReFService serviceReF; // ReF Service

	/** Creates a new instance of QAGESAPlatform */
	public QAGESAPlatform(boolean trace) throws Exception {
		super("QAGESA", trace);
	}

    @Override
        public void createServices() throws Exception {
            super.createServices();
            
            this.setServiceMuM(new MuMService(this, false));
            this.setServiceReF(new ReFService(this, false, 0.0, 0.0));
        }
        
    @Override
        public void initialize() throws Exception {
            super.initialize();
            this.initPlatform();
            this.initAgents();
            QAGESAStat.reset(this.getVirtualOrganization().getNumCEs());
        }
        
        public void initAgents() throws Exception {
            int totalAgents = this.getTotalAgents();
            for (int i=0;i<totalAgents;i++) {
                TranscodingAgent agent = (TranscodingAgent) Sim_system.get_entity("AGENT_"+i);
                agent.initialize();
            }
        }
        
	@Override
	public void initPlatform() throws Exception {
		super.initPlatform();

                this.getVirtualOrganization().initialize();
                
		this.getServiceMuM().initialize();

		this.getServiceReF().initialize();
	}

	private void asyncProcessNetworkMap() {
		this.getNetworkMonitor().asyncProcessNetworkMap();
	}

	/**
	 * The core method that handles communications among GridSim entities.
	 */
	@Override
	public void body() {
		// wait for a little while for about 3 seconds.
		// This to give a time for GridResource entities to register their
		// services to GIS (GridInformationService) entity.
		super.gridSimHold(QAGESA.getPlatformStartTime());
                
                try {
                this.initialize();
                } catch (Exception e) {
                    e.printStackTrace();
                }

		this.asyncProcessNetworkMap();

                Sim_event ev = new Sim_event();
		while (QAGESA.isRunning()) {
			super.sim_wait_for(Sim_system.SIM_ANY, 10.0, ev);
                        
			this.processEvent(ev);
			while (super.sim_waiting() > 0) {
				this.processEvents();
			}
		}

		// this.getNetworkMonitor().showNetworkMap();

		// //////////////////////////////////////////////////////
		// shut down I/O ports
		this.shutdownUserEntity();
		this.terminateIOEntities();

		// don't forget to close the file
		if (this.getReport_() != null) {
			this.getReport_().finalWrite();
		}
	}

	@Override
	public void processOtherEvent(Sim_event ev) {
	}

	protected void processEvents() {
		Sim_event ev = new Sim_event();

		super.sim_get_next(ev);
		this.processEvent(ev);
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
}
