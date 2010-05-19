/*
 * $Id: PseudoClassConditionImpl.java,v 1.2 2008-11-28 13:05:17 waldbaer Exp $
 *
 * CSS Parser Project
 *
 * Copyright (C) 1999-2005 David Schweinsberg.  All rights reserved.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 * To contact the authors of the library:
 *
 * http://cssparser.sourceforge.net/
 * mailto:davidsch@users.sourceforge.net
 */

package com.steadystate.css.parser.selectors;

import java.io.Serializable;
import org.w3c.css.sac.*;

import com.steadystate.css.parser.LocatableImpl;

/**
 *
 * @author <a href="mailto:davidsch@users.sourceforge.net">David Schweinsberg</a>
 * @version $Id: PseudoClassConditionImpl.java,v 1.2 2008-11-28 13:05:17 waldbaer Exp $
 */
public class PseudoClassConditionImpl extends LocatableImpl implements AttributeCondition, Serializable {

    private static final long serialVersionUID = 1798016773089155610L;

    private String value;

    public void setValue(String value)
    {
        this.value = value;
    }


    public PseudoClassConditionImpl(String value) {
        this.value = value;
    }

    public PseudoClassConditionImpl()
    {
    }


    public short getConditionType() {
        return Condition.SAC_PSEUDO_CLASS_CONDITION;
    }

    public String getNamespaceURI() {
        return null;
    }

    public String getLocalName() {
        return null;
    }

    public boolean getSpecified() {
        return true;
    }

    public String getValue() {
        return this.value;
    }
    
    public String toString() {
        return ":" + this.getValue();
    }
}