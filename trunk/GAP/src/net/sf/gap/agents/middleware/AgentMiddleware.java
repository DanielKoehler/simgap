/*
 ****************************************************************************************
 * Copyright © Giovanni Novelli                                             
 * All Rights Reserved.                                                                 
 ****************************************************************************************
 *
 * Title:        GAP Simulator
 * Description:  GAP (Grid Agents Platform) Toolkit for Modeling and Simulation
 *               of Mobile Agents on Grids
 * License:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * AgentMiddleware.java
 *
 * Created on 16 August 2006, 12.00 by Giovanni Novelli
 *
 ****************************************************************************************
 *
 * $Revision: 1141 $
 * $Id: AgentMiddleware.java 1141 2007-07-18 18:22:15Z gnovelli $
 * $HeadURL: file:///var/svn/grid/trunk/GAP/src/net/sf/gap/agents/middleware/AgentMiddleware.java $
 *
 *****************************************************************************************
 */

package net.sf.gap.agents.middleware;

import gridsim.GridSim;
import gridsim.util.SimReport;
import net.sf.gap.grid.AbstractVirtualOrganization;
import net.sf.gap.grid.components.GridElement;
import net.sf.gap.grid.factories.LinkFactory;

/**
 * 
 * This is a very simple abstraction of Mobile Agents Middleware modeled as a
 * GridSim entity
 * 
 * @author Giovanni Novelli
 */

public class AgentMiddleware extends GridSim {
        private boolean platform;
    
        private boolean traceFlag;
    
	private SimReport report_; // logs every events

	private GridElement gridElement;

	private AbstractVirtualOrganization virtualOrganization;

	/**
	 * Creates a new instance of AgentMiddleware
	 * 
	 * @param name
	 *            AgentMiddleware entity name
	 * @param trace_flag
	 *            GridSim trace flag
	 * @throws Exception
	 *             This happens when name is null or haven't initialized
	 *             GridSim.
	 */
	public AgentMiddleware(String name, boolean trace_flag) throws Exception {
		super(name, LinkFactory.getMiddlewareLink(name));
                this.setPlatform(false);
                this.setTraceFlag(trace_flag);
	}

        public void initialize() throws Exception {
		// creates a report file
		if (this.isTraceFlag() == true) {
			this.setReport_(new SimReport(this.get_name()));
		}
        }
        
	public SimReport getReport_() {
		return this.report_;
	}

	public void setReport_(SimReport report_) {
		this.report_ = report_;
	}

	/**
	 * Prints out the given message into stdout. In addition, writes it into a
	 * file.
	 * 
	 * @param msg
	 *            a message
	 */
	protected void write(String msg) {
		System.out.println(msg);
		if (this.report_ != null) {
			this.report_.write(msg);
		}
	}

	public AbstractVirtualOrganization getVirtualOrganization() {
		return this.virtualOrganization;
	}

	public void setVirtualOrganization(AbstractVirtualOrganization vo) {
		this.virtualOrganization = vo;
	}

	/**
	 * @param gridElement
	 *            the gridElement to set
	 */
	public void setGridElement(GridElement gridElement) {
		this.gridElement = gridElement;
	}

	/**
	 * @return the gridElement
	 */
	public GridElement getGridElement() {
		return gridElement;
	}

    public boolean isTraceFlag() {
        return traceFlag;
    }

    public void setTraceFlag(boolean traceFlag) {
        this.traceFlag = traceFlag;
    }

    public boolean isPlatform() {
        return platform;
    }

    public void setPlatform(boolean platform) {
        this.platform = platform;
    }
}
