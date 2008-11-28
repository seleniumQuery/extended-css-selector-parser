/*
 * $Id: DirectAdjacentSelectorImpl.java,v 1.2 2008-11-28 13:05:17 waldbaer Exp $
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

import com.steadystate.css.parser.Locatable;
import com.steadystate.css.parser.LocatableImpl;

/**
 *
 * @author <a href="mailto:davidsch@users.sourceforge.net">David Schweinsberg</a>
 * @version $Id: DirectAdjacentSelectorImpl.java,v 1.2 2008-11-28 13:05:17 waldbaer Exp $
 */
public class DirectAdjacentSelectorImpl extends LocatableImpl implements SiblingSelector, Serializable {

    private static final long serialVersionUID = -7328602345833826516L;

    private short nodeType;
    private Selector selector;  // child
    private SimpleSelector siblingSelector; // direct adjacent

    public void setNodeType(short nodeType)
    {
        this.nodeType = nodeType;
    }

    public void setSelector(Selector child)
    {
        this.selector = child;
        if (child instanceof Locatable)
        {
        	this.setLocator(((Locatable) child).getLocator());
        }
        else if (child == null)
        {
        	this.setLocator(null);
        }
    }

    public void setSiblingSelector(SimpleSelector directAdjacent)
    {
        this.siblingSelector = directAdjacent;
    }


    public DirectAdjacentSelectorImpl(short nodeType, Selector child, SimpleSelector directAdjacent) {
        this.setNodeType(nodeType);
        this.setSelector(child);
        this.setSiblingSelector(directAdjacent);
    }

    public DirectAdjacentSelectorImpl()
    {
    }


    public short getNodeType() {
        return this.nodeType;
    }
    
    public short getSelectorType() {
        return Selector.SAC_DIRECT_ADJACENT_SELECTOR;
    }

    public Selector getSelector() {
        return this.selector;
    }

    public SimpleSelector getSiblingSelector() {
        return this.siblingSelector;
    }
    
    public String toString() {
        return this.selector.toString() + " + " + this.siblingSelector.toString();
    }
}
