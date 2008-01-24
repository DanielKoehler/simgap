/*
 ****************************************************************************************
 * Copyright © Giovanni Novelli                                             
 * All Rights Reserved.                                                                 
 ****************************************************************************************
 *
 * License:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * WeekendListType.java
 *
 * Created on 22 January 2008, 09.00 by Giovanni Novelli
 *
 * $Id$
 *
 */

package net.sf.gap.xml.types;

import java.util.Calendar;
import java.util.LinkedList;

/**
 *
 * @author Giovanni Novelli
 */
public class WeekendListType {
    private LinkedList<Integer> items;

    public WeekendListType() {
        this.setItems(new LinkedList<Integer>());
        this.addWeekendDay(Calendar.SUNDAY);
        this.addWeekendDay(Calendar.SATURDAY);
    }

    public boolean addWeekendDay(int day) {
        return this.getItems().add(day);
    }
    
    public LinkedList<Integer> getItems() {
        return items;
    }

    public void setItems(LinkedList<Integer> items) {
        this.items = items;
    }
    
}
