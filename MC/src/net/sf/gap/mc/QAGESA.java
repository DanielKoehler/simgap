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

import java.util.Locale;
import java.util.Properties;
import java.io.*;

import net.sf.gap.mc.qagesa.constants.QAGESAMeasures;
import net.sf.gap.mc.qagesa.simulation.impl.Simulation;
import net.sf.gap.mc.qagesa.simulation.impl.XMLSimulation;
import net.sf.gap.mc.qagesa.users.impl.User;
import net.sf.gap.ui.UserInterface;


// All writes to this print stream are copied to two print streams
/**
 * This class is responsible of main entry method of QAGESA's simulation
 * 
 * @author Giovanni Novelli
 */
public class QAGESA {

    public static double getStartTime() {
        return startTime;
    }

    public static void setStartTime(double aStartTime) {
        startTime = aStartTime;
    }
    public static double relaxTime = 200.0;
    public static int requests;
    public static int retryCount;
    public static double linearDelta=1.0-0.61803398874989484820458683436564;
    public static double thetaU=0.5;
    public static double thetaR=0.25;
    public static boolean caching;
    public static boolean repeated;
    public static boolean reuseagents;
    public static boolean ceGIS;
    public static boolean directSubmit;
    private static double startTime;
    public static int div;
    public static int peExpansion;
    public static int expansion;
    public static double beta;
    public static double qosloss;
    public static double gridload;
    public static double normalizedViolationThreeshold;
    public static boolean abortEnabled;
    public static double abortThreeshold;
    public static double failThreeshold;
    public static double initialCompressionRatio;
    public static int fromRouter;
    
    public static double alfaLoad;
    public static double betaLatency;

    public static boolean randomSelection;
    
    public static boolean p2p;
    
    public static String getOutputPath() {
        return outputPath;
    }

    public static void setOutputPath(String aOutputPath) {
        outputPath = aOutputPath;
    }
    private static String outputPath;
    private static String usercsvName;

    public static void main(String[] args) {
    // Get default locale
    Locale locale = new Locale("en", "US");
    Locale.setDefault(locale);
        String confname = "QAGESA.conf";
        if (args.length == 1) {
            confname = args[0];
        }
        QAGESA.execute(confname);
    }

