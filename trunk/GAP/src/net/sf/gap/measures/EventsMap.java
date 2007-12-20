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
 * EventsMap.java
 *
 * Created on 29 March 2007, 12.00 by Giovanni Novelli
 *
 ****************************************************************************************
 *
 * $Revision: 1141 $
 * $Id: EventsMap.java 1141 2007-07-18 18:22:15Z gnovelli $
 * $HeadURL: file:///var/svn/grid/trunk/GAP/src/net/sf/gap/measures/EventsMap.java $
 *
 *****************************************************************************************
 */

package net.sf.gap.measures;

import java.util.concurrent.ConcurrentHashMap;
import net.sf.gap.messages.MeasureRequest;

/**
 *
 * @author Giovanni Novelli
 */
public class EventsMap extends ConcurrentHashMap<Integer, MeasureRequest> {
}
