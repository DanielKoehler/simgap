/*
 ****************************************************************************************
 * Copyright © Giovanni Novelli                                             
 * All Rights Reserved.                                                                 
 ****************************************************************************************
 *
 * License:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * COREGridElement.java
 *
 * Created on 21 December 2007, 09.00 by Giovanni Novelli
 *
 * $Id$
 *
 */

package net.sf.gap.mc.core.grid.components;

import eduni.simjava.Sim_event;

import gridsim.ResourceCalendar;
import gridsim.ResourceCharacteristics;
import gridsim.datagrid.ReplicaManager;
import gridsim.net.Link;

import net.sf.gap.grid.components.GridElement;


/**
 * 
 * @author Giovanni Novelli
 */
public class COREGridElement extends GridElement {
	/** Creates a new instance of StorageElement */
	public COREGridElement(String name, Link link,
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