    private static String usedConf;
    public static void execute(String confname) {
        usedConf=confname;
        Properties conf = new Properties();
        try {
            conf.load(new FileInputStream(confname));
        } catch (final IOException e) {
            e.printStackTrace(System.err);
            System.exit(1);
        }
        boolean swing = false;
        String prop;
        prop = conf.getProperty("link");
        if (prop != null) {
            try {
                conf.load(new FileInputStream(prop));
                usedConf = prop;
            } catch (final IOException e) {
                e.printStackTrace(System.err);
                System.exit(1);
            }
        }
        setOutputPath(conf.getProperty("output"));
        QAGESA.prepareOutput();
        prop = conf.getProperty("ui");
        if (prop.compareTo("false") == 0) {
            swing = false;
        }
        if (prop.compareTo("true") == 0) {
            swing = true;
        }
        prop = conf.getProperty("measure");
        Integer whichMeasure = 3;
        usercsvName = "USERS_" + prop + ".csv";
        QAGESA.openOutput();
        if (prop.compareTo("MS") == 0) {
            whichMeasure = QAGESAMeasures.MS;
            randomSelection = false;
        }
        if (prop.compareTo("MF") == 0) {
            whichMeasure = QAGESAMeasures.MF;
            randomSelection = false;
        }
        if (prop.compareTo("MR") == 0) {
            whichMeasure = QAGESAMeasures.MR;
            randomSelection = false;
        }
        if (prop.compareTo("RMS") == 0) {
            whichMeasure = QAGESAMeasures.RMS;
            randomSelection = true;
        }
        if (prop.compareTo("RMF") == 0) {
            whichMeasure = QAGESAMeasures.RMF;
            randomSelection = true;
        }
        if (prop.compareTo("RMR") == 0) {
            whichMeasure = QAGESAMeasures.RMR;
            randomSelection = true;
        }

        prop = conf.getProperty("p2p");
        p2p = Boolean.parseBoolean(prop);
        
        prop = conf.getProperty("ceGIS");
        ceGIS = Boolean.parseBoolean(prop) && randomSelection;

        prop = conf.getProperty("directSubmit");
        directSubmit = Boolean.parseBoolean(prop) && randomSelection;

        prop = conf.getProperty("users");
        Integer numUsers = Integer.parseInt(prop);
        
        prop = conf.getProperty("fromRouter");
        fromRouter = Integer.parseInt(prop);
        
        prop = conf.getProperty("requests");
        Integer numRequests;
        numRequests = Integer.parseInt(prop);
        prop = conf.getProperty("retryCount");
        retryCount = Integer.parseInt(prop);
        prop = conf.getProperty("peExpansion");
        peExpansion = Integer.parseInt(prop);
        prop = conf.getProperty("div");
        div = Integer.parseInt(prop);
        prop = conf.getProperty("expansion");
        expansion = Integer.parseInt(prop);
        prop = conf.getProperty("ratio");
        initialCompressionRatio = Double.parseDouble(prop);
        prop = conf.getProperty("beta");
        beta = Double.parseDouble(prop);
        prop = conf.getProperty("betaLatency");
        betaLatency = Double.parseDouble(prop);
        alfaLoad = 1.0 - betaLatency;
        prop = conf.getProperty("qosloss");
        qosloss = Double.parseDouble(prop);
        prop = conf.getProperty("gridload");
        gridload = Double.parseDouble(prop);
        prop = conf.getProperty("violations");
        normalizedViolationThreeshold = Double.parseDouble(prop);
        prop = conf.getProperty("abortEnabled");
        abortEnabled = Boolean.parseBoolean(prop) && !randomSelection;
        prop = conf.getProperty("abortThreeshold");
        abortThreeshold = Double.parseDouble(prop);
        prop = conf.getProperty("failThreeshold");
        failThreeshold = Double.parseDouble(prop);
        prop = conf.getProperty("distribution");
        String distribution = prop;
        if (prop != null) {
            User.setDistribution(distribution);
            if (prop.compareToIgnoreCase("zipf")==0) {
                prop = conf.getProperty("thetaU");
                QAGESA.thetaU = Double.parseDouble(prop);
                prop = conf.getProperty("thetaR");
                QAGESA.thetaR = Double.parseDouble(prop);
            } 
            if (prop.compareToIgnoreCase("scaled")==0) {
                prop = conf.getProperty("linearDelta");
                QAGESA.linearDelta = Double.parseDouble(prop);
            } 
        }

        prop = conf.getProperty("replications");
        Integer numReplications = Integer.parseInt(prop);
        prop = conf.getProperty("confidence");
        Double confidence = Double.parseDouble(prop);
        prop = conf.getProperty("accuracy");
        Double accuracy = Double.parseDouble(prop);
        //prop = conf.getProperty("pstart");
        //Double pstart = Double.parseDouble(prop);
        Double pstart = 500.0;
        prop = conf.getProperty("start");
        Double start = Double.parseDouble(prop);
        QAGESA.setStartTime(start);
        prop = conf.getProperty("end");
        Double end = Double.parseDouble(prop);
        prop = conf.getProperty("relax");
        relaxTime = Double.parseDouble(prop);
        prop = conf.getProperty("caching");
        caching = Boolean.parseBoolean(prop);
        prop = conf.getProperty("repeated");
        repeated = Boolean.parseBoolean(prop);
        prop = conf.getProperty("reuseagents");
        reuseagents = Boolean.parseBoolean(prop);
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

        String xml = conf.getProperty("xml");
        String xsd = conf.getProperty("xsd");
        if (xml != null) {
            QAGESA.simulate(xml, xsd, pstart, start, end, numUsers, numRequests, false, whichMeasure,
                    numReplications, confidence, accuracy, swing);
        } else {
            prop = conf.getProperty("ces");
            Integer numCE = Integer.parseInt(prop);
            prop = conf.getProperty("ses");
            Integer numSE = Integer.parseInt(prop);
            prop = conf.getProperty("machines");
            Integer numMachine = Integer.parseInt(prop);
            prop = conf.getProperty("pes");
            Integer numPE = Integer.parseInt(prop);
            prop = conf.getProperty("mips");
            Integer MIPS = Integer.parseInt(prop);
            QAGESA.simulate(pstart, start, end, numCE, numMachine, numPE, MIPS, numSE, numUsers, numRequests, caching, whichMeasure,
                    numReplications, confidence, accuracy, swing);
        }
        QAGESA.closeOutput();
    }

