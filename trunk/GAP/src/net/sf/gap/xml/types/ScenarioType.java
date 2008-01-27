/*
 ****************************************************************************************
 * Copyright © Giovanni Novelli                                             
 * All Rights Reserved.                                                                 
 ****************************************************************************************
 *
 * License:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * GridType.java
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
public class ScenarioType {
	private String name;
	private boolean trace;
	private NetworkTopologyType topology;
	private GridType grid;
	private VOSType vos;

	public ScenarioType() {
		this.setName("scenario");
		this.setTrace(false);
		this.setTopology(new NetworkTopologyType());
		this.setGrid(new GridType());
		VOSType aVOS = new VOSType();
		this.setVos(aVOS);
	}

	public ScenarioType(String name) {
		this.setName(name);
		this.setTrace(false);
		this.setTopology(new NetworkTopologyType());
		this.setGrid(new GridType());
		VOSType aVOS = new VOSType();
		this.setVos(aVOS);
	}

	public ScenarioType(String name, boolean trace) {
		this.setName(name);
		this.setTrace(trace);
		this.setTopology(new NetworkTopologyType());
		this.setGrid(new GridType());
		VOSType aVOS = new VOSType();
		this.setVos(aVOS);
	}

	public ScenarioType(String name, boolean trace, NetworkTopologyType topology) {
		this.setName(name);
		this.setTrace(trace);
		this.setTopology(topology);
		this.setGrid(new GridType());
		VOSType aVOS = new VOSType();
		this.setVos(aVOS);
	}

	public ScenarioType(String name, boolean trace,
			NetworkTopologyType topology, GridType grid) {
		this.setName(name);
		this.setTrace(trace);
		this.setTopology(topology);
		this.setGrid(grid);
		int nge = grid.getGridElements().size();
		VOSType aVOS = new VOSType();
		VOType aVO = new VOType();
		for (int i = 0; i < nge; i++) {
			aVO.addGE(grid.getGridElements().get(i).getName());
		}
		this.setVos(aVOS);
	}

	public ScenarioType(String name, boolean trace,
			NetworkTopologyType topology, GridType grid, VOSType vos) {
		this.setName(name);
		this.setTrace(trace);
		this.setTopology(topology);
		this.setGrid(grid);
		this.setVos(vos);
	}

	public int getNumRouters() {
		return this.getTopology().getRouters().size();
	}

	public NetworkTopologyType getTopology() {
		return topology;
	}

	public void setTopology(NetworkTopologyType topology) {
		this.topology = topology;
	}

	public GridType getGrid() {
		return grid;
	}

	public void setGrid(GridType grid) {
		this.grid = grid;
	}

	public VOSType getVos() {
		return vos;
	}

	public void setVos(VOSType vos) {
		this.vos = vos;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isTrace() {
		return trace;
	}

	public void setTrace(boolean trace) {
		this.trace = trace;
	}
}
