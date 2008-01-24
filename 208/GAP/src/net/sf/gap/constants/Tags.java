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
 * Tags.java
 *
 * Created on 19 August 2006, 12.00 by Giovanni Novelli
 *
 ****************************************************************************************
 *
 * $Revision$
 * $Id$
 * $HeadURL$
 *
 *****************************************************************************************
 */

package net.sf.gap.constants;

/**
 * 
 * @author Giovanni Novelli
 */
public abstract class Tags {
	private static Tags instance = null;

	/**
	 * Creates a new instance of Tags
	 */
	public Tags() {
	}

        public static final int TAGSBASE = 1000;

        public static final int AGENTBASE = 100 + TAGSBASE;
                
        public static final int SSBASE = 200 + TAGSBASE;

        public static final int GRIDLETSBASE = 300 + TAGSBASE;
        
	// AgentPlatform
	public static final int AP_SERVICES_LIST_REQ = 1 + TAGSBASE; // From any source
															// entity to
															// AgentPlatform

	public static final int AP_SERVICES_LIST_REP = 2 + TAGSBASE; // From
															// AgentPlatform to
															// any source entity

	// TestService
	public static final int TEST_SERVICE_REQ = 11 + TAGSBASE; // To TestService

	public static final int TEST_SERVICE_REP = 12 + TAGSBASE; // From TestService

	// Agent
	public static final int AGENT_RUN_REQ = 1 + AGENTBASE; // Final destination is an
													// Agent

	public static final int AGENT_RUN_REP = 2 + AGENTBASE;

	public static final int AGENT_RESUME_REQ = 11 + AGENTBASE; // Final destination is
														// an Agent

	public static final int AGENT_RESUME_REP = 12 + AGENTBASE;

	public static final int AGENT_PAUSE_REQ = 21 + AGENTBASE; // Final destination is an
													// Agent

	public static final int AGENT_PAUSE_REP = 22 + AGENTBASE;

	public static final int AGENT_KILL_REQ = 31 + AGENTBASE; // Final destination is an
													// Agent
	public static final int AGENT_KILL_REP = 32 + AGENTBASE;

	public static final int AGENT_KILLAWAIT_REQ = 41 + AGENTBASE; // Final destination is
														// an Agent
	public static final int AGENT_KILLAWAIT_REP = 42 + AGENTBASE;

	public static final int AGENT_MOVE_REQ = 51 + AGENTBASE; // Final destination is an
													// Agent
	public static final int AGENT_MOVE_REP = 52 + AGENTBASE;

	// DirectoryFacilitator
	public static final int DF_REGISTER_REQ = 1 + SSBASE; // Final destination is
													// DirectoryFacilitator

	public static final int DF_REGISTER_REP = 2 + SSBASE;

	public static final int DF_DEREGISTER_REQ = 11 + SSBASE; // Final destination is
														// DirectoryFacilitator

	public static final int DF_DEREGISTER_REP = 12 + SSBASE;

	public static final int DF_PAUSED_REQ = 21 + SSBASE; // Final destination is
													// DirectoryFacilitator

	public static final int DF_PAUSED_REP = 22 + SSBASE;

	public static final int DF_RESUMED_REQ = 31 + SSBASE; // Final destination is
													// DirectoryFacilitator
	public static final int DF_RESUMED_REP = 32 + SSBASE;

	// NetworkMonitor
	public static final int NM_NETWORKMAP_REQ = 41 + SSBASE; // Final destination is
														// NetworkMonitor
	public static final int NM_NETWORKMAP_REP = 42 + SSBASE;

	// TODO: REVIEW ALL
	public static final int GIS_REQUEST = 51 + SSBASE; // Request to GIS

	public static final int GIS_REPLY = 52 + SSBASE; // Reply from GIS

	public static final int PING_REQ = 61 + SSBASE; // Mainly from NetworkMonitor to
										// any GridElement
	public static final int PING_REP = 62 + SSBASE; // From a GridElement to,
												// mainly, NetworkMonitor

        public static final int GRIDLET_SUBMIT_REQ = 11 + GRIDLETSBASE; // Destination is any
														// Agent
	public static final int GRIDLET_SUBMIT_REP = 12 + GRIDLETSBASE; // Source of reply is an
														// Agent

	public static final int GRIDLET_STATUS_REQ = 21 + GRIDLETSBASE;

	public static final int GRIDLET_STATUS_REP = 22 + GRIDLETSBASE;
        

	public static final int HASGRIDLETS_REQUEST = 31 + GRIDLETSBASE;

	public static final int HASGRIDLETS_REPLY = 32 + GRIDLETSBASE;

	public static final int GRIDLETS_WAIT = 41 + GRIDLETSBASE;

	public static final int GRIDLETS_REPLY = 42 + GRIDLETSBASE;

	public static final String toString(int tag) {
		if (getInstance() != null) {
			return getInstance().intToString(tag);
		} else {
			return "Tags instance wasn't initilialised";
		}
	}

	protected String intToString(int tag) {
		switch (tag) {
		case Tags.DF_REGISTER_REQ:
			return "REGISTER_AGENT";
		case Tags.DF_DEREGISTER_REQ:
			return "DEREGISTER_AGENT";
		case Tags.DF_PAUSED_REQ:
			return "PAUSED_AGENT";
		case Tags.DF_RESUMED_REQ:
			return "RESUMED_AGENT";

		case Tags.AP_SERVICES_LIST_REQ:
			return "SERVICES_LIST_REQUEST";
		case Tags.AP_SERVICES_LIST_REP:
			return "SERVICES_LIST_REPLY";
		case Tags.TEST_SERVICE_REQ:
			return "TEST_SERVICE";
		case Tags.AGENT_RUN_REQ:
			return "RUN_AGENT";
		case Tags.AGENT_PAUSE_REQ:
			return "PAUSE_AGENT";
		case Tags.AGENT_RESUME_REQ:
			return "RESUME_AGENT";
		case Tags.AGENT_KILL_REQ:
			return "KILL_AGENT";
		case Tags.AGENT_KILLAWAIT_REQ:
			return "KILLWAIT_AGENT";
		case Tags.AGENT_MOVE_REQ:
			return "MOVE_AGENT";

		case Tags.GIS_REQUEST:
			return "GIS_REQUEST";
		case Tags.GIS_REPLY:
			return "GIS_REPLY";

		case Tags.PING_REQ:
			return "PING_REQUEST";
		case Tags.PING_REP:
			return "PING_REPLY";

		case Tags.GRIDLET_SUBMIT_REQ:
			return "GRIDLET_REQUEST";
		case Tags.GRIDLET_SUBMIT_REP:
			return "GRIDLET_REPLY";

		case Tags.GRIDLET_STATUS_REQ:
			return "GRIDLET_STATUS_REQUEST";
		case Tags.GRIDLET_STATUS_REP:
			return "GRIDLET_STATUS_REPLY";

		case Tags.HASGRIDLETS_REQUEST:
			return "HASGRIDLETS_REQUEST";
		case Tags.HASGRIDLETS_REPLY:
			return "HASGRIDLETS_REPLY";

		case Tags.GRIDLETS_WAIT:
			return "GRIDLETS_WAIT";
		case Tags.GRIDLETS_REPLY:
			return "GRIDLETS_REPLY";

		default:
			return getInstance().otherTags(tag);
		}
	}

	protected abstract String otherTags(int tag);

	public static final Tags getInstance() {
		return instance;
	}

	public static final void setInstance(Tags instance) {
		Tags.instance = instance;
	}
}
