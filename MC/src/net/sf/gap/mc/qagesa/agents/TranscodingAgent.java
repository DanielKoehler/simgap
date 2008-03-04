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
 * $Id$
 *
 */
package net.sf.gap.mc.qagesa.agents;

import junit.framework.Assert;
import net.sf.gap.agents.GridAgent;
import net.sf.gap.agents.predicates.Predicate;
import net.sf.gap.grid.components.AbstractGridElement;
import net.sf.gap.mc.QAGESA;
import net.sf.gap.mc.qagesa.constants.QAGESATags;
import net.sf.gap.mc.qagesa.grid.components.QAGESAGridElement;
import net.sf.gap.mc.qagesa.messages.*;
import net.sf.gap.mc.qagesa.multimedia.Chunk;
import net.sf.gap.mc.qagesa.multimedia.ChunksSequence;
import net.sf.gap.mc.qagesa.stats.QAGESAStat;
import net.sf.gap.util.EntitiesCounter;
import eduni.simjava.Sim_event;
import eduni.simjava.Sim_stat;
import eduni.simjava.Sim_system;
import gridsim.GridSim;
import gridsim.GridSimTags;
import gridsim.Gridlet;
import gridsim.IO_data;

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
     * between receival of TRANSCODE_CHUNKS_REQ and the send of tag SENT_LAST_CHUNK_REP
     */
    private Sim_stat statGTS;
    private boolean enabledCaching;
    private double gridMIPS;

    public TranscodingAgent(AbstractGridElement ge, String name, int agentSizeInBytes,
            boolean trace_flag, boolean enabledCaching) throws Exception {
        super(ge, name, agentSizeInBytes, trace_flag);
        this.setEnabledCaching(enabledCaching);
        //this.setupStatGTS();
    }

    private void setupStatGTS() {
        Sim_stat stat = new Sim_stat();
        int[] tags = 
        {
            QAGESATags.ASK_CHUNK_REQ, 
            QAGESATags.SEND_CHUNK_REP,
            QAGESATags.GET_CHUNK_REP,
            QAGESATags.TRANSCODE_CHUNKS_REQ
        };
        stat.measure_for(tags);
        stat.add_measure(Sim_stat.ARRIVAL_RATE);
        stat.add_measure(Sim_stat.QUEUE_LENGTH);
        stat.add_measure(Sim_stat.RESIDENCE_TIME);
        stat.add_measure(Sim_stat.WAITING_TIME);
        stat.add_measure(Sim_stat.SERVICE_TIME);
        stat.add_measure(Sim_stat.UTILISATION);
        stat.add_measure(Sim_stat.THROUGHPUT);
        this.set_stat(stat);
        this.setStatGTS(stat);
    }

    @Override
    public void initialize() throws Exception {
        super.initialize();
        this.setGridMIPS(this.getAgentPlatform().getVirtualOrganization().getMIPS());
    }

    @Override
    protected void dispose() {
    }

    public Chunk transcode(Chunk chunk, double quality) {
        // IF chunk is transcoded or its quality is less than 1.0 than transcoded it
        if ((chunk.getMIPS()>0) || (quality<0.99)) {
            if (!EntitiesCounter.contains("Gridlet")) {
                EntitiesCounter.create("Gridlet");
            }
            double length = chunk.getMIPS()*quality;
            long file_size = chunk.getInputSize();
            long output_size = Math.round(chunk.getOutputSize()*quality*quality);
            Gridlet gridlet = new Gridlet(EntitiesCounter.inc("Gridlet"), length,
                    file_size, output_size);
            gridlet.setUserID(this.get_id());
            super.gridletSubmit(gridlet, this.getResourceID());
            QAGESAStat.incComputedMIPS(chunk.getMIPS());
            double potentialGridMIPS = this.getGridMIPS()*(TranscodingAgent.clock()-QAGESA.getStartTime());
            double globalLoad =QAGESAStat.getComputedMIPS()/potentialGridMIPS;
            QAGESA.outMIPS.println("CSV;MIPS;"+QAGESAStat.getReplication()+";"+QAGESAStat.getNumUsers()+";"+QAGESAStat.isCachingEnabled()+";"+QAGESAStat.getWhichMeasure()+";"+this.clock()+";"+QAGESAStat.getComputedMIPS()+";"+potentialGridMIPS+";"+globalLoad);
            gridlet = super.gridletReceive();
        }
        Chunk transcodedChunk = chunk.transcode(quality);
        return transcodedChunk;
    }

    private void processAskChunkRequest(Sim_event ev) {
        int SIZE = 128;
        ChunkRequest askedCR = ChunkRequest.get_data(ev);
        double askedTime = this.clock();
        int askedSN = askedCR.getSequenceNumber();
        TranscodeRequest askedTR = askedCR.getTranscodeRequest();
        double evsend_time = 0;
        ChunkRequest askedRequest = new ChunkRequest(this.get_id(), this.get_id(),
                askedTR.getPlayReqrepID(),
                askedTR.getUserID(), askedTR.getMovieTag(), askedSN, askedTR.getStorageElementID(), askedTR, askedTime);
        int askedReqrepID = askedRequest.getReqrepID();
        super.send(super.output, GridSimTags.SCHEDULE_NOW,
                QAGESATags.GET_CHUNK_REQ, new IO_data(askedRequest, SIZE, askedRequest.getStorageElementID()));
        evsend_time = GridSim.clock();
        String msg = String.format(
                "%1$f %2$d %3$s --> %4$s GET_CHUNK_REQUEST %5$s %6$d",
                evsend_time,
                askedReqrepID,
                this.get_name(),
                Sim_system.get_entity(askedTR.getStorageElementID()).get_name(),
                askedTR.getMovieTag(),
                askedSN);
        this.write(msg);
        sim_completed(ev);
    }
    
    private void processSendChunkReply(Sim_event ev) {
        ChunkReply userChunkReply = ChunkReply.get_data(ev);
        if (!userChunkReply.getRequest().getTranscodeRequest().getPlayRequest().isRandomSelection() || true) {
            int SN = userChunkReply.getRequest().getSequenceNumber();
            double replyTime = this.clock();
            double memoizedAskedTime = userChunkReply.getRequest().getAskedTime();
            double delta = replyTime-memoizedAskedTime;
            double neededDelta = (userChunkReply.getRequest().getChunk().getDuration()*0.001) * SN;
            double updateQuality = userChunkReply.getRequest().getTranscodeRequest().getQuality();
            if ((delta > (neededDelta/0.9)) && (updateQuality>0.5)) {
                updateQuality = Math.max(updateQuality * 0.9,0.5);
                userChunkReply.getRequest().getTranscodeRequest().setQuality(updateQuality);
                //System.out.println("Downgrading quality to " + updateQuality + " for delta " + delta + " > " + neededDelta);
            } else if ((updateQuality<1.0) && (delta < (neededDelta * 0.81))) {
                updateQuality = Math.min(1.0, updateQuality / 0.9);
                userChunkReply.getRequest().getTranscodeRequest().setQuality(updateQuality);
                //System.out.println("Regaining quality to " + updateQuality + " for delta " + delta + " < " + neededDelta);
            }
        }
        sim_completed(ev);
    }
    
    private void processGetChunkReply(Sim_event ev) {
        int SIZE = 128;
        ChunkReply gotChunkReply = ChunkReply.get_data(ev);
        ChunkRequest gotChunkRequest = gotChunkReply.getRequest();
        TranscodeRequest transcodeRequest = gotChunkRequest.getTranscodeRequest();
        int seID = transcodeRequest.getStorageElementID();
        String movieTag = gotChunkRequest.getMovieTag();
        // Get current chunks sequence on the storage element
        QAGESAGridElement se = (QAGESAGridElement) Sim_system.get_entity(seID);
        ChunksSequence sequence = se.getTranscodingSet().get(movieTag);
        boolean previouslyTranscoded = sequence.isTranscoded();
        int requestID = gotChunkRequest.getRequestID();
        int reqrepID = gotChunkRequest.getReqrepID();
        int sequenceNumber = gotChunkRequest.getSequenceNumber();
        Assert.assertEquals(requestID, gotChunkReply.getRequestID());
        Assert.assertEquals(QAGESATags.GET_CHUNK_REQ, gotChunkReply.getRequestTAG());
        Assert.assertEquals(QAGESATags.GET_CHUNK_REP, ev.get_tag());
        double evrecv_time = GridSim.clock();
        String msg = String.format("%1$f %2$d %3$s <-- %4$s GET_CHUNK_REPLY %5$s %6$d %7$s",
                evrecv_time,
                reqrepID,
                this.get_name(),
                se.get_name(),
                movieTag,
                sequenceNumber,
                gotChunkReply.getChunk());
        this.write(msg);
        Chunk gotChunk = gotChunkReply.getChunk();
        Chunk transcodedChunk = null;
        transcodedChunk = this.transcode(gotChunk, transcodeRequest.getQuality());
        if (sequenceNumber == 1) {
            ChunkRequest chunkRequest = new ChunkRequest(this.get_id(), this.get_id(),
                    transcodeRequest.getPlayReqrepID(),
                    transcodeRequest.getUserID(), movieTag, sequenceNumber, transcodeRequest.getStorageElementID(), transcodeRequest, gotChunkRequest.getAskedTime());
            super.send(super.output, GridSimTags.SCHEDULE_NOW,
                    QAGESATags.TRANSCODED_FIRST_CHUNK_REP,
                    new IO_data(chunkRequest, 1, transcodeRequest.getUserID()));
        }
        this.sendChunk(transcodeRequest.getPlayReqrepID(), transcodeRequest.getUserID(), movieTag, sequenceNumber, transcodedChunk, transcodeRequest.getStorageElementID(), transcodeRequest, gotChunkRequest.getAskedTime());
        int nc = sequence.size();
        if (sequenceNumber <nc) {
            int nextSequenceNumber = sequenceNumber+1;
            ChunkRequest request = new ChunkRequest(this.get_id(), this.get_id(),
                    transcodeRequest.getPlayReqrepID(),
                    transcodeRequest.getUserID(), movieTag, nextSequenceNumber, transcodeRequest.getStorageElementID(), transcodeRequest, gotChunkRequest.getAskedTime());
            requestID = request.getRequestID();
            reqrepID = request.getReqrepID();
            super.send(super.output, GridSimTags.SCHEDULE_NOW,
                    QAGESATags.GET_CHUNK_REQ, new IO_data(request, SIZE, request.getStorageElementID()));
            double evsend_time = GridSim.clock();
            msg = String.format(
                    "%1$f %2$d %3$s --> %4$s GET_CHUNK_REQUEST %5$s %6$d",
                    evsend_time,
                    reqrepID,
                    this.get_name(),
                    se.get_name(),
                    movieTag,
                    nextSequenceNumber);
            this.write(msg);
        } else {
            this.sendLastChunk(transcodeRequest.getPlayReqrepID(), transcodeRequest.getUserID(), movieTag, seID, transcodeRequest, gotChunkRequest.getAskedTime());
            TranscodeReply transcodeReply = new TranscodeReply(ev.get_tag(), true, transcodeRequest, this.get_id());
            super.send(super.output, GridSimTags.SCHEDULE_NOW,
                    QAGESATags.TRANSCODE_CHUNKS_REP, new IO_data(transcodeReply, 500, transcodeRequest.getSrc_ID()));
            if (!previouslyTranscoded && this.isEnabledCaching()) {
                ChunksSequence transcodedSequence = new ChunksSequence(sequence.getMovie(), sequence.getOperation(), sequence.getOperationParameters());
                for (int k=0;k<sequence.size();k++) {
                    transcodedSequence.add(sequence.get(k).clone());
                }
                this.setTranscoded(transcodedSequence);
                transcodeRequest.setSequence(transcodedSequence);
                reqrepID = transcodeRequest.getReqrepID();
                super.send(super.output, GridSimTags.SCHEDULE_NOW,
                        QAGESATags.CACHE_CHUNKS_REQ, new IO_data(transcodeRequest, transcodeRequest.getSequence().getInputSize(), transcodeRequest.getStorageElementID()));
                double evsend_time = GridSim.clock();
                msg = String.format(
                        "%1$f %2$d %3$s --> %4$s CACHE_CHUNKS %5$s",
                        evsend_time,
                        reqrepID,
                        this.get_name(),
                        Sim_system.get_entity(transcodeRequest.getStorageElementID()).get_name(),
                        movieTag);
                this.write(msg);
            }
        }
        sim_completed(ev);
    }
    
    private void processTranscodeChunksRequest(Sim_event ev) {
        TranscodeRequest transcodeRequest = TranscodeRequest.get_data(ev);
        int playReqrepID = transcodeRequest.getPlayReqrepID();
        int userID = transcodeRequest.getUserID();
        String movieTag = transcodeRequest.getMovieTag();
        int seID = transcodeRequest.getStorageElementID();
        // Get current chunks sequence on the storage element
        QAGESAGridElement se = (QAGESAGridElement) Sim_system.get_entity(seID);
        ChunksSequence sequence = se.getTranscodingSet().get(movieTag);
        Chunk chunk = sequence.get(0);
        int sequenceNumber = chunk.getSequenceNumber();
        ChunkRequest chunkRequest = new ChunkRequest(this.get_id(), this.get_id(),
                playReqrepID,
                userID, movieTag, sequenceNumber, seID, transcodeRequest, this.clock());
        super.send(super.output, GridSimTags.SCHEDULE_NOW,
                QAGESATags.SENDING_FIRST_CHUNK_REP,
                new IO_data(chunkRequest, 1, userID));
        sim_completed(ev);
    }    
    
    @Override
    public void processOtherEvent(Sim_event ev) {
        switch (ev.get_tag()) {
            case QAGESATags.ASK_CHUNK_REQ:
                this.processAskChunkRequest(ev);
                break;
            case QAGESATags.SEND_CHUNK_REP:
                this.processSendChunkReply(ev);
                break;
            case QAGESATags.GET_CHUNK_REP:
                this.processGetChunkReply(ev);
                break;
            case QAGESATags.TRANSCODE_CHUNKS_REQ:
                this.processTranscodeChunksRequest(ev);
                break;
            default:
                break;
        }
    }
    
    private void setTranscoded(ChunksSequence sequence) {
        int nc = sequence.size();
        for (int i=0;i<nc;i++) {
           Chunk chunk = sequence.get(i);
           chunk.setInputSize(chunk.getOutputSize());
           chunk.setMIPS(0);
           chunk.setTranscoded(true);
        }
    }

    protected void sendChunk(int playReqrepID, int userID, String movieTag,
            int sequenceNumber, Chunk chunk, int seID, TranscodeRequest transcodeRequest, double askedTime) {
        double evsend_time = 0;
        ChunkRequest request = new ChunkRequest(this.get_id(), this.get_id(),
                playReqrepID,
                userID, movieTag, sequenceNumber, seID, transcodeRequest, askedTime);
        request.setChunk(chunk);
        @SuppressWarnings("unused")
        int requestID = request.getRequestID();
        int reqrepID = request.getReqrepID();
        request.setChunk(chunk);
        super.send(super.output, GridSimTags.SCHEDULE_NOW,
                QAGESATags.SEND_CHUNK_REQ, new IO_data(request, chunk.getOutputSize(), userID));
        evsend_time = GridSim.clock();
        String msg = String.format(
                "%1$f %2$d %3$s --> %4$s SEND_CHUNK %5$s %6$d %7$s",
                evsend_time, reqrepID, this.get_name(), Sim_system.get_entity(
                userID).get_name(), movieTag, sequenceNumber, chunk.toString());
        this.write(msg);
    }

    protected void sendLastChunk(int playReqrepID, int userID, String movieTag,
            int seID, TranscodeRequest transcodeRequest, double askedTime) {
        double evsend_time = 0;
        ChunkRequest request = new ChunkRequest(this.get_id(), this.get_id(),
                playReqrepID,
                userID, movieTag, 0, seID, transcodeRequest, askedTime);
        @SuppressWarnings("unused")
        int requestID = request.getRequestID();
        int reqrepID = request.getReqrepID();
        int SIZE = 500;
        super.send(super.output, GridSimTags.SCHEDULE_NOW,
                QAGESATags.SENT_LAST_CHUNK_REP, new IO_data(request, SIZE, userID));
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

    public double getGridMIPS() {
        return gridMIPS;
    }

    public void setGridMIPS(double gridMIPS) {
        this.gridMIPS = gridMIPS;
    }
}
