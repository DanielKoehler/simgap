/*
 ****************************************************************************************
 * Copyright © Giovanni Novelli                                             
 * All Rights Reserved.                                                                 
 ****************************************************************************************
 *
 * License:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * ExperimentsTags.java
 *
 * Created on 8 January 2008, 11.15 by Giovanni Novelli
 *
 * $Id$
 *
 */

package net.sf.gap.mc.experiments.constants;

import net.sf.gap.constants.Tags;

/**
 * 
 * @author Giovanni Novelli
 */
public class ExperimentsTags extends Tags {

	public static final int ETAGS = 3000;

	public static final int TEST_REQ = 11 + ETAGS;
	public static final int TEST_REP = 12 + ETAGS;

	/**
	 * Creates a new instance of ExperimentsTags
	 */
	public ExperimentsTags() {
	}

	@Override
	public String otherTags(int tag) {
		String str = null;
		switch (tag) {
		case ExperimentsTags.TEST_REQ:
			str = "TEST_REQUEST";
			break;
		case ExperimentsTags.TEST_REP:
			str = "TEST_REPLY";
			break;
		default:
			str = "UNKNOWN_TAG";
			break;
		}
		return str;
	}
}
