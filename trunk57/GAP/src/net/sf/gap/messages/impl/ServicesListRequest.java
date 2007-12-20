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
 * ServicesListRequest.java
 *
 * Created on 16 March 2007, 12.00 by Giovanni Novelli
 *
 ****************************************************************************************
 *
 * $Revision: 1141 $
 * $Id: ServicesListRequest.java 1141 2007-07-18 18:22:15Z gnovelli $
 * $HeadURL: file:///var/svn/grid/trunk/GAP/src/net/sf/gap/messages/impl/ServicesListRequest.java $
 *
 *****************************************************************************************
 */


package net.sf.gap.messages.impl;

import net.sf.gap.agents.services.ServicesList;
import eduni.simjava.Sim_event;
import net.sf.gap.messages.Message;
import net.sf.gap.messages.Request;

/**
 * 
 * @author Giovanni Novelli
 */
public class ServicesListRequest extends Request {

	private ServicesList servicesList; // Used in reply

	/**
	 * Creates a new instance of ServicesListRequest
	 */
	public ServicesListRequest(int src_ID, int src_resID) {
		super(src_ID, src_resID);
	}

	public static ServicesListRequest get_data(Sim_event ev) {
		Message<ServicesListRequest> message = new Message<ServicesListRequest>();
		return message.get_data(ev);
	}

	@Override
	public ServicesListRequest clone() {
		return new ServicesListRequest(this.getSrc_ID(), this.getSrc_resID());
	}

	public ServicesList getServicesList() {
		return this.servicesList;
	}

	public void setServicesList(ServicesList servicesList) {
		this.servicesList = servicesList;
	}
}
