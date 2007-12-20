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
 * Predicate.java
 *
 * Created on 21 August 2006, 12.00 by Giovanni Novelli
 *
 ****************************************************************************************
 *
 * $Revision: 1141 $
 * $Id: Predicate.java 1141 2007-07-18 18:22:15Z gnovelli $
 * $HeadURL: file:///var/svn/grid/trunk/GAP/src/net/sf/gap/agents/predicates/Predicate.java $
 *
 *****************************************************************************************
 */

package net.sf.gap.agents.predicates;

import eduni.simjava.Sim_event;
import eduni.simjava.Sim_predicate;

/**
 * 
 * @author Giovanni Novelli
 */
public class Predicate extends Sim_predicate {
	private int[] tags;

	/** Creates a new instance of Predicate */
	public Predicate(int tag) {
            int[] atag = {tag};
            this.setTags(atag);
	}

	/** Creates a new instance of Predicate */
	public Predicate(int[] tags) {
            this.setTags(tags);
	}

	@Override
	public boolean match(Sim_event ev) {
            int nt = this.getTags().length;
            boolean matched = false;
            for (int i=0;i<nt;i++) {
                  matched = matched || (this.getTags()[i]==ev.get_tag());
            }
            return matched;
	}

    public int[] getTags() {
        return tags;
    }

    public void setTags(int[] tags) {
        this.tags = tags;
    }
}
