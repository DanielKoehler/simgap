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
 * MuMSearchRequest.java
 *
 * Created on 16 March 2007, 16.16 by Giovanni Novelli
 *
 * $Id: MuMSearchRequest.java 1142 2007-07-18 20:49:58Z gnovelli $
 *
 */

package net.sf.qagesa.messages;

import eduni.simjava.Sim_event;

import net.sf.gap.messages.Message;
import net.sf.gap.messages.Request;

import net.sf.qagesa.agents.services.impl.mum.GEList;

/**
 * 
 * @author Giovanni Novelli
 */
public class MuMSearchRequest extends Request {

	private String movieTag; // Used in request

	private GEList gelist; // Used in reply

	/**
	 * Creates a new instance of MuMSearchRequest
	 */
	public MuMSearchRequest(int src_ID, int src_resID, String movieTag) {
		super(src_ID, src_resID);
		this.setMovieTag(movieTag);
	}

	public static MuMSearchRequest get_data(Sim_event ev) {
		Message<MuMSearchRequest> message = new Message<MuMSearchRequest>();
		return message.get_data(ev);
	}

	@Override
	public MuMSearchRequest clone() {
		return new MuMSearchRequest(this.getSrc_ID(), this.getSrc_resID(), this
				.getMovieTag());
	}

	public String getMovieTag() {
		return movieTag;
	}

	public void setMovieTag(String movieTag) {
		this.movieTag = movieTag;
	}

	public GEList getGelist() {
		return gelist;
	}

	public void setGelist(GEList gelist) {
		this.gelist = gelist;
	}
}
