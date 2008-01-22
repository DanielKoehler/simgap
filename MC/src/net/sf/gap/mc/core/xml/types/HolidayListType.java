/*
 ****************************************************************************************
 * Copyright © Giovanni Novelli                                             
 * All Rights Reserved.                                                                 
 ****************************************************************************************
 *
 * License:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * HolidayListType.java
 *
 * Created on 22 January 2008, 09.00 by Giovanni Novelli
 *
 * $Id$
 *
 */

package net.sf.gap.mc.core.xml.types;

import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;

/**
 *
 * @author Giovanni Novelli
 */
public class HolidayListType {
    private LinkedList<Date> items;

    public HolidayListType() {
        this.setItems(new LinkedList<Date>());
    }
    
    public boolean addDate(Date date) {
        return this.getItems().add(date);
    }
    
    public boolean addCalendarDay(int year, int month, int day) {
        @SuppressWarnings("deprecation")
        Date date = new Date(year, month, day);
        return this.getItems().add(date);
    }
    
    public LinkedList<Date> getItems() {
        return items;
    }

    public void setItems(LinkedList<Date> items) {
        this.items = items;
    }
}
