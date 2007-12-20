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
 * NetworkTopology.java
 *
 * Created on 7 August 2006, 21.38 by Giovanni Novelli
 *
 * $Id: NetworkTopology.java 1142 2007-07-18 20:49:58Z gnovelli $
 *
 */

package net.sf.qagesa.grid;

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
