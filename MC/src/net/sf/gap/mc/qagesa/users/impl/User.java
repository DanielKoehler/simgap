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
 * $Id$
 *
 */
package net.sf.gap.mc.qagesa.users.impl;

import java.util.List;

import net.sf.gap.GAP;
import net.sf.gap.agents.predicates.Predicate;
import net.sf.gap.mc.QAGESA;
import net.sf.gap.mc.qagesa.agents.services.impl.MuMService;
import net.sf.gap.mc.qagesa.constants.QAGESAEntityTypes;
import net.sf.gap.mc.qagesa.constants.QAGESATags;
import net.sf.gap.mc.qagesa.messages.*;
import net.sf.gap.mc.qagesa.stats.QAGESAStat;
import net.sf.gap.mc.qagesa.users.QAGESAUser;
import eduni.simjava.Sim_event;
import eduni.simjava.Sim_stat;
import eduni.simjava.Sim_system;
import net.sf.gap.distributions.Uniform_int;
import gridsim.GridSim;
import gridsim.GridSimTags;
import gridsim.IO_data;
import gridsim.net.InfoPacket;
import gridsim.net.Link;

/**
 * @author Giovanni Novelli
 */
public class User extends QAGESAUser {

    public static final int MEASURE_RESPONSE = 1;
    public static final int MEASURE_STREAMING = 2;
    public static final int MEASURE_FIRST = 3;
    private static String distribution = "";
    private static Uniform_int rand = new Uniform_int("prob_ql");

    public static String getDistribution() {
        return distribution;
    }

