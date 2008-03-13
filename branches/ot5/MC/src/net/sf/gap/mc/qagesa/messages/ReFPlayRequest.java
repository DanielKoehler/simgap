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
        
        private double requestTime; // request time user side
        private double replyTime;   // request reply time user side
        private double fcTime;      // first chunk time user side
        private double lcTime;      // last chunk time user side
        private double endTime;     // request completion time user side
        
        private long fetchedBytes;  // bytes fetched from SE to CE considering caching
        private long streamedBytes; // bytes streamed to user considering QoS adaptation

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
                
                this.setRequestTime(-1.0);
                this.setReplyTime(-1.0);
                this.setFcTime(-1.0);
                this.setLcTime(-1.0);
                this.setEndTime(-1.0);
                
                this.setFetchedBytes(-1);
                this.setStreamedBytes(-1);
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
                
                request.setRequestTime(this.getRequestTime());
                request.setReplyTime(this.getReplyTime());
                request.setFcTime(this.getFcTime());
                request.setLcTime(this.getLcTime());
                request.setEndTime(this.getEndTime());
                
                request.setFetchedBytes(this.getFetchedBytes());
                request.setStreamedBytes(this.getStreamedBytes());
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

    public double getRequestTime() {
        return requestTime;
    }

    public void setRequestTime(double requestTime) {
        this.requestTime = requestTime;
    }

    public double getReplyTime() {
        return replyTime;
    }

    public void setReplyTime(double replyTime) {
        this.replyTime = replyTime;
    }

    public double getEndTime() {
        return endTime;
    }

    public void setEndTime(double endTime) {
        this.endTime = endTime;
    }

    public double getFcTime() {
        return fcTime;
    }

    public void setFcTime(double fcTime) {
        this.fcTime = fcTime;
    }

    public double getLcTime() {
        return lcTime;
    }

    public void setLcTime(double lcTime) {
        this.lcTime = lcTime;
    }

    public long getFetchedBytes() {
        return fetchedBytes;
    }

    public void setFetchedBytes(long fetchedBytes) {
        this.fetchedBytes = fetchedBytes;
    }

    public long getStreamedBytes() {
        return streamedBytes;
    }

    public void setStreamedBytes(long streamedBytes) {
        this.streamedBytes = streamedBytes;
    }
    
    public long incStreamedBytes(long inc) {
        return this.streamedBytes += inc;
    }

    public long incFetchedBytes(long inc) {
        return this.fetchedBytes += inc;
    }
}
