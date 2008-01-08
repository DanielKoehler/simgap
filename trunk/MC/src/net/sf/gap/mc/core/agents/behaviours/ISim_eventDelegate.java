/*
 ****************************************************************************************
 * Copyright © Giovanni Novelli                                             
 * All Rights Reserved.                                                                 
 ****************************************************************************************
 *
 * License:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * ISim_eventDelegate.java
 *
 * Created on 8 January 2008, 09.28 by Giovanni Novelli
 *
 * $Id$
 *
 */


package net.sf.gap.mc.core.agents.behaviours;

/**
 *
 * @author Giovanni Novelli
 */
public interface ISim_eventDelegate {
    public Sim_eventListener addListener(int tag, Sim_eventListener evListener);
    public Sim_eventListener removeListener(int tag);
}