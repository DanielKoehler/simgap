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
 * Created on 4 February 2007, 18.07 by Giovanni Novelli
 *
 * $Id$
 *
 */

package net.sf.gap.mc.qagesa.simulation.impl;

import eduni.simjava.Sim_stat;
import eduni.simjava.Sim_system;
import gridsim.GridSim;

import java.util.Calendar;

import net.sf.gap.mc.qagesa.QAGESA;
import net.sf.gap.mc.qagesa.grid.QAGESAXMLVirtualOrganization;
import net.sf.gap.mc.qagesa.constants.QAGESAMeasures;
import net.sf.gap.mc.qagesa.stats.QAGESAStat;
import net.sf.gap.simulation.AbstractSimulation;

/**
 * 
 * @author Giovanni Novelli
 */
public class XMLSimulation extends AbstractSimulation {
        private String _XML;
        private String _XSD;
    
        private int numUsers;
        private int numRequests;
        private boolean caching;
        private int whichMeasure;
        
        private double pstart;
        private double start;
        private double end;
        
	private QAGESAXMLVirtualOrganization virtualOrganization;
        
	/**
	 * Creates a new instance of Simulation
	 */
	public XMLSimulation(String xml, String xsd, double pstart, double start, double end, int numUsers, int numRequests, boolean caching, int whichMeasure, int replications, double confidence, double accuracy) {
            super(replications, confidence, accuracy);
            this.setPstart(pstart);
            this.setStart(start);
            this.setEnd(end);
            this.setXML(xml);
            this.setXSD(xsd);
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
	public XMLSimulation(String xml, String xsd, double pstart, double start, double end, int numUsers, boolean caching, int whichMeasure, int replications) {
            super(replications);
            this.setPstart(pstart);
            this.setStart(start);
            this.setEnd(end);
            this.setXML(xml);
            this.setXSD(xsd);
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
	public XMLSimulation(String xml, String xsd, double pstart, double start, double end, int numUsers, boolean caching, int whichMeasure, int replications, double confidence, double accuracy) {
            super(replications,confidence, accuracy);
            this.setPstart(pstart);
            this.setStart(start);
            this.setEnd(end);
            this.setXML(xml);
            this.setXSD(xsd);
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
	public XMLSimulation(String xml, String xsd, double pstart, double start, double end, int numUsers, boolean caching, int whichMeasure, int minReplications, int maxReplications, double confidence) {
            super(minReplications,maxReplications,confidence);
            this.setPstart(pstart);
            this.setStart(start);
            this.setEnd(end);
            this.setXML(xml);
            this.setXSD(xsd);
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
		this.setVirtualOrganization(
                        new QAGESAXMLVirtualOrganization(
                            this.getXML(), 
                            this.getXSD(),
                            1.0,
                            this.getNumUsers(), 
                            this.isCaching(), 
                            this.getWhichMeasure(), 
                            this.getNumRequests()));
                System.out.println("AVAILABLE MIPS: " + this.getVirtualOrganization().getMIPS()*net.sf.gap.mc.QAGESA.peExpansion);
	}

	public void start() {
		try {
			this.initialize();

			// ////////////////////////////////////////
			// Final step: Starts the simulation
                        super.setupOutputAnalysis();

                        String prefix = "";
                      switch (this.getWhichMeasure()) {
                          case QAGESAMeasures.RMR:
                              prefix = "RMR";
                              break;
                          case QAGESAMeasures.MR:
                              prefix = "MR";
                              break;
                          case QAGESAMeasures.RMS:
                              prefix = "RMS";
                              break;
                          case QAGESAMeasures.MS:
                              prefix = "MS";
                              break;
                          case QAGESAMeasures.RMF:
                              prefix = "RMF";
                              break;
                          case QAGESAMeasures.MF:
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

	public QAGESAXMLVirtualOrganization getVirtualOrganization() {
		return virtualOrganization;
	}

	public void setVirtualOrganization(QAGESAXMLVirtualOrganization aVirtualOrganization) {
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

    public String getXML() {
        return _XML;
    }

    public void setXML(String XML) {
        this._XML = XML;
    }

    public String getXSD() {
        return _XSD;
    }

    public void setXSD(String XSD) {
        this._XSD = XSD;
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
