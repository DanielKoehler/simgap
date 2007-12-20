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
 * IJobScheduler.java
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

/**
 * This interface is responsible for exposing a standard interface for any
 * jobs management feature of an agent 
 * 
 * @author Giovanni Novelli
 * 
 * @see gridsim.Gridlet;
 */
public interface IJobScheduler {
    /**
     * This method enques a gridlet in this scheduler. If the implementing class
     * has queue management policies too, then it should return <b>true</b> if
     * the gridlet was successfully enqued. If the gridlet was dropped, or could
     * not be accomodated due to any other reason, it should return 
     * <b>false</b>.
     * 
     * @param gridlet    A gridlet to be enqued by this scheduler.
     * @return <tt>true</tt> if enqued, <tt>false</tt> otherwise
     * @pre gridlet != null
     * @post $none
     */
    public abstract boolean enque(Gridlet gridlet);

    /**
     * Retrieves, but does not remove, a single gridlet and returns it. 
     * This gridlet should be submitted to the Grid Element to which agent 
     * is currently attached
     *
     * @return the gridlet to be submitted
     * @pre $none
     * @post $none
     */
    public abstract Gridlet peek();

    /**
     * Removes a single gridlet and returns it. This gridlet should be submitted
     * to the Grid Element to which agent is currently attached
     *
     * @return the gridlet to be submitted
     * @pre $none
     * @post $none
     */
    public abstract Gridlet deque();

    /**
     * Determines whether the scheduler is currently keeping any gridlets in
     * its queue(s).
     *
     * @return <tt>true</tt> if no gridlets are enqueued, <tt>false</tt>
     *         otherwise
     * @pre $none
     * @post $none
     */
    public abstract boolean isEmpty();

    /**
     * Determines whether the scheduler has reached an upper bound for gridlets 
     * in its queue(s).
     *
     * @return <tt>true</tt> if queue is full, <tt>false</tt>
     *         otherwise
     * @pre $none
     * @post $none
     */
    public abstract boolean isFull();

    /**
     * Determines the number of gridlets that are currently enqueued in this
     * scheduler.
     *
     * @return the number of gridlets enqueud by this scheduler.
     * @pre $none
     * @post $none
     */
    public abstract int size();
}
