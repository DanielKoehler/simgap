/*
 ****************************************************************************************
 * Copyright © Giovanni Novelli                                             
 * All Rights Reserved.                                                                 
 ****************************************************************************************
 *
 * MuMRepository.java
 *
 * Created on 15 March 2007, 21.06
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 *
 * $Id$
 *
 */

package net.sf.gap.mc.qagesa.agents.services.impl.mum;

import java.util.Iterator;
import java.util.HashMap;

/**
 * 
 * @author admin
 */
public class MuMRepository extends HashMap<String, GEList> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8523491610508912425L;

        public boolean containsSE(GEList list, int se_id) {
            boolean result = false;
            Iterator it = list.iterator();
            while (it.hasNext()) {
                Integer id = (Integer) it.next();
                if (id==se_id) {
                    result = true;
                    break;
                }
            }
            return result;
        }
        
	public GEList put(String repTag, int se_id) {
            GEList list;
            if (this.containsKey(repTag)) {
                    list = this.get(repTag);
            } else {
                    list = new GEList();
            }
            if (!this.containsSE(list, se_id)) {
                list.add(se_id);
                return this.put(repTag, list);
            } else {
                return list;
            }
	}

	public GEList getGEList(String name) {
		return this.get(name);
	}
}
