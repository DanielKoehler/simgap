/*
 ****************************************************************************************
 * Copyright © Giovanni Novelli                                             
 * All Rights Reserved.                                                                 
 ****************************************************************************************
 *
 * License:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * EXPERIMENTS.java
 *
 * Created on 8 January 2008, 14.45 by Giovanni Novelli
 *
 * $Id$
 *
 */

package net.sf.gap.mc;

import net.sf.gap.mc.core.ui.UserInterface;

import net.sf.gap.mc.experiments.simulation.impl.Simulation;

/**
 * This class is responsible of main entry method of EXPERIMENTS's simulation
 * 
 * @author Giovanni Novelli
 */
public class EXPERIMENTS {
    public static int _01_JOB_SUBMISSION = 1;
    
	public static void main(String[] args) {
		boolean swing = false;
		if (args.length > 0) {
			if (args[0].compareTo("--noui") == 0) {
				swing = false;
			}
			if (args[0].compareTo("--ui") == 0) {
				swing = true;
			}
                        Integer experimentID = Integer.parseInt(args[1]);

                        Integer numUsers = Integer.parseInt(args[2]);
                        Integer numRequests = Integer.parseInt(args[3]);
                        
                        Integer numReplications = Integer.parseInt(args[4]);
                        Double confidence = Double.parseDouble(args[5]);
                        Double accuracy = Double.parseDouble(args[6]);
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
                
        	      EXPERIMENTS.simulate(experimentID, numUsers,numRequests,numReplications,confidence,accuracy,swing);
		}
	}

	private static void simulate(int experimentID, int numUsers, int numRequests, 
                                     int replications, double confidence, double accuracy, boolean swing) {

                        Simulation simulation;
                        simulation = new Simulation(experimentID, numUsers, numRequests, replications, confidence, accuracy);
                        simulation.start();
	}
}
