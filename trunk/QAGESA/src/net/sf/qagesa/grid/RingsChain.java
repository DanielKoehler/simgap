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
 * RingsChain.java
 *
 * Created on 7 August 2006, 14.28 by Giovanni Novelli
 *
 * $Id: RingsChain.java 1142 2007-07-18 20:49:58Z gnovelli $
 *
 */

package net.sf.qagesa.grid;

import eduni.simjava.Sim_system;

import gridsim.GridSim;
import gridsim.net.FIFOScheduler;
import gridsim.net.Link;
import gridsim.net.RIPRouter;

import java.util.Calendar;
import java.util.Vector;

import net.sf.gap.distributions.Uniform_int;
import net.sf.gap.grid.NetworkTopology;
import net.sf.qagesa.factories.LinkFactory;

/**
 * 
 * @author Giovanni Novelli
 */
public class RingsChain extends NetworkTopology {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6731817478871055731L;

	private int cloudSize;

	private int clouds;
        
        /**
         * links delay factor
         */
        private double factor;

	/**
	 * Creates a new instance of RingsChain
	 */
	public RingsChain(boolean trace) throws Exception {
		super(4*3);
		this.setCloudSize(4);
		this.setClouds(3);
                this.setFactor(1.0);
		this.create(trace);
	}

	/**
	 * Creates a new instance of RingsChain
	 */
	public RingsChain(int m, int c, double factor, boolean trace)
			throws Exception {
		super(m*c);
		this.setCloudSize(m);
		this.setClouds(c);
                this.setFactor(factor);
		this.create(trace);
	}
        
        public void initialize() {
                int N = this.getNumRouters();
		this.setRouters(new Vector<RIPRouter>(N));
		for (int i = 0; i < N; i++) {
			RIPRouter router = (RIPRouter) Sim_system.get_entity("ROUTER_" + i);
			this.addElement(router);
		}
        }

	@Override
    @SuppressWarnings("empty-statement")
	public void create(boolean trace) throws Exception {
		Uniform_int r = new Uniform_int("RingsChain");
                int N = this.getNumRouters();
		for (int i = 0; i < N; i++) {
			RIPRouter router = new RIPRouter("ROUTER_"+i,trace);
		}
		for (int i = 0; i < this.getClouds(); i++) {
			for (int j = 0; j < this.getCloudSize(); j++) {
				int index = i * this.getCloudSize() + j; // Index of current
				// router
				int nextJ = j + 1;
				if (nextJ == this.getCloudSize()) {
					nextJ = 0;
				}
				int nextIndex = i * this.getCloudSize() + nextJ; // Index of
				// next
				// router in
				// cloud
				// (next as
				// a
				// polygon)
				RIPRouter r1 = (RIPRouter) Sim_system.get_entity("ROUTER_"+index);
				RIPRouter r2 = (RIPRouter) Sim_system.get_entity("ROUTER_"+nextIndex);
				FIFOScheduler resSched1 = new FIFOScheduler("sched_cloud" + i
						+ "_r" + index + "_r" + nextIndex);
				FIFOScheduler resSched2 = new FIFOScheduler("sched_cloud" + i
						+ "_r" + nextIndex + "_r" + index);
				double delay = 4.0 * this.getFactor();
				Link link = LinkFactory.randomLink(delay);
				r1.attachRouter(r2, link, resSched1, resSched2);
			}
			int index = i * this.getCloudSize(); // Index of first router in
			// cloud i
			int nextI = i + 1;
			if (nextI == this.getClouds()) {
				nextI = 0;
			}
			int nextIndex = nextI * this.getCloudSize(); // Index of first
			// router in cloud
			// i+1 (next as a
			// polygon)
			RIPRouter r1 = (RIPRouter) Sim_system.get_entity("ROUTER_"+index);;
			RIPRouter r2 = (RIPRouter) Sim_system.get_entity("ROUTER_"+nextIndex);;
			FIFOScheduler resSched1 = new FIFOScheduler("sched_intercloud" + i
					+ "_r" + index + "_r" + nextIndex);
			FIFOScheduler resSched2 = new FIFOScheduler("sched_intercloud" + i
					+ "_r" + nextIndex + "_r" + index);
        		double delay = 8.0 * this.getFactor();
			Link link = LinkFactory.randomLink(delay);
			r1.attachRouter(r2, link, resSched1, resSched2);
		}
	}

	public int getCloudSize() {
		return this.cloudSize;
	}

	public void setCloudSize(int cloudSize) {
		this.cloudSize = cloudSize;
	}

	public int getClouds() {
		return this.clouds;
	}

	public void setClouds(int clouds) {
		this.clouds = clouds;
	}

	public static void main(String[] args) {
		try {
			int num_user = 0;
			Calendar calendar = Calendar.getInstance();
			boolean trace_flag = false; // mean trace GridSim events/activities

			String[] exclude_from_file = { "" };
			String[] exclude_from_processing = { "" };

			String report_name = null;

			System.out.println("Initializing GridSim package");
			GridSim.init(num_user, calendar, trace_flag, exclude_from_file,
					exclude_from_processing, report_name);

			RingsChain nt = new RingsChain(trace_flag);
			System.out.println(nt.size());

			// ////////////////////////////////////////
			// Final step: Starts the simulation
                        Sim_system.set_output_analysis(Sim_system.IND_REPLICATIONS,5,0.95);
                        GridSim.startGridSimulation();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Unwanted errors happen");
		}
	}

    public double getFactor() {
        return factor;
    }

    public void setFactor(double factor) {
        this.factor = factor;
    }
}
