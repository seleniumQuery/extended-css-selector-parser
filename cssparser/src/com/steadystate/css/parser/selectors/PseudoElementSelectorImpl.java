/*
 * $Id: PseudoElementSelectorImpl.java,v 1.3 2006-10-27 13:30:04 waldbaer Exp $
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

import org.w3c.css.sac.*;
import java.io.Serializable;

/**
 *
 * @author <a href="mailto:davidsch@users.sourceforge.net">David Schweinsberg</a>
 * @version $Id: PseudoElementSelectorImpl.java,v 1.3 2006-10-27 13:30:04 waldbaer Exp $
 */
public class PseudoElementSelectorImpl implements ElementSelector, Serializable {

    private String localName;

    public void setLocaleName(String localName)
    {
        this.localName = localName;
    }


    public PseudoElementSelectorImpl(String localName) {
        this.localName = localName;
    }

    public PseudoElementSelectorImpl()
    {
    }


    public short getSelectorType() {
        return Selector.SAC_PSEUDO_ELEMENT_SELECTOR;
    }

    public String getNamespaceURI() {
        return null;
    }

    public String getLocalName() {
        return this.localName;
    }
}
