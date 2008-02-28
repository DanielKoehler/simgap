/*
 ****************************************************************************************
 * Copyright © Giovanni Novelli                                             
 * All Rights Reserved.                                                                 
 ****************************************************************************************
 *
 * License:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * ResourceCharacteristicsType.java
 *
 * Created on 22 January 2008, 09.00 by Giovanni Novelli
 *
 * $Id$
 *
 */

package net.sf.gap.xml.types;

import gridsim.ResourceCharacteristics;

/**
 * 
 * @author Giovanni Novelli
 */
public class ResourceCharacteristicsType {
	private String name;
	private String Architecture;
	private String OS;
	private MachineListType machineList;
	private int Policy;
	private double Timezone;
	private double Cost;

	public ResourceCharacteristicsType(MachineListType machineList) {
		this.setName("Default Resource Characteristics");
		this.setArchitecture("System Architecture");
		this.setOS("Operating System");
		this.setMachineList(machineList);
		this.setPolicy(ResourceCharacteristics.TIME_SHARED);
		this.setTimezone(0.0);
		this.setCost(1.0);
	}

	public ResourceCharacteristicsType(String name, String Architecture,
			String OS, MachineListType machineList, int Policy,
			double Timezone, double Cost) {
		this.setName(name);
		this.setArchitecture(Architecture);
		this.setOS(OS);
		this.setMachineList(machineList);
		this.setPolicy(Policy);
		this.setTimezone(Timezone);
		this.setCost(Cost);
	}

	public String getArchitecture() {
		return Architecture;
	}

	public void setArchitecture(String Architecture) {
		this.Architecture = Architecture;
	}

	public String getOS() {
		return OS;
	}

	public void setOS(String OS) {
		this.OS = OS;
	}

	public MachineListType getMachineList() {
		return machineList;
	}

	public void setMachineList(MachineListType machineList) {
		this.machineList = machineList;
	}

	public int getPolicy() {
		return Policy;
	}

	public void setPolicy(int Policy) {
		this.Policy = Policy;
	}

	public double getTimezone() {
		return Timezone;
	}

	public void setTimezone(double Timezone) {
		this.Timezone = Timezone;
	}

	public double getCost() {
		return Cost;
	}

	public void setCost(double Cost) {
		this.Cost = Cost;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
