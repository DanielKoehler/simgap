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
 * MeasureReply.java
 *
 * Created on 29 March 2007, 12.00 by Giovanni Novelli
 *
 ****************************************************************************************
 *
 * $Revision: 1141 $
 * $Id: MeasureReply.java 1141 2007-07-18 18:22:15Z gnovelli $
 * $HeadURL: file:///var/svn/grid/trunk/GAP/src/net/sf/gap/messages/MeasureReply.java $
 *
 *****************************************************************************************
 */


package net.sf.gap.messages;

import net.sf.gap.messages.Message;
import net.sf.gap.messages.Reply;
import eduni.simjava.Sim_event;

/**
 * 
 * @author Giovanni Novelli
 */
public class MeasureReply extends Reply {

	/**
     * Creates a new instance of MeasureRequest
     */
	public MeasureReply(int requestTAG, boolean ok, MeasureRequest request) {
		super(requestTAG, ok, request);
	}

	@Override
	public MeasureReply clone() {
		return new MeasureReply(this.getRequestTAG(), this.isOk(), this
				.getRequest().clone());
	}

	public static MeasureReply get_data(Sim_event ev) {
		Message<MeasureReply> message = new Message<MeasureReply>();
		return message.get_data(ev);
	}

	@Override
	public MeasureRequest getRequest() {
		return (MeasureRequest) this.request;
	}

	public void setRequest(MeasureRequest request) {
		this.request = request;
	}
        
        public int getID() {
            return this.getRequest().getID();
        }
}
