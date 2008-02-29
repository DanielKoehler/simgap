/*
 ****************************************************************************************
 * Copyright © Giovanni Novelli                                             
 * All Rights Reserved.                                                                 
 ****************************************************************************************
 *
 * License:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * RouterListType.java
 *
 * Created on 5 February 2008, 09.00 by Giovanni Novelli
 *
 * $Id$
 *
 */

package net.sf.gap.xml.types;

import java.util.LinkedList;

/**
 *
 * @author Giovanni Novelli
 */
public class RouterListType extends LinkedList<RouterType> {
    public boolean contains(RouterType o) {
        int n = this.size();
        boolean result = false;
        for (int i=0;i<n;i++) {
           if (this.get(i).equals(o)) {
               result=true;
               break;
           }
        }
        return result;
    }
}
