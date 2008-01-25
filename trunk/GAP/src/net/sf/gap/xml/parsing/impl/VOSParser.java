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

import net.sf.gap.xml.parsing.Parser;
import net.sf.gap.xml.types.GridType;
import net.sf.gap.xml.types.VOSType;
import net.sf.gap.xml.types.VOType;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

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
                VOType aVO = new VOType(voName);
                NodeList vogeItems = voItem.getElementsByTagName("vogeItem");
                for (int j = 0; j < vogeItems.getLength(); j++) {
                    Element vogeItem = (Element) vogeItems.item(j);
                    aVO.addGE(vogeItem.getAttribute("name"));
                }
                vos.addVO(aVO);
            }
        } else {
            vos = new VOSType(grid);
        }
        return vos;
    }
}
