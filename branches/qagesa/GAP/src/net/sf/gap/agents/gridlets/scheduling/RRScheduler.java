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

import java.util.concurrent.LinkedBlockingQueue;
import gridsim.Gridlet;
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
        this.setQueue(new LinkedBlockingQueue<Gridlet>(this.getUpperBound()));
    }
    
    public RRScheduler(
            GridAgent anAgent, 
            int anUpperBound) {
        super(anAgent,anUpperBound);
        this.setQueue(new LinkedBlockingQueue<Gridlet>(this.getUpperBound()));
    }
    
    public boolean enque(Gridlet gridlet) 
    {
        return this.getQueue().add(gridlet);
    }
    
    public Gridlet deque()
    {
        return this.getQueue().poll();
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
