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
 * Uniform_int.java
 *
 * Created on 25 August 2006, 12.00 by Giovanni Novelli
 *
 ****************************************************************************************
 *
 * $Revision: 1141 $
 * $Id: Uniform_int.java 1141 2007-07-18 18:22:15Z gnovelli $
 * $HeadURL: file:///var/svn/grid/trunk/GAP/src/net/sf/gap/distributions/Uniform_int.java $
 *
 *****************************************************************************************
 */

package net.sf.gap.distributions;

import eduni.simjava.distributions.Sim_uniform_obj;

/**
 * A random number generator of int numbers based on the Uniform distribution
 * 
 * @author Giovanni Novelli
 * @see eduni.simjava.distributions.Sim_uniform_obj
 */
public class Uniform_int extends Sim_uniform_obj {

	/** Creates a new instance of Uniform_int */
	public Uniform_int(String name) {
		super(name, 0.0, 1.0);
	}

	public int sample(int size) {
		double d = this.sample();
		int index = (int) (d * size);
		if (index < 1) {
			index = 0;
		}
		if (index >= size) {
			index = size - 1;
		}
		return index;
	}
}
