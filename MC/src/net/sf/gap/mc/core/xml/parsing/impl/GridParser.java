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
                grid = new GridType();
                for (int ig=0;ig < gridElements.getLength();ig++) {
                    Element geItem = (Element) gridElements.item(ig);
                    GridElementType gridElementInstance = new GridElementType();
                    gridElementInstance.setName(geItem.getAttribute("name"));
                    NodeList machineListItems = geItem.getElementsByTagName("Machine");
                    for (int i = 0; i < machineListItems.getLength(); i++) {
                        Element machineItem = (Element) machineListItems.item(i);
                        MachineType machine = new MachineType();
                        NodeList peListItems = machineItem.getElementsByTagName("PE");
                        for (int j = 0; j < peListItems.getLength(); j++) {
                            Element peItem = (Element) peListItems.item(j);
                            Element mipsItem = (Element) peItem.getElementsByTagName("MIPS").item(0);
                            int MIPS = Integer.parseInt(mipsItem.getTextContent());
                            machine.addPE(MIPS);
                        }
                        gridElementInstance.addMachine(machine);
                    }
                    grid.addGridElement(gridElementInstance);
                }
            }
        }
        return grid;
    }
}
