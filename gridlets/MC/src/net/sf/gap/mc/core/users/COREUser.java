/*
 ****************************************************************************************
 * Copyright © Giovanni Novelli                                             
 * All Rights Reserved.                                                                 
 ****************************************************************************************
 *
 * Title:        QAGESA Simulator 
 * Description:  QAGESA (QoS Aware Grid Enabled Streaming Architecture) Simulator
 *               of a QoS-Aware Architecture for Multimedia Content Provisioning in a GRID Environment
 * License:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * QAGESAUser.java
 *
 * Created on 13 August 2006, 20.24 by Giovanni Novelli
 *
 * $Id$
 *
 */

package net.sf.gap.mc.core.users;

import eduni.simjava.Sim_event;
import gridsim.net.Link;
import net.sf.gap.users.AbstractUser;

/**
 * 
 * @author Giovanni Novelli
 */
public abstract class COREUser extends AbstractUser {

	/** Creates a new instance of QAGESAUser */
	public COREUser(String name, Link link, int entityType, boolean trace_flag)
			throws Exception {
		super(name, link, entityType, trace_flag);
	}

	@Override
	public abstract void processOtherEvent(Sim_event ev);
}
