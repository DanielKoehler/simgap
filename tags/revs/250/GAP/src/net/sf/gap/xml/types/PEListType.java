/*
 ****************************************************************************************
 * Copyright © Giovanni Novelli                                             
 * All Rights Reserved.                                                                 
 ****************************************************************************************
 *
 * License:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * PEListType.java
 *
 * Created on 22 January 2008, 09.00 by Giovanni Novelli
 *
 * $Id$
 *
 */

package net.sf.gap.xml.types;

import java.util.LinkedList;

/**
 *
 * @author Giovanni Novelli
 */
public class PEListType {
    private LinkedList<PEType> items;
    
    private int MIPS;

    public PEListType() {
        this.setItems(new LinkedList<PEType>());
        this.setMIPS(0);
    }
    
    public PEListType(LinkedList<PEType> items) {
        this.setItems(items);
        for (int i=0;i<items.size();i++) {
            this.MIPS += items.get(i).getMIPS();
        }
    }
    
    public boolean add(PEType PE) {
        this.MIPS += PE.getMIPS();
        return this.getItems().add(PE);
    }
    
    public LinkedList<PEType> getItems() {
        return items;
    }

    public void setItems(LinkedList<PEType> items) {
        this.items = items;
    }

    public int getMIPS() {
        return MIPS;
    }

    public void setMIPS(int MIPS) {
        this.MIPS = MIPS;
    }
    
}
