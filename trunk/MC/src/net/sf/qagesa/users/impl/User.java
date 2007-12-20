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
 * User.java
 *
 * Created on 15 March 2007, 21.32 by Giovanni Novelli
 *
 * $Id: User.java 1142 2007-07-18 20:49:58Z gnovelli $
 *
 */

package net.sf.qagesa.users.impl;

import eduni.simjava.Sim_event;
import eduni.simjava.Sim_system;
import gridsim.GridSim;
import gridsim.GridSimTags;
import gridsim.IO_data;
import gridsim.net.Link;
import java.util.List;
import net.sf.gap.agents.predicates.Predicate;
import net.sf.qagesa.messages.ChunkRequest;
import net.sf.qagesa.messages.ReFPlayRequest;
import net.sf.qagesa.constants.QAGESATags;
import eduni.simjava.Sim_event;
import eduni.simjava.distributions.*;
import eduni.simjava.Sim_stat;
import eduni.simjava.Sim_system;

import gridsim.GridSim;
import gridsim.net.InfoPacket;
import gridsim.net.Link;

import net.sf.gap.agents.predicates.Predicate;
import net.sf.qagesa.agents.services.impl.MuMService;
import net.sf.qagesa.constants.QAGESAEntityTypes;
import net.sf.qagesa.constants.QAGESATags;
import net.sf.qagesa.messages.ChunkRequest;
import net.sf.qagesa.messages.ReFPlayReply;
import net.sf.qagesa.messages.ReFPlayRequest;
import net.sf.qagesa.stats.QAGESAStat;
import net.sf.qagesa.users.QAGESAUser;

/**
 * @author Giovanni Novelli
 */
public class User extends QAGESAUser {
    public static final int MEASURE_RESPONSE  = 1;
    public static final int MEASURE_STREAMING = 2;
    public static final int MEASURE_FIRST     = 3;
    
    private int selectedMeasure;
    
    /**
     * 
     * Sim_stat object used to measure SERVICE_TIME between REF_PLAY_REQ and REF_PLAY_REPLY
     */
    private Sim_stat statResponseTime;
    
    /**
     * 
     * Sim_stat object used to measure SERVICE_TIME between SENDING_FIRST_CHUNK_REQ and SENT_LAST_CHUNK_REQ
     */
    private Sim_stat statStreaming;
    
    /**
     * 
     * Sim_stat object used to measure SERVICE_TIME between SENDING_FIRST_CHUNK_REQ and 1st SEND_CHUNK_REQ
     */
    private Sim_stat statFirst;
    
    /**
     *
     * Movie tag that can be reiterated in subsequent play requests to measure the effect of chunks caching
     * on performances. This tag is considered when field repeated is set to true.
     *
     */
    private String repeatedMovieTag;
    
    /**
     *
     * Flag used to instruct an User entity instance to reiterate subsequent play requests with the same movie tag.
     * The shared movie tag is maintained in field repeatedMovieTag.
     *
     */
    private boolean repeated;
    
    /**
     *
     * Field used to instruct an User entit instance about how much max subsequent play requests it has to do.
     *
     */
    private int numRequests;
    
    /**
     *
     * Flag used to signal to ReF Service to not use its metric to select CE and SE but to do just a random selection of them.
     *
     */
    private boolean randomSelection;
    
    /**
     *
     * Creates a new instance of User
     *
     * @param name
     *            AgentMiddleware entity name
     * @param trace_flag
     *            GridSim trace flag
     * @throws Exception
     *             This happens when name is null or haven't initialized
     *             GridSim.
     */
    public User(String name, Link link, boolean trace_flag, boolean randomSelection, int numRequests, boolean repeated, String movieTag, int selectedMeasure) throws Exception {
        super(name, link, QAGESAEntityTypes.USER_USER, trace_flag);
        this.setRandomSelection(randomSelection);
        this.setNumRequests(numRequests);
        this.setRepeated(repeated);
        this.setRepeatedMovieTag(movieTag);
        this.setSelectedMeasure(selectedMeasure);
        switch (this.getSelectedMeasure()) {
            case User.MEASURE_RESPONSE:
                this.setupStatPlay();
                break;
            case User.MEASURE_STREAMING:
                this.setupStatStreaming();
                break;
            case User.MEASURE_FIRST:
                this.setupStatFirst();
                break;
        }
    }
    
    private void setupStatPlay() {
        Sim_stat stat = new Sim_stat();
        int[] tags = { QAGESATags.REF_PLAY_START_REP};
        stat.measure_for(tags);
        stat.add_measure(Sim_stat.SERVICE_TIME);
        this.set_stat(stat);
        this.setStatResponseTime(stat);
    }
    private void setupStatStreaming() {
        Sim_stat stat = new Sim_stat();
        int[] tags = { QAGESATags.SENDING_FIRST_CHUNK_REQ, QAGESATags.SENT_LAST_CHUNK_REQ };
        stat.measure_for(tags);
        stat.add_measure(Sim_stat.SERVICE_TIME);
        this.set_stat(stat);
        this.setStatStreaming(stat);
    }
    
