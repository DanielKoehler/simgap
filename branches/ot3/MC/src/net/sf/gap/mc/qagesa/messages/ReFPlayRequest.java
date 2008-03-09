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
 * $Id$
 *
 */

package net.sf.gap.mc.qagesa.messages;

import net.sf.gap.messages.*;
import eduni.simjava.Sim_event;

/**
 * 
 * @author Giovanni Novelli
 */
public class ReFPlayRequest extends Request {
        private Sim_event replyEv;
        private Sim_event fcEv;
        
	// Used to disable ReF metric based selection process
	// ceID and seID selected randomly from available CEs and from SEs
	// containing movieTag
	private boolean randomSelection; // Used in request

	private int userID; // Used in request

	private String movieTag; // Used in request
        
        private TranscodeRequest transcodeRequest;
        
        private double minQuality;

	/**
	 * Creates a new instance of ReFPlayRequest
	 */
	public ReFPlayRequest(int src_ID, int src_resID, int userID,
			String movieTag, double acceptableQualityLoss, boolean randomSelection) {
		super(src_ID, src_resID);
		this.setUserID(userID);
		this.setMovieTag(movieTag);
		this.setRandomSelection(randomSelection);
                this.replyEv=null;
                this.fcEv=null;
                this.setTranscodeRequest(null);
                this.setMinQuality(acceptableQualityLoss);
	}

	public static ReFPlayRequest get_data(Sim_event ev) {
		Message<ReFPlayRequest> message = new Message<ReFPlayRequest>();
		return message.get_data(ev);
	}

	@Override
	public ReFPlayRequest clone() {
		ReFPlayRequest request = new ReFPlayRequest(this.getSrc_ID(), this.getSrc_resID(), this
				.getUserID(), this.getMovieTag(), this.getMinQuality(), this.isRandomSelection());
                request.setFcEv(this.getFcEv());
                request.setReplyEv(replyEv);
                request.setTranscodeRequest(transcodeRequest);
                return request;
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

    public Sim_event getReplyEv() {
        return replyEv;
    }

    public void setReplyEv(Sim_event replyEv) {
        this.replyEv = replyEv;
    }

    public Sim_event getFcEv() {
        return fcEv;
    }

    public void setFcEv(Sim_event fcEv) {
        this.fcEv = fcEv;
    }

    public TranscodeRequest getTranscodeRequest() {
        return transcodeRequest;
    }

    public void setTranscodeRequest(TranscodeRequest transcodeRequest) {
        this.transcodeRequest = transcodeRequest;
    }

    public double getMinQuality() {
        return minQuality;
    }

    public void setMinQuality(double minQuality) {
        this.minQuality = minQuality;
    }
}
