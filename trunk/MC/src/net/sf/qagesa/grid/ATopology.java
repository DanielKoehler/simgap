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
 * Created on 5 April 2007, 14.35 by Giovanni Novelli
 *
 * $Id: ATopology.java 1142 2007-07-18 20:49:58Z gnovelli $
 *
 */

package net.sf.qagesa.grid;

import eduni.simjava.Sim_system;

import gridsim.net.FIFOScheduler;
import gridsim.net.Link;
import gridsim.net.RIPRouter;
import gridsim.net.SimpleLink;

import java.util.Vector;

import net.sf.gap.grid.NetworkTopology;
import net.sf.qagesa.factories.LinkFactory;

/**
 * 
 * @author Giovanni Novelli
 */
public class ATopology extends NetworkTopology {
    /**
     *
     * Field used to equally scale delays of all links between routers
     *
     */
    private double factor;
    
    /** Creates a new instance of ATopology */
    public ATopology(boolean trace, double factor) throws Exception {
        super(9);
        this.setFactor(factor);
        this.create(trace);
    }

    @Override
    public void create(boolean trace) throws Exception {
        int N = this.getNumRouters();
        for (int i = 0; i < N; i++) {
                RIPRouter router = new RIPRouter("ROUTER_"+i,trace);
        }
        this.linkRouters(0,4,0.008*this.getFactor(),3000);
        this.linkRouters(4,0,0.008*this.getFactor(),800);
        this.linkRouters(0,5,0.008*this.getFactor(),1000);
        this.linkRouters(5,0,0.004*this.getFactor(),2000);
        this.linkRouters(1,2,0.004*this.getFactor(),800);
        this.linkRouters(2,1,0.008*this.getFactor(),800);
        this.linkRouters(2,3,0.008*this.getFactor(),1000);
        this.linkRouters(3,2,0.004*this.getFactor(),800);
        this.linkRouters(3,4,0.002*this.getFactor(),1000);
        this.linkRouters(4,3,0.004*this.getFactor(),2000);
        this.linkRouters(5,7,0.008*this.getFactor(),2000);
        this.linkRouters(7,5,0.004*this.getFactor(),2000);
        this.linkRouters(6,8,0.004*this.getFactor(),1000);
        this.linkRouters(8,6,0.004*this.getFactor(),1000);
        this.linkRouters(6,7,0.004*this.getFactor(),800);
        this.linkRouters(7,6,0.008*this.getFactor(),800);
        this.linkRouters(1,4,0.004*this.getFactor(),1000);
        this.linkRouters(4,1,0.004*this.getFactor(),1000);
        this.linkRouters(8,5,0.004*this.getFactor(),1000);
        this.linkRouters(5,8,0.004*this.getFactor(),1000);
    }
    
    /**
     *
     * @param i1 index of first router
     * @param i2 index of second router
     * @param delay delay in seconds
     * @param mbps bandwidth in mbps
     */
    private void linkRouters(int i1, int i2, double delay, int mbps) throws Exception {
        RIPRouter r1;
        RIPRouter r2;
        FIFOScheduler resSched1;
        FIFOScheduler resSched2;
        Link link;
        r1 = (RIPRouter) Sim_system.get_entity("ROUTER_"+i1);
        r2 = (RIPRouter) Sim_system.get_entity("ROUTER_"+i2);
        resSched1 = new FIFOScheduler("sched_cloud" + "_r" + i1 + "_r" + i2);
        resSched2 = new FIFOScheduler("sched_cloud" + "_r" + i2 + "_r" + i1);
        link = this.makeLink(delay*1000.0, mbps);
        r1.attachRouter(r2, link, resSched1, resSched2);
    }
    
        private Link makeLink(double delay, int mbps) throws Exception {
		double bps = mbps * 1000000.0;
		Link link;
		link = new SimpleLink("link_" + LinkFactory.incCounter(), bps, delay,
				LinkFactory.getMTU());
		return link;
	}
        
    public void initialize() {
            int N = this.getNumRouters();
            this.setRouters(new Vector<RIPRouter>(N));
            for (int i = 0; i < N; i++) {
                    RIPRouter router = (RIPRouter) Sim_system.get_entity("ROUTER_" + i);
                    this.addElement(router);
            }
    }

    public double getFactor() {
        return factor;
    }

    public void setFactor(double factor) {
        this.factor = factor;
    }
}
