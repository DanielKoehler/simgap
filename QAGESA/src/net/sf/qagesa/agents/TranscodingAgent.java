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
 * TranscodingAgent.java
 *
 * Created on 12 August 2006, 19.35 by Giovanni Novelli
 *
 * $Id: TranscodingAgent.java 1142 2007-07-18 20:49:58Z gnovelli $
 *
 */

package net.sf.qagesa.agents;

import eduni.simjava.Sim_event;
import eduni.simjava.Sim_stat;
import eduni.simjava.Sim_system;

import gridsim.Gridlet;
import gridsim.GridSim;
import gridsim.GridSimTags;
import gridsim.IO_data;

import junit.framework.Assert;

import net.sf.gap.agents.GridAgent;
import net.sf.gap.agents.predicates.Predicate;
import net.sf.gap.grid.components.GridElement;
import net.sf.gap.util.EntitiesCounter;

import net.sf.qagesa.constants.QAGESATags;
import net.sf.qagesa.grid.components.QAGESAGridElement;
import net.sf.qagesa.messages.ChunkReply;
import net.sf.qagesa.messages.ChunkRequest;
import net.sf.qagesa.messages.TranscodeReply;
import net.sf.qagesa.messages.TranscodeRequest;
import net.sf.qagesa.multimedia.Chunk;
import net.sf.qagesa.multimedia.ChunksSequence;

/**
 * 
 * @author Giovanni Novelli
 */
public class TranscodingAgent extends GridAgent {
        
        /**
     * 
     * Sim_stat object used to measure the loop about:
     *  - get chunk
     *  - process chunk
     *  - send chunk
     * between receival of TRANSCODE_CHUNKS_REQ and the send of tag SENT_LAST_CHUNK_REQ
     */
        private Sim_stat statGTS;
        
        private boolean enabledCaching;
    
	public TranscodingAgent(GridElement ge, String name, int agentSizeInBytes,
			boolean trace_flag, boolean enabledCaching) throws Exception {
		super(ge, name, agentSizeInBytes, trace_flag);
                this.setEnabledCaching(enabledCaching);
                //this.setupStatGTS();
	}

    private void setupStatGTS() {
        Sim_stat stat = new Sim_stat();
        int[] tags = { QAGESATags.TRANSCODE_CHUNKS_REQ };
        stat.measure_for(tags);
        stat.add_measure(Sim_stat.SERVICE_TIME);
        this.set_stat(stat);
        this.setStatGTS(stat);
    }

    @Override
	public void initialize() throws Exception {
            super.initialize();
	}

	@Override
	protected void dispose() {
	}

        public Chunk transcode(Chunk chunk) {
		if (!EntitiesCounter.contains("Gridlet")) {
			EntitiesCounter.create("Gridlet");
		}
		double length = chunk.getMIPS();
		long file_size = chunk.getInputSize();
		long output_size = chunk.getOutputSize();
		Gridlet gridlet = new Gridlet(EntitiesCounter.inc("Gridlet"), length,
				file_size, output_size);
				gridlet.setUserID(this.get_id());
                 super.send(this.getResourceID(), GridSimTags.SCHEDULE_NOW,
                         GridSimTags.GRIDLET_SUBMIT, gridlet);
                // Receiving a Gridlet back
                gridlet = super.gridletReceive();
                Chunk transcodedChunk = chunk.transcode();
                return transcodedChunk;
        }
        
