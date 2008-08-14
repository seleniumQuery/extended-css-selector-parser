/*
 * $Id: CSSStyleSheetListImpl.java,v 1.4 2008-08-14 08:17:55 waldbaer Exp $
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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.w3c.dom.css.CSSStyleSheet;

import org.w3c.dom.stylesheets.StyleSheet;
import org.w3c.dom.stylesheets.StyleSheetList;

import com.steadystate.css.util.LangUtils;

/**
 * Implementation of {@link StyleSheetList}.
 * 
 * @author <a href="mailto:waldbaer@users.sourceforge.net">Johannes Koch</a>
 * @version $Id: CSSStyleSheetListImpl.java,v 1.4 2008-08-14 08:17:55 waldbaer Exp $
 */
public class CSSStyleSheetListImpl implements StyleSheetList
{
    private List<CSSStyleSheet> cssStyleSheets;

    public List<CSSStyleSheet> getCSSStyleSheets()
    {
        if (this.cssStyleSheets == null)
        {
            this.cssStyleSheets = new ArrayList<CSSStyleSheet>();
        }
        return this.cssStyleSheets;
    }

    public void setCSSStyleSheets(List<CSSStyleSheet> cssStyleSheets)
    {
        this.cssStyleSheets = cssStyleSheets;
    }

    /** Creates a new instance of CSSStyleSheetListImpl */
    public CSSStyleSheetListImpl()
    {
    }

    // start StyleSheetList
    public int getLength()
    {
        return this.getCSSStyleSheets().size();
    }
    
    public StyleSheet item(int index)
    {
        return this.getCSSStyleSheets().get(index);
    }
    
    /**
     * Adds a CSSStyleSheet.
     *
     * @param cssStyleSheet the CSSStyleSheet
     */
    public void add(CSSStyleSheet cssStyleSheet)
    {
        this.getCSSStyleSheets().add(cssStyleSheet);
    }
    // end StyleSheetList
    
    /**
     * Merges all StyleSheets in this list into one.
     *
     * @return the new (merged) StyleSheet
     */
    public StyleSheet merge()
    {
        CSSStyleSheetImpl merged = new CSSStyleSheetImpl();
        CSSRuleListImpl cssRuleList = new CSSRuleListImpl();
        Iterator it = this.getCSSStyleSheets().iterator();
        while (it.hasNext())
        {
            CSSStyleSheetImpl cssStyleSheet = (CSSStyleSheetImpl) it.next();
            CSSMediaRuleImpl cssMediaRule =
                new CSSMediaRuleImpl(merged, null, cssStyleSheet.getMedia());
            cssMediaRule.setRuleList(
                (CSSRuleListImpl) cssStyleSheet.getCssRules());
            cssRuleList.add(cssMediaRule);
        }
        merged.setCssRules(cssRuleList);
        merged.setMediaText("all");
        return merged;
    }
    

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
        {
            return true;
        }
        if (!(obj instanceof StyleSheetList))
        {
            return false;
        }
        StyleSheetList ssl = (StyleSheetList) obj;
        return this.equalsStyleSheets(ssl);
    }

    private boolean equalsStyleSheets(StyleSheetList ssl)
    {
        if ((ssl == null) || (this.getLength() != ssl.getLength()))
        {
            return false;
        }
        for (int i = 0; i < this.getLength(); i++)
        {
            StyleSheet styleSheet1 = this.item(i);
            StyleSheet styleSheet2 = ssl.item(i);
            if (!LangUtils.equals(styleSheet1, styleSheet2))
            {
                return false;
            }
        }
        return true;
    }

    @Override
    public int hashCode()
    {
        int hash = LangUtils.HASH_SEED;
        hash = LangUtils.hashCode(hash, this.cssStyleSheets);
        return hash;
    }

}
