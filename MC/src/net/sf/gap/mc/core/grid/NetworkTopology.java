/*
 ****************************************************************************************
 * Copyright © Giovanni Novelli                                             
 * All Rights Reserved.                                                                 
 ****************************************************************************************
 *
 * License:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * NetworkTopology.java
 *
 * Created on 7 August 2006, 21.38 by Giovanni Novelli
 *
 * $Id: NetworkTopology.java 1142 2007-07-18 20:49:58Z gnovelli $
 *
 */

package net.sf.gap.mc.core.grid;

import gridsim.net.RIPRouter;

import java.util.Vector;

/**
 * 
 * @author Giovanni Novelli
 */
public abstract class NetworkTopology extends Vector<RIPRouter> {

	/** Creates a new instance of NetworkTopology */
	public NetworkTopology() {
	}

	public abstract void create(boolean trace) throws Exception;

	public void setRouters(Vector<RIPRouter> routers) {
		this.addAll(routers);
	}
}
