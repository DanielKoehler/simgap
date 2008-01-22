/*
 ****************************************************************************************
 * Copyright © Giovanni Novelli                                             
 * All Rights Reserved.                                                                 
 ****************************************************************************************
 *
 * License:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * LinkType.java
 *
 * Created on 22 January 2008, 09.00 by Giovanni Novelli
 *
 * $Id$
 *
 */

package net.sf.gap.mc.core.xml;

import gridsim.net.Link;

/**
 *
 * @author Giovanni Novelli
 */
public class LinkType {
    private String name;
    private double Baudrate;
    private double Delay;
    private int MTU;
    private String fromEntity;
    private String toEntity;
    
    public LinkType(
            String name,
            String fromEntity,
            String toEntity) {
        this.setName(name);
        this.setFromEntity(fromEntity);
        this.setToEntity(toEntity);
        this.setBaudrate(Link.DEFAULT_BAUD_RATE);
        this.setDelay(Link.DEFAULT_PROP_DELAY);
        this.setMTU(Link.DEFAULT_MTU);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getBaudrate() {
        return Baudrate;
    }

    public void setBaudrate(double Baudrate) {
        this.Baudrate = Baudrate;
    }

    public double getDelay() {
        return Delay;
    }

    public void setDelay(double Delay) {
        this.Delay = Delay;
    }

    public int getMTU() {
        return MTU;
    }

    public void setMTU(int MTU) {
        this.MTU = MTU;
    }

    public String getFromEntity() {
        return fromEntity;
    }

    public void setFromEntity(String fromEntity) {
        this.fromEntity = fromEntity;
    }

    public String getToEntity() {
        return toEntity;
    }

    public void setToEntity(String toEntity) {
        this.toEntity = toEntity;
    }
}
