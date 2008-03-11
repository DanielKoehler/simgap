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
 * QAGESAMeasures.java
 *
 * Created on 8 March 2008, 22.15 by Giovanni Novelli
 *
 * $Id$
 *
 */

package net.sf.gap.mc.qagesa.constants;

/**
 *
 * @author giovanni
 */
public class QAGESAMeasures {
    public static final int MF = 5;
    public static final int MR = 1;
    public static final int MS = 3;
    public static final int RMF = 4;
    public static final int RMR = 0;
    public static final int RMS = 2;

	protected String intToString(int tag) {
            String str;
		switch (tag) {
                    case MF:
                        str="FIRST CHUNK (PROXIMITY)";
                        break;
                    case MR:
                        str="RESPONSE TIME (PROXIMITY)";
                        break;
                    case MS:
                        str="STREAMING TIME (PROXIMITY)";
                        break;
                    case RMF:
                        str="FIRST CHUNK (RANDOM)";
                        break;
                    case RMR:
                        str="RESPONSE TIME (RANDOM)";
                        break;
                    case RMS:
                        str="STREAMING TIME (RANDOM)";
                        break;
		default:
                    str="UNKNOWN_MEASURE";
			break;
		}
                return str;
	}
}
