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
    
    public NetworkTopologyType getTopology() {
        NetworkTopologyType topology = null;
        try {
        XPathFactory xpfactory = XPathFactory.newInstance();
        XPath xpath = xpfactory.newXPath();
        XPathExpression expr 
         = xpath.compile("/scenario/*");
            Object results = expr.evaluate(document, XPathConstants.NODESET);
            NodeList nodes = (NodeList) results;
            for (int i = 0; i < nodes.getLength(); i++) {
                System.out.println(
                        nodes.item(i).getParentNode().getNodeName() 
                        + " " + 
                        nodes.item(i).getNodeValue()); 
            }
        } catch (XPathExpressionException e) {
            System.err.println("XPathExpressionException caught...");
            e.printStackTrace();
        }
        return topology;
    }
    
    public Document getDocument() {
        return document;
    }

    public void setDocument(Document document) {
        this.document = document;
    }
}
