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

import gridsim.Gridlet;
import net.sf.gap.agents.GridAgent;

/**
 *
 * @author Giovanni Novelli
 */
public abstract class AbstractScheduler implements IJobScheduler {
    /**
     * GridAgent using this scheduler
     */
    private GridAgent agent;
    
    /**
     * Upper bound for queued gridlets
     */
    private int upperBound;
    
    /**
     * Current gridlet on Grid Element
     */
    private Gridlet current;
    
    public AbstractScheduler(GridAgent anAgent) {
        this.setUpperBound(1);
        this.setAgent(anAgent);
    }

    public AbstractScheduler(
            GridAgent anAgent, 
            int anUpperBound) {
        this.setUpperBound(anUpperBound);
        this.setAgent(anAgent);
    }

    protected int getUpperBound() {
        return upperBound;
    }

    protected void setUpperBound(int upperBound) {
        this.upperBound = upperBound;
    }
    
    public boolean hasGridlets() 
    {
        return !this.isEmpty() || (this.getCurrent()!=null);
    }

    public Gridlet getCurrent() {
        return current;
    }

    public void setCurrent(Gridlet current) {
        this.current = current;
    }
    
    public boolean gridletSubmit() 
    {
      // if there is NOT any gridlet on the Grid Element
      if (this.getCurrent()==null) {
          // if there are gridlets on local agent's queue
          if (!this.isEmpty()) {
              Gridlet gridlet = this.peek();
              boolean submitted = this.getAgent().gridletSubmit(gridlet);
              if (submitted) {
                  this.setCurrent(this.deque());
              }
              return submitted;
          } else {
              return false;
          }
      } else {
          return false;
      }
    }
    
    public Gridlet gridletReceive() 
    {
      // if there is a gridlet on the Grid Element
      if (this.getCurrent()!=null) {
          Gridlet receivedGridlet = this.getAgent().gridletReceive();
          if (receivedGridlet!=null) {
              this.setCurrent(null);
          }
          return receivedGridlet;
      } else {
          return null;
      }
    }
    
    public abstract boolean enque(Gridlet gridlet);
    public abstract Gridlet deque();
    public abstract Gridlet peek();
    public abstract boolean isEmpty();
    public abstract boolean isFull();
    public abstract int size();

    public GridAgent getAgent() {
        return agent;
    }

    public void setAgent(GridAgent agent) {
        this.agent = agent;
    }
}
