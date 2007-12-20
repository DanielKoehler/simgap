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
 * ReFPlayRequest.java
 *
 * Created on 21 March 2007, 23.03 by Giovanni Novelli
 *
 * $Id: ReFPlayRequest.java 1142 2007-07-18 20:49:58Z gnovelli $
 *
 */

package net.sf.qagesa.messages;

import net.sf.gap.messages.Message;
import net.sf.gap.messages.Request;
import eduni.simjava.Sim_event;

/**
 * 
 * @author Giovanni Novelli
 */
public class ReFPlayRequest extends Request {
        // Used to disable ReF metric based selection process
        // ceID and seID selected randomly from available CEs and from SEs containing movieTag
        private boolean randomSelection; // Used in request

        private int userID; // Used in request
        
	private String movieTag; // Used in request

        /**
	 * Creates a new instance of ReFPlayRequest
	 */
	public ReFPlayRequest(int src_ID, int src_resID, int userID, String movieTag, boolean randomSelection) {
		super(src_ID, src_resID);
                this.setUserID(userID);
		this.setMovieTag(movieTag);
                this.setRandomSelection(randomSelection);
	}

	public static ReFPlayRequest get_data(Sim_event ev) {
		Message<ReFPlayRequest> message = new Message<ReFPlayRequest>();
		return message.get_data(ev);
	}

	@Override
	public ReFPlayRequest clone() {
		return new ReFPlayRequest(this.getSrc_ID(), this.getSrc_resID(), this.getUserID(), this
				.getMovieTag(), this.isRandomSelection());
	}

	public String getMovieTag() {
		return movieTag;
	}

	public void setMovieTag(String movieTag) {
		this.movieTag = movieTag;
	}

    public boolean isRandomSelection() {
        return randomSelection;
    }

    public void setRandomSelection(boolean randomSelection) {
        this.randomSelection = randomSelection;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }
}
