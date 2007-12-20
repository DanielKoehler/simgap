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
 * $Id: RequestsHistory.java 1142 2007-07-18 20:49:58Z gnovelli $
 *
 */

package net.sf.qagesa.stats;


import java.util.Iterator;
import java.util.LinkedList;


/**
 *
 * @author Giovanni Novelli
 */
public class RequestsHistory extends LinkedList<RequestsHistoryEntry> {
    private int numCEs;
    
    private int playRequests;
    
    /** Creates a new instance of RequestsHistory */
    public RequestsHistory(int numCEs) {
        this.setPlayRequests(0.0,0);
        this.setNumCEs(numCEs);
        //System.out.println("FILEFORMAT;REPLICATION;NUMUSERS;CACHING;USERTYPE;ENTITY;TIMESTAMP;REQUESTS");
    }
    
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
            System.out.println("CSV;ReF_CR;"+QAGESAStat.getReplication()+";"+QAGESAStat.getNumUsers()+";"+QAGESAStat.isCachingEnabled()+";"+QAGESAStat.getWhichMeasure()+";"+entry);
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
