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

package net.sf.gap.xml.parsing.impl;

import net.sf.gap.xml.parsing.*;
import org.w3c.dom.*;

import net.sf.gap.xml.types.*;

import net.sf.gap.xml.parsing.Parser;

/**
 *
 * @author Giovanni Novelli
 */
public class VOSParser extends Parser {
    public VOSParser(Document document) {
        super(document);
    }
    
    public VOSType getVOS(GridType grid) {
        VOSType vos;
        NodeList voItems = this.getDocument().getElementsByTagName("voItem");
        if (voItems.getLength()>0) {
            vos = new VOSType();
            for (int i = 0; i < voItems.getLength(); i++) {
                Element voItem = (Element) voItems.item(i);
                String voName = voItem.getAttribute("name");
                
            }
        } else {
            vos = new VOSType(grid);
        }
        return vos;
    }
}
