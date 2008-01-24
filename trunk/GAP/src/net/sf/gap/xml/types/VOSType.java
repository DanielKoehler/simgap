/*
 ****************************************************************************************
 * Copyright © Giovanni Novelli                                             
 * All Rights Reserved.                                                                 
 ****************************************************************************************
 *
 * License:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * VOSType.java
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
public class VOSType {
    private LinkedList<VOType> voItems;
    
    public VOSType() {
        this.setVoItems(new LinkedList<VOType>());
    }

    public VOSType(GridType grid) {
        this.setVoItems(new LinkedList<VOType>());
        VOType aVO = new VOType();
        aVO.setGridElements(new LinkedList<String>());
        for (int i=0;i<grid.getGridElements().size();i++) {
            aVO.addGE(grid.getGridElements().get(i).getName());
        }
        this.addVO(aVO);
    }
    
    public boolean addVO(VOType aVO) {
        return this.getVoItems().add(aVO);
    }
    
    public LinkedList<VOType> getVoItems() {
        return voItems;
    }

    public void setVoItems(LinkedList<VOType> voItems) {
        this.voItems = voItems;
    }
}
