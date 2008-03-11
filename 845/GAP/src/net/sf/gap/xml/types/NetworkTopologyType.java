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

package net.sf.gap.xml.types;

import java.util.HashMap;
import java.util.LinkedList;

/**
 * 
 * @author Giovanni Novelli
 */
public class NetworkTopologyType {
	private RouterListType routers;
	private LinkedList<String> ges;
	private LinkedList<LinkType> links;
	private HashMap<String, LinkType> mapLinks;

	public NetworkTopologyType() {
		this.setRouters(new RouterListType());
		this.setGes(new LinkedList<String>());
		this.setLinks(new LinkedList<LinkType>());
		this.setMapLinks(new HashMap<String, LinkType>());
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
		this.getMapLinks().put(link.getName(), link);
		return this.getLinks().add(link);
	}

	public RouterListType getRouters() {
		return routers;
	}

	public void setRouters(RouterListType routers) {
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

	public HashMap<String, LinkType> getMapLinks() {
		return mapLinks;
	}

	public void setMapLinks(HashMap<String, LinkType> mapLinks) {
		this.mapLinks = mapLinks;
	}

}
