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
 * MeasureRequest.java
 *
 * Created on 29 March 2007, 12.00 by Giovanni Novelli
 *
 ****************************************************************************************
 *
 * $Revision: 1141 $
 * $Id: MeasureRequest.java 1141 2007-07-18 18:22:15Z gnovelli $
 * $HeadURL: file:///var/svn/grid/trunk/GAP/src/net/sf/gap/messages/MeasureRequest.java $
 *
 *****************************************************************************************
 */

package net.sf.gap.messages;

import net.sf.gap.messages.Message;
import net.sf.gap.messages.Request;
import eduni.simjava.Sim_event;

/**
 * 
 * @author Giovanni Novelli
 */
public class MeasureRequest extends Request {
     private int ID;
     
     private double timestamp;
     
     private Sim_event event;
     
     /**
     * Creates a new instance of MeasureRequest
     */
	public MeasureRequest(int src_ID, int ID, double timestamp, Sim_event event) {
		super(src_ID, ID);
                this.setTimestamp(timestamp);
                this.setEvent(event);
	}

	public static MeasureRequest get_data(Sim_event ev) {
		Message<MeasureRequest> message = new Message<MeasureRequest>();
		return message.get_data(ev);
	}

	@Override
	public MeasureRequest clone() {
		return new MeasureRequest(this.getSrc_ID(), this.getID(), this.getTimestamp(), this.getEvent());
	}

    public int getID() {
        return this.getSrc_resID();
    }

    public Sim_event getEvent() {
        return event;
    }

    public void setEvent(Sim_event event) {
        this.event = event;
    }

    public double getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(double timestamp) {
        this.timestamp = timestamp;
    }
}
