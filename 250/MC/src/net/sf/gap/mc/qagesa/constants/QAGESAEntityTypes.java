/*
 ****************************************************************************************
 * Copyright © Giovanni Novelli                                             
 * All Rights Reserved.                                                                 
 ****************************************************************************************
 *
 * Title:        QAGESA Simulator 
 * Description:  QAGESA (QoS Aware Grid Enabled Streaming Architecture) Simulator
 *               of a QoS-Aware Architecture for Multimedia Content Provisioning in a GRID Environment
 * License:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * QAGESAEntityTypes.java
 *
 * Created on 16 August 2006, 11.51 by Giovanni Novelli
 *
 * $Id$
 *
 */

package net.sf.gap.mc.qagesa.constants;

import net.sf.gap.constants.EntityTypes;

/**
 * 
 * @author Giovanni Novelli 
 */
public class QAGESAEntityTypes extends EntityTypes {
    public static final int QEBASE = 100 + ENTITYBASE;
    
	public static final int SERVER_PROXY = 1 + QEBASE;

	public static final int SERVICE_AL =  11 + QEBASE;

	public static final int SERVICE_CEL = 12 + QEBASE;

	public static final int SERVICE_MUC = 13 + QEBASE;

	public static final int SERVICE_REF = 14 + QEBASE;

	public static final int USER_PUBLISHER = 21 + QEBASE;

	public static final int USER_PLAYER = 22 + QEBASE;

	public static final int USER_SUBMITTER = 23 + QEBASE;

	public static final int USER_USER = 24 + QEBASE;

	/**
	 * Creates a new instance of QAGESAEntityTypes
	 */
	public QAGESAEntityTypes() {
	}

	@Override
	public String otherTypes(int entityType) {
		String str = null;
		switch (entityType) {
		case QAGESAEntityTypes.SERVER_PROXY:
			str = "SERVER_PROXY";
			break;

		case QAGESAEntityTypes.SERVICE_AL:
			str = "SERVICE_AL";
			break;
		case QAGESAEntityTypes.SERVICE_CEL:
			str = "SERVICE_CEL";
			break;
		case QAGESAEntityTypes.SERVICE_MUC:
			str = "SERVICE_MUC";
			break;
		case QAGESAEntityTypes.SERVICE_REF:
			str = "SERVICE_REF";
			break;

		case QAGESAEntityTypes.USER_PLAYER:
			str = "USER_PLAYER";
			break;
		case QAGESAEntityTypes.USER_PUBLISHER:
			str = "USER_PUBLISHER";
			break;
		case QAGESAEntityTypes.USER_SUBMITTER:
			str = "USER_SUBMITTER";
			break;
		case QAGESAEntityTypes.USER_USER:
			str = "USER_USER";
			break;
		default:
			str = "UNKNOWN_ENTITY_TYPE";
			break;
		}
		return str;
	}
}