    private static void simulate(double pstart, double start, double end, 
            int numCE, int numMachine, int numPE, int MIPS, int numSE,
            int numUsers, int numRequests,
            boolean caching, int whichMeasure, int replications,
            double confidence, double accuracy, boolean swing) {

        Simulation simulation;
        simulation = new Simulation(pstart, start, end, numCE, numMachine, numPE, MIPS, numSE, numUsers, numRequests, caching,
                whichMeasure, replications, confidence, accuracy);
        simulation.start();
    }

    private static void simulate(String xml, String xsd, double pstart, double start, double end, int numUsers, int numRequests,
            boolean caching, int whichMeasure, int replications,
            double confidence, double accuracy, boolean swing) {

        XMLSimulation simulation;
        simulation = new XMLSimulation(xml, xsd, pstart, start, end, numUsers, numRequests, caching,
                whichMeasure, replications, confidence, accuracy);
        simulation.start();
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
    // Copies src file to dst file.
    // If the dst file does not exist, it is created
    private static void copywd(String srcPath, String dstPath) throws IOException {
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
        }
    }
    public static PrintStream outIO;
    public static PrintStream outFuzzy_QoS;
    public static PrintStream outQoS;
    public static PrintStream outReF_RT;
    public static PrintStream outReF_CR;
    public static PrintStream outReF_PR;
    public static PrintStream outUSER;
    public static PrintStream outUSER_Streaming;
    public static PrintStream outUSER_QoS;

    private static void prepareOutput() {
        // Create a directory; all non-existent ancestor directories are
        // automatically created
        File outputDir = new File(getOutputPath());
        QAGESA.deleteDir(outputDir);
        boolean success = outputDir.mkdirs();
        if (!success) {
            success = QAGESA.deleteDir(outputDir);
            success = outputDir.mkdirs();
        }
        if (!success) {
            System.exit(2);
        }
    }

