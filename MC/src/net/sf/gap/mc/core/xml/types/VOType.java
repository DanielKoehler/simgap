/*
 ****************************************************************************************
 * Copyright © Giovanni Novelli                                             
 * All Rights Reserved.                                                                 
 ****************************************************************************************
 *
 * License:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * VOType.java
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
public class VOType {
    private LinkedList<String> gridElements;
    
    public VOType() {
        this.setGridElements(new LinkedList<String>());
    }

    public boolean addGridElement(String gridElement) {
        return this.getGridElements().add(gridElement);
    }
    
    public LinkedList<String> getGridElements() {
        return gridElements;
    }

    public void setGridElements(LinkedList<String> gridElements) {
        this.gridElements = gridElements;
    }
}