    private void setupStatFirst() {
        Sim_stat stat = new Sim_stat();
        int[] tags = { QAGESATags.SENDING_FIRST_CHUNK_REQ, QAGESATags.TRANSCODED_FIRST_CHUNK_REQ};
        stat.measure_for(tags);
        stat.add_measure(Sim_stat.SERVICE_TIME);
        this.set_stat(stat);
        this.setStatFirst(stat);
    }
    
    private void printStatFirst() {
        List[] list = this.getStatFirst().get_data();
        List datas = list[1];
        List data = (List) datas.toArray()[0];
        //System.out.println("FILEFORMAT;REPLICATION;NUMUSERS;CACHING;USERTYPE;ENTITY;START_TIME;END_TIME;STREAMING_TIME");
        int nd = data.size();
        for (int i=0;i<nd;i++) {
           double[] times = (double[] ) data.toArray()[i];
           System.out.println("CSV;USERS_FT;"+QAGESAStat.getReplication()+";"+QAGESAStat.getNumUsers()+";"+QAGESAStat.isCachingEnabled()+";"+QAGESAStat.getWhichMeasure()+";"+this.get_name()+";"+times[0]+";"+times[1]+";"+(times[1]-times[0]));
        }
    }

    private void printStatStreaming() {
        List[] list = this.getStatStreaming().get_data();
        List datas = list[1];
        List data = (List) datas.toArray()[0];
        //System.out.println("FILEFORMAT;REPLICATION;NUMUSERS;CACHING;USERTYPE;ENTITY;START_TIME;END_TIME;STREAMING_TIME");
        int nd = data.size();
        for (int i=0;i<nd;i++) {
           double[] times = (double[] ) data.toArray()[i];
           System.out.println("CSV;USERS_ST;"+QAGESAStat.getReplication()+";"+QAGESAStat.getNumUsers()+";"+QAGESAStat.isCachingEnabled()+";"+QAGESAStat.getWhichMeasure()+";"+this.get_name()+";"+times[0]+";"+times[1]+";"+(times[1]-times[0]));
        }
    }
    
    private void printStatResponseTime() {
        List[] list = this.getStatResponseTime().get_data();
        List datas = list[1];
        List data = (List) datas.toArray()[0];
        //System.out.println("FILEFORMAT;REPLICATION;NUMUSERS;CACHING;USERTYPE;ENTITY;START_TIME;END_TIME;RESPONSE_TIME");
        int nd = data.size();
        for (int i=0;i<nd;i++) {
           double[] times = (double[] ) data.toArray()[i];
           System.out.println("CSV;USERS_RT;"+QAGESAStat.getReplication()+";"+QAGESAStat.getNumUsers()+";"+QAGESAStat.isCachingEnabled()+";"+QAGESAStat.getWhichMeasure()+";"+this.get_name()+";"+times[0]+";"+times[1]+";"+(times[1]-times[0]));
        }
    }
    
    public void printStats() {
        switch (this.getSelectedMeasure()) {
            case User.MEASURE_RESPONSE:
                this.printStatResponseTime();
                break;
            case User.MEASURE_STREAMING:
                this.printStatStreaming();
                break;
            case User.MEASURE_FIRST:
                this.printStatFirst();
                break;
        }
    }
     