    public static void setDistribution(String aDistribution) {
        distribution = aDistribution;
    }
    private int selectedMeasure;
    private int uid;
    /**
     * 
     * Sim_stat object used to measure SERVICE_TIME between REF_PLAY_REQ and REF_PLAY_REPLY
     */
    private Sim_stat statResponseTime;
    /**
     * 
     * Sim_stat object used to measure SERVICE_TIME between SENDING_FIRST_CHUNK_REP and SENT_LAST_CHUNK_REP
     */
    private Sim_stat statStreaming;
    /**
     * 
     * Sim_stat object used to measure SERVICE_TIME between SENDING_FIRST_CHUNK_REP and 1st SEND_CHUNK_REQ
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
                this.setupStatResponseTime();
                break;
            case User.MEASURE_STREAMING:
                this.setupStatStreaming();
                break;
            case User.MEASURE_FIRST:
                this.setupStatFirst();
                break;
        }
    }

    private void setupStatResponseTime() {
        Sim_stat stat = new Sim_stat();
        int[] tags = {QAGESATags.REF_PLAY_REP_START, QAGESATags.SENDING_FIRST_CHUNK_REP};
        stat.measure_for(tags);
        stat.add_measure(Sim_stat.SERVICE_TIME);
        this.set_stat(stat);
        this.setStatResponseTime(stat);
    }

    private void setupStatStreaming() {
        Sim_stat stat = new Sim_stat();
        int[] tags = {QAGESATags.REF_PLAY_REP_START, QAGESATags.SENDING_FIRST_CHUNK_REP, QAGESATags.SEND_CHUNK_REQ, QAGESATags.SENT_LAST_CHUNK_REP};
        stat.measure_for(tags);
        stat.add_measure(Sim_stat.SERVICE_TIME);
        this.set_stat(stat);
        this.setStatStreaming(stat);
    }

    private void setupStatFirst() {
        Sim_stat stat = new Sim_stat();
        int[] tags = {QAGESATags.REF_PLAY_REP_START, QAGESATags.SENDING_FIRST_CHUNK_REP, QAGESATags.TRANSCODED_FIRST_CHUNK_REP};
        stat.measure_for(tags);
        stat.add_measure(Sim_stat.SERVICE_TIME);
        this.set_stat(stat);
        this.setStatFirst(stat);
    }

    @SuppressWarnings("unchecked")
    private void printStatFirst() {
        List[] list = this.getStatFirst().get_data();
        List datas = list[1];
        List data = (List) datas.toArray()[0];
        int nd = data.size();
        int rep = QAGESAStat.getReplication();
        int nu = QAGESAStat.getNumUsers();
        int ca = 0;
        if (QAGESAStat.isCachingEnabled()) {
            ca = 1;
        }
        int wm = QAGESAStat.getWhichMeasure();
        for (int i = 0; i < nd; i++) {
            double[] times = (double[]) data.toArray()[i];
            //QAGESA.outUSER.println("CSV;USERS_FT;" + QAGESAStat.getReplication() + ";" + QAGESAStat.getNumUsers() + ";" + QAGESAStat.isCachingEnabled() + ";" + QAGESAStat.getWhichMeasure() + ";" + this.get_name() + ";" + (times[0]-QAGESA.getStartTime()) + ";" + (times[1]-QAGESA.getStartTime()) + ";" + (times[1] - times[0]));
            double ta = (times[0] - QAGESA.getStartTime());
            double tb = (times[1] - QAGESA.getStartTime());
            double dt = (times[1] - times[0]);
            double perct = dt/(12*QAGESA.expansion*1.0);
            QAGESA.outUSER.printf(
                    "CSV\tUSERS_FT\t%2d\t%4d\t%d\t%d\t%s\t%6.4f\t%6.4f\t%6.6f\t%1.4f\n",
                    rep,
                    nu,
                    ca,
                    wm,
                    this.get_name(),
                    ta,
                    tb,
                    dt,
                    perct);
        }
    }

    @SuppressWarnings("unchecked")
    private void printStatStreaming() {
        List[] list = this.getStatStreaming().get_data();
        List datas = list[1];
        List data = (List) datas.toArray()[0];
        int nd = data.size();
        int rep = QAGESAStat.getReplication();
        int nu = QAGESAStat.getNumUsers();
        int ca = 0;
        if (QAGESAStat.isCachingEnabled()) {
            ca = 1;
        }
        int wm = QAGESAStat.getWhichMeasure();
        for (int i = 0; i < nd; i++) {
            double[] times = (double[]) data.toArray()[i];
            //QAGESA.outUSER.println("CSV;USERS_ST;" + QAGESAStat.getReplication() + ";" + QAGESAStat.getNumUsers() + ";" + QAGESAStat.isCachingEnabled() + ";" + QAGESAStat.getWhichMeasure() + ";" + this.get_name() + ";" + (times[0]-QAGESA.getStartTime()) + ";" + (times[1]-QAGESA.getStartTime()) + ";" + (times[1] - times[0]));
            double ta = (times[0] - QAGESA.getStartTime());
            double tb = (times[1] - QAGESA.getStartTime());
            double dt = (times[1] - times[0]);
            double perct = dt/(12*QAGESA.expansion*1.0);
            QAGESA.outUSER.printf(
                    "CSV\tUSERS_ST\t%2d\t%4d\t%d\t%d\t%s\t%6.4f\t%6.4f\t%6.4f\t%1.4f\n",
                    rep,
                    nu,
                    ca,
                    wm,
                    this.get_name(),
                    ta,
                    tb,
                    dt,
                    perct);
        }
    }

    @SuppressWarnings("unchecked")
    private void printStatResponseTime() {
        List[] list = this.getStatResponseTime().get_data();
        List datas = list[1];
        List data = (List) datas.toArray()[0];
        int nd = data.size();
        int rep = QAGESAStat.getReplication();
        int nu = QAGESAStat.getNumUsers();
        int ca = 0;
        if (QAGESAStat.isCachingEnabled()) {
            ca = 1;
        }
        int wm = QAGESAStat.getWhichMeasure();
        for (int i = 0; i < nd; i++) {
            double[] times = (double[]) data.toArray()[i];
            //QAGESA.outUSER.println("CSV;USERS_RT;" + QAGESAStat.getReplication() + ";" + QAGESAStat.getNumUsers() + ";" + QAGESAStat.isCachingEnabled() + ";" + QAGESAStat.getWhichMeasure() + ";" + this.get_name() + ";" + (times[0]-QAGESA.getStartTime()) + ";" + (times[1]-QAGESA.getStartTime()) + ";" + (times[1] - times[0]));
            double ta = (times[0] - QAGESA.getStartTime());
            double tb = (times[1] - QAGESA.getStartTime());
            double dt = (times[1] - times[0]);
            double perct = dt/(12*QAGESA.expansion*1.0);
            QAGESA.outUSER.printf(
                    "CSV\tUSERS_RT\t%2d\t%4d\t%d\t%d\t%s\t%6.4f\t%6.4f\t%6.4f\t%1.4f\n",
                    rep,
                    nu,
                    ca,
                    wm,
                    this.get_name(),
                    ta,
                    tb,
                    dt,
                    perct);
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

    private void askReF(String movieTag) {
        int SIZE = 500;
        double evsend_time = User.clock();
        int userID = this.get_id();
        //double acceptableQualityLoss = rand.sample(69) * 0.01;
        double acceptableQualityLoss =QAGESA.qosloss;
        QAGESAStat.updateAcceptableQualityLoss(acceptableQualityLoss);
        double minQuality = 1.0 - acceptableQualityLoss;
        ReFPlayRequest request = new ReFPlayRequest(this.get_id(), this.get_id(), userID, movieTag, minQuality, this.isRandomSelection());
        request.setRequestTime(evsend_time);
        @SuppressWarnings("unused")
        int requestID = request.getRequestID();
        int reqrepID = request.getReqrepID();
        super.send(super.output, GridSimTags.SCHEDULE_NOW,
                QAGESATags.REF_PLAY_REQ, new IO_data(request, SIZE, QAGESAUser.getEntityId("ReF")));
        evsend_time = GridSim.clock();
        String msg = String.format(
                "%1$f %2$d %3$s --> ReF REF_PLAY_REQUEST %4$s", evsend_time,
                reqrepID, this.get_name(), movieTag);
        this.write(msg);

        Sim_event ev = new Sim_event();
        Predicate predicate = new Predicate(QAGESATags.REF_PLAY_REP_START);
        super.sim_get_next(predicate, ev); // only look for this type of ack
        ReFPlayReply playReply = ReFPlayReply.get_data(ev);
        playReply.setReplyEv(ev);
        request = playReply.getRequest();
        double evrecv_time = GridSim.clock();
        request.setReplyTime(evrecv_time);
        if (playReply.isOk()) {
            msg = String.format(
                    "%1$f %2$d %3$s <-- ReF REF_PLAY_REPLY_START (SUCCESS) %4$d %5$s",
                    evrecv_time, request.getReqrepID(), this.get_name(),
                    request.getReqrepID(),
                    request.getMovieTag());
            ChunkRequest askChunkRequest =
                    new ChunkRequest(
                    this.get_id(),
                    this.get_id(),
                    request.getReqrepID(),
                    this.get_id(),
                    request.getMovieTag(),
                    1,
                    request.getTranscodeRequest().getStorageElementID(),
                    request.getTranscodeRequest(),
                    this.clock());
            super.send(super.output, GridSimTags.SCHEDULE_NOW,
                    QAGESATags.ASK_CHUNK_REQ, new IO_data(askChunkRequest, 32, playReply.getAgentID()));
        } else {
            msg = String.format(
                    "%1$f %2$d %3$s <-- ReF REF_PLAY_REPLY_START (FAILURE) %4$d %5$s",
                    evrecv_time, request.getReqrepID(), this.get_name(),
                    request.getReqrepID(),
                    request.getMovieTag());
            if (this.getSelectedMeasure() == User.MEASURE_RESPONSE) {
                sim_completed(playReply.getReplyEv());
            }
        }
        this.write(msg);
    }

    protected void playRequest(String movieTag) {
        askReF(movieTag);
    }

    @Override
    public void processOtherEvent(Sim_event ev) {
        switch (ev.get_tag()) {
            case QAGESATags.TRANSCODED_FIRST_CHUNK_REP:
                ChunkRequest chunkRequest = ChunkRequest.get_data(ev);
                int treqrepID = chunkRequest.getReqrepID();
                double tevrecv_time = GridSim.clock();
                String tmsg = String.format(
                        "%1$f %2$d %3$s <-- %4$s TRANSCODED_FIRST_CHUNK %5$d %6$d",
                        tevrecv_time, treqrepID, this.get_name(),
                        Sim_system.get_entity(chunkRequest.getSrc_ID()).get_name(),
                        chunkRequest.getPlayReqrepID(),
                        chunkRequest.getSequenceNumber());
                this.write(tmsg);
                if (this.getSelectedMeasure() == User.MEASURE_FIRST) {
                    sim_completed(chunkRequest.getTranscodeRequest().getPlayRequest().getReplyEv());
                }

                break;
            case QAGESATags.SENDING_FIRST_CHUNK_REP:
                chunkRequest = ChunkRequest.get_data(ev);
                chunkRequest.getTranscodeRequest().getPlayRequest().setFcEv(ev);
                int reqrepID = chunkRequest.getReqrepID();
                double evrecv_time = GridSim.clock();
                chunkRequest.getTranscodeRequest().getPlayRequest().setFcTime(evrecv_time);
                String msg = String.format(
                        "%1$f %2$d %3$s <-- %4$s SENDING_FIRST_CHUNK %5$d %6$d",
                        evrecv_time, reqrepID, this.get_name(),
                        Sim_system.get_entity(chunkRequest.getSrc_ID()).get_name(),
                        chunkRequest.getPlayReqrepID(),
                        chunkRequest.getSequenceNumber());
                this.write(msg);
                if (this.getSelectedMeasure() == User.MEASURE_RESPONSE) {
                    sim_completed(chunkRequest.getTranscodeRequest().getPlayRequest().getReplyEv());
                }
                break;
            case QAGESATags.SENT_LAST_CHUNK_REP:
                chunkRequest = ChunkRequest.get_data(ev);
                @SuppressWarnings("unused") int playReqrepID = chunkRequest.getPlayReqrepID();
                reqrepID = chunkRequest.getReqrepID();
                evrecv_time = GridSim.clock();
                chunkRequest.getTranscodeRequest().getPlayRequest().setLcTime(evrecv_time);
                msg = String.format(
                        "%1$f %2$d %3$s <-- %4$s SENT_LAST_CHUNK %5$d",
                        evrecv_time, reqrepID, this.get_name(),
                        Sim_system.get_entity(chunkRequest.getSrc_ID()).get_name(),
                        chunkRequest.getPlayReqrepID());
                this.write(msg);
                if (this.getSelectedMeasure() == User.MEASURE_STREAMING) {
                    sim_completed(chunkRequest.getTranscodeRequest().getPlayRequest().getReplyEv());
                }
                QAGESAStat.decRequests(ev.event_time(), true);
                break;
            case QAGESATags.SEND_CHUNK_REQ:
                chunkRequest = ChunkRequest.get_data(ev);
                reqrepID = chunkRequest.getReqrepID();
                evrecv_time = GridSim.clock();
                chunkRequest.getTranscodeRequest().getPlayRequest().incStreamedBytes(chunkRequest.getChunk().getOutputSize());
                msg = String.format(
                        "%1$f %2$d %3$s <-- %4$s SEND_CHUNK %5$d %6$d",
                        evrecv_time, reqrepID, this.get_name(),
                        Sim_system.get_entity(chunkRequest.getSrc_ID()).get_name(),
                        chunkRequest.getPlayReqrepID(),
                        chunkRequest.getSequenceNumber());
                this.write(msg);

                int rep = QAGESAStat.getReplication();
                int nu = QAGESAStat.getNumUsers();
                int ca = 0;
                if (QAGESAStat.isCachingEnabled()) {
                    ca = 1;
                }
                int wm = QAGESAStat.getWhichMeasure();
                double time = User.clock();
                double quality = chunkRequest.getChunk().getQuality();
                double minQuality = chunkRequest.getTranscodeRequest().getMinQuality();
                double qualityLoss = 1.0-quality;
                double aQL = 1.0 - minQuality;
                long size = chunkRequest.getChunk().getOutputSize();
                long MIPS = chunkRequest.getChunk().getMIPS();
                int sn = chunkRequest.getChunk().getSequenceNumber();
                double streamQuality = chunkRequest.getTranscodeRequest().updateQualityMean();
                int playreqrepID = chunkRequest.getPlayReqrepID();
                long fetchedBytes = chunkRequest.getTranscodeRequest().getPlayRequest().getFetchedBytes();
                long streamedBytes = chunkRequest.getTranscodeRequest().getPlayRequest().getStreamedBytes();
                QAGESA.outUSER_QoS.printf(
                        "CSV\tUSERS_QoS\t%2d\t%4d\t%1d\t%1d\t%12s\t%6.4f\t%12d\t%2d\t%6d\t%4d\t%1.4f\t%1.4f\t%1.4f\t%1.4f\t%1.4f\t%12d\t%12d\n",
                        rep,
                        nu,
                        ca,
                        wm,
                        this.get_name(),
                        time,
                        playreqrepID,
                        sn,
                        size,
                        MIPS,
                        quality,
                        minQuality,
                        qualityLoss,
                        aQL,
                        streamQuality,
                        fetchedBytes,
                        streamedBytes);
                
                 ChunkReply chunkReply = new ChunkReply(QAGESATags.SEND_CHUNK_REP, true, chunkRequest, chunkRequest.getChunk());
                super.send(super.output, GridSimTags.SCHEDULE_NOW,
                        QAGESATags.SEND_CHUNK_REP, new IO_data(chunkReply, 32, chunkRequest.getSrc_ID()));
                break;
            case QAGESATags.REF_PLAY_REP_END:
                 ReFPlayReply playReply = ReFPlayReply.get_data(ev);
                 ReFPlayRequest request = playReply.getRequest();
                evrecv_time = GridSim.clock();
                if (playReply.isOk()) {
                    msg = String.format(
                            "%1$f %2$d %3$s <-- ReF REF_PLAY_REPLY_END (SUCCESS) %4$d %5$s",
                            evrecv_time, request.getReqrepID(), this.get_name(),
                            request.getReqrepID(),
                            request.getMovieTag());
                } else {
                    msg = String.format(
                            "%1$f %2$d %3$s <-- ReF REF_PLAY_REPLY_END (FAILURE) %4$d %5$s",
                            evrecv_time, request.getReqrepID(), this.get_name(),
                            request.getReqrepID(),
                            request.getMovieTag());
                    //QAGESAStat.incViolations(1.0);
                    QAGESAStat.decRequests(ev.event_time(), false);
                }
                this.write(msg);
                asked--;
                request.setEndTime(evrecv_time);
                
                if (!request.getTranscodeRequest().isAborted()) {
                    rep = QAGESAStat.getReplication();
                    nu = QAGESAStat.getNumUsers();
                    ca = 0;
                    if (QAGESAStat.isCachingEnabled()) {
                        ca = 1;
                    }
                    wm = QAGESAStat.getWhichMeasure();
                    time = User.clock()-QAGESA.getStartTime();
                    playreqrepID = request.getReqrepID();
                    minQuality = request.getMinQuality();
                    double requestTime = request.getRequestTime();
                    double replyTime = request.getReplyTime();
                    double fcTime = request.getFcTime();
                    double lcTime = request.getLcTime();
                    double endTime = request.getEndTime();

                    double responseTime = replyTime-requestTime;
                    double firstChunkTime = fcTime-requestTime;
                    double streamingTime = endTime-fcTime;

                    double duration=0.0;
                    int sequenceSize=0;
                    double chunkDuration=0.0;
                    double normalizedStreamingTime=0.0;
                    double meanDelay=0.0;
                    double normalizedMeanDelay=0.0;
                    double delay=0.0;
                    if (playReply.isOk()) {
                        sequenceSize = request.getTranscodeRequest().getSequence().size();
                        duration = request.getTranscodeRequest().getSequence().getDuration()*0.001;
                        chunkDuration = duration/(sequenceSize*1.0);
                        normalizedStreamingTime = streamingTime/duration;
                        delay=streamingTime-duration;
                        meanDelay=delay/(sequenceSize*1.0);
                        normalizedMeanDelay = meanDelay/chunkDuration;
                        if (normalizedStreamingTime>QAGESA.normalizedViolationThreeshold) {
                            QAGESAStat.incViolations(1.0);
                        } else {
                            QAGESAStat.incViolations(0.0);
                        }
                    }
                    double normalizedMeanViolations = QAGESAStat.getViolations().getMean();

                    fetchedBytes = request.getFetchedBytes();
                    streamedBytes = request.getStreamedBytes();

                    int intimeStreams = QAGESAStat.getIntimeStreams();
                    int outtimeStreams = QAGESAStat.getOuttimeStreams();
                    int totalStreams = intimeStreams+outtimeStreams;
                    double normalizedIntimeStreams = (intimeStreams*1.0)/(totalStreams*1.0);
                    double normalizedOuttimeStreams = (outtimeStreams*1.0)/(totalStreams*1.0);
                    QAGESA.outUSER_Streaming.printf(
                            "CSV\tUSERS_Streaming\t%2d\t%4d\t%1d\t%1d\t%12s\t%6.4f\t%12d\t%1.4f\t%6.4f\t%6.4f\t%6.4f\t%6.4f\t%6.4f\t%12d\t%12d\t%6.4f\t%6.4f\t%6.4f\t%1.4f\t%6.4f\t%6.4f\t%1.4f\t%1.4f\t%12d\t%12d\t%1.4f\t%1.4f\n",
                            rep,
                            nu,
                            ca,
                            wm,
                            this.get_name(),
                            time,
                            playreqrepID,
                            minQuality,
                            requestTime,
                            replyTime,
                            fcTime,
                            lcTime,
                            endTime,
                            fetchedBytes,
                            streamedBytes,
                            firstChunkTime,
                            responseTime,
                            streamingTime,
                            normalizedStreamingTime,
                            delay,
                            meanDelay,
                            normalizedMeanDelay,
                            normalizedMeanViolations,
                            intimeStreams,
                            outtimeStreams,
                            normalizedIntimeStreams,
                            normalizedOuttimeStreams
                            );
                }
                break;
            default:
                break;
        }
    }
    static double[] probsUsers;
    static double[] probsRequests;
    static int nSeconds;
    static double step;
    static Uniform_int randuid;

    {
        double a = GAP.getStartTime();
        double b = GAP.getEndTime() + 1.0 - QAGESA.relaxTime;
        step = 1.0;
        nSeconds = (int) Math.round((b - a) / step);
        ZipF zipfUsers = new ZipF(nSeconds, QAGESA.thetaU);
        ZipF zipfRequests = new ZipF(nSeconds, QAGESA.thetaR);
        probsUsers = zipfUsers.getProbs();
        probsRequests = zipfRequests.getProbs();
        randuid = new Uniform_int("randuid");
    }
    private int asked;

    @Override
    public void initWork() {
        asked = 0;
        this.DoIt();
    }

    @Override
    public void doWork() {
        if (User.clock() < (GAP.getEndTime() - QAGESA.relaxTime)) {
            if (hastoask()) {
                asked++;
                QAGESAStat.incRequests(User.clock());
                this.repeatedRandomRequest();
            }
        }
    }

    private void DoIt() {
        this.pingCEs();
        if (this.isRepeated() && this.getRepeatedMovieTag() == null) {
            String movieTag = MuMService.getMUMTranscodingSet().selectRandomTag();
            this.setRepeatedMovieTag(movieTag);
        }
    }

    private boolean hastoask() {
        return hastoask(User.clock());
    }

    private boolean hastoask(double time) {
        boolean result;
        if (User.distribution.equalsIgnoreCase("zipf")) {
            double a = GAP.getStartTime();
            int i = (int) Math.round((time - a) / step);
            double utoask = probsUsers[i] * QAGESAStat.getNumUsers();
            double rtoask = Math.max(probsRequests[i] * this.numRequests, 1.0);
            double ntoask = utoask * rtoask;
            result = ((QAGESAStat.getRequests() + 0.5) < (ntoask)) && (this.asked < rtoask);
        } else {
            int neededRequests = fr(
                    GAP.getStartTime(),
                    GAP.getEndTime() - QAGESA.relaxTime,
                    QAGESAStat.getNumUsers(),
                    time);
            result = ((QAGESAStat.getRequests() < neededRequests) && (asked < this.numRequests));
        }
        return result;
    }

    private int fr(double a, double b, int maxusers, double currentTime) {
        int result = 0;
        if (User.distribution.equalsIgnoreCase("linear")) {
            result = linear(a, b, maxusers, currentTime);
        } else if (User.distribution.equalsIgnoreCase("scaled")) {
            result = scaled(a, b, maxusers, currentTime);
        } else if (User.distribution.equalsIgnoreCase("top")) {
            result = maxusers;
        } else {
            result = scaled(a, b, maxusers, currentTime);
        }
        return result;
    }

    private int linear(double a, double b, int maxusers, double currentTime) {
        int result = (int) Math.round(((currentTime - a) / (b - a)) * maxusers);
        return result;
    }

    private int scaled(double a, double b, int maxusers, double currentTime) {
        double center = (b - a) * QAGESA.linearDelta + a;
        int result;
        if (currentTime > center) {
            result = maxusers;
        } else {
            result = (int) Math.round(((currentTime - a) / (center - a)) * maxusers);
        }
        //System.out.println(a + " " + b + " " + center + " " + currentTime +": " + this.getUid() + " " + result + " with current " + QAGESAStat.getRequests());
        return result;
    }

    private void repeatedRandomRequest() {
        if (this.isRepeated()) {
            this.playRequest(this.getRepeatedMovieTag());
        } else {
            String movieTag = MuMService.getMUMTranscodingSet().selectRandomTag();
            this.playRequest(movieTag);
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
        for (int i = 0; i < this.getVirtualOrganization().getNumCEs(); i++) {
            int ceID = this.getVirtualOrganization().getCEs().get(i).get_id();
            InfoPacket pkt = super.pingBlockingCall(ceID,
                    50);
            if (pkt != null) {
                this.getVirtualOrganization().getPlatform().getNetworkMonitor().getNetworkMap().addRTT(ceID, this.get_id(), pkt);
            }
        }
    }

    public Sim_stat getStatFirst() {
        return statFirst;
    }

    public void setStatFirst(Sim_stat statFirst) {
        this.statFirst = statFirst;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }
}
