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
 * StorageElement.java
 *
 * Created on 9 August 2006, 14.14 by Giovanni Novelli
 *
 * $Id$
 *
 */

package net.sf.gap.mc.qagesa.grid.components;

import eduni.simjava.Sim_event;
import gridsim.Accumulator;
import gridsim.GridSimTags;
import gridsim.IO_data;
import gridsim.ResourceCalendar;
import gridsim.ResourceCharacteristics;
import gridsim.datagrid.ReplicaManager;
import gridsim.net.Link;
import net.sf.gap.grid.components.GridElement;
import net.sf.gap.mc.QAGESA;
import net.sf.gap.mc.qagesa.constants.QAGESATags;
import net.sf.gap.mc.qagesa.messages.ChunkReply;
import net.sf.gap.mc.qagesa.messages.ChunkRequest;
import net.sf.gap.mc.qagesa.messages.TranscodeRequest;
import net.sf.gap.mc.qagesa.multimedia.Chunk;
import net.sf.gap.mc.qagesa.multimedia.ChunksSequence;
import net.sf.gap.mc.qagesa.multimedia.TranscodingSet;
import net.sf.gap.mc.qagesa.stats.QAGESAStat;

/**
 * 
 * @author Giovanni Novelli
 */
public class QAGESAGridElement extends GridElement {
	private TranscodingSet transcodingSet;
        
	/** Creates a new instance of StorageElement */
	public QAGESAGridElement(String name, Link link,
			ResourceCharacteristics resourceCharacteristics,
			ResourceCalendar resourceCalendar, ReplicaManager replicaManager)
			throws Exception {
		super(name, link, resourceCharacteristics, resourceCalendar,
				replicaManager);
		this.setTranscodingSet(new TranscodingSet());
	}

	@Override
	protected void processCustomEvents(Sim_event ev) {
		switch (ev.get_tag()) {
		case QAGESATags.GET_CHUNK_REQ:
                    ChunkRequest request = ChunkRequest.get_data(ev);
                    ChunksSequence sequence = this.getTranscodingSet().get(request.getMovieTag());
                    Chunk chunk = sequence.get(request.getSequenceNumber()-1);
                    ChunkReply reply = new ChunkReply(ev.get_tag(), true, request, chunk);
                    incOutputBytes(chunk.getInputSize());
                    incTotalBytes(chunk.getInputSize());
                    this.reportIO(ev.event_time());
                    //super.send(super.output, GridSimTags.SCHEDULE_NOW,
                    //                QAGESATags.GET_CHUNK_REP, new IO_data(reply, SIZE, request.getSrc_ID()));
                    super.send(super.output, GridSimTags.SCHEDULE_NOW,
                                    QAGESATags.GET_CHUNK_REP, new IO_data(reply, chunk.getOutputSize(), request.getSrc_ID()));
			break;

		case QAGESATags.CACHE_CHUNKS_REQ:
                    TranscodeRequest cacheRequest = TranscodeRequest.get_data(ev);
                    ChunksSequence transcodedSequence = cacheRequest.getSequence();
                    String md5 = transcodedSequence.getMessageDigest();
                    this.addSequence(md5,transcodedSequence);
                    incInputBytes(transcodedSequence.getOutputSize());
                    incTotalBytes(transcodedSequence.getOutputSize());
                    this.reportIO(ev.event_time());
			break;

		default:
			break;
		}
	}
        
        private void reportIO(double time) {
            int rep=QAGESAStat.getReplication();
            int nu = QAGESAStat.getNumUsers();
            int ca=0;
            if (QAGESAStat.isCachingEnabled()) {
                ca=1;
            }
            int wm = QAGESAStat.getWhichMeasure();
            QAGESA.outSE_IO.printf
                    ("CSV\tSE_IO\t%2d\t%4d\t%d\t%d\t%6.2f\t%s\t%f\t%f\t%f\t%f\t%f\t%f\t%f\n",
                    rep,
                    nu,
                    ca,
                    wm,
                    time,
                    this.get_name(),
                    this.getInputBytes().getSum(),
                    this.getOutputBytes().getSum(),
                    this.getTotalBytes().getSum(),
                    this.getInputBytes().getMean(),
                    this.getOutputBytes().getMean(),
                    this.getTotalBytes().getMean(),
                    this.getLoad()
                    );
        }

	public boolean addSequence(String movieTag, ChunksSequence sequence) {
		if (this.isSE() && sequence != null) {
			this.getTranscodingSet().addSequence(movieTag, sequence);
			return true;
		} else {
			return false;
		}
	}

	public boolean containsSequence(String movieTag) {
		return this.getTranscodingSet().containsSequence(movieTag);
	}

	public boolean delSequence(String movieTag) {
		if (this.containsSequence(movieTag)) {
			this.getTranscodingSet().remove(movieTag);
			return true;
		} else {
			return false;
		}
	}

	public TranscodingSet getTranscodingSet() {
		return transcodingSet;
	}

	public void setTranscodingSet(TranscodingSet transcodingSet) {
		this.transcodingSet = transcodingSet;
	}

    public synchronized double getLoad() {
      double load = (this.getTotalBytes().getMean()*8.0)/(this.getBaudrate()*mbFactor);
      return load;
    }
}
