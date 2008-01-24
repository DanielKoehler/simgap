/*
 ****************************************************************************************
 * Copyright © Giovanni Novelli                                             
 * All Rights Reserved.                                                                 
 ****************************************************************************************
 *
 * License:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * Measure.java
 *
 * Created on 11 January 2008, 10.20 by Giovanni Novelli
 *
 * $Id$
 *
 */

package net.sf.gap.mc.experiments.users.impl;

import eduni.simjava.Sim_stat;

/**
 *
 * @author Giovanni Novelli
 */
public class Measure {
    private int experimentID;
    private int[] tags;
    private int measure;

    private Sim_stat stat;
    
    public Measure(int experimentID) {
        this.setExperimentID(experimentID);
        this.setup();
    }

    public Measure(int experimentID, int[] tags, int measure) {
        this.setExperimentID(experimentID);
        this.setTags(tags);
        this.setMeasure(measure);
        this.setup(tags, measure);
    }

    public void setup() {
        Sim_stat stat = new Sim_stat();
        this.setMeasure(Sim_stat.SERVICE_TIME);
        stat.add_measure(this.getMeasure());
        this.setStat(stat);
    }

    public void setup(int[] tags, int measure) {
        Sim_stat stat = new Sim_stat();
        stat.measure_for(tags);
        stat.add_measure(measure);
        this.setStat(stat);
    }
    
    public Sim_stat getStat() {
        return stat;
    }

    public void setStat(Sim_stat stat) {
        this.stat = stat;
    }

    public int getExperimentID() {
        return experimentID;
    }

    public void setExperimentID(int experimentID) {
        this.experimentID = experimentID;
    }

    public int[] getTags() {
        return tags;
    }

    public void setTags(int[] tags) {
        this.tags = tags;
    }

    public int getMeasure() {
        return measure;
    }

    public void setMeasure(int measure) {
        this.measure = measure;
    }
}
