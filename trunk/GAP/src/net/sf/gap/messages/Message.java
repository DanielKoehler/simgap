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
 * Message.java
 *
 * Created on 29 March 2007, 12.00 by Giovanni Novelli
 *
 ****************************************************************************************
 *
 * $Revision: 1141 $
 * $Id: Message.java 1141 2007-07-18 18:22:15Z gnovelli $
 * $HeadURL: file:///var/svn/grid/trunk/GAP/src/net/sf/gap/messages/Message.java $
 *
 *****************************************************************************************
 */


package net.sf.gap.messages;

import eduni.simjava.Sim_event;
import gridsim.IO_data;

/**
 * 
 * @author admin
 */
public class Message<T> {
	private T eventContent;

	@SuppressWarnings("unchecked")
	public T get_data(Sim_event ev) {
		try {

			this.setEventContent((T) ev.get_data());
		} catch (Exception e) {
			this.setEventContent((T) ((IO_data) ev.get_data()).getData());
		}
		return this.getEventContent();
	}

	public T getEventContent() {
		return eventContent;
	}

	public void setEventContent(T eventContent) {
		this.eventContent = eventContent;
	}
}
