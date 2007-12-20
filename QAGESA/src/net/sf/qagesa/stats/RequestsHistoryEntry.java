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
 * RequestsHistoryEntry.java
 *
 * Created on 06 April 2007, 18:19 by Giovanni Novelli
 *
 * $Id: RequestsHistoryEntry.java 1142 2007-07-18 20:49:58Z gnovelli $
 *
 */

package net.sf.qagesa.stats;

/**
 *
 * @author Giovanni Novelli
 */
public class RequestsHistoryEntry {
    private double clock;
    private int playRequests;
    
    /** Creates a new instance of RequestsHistoryEntry */
    public RequestsHistoryEntry(double clock, int playRequests) {
        this.setClock(clock);
        this.setPlayRequests(playRequests);
    }

    public double getClock() {
        return clock;
    }

    public void setClock(double clock) {
        this.clock = clock;
    }

    public int getPlayRequests() {
        return playRequests;
    }

    public void setPlayRequests(int playRequests) {
        this.playRequests = playRequests;
    }
    
    public String toString() {
        return "ReF;" + this.getClock() + ";" + this.getPlayRequests();
    }
}