    protected ReFPlayReply playRequest(String movieTag) {
        int SIZE = 500;
        double evsend_time = 0;
        int userID = this.get_id();
        ReFPlayRequest request = new ReFPlayRequest(this.get_id(), this
                .get_id(), userID, movieTag, this.isRandomSelection());
        int requestID = request.getRequestID();
        int reqrepID = request.getReqrepID();
        super.send(super.output, GridSimTags.SCHEDULE_NOW,
                QAGESATags.REF_PLAY_REQ, new IO_data(request, SIZE, super
                .getEntityId("ReF")));
        evsend_time = GridSim.clock();
        String msg = String.format(
                "%1$f %2$d %3$s --> ReF REF_PLAY_REQUEST %4$s", evsend_time,
                reqrepID, this.get_name(), movieTag);
        this.write(msg);
        
        Sim_event ev = new Sim_event();
        Predicate predicate = new Predicate(QAGESATags.REF_PLAY_START_REP);
        super.sim_get_next(predicate, ev); // only look for this type of ack
        ReFPlayReply playReply = ReFPlayReply.get_data(ev);
        request = playReply.getRequest();
        double evrecv_time = GridSim.clock();
        if (playReply.isOk()) {
            msg = String.format(
                    "%1$f %2$d %3$s <-- ReF REF_PLAYSTART_REPLY (SUCCESS) %4$d %5$s",
                    evrecv_time, request.getReqrepID(), this.get_name(),
                    request.getReqrepID(),
                    request.getMovieTag());
        } else {
            msg = String.format(
                    "%1$f %2$d %3$s <-- ReF REF_PLAYSTART_REPLY (FAILURE) %4$d %5$s",
                    evrecv_time, request.getReqrepID(), this.get_name(),
                    request.getReqrepID(),
                    request.getMovieTag());
        }
        this.write(msg);
        if (playReply.isOk()) {
            Sim_event fcev = new Sim_event();
            predicate = new Predicate(QAGESATags.SENDING_FIRST_CHUNK_REQ);
            super.sim_get_next(predicate, fcev); // only look for this type of ack
            ChunkRequest chunkRequest = ChunkRequest.get_data(fcev);
            reqrepID = chunkRequest.getReqrepID();
            evrecv_time = GridSim.clock();
            msg = String.format(
                    "%1$f %2$d %3$s <-- %4$s SENDING_FIRST_CHUNK %5$d %6$d",
                    evrecv_time, reqrepID, this.get_name(),
                    Sim_system.get_entity(chunkRequest.getSrc_ID()).get_name(),
                    chunkRequest.getPlayReqrepID(),
                    chunkRequest.getSequenceNumber());
            this.write(msg);

            Sim_event tfcev = new Sim_event();
            predicate = new Predicate(QAGESATags.TRANSCODED_FIRST_CHUNK_REQ);
            super.sim_get_next(predicate, tfcev); // only look for this type of ack
            ChunkRequest tchunkRequest = ChunkRequest.get_data(tfcev);
            int treqrepID = chunkRequest.getReqrepID();
            double tevrecv_time = GridSim.clock();
            String tmsg = String.format(
                    "%1$f %2$d %3$s <-- %4$s TRANSCODED_FIRST_CHUNK %5$d %6$d",
                    tevrecv_time, treqrepID, this.get_name(),
                    Sim_system.get_entity(tchunkRequest.getSrc_ID()).get_name(),
                    tchunkRequest.getPlayReqrepID(),
                    tchunkRequest.getSequenceNumber());
            this.write(tmsg);
            if (this.getSelectedMeasure()==User.MEASURE_FIRST) {
                sim_completed(fcev);
            } 

            if (this.getSelectedMeasure()==User.MEASURE_RESPONSE) {
                sim_completed(ev);
            }
            
            Sim_event tev = new Sim_event();
            int[] tags = {QAGESATags.SENT_LAST_CHUNK_REQ, QAGESATags.SEND_CHUNK_REQ};
            predicate = new Predicate(tags);
            super.sim_get_next(predicate,tev);
            this.processEvent(tev);
            for (int i=0;i<6;i++) {
                Sim_event anev = new Sim_event();
                super.sim_get_next(predicate,anev);
                this.processEvent(anev);
            }
            if (this.getSelectedMeasure()==User.MEASURE_STREAMING) {
                sim_completed(fcev);
            }
            
            Sim_event perev = new Sim_event();
            predicate = new Predicate(QAGESATags.REF_PLAY_END_REP);
            super.sim_get_next(predicate,perev);
            this.processEvent(perev);
            
        } else {
            if (this.getSelectedMeasure()==User.MEASURE_RESPONSE) {
                sim_completed(ev);
            }
        }
        return playReply;
    }
    
    @Override
    public void processOtherEvent(Sim_event ev) {
        switch (ev.get_tag()) {
            case QAGESATags.SENT_LAST_CHUNK_REQ:
                ChunkRequest chunkRequest = ChunkRequest.get_data(ev);
                int playReqrepID = chunkRequest.getPlayReqrepID();
                int reqrepID = chunkRequest.getReqrepID();
                double evrecv_time = GridSim.clock();
                String msg = String.format(
                        "%1$f %2$d %3$s <-- %4$s SENT_LAST_CHUNK %5$d",
                        evrecv_time, reqrepID, this.get_name(),
                        Sim_system.get_entity(chunkRequest.getSrc_ID()).get_name(),
                        chunkRequest.getPlayReqrepID());
                this.write(msg);
                break;
            case QAGESATags.SEND_CHUNK_REQ:
                chunkRequest = ChunkRequest.get_data(ev);
                reqrepID = chunkRequest.getReqrepID();
                evrecv_time = GridSim.clock();
                msg = String.format(
                        "%1$f %2$d %3$s <-- %4$s SEND_CHUNK %5$d %6$d",
                        evrecv_time, reqrepID, this.get_name(),
                        Sim_system.get_entity(chunkRequest.getSrc_ID()).get_name(),
                        chunkRequest.getPlayReqrepID(),
                        chunkRequest.getSequenceNumber());
                this.write(msg);
                break;
            case QAGESATags.REF_PLAY_END_REP:
                ReFPlayReply playReply = ReFPlayReply.get_data(ev);
                ReFPlayRequest request = playReply.getRequest();
                evrecv_time = GridSim.clock();
                if (playReply.isOk()) {
                    msg = String.format(
                            "%1$f %2$d %3$s <-- ReF REF_PLAYEND_REPLY (SUCCESS) %4$d %5$s",
                            evrecv_time, request.getReqrepID(), this.get_name(),
                            request.getReqrepID(),
                            request.getMovieTag());
                } else {
                    msg = String.format(
                            "%1$f %2$d %3$s <-- ReF REF_PLAYEND_REPLY (FAILURE) %4$d %5$s",
                            evrecv_time, request.getReqrepID(), this.get_name(),
                            request.getReqrepID(),
                            request.getMovieTag());
                }
                this.write(msg);
                break;
            default:
                break;
        }
    }
    
