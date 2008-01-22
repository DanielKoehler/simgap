/*
 ****************************************************************************************
 * Copyright © Giovanni Novelli                                             
 * All Rights Reserved.                                                                 
 ****************************************************************************************
 *
 * License:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * TapeListType.java
 *
 * Created on 22 January 2008, 09.00 by Giovanni Novelli
 *
 * $Id$
 *
 */

package net.sf.gap.mc.core.xml.types;

import java.util.LinkedList;

/**
 *
 * @author Giovanni Novelli
 */
public class TapeListType {
    private LinkedList<TapeType> items;
    
    public TapeListType() {
        this.setItems(new LinkedList<TapeType>());
    }

    public TapeListType(LinkedList<TapeType> items) {
        this.setItems(items);
    }
    
    public boolean addTape(TapeType tape) {
        return this.getItems().add(tape);
    }
    
    public LinkedList<TapeType> getItems() {
        return items;
    }

    public void setItems(LinkedList<TapeType> items) {
        this.items = items;
    }
}
