/*
 ****************************************************************************************
 * Copyright © Giovanni Novelli                                             
 * All Rights Reserved.                                                                 
 ****************************************************************************************
 *
 * Title:        GAP Simulator
 * Description:  GAP (Grid Agents Platform) Toolkit for Modeling and Simulation
 *               of Mobile Agents on Grids
 * License:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * AbstractScheduler.java
 *
 * Created on 13 December 2007, 12.00 by Giovanni Novelli
 *
 ****************************************************************************************
 *
 * $Revision$
 * $Id$
 * $HeadURL$
 *
 *****************************************************************************************
 */

package net.sf.gap.agents.gridlets.scheduling;

/**
 *
 * @author Giovanni Novelli
 */
public abstract class AbstractScheduler {
    /**
     * Upper bound for queued gridlets
     */
    private int upperBound;

    public AbstractScheduler() {
        this.setUpperBound(1);
    }

    public AbstractScheduler(int anUpperBound) {
        this.setUpperBound(anUpperBound);
    }

    protected int getUpperBound() {
        return upperBound;
    }

    protected void setUpperBound(int upperBound) {
        this.upperBound = upperBound;
    }
}
