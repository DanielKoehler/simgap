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
 * NetworkMap.java
 *
 * Created on 25 August 2006, 12.00 by Giovanni Novelli
 *
 ****************************************************************************************
 *
 * $Revision: 1141 $
 * $Id: NetworkMap.java 1141 2007-07-18 18:22:15Z gnovelli $
 * $HeadURL: file:///var/svn/grid/trunk/GAP/src/net/sf/gap/agents/services/impl/nm/NetworkMap.java $
 *
 *****************************************************************************************
 */

package net.sf.gap.agents.services.impl.nm;

import gridsim.net.InfoPacket;

import java.util.HashMap;
import java.util.Iterator;

/**
 * 
 * @author Giovanni Novelli
 */
public class NetworkMap extends HashMap<Integer, RTTMap> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2933290656074228894L;

	private double lastRequestTime;

	private void registerRTTMap(Integer geid) {
		if (!this.containsKey(geid)) {
			RTTMap rttMap = new RTTMap();
			this.put(geid, rttMap);
		}
	}

	public void addRTT(Integer src, Integer dst, InfoPacket pkt) {
		this.registerRTTMap(src);
		this.get(src).put(dst, pkt);
	}

	public void show() {
		Iterator<Integer> it = this.keySet().iterator();
		while (it.hasNext()) {
			Integer geid = it.next();
			Iterator<Integer> it2 = this.get(geid).keySet().iterator();
			System.out.println("GE id = " + geid);
			while (it2.hasNext()) {
				Integer dstid = it2.next();
				InfoPacket pkt = this.get(geid).get(dstid);
				System.out.println(geid + " --> " + dstid + "\n" + pkt);
				if (it2.hasNext()) {
					System.out.println();
				}
			}
			if (it.hasNext()) {
				System.out.println();
			}
		}
	}

	public double getLastRequestTime() {
		return lastRequestTime;
	}

	public void setLastRequestTime(double lastRequestTime) {
		this.lastRequestTime = lastRequestTime;
	}
}
