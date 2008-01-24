/*
 ****************************************************************************************
 * Copyright © Giovanni Novelli                                             
 * All Rights Reserved.                                                                 
 ****************************************************************************************
 *
 * License:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * MachineType.java
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
public class MachineType {
    private PEListType peList;
    
    public MachineType() {
        this.setPeList(new PEListType());
    }
    
    public MachineType(PEListType peList) {
        this.setPeList(peList);
    }

    public boolean addPE(int MIPS) {
        return this.getPeList().add(new PEType(MIPS));
    }
    
    public int getMIPS() {
        return this.getPeList().getMIPS();
    }
    
    public boolean addPE(PEType pe) {
        return this.getPeList().add(pe);
    }
            
    public PEListType getPeList() {
        return peList;
    }

    public void setPeList(PEListType peList) {
        this.peList = peList;
    }
}
