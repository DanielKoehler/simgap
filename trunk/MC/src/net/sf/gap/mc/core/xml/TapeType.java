/*
 ****************************************************************************************
 * Copyright © Giovanni Novelli                                             
 * All Rights Reserved.                                                                 
 ****************************************************************************************
 *
 * License:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * TapeType.java
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
public class TapeType {
  private String name;
  private double Capacity;
  private double AccessTime;
  private double RewindTime;
  private double MaxTransferRate;

  public TapeType(String name) {
      this.setName(name);
      this.setCapacity(100000.0);
      this.setAccessTime(142.0);
      this.setRewindTime(284.0);
      this.setMaxTransferRate(8.0);
  }
  
  public TapeType(
          String name, 
          double Capacity, 
          double AccessTime,
          double RewindTime,
          double MaxTransferRate) {
      this.setName(name);
      this.setCapacity(Capacity);
      this.setAccessTime(AccessTime);
      this.setRewindTime(RewindTime);
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

    public double getAccessTime() {
        return AccessTime;
    }

    public void setAccessTime(double AccessTime) {
        this.AccessTime = AccessTime;
    }

    public double getRewindTime() {
        return RewindTime;
    }

    public void setRewindTime(double RewindTime) {
        this.RewindTime = RewindTime;
    }

    public double getMaxTransferRate() {
        return MaxTransferRate;
    }

    public void setMaxTransferRate(double MaxTransferRate) {
        this.MaxTransferRate = MaxTransferRate;
    }
}
