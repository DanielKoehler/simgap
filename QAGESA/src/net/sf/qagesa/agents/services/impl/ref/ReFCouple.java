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
 * ReFCouple.java
 *
 * Created on 29 March 2007, 13:03 by Giovanni Novelli
 *
 * $Id: ReFCouple.java 1142 2007-07-18 20:49:58Z gnovelli $
 *
 */

package net.sf.qagesa.agents.services.impl.ref;

/**
 *
 * @author Giovanni Novelli
 */
public class ReFCouple {
    private int computingElementID;
    private int storageElementID;
    
    /** Creates a new instance of ReFCouple */
    public ReFCouple(int computingElementID, int storageElementID) {
        this.setComputingElementID(computingElementID);
        this.setStorageElementID(storageElementID);
    }

    public String toString() {
        return "(" + this.getComputingElementID() + ", " + this.getStorageElementID() + ")";
    }
    
    public boolean equals(Object o) {
        ReFCouple c = (ReFCouple) o;
        return (this.getComputingElementID()==c.getComputingElementID()) && (this.getStorageElementID()==c.getStorageElementID());
    }

    public int getComputingElementID() {
        return computingElementID;
    }

    public void setComputingElementID(int computingElementID) {
        this.computingElementID = computingElementID;
    }

    public int getStorageElementID() {
        return storageElementID;
    }

    public void setStorageElementID(int storageElementID) {
        this.storageElementID = storageElementID;
    }
    
}
