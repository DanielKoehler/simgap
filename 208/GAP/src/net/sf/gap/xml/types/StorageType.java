/*
 ****************************************************************************************
 * Copyright © Giovanni Novelli                                             
 * All Rights Reserved.                                                                 
 ****************************************************************************************
 *
 * License:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * StorageType.java
 *
 * Created on 22 January 2008, 09.00 by Giovanni Novelli
 *
 * $Id$
 *
 */
package net.sf.gap.xml.types;

/**
 *
 * @author Giovanni Novelli
 */
public class StorageType {
    private ReplicaManagerType RM;
    private HardDiskListType HardDiskList;
    private TapeListType TapeList;
    
    public StorageType(
            String RM_name
            ) {
        this.setRM(new ReplicaManagerType(RM_name));
        this.setHardDiskList(new HardDiskListType());
        this.setTapeList(new TapeListType());
    }

    public boolean addHardDisk(HardDiskType hd) {
        return this.getHardDiskList().addHardDisk(hd);
    }
    
    public boolean addTape(TapeType tape) {
        return this.getTapeList().addTape(tape);
    }
    
    public ReplicaManagerType getRM() {
        return RM;
    }

    public void setRM(ReplicaManagerType RM) {
        this.RM = RM;
    }

    public HardDiskListType getHardDiskList() {
        return HardDiskList;
    }

    public void setHardDiskList(HardDiskListType HardDiskList) {
        this.HardDiskList = HardDiskList;
    }

    public TapeListType getTapeList() {
        return TapeList;
    }

    public void setTapeList(TapeListType TapeList) {
        this.TapeList = TapeList;
    }
}
