/*
 ****************************************************************************************
 * Copyright © Giovanni Novelli                                             
 * All Rights Reserved.                                                                 
 ****************************************************************************************
 *
 * License:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * Experiment.java
 *
 * Created on 10 January 2008, 11.00 by Giovanni Novelli
 *
 * $Id$
 *
 */

package net.sf.gap.mc.experiments.simulation.specs;

/**
 *
 * @author giovanni
 */
public class Experiment {
    private int ID;
    public Experiment(int ID) {
        this.setID(ID);
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }
}
