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

import java.util.Properties;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;

import net.sf.gap.mc.qagesa.grid.QAGESAVirtualOrganization;
import net.sf.gap.mc.qagesa.simulation.impl.Simulation;
import net.sf.gap.mc.qagesa.simulation.impl.XMLSimulation;
import net.sf.gap.ui.UserInterface;

/**
 * This class is responsible of main entry method of QAGESA's simulation
 * 
 * @author Giovanni Novelli
 */
public class QAGESA {
	public static void main(String[] args) {
            String outputPath;
		Properties conf = new Properties();
		try {
			conf.load(new FileInputStream("QAGESA.conf"));
		} catch (final IOException e) {
			e.printStackTrace(System.err);
			System.exit(1);
		}
		boolean swing = false;
                String prop;
                prop = conf.getProperty("link");
                if (prop!=null) {
                    try {
                            conf.load(new FileInputStream(prop));
                    } catch (final IOException e) {
                            e.printStackTrace(System.err);
                            System.exit(1);
                    }
                }
                outputPath = conf.getProperty("output");
                // Create a directory; all non-existent ancestor directories are
                // automatically created
                File outputDir = new File(outputPath);
                boolean success = outputDir.mkdirs();
                if (!success) {
                    success = QAGESA.deleteDir(outputDir);
                    success = outputDir.mkdirs();
                }
                if (!success) {
                    System.exit(2);
                }
                prop = conf.getProperty("ui");
                if (prop.compareTo("false") == 0) {
                        swing = false;
                }
                if (prop.compareTo("true") == 0) {
                        swing = true;
                }
                prop = conf.getProperty("measure");
                Integer whichMeasure = 3;
                if (prop.compareTo("MS") == 0) {
                        whichMeasure = QAGESAVirtualOrganization.MS;
                }
                if (prop.compareTo("MF") == 0) {
                        whichMeasure = QAGESAVirtualOrganization.MF;
                }
                if (prop.compareTo("MR") == 0) {
                        whichMeasure = QAGESAVirtualOrganization.MR;
                }
                if (prop.compareTo("RMS") == 0) {
                        whichMeasure = QAGESAVirtualOrganization.RMS;
                }
                if (prop.compareTo("RMF") == 0) {
                        whichMeasure = QAGESAVirtualOrganization.RMF;
                }
                if (prop.compareTo("RMR") == 0) {
                        whichMeasure = QAGESAVirtualOrganization.RMR;
                }
                prop = conf.getProperty("users");
                Integer numUsers = Integer.parseInt(prop);
                prop = conf.getProperty("requests");
                Integer numRequests = Integer.parseInt(prop);

                prop = conf.getProperty("replications");
                Integer numReplications = Integer.parseInt(prop);
                prop = conf.getProperty("confidence");
                Double confidence = Double.parseDouble(prop);
                prop = conf.getProperty("accuracy");
                Double accuracy = Double.parseDouble(prop);
                try {
                        if (swing) {
                                java.awt.EventQueue.invokeAndWait(new Runnable() {
                                        public void run() {
                                                new UserInterface("QAGESA Simulation")
                                                                .setVisible(true);
                                        }
                                });
                        }
                } catch (Exception e) {
                        e.printStackTrace();
                        System.out.println("Unwanted errors happen");
                }

                String xml = conf.getProperty("xml");
                String xsd = conf.getProperty("xsd");
                if (xml!=null) {
			QAGESA.simulate(xml,xsd,numUsers, numRequests, false, whichMeasure,
					numReplications, confidence, accuracy, swing);
                } else  {
                    QAGESA.simulate(numUsers, numRequests, false, whichMeasure,
                                numReplications, confidence, accuracy, swing);
                }
		try {
                    QAGESA.copy("sim_trace", outputPath+"/sim_trace");
                    QAGESA.copy("sim_report", outputPath+"/sim_report");
		} catch (final IOException e) {
			e.printStackTrace(System.err);
			System.exit(1);
		}
	}

	private static void simulate(int numUsers, int numRequests,
			boolean caching, int whichMeasure, int replications,
			double confidence, double accuracy, boolean swing) {

		Simulation simulation;
		simulation = new Simulation(numUsers, numRequests, caching,
				whichMeasure, replications, confidence, accuracy);
		simulation.start();
	}

	private static void simulate(String xml, String xsd, int numUsers, int numRequests,
			boolean caching, int whichMeasure, int replications,
			double confidence, double accuracy, boolean swing) {

		XMLSimulation simulation;
		simulation = new XMLSimulation(xml, xsd, numUsers, numRequests, caching,
				whichMeasure, replications, confidence, accuracy);
		simulation.start();
	}
        // Deletes all files and subdirectories under dir.
        // Returns true if all deletions were successful.
        // If a deletion fails, the method stops attempting to delete and returns false.
        private static boolean deleteDir(File dir) {
            if (dir.isDirectory()) {
                String[] children = dir.list();
                for (int i=0; i<children.length; i++) {
                    boolean success = deleteDir(new File(dir, children[i]));
                    if (!success) {
                        return false;
                    }
                }
            }

            // The directory is now empty so delete it
            return dir.delete();
        }

    // Copies src file to dst file.
    // If the dst file does not exist, it is created
    private static void copy(String srcPath, String dstPath) throws IOException {
        File src = new File(srcPath);
        File dst = new File(dstPath);
        InputStream in = new FileInputStream(src);
        OutputStream out = new FileOutputStream(dst);
    
        // Transfer bytes from in to out
        byte[] buf = new byte[1024];
        int len;
        while ((len = in.read(buf)) > 0) {
            out.write(buf, 0, len);
        }
        in.close();
        out.close();
        src.delete();
    }

}
