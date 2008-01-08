/*
 ****************************************************************************************
 * Copyright © Giovanni Novelli                                             
 * All Rights Reserved.                                                                 
 ****************************************************************************************
 *
 * License:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * ExperimentsEntityTypes.java
 *
 * Created on 8 January 2008, 11.51 by Giovanni Novelli
 *
 * $Id$
 *
 */

package net.sf.gap.mc.experiments.constants;

import net.sf.gap.mc.qagesa.constants.*;
import net.sf.gap.constants.EntityTypes;

/**
 * 
 * @author Giovanni Novelli
 */
public class ExperimentsEntityTypes extends EntityTypes {
	public static final int USER_USER = 2004;

	/**
	 * Creates a new instance of ExperimentsEntityTypes
	 */
	public ExperimentsEntityTypes() {
	}

	@Override
	public String otherTypes(int entityType) {
		String str = null;
		switch (entityType) {
		case ExperimentsEntityTypes.USER_USER:
			str = "USER_USER";
			break;
		default:
			str = "UNKNOWN_ENTITY_TYPE";
			break;
		}
		return str;
	}
}
