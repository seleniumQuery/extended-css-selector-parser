/*
 * $Id: RectImpl.java,v 1.3 2005-10-12 08:47:13 waldbaer Exp $
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
 
package com.steadystate.css.dom;

import java.io.Serializable;

import org.w3c.dom.DOMException;
import org.w3c.dom.css.CSSPrimitiveValue;
import org.w3c.dom.css.Rect;

import org.w3c.css.sac.LexicalUnit;

/** 
 *
 * @author <a href="mailto:davidsch@users.sourceforge.net">David Schweinsberg</a>
 * @version $Id: RectImpl.java,v 1.3 2005-10-12 08:47:13 waldbaer Exp $
 */
public class RectImpl implements Rect, Serializable {
    
    private CSSPrimitiveValue _left;
    private CSSPrimitiveValue _top;
    private CSSPrimitiveValue _right;
    private CSSPrimitiveValue _bottom;

    /** Creates new RectImpl */
    public RectImpl(LexicalUnit lu) throws DOMException {
        LexicalUnit next = lu;
        this._left = new CSSValueImpl(next, true);
        next = next.getNextLexicalUnit();  // ,
        if (next != null)
        {
            if (next.getLexicalUnitType() != LexicalUnit.SAC_OPERATOR_COMMA)
            {
                // error
                throw new DOMException(DOMException.SYNTAX_ERR,
                    "Rect parameters must be separated by ','.");
            }
            next = next.getNextLexicalUnit();
            if (next != null)
            {
                this._top = new CSSValueImpl(next, true);
                next = next.getNextLexicalUnit();   // ,
                if (next != null)
                {
                    if (next.getLexicalUnitType() != LexicalUnit.SAC_OPERATOR_COMMA)
                    {
                        // error
                        throw new DOMException(DOMException.SYNTAX_ERR,
                            "Rect parameters must be separated by ','.");
                    }
                    next = next.getNextLexicalUnit();
                    if (next != null)
                    {
                        this._right = new CSSValueImpl(next, true);
                        next = next.getNextLexicalUnit();   // ,
                        if (next != null)
                        {
                            if (next.getLexicalUnitType() != LexicalUnit.SAC_OPERATOR_COMMA)
                            {
                                // error
                                throw new DOMException(DOMException.SYNTAX_ERR,
                                    "Rect parameters must be separated by ','.");
                            }
                            next = next.getNextLexicalUnit();
                            if (next != null)
                            {
                                this._bottom = new CSSValueImpl(next, true);
                                if ((next = next.getNextLexicalUnit()) != null)
                                {
                                    // error
                                    throw new DOMException(DOMException.SYNTAX_ERR,
                                        "Too many parameters for rect function.");
                                }
                            }
                        }
                    }
                }
            }
        }
    }
  
    public CSSPrimitiveValue getTop() {
        return this._top;
    }

    public CSSPrimitiveValue getRight() {
        return this._right;
    }

    public CSSPrimitiveValue getBottom() {
        return this._bottom;
    }

    public CSSPrimitiveValue getLeft() {
        return this._left;
    }
    
    public String toString() {
        return new StringBuffer("rect(")
            .append(this._left).append(", ")
            .append(this._top).append(", ")
            .append(this._right).append(", ")
            .append(this._bottom).append(")").toString();
    }
}