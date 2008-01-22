/*
 ****************************************************************************************
 * Copyright © Giovanni Novelli                                             
 * All Rights Reserved.                                                                 
 ****************************************************************************************
 *
 * License:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * HardDiskType.java
 *
 * Created on 22 January 2008, 09.00 by Giovanni Novelli
 *
 * $Id$
 *
 */

package net.sf.gap.mc.core.xml;

/**
 *
 * @author Giovanni Novelli
 */
public class HardDiskType {
    private String name;
    private double Capacity;
    private double Latency;
    private double AvgSeekTime;
    private double MaxTransferRate;

    public HardDiskType(String name) {
        this.setName(name);
        this.setCapacity(100000.0);
        this.setLatency(4.17);
        this.setAvgSeekTime(9.0);
        this.setMaxTransferRate(133.0);
    }
    
    public HardDiskType(
            String name,
            double Capacity,
            double Latency,
            double AvgSeekTime,
            double MaxTransferRate
            ) {
        this.setName(name);
        this.setCapacity(Capacity);
        this.setLatency(Latency);
        this.setAvgSeekTime(AvgSeekTime);
        this.setMaxTransferRate(MaxTransferRate);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getCapacity() {
        return Capacity;
    }

    public void setCapacity(double Capacity) {
        this.Capacity = Capacity;
    }

    public double getLatency() {
        return Latency;
    }

    public void setLatency(double Latency) {
        this.Latency = Latency;
    }

    public double getAvgSeekTime() {
        return AvgSeekTime;
    }

    public void setAvgSeekTime(double AvgSeekTime) {
        this.AvgSeekTime = AvgSeekTime;
    }

    public double getMaxTransferRate() {
        return MaxTransferRate;
    }

    public void setMaxTransferRate(double MaxTransferRate) {
        this.MaxTransferRate = MaxTransferRate;
    }
}
