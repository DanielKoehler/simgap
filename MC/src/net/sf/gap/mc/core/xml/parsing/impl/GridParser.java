/*
 ****************************************************************************************
 * Copyright © Giovanni Novelli                                             
 * All Rights Reserved.                                                                 
 ****************************************************************************************
 *
 * License:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * GridParser.java
 *
 * Created on 22 January 2008, 09.00 by Giovanni Novelli
 *
 * $Id$
 *
 */

package net.sf.gap.mc.core.xml.parsing.impl;

import net.sf.gap.mc.core.xml.parsing.*;
import org.w3c.dom.*;

import javax.xml.xpath.*;

import net.sf.gap.mc.core.xml.types.*;

import net.sf.gap.mc.core.xml.parsing.Parser;

/**
 *
 * @author Giovanni Novelli
 */
public class GridParser extends Parser {
    public GridParser(Document document) {
        super(document);
    }
    
    public GridType getGrid() {
        GridType grid = null;
        NodeList gridElement = this.getDocument().getElementsByTagName("grid");
        if (gridElement.getLength()>0) {
            NodeList gridElements = this.getDocument().getElementsByTagName("gridElement");
            if (gridElements.getLength()>0) {
            } else {
                System.out.println("There are NOT grid elements");
            }
        } else {
            System.out.println("There is NOT a grid environment in the scenario");
        }
        return grid;
    }
}
