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
 * $Revision: 1141 $
 * $Id: Tags.java 1141 2007-07-18 18:22:15Z gnovelli $
 * $HeadURL: file:///var/svn/grid/trunk/GAP/src/net/sf/gap/constants/Tags.java $
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

	// AgentPlatform
	public static final int AP_SERVICES_LIST_REQ = 1001; // From any source
															// entity to
															// AgentPlatform

	public static final int AP_SERVICES_LIST_REP = 1002; // From
															// AgentPlatform to
															// any source entity

	// TestService
	public static final int TEST_SERVICE_REQ = 1011; // To TestService

	public static final int TEST_SERVICE_REP = 1012; // From TestService

	// Agent
	public static final int AGENT_RUN_REQ = 1101; // Final destination is an
													// Agent

	public static final int AGENT_RUN_REP = 1102;

	public static final int AGENT_RESUME_REQ = 1111; // Final destination is
														// an Agent

	public static final int AGENT_RESUME_REP = 1112;

	public static final int AGENT_PAUSE_REQ = 1121; // Final destination is an
													// Agent

	public static final int AGENT_PAUSE_REP = 1122;

	public static final int AGENT_KILL_REQ = 1131; // Final destination is an
													// Agent

	public static final int AGENT_KILL_REP = 1132;

	public static final int AGENT_KILLAWAIT_REQ = 1141; // Final destination is
														// an Agent

	public static final int AGENT_KILLAWAIT_REP = 1142;

	public static final int AGENT_MOVE_REQ = 1151; // Final destination is an
													// Agent

	public static final int AGENT_MOVE_REP = 1152;

	// DirectoryFacilitator
	public static final int DF_REGISTER_REQ = 1201; // Final destination is
													// DirectoryFacilitator

	public static final int DF_REGISTER_REP = 1202;

	public static final int DF_DEREGISTER_REQ = 1211; // Final destination is
														// DirectoryFacilitator

	public static final int DF_DEREGISTER_REP = 1212;

	public static final int DF_PAUSED_REQ = 1221; // Final destination is
													// DirectoryFacilitator

	public static final int DF_PAUSED_REP = 1222;

	public static final int DF_RESUMED_REQ = 1231; // Final destination is
													// DirectoryFacilitator

	public static final int DF_RESUMED_REP = 1232;

	// NetworkMonitor
	public static final int NM_NETWORKMAP_REQ = 1241; // Final destination is
														// NetworkMonitor

	public static final int NM_NETWORKMAP_REP = 1242;

	// TODO: REVIEW ALL
	public static final int GIS_REQUEST = 1560; // Request to GIS

	public static final int GIS_REPLY = 1561; // Reply from GIS

	public static final int PING_REQ = 1570; // Mainly from NetworkMonitor to
												// any GridElement

	public static final int PING_REP = 1571; // From a GridElement to,
												// mainly, NetworkMonitor

	public static final int GRIDLET_SUBMIT_REQ = 1580; // Destination is any
														// Agent

	public static final int GRIDLET_SUBMIT_REP = 1581; // Source of reply is an
														// Agent

	public static final int GRIDLET_STATUS_REQ = 1590;

	public static final int GRIDLET_STATUS_REP = 1591;

	public static final int HASGRIDLETS_REQUEST = 1592;

	public static final int HASGRIDLETS_REPLY = 1593;

	public static final int GRIDLETS_WAIT = 1594;

	public static final int GRIDLETS_REPLY = 1595;

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
