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
 * ChunkReply.java
 *
 * Created on 19 March 2007, 21.48 by Giovanni Novelli
 *
 * $Id: ChunkReply.java 1142 2007-07-18 20:49:58Z gnovelli $
 *
 */

package net.sf.qagesa.messages;

import net.sf.gap.messages.Message;
import net.sf.gap.messages.Reply;
import eduni.simjava.Sim_event;
import net.sf.qagesa.multimedia.Chunk;

/**
 * 
 * @author Giovanni Novelli
 */
public class ChunkReply extends Reply {

	/**
	 * Creates a new instance of ChunkRequest
	 */
	public ChunkReply(int requestTAG, boolean ok, ChunkRequest request,
			Chunk chunk) {
		super(requestTAG, ok, request);
		this.setChunk(chunk);
	}

	@Override
	public ChunkReply clone() {
		return new ChunkReply(this.getRequestTAG(), this.isOk(), this
				.getRequest().clone(), this.getChunk());
	}

	public static ChunkReply get_data(Sim_event ev) {
		Message<ChunkReply> message = new Message<ChunkReply>();
		return message.get_data(ev);
	}

	@Override
	public ChunkRequest getRequest() {
		return (ChunkRequest) this.request;
	}

	public void setRequest(ChunkRequest request) {
		this.request = request;
	}

	public Chunk getChunk() {
		return this.getRequest().getChunk();
	}

	public void setChunk(Chunk chunk) {
		this.getRequest().setChunk(chunk);
	}
}
