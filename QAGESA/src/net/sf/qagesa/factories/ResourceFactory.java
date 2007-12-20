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
 * ResourceFactory.java
 *
 * Created on 7 August 2006, 13.37 by Giovanni Novelli
 *
 * $Id: ResourceFactory.java 1142 2007-07-18 20:49:58Z gnovelli $
 *
 */

package net.sf.qagesa.factories;

import gridsim.net.Link;
import net.sf.gap.grid.components.GridElement;

/**
 * 
 * @author Giovanni Novelli
 */
public abstract class ResourceFactory {
	/** Creates a new instance of ResourceFactory */
	public ResourceFactory() {
	}

	public abstract GridElement create(boolean fixed, int geIndex, Link link, boolean isSE);
}
