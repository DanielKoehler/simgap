/*
 ****************************************************************************************
 * Copyright © Giovanni Novelli                                             
 * All Rights Reserved.                                                                 
 ****************************************************************************************
 *
 * License:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * QAGESA.java
 *
 * Created on 19 August 2006, 17.14 by Giovanni Novelli
 *
 * $Id$
 *
 */

package net.sf.gap.mc.qagesa;

import net.sf.gap.GAP;
import net.sf.gap.constants.EntityTypes;
import net.sf.gap.constants.Tags;
import net.sf.gap.impl.MC;
import net.sf.gap.mc.qagesa.constants.QAGESAEntityTypes;
import net.sf.gap.mc.qagesa.constants.QAGESATags;

/**
 *
 * @author Giovanni Novelli
 */
public class QAGESA extends MC {
	/** 
         * Initializes simulation's time parameters
         * @param platformStart Platform's start time
         * @param start         Simulation's start time
         * @param end           Simulation's end time
         */
	public static void initialize(double platformStart, double start, double end) {
		GAP.setPlatformStartTime(platformStart);
		GAP.setStartTime(start);
		GAP.setEndTime(end);
		EntityTypes.setInstance(new QAGESAEntityTypes());
		Tags.setInstance(new QAGESATags());
	}
}
