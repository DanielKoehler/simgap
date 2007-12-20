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
 * Created on 20 December 2007, 20:00 by Giovanni Novelli
 *
 * $Id: Platform.java 1142 2007-07-18 20:49:58Z gnovelli $
 *
 */

package net.sf.gap.mc.agents.middleware;

import net.sf.gap.agents.middleware.AgentPlatform;
import net.sf.gap.mc.qagesa.agents.TranscodingAgent;
import net.sf.gap.mc.qagesa.stats.QAGESAStat;
import eduni.simjava.Sim_system;

/**
 * 
 * @author Giovanni Novelli
 */
public abstract class Platform extends AgentPlatform {


	/** Creates a new instance of Platform */
	public Platform(String name, boolean trace) throws Exception {
		super(name, trace);
	}

    @Override
        public void initialize() throws Exception {
            super.initialize();
            this.initPlatform();
            this.initAgents();
            QAGESAStat.reset(this.getVirtualOrganization().getNumCEs());
        }
        
        public abstract void initializeServices() throws Exception;
        
        public abstract void initAgents() throws Exception;
        
	@Override
	public void initPlatform() throws Exception {
		super.initPlatform();

                this.getVirtualOrganization().initialize();

                this.initializeServices();
	}
}
