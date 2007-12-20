/*
 ****************************************************************************************
 * Copyright © Giovanni Novelli                                             
 * All Rights Reserved.                                                                 
 ****************************************************************************************
 *
 * Title:        GAP Simulator
 * Description:  GAP (Grid Agents Platform) Toolkit for Modeling and Simulation
 *               of Mobile Agents on Grids
 * License:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * AbstractSimulation.java
 *
 * Created on 25 August 2006, 12.00 by Giovanni Novelli
 *
 ****************************************************************************************
 *
 * $Revision: 1141 $
 * $Id: AbstractSimulation.java 1141 2007-07-18 18:22:15Z gnovelli $
 * $HeadURL: file:///var/svn/grid/trunk/GAP/src/net/sf/gap/simulation/AbstractSimulation.java $
 *
 *****************************************************************************************
 */


package net.sf.gap.simulation;

import eduni.simjava.Sim_system;

import net.sf.gap.util.EntitiesCounter;
/**
 *
 * @author Giovanni Novelli
 */
public abstract class AbstractSimulation {
    // Parameters for Sim_system.IND_REPLICATIONS output analysis method
    private int replications; // The number of replications to perform
    private int maxReplications; // The max number of replications to perform in BATCH_MEANS
    private double confidence; // The confidence level that will be used to produce the simulation's measures' confidence intervals
    private double accuracy;

	/**
	 * Creates a new instance of AbstractSimulation
	 */
	public AbstractSimulation() {
		EntitiesCounter entitiesCounter = new EntitiesCounter();
		this.setReplications(1);
		this.setMaxReplications(1);
	}

	/**
	 * Creates a new instance of AbstractSimulation
	 */
	public AbstractSimulation(int replications) {
		EntitiesCounter entitiesCounter = new EntitiesCounter();
		this.setReplications(replications);
		this.setMaxReplications(replications);
                this.setConfidence(0.95);
	}

	/**
	 * Creates a new instance of AbstractSimulation
	 */
	public AbstractSimulation(int replications, double confidence, double accuracy) {
		EntitiesCounter entitiesCounter = new EntitiesCounter();
		this.setReplications(replications);
                this.setMaxReplications(replications);
                this.setConfidence(confidence);
                this.setAccuracy(accuracy);
	}

	/**
	 * Creates a new instance of AbstractSimulation
	 */
	public AbstractSimulation(int minReplications, int maxReplications, double confidence) {
		EntitiesCounter entitiesCounter = new EntitiesCounter();
		this.setReplications(minReplications);
                this.setMaxReplications(maxReplications);
                this.setConfidence(confidence);
	}
    public abstract void start();

    public boolean useReplications() {
        return this.getReplications()>2;
    }
    
    public void setupOutputAnalysis() {
			if (useReplications()) {
                            if (this.getMaxReplications() > this.getReplications()) {
                                    Sim_system.set_output_analysis(Sim_system.BATCH_MEANS,
                                                    this.getReplications(), this.getMaxReplications(), this.getConfidence());
                            } else {
                                    Sim_system.set_output_analysis(Sim_system.IND_REPLICATIONS,
                                                    this.getReplications(), this.getConfidence());
                            }
                        }
    }
    
    public int getReplications() {
        return replications;
    }

    public void setReplications(int replications) {
        this.replications = replications;
    }

    public double getConfidence() {
        return confidence;
    }

    public void setConfidence(double confidence) {
        this.confidence = confidence;
    }

    public int getMaxReplications() {
        return maxReplications;
    }

    public void setMaxReplications(int maxReplications) {
        this.maxReplications = maxReplications;
    }

    public double getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(double accuracy) {
        this.accuracy = accuracy;
    }
}
