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
 * AgentHistory.java
 *
 * Created on 20 August 2006, 16.00 by Giovanni Novelli
 *
 ****************************************************************************************
 *
 * $Revision: 1141 $
 * $Id: AgentHistory.java 1141 2007-07-18 18:22:15Z gnovelli $
 * $HeadURL: file:///var/svn/grid/trunk/GAP/src/net/sf/gap/agents/history/AgentHistory.java $
 *
 *****************************************************************************************
 */

package net.sf.gap.agents.history;

import java.util.LinkedList;

/**
 * AgentHistory is a LinkedList that contains a list of entries about agents'
 * lifecycle
 * 
 * @author Giovanni Novelli
 * @see net.p2pgrid.gap.agents.history.AgentHistoryEntry
 */
public class AgentHistory extends LinkedList<AgentHistoryEntry> {
	private static final long serialVersionUID = -3239225485248569470L;
}
