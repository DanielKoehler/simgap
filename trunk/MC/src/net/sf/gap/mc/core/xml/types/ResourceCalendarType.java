/*
 ****************************************************************************************
 * Copyright © Giovanni Novelli                                             
 * All Rights Reserved.                                                                 
 ****************************************************************************************
 *
 * License:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * ResourceCalendarType.java
 *
 * Created on 22 January 2008, 09.00 by Giovanni Novelli
 *
 * $Id$
 *
 */

package net.sf.gap.mc.core.xml.types;

/**
 *
 * @author Giovanni Novelli
 */
public class ResourceCalendarType {
    private String name;
    private double TimeZone;
    private double PeakLoad;
    private double OffPeakLoad;
    private double RelativeHolidayLoad;
    private WeekendListType weekendList;
    private HolidayListType holidayList;

    public ResourceCalendarType() {
        this.setName("Default Resource Calendar");
        this.setTimeZone(0.0);
        this.setPeakLoad(0.0);
        this.setOffPeakLoad(0.0);
        this.setRelativeHolidayLoad(0.0);
        this.setWeekendList(new WeekendListType());
        this.setHolidayList(new HolidayListType());
    }
    
    public ResourceCalendarType(
            String name,
            double TimeZone,
            double PeakLoad,
            double OffPeakLoad,
            double RelativeHolidayLoad,
            WeekendListType weekendList,
            HolidayListType holidayList
            ) {
        this.setName(name);
        this.setTimeZone(TimeZone);
        this.setPeakLoad(PeakLoad);
        this.setOffPeakLoad(OffPeakLoad);
        this.setRelativeHolidayLoad(RelativeHolidayLoad);
        this.setWeekendList(weekendList);
        this.setHolidayList(holidayList);
    }
    
    public double getTimeZone() {
        return TimeZone;
    }

    public void setTimeZone(double TimeZone) {
        this.TimeZone = TimeZone;
    }

    public double getPeakLoad() {
        return PeakLoad;
    }

    public void setPeakLoad(double PeakLoad) {
        this.PeakLoad = PeakLoad;
    }

    public double getOffPeakLoad() {
        return OffPeakLoad;
    }

    public void setOffPeakLoad(double OffPeakLoad) {
        this.OffPeakLoad = OffPeakLoad;
    }

    public double getRelativeHolidayLoad() {
        return RelativeHolidayLoad;
    }

    public void setRelativeHolidayLoad(double RelativeHolidayLoad) {
        this.RelativeHolidayLoad = RelativeHolidayLoad;
    }

    public WeekendListType getWeekendList() {
        return weekendList;
    }

    public void setWeekendList(WeekendListType weekendList) {
        this.weekendList = weekendList;
    }

    public HolidayListType getHolidayList() {
        return holidayList;
    }

    public void setHolidayList(HolidayListType holidayList) {
        this.holidayList = holidayList;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
