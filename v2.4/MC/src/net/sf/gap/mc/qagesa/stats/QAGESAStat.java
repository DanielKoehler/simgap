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
 * $Id$
 *
 */

package net.sf.gap.mc.qagesa.stats;
import gridsim.Accumulator;

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
        
        private static double computedMIPS;
        
        private static Accumulator globalQualityLoss;
        private static Accumulator acceptableQualityLoss;

    public static double getComputedMIPS() {
        return computedMIPS;
    }

    public static void setComputedMIPS(double aComputedMIPS) {
        computedMIPS = aComputedMIPS;
    }

    public synchronized static void incComputedMIPS(double aComputedMIPS) {
        computedMIPS = computedMIPS + aComputedMIPS;
    }

    public synchronized static void updateGlobalQualityLoss(double aQualityLoss) {
        QAGESAStat.getGlobalQualityLoss().add(aQualityLoss);
    }

    public synchronized static void updateAcceptableQualityLoss(double aAQL) {
        QAGESAStat.getAcceptableQualityLoss().add(aAQL);
    }

    public static Accumulator getGlobalQualityLoss() {
        return globalQualityLoss;
    }

    public static void setGlobalQualityLoss(Accumulator aGlobalQualityLoss) {
        globalQualityLoss = aGlobalQualityLoss;
    }

    public static Accumulator getAcceptableQualityLoss() {
        return acceptableQualityLoss;
    }

    public static void setAcceptableQualityLoss(Accumulator aAcceptableQualityLoss) {
        acceptableQualityLoss = aAcceptableQualityLoss;
    }
    
	/**
	 * Creates a new instance of QAGESAStat
	 */
	public QAGESAStat() {
	}

	public synchronized static void reset(int numCEs) {
		QAGESAStat.replication++;
		QAGESAStat.setRequestsHistory(new RequestsHistory(numCEs));
                QAGESAStat.setComputedMIPS(0);
                QAGESAStat.setGlobalQualityLoss(new Accumulator());
                QAGESAStat.setAcceptableQualityLoss(new Accumulator());
	}

	public synchronized static void incRequests(double clock) {
		QAGESAStat.getRequestsHistory().inc(clock);
	}

	public synchronized static void decRequests(double clock,boolean success) {
		QAGESAStat.getRequestsHistory().dec(clock,success);
	}

	public synchronized static int getRequests() {
		return QAGESAStat.getRequestsHistory().getPlayRequests();
	}

	public synchronized static int getProcessedRequests() {
		return QAGESAStat.getRequestsHistory().getProcessed();
	}

	public synchronized static RequestsHistory getRequestsHistory() {
		return requestsHistory;
	}

	public synchronized static void setRequestsHistory(
			RequestsHistory aRequestsHistory) {
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
