/*
 ****************************************************************************************
 * Copyright © Giovanni Novelli                                             
 * All Rights Reserved.                                                                 
 ****************************************************************************************
 *
 * License:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * User.java
 *
 * Created on 8 January 2008, 12:45 by Giovanni Novelli
 *
 * $Id$
 *
 */

package net.sf.gap.mc.experiments.users;

import eduni.simjava.Sim_event;
import eduni.simjava.Sim_stat;

import gridsim.net.Link;

import net.sf.gap.mc.core.users.COREUser;

import net.sf.gap.mc.experiments.constants.ExperimentsEntityTypes;
import net.sf.gap.mc.experiments.users.impl.Measure;

/**
 * @author Giovanni Novelli
 */
public class User extends COREUser {
    private int experimentID;
    
    private Measure measure;
    
    private Sim_stat stat;
    
	/**
	 * 
	 * Creates a new instance of Submitter
	 * 
	 * @param name
	 *            AgentMiddleware entity name
	 * @param trace_flag
	 *            GridSim trace flag
	 * @throws Exception
	 *             This happens when name is null or haven't initialized
	 *             GridSim.
	 */
	public User(int experimentID, String name, Link link, boolean trace_flag)
			throws Exception {
		super(name, link, ExperimentsEntityTypes.USER_USER, trace_flag);
                this.setExperimentID(experimentID);
                this.setMeasure(new Measure(experimentID));
                this.setStat(this.getMeasure().getStat());
	}

        public void printStats() {
        }
        
        @Override
	public void processOtherEvent(Sim_event ev) {
	}

        @Override
	public void initWork() {
		this.DoIt();
	}

	private void DoIt() {
	}

    public Measure getMeasure() {
        return measure;
    }

    public void setMeasure(Measure measure) {
        this.measure = measure;
    }

    public int getExperimentID() {
        return experimentID;
    }

    public void setExperimentID(int experimentID) {
        this.experimentID = experimentID;
    }

    public Sim_stat getStat() {
        return stat;
    }

    public void setStat(Sim_stat stat) {
        this.stat = stat;
    }
}
