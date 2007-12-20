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
 * TranscodeRequest.java
 *
 * Created on 23 March 2007, 15.06 by Giovanni Novelli
 *
 * $Id: TranscodeRequest.java 1142 2007-07-18 20:49:58Z gnovelli $
 *
 */

package net.sf.qagesa.messages;

import net.sf.gap.messages.Message;
import net.sf.gap.messages.Request;
import eduni.simjava.Sim_event;
import net.sf.qagesa.multimedia.ChunksSequence;

/**
 * 
 * @author Giovanni Novelli
 */
public class TranscodeRequest extends Request {
        private ReFPlayRequest playRequest;
    
	// Entity ID of SE from which TranscodingAgent will get chunks to transcode
	private int storageElementID; // Used in request

        private ChunksSequence sequence; // Used in request
        
        private int agentID;            // Used in reply

	/**
	 * Creates a new instance of TranscodeRequest
	 */
	public TranscodeRequest(int src_ID, int src_resID, 
                        ReFPlayRequest playRequest, int storageElementID) {
		super(src_ID, src_resID);
                this.setPlayRequest(playRequest);
		this.setStorageElementID(storageElementID);
	}

	public static TranscodeRequest get_data(Sim_event ev) {
		Message<TranscodeRequest> message = new Message<TranscodeRequest>();
		return message.get_data(ev);
	}

	@Override
	public TranscodeRequest clone() {
		return new TranscodeRequest(this.getSrc_ID(), this.getSrc_resID(), 
                        this.getPlayRequest(), this.getStorageElementID());
	}

	public ChunksSequence getSequence() {
		return sequence;
	}

	public void setSequence(ChunksSequence sequence) {
		this.sequence = sequence;
	}

	public String getMovieTag() {
		return this.getPlayRequest().getMovieTag();
	}

	public int getStorageElementID() {
		return storageElementID;
	}

	public void setStorageElementID(int storageElementID) {
		this.storageElementID = storageElementID;
	}

	public int getUserID() {
		return this.getPlayRequest().getUserID();
	}

    public int getPlayReqrepID() {
        return this.getPlayRequest().getReqrepID();
    }

    public ReFPlayRequest getPlayRequest() {
        return playRequest;
    }

    public void setPlayRequest(ReFPlayRequest playRequest) {
        this.playRequest = playRequest;
    }

    public int getAgentID() {
        return agentID;
    }

    public void setAgentID(int agentID) {
        this.agentID = agentID;
    }
}