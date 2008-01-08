/*
 ****************************************************************************************
 * Copyright © Giovanni Novelli                                             
 * All Rights Reserved.                                                                 
 ****************************************************************************************
 *
 * License:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * COREAgent.java
 *
 * Created on 8 January 2008, 09.28 by Giovanni Novelli
 *
 * $Id$
 *
 */
package net.sf.gap.mc.core.agents;

import eduni.simjava.Sim_event;

import net.sf.gap.agents.GridAgent;
import net.sf.gap.grid.components.GridElement;

/**
 * 
 * This class is responsible for abstracting new behaviours plugging
 *
 * @author Giovanni Novelli
 */
public abstract class COREAgent extends GridAgent {

	/**
	 * Creates a new instance of COREAgent class
	 * 
	 * @param name agent name
	 * @param agentSizeInBytes agent size in bytes
	 * @param trace_flag trace flag for GridSim
	 * @throws Exception
	 */
	public COREAgent(
                GridElement ge, 
                String name, 
                int agentSizeInBytes,
		boolean trace_flag) 
                throws Exception 
        {
		super(ge, name, agentSizeInBytes, trace_flag);
	}

        
        
        
        /**
         * Method used to process extended agent's behaviour
         * 
         * @param ev Event triggering agent's actions
         */
        public void processOtherEvent(Sim_event ev) {
            switch (ev.get_tag()) {
                default:
                    break;
            }
        }
}
