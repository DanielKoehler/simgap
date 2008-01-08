/*
 ****************************************************************************************
 * Copyright © Giovanni Novelli                                             
 * All Rights Reserved.                                                                 
 ****************************************************************************************
 *
 * License:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * Simulation.java
 *
 * Created on 8 January 2008, 13.00 by Giovanni Novelli
 *
 * $Id$
 *
 */

package net.sf.gap.mc.experiments.simulation.impl;

import eduni.simjava.Sim_system;
import eduni.simjava.Sim_stat;

import gridsim.GridSim;

import java.util.Calendar;

import net.sf.gap.simulation.AbstractSimulation;

import net.sf.gap.mc.experiments.EXPERIMENTS;
import net.sf.gap.mc.experiments.grid.ExperimentsVirtualOrganization;

/**
 * 
 * @author Giovanni Novelli
 */
public class Simulation extends AbstractSimulation {
        private int numUsers;
        private int numRequests;
        private int whichMeasure;
        
	private ExperimentsVirtualOrganization virtualOrganization;
        
	/**
	 * Creates a new instance of Simulation
	 */
	public Simulation(int numUsers, int numRequests, int whichMeasure, int replications, double confidence, double accuracy) {
            super(replications, confidence, accuracy);
            this.setNumUsers(numUsers);
            this.setNumRequests(numRequests);
            this.setWhichMeasure(whichMeasure);
	}

	private void initialize() throws Exception {
		Calendar calendar = Calendar.getInstance();
		boolean trace_flag = false; // mean trace GridSim events/activities
		boolean default_gis = false;

		System.out.println("Initializing GridSim package");
		GridSim.init(1, calendar, trace_flag, default_gis);
		EXPERIMENTS.initialize(500.0, 1000.0, 5000.0);
		this.setVirtualOrganization(new ExperimentsVirtualOrganization(trace_flag, 4,
				1000, 1, 16, 16, 4, 100, 100, 2, 2, true, 1.0,
                                this.getNumUsers(), this.getWhichMeasure()));
	}

	public void start() {
		try {
			this.initialize();

			// ////////////////////////////////////////
			// Final step: Starts the simulation
                        super.setupOutputAnalysis();

                      /*
                                Sim_system.set_termination_condition(Sim_system.INTERVAL_ACCURACY,
                                                           Sim_system.IND_REPLICATIONS,
                                                           this.getConfidence(),
                                                           this.getAccuracy(),
                                                           "USER_0",
                                                           Sim_stat.SERVICE_TIME);			
                       * */
                        EXPERIMENTS.startSimulation();

			System.out.println("\nFinished Simulation ...");
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Unwanted errors happen");
		}
	}

	public ExperimentsVirtualOrganization getVirtualOrganization() {
		return virtualOrganization;
	}

	public void setVirtualOrganization(ExperimentsVirtualOrganization aVirtualOrganization) {
		virtualOrganization = aVirtualOrganization;
	}

    public int getNumUsers() {
        return numUsers;
    }

    public void setNumUsers(int numUsers) {
        this.numUsers = numUsers;
    }

    public int getWhichMeasure() {
        return whichMeasure;
    }

    public void setWhichMeasure(int whichMeasure) {
        this.whichMeasure = whichMeasure;
    }

    public int getNumRequests() {
        return numRequests;
    }

    public void setNumRequests(int numRequests) {
        this.numRequests = numRequests;
    }
}
