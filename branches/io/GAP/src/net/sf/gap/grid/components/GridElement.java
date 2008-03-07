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
 * GridElement.java
 *
 * Created on 21 December 2007, 09.00 by Giovanni Novelli
 *
 * $Id$
 *
 */

package net.sf.gap.grid.components;

import eduni.simjava.Sim_event;
import gridsim.Accumulator;
import gridsim.ResourceCalendar;
import gridsim.ResourceCharacteristics;
import gridsim.datagrid.ReplicaManager;
import gridsim.net.Link;

/**
 * 
 * @author Giovanni Novelli
 */
public class GridElement extends AbstractGridElement {
    private Accumulator inputBytes;
    private Accumulator outputBytes;
    private Accumulator totalBytes;
    private Accumulator IOLoad;
    protected static final double mbFactor = 0.000001;
    private double baudrate;
        
	/** Creates a new instance of StorageElement */
	public GridElement(String name, Link link,
			ResourceCharacteristics resourceCharacteristics,
			ResourceCalendar resourceCalendar, ReplicaManager replicaManager)
			throws Exception {
		super(name, link, resourceCharacteristics, resourceCalendar,
				replicaManager);
                this.setInputBytes(new Accumulator());
                this.setOutputBytes(new Accumulator());
                this.setTotalBytes(new Accumulator());
                this.setBaudrate(link.getBaudRate());
	}

	@Override
	protected void processCustomEvents(Sim_event ev) {
		switch (ev.get_tag()) {
		default:
			break;
		}
	}

    public Accumulator getInputBytes() {
        return inputBytes;
    }

    public Accumulator getOutputBytes() {
        return outputBytes;
    }

    public Accumulator getTotalBytes() {
        return totalBytes;
    }

    public void incInputBytes(long inc, double time) {
        getInputBytes().add(inc*mbFactor);
        this.incTotalBytes(inc*mbFactor);
        this.reportIO(time);
    }
    
    public void incOutputBytes(long inc, double time) {
        getOutputBytes().add(inc*mbFactor);
        this.incTotalBytes(inc*mbFactor);
        this.reportIO(time);
    }
    
    private void incTotalBytes(double inc) {
        getTotalBytes().add(inc);
        //this.updateLoad();
    }
    
    protected void reportIO(double time) {}

    public void setInputBytes(Accumulator inputBytes) {
        this.inputBytes = inputBytes;
    }

    public void setOutputBytes(Accumulator outputBytes) {
        this.outputBytes = outputBytes;
    }

    public void setTotalBytes(Accumulator totalBytes) {
        this.totalBytes = totalBytes;
    }

    public double getBaudrate() {
        return baudrate;
    }

    public void setBaudrate(double baudrate) {
        this.baudrate = baudrate;
    }

    public Accumulator getIOLoad() {
        return IOLoad;
    }

    public void updateLoad() {
        this.getIOLoad().add(this.getLoad());
    }
    
    public void setIOLoad(Accumulator IOLoad) {
        this.IOLoad = IOLoad;
    }

    public double getLoad() {
      double load = (this.getTotalBytes().getMean()*8.0)/(this.getBaudrate()*mbFactor);
      return load;
    }
}
