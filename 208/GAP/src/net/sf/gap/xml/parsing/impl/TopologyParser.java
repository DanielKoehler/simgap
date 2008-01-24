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

package net.sf.gap.xml.parsing.impl;

import org.w3c.dom.*;

import net.sf.gap.xml.parsing.*;
import net.sf.gap.xml.types.*;

/**
 *
 * @author Giovanni Novelli
 */
public class TopologyParser extends Parser {
    public TopologyParser(Document document) {
        super(document);
    }

    public NetworkTopologyType getTopology() {
        NetworkTopologyType topology = new NetworkTopologyType();
        NodeList routerItems = this.getDocument().getElementsByTagName("routerItem");
        for (int i = 0; i < routerItems.getLength(); i++) {
            Element routerItem = (Element) routerItems.item(i);
            topology.addRouter(routerItem.getAttribute("name"));
        }
        NodeList geItems = this.getDocument().getElementsByTagName("geItem");
        for (int i = 0; i < geItems.getLength(); i++) {
            Element geItem = (Element) geItems.item(i);
            topology.addGE(geItem.getAttribute("name"));
        }
        NodeList netLinkItems = this.getDocument().getElementsByTagName("netLinkItem");
        for (int i = 0; i < netLinkItems.getLength(); i++) {
            Element linkItem = (Element) netLinkItems.item(i);
            String aName = linkItem.getAttribute("name");
            Element element;
            element = (Element) linkItem.getElementsByTagName("Baudrate").item(0);
            double aBaudrate = Double.parseDouble(element.getTextContent());
            element = (Element) linkItem.getElementsByTagName("Delay").item(0);
            double aDelay = Double.parseDouble(element.getTextContent());
            element = (Element) linkItem.getElementsByTagName("MTU").item(0);
            int aMTU = Integer.parseInt(element.getTextContent());
            element = (Element) linkItem.getElementsByTagName("fromEntity").item(0);
            String fromEntity = element.getTextContent();
            element = (Element) linkItem.getElementsByTagName("toEntity").item(0);
            String toEntity = element.getTextContent();
            element = (Element) linkItem.getElementsByTagName("bidirectional").item(0);
            boolean aBidirectional = true;
            if (element!=null) {
            aBidirectional = Boolean.parseBoolean(element.getTextContent());
            }
            LinkType link = 
                    new LinkType(
                    aName,
                    aBaudrate,
                    aDelay,
                    aMTU,
                    fromEntity,
                    toEntity,
                    aBidirectional);
            topology.addLink(link);
        }
        NodeList geLinkItems = this.getDocument().getElementsByTagName("geLinkItem");
        for (int i = 0; i < geLinkItems.getLength(); i++) {
            Element linkItem = (Element) geLinkItems.item(i);
            String aName = linkItem.getAttribute("name");
            Element element;
            element = (Element) linkItem.getElementsByTagName("Baudrate").item(0);
            double aBaudrate = Double.parseDouble(element.getTextContent());
            element = (Element) linkItem.getElementsByTagName("Delay").item(0);
            double aDelay = Double.parseDouble(element.getTextContent());
            element = (Element) linkItem.getElementsByTagName("MTU").item(0);
            int aMTU = Integer.parseInt(element.getTextContent());
            element = (Element) linkItem.getElementsByTagName("fromEntity").item(0);
            String fromEntity = element.getTextContent();
            element = (Element) linkItem.getElementsByTagName("toEntity").item(0);
            String toEntity = element.getTextContent();
            element = (Element) linkItem.getElementsByTagName("bidirectional").item(0);
            boolean aBidirectional = true;
            if (element!=null) {
            aBidirectional = Boolean.parseBoolean(element.getTextContent());
            }
            LinkType link = 
                    new LinkType(
                    aName,
                    aBaudrate,
                    aDelay,
                    aMTU,
                    fromEntity,
                    toEntity,
                    aBidirectional);
            topology.addLink(link);
        }
        return topology;
    }
}
