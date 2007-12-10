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
 * LinkFactory.java
 *
 * Created on 7 August 2006, 14.22 by Giovanni Novelli
 *
 * $Id: LinkFactory.java 1142 2007-07-18 20:49:58Z gnovelli $
 *
 */

package net.sf.qagesa.factories;

import gridsim.net.Link;
import gridsim.net.SimpleLink;

/**
 * 
 * @author Giovanni Novelli
 */
public class LinkFactory {
	private static int MTU = Link.DEFAULT_MTU;

	private static int counter;

	/** Creates a new instance of LinkFactory */
	public LinkFactory() {
	}

	static {
		counter = 0;
	}

	public static Link randomLink(double delay) throws Exception {
		double bps=1000000000.0;
		Link link;
		link = new SimpleLink("link_" + LinkFactory.incCounter(), bps, delay,
				LinkFactory.getMTU());
		return link;
	}

	public static Link UserLink(double bps, double delay) throws Exception {
		Link link = new SimpleLink("agentlink_" + LinkFactory.incCounter(),
				bps, delay, LinkFactory.getMTU());
		return link;
	}

	private static Link CELink() throws Exception {
		double bps;
                bps = 1000000000;
		double delay = 2.0;
		Link link = new SimpleLink("celink_" + LinkFactory.incCounter(), bps,
				delay, LinkFactory.getMTU());
		return link;
	}

	private static Link SELink() throws Exception {
		double bps;
                bps = 10000000000.0;
		double delay = 2.0;
		Link link = new SimpleLink("selink_" + LinkFactory.incCounter(), bps,
				delay, LinkFactory.getMTU());
		return link;
	}

	public static Link GELink(boolean isSE) throws Exception {
		if (isSE) {
			return LinkFactory.SELink();
		} else {
			return LinkFactory.CELink();
		}
	}

	public static int getCounter() {
		return counter;
	}

	public static void setCounter(int aCounter) {
		counter = aCounter;
	}

	public static int incCounter() {
		LinkFactory.setCounter(LinkFactory.getCounter() + 1);
		return LinkFactory.getCounter();
	}

	public static int getMTU() {
		return LinkFactory.MTU;
	}

	public static void setMTU(int MTU) {
		LinkFactory.MTU = MTU;
	}
}
