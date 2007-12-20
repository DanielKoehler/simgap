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
 * QAGESAUser.java
 *
 * Created on 13 August 2006, 20.24 by Giovanni Novelli
 *
 * $Id: QAGESAUser.java 1142 2007-07-18 20:49:58Z gnovelli $
 *
 */

package net.sf.qagesa.users;

import eduni.simjava.Sim_event;
import gridsim.GridSim;
import gridsim.GridSimTags;
import gridsim.IO_data;
import gridsim.net.Link;
import junit.framework.Assert;
import net.sf.gap.agents.predicates.Predicate;
import net.sf.gap.messages.impl.GISReply;
import net.sf.gap.messages.impl.GISRequest;
import net.sf.gap.users.AbstractUser;
import net.sf.qagesa.messages.MuMSearchReply;
import net.sf.qagesa.messages.MuMSearchRequest;
import net.sf.qagesa.constants.QAGESATags;

/**
 * 
 * @author Giovanni Novelli
 */
public abstract class QAGESAUser extends AbstractUser {

	/** Creates a new instance of QAGESAUser */
	public QAGESAUser(String name, Link link, int entityType, boolean trace_flag)
			throws Exception {
		super(name, link, entityType, trace_flag);
	}

	protected MuMSearchReply requestGEList(String repTag) {
		int SIZE = 500;
		double evsend_time = 0;
		MuMSearchRequest request = new MuMSearchRequest(this.get_id(), this
				.get_id(), repTag);
		int requestID = request.getRequestID();
		int reqrepID = request.getReqrepID();
		super.send(super.output, GridSimTags.SCHEDULE_NOW,
				QAGESATags.MUM_SEARCH_REQ, new IO_data(request, SIZE, AbstractUser.getEntityId("MuM")));
		evsend_time = GridSim.clock();
		String msg = String.format(
				"%1$f %2$d %3$s --> MuM MUM_SEARCH_REQUEST %4$s", evsend_time,
				reqrepID, this.get_name(), repTag);
		this.write(msg);

		Sim_event ev = new Sim_event();
		Predicate predicate = new Predicate(QAGESATags.MUM_SEARCH_REP);
		super.sim_get_next(predicate, ev); // only look for this type of ack
		MuMSearchReply reply = MuMSearchReply.get_data(ev);
		Assert.assertEquals(requestID, reply.getRequestID());
		Assert.assertEquals(QAGESATags.MUM_SEARCH_REQ, reply
				.getRequestTAG());
		Assert.assertEquals(QAGESATags.MUM_SEARCH_REP, ev.get_tag());
		double evrecv_time = GridSim.clock();
		if (reply.isOk()) {
			msg = String.format(
					"%1$f %2$d %3$s <-- MuM MUM_SEARCH_REPLY %4$s %5$s",
					evrecv_time, reqrepID, this.get_name(), repTag, reply
							.getGelist());
		} else {
			msg = String.format(
					"%1$f %2$d %3$s <-- MuM MUM_SEARCH_REPLY %4$s %5$s",
					evrecv_time, reqrepID, this.get_name(), repTag, "NONE");
		}
		this.write(msg);
		return reply;
	}

    @Override
	protected GISReply requestToGIS() {
		int SIZE = 500;
		double evsend_time = 0;
		GISRequest request = new GISRequest(this.get_id(), this.get_id());
		int requestID = request.getRequestID();
		int reqrepID = request.getReqrepID();
		super.send(super.output, GridSimTags.SCHEDULE_NOW,
				QAGESATags.CEL_SEARCH_REQ, new IO_data(request, SIZE, AbstractUser
						.getEntityId("CEL")));
		evsend_time = GridSim.clock();
		String msg = String.format("%1$f %2$d %3$s --> CEL CEL_SEARCH_REQUEST",
				evsend_time, reqrepID, this.get_name());
		this.write(msg);

		Sim_event ev = new Sim_event();
		Predicate predicate = new Predicate(QAGESATags.CEL_SEARCH_REP);
		super.sim_get_next(predicate, ev); // only look for this type of ack
		GISReply reply = GISReply.get_data(ev);
		Assert.assertEquals(requestID, reply.getRequestID());
		Assert.assertEquals(QAGESATags.CEL_SEARCH_REQ, reply
				.getRequestTAG());
		Assert.assertEquals(QAGESATags.CEL_SEARCH_REP, ev.get_tag());
		double evrecv_time = GridSim.clock();
		this.write(evrecv_time + ": " + this.get_name() + " <-- GIS:\n"
				+ reply.getGIS());
		return reply;
	}

	@Override
	public abstract void processOtherEvent(Sim_event ev);
}