	@Override
	public void processOtherEvent(Sim_event ev) {
		switch (ev.get_tag()) {
		case QAGESATags.TRANSCODE_CHUNKS_REQ:
			TranscodeRequest transcodeRequest = TranscodeRequest.get_data(ev);
                        int playReqrepID = transcodeRequest.getPlayReqrepID();
			int userID = transcodeRequest.getUserID();
			String movieTag = transcodeRequest.getMovieTag();
                        int seID = transcodeRequest.getStorageElementID();
                        // Get current chunks sequence on the storage element
                        QAGESAGridElement se = (QAGESAGridElement) Sim_system.get_entity(seID);
			ChunksSequence sequence = se.getTranscodingSet().get(movieTag);
                        ChunksSequence transcodedSequence = new ChunksSequence(sequence.getMovie(), sequence.getOperation(),sequence.getOperationParameters());
                        boolean previouslyTranscoded = sequence.isTranscoded();
                        //System.out.println(sequence);
			int nc = sequence.size();
			for (int i = 0; i < nc; i++) {
				Chunk chunk = sequence.get(i);
				int sequenceNumber = chunk.getSequenceNumber();
                                if (sequenceNumber==1) {
                                    ChunkRequest request = new ChunkRequest(this.get_id(), this.get_id(),
                                                    playReqrepID,
                                                    userID, movieTag, sequenceNumber, seID);
                                    super.send(super.output, GridSimTags.SCHEDULE_NOW,
                                                    QAGESATags.SENDING_FIRST_CHUNK_REQ, 
                                            new IO_data(request, 1, userID));
                                }
				ChunkReply getChunkReply = this.getChunk(
                                        playReqrepID,
                                        userID, movieTag, sequenceNumber,seID);
				Chunk gotChunk = getChunkReply.getChunk();
                                Chunk transcodedChunk = null;
                                if (!gotChunk.isTranscoded()) {
                                transcodedChunk = this.transcode(gotChunk);
                                if (this.isEnabledCaching()) {
                                    transcodedSequence.add(transcodedChunk);
                                }
                                } else {
                                  transcodedChunk = gotChunk;
                                }
                                if (sequenceNumber==1) {
                                    ChunkRequest request = new ChunkRequest(this.get_id(), this.get_id(),
                                                    playReqrepID,
                                                    userID, movieTag, sequenceNumber, seID);
                                    super.send(super.output, GridSimTags.SCHEDULE_NOW,
                                                    QAGESATags.TRANSCODED_FIRST_CHUNK_REQ, 
                                            new IO_data(request, 1, userID));
                                }
 			        this.sendChunk(playReqrepID, userID, movieTag,sequenceNumber, transcodedChunk, seID);
			}
        	        this.sendLastChunk(playReqrepID, userID, movieTag, seID);
			super.sim_completed(ev);
                        if (!previouslyTranscoded && this.isEnabledCaching()) {
                            transcodeRequest.setSequence(transcodedSequence);
                            double evsend_time = 0;
                            int requestID = transcodeRequest.getRequestID();
                            int reqrepID = transcodeRequest.getReqrepID();
                            super.send(super.output, GridSimTags.SCHEDULE_NOW,
                                            QAGESATags.CACHE_CHUNKS_REQ, new IO_data(transcodeRequest, transcodeRequest.getSequence().getInputSize(), transcodeRequest.getStorageElementID()));
                            evsend_time = GridSim.clock();
                            String msg = String.format(
                                            "%1$f %2$d %3$s --> %4$s CACHE_CHUNKS %5$s", 
                                            evsend_time,
                                            reqrepID, 
                                            this.get_name(), 
                                            Sim_system.get_entity(transcodeRequest.getStorageElementID()).get_name(), 
                                            movieTag);
                            this.write(msg);
                        }
                        TranscodeReply transcodeReply = new TranscodeReply(ev.get_tag(), true, transcodeRequest, this.get_id());
                        super.send(super.output, GridSimTags.SCHEDULE_NOW,
                                        QAGESATags.TRANSCODE_CHUNKS_REP, new IO_data(transcodeReply, 500, transcodeRequest
                                                        .getSrc_ID()));
			break;
                        
		default:
			break;
		}
	}

	protected ChunkReply getChunk(int playReqrepID, int userID, String movieTag,
			int sequenceNumber, int storageElementID) {
		int SIZE = 500;
		double evsend_time = 0;
		ChunkRequest request = new ChunkRequest(this.get_id(), this.get_id(),
                                playReqrepID,
				userID, movieTag, sequenceNumber, storageElementID);
		int requestID = request.getRequestID();
		int reqrepID = request.getReqrepID();
		super.send(super.output, GridSimTags.SCHEDULE_NOW,
				QAGESATags.GET_CHUNK_REQ, new IO_data(request, SIZE, request.getStorageElementID()));
		evsend_time = GridSim.clock();
		String msg = String.format(
				"%1$f %2$d %3$s --> %4$s GET_CHUNK_REQUEST %5$s %6$d", 
                                evsend_time,
				reqrepID, 
                                this.get_name(), 
                                this.getGridElement().get_name(), 
                                movieTag,
                                sequenceNumber);
		this.write(msg);

		Sim_event ev = new Sim_event();
		Predicate predicate = new Predicate(QAGESATags.GET_CHUNK_REP);
		super.sim_get_next(predicate, ev); // only look for this type of ack
		ChunkReply reply = ChunkReply.get_data(ev);
		Assert.assertEquals(requestID, reply.getRequestID());
		Assert
				.assertEquals(QAGESATags.GET_CHUNK_REQ, reply
						.getRequestTAG());
		Assert.assertEquals(QAGESATags.GET_CHUNK_REP, ev.get_tag());
		double evrecv_time = GridSim.clock();
			msg = String.format("%1$f %2$d %3$s <-- %4$s GET_CHUNK_REPLY %5$s %6$d %7$s",
					evrecv_time, 
                                        reqrepID,
                                        this.get_name(), 
                                        this.getGridElement().get_name(), 
                                        movieTag, 
                                        sequenceNumber,
                                        reply.getChunk());
		this.write(msg);
		return reply;
	}

