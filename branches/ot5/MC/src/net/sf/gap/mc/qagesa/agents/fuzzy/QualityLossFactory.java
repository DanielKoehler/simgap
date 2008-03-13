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
 * QualityLossFactory.java
 *
 * Created on 16 September 2007, 11.45 by Giovanni Novelli
 *
 * $Id$
 *
 */

package net.sf.gap.mc.qagesa.agents.fuzzy;

/**
 *
 * @author Giovanni Novelli
 */
public class QualityLossFactory {
    public static enum Type {DELAY_RAW};
    
    public static AbstractQualityLoss create(Type aType) {
        AbstractQualityLoss ql;
        switch (aType) {
            case DELAY_RAW:
                ql = new DelayQualityLoss();
                break;
            default:
                ql = new DelayQualityLoss();
                break;
        }
        return ql;
    }
}
