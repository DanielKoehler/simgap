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
 * RRScheduler.java
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

import gridsim.Gridlet;

import java.util.concurrent.LinkedBlockingQueue;

import net.sf.gap.agents.GridAgent;


/**
 * This class is responsible to implement a Round Robin scheduling of gridlets
 * managed by an agent
 * 
 * @author Giovanni Novelli
 */
public class RRScheduler extends AbstractScheduler {
    private LinkedBlockingQueue<Gridlet> queue;

    
    public RRScheduler(GridAgent anAgent) {
        super(anAgent);
        this.setInputSize(0);
        this.setOutputSize(0);
        this.setQueue(new LinkedBlockingQueue<Gridlet>(this.getUpperBound()));
    }
    
    public RRScheduler(
            GridAgent anAgent, 
            int anUpperBound) {
        super(anAgent,anUpperBound);
        this.setInputSize(0);
        this.setOutputSize(0);
        this.setQueue(new LinkedBlockingQueue<Gridlet>(this.getUpperBound()));
    }
    
    public boolean enque(Gridlet gridlet) 
    {
        boolean result = this.getQueue().add(gridlet);
        if (result) {
            this.incMIPS(gridlet.getGridletLength());
            this.incInputSize(gridlet.getGridletFileSize());
            this.incOutputSize(gridlet.getGridletOutputSize());
        }
        return result;
    }
    
    public Gridlet deque()
    {
        Gridlet gridlet = this.getQueue().poll();
        if (gridlet!=null) {
            this.decMIPS(gridlet.getGridletLength());
            this.decInputSize(gridlet.getGridletFileSize());
            this.decOutputSize(gridlet.getGridletOutputSize());
        }
        return gridlet;
    }
    
    public Gridlet peek()
    {
        return this.getQueue().peek();
    }
    
    public boolean isEmpty()
    {
        return this.getQueue().isEmpty();
    }

    public boolean isFull()
    {
        return this.getQueue().remainingCapacity()==0;
    }
    
    public int size()
    {
        return this.getQueue().size();
    }

    private LinkedBlockingQueue<Gridlet> getQueue() {
        return queue;
    }

    private void setQueue(LinkedBlockingQueue<Gridlet> queue) {
        this.queue = queue;
    }
}
