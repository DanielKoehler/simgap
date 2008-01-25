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
 * GAPGridletList.java
 *
 * Created on 15 January 2008, 17.00 by Giovanni Novelli
 *
 ****************************************************************************************
 *
 * $Revision$
 * $Id$
 * $HeadURL$
 *
 *****************************************************************************************
 */

package net.sf.gap.agents.gridlets;

import gridsim.GridletList;

/**
 *
 * @author Giovanni Novelli
 */
public class GAPGridletList extends GridletList {
    /**
	 * 
	 */
	private static final long serialVersionUID = 3695668616762663229L;
	private double MIPS;
    private long inputSize;
    private long outputSize;
    private double processingCost;
    private double actualCPUTime;
    private double waitingTime;
    private double wallClockTime;

    public double getMIPS() {
        return MIPS;
    }

    public void setMIPS(double MIPS) {
        this.MIPS = MIPS;
    }

    public void decMIPS(double MIPS) {
        this.MIPS -= MIPS;
    }

    public void incMIPS(double MIPS) {
        this.MIPS += MIPS;
    }
    
    public long getInputSize() {
        return inputSize;
    }

    public void decInputSize(long inputSize) {
        this.inputSize -= inputSize;
    }

    public void incInputSize(long inputSize) {
        this.inputSize += inputSize;
    }

    public void setInputSize(long inputSize) {
        this.inputSize = inputSize;
    }

    public long getOutputSize() {
        return outputSize;
    }

    public void decOutputSize(long outputSize) {
        this.outputSize -= outputSize;
    }

    public void incOutputSize(long outputSize) {
        this.outputSize += outputSize;
    }

    public void setOutputSize(long outputSize) {
        this.outputSize = outputSize;
    }

    public double getProcessingCost() {
        return processingCost;
    }

    public void decProcessingCost(double processingCost) {
        this.processingCost -= processingCost;
    }

    public void incProcessingCost(double processingCost) {
        this.processingCost += processingCost;
    }

    public void setProcessingCost(double processingCost) {
        this.processingCost = processingCost;
    }

    public double getActualCPUTime() {
        return actualCPUTime;
    }

    public void decActualCPUTime(double actualCPUTime) {
        this.actualCPUTime -= actualCPUTime;
    }

    public void incActualCPUTime(double actualCPUTime) {
        this.actualCPUTime += actualCPUTime;
    }

    public void setActualCPUTime(double actualCPUTime) {
        this.actualCPUTime = actualCPUTime;
    }

    public double getWaitingTime() {
        return waitingTime;
    }

    public void decWaitingTime(double waitingTime) {
        this.waitingTime -= waitingTime;
    }

    public void incWaitingTime(double waitingTime) {
        this.waitingTime += waitingTime;
    }

    public void setWaitingTime(double waitingTime) {
        this.waitingTime = waitingTime;
    }

    public double getWallClockTime() {
        return wallClockTime;
    }

    public void decWallClockTime(double wallClockTime) {
        this.wallClockTime -= wallClockTime;
    }

    public void incWallClockTime(double wallClockTime) {
        this.wallClockTime += wallClockTime;
    }

    public void setWallClockTime(double wallClockTime) {
        this.wallClockTime = wallClockTime;
    }
}
