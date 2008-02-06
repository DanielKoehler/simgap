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
    
    private int playRequests;
    
    /** Creates a new instance of RequestsHistory */
    public RequestsHistory(int numCEs) {
        this.setPlayRequests(0.0,0);
        this.setNumCEs(numCEs);
    }
    
    @SuppressWarnings("unchecked")
	public String toString() {
        String str = "";
        Iterator it = this.iterator();
        while (it.hasNext()) {
            str += "CSV;"+QAGESAStat.getReplication()+";"+it.next().toString() + "\n";
        }
        return str;
    }
    
    public void inc(double clock) {
        this.setPlayRequests(clock, this.getPlayRequests()+1);
    }

    public void dec(double clock) {
        this.setPlayRequests(clock, this.getPlayRequests()-1);
    }

    public synchronized int getPlayRequests() {
        return playRequests;
    }

    public synchronized void setPlayRequests(double clock, int playRequests) {
            RequestsHistoryEntry entry = new RequestsHistoryEntry(clock,playRequests);
            QAGESA.outReF_CR.println("CSV;ReF_CR;"+QAGESAStat.getReplication()+";"+QAGESAStat.getNumUsers()+";"+QAGESAStat.isCachingEnabled()+";"+QAGESAStat.getWhichMeasure()+";"+entry);
            this.add(entry);
            this.playRequests = playRequests;
    }

    public int getNumCEs() {
        return numCEs;
    }

    public void setNumCEs(int numCEs) {
        this.numCEs = numCEs;
    }
}
