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
 * PluggableAgent.java
 *
 * Created on 8 January 2008, 09.28 by Giovanni Novelli
 *
 * $Id$
 *
 */

package net.sf.gap.agents;

import eduni.simjava.Sim_port;

import net.sf.gap.agents.behaviours.Sim_eventListener;
import net.sf.gap.agents.behaviours.Sim_eventListenerList;
import net.sf.gap.grid.components.AbstractGridElement;
import eduni.simjava.Sim_event;

/**
 * 
 * This class is responsible for abstracting new behaviours plugging
 * 
 * @author Giovanni Novelli
 */
public abstract class PluggableAgent extends GridAgent {
	private Sim_eventListenerList evlList;

	/**
	 * Creates a new instance of PluggableAgent class
	 * 
	 * @param name
	 *            agent name
	 * @param agentSizeInBytes
	 *            agent size in bytes
	 * @param trace_flag
	 *            trace flag for GridSim
	 * @throws Exception
	 */
	public PluggableAgent(AbstractGridElement ge, String name,
			int agentSizeInBytes, boolean trace_flag) throws Exception {
		super(ge, name, agentSizeInBytes, trace_flag);
	}

	public Sim_eventListener addListener(Sim_eventListener evListener) {
		return this.getEvlList().addListener(evListener.getTag(), evListener);
	}

	public Sim_eventListener removeListener(int tag) {
		return this.getEvlList().removeListener(tag);
	}

	/**
	 * PluggableAgent's Initialization
	 * 
	 * @throws java.lang.Exception
	 */
	@Override
	public void initialize() throws Exception {
		super.initialize();
		this.setEvlList(new Sim_eventListenerList());
		this.addListeners();
	}

	public abstract void addListeners();

	/**
	 * Method used to process extended agent's behaviour
	 * 
	 * @param ev
	 *            Event triggering agent's actions
	 */
	public void processOtherEvent(Sim_event ev) {
		Sim_eventListener listener = this.getEvlList().get(ev.get_tag());

		if (listener != null) {
			listener.eventOccurred(ev);
		}
	}

	public Sim_eventListenerList getEvlList() {
		return evlList;
	}

	public void setEvlList(Sim_eventListenerList evlList) {
		this.evlList = evlList;
	}
        
        public Sim_port getOutput() {
            return super.output;
        }

        public void send(Sim_port destPort, double delay, int gridSimTag,
            Object data) {
            super.send(destPort, delay, gridSimTag, data);
        }

	/**
	 * Prints out the given message into stdout. In addition, writes it into a
	 * file.
	 * 
	 * @param msg
	 *            a message
	 */
	public void write(String msg) {
		System.out.println(msg);
		if (this.getReport_() != null) {
			this.getReport_().write(msg);
		}
	}
}
