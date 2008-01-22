/*
 ****************************************************************************************
 * Copyright © Giovanni Novelli                                             
 * All Rights Reserved.                                                                 
 ****************************************************************************************
 *
 * License:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * TopologyParser.java
 *
 * Created on 22 January 2008, 09.00 by Giovanni Novelli
 *
 * $Id$
 *
 */

package net.sf.gap.mc.core.xml;

import org.w3c.dom.*;

import javax.xml.xpath.*;

import net.sf.gap.mc.core.xml.types.*;

/**
 *
 * @author Giovanni Novelli
 */
public class TopologyParser {
    private Document document;

    public TopologyParser(Document document) {
        this.setDocument(document);
    }
    
    public Document getDocument() {
        return document;
    }

    public void setDocument(Document document) {
        this.document = document;
    }
}
