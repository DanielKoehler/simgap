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
 * User.java
 *
 * Created on 13 August 2006, 20.24 by Giovanni Novelli
 *
 * $Id$
 *
 */

package net.sf.gap.users;

import eduni.simjava.Sim_event;
import gridsim.net.Link;
import net.sf.gap.users.AbstractUser;

/**
 * 
 * @author Giovanni Novelli
 */
public abstract class User extends AbstractUser {

	/** Creates a new instance of QAGESAUser */
	public User(String name, Link link, int entityType, boolean trace_flag)
			throws Exception {
		super(name, link, entityType, trace_flag);
	}

	@Override
	public abstract void processOtherEvent(Sim_event ev);
}
