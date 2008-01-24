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
 * EntityTypes.java
 *
 * Created on 19 August 2006, 12.00 by Giovanni Novelli
 *
 ****************************************************************************************
 *
 * $Revision$
 * $Id$
 * $HeadURL$
 *
 *****************************************************************************************
 */

package net.sf.gap.constants;

/**
 * 
 * @author Giovanni Novelli
 */
public abstract class EntityTypes {

	private static EntityTypes instance = null;

	/**
	 * Creates a new instance of EntityTypes
	 */
	public EntityTypes() {
	}

        public static final int ENTITYBASE = 200;

        /*
         * @TODO Somewhere in the code NOBODY tag is used as value 0, fix it
         * 
         */
	public static final int NOBODY = 0;

	public static final int AGENT_ZOMBIE = 1 + ENTITYBASE;

	public static final int USER = 2 + ENTITYBASE;

	public static final String toString(int entityType) {
		if (getInstance() != null) {
			return getInstance().intToString(entityType);
		} else {
			return "EntityTypes instance wasn't initilialised";
		}
	}

	protected String intToString(int entityType) {
		switch (entityType) {
		case EntityTypes.NOBODY:
			return "NOBODY";
		case EntityTypes.AGENT_ZOMBIE:
			return "AGENT_ZOMBIE";
		case EntityTypes.USER:
			return "USER";
		default:
			return getInstance().otherTypes(entityType);
		}
	}

	protected abstract String otherTypes(int entityType);

	public static final EntityTypes getInstance() {
		return instance;
	}

	public static final void setInstance(EntityTypes instance) {
		EntityTypes.instance = instance;
	}
}
