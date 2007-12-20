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
 * ReFTriple.java
 *
 * Created on 29 March 2007, 13:16 by Giovanni Novelli
 *
 * $Id: ReFTriple.java 1142 2007-07-18 20:49:58Z gnovelli $
 *
 */

package net.sf.qagesa.agents.services.impl.ref;

import java.util.Comparator;

/**
 *
 * @author gnovelli
 */
public class ReFTriple implements Comparator, Comparable<ReFTriple> {
    private Double proximity;
    private ReFCouple couple;
    
    /** Creates a new instance of ReFTriple */
    public ReFTriple(Double proximity, ReFCouple couple) {
       this.setProximity(proximity);
       this.setCouple(couple);
    }

    public ReFTriple(double load, double latency, int ceID, int seID) {
        double aproximity = load * latency;
        Double aDistance = new Double(aproximity);
        ReFCouple aCouple = new ReFCouple(ceID,seID);
        this.setProximity(aDistance);
        this.setCouple(aCouple);
    }
    
    public String toString() {
        return "[" + this.getProximity() + ", " + this.getCouple() + "]";
    }
    
    public int compareTo(ReFTriple t) {
        return this.compare(this,t);
    }
    
    public int compare(Object o1, Object o2) {
        ReFTriple t1 = (ReFTriple) o1;
        ReFTriple t2 = (ReFTriple) o2;
        Double d1 = t1.getProximity();
        Double d2 = t2.getProximity();
        return d1.compareTo(d2);
    }

    public boolean equals(Object o) {
        ReFTriple t = (ReFTriple) o;
        return (this.getProximity().equals(t.getProximity())) && (this.getCouple().equals(t.getCouple()));
    }
    
    public Double getProximity() {
        return proximity;
    }

    public void setProximity(Double proximity) {
        this.proximity = proximity;
    }

    public ReFCouple getCouple() {
        return couple;
    }

    public void setCouple(ReFCouple couple) {
        this.couple = couple;
    }
}
