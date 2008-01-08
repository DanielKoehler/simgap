/*
 ****************************************************************************************
 * Copyright © Giovanni Novelli                                             
 * All Rights Reserved.                                                                 
 ****************************************************************************************
 *
 * License:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * ExperimentsVirtualOrganization.java
 *
 * Created on 7 August 2006, 14.40 by Giovanni Novelli
 *
 * $Id$
 *
 */

package net.sf.gap.mc.experiments.grid;

import net.sf.gap.mc.core.grid.COREVirtualOrganization;

/**
 *
 * @author Giovanni Novelli
 */
public class ExperimentsVirtualOrganization extends COREVirtualOrganization {
    public ExperimentsVirtualOrganization(boolean traceFlag, int numCE, int MIPS,
            int PEMax, int MMin, int MMax, int numSE, int GBMin,
            int GBMax, int routersPerCloud, int clouds, 
            boolean fixedInfrastructure, double factor, int numUsers, int whichMeasure) 
            throws Exception {
        super(
                traceFlag, numCE, MIPS,
                PEMax, MMin, MMax, numSE, GBMin,
                GBMax, routersPerCloud, clouds,
                fixedInfrastructure, factor, numUsers, whichMeasure
                );
    }
    
    protected void initializeAgents() {}
    
    public void createAndAttachAgentPlatform() throws Exception {}
    
    public void createAndAttachAgents() throws Exception {}
    
    public void createAndAttachUsers() throws Exception {}
}
