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
 * Created on 1 March 2008, 18.07 by Giovanni Novelli
 *
 * $Id$
 *
 */
package net.sf.gap.mc;

import java.io.*;

public class QAGESAConfigurator {

    public QAGESAConfigurator() {
    }
    private static String confDir = "conf/";
    public static void main(String[] args) {
        prepareConf();
        try {
            for (int iXML = 1; iXML <= 1; iXML++) {
                for (int iMeasure=0;iMeasure<2;iMeasure++) {
                    String measure="";
                    switch (iMeasure) {
                        case 0:
                            measure = "MS";
                            break;
                        case 1:
                            measure = "RMS";
                            break;
                        case 2:
                            measure = "MF";
                            break;
                        case 3:
                            measure = "RMF";
                            break;
                        case 4:
                            measure = "MR";
                            break;
                        case 5:
                            measure = "RMR";
                            break;
                        default:
                            break;
                    }
                    int[] users = {16,32,64};
                    for (int iUsers=0;iUsers<users.length;iUsers++) {
                        int[] requests = {1};
                        for (int iRequests=0;iRequests<requests.length;iRequests++) {
                            String[] distribution = {"scaled"};
                            for (int iDistribution=0;iDistribution<distribution.length;iDistribution++) {
                                boolean[] ca = {false};
                                for (int iCA=0;iCA<ca.length;iCA++) {
                                    //boolean[] re = {false, true};
                                    boolean[] re = {true};
                                    for (int iRE=0;iRE<re.length;iRE++) {
                                        boolean[] ra = {false,true};
                                        for (int iRA=0;iRA<ra.length;iRA++) {
                                            boolean[] ds = {false,true};
                                            for (int iDS=0;iDS<ds.length;iDS++) {
                                                boolean[] cg = {false,true};
                                                for (int iCG=0;iCG<cg.length;iCG++) {
                                                String name = "";
                                                if (iXML == 0) {
                                                    name += "notxml";
                                                } else {
                                                    name += "xml";
                                                }
                                                name = name + "_" + users[iUsers];
                                                name = name + "_" + requests[iRequests];
                                                name = name + "_" + distribution[iDistribution];
                                                if (ds[iDS]) {
                                                  name = name + "_ds";   
                                                } else {
                                                  name = name + "_notds";   
                                                }
                                                if (cg[iCG]) {
                                                  name = name + "_cg";   
                                                } else {
                                                  name = name + "_notcg";   
                                                }
                                                if (ca[iCA]) {
                                                  name = name + "_ca";   
                                                } else {
                                                  name = name + "_notca";   
                                                }
                                                if (re[iRE]) {
                                                  name = name + "_re";   
                                                } else {
                                                  name = name + "_notre";   
                                                }
                                                if (ra[iRA]) {
                                                  name = name + "_ra";   
                                                } else {
                                                  name = name + "_notra";   
                                                }
                                                name = name + "_" + measure;
                                                File outFile = new File(confDir+"QAGESA_"+name+".conf");
                                                PrintStream outConf = new PrintStream(new FileOutputStream(outFile, false));
                                                outConf.println("p2p            = false");
                                                outConf.println("output         = results");
                                                if (iXML==1) {
                                                outConf.println("xml            = xml/egeeit.xml");
                                                outConf.println("xsd            = xml/scenario.xsd");
                                                outConf.println("peExpansion    = 1");
                                                } else {
                                                outConf.println("ces            = 4");
                                                outConf.println("machines       = 16");
                                                outConf.println("pes            = 1");
                                                outConf.println("mips           = 1000");
                                                outConf.println("ses            = 4");
                                                }
                                                outConf.println("measure        = " + measure);
                                                outConf.println("users          = " + users[iUsers]);
                                                outConf.println("fromRouter     = 0");
                                                outConf.println("requests       = " + requests[iRequests]);
                                                outConf.println("start          = 1000");
                                                outConf.println("end            = 6000");
                                                outConf.println("relax          = 1400");

                                                outConf.println("directSubmit   = " + ds[iDS]);
                                                outConf.println("ceGIS          = " + cg[iCG]);

                                                outConf.println("caching        = " + ca[iCA]);
                                                outConf.println("repeated       = " + re[iRE]);
                                                outConf.println("reuseagents    = " + ra[iRA]);

                                                outConf.println("abortEnabled   = false");

                                                outConf.println("betaLatency    = 0.5");

                                                outConf.println("div            = 10");
                                                outConf.println("expansion      = 5");
                                                outConf.println("retryCount     = 2");

                                                outConf.println("violations     = 1.05");
                                                outConf.println("abortThreeshold= 0.99");
                                                outConf.println("failThreeshold = 0.1");

                                                outConf.println("# Initial compression ratio");
                                                outConf.println("ratio          = 0.10");
                                                outConf.println("beta           = 0.318");
                                                outConf.println("qosloss        = 0.5");
                                                outConf.println("gridload       = 0.0");

                                                outConf.println("distribution   = " + distribution[iDistribution]);
                                                if (distribution[iDistribution].compareTo("zipf")==0) {
                                                outConf.println("thetaU         = 0.5");
                                                outConf.println("thetaR         = 0.25");
                                                }
                                                if (distribution[iDistribution].compareTo("scaled")==0) {
                                                outConf.println("linearDelta    = 0.5");
                                                }
                                                outConf.println("replications   = 1");
                                                outConf.println("confidence     = 0.95");
                                                outConf.println("accuracy       = 0.10");

                                                outConf.println("ui             = false");
                                                /*
                                                int seconds = users[iUsers] * requests[iRequests] * 60;
                                                int relax   = seconds - 100;
                                                outConf.println("end            = " + (1000+seconds));
                                                outConf.println("relax          = " + relax);
                                                 */
                                                outConf.close();
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void prepareConf() {
        File outputDir = new File(confDir);
        deleteDir(outputDir);
        boolean success = outputDir.mkdirs();
        if (!success) {
            success = deleteDir(outputDir);
            success = outputDir.mkdirs();
        }
        if (!success) {
            System.exit(2);
        }
    }

    // Deletes all files and subdirectories under dir.
    // Returns true if all deletions were successful.
    // If a deletion fails, the method stops attempting to delete and returns false.
    private static boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
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
        if (src.exists()) {
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
}

