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
 * GISReply.java
 *
 * Created on 25 August 2006, 12.00 by Giovanni Novelli
 *
 ****************************************************************************************
 *
 * $Revision: 1141 $
 * $Id: GISReply.java 1141 2007-07-18 18:22:15Z gnovelli $
 * $HeadURL: file:///var/svn/grid/trunk/GAP/src/net/sf/gap/messages/impl/GISReply.java $
 *
 *****************************************************************************************
 */


package net.sf.gap.messages.impl;

import net.sf.gap.agents.services.impl.gis.GISRepository;
import net.sf.gap.messages.Message;
import net.sf.gap.messages.Reply;
import eduni.simjava.Sim_event;

/**
 * 
 * @author Giovanni Novelli
 */
public class GISReply extends Reply {

	/**
	 * Creates a new instance of GISReply
	 */
	public GISReply(int requestTAG, boolean ok, GISRequest request,
			GISRepository gapgis) {
		super(requestTAG, ok, request);
		this.setGIS(gapgis);
	}

	public GISRepository getGIS() {
		return this.getRequest().getGIS();
	}

	public void setGIS(GISRepository gis) {
		this.getRequest().setGIS(gis);
	}

	@Override
	public GISReply clone() {
		return new GISReply(this.getRequestTAG(), this.isOk(), this
				.getRequest().clone(), this.getGIS());
	}

	public static GISReply get_data(Sim_event ev) {
		Message<GISReply> message = new Message<GISReply>();
		return message.get_data(ev);
	}

	@Override
	public GISRequest getRequest() {
		return (GISRequest) this.request;
	}

	public void setRequest(GISRequest request) {
		this.request = request;
	}
}
