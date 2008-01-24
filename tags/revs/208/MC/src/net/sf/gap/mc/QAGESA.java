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

package net.sf.gap.mc;

import net.sf.gap.mc.qagesa.grid.QAGESAVirtualOrganization;
import net.sf.gap.mc.qagesa.simulation.impl.Simulation;
import net.sf.gap.impl.ui.UserInterface;

/**
 * This class is responsible of main entry method of QAGESA's simulation
 * 
 * @author Giovanni Novelli
 */
public class QAGESA {
	public static void main(String[] args) {
		boolean swing = false;
		if (args.length > 0) {
			if (args[0].compareTo("--noui") == 0) {
				swing = false;
			}
			if (args[0].compareTo("--ui") == 0) {
				swing = true;
			}
                        Integer whichMeasure = 3;
			if (args[1].compareTo("--MS") == 0) {
				whichMeasure = QAGESAVirtualOrganization.MS;
			}
			if (args[1].compareTo("--MF") == 0) {
				whichMeasure = QAGESAVirtualOrganization.MF;
			}
			if (args[1].compareTo("--MR") == 0) {
				whichMeasure = QAGESAVirtualOrganization.MR;
			}
			if (args[1].compareTo("--RMS") == 0) {
				whichMeasure = QAGESAVirtualOrganization.RMS;
			}
			if (args[1].compareTo("--RMF") == 0) {
				whichMeasure = QAGESAVirtualOrganization.RMF;
			}
			if (args[1].compareTo("--RMR") == 0) {
				whichMeasure = QAGESAVirtualOrganization.RMR;
			}
                        Integer numUsers = Integer.parseInt(args[2]);
                        Integer numRequests = Integer.parseInt(args[3]);
                        
                        Integer numReplications = Integer.parseInt(args[4]);
                        Double confidence = Double.parseDouble(args[5]);
                        Double accuracy = Double.parseDouble(args[6]);
		try {
			if (swing) {
				java.awt.EventQueue.invokeAndWait(new Runnable() {
					public void run() {
						new UserInterface("QAGESA Simulation").setVisible(true);
					}
				});
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Unwanted errors happen");
		}
                
        	      QAGESA.simulate(numUsers,numRequests,false,whichMeasure,numReplications,confidence,accuracy,swing);
		}
	}

	private static void simulate(int numUsers, int numRequests, boolean caching, int whichMeasure, 
                                     int replications, double confidence, double accuracy, boolean swing) {

                        Simulation simulation;
                        simulation = new Simulation(numUsers, numRequests, caching, whichMeasure, replications, confidence, accuracy);
                        simulation.start();
	}
}
