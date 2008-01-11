/*
 ****************************************************************************************
 * Copyright © Giovanni Novelli                                             
 * All Rights Reserved.                                                                 
 ****************************************************************************************
 *
 * License:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * Experiment01.java
 *
 * Created on 10 January 2008, 11.00 by Giovanni Novelli
 *
 * $Id$
 *
 */

package net.sf.gap.mc.experiments.simulation.specs;

import net.sf.gap.mc.core.ui.UserInterface;

import net.sf.gap.mc.experiments.simulation.impl.Simulation;

/**
 *
 * @author Giovanni Novelli
 */
public class Experiment01 extends Experiment {
	public static void main(String[] args) {
		boolean swing = false;
		if (args.length > 0) {
			if (args[0].compareTo("--noui") == 0) {
				swing = false;
			}
			if (args[0].compareTo("--ui") == 0) {
				swing = true;
			}

                        Integer numUsers = Integer.parseInt(args[1]);
                        Integer numRequests = Integer.parseInt(args[2]);
                        
                        Integer numReplications = Integer.parseInt(args[3]);
                        Double confidence = Double.parseDouble(args[4]);
                        Double accuracy = Double.parseDouble(args[5]);
		try {
			if (swing) {
				java.awt.EventQueue.invokeAndWait(new Runnable() {
					public void run() {
						new UserInterface("Simulation").setVisible(true);
					}
				});
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Unwanted errors happen");
		}
                
        	      simulate(numUsers,numRequests,numReplications,confidence,accuracy);
		}
	}

	private static void simulate(int numUsers, int numRequests, 
                                     int replications, double confidence, double accuracy) {
                        Simulation simulation;
                        simulation = new Simulation(Experiment._01_JOB_SUBMISSION,numUsers, numRequests, replications, confidence, accuracy);
                        simulation.start();
	}
}
