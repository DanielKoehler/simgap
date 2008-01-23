/*
 ****************************************************************************************
 * Copyright © Giovanni Novelli                                             
 * All Rights Reserved.                                                                 
 ****************************************************************************************
 *
 * License:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * NetworkTopologyType.java
 *
 * Created on 22 January 2008, 09.00 by Giovanni Novelli
 *
 * $Id$
 *
 */

package net.sf.gap.mc.core.xml.types;

import java.util.LinkedList;

/**
 *
 * @author Giovanni Novelli
 */
public class NetworkTopologyType {
    private LinkedList<RouterType> routers;
    private LinkedList<String> ges;
    private LinkedList<LinkType> links;

    public NetworkTopologyType() {
        this.setRouters(new LinkedList<RouterType>());
        this.setGes(new LinkedList<String>());
        this.setLinks(new LinkedList<LinkType>());
    }
    
    public boolean addRouter(String routerName) {
        return this.getRouters().add(new RouterType(routerName));
    }

    public boolean addGE(String geName) {
        return this.getGes().add(geName);
    }
    
    public boolean addRouter(RouterType router) {
        return this.getRouters().add(router);
    }
    
    public boolean addLink(LinkType link) {
        return this.getLinks().add(link);
    }
    
    public LinkedList<RouterType> getRouters() {
        return routers;
    }

    public void setRouters(LinkedList<RouterType> routers) {
        this.routers = routers;
    }

    public LinkedList<LinkType> getLinks() {
        return links;
    }

    public void setLinks(LinkedList<LinkType> links) {
        this.links = links;
    }

    public LinkedList<String> getGes() {
        return ges;
    }

    public void setGes(LinkedList<String> ges) {
        this.ges = ges;
    }
    
    
}
