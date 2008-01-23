/*
 ****************************************************************************************
 * Copyright © Giovanni Novelli                                             
 * All Rights Reserved.                                                                 
 ****************************************************************************************
 *
 * License:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * GridElementType.java
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
public class GridElementType {
    private String name;
    private ResourceCalendarType resourceCalendar;
    private ResourceCharacteristicsType resourceCharacteristics;
    private MachineListType machineList;
    private StorageType storage;
    private String link;

    public GridElementType() {
        this.setMachineList(new MachineListType());
    }

    public GridElementType(
            String name,
            ResourceCalendarType resourceCalendar,
            ResourceCharacteristicsType resourceCharacteristics,
            MachineListType machineList,
            StorageType storage,
            String link
            ) {
        this.setName(name);
        this.setResourceCalendar(resourceCalendar);
        this.setResourceCharacteristics(resourceCharacteristics);
        this.setMachineList(machineList);
        this.setStorage(storage);
        this.setLink(link);
    }
    
    public boolean addMachine(MachineType machine) {
        return this.getMachineList().addMachine(machine);
    }
    
    public boolean isSE() {
        return (this.getStorage()!=null);
    }
    
    public ResourceCalendarType getResourceCalendar() {
        return resourceCalendar;
    }

    public void setResourceCalendar(ResourceCalendarType resourceCalendar) {
        this.resourceCalendar = resourceCalendar;
    }

    public ResourceCharacteristicsType getResourceCharacteristics() {
        return resourceCharacteristics;
    }

    public void setResourceCharacteristics(ResourceCharacteristicsType resourceCharacteristics) {
        this.resourceCharacteristics = resourceCharacteristics;
    }

    public MachineListType getMachineList() {
        return machineList;
    }

    public void setMachineList(MachineListType machineList) {
        this.machineList = machineList;
    }

    public StorageType getStorage() {
        return storage;
    }

    public void setStorage(StorageType storage) {
        this.storage = storage;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
