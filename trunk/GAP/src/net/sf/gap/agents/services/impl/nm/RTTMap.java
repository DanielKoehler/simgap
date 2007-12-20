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
 * RTTMap.java
 *
 * Created on 25 August 2006, 12.00 by Giovanni Novelli
 *
 ****************************************************************************************
 *
 * $Revision: 1141 $
 * $Id: RTTMap.java 1141 2007-07-18 18:22:15Z gnovelli $
 * $HeadURL: file:///var/svn/grid/trunk/GAP/src/net/sf/gap/agents/services/impl/nm/RTTMap.java $
 *
 *****************************************************************************************
 */

package net.sf.gap.agents.services.impl.nm;

import gridsim.net.InfoPacket;

import java.util.HashMap;

/**
 * 
 * @author admin
 */
public class RTTMap extends HashMap<Integer, InfoPacket> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2437318028250188286L;

	/** Creates a new instance of RTTMap */
	public RTTMap() {
	}

}
