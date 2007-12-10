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
 * QAGESAStat.java
 *
 * Created on 06 April 2007, 18:19 by Giovanni Novelli
 *
 * $Id: QAGESAStat.java 1142 2007-07-18 20:49:58Z gnovelli $
 *
 */

package net.sf.qagesa.stats;

/**
 *
 * @author Giovanni Novelli
 */
public class QAGESAStat {
    private static int replication = 0;
    
    private static int numUsers;
    
    private static int activeUsers;
    
    private static boolean cachingEnabled;
    
    private static int whichMeasure;
    
    private static RequestsHistory requestsHistory;
    
    /**
     * Creates a new instance of QAGESAStat
     */
    public QAGESAStat() {
    }
    
    public synchronized static void reset(int numCEs) {
        QAGESAStat.replication++;
        QAGESAStat.setRequestsHistory(new RequestsHistory(numCEs));
    }
    
    public synchronized static void incRequests(double clock) {
        QAGESAStat.getRequestsHistory().inc(clock);
    }

    public synchronized static void decRequests(double clock) {
        QAGESAStat.getRequestsHistory().dec(clock);
    }

    public synchronized static int getRequests() {
        return QAGESAStat.getRequestsHistory().getPlayRequests();
    }

    public synchronized static RequestsHistory getRequestsHistory() {
        return requestsHistory;
    }

    public synchronized static void setRequestsHistory(RequestsHistory aRequestsHistory) {
        requestsHistory = aRequestsHistory;
    }

    public static int getReplication() {
        return replication;
    }

    public static void setReplication(int aReplication) {
        replication = aReplication;
    }

    public static int getNumUsers() {
        return numUsers;
    }

    public static void setNumUsers(int aNumUsers) {
        numUsers = aNumUsers;
    }

    public static boolean isCachingEnabled() {
        return cachingEnabled;
    }

    public static void setCachingEnabled(boolean aCachingEnabled) {
        cachingEnabled = aCachingEnabled;
    }

    public static int getWhichMeasure() {
        return whichMeasure;
    }

    public static void setWhichMeasure(int aWhichMeasure) {
        whichMeasure = aWhichMeasure;
    }

    public static int getActiveUsers() {
        return activeUsers;
    }

    public static void setActiveUsers(int aActiveUsers) {
        activeUsers = aActiveUsers;
    }
}
