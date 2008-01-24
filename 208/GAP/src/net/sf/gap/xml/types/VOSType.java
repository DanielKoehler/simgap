/*
 ****************************************************************************************
 * Copyright © Giovanni Novelli                                             
 * All Rights Reserved.                                                                 
 ****************************************************************************************
 *
 * License:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * VOSType.java
 *
 * Created on 22 January 2008, 09.00 by Giovanni Novelli
 *
 * $Id$
 *
 */

package net.sf.gap.xml.types;

/**
 *
 * @author Giovanni Novelli
 */
public class VOSType {
    private VOType vo;
    
    public VOSType(VOType vo) {
        this.setVo(vo);
    }

    public VOType getVo() {
        return vo;
    }

    public void setVo(VOType vo) {
        this.vo = vo;
    }
}
