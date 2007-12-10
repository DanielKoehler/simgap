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
 * MeasuresContainer.java
 *
 * Created on 29 March 2007, 22.35 by Giovanni Novelli
 *
 * $Id: MeasuresContainer.java 1142 2007-07-18 20:49:58Z gnovelli $
 *
 */

package net.sf.qagesa.measures;

import net.sf.gap.measures.Measure;
import net.sf.qagesa.constants.QAGESATags;

/**
 *
 * @author Giovanni Novelli
 */
public class MeasuresContainer {
    private Measure RESPONSE_TIME;
    
    /** Creates a new instance of MeasuresContainer */
    public MeasuresContainer() throws Exception {
        this.setRESPONSE_TIME(new Measure("RESPONSE_TIME", QAGESATags.RESPONSE_TIME_REQ, QAGESATags.RESPONSE_TIME_REP));
    }
    
    public void initialize() {
        this.getRESPONSE_TIME().initialize();
    }

    public Measure getRESPONSE_TIME() {
        return RESPONSE_TIME;
    }

    public void setRESPONSE_TIME(Measure RESPONSE_TIME) {
        this.RESPONSE_TIME = RESPONSE_TIME;
    }

}