	//protected ChunkReply sendChunk(int userID, String movieTag,
	protected void sendChunk(int playReqrepID, int userID, String movieTag,
			int sequenceNumber, Chunk chunk, int seID) {
		double evsend_time = 0;
		ChunkRequest request = new ChunkRequest(this.get_id(), this.get_id(),
                                playReqrepID,
				userID, movieTag, sequenceNumber, seID);
                request.setChunk(chunk);
		int requestID = request.getRequestID();
		int reqrepID = request.getReqrepID();
		request.setChunk(chunk);
		super.send(super.output, GridSimTags.SCHEDULE_NOW,
				QAGESATags.SEND_CHUNK_REQ, new IO_data(request, chunk
						.getOutputSize(), userID));
		evsend_time = GridSim.clock();
		String msg = String.format(
				"%1$f %2$d %3$s --> %4$s SEND_CHUNK %5$s %6$d %7$s",
				evsend_time, reqrepID, this.get_name(), Sim_system.get_entity(
						userID).get_name(), movieTag, sequenceNumber, chunk
						.toString());
		this.write(msg);
/*
		Sim_event ev = new Sim_event();
		Predicate predicate = new Predicate(QAGESATags.SEND_CHUNK_REPLY);
		super.sim_get_next(predicate, ev); // only look for this type of ack
		ChunkReply reply = ChunkReply.get_data(ev);
		Assert.assertEquals(requestID, reply.getRequestID());
		Assert.assertEquals(QAGESATags.SEND_CHUNK_REQ, reply
				.getRequestTAG());
		Assert.assertEquals(QAGESATags.SEND_CHUNK_REPLY, ev.get_tag());
		double evrecv_time = GridSim.clock();
		if (reply.isOk()) {
			msg = String.format(
					"%1$f %2$d %3$s <-- %4$s SEND_CHUNK_REPLY %5$s %6$s",
					evrecv_time, reqrepID, this.get_name(), this
							.getGridElement().get_name(), movieTag, reply
							.getChunk());
		} else {
			msg = String.format(
					"%1$f %2$d %3$s <-- %4$s SEND_CHUNK_REPLY %5$s %6$s",
					evrecv_time, reqrepID, this.get_name(), this
							.getGridElement().get_name(), movieTag, "NONE");
		}
		this.write(msg);
		return reply;
 */
	}

	protected void sendLastChunk(int playReqrepID, int userID, String movieTag,
			int seID) {
		double evsend_time = 0;
		ChunkRequest request = new ChunkRequest(this.get_id(), this.get_id(),
                                playReqrepID,
				userID, movieTag, 0, seID);
		int requestID = request.getRequestID();
		int reqrepID = request.getReqrepID();
                int SIZE = 500;
		super.send(super.output, GridSimTags.SCHEDULE_NOW,
				QAGESATags.SENT_LAST_CHUNK_REQ, new IO_data(request, SIZE, userID));
		evsend_time = GridSim.clock();
		String msg = String.format(
				"%1$f %2$d %3$s --> %4$s SENT_LAST_CHUNK %5$d %6$s",
				evsend_time, reqrepID, this.get_name(), Sim_system.get_entity(
						userID).get_name(), 
                                playReqrepID,
                                movieTag);
		this.write(msg);
	}

    public Sim_stat getStatGTS() {
        return statGTS;
    }

    public void setStatGTS(Sim_stat statGTS) {
        this.statGTS = statGTS;
    }

    public boolean isEnabledCaching() {
        return enabledCaching;
    }

    public void setEnabledCaching(boolean enabledCaching) {
        this.enabledCaching = enabledCaching;
    }
}
