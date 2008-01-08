/*
 ****************************************************************************************
 * Copyright © Giovanni Novelli                                             
 * All Rights Reserved.                                                                 
 ****************************************************************************************
 *
 * License:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * User.java
 *
 * Created on 8 January 2008, 12:45 by Giovanni Novelli
 *
 * $Id$
 *
 */

package net.sf.gap.mc.experiments.users;

import eduni.simjava.Sim_event;

import gridsim.net.Link;

import net.sf.gap.mc.core.users.COREUser;

import net.sf.gap.mc.experiments.constants.ExperimentsEntityTypes;

/**
 * @author Giovanni Novelli
 */
public class User extends COREUser {
	/**
	 * 
	 * Creates a new instance of Submitter
	 * 
	 * @param name
	 *            AgentMiddleware entity name
	 * @param trace_flag
	 *            GridSim trace flag
	 * @throws Exception
	 *             This happens when name is null or haven't initialized
	 *             GridSim.
	 */
	public User(String name, Link link, boolean trace_flag)
			throws Exception {
		super(name, link, ExperimentsEntityTypes.USER_USER, trace_flag);
	}

        public void printStats() {
        }
        
        @Override
	public void processOtherEvent(Sim_event ev) {
	}

        @Override
	public void initWork() {
		this.DoIt();
	}

	private void DoIt() {
	}
}
