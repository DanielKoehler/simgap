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
 * GridElement.java
 *
 * Created on 21 December 2007, 09.00 by Giovanni Novelli
 *
 * $Id$
 *
 */

package net.sf.gap.grid.components;

import eduni.simjava.Sim_event;
import gridsim.ResourceCalendar;
import gridsim.ResourceCharacteristics;
import gridsim.datagrid.ReplicaManager;
import gridsim.net.Link;


/**
 * 
 * @author Giovanni Novelli
 */
public class GridElement extends AbstractGridElement {
	/** Creates a new instance of StorageElement */
	public GridElement(String name, Link link,
			ResourceCharacteristics resourceCharacteristics,
			ResourceCalendar resourceCalendar, ReplicaManager replicaManager)
			throws Exception {
		super(name, link, resourceCharacteristics, resourceCalendar,
				replicaManager);
	}

	@Override
	protected void processCustomEvents(Sim_event ev) {
		switch (ev.get_tag()) {
		default:
			break;
		}
	}
}