    @Override
    public void initWork() {
        this.DoIt();
    }
    
    private void DoIt() {
        this.pingCEs();
        if (this.isRepeated() && this.getRepeatedMovieTag()==null) {
            String movieTag = MuMService.getMUMTranscodingSet().selectRandomTag();
            this.setRepeatedMovieTag(movieTag);
        }
        double start_clock = this.clock();
        for (int cycle=1;cycle<=this.getNumRequests();cycle++) {
                int ratio = QAGESAStat.getNumUsers()/this.getNumRequests();
                int maxUsers = cycle*ratio;
                double mean_interarrival = (QAGESAStat.getNumUsers() * 1.0)/maxUsers;
                Sim_negexp_obj negexp = new Sim_negexp_obj("interval",mean_interarrival);
                double diff_time = negexp.sample();
                double start_time = diff_time;
                super.gridSimHold(start_time);
                QAGESAStat.incRequests(this.clock());
                this.repeatedRandomRequest();
                QAGESAStat.decRequests(this.clock());
        }
        for (int cycle=1;cycle<=this.getNumRequests();cycle++) {
                int ratio = QAGESAStat.getNumUsers()/this.getNumRequests();
                Sim_negexp_obj negexp = new Sim_negexp_obj("interval",1.0);
                double diff_time = negexp.sample();
                double start_time = diff_time;
                super.gridSimHold(start_time);
                QAGESAStat.incRequests(this.clock());
                this.repeatedRandomRequest();
                QAGESAStat.decRequests(this.clock());
        }
    }
    
    private void repeatedRandomRequest() {
        if (this.isRepeated()) {
            ReFPlayReply playReply = this.playRequest(this.getRepeatedMovieTag());
        } else {
            String movieTag = MuMService.getMUMTranscodingSet().selectRandomTag();
            ReFPlayReply playReply = this.playRequest(movieTag);
        }
    }
    
    public Sim_stat getStatResponseTime() {
        return statResponseTime;
    }
    
    public void setStatResponseTime(Sim_stat statResponseTime) {
        this.statResponseTime = statResponseTime;
    }
    
    public int getNumRequests() {
        return numRequests;
    }
    
    public void setNumRequests(int numRequests) {
        this.numRequests = numRequests;
    }
    
    private String getRepeatedMovieTag() {
        return repeatedMovieTag;
    }
    
    private void setRepeatedMovieTag(String repeatedMovieTag) {
        this.repeatedMovieTag = repeatedMovieTag;
    }
    
    private boolean isRepeated() {
        return repeated;
    }
    
    private void setRepeated(boolean repeated) {
        this.repeated = repeated;
    }
    
    public boolean isRandomSelection() {
        return randomSelection;
    }
    
    public void setRandomSelection(boolean randomSelection) {
        this.randomSelection = randomSelection;
    }

    public Sim_stat getStatStreaming() {
        return statStreaming;
    }

    public void setStatStreaming(Sim_stat statStreaming) {
        this.statStreaming = statStreaming;
    }

    public int getSelectedMeasure() {
        return selectedMeasure;
    }

    public void setSelectedMeasure(int selectedMeasure) {
        this.selectedMeasure = selectedMeasure;
    }
    
	private void pingCEs() {
            for (int i=0;i<this.getVirtualOrganization().getNumCEs();i++) {
                int ceID = Sim_system.get_entity("CE_"+i).get_id();
		InfoPacket pkt = super.pingBlockingCall(ceID,
				50);
		if (pkt != null) {
                        this.getVirtualOrganization().getAgentPlatform().getNetworkMonitor().getNetworkMap().addRTT(ceID,this.get_id(),pkt);
		}
            }
	}

    public Sim_stat getStatFirst() {
        return statFirst;
    }

    public void setStatFirst(Sim_stat statFirst) {
        this.statFirst = statFirst;
    }
}
