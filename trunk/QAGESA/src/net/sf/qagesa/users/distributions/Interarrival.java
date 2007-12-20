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
 * Created on 28 May 2007, 14.32 by Giovanni Novelli
 *
 * $Id: Interarrival.java 1142 2007-07-18 20:49:58Z gnovelli $
 *
 */

package net.sf.qagesa.users.distributions;
import eduni.simjava.distributions.*;

/**
 *
 * @author Giovanni Novelli
 */
public class Interarrival {
    /** Creates a new instance of Interarrival */
    public Interarrival() {
    }
    private static int numUsers;
    private static int numCycles;
    private static int ratio;
    private static boolean[] activeUsers;
    private static void initUsers(int aNumUsers, int aNumCycles, int aRatio) {
        numUsers = aNumUsers;
        numCycles = aNumCycles;
        ratio = aRatio;
        activeUsers = new boolean[numUsers];
        for (int i=0;i<numUsers;i++) {
            activeUsers[i] = false;
        }
    }
    private static int activateUsers(int cycle) {
        int active = 0;
        int maxUsers = cycle*ratio;
        double mean = (maxUsers * 1.0) / (numUsers * 1.0);
        Sim_uniform_obj uniform = new Sim_uniform_obj("uniform",0.0,1.0);
        while (active<maxUsers) {
            for (int i=0;i<numUsers && active<maxUsers;i++) {
                double sample = uniform.sample();
                if (sample<=mean) {
                    activeUsers[i]=true;
                    active++;
                } else {
                    activeUsers[i]=false;
                }
            }
        }
        return active;
    }
    
    private static double playRequest(double interval) {
        Sim_negexp_obj negexp = new Sim_negexp_obj("interval",interval);
        return negexp.sample();
    }
    
    public static void main(String[] args) {
        initUsers(256,16,16);
        for (int cycle=1;cycle<=numCycles;cycle++) {
            System.out.println("Cycle: " + cycle + " Active Users: " + activateUsers(cycle));
            for (int user=0;user<numUsers;user++) {
                if (activeUsers[user]) {
                    System.out.println("\tStart: " + playRequest(1.0));
                }
            }
        }
    }
}
