/*
 ****************************************************************************************
 * Copyright © Giovanni Novelli                                             
 * All Rights Reserved.                                                                 
 ****************************************************************************************
 *
 * Title:        GAP Simulator
 * Description:  GAP (Grid Agents Platform) Toolkit for Modeling and Simulation
 *               of Mobile Agents on Grids
 * License:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * Measure.java
 *
 * Created on 29 March 2007, 12.00 by Giovanni Novelli
 *
 ****************************************************************************************
 *
 * $Revision: 1141 $
 * $Id: Measure.java 1141 2007-07-18 18:22:15Z gnovelli $
 * $HeadURL: file:///var/svn/grid/trunk/GAP/src/net/sf/gap/measures/Measure.java $
 *
 *****************************************************************************************
 */


package net.sf.gap.measures;

import eduni.simjava.Sim_event;
import eduni.simjava.Sim_stat;
import eduni.simjava.Sim_system;

import gridsim.GridSim;

import java.util.concurrent.ConcurrentHashMap;

import net.sf.gap.GAP;
import net.sf.gap.messages.MeasureReply;
import net.sf.gap.messages.MeasureRequest;

/**
 *
 * @author Giovanni Novelli
 */
public class Measure extends GridSim {
    private int initialTag;
    private int finalTag;
    
    private EventsMap eventsMap;
    
    private Sim_stat stat;
    
    /** Creates a new instance of Measure */
    public Measure(String name, int initialTag, int finalTag) throws Exception {
        super(name);
        this.setInitialTag(initialTag);
        this.setFinalTag(finalTag);
        this.setupStat();
    }
    
    public void initialize() {
        this.setEventsMap(new EventsMap());
    }
    
    private void setupStat() {
        Sim_stat stat = new Sim_stat();
        int[] tags = { this.getInitialTag(), this.getFinalTag() };
        stat.measure_for(tags);
        stat.add_measure(Sim_stat.SERVICE_TIME);
        stat.add_measure(Sim_stat.ARRIVAL_RATE);
        stat.add_measure(Sim_stat.QUEUE_LENGTH);
        stat.add_measure(Sim_stat.RESIDENCE_TIME);
        stat.add_measure(Sim_stat.THROUGHPUT);
        stat.add_measure(Sim_stat.UTILISATION);
        stat.add_measure(Sim_stat.WAITING_TIME);
        this.set_stat(stat);
        this.setStat(stat);
    }

	@Override
	public void body() {
		super.gridSimHold(GAP.getStartTime());

		Sim_event ev = new Sim_event();
		while (GAP.isRunning()) {
			super.sim_wait_for(Sim_system.SIM_ANY, 1.0, ev);
			this.processEvent(ev);
			while (super.sim_waiting() > 0) {
				this.processEvents();
			}
		}

		// //////////////////////////////////////////////////////
		// shut down I/O ports
		this.shutdownUserEntity();
		this.terminateIOEntities();
	}

    protected void processEvent(Sim_event ev) {
            int tag = ev.get_tag();
            if (tag==this.getInitialTag()) {
                // Do nothing
                MeasureRequest request = MeasureRequest.get_data(ev);
                request.setEvent(ev);
                this.getEventsMap().put(request.getID(),request);
            } 
            if (tag==this.getFinalTag()) {
                MeasureReply reply = MeasureReply.get_data(ev);
                MeasureRequest request = this.getEventsMap().get(reply.getID());
                double delay = super.clock()-request.getTimestamp();
                sim_process(delay);
                sim_completed(request.getEvent());
                sim_completed(ev);
            } 
    }

    protected void processEvents() {
            Sim_event ev = new Sim_event();

            super.sim_get_next(ev);
            this.processEvent(ev);
    }

    public int getInitialTag() {
        return initialTag;
    }

    public void setInitialTag(int initialTag) {
        this.initialTag = initialTag;
    }

    public int getFinalTag() {
        return finalTag;
    }

    public void setFinalTag(int finalTag) {
        this.finalTag = finalTag;
    }

    public Sim_stat getStat() {
        return stat;
    }

    public void setStat(Sim_stat stat) {
        this.stat = stat;
    }

    public EventsMap getEventsMap() {
        return eventsMap;
    }

    public void setEventsMap(EventsMap eventsMap) {
        this.eventsMap = eventsMap;
    }
}
