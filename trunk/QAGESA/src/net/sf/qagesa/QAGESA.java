/*
 *
 * Title:        QAGESA Simulator
 * Description:  QAGESA (QoS Aware Grid Enabled Streaming Architecture) Simulator
 *               of a QoS-Aware Architecture for Multimedia Content Provisioning in a GRID Environment
 * License:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * QAGESA.java
 *
 * Created on 19 August 2006, 17.14 by Giovanni Novelli
 *
 * $Id: QAGESA.java 1142 2007-07-18 20:49:58Z gnovelli $
 *
 */

package net.sf.qagesa;

import net.sf.gap.GAP;
import net.sf.gap.constants.EntityTypes;
import net.sf.gap.constants.Tags;
import net.sf.qagesa.constants.QAGESAEntityTypes;
import net.sf.qagesa.constants.QAGESATags;

/**
 * 
 * @author Giovanni Novelli
 */
public class QAGESA extends GAP {

	/** Creates a new instance of QAGESA */
	public QAGESA() {
		super();
	}

	public static void startSimulation() {
		System.out.println("Starting QAGESA Simulation ");
		GAP.startSimulation();
	}

	public static void stopSimulation() {
		System.out.println("Stopping QAGESA Simulation ");
		GAP.stopSimulation();
	}

	public static void initialize(double platformStart, double start, double end) {
		GAP.setPlatformStartTime(platformStart);
		GAP.setStartTime(start);
		GAP.setEndTime(end);
		EntityTypes.setInstance(new QAGESAEntityTypes());
		Tags.setInstance(new QAGESATags());
	}
}
