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
import gridsim.GridletList;
import net.sf.gap.agents.GridAgent;
import net.sf.gap.agents.gridlets.Gridlets;

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
     * Gridlets tracking
     */
    private Gridlets gridlets;

    public AbstractScheduler(GridAgent anAgent) {
        this.setUpperBound(1);
        this.setAgent(anAgent);
        this.setGridlets(new Gridlets());
    }

    public AbstractScheduler(
            GridAgent anAgent,
            int anUpperBound) {
        this.setUpperBound(anUpperBound);
        this.setAgent(anAgent);
        this.setGridlets(new Gridlets());
    }

    protected int getUpperBound() {
        return upperBound;
    }

    protected void setUpperBound(int upperBound) {
        this.upperBound = upperBound;
    }

    /**
     * This method is responsible to check if there are gridlets submitted by
     * agent on Grid Element
     * 
     * @return <tt>true</tt> if there are agent's gridlets on Grid Element, <tt>false</tt>
     *         otherwise
     * @pre $none
     * @post $none
     */
    public boolean hasGridlets() {
        return !this.isEmpty() || (!this.getCurrentList().isEmpty());
    }

    /**
     * This method is responsible for gridlets submission to Grid Element
     * 
     * @return <tt>true</tt> if gridlet has been successfully submitted,
     *         <tt>false</tt> otherwise
     * @pre $none
     * @post $none
     */
    @SuppressWarnings("unchecked")
    public boolean gridletSubmit() {
        // @TODO Add another method for 1+ concurrent gridlets by an agent
        // if there is NOT any gridlet on the Grid Element
        if (this.getCurrentList().isEmpty()) {
            // if there are gridlets on local agent's queue
            if (!this.isEmpty()) {
                // Peeks the gridlet on the head of agent's scheduling queue
                Gridlet gridlet = this.peek();
                // Submits the gridlet to agent's rid Element
                boolean submitted = this.getAgent().gridletSubmit(gridlet);
                // If the gridlet has been successfully submitted
                if (submitted) {
                    // Removes the gridlet from the head of agent's
                    // scheduling queue
                    this.getCurrentList().add(this.deque());
                    // Add the gridlet to a list used for tracking
                    // submitted gridlets
                    this.getGridlets().addSubmitted(gridlet);
                }
                return submitted;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

        /**
         * Cancels a Gridlet on the Grid Element of this agent 
         * without any delay. An acknowledgement to denote the success 
         * of this method is by default off or false
         * 
         * @param gl the gridlet to be canceled
         * @return canceled gridlet or null otherwise
         */
    public Gridlet gridletCancel(Gridlet gl) {
        // If the gridlet has been previously submitted and has yet NOT returned
        if (this.getGridlets().getGridletSubmitted().contains(gl)) {
            Gridlet canceledGridlet = this.gridletCancel(gl);
            if (canceledGridlet.getGridletStatus()==Gridlet.CANCELED) {
                this.getGridlets().addCanceled(canceledGridlet);
                return canceledGridlet;
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

        /**
         * Pauses a Gridlet on the Grid Element of this agent 
         * without any delay. An acknowledgement to denote the success 
         * of this method is by default off or false
         * 
         * @param gl the gridlet to be paused
         * @return paused gridlet or null otherwise
         */
    public Gridlet gridletPause(Gridlet gl) {
        // If the gridlet has been previously submitted and has yet NOT returned
        if (this.getGridlets().getGridletSubmitted().contains(gl)) {
            Gridlet pausedGridlet = this.gridletPause(gl);
            if (pausedGridlet.getGridletStatus()==Gridlet.PAUSED) {
                this.getGridlets().addPaused(pausedGridlet);
                return pausedGridlet;
            } else {
                return null;
            }
        } else {
            return null;
        }
    }
    
        /**
         * Resumes a Gridlet on the Grid Element of this agent 
         * without any delay. An acknowledgement to denote the success 
         * of this method is by default off or false
         * 
         * @param gl the gridlet to be resumed
         * @return resumed gridlet or null otherwise
         */
    public Gridlet gridletResume(Gridlet gl) {
        // If the gridlet has been previously submitted and has yet NOT returned
        if (this.getGridlets().getGridletSubmitted().contains(gl)) {
            Gridlet resumedGridlet = this.gridletResume(gl);
            if (resumedGridlet.getGridletStatus()==Gridlet.RESUMED) {
                this.getGridlets().getGridletPaused().remove(gl);
                return resumedGridlet;
            } else {
                return null;
            }
        } else {
            return null;
        }
    }
    
    // @TODO Add MOVE gridlet
    
    /**
     * This method is responsible for syncronously receiving 
     * a gridlet back from Grid Element
     * 
     * @return the gridlet received by Grid Element or null
     */
    public Gridlet gridletReceive() {
        // If there is at least a gridlet on agent's Grid Element
        if (!this.getCurrentList().isEmpty()) {
            // gets the gridlet back from agent's Grid Element
            Gridlet receivedGridlet = this.getAgent().gridletReceive();
            return this.gridletReceive(receivedGridlet);
        } else {
            return null;
        }
    }

    /**
     * This method is responsible for managing 
     * a gridlet received in some way (sync/asyng) back from Grid Element
     * 
     * @return the gridlet received by Grid Element or null
     */
    public Gridlet gridletReceive(Gridlet receivedGridlet) {
        // if the received gridlet is NOT null
        if (receivedGridlet != null) {
            // Extract receveid gridlet's ID
            int gridletID = receivedGridlet.getGridletID();
            // Gets original gridlet from its ID
            Gridlet gridlet = this.getGridlets().getMapGR().get(gridletID).getGridlet();
            // If the gridlet extracted from gridlets tracking field 
            // is NOT null
            if (gridlet != null) {
                // Checks that the extracted gridlet is really on
                // current gridlets list
                if (this.getCurrentList().contains(gridlet)) {
                    // Removes the gridlet from such list
                    this.getCurrentList().remove(gridlet);
                    // Tracks the received gridlet in the correct list
                    // based on its return status
                    switch (receivedGridlet.getGridletStatus()) {
                        case Gridlet.SUCCESS:
                            this.getGridlets().addSuccesses(receivedGridlet);
                            break;
                        case Gridlet.FAILED:
                        case Gridlet.FAILED_RESOURCE_UNAVAILABLE:
                            this.getGridlets().addFailures(receivedGridlet);
                            break;
                        default:
                            break;
                        }
                }
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

    public GridletList getCurrentList() {
        return this.getGridlets().getGridletSubmitted();
    }

    public Gridlets getGridlets() {
        return gridlets;
    }

    public void setGridlets(Gridlets gridlets) {
        this.gridlets = gridlets;
    }
}