    private static void openOutput() {
        try {
                // Tee standard output
                PrintStream out = new PrintStream(new FileOutputStream("sim_out.txt"));
                PrintStream tee = new TeeStream(System.out, out);

                System.setOut(tee);

                // Tee standard error
                PrintStream err = new PrintStream(new FileOutputStream("sim_err.txt"));
                tee = new TeeStream(System.err, err);

                System.setErr(tee);
            } catch (FileNotFoundException e) {
            }
        try {
            File outFile;
            outFile = new File(QAGESA.getOutputPath() + "/ReF_RT.csv");
            outReF_RT = new PrintStream(new FileOutputStream(outFile, true));
            outReF_RT.printf(
                    "FORMAT\t"+
                    "MEASURE\t"+
                    "REPLICATION\t"+
                    "USERS\t"+
                    "CACHING\t"+
                    "WHICH MEASURE\t"+
                    "ENTITY\t"+
                    "REQUEST TIME\t"+
                    "REPLY TIME\t"+
                    "RESPONSE TIME"+
                    "\n"
                    );
            outFile = new File(QAGESA.getOutputPath() + "/ReF_CR.csv");
            outReF_CR = new PrintStream(new FileOutputStream(outFile, true));
            outReF_CR.printf(
                    "FORMAT\t"+
                    "MEASURE\t"+
                    "REPLICATION\t"+
                    "USERS\t"+
                    "CACHING\t"+
                    "WHICH MEASURE\t"+
                    "TIME\t"+
                    "CONCURRENT REQUESTS\t"+
                    "NORMALIZED CONCURRENT REQUESTS"+
                    "\n"
                    );
            outFile = new File(QAGESA.getOutputPath() + "/ReF_PR.csv");
            outReF_PR = new PrintStream(new FileOutputStream(outFile, true));
            outReF_PR.printf(
                    "FORMAT\t"+
                    "MEASURE\t"+
                    "REPLICATION\t"+
                    "USERS\t"+
                    "CACHING\t"+
                    "WHICH MEASURE\t"+
                    "TIME\t"+
                    "REQUESTS SERVED\t"+
                    "CONCURRENT REQUESTS\t"+
                    "WR\t"+
                    "REQUESTS SERVED PER SECOND\t"+
                    "NORMALIZED CONCURRENT REQUESTS"+
                    "\n"
                    );
            outFile = new File(QAGESA.getOutputPath() + "/IO.csv");
            outIO = new PrintStream(new FileOutputStream(outFile, true));
            outIO.printf(
                    "FORMAT\t"+
                    "MEASURE\t"+
                    "REPLICATION\t"+
                    "USERS\t"+
                    "CACHING\t"+
                    "WHICH MEASURE\t"+
                    "TIME\t"+
                    "GRID ELEMENT\t"+
                    "CUMULATIVE INPUT LOAD\t"+
                    "CUMULATIVE OUTPUT LOAD\t"+
                    "CUMULATIVE IO LOAD\t"+
                    "MEAN INPUT LOAD\t"+
                    "MEAN OUTPUT LOAD\t"+
                    "MEAN IO LOAD\t"+
                    "RELATIVE IO LOAD"+
                    "\n"
                    );
            outFile = new File(QAGESA.getOutputPath() + "/Fuzzy_QoS.csv");
            outFuzzy_QoS = new PrintStream(new FileOutputStream(outFile, true));
            outFuzzy_QoS.printf(
                    "FORMAT\t"+
                    "MEASURE\t"+
                    "REPLICATION\t"+
                    "USERS\t"+
                    "CACHING\t"+
                    "WHICH MEASURE\t"+
                    "TIME\t"+
                    "AGENT\t"+
                    "COMPUTED GIGAFLOPS\t"+
                    "POTENTIAL GIGAFLOPS\t"+
                    "GRID LOAD\t"+
                    "GLOBAL QUALITY LOSS\t"+
                    "ACCEPTABLE QUALITY LOSS\t"+
                    "USER\t"+
                    "STREAM ID\t"+
                    "SEQUENCE NUMBER\t"+
                    "BYTES\t"+
                    "GIGAFLOPS\t"+
                    "CURRENT STREAM QUALITY\t"+
                    "UPDATED STREAM QUALITY\t"+
                    "ACCEPTABLE STREAM QUALITY\t"+
                    "QUALITY LOSS\t"+
                    "ACCEPTABLE QUALITY LOSS\t"+
                    "DELAY\t"+
                    "\n"
                    );
            outFile = new File(QAGESA.getOutputPath() + "/QoS.csv");
            outQoS = new PrintStream(new FileOutputStream(outFile, true));
            outQoS.printf(
                    "FORMAT\t"+
                    "MEASURE\t"+
                    "REPLICATION\t"+
                    "USERS\t"+
                    "CACHING\t"+
                    "WHICH MEASURE\t"+
                    "TIME\t"+
                    "COMPUTED GIGAFLOPS\t"+
                    "QAGESA GIGAFLOPS\t"+
                    "QAGESA LOAD\t"+
                    "GLOBAL QUALITYLOSS\t"+
                    "ACCEPTABLE QUALITYLOSS\t"+
                    "\n"
                    );
            outFile = new File(QAGESA.getOutputPath() + "/" + usercsvName);
            outUSER = new PrintStream(new FileOutputStream(outFile, true));
            outUSER.printf(
                    "FORMAT\t"+
                    "MEASURE\t"+
                    "REPLICATION\t"+
                    "USERS\t"+
                    "CACHING\t"+
                    "WHICH MEASURE\t"+
                    "USER\t"+
                    "REQUEST TIME\t"+
                    "REPLY TIME\t"+
                    "SERVICE TIME\t"+
                    "NORMALIZED SERVICE TIME\t"+
                    "\n"
                    );
            outFile = new File(QAGESA.getOutputPath() + "/USERS_QoS.csv");
            outUSER_QoS = new PrintStream(new FileOutputStream(outFile, true));
            outUSER_QoS.printf(
                    "FORMAT\t"+
                    "MEASURE\t"+
                    "REPLICATION\t"+
                    "USERS\t"+
                    "CACHING\t"+
                    "WHICH MEASURE\t"+
                    "TIME\t"+
                    "USER\t"+
                    "STREAM ID\t"+
                    "SEQUENCE NUMBER\t"+
                    "BYTES\t"+
                    "GIGAFLOPS\t"+
                    "CURRENT STREAM QUALITY\t"+
                    "ACCEPTABLE STREAM QUALITY\t"+
                    "QUALITY LOSS\t"+
                    "ACCEPTABLE QUALITY LOSS\t"+
                    "MEAN STREAM QUALITY\t" +
                    "CUMULATIVE FETCHED BYTES\t" +
                    "CUMULATIVE STREAMED BYTES\t" +
                    "\n"
                    );
            outFile = new File(QAGESA.getOutputPath() + "/USERS_Streaming.csv");
            outUSER_Streaming = new PrintStream(new FileOutputStream(outFile, true));
            outUSER_Streaming.printf(
                    "FORMAT\t"+
                    "MEASURE\t"+
                    "REPLICATION\t"+
                    "USERS\t"+
                    "CACHING\t"+
                    "WHICH MEASURE\t"+
                    "USER\t"+
                    "TIME\t"+
                    "STREAM ID\t"+
                    "ACCEPTABLE STREAM QUALITY\t"+
                    "REQUEST TIME\t" +
                    "REPLY TIME\t" +
                    "TRANSCODED FIRST CHUNK TIME\t" +
                    "SENT LAST CHUNK TIME\t" +
                    "END TIME\t" +
                    "FETCHED BYTES\t" +
                    "STREAMED BYTES\t" +
                    "RESPONSE TIME\t" +
                    "FIRST CHUNK TIME\t" +
                    "STREAMING TIME\t" +
                    "NORMALIZED STREAMING TIME\t" +
                    "DELAY\t" +
                    "MEAN DELAY\t" +
                    "NORMALIZED MEAN DELAY\t" +
                    "NORMALIZED VIOLATIONS\t" +
                    "INTIME STREAMS\t" +
                    "OUTTIME STREAMS\t" +
                    "NORMALIZED INTIME STREAMS\t" +
                    "NORMALIZED OUTTIME STREAMS" +
                    "\n"
                    );
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void closeOutput() {
        try {
            outIO.close();
            outFuzzy_QoS.close();
            outQoS.close();
            outReF_RT.close();
            outReF_CR.close();
            outReF_PR.close();
            outUSER.close();
            outUSER_QoS.close();
            outUSER_Streaming.close();
            File usedConfFile = new File(usedConf);
            QAGESA.copywd(usedConf, getOutputPath() + "/" + usedConfFile.getName());
            QAGESA.copy("sim_graphs.sjg", getOutputPath() + "/sim_graphs.sjg");
            QAGESA.copy("sim_trace", getOutputPath() + "/sim_trace.txt");
            QAGESA.copy("sim_report", getOutputPath() + "/sim_report.txt");
            //QAGESA.copy("sim_out.txt", getOutputPath() + "/sim_out.txt");
            QAGESA.copy("sim_err.txt", getOutputPath() + "/sim_err.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
class TeeStream extends PrintStream {
    PrintStream out;
    public TeeStream(PrintStream out1, PrintStream out2) {
        super(out1);
        this.out = out2;
    }
    public void write(byte buf[], int off, int len) {
        try {
            super.write(buf, off, len);
            out.write(buf, off, len);
        } catch (Exception e) {
        }
    }
    public void flush() {
        super.flush();
        out.flush();
    }
}
