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
 * Simulation.java
 *
 * Created on 7 August 2006, 18.07 by Giovanni Novelli
 *
 * $Id$
 *
 */

package net.sf.gap.mc.qagesa.simulation.impl;

import eduni.simjava.Sim_stat;
import eduni.simjava.Sim_system;
import gridsim.GridSim;

import java.util.Calendar;

import net.sf.gap.GAP;
import net.sf.gap.mc.qagesa.QAGESA;
import net.sf.gap.mc.qagesa.grid.QAGESAVirtualOrganization;
import net.sf.gap.mc.qagesa.stats.QAGESAStat;
import net.sf.gap.simulation.AbstractSimulation;

/**
 * 
 * @author Giovanni Novelli
 */
public class Simulation extends AbstractSimulation {
        private int numUsers;
        private int numRequests;
        private boolean caching;
        private int whichMeasure;

        private double pstart;
        private double start;
        private double end;
        
	private QAGESAVirtualOrganization virtualOrganization;
        
	/**
	 * Creates a new instance of Simulation
	 */
	public Simulation(double pstart, double start, double end, int numUsers, int numRequests, boolean caching, int whichMeasure, int replications, double confidence, double accuracy) {
            super(replications, confidence, accuracy);
            this.setPstart(pstart);
            this.setStart(start);
            this.setEnd(end);
            this.setNumUsers(numUsers);
            this.setNumRequests(numRequests);
            this.setCaching(caching);
            this.setWhichMeasure(whichMeasure);
            QAGESAStat.setReplication(0);
            QAGESAStat.setActiveUsers(0);
            QAGESAStat.setNumUsers(this.getNumUsers());
            QAGESAStat.setCachingEnabled(caching);
            QAGESAStat.setWhichMeasure(whichMeasure);
	}

	/**
	 * Creates a new instance of Simulation
	 */
	public Simulation(double pstart, double start, double end, int numUsers, boolean caching, int whichMeasure, int replications) {
            super(replications);
            this.setPstart(pstart);
            this.setStart(start);
            this.setEnd(end);
            this.setNumUsers(numUsers);
            this.setCaching(caching);
            this.setWhichMeasure(whichMeasure);
            QAGESAStat.setNumUsers(this.getNumUsers());
            QAGESAStat.setCachingEnabled(caching);
            QAGESAStat.setWhichMeasure(whichMeasure);
	}

	/**
	 * Creates a new instance of Simulation
	 */
	public Simulation(double pstart, double start, double end, int numUsers, boolean caching, int whichMeasure, int replications, double confidence, double accuracy) {
            super(replications,confidence, accuracy);
            this.setPstart(pstart);
            this.setStart(start);
            this.setEnd(end);
            this.setNumUsers(numUsers);
            this.setCaching(caching);
            this.setWhichMeasure(whichMeasure);
            QAGESAStat.setNumUsers(this.getNumUsers());
            QAGESAStat.setCachingEnabled(caching);
            QAGESAStat.setWhichMeasure(whichMeasure);
	}
	/**
	 * Creates a new instance of Simulation
	 */
	public Simulation(double pstart, double start, double end, int numUsers, boolean caching, int whichMeasure, int minReplications, int maxReplications, double confidence) {
            super(minReplications,maxReplications,confidence);
            this.setPstart(pstart);
            this.setStart(start);
            this.setEnd(end);
            this.setNumUsers(numUsers);
            this.setCaching(caching);
            this.setWhichMeasure(whichMeasure);
            QAGESAStat.setNumUsers(this.getNumUsers());
            QAGESAStat.setCachingEnabled(caching);
            QAGESAStat.setWhichMeasure(whichMeasure);
	}

	private void initialize() throws Exception {
		Calendar calendar = Calendar.getInstance();
		boolean trace_flag = false; // mean trace GridSim events/activities
		boolean default_gis = false;
                boolean graphing = true;

		System.out.println("Initializing GridSim package");
		GridSim.init(1, calendar, trace_flag, default_gis);
                QAGESA.setGraphing(graphing);
		QAGESA.initialize(this.getPstart(), this.getStart(), this.getEnd());
		this.setVirtualOrganization(new QAGESAVirtualOrganization(trace_flag, 4,
				1000, 1, 16, 16, 4, 100, 100, 2, 2, true, 1.0,
                                this.getNumUsers(), this.isCaching(), this.getWhichMeasure(), this.getNumRequests()));
	}

	public void start() {
		try {
			this.initialize();

			// ////////////////////////////////////////
			// Final step: Starts the simulation
                        super.setupOutputAnalysis();

                        String prefix = "";
                      switch (this.getWhichMeasure()) {
                          case QAGESAVirtualOrganization.RMR:
                              prefix = "RMR";
                              break;
                          case QAGESAVirtualOrganization.MR:
                              prefix = "MR";
                              break;
                          case QAGESAVirtualOrganization.RMS:
                              prefix = "RMS";
                              break;
                          case QAGESAVirtualOrganization.MS:
                              prefix = "MS";
                              break;
                          case QAGESAVirtualOrganization.RMF:
                              prefix = "RMF";
                              break;
                          case QAGESAVirtualOrganization.MF:
                              prefix = "MF";
                              break;
                      }
                      /*
                      Sim_system.set_termination_condition(Sim_system.INTERVAL_ACCURACY,
                                                           Sim_system.IND_REPLICATIONS,
                                                           this.getConfidence(),
                                                           this.getAccuracy(),
                                                           prefix+"USER_0",
                                                           Sim_stat.SERVICE_TIME);			
                       */
                        QAGESA.startSimulation();

			System.out.println("\nFinished QAGESA Simulation ...");
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Unwanted errors happen");
		}
	}

	public QAGESAVirtualOrganization getVirtualOrganization() {
		return virtualOrganization;
	}

	public void setVirtualOrganization(QAGESAVirtualOrganization aVirtualOrganization) {
		virtualOrganization = aVirtualOrganization;
	}

    public int getNumUsers() {
        return numUsers;
    }

    public void setNumUsers(int numUsers) {
        this.numUsers = numUsers;
    }

    public boolean isCaching() {
        return caching;
    }

    public void setCaching(boolean caching) {
        this.caching = caching;
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

    public double getPstart() {
        return pstart;
    }

    public void setPstart(double pstart) {
        this.pstart = pstart;
    }

    public double getStart() {
        return start;
    }

    public void setStart(double start) {
        this.start = start;
    }

    public double getEnd() {
        return end;
    }

    public void setEnd(double end) {
        this.end = end;
    }
}
