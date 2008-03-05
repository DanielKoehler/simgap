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
 * AbstractQualityLoss.java
 *
 * Created on 16 September 2007, 10.26 by Giovanni Novelli
 *
 * $Id$
 *
 */
package net.sf.gap.mc.qagesa.agents.fuzzy;

/**
 *
 * @author Giovanni Novelli
 */
public abstract class AbstractQualityLoss {
    public abstract double predict(double delay, double minQuality, double currentQuality);
}
