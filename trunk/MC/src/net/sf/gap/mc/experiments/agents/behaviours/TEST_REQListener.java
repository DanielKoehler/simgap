/*
 ****************************************************************************************
 * Copyright © Giovanni Novelli                                             
 * All Rights Reserved.                                                                 
 ****************************************************************************************
 *
 * License:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * TEST_REQListener.java
 *
 * Created on 8 January 2008, 09.28 by Giovanni Novelli
 *
 * $Id$
 *
 */  

package net.sf.gap.mc.experiments.agents.behaviours;

import eduni.simjava.*;

import gridsim.*;

import net.sf.gap.agents.PluggableAgent;
import net.sf.gap.agents.behaviours.Sim_eventListener;
import net.sf.gap.mc.experiments.constants.ExperimentsTags;

/**
 *
 * @author Giovanni Novelli
 */
public class TEST_REQListener extends Sim_eventListener {
    public TEST_REQListener(PluggableAgent anAgent, int aTag) {
        super(anAgent, aTag);
    }
    
    public void processEvent(Sim_event ev) {
		switch (ev.get_tag()) {
                // Gridlets Management
		case ExperimentsTags.TEST_REQ:
                    int userid = ev.get_src();
                    this.getAgent().send(this.getAgent().getOutput(), GridSimTags.SCHEDULE_NOW,
                            ExperimentsTags.TEST_REP, new IO_data(ExperimentsTags.TEST_REP, 10, userid));
                    double evsend_time = GridSim.clock();
                    String msg = String.format(
                            "%1$f TEST_REP %2$s --> %3$s", evsend_time,
                           this.getAgent().get_name(), Sim_system.get_entity(userid).get_name());
                    this.getAgent().write(msg);
                    break;
                default:
                    break;
                }
    }
}
