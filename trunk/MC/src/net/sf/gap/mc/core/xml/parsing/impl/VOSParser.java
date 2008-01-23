/*
 ****************************************************************************************
 * Copyright © Giovanni Novelli                                             
 * All Rights Reserved.                                                                 
 ****************************************************************************************
 *
 * License:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * VOSParser.java
 *
 * Created on 22 January 2008, 09.00 by Giovanni Novelli
 *
 * $Id$
 *
 */

package net.sf.gap.mc.core.xml.parsing.impl;

import net.sf.gap.mc.core.xml.parsing.*;
import org.w3c.dom.*;

import net.sf.gap.mc.core.xml.types.*;

import net.sf.gap.mc.core.xml.parsing.Parser;

/**
 *
 * @author Giovanni Novelli
 */
public class VOSParser extends Parser {
    public VOSParser(Document document) {
        super(document);
    }
    
    public VOSType getVOS() {
        VOSType vos = null;
        return vos;
    }
}
