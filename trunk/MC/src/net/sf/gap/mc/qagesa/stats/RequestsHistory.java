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
 * RequestsHistory.java
 *
 * Created on 06 April 2007, 18:19 by Giovanni Novelli
 *
 * $Id$
 *
 */
package net.sf.gap.mc.qagesa.stats;

import java.util.Iterator;
import java.util.LinkedList;

import net.sf.gap.mc.QAGESA;

/**
 *
 * @author Giovanni Novelli
 */
public class RequestsHistory extends LinkedList<RequestsHistoryEntry> {

    /**
     * 
     */
    private static final long serialVersionUID = 3973086405868206555L;
    private int numCEs;
    private int processed;
    private int playRequests;

    /** Creates a new instance of RequestsHistory */
    public RequestsHistory(int numCEs) {
        this.setPlayRequests(0.0, 0, false);
        this.processed = 0;
        this.setNumCEs(numCEs);
    }

    @SuppressWarnings("unchecked")
    public String toString() {
        String str = "";
        Iterator it = this.iterator();
        while (it.hasNext()) {
            str += "CSV;" + QAGESAStat.getReplication() + ";" + it.next().toString() + "\n";
        }
        return str;
    }

    public void inc(double clock) {
        this.setPlayRequests(clock, this.getPlayRequests() + 1, false);
    }

    public void dec(double clock, boolean success) {
        if (success) {
            processed++;
        }
        this.setPlayRequests(clock, this.getPlayRequests() - 1, success);
    }

    public synchronized int getPlayRequests() {
        return playRequests;
    }

    public synchronized void setPlayRequests(double clock, int playRequests, boolean success) {
            int rep = QAGESAStat.getReplication();
            int nu = QAGESAStat.getNumUsers();
            int ca = 0;
            if (QAGESAStat.isCachingEnabled()) {
                ca = 1;
            }
            int wm = QAGESAStat.getWhichMeasure();
        RequestsHistoryEntry entry = new RequestsHistoryEntry(clock - QAGESA.getStartTime(), playRequests);
        if (clock > QAGESA.getStartTime()) {
            //QAGESA.outReF_CR.println("CSV;ReF_CR;" + QAGESAStat.getReplication() + ";" + QAGESAStat.getNumUsers() + ";" + QAGESAStat.isCachingEnabled() + ";" + QAGESAStat.getWhichMeasure() + ";" + entry);
            double t = entry.getClock();
            int r = entry.getPlayRequests();
            QAGESA.outReF_CR.printf(
                    "CSV\tReF_CR\t%2d\t%4d\t%d\t%d\t%6.4f\t%d\n",
                    rep,
                    nu,
                    ca,
                    wm,
                    t,
                    r);
        }
        if (success) {
            RequestsHistoryEntry pentry = new RequestsHistoryEntry(clock - QAGESA.getStartTime(), processed);
            if (clock > QAGESA.getStartTime()) {
                //QAGESA.outReF_PR.println("CSV;ReF_PR;" + QAGESAStat.getReplication() + ";" + QAGESAStat.getNumUsers() + ";" + QAGESAStat.isCachingEnabled() + ";" + QAGESAStat.getWhichMeasure() + ";" + pentry);
                double t = pentry.getClock();
                int pr = pentry.getPlayRequests();
                int cr = entry.getPlayRequests();
                int wr=Math.max(cr-pr,0);
                QAGESA.outReF_PR.printf(
                        "CSV\tReF_PR\t%2d\t%4d\t%d\t%d\t%6.4f\t%d\t%d\t%d\n",
                        rep,
                        nu,
                        ca,
                        wm,
                        t,
                        pr,
                        cr,
                        wr);
            }
        }
        this.add(entry);
        this.playRequests = playRequests;
    }

    public int getNumCEs() {
        return numCEs;
    }

    public void setNumCEs(int numCEs) {
        this.numCEs = numCEs;
    }

    public int getProcessed() {
        return processed;
    }

    public void setProcessed(int processed) {
        this.processed = processed;
    }
}
