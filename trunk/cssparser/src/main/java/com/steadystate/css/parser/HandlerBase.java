/*
 * $Id: HandlerBase.java,v 1.3 2008-11-27 14:49:13 waldbaer Exp $
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

package com.steadystate.css.parser;

import org.w3c.css.sac.*;

import com.steadystate.css.sac.DocumentHandlerExt;

public class HandlerBase implements DocumentHandlerExt, ErrorHandler {

    public void startDocument(InputSource source)
        throws CSSException {
    }
    
    public void endDocument(InputSource source) throws CSSException {
    }

    public void comment(String text) throws CSSException {
    }

    public void ignorableAtRule(String atRule) throws CSSException {
    }

    public void ignorableAtRule(String atRule, Locator locator)
        throws CSSException
    {
    }

    public void namespaceDeclaration(String prefix, String uri)
	    throws CSSException {
    }

    public void importStyle(String uri, SACMediaList media, 
			String defaultNamespaceURI)
	    throws CSSException {
    }

    public void importStyle(String uri, SACMediaList media, 
        String defaultNamespaceURI, Locator locator)
        throws CSSException
    {
    }

    public void startMedia(SACMediaList media) throws CSSException {
    }

    public void startMedia(SACMediaList media, Locator locator)
        throws CSSException
    {
    }

    public void endMedia(SACMediaList media) throws CSSException {
    }

    public void startPage(String name, String pseudo_page) throws CSSException {
    }

    public void endPage(String name, String pseudo_page) throws CSSException {
    }

    public void startFontFace() throws CSSException {
    }

    public void endFontFace() throws CSSException {
    }

    public void startSelector(SelectorList selectors) throws CSSException {
    }

    public void startSelector(SelectorList selectors, Locator locator)
        throws CSSException
    {
    }

    public void endSelector(SelectorList selectors) throws CSSException {
    }

    public void property(String name, LexicalUnit value, boolean important)
        throws CSSException {
    }

    public void property(String name, LexicalUnit value, boolean important,
        Locator locator)
	{
	}

    public void charset(String characterEncoding, Locator locator)
        throws CSSException
    {
    }

    public void warning(CSSParseException exception) throws CSSException {
        StringBuilder sb = new StringBuilder();
        sb.append(exception.getURI())
            .append(" [")
            .append(exception.getLineNumber())
            .append(":")
            .append(exception.getColumnNumber())
            .append("] ")
            .append(exception.getMessage());
        System.err.println(sb.toString());
    }

    public void error(CSSParseException exception) throws CSSException {
        StringBuilder sb = new StringBuilder();
        sb.append(exception.getURI())
            .append(" [")
            .append(exception.getLineNumber())
            .append(":")
            .append(exception.getColumnNumber())
            .append("] ")
            .append(exception.getMessage());
        System.err.println(sb.toString());
    }

    public void fatalError(CSSParseException exception) throws CSSException {
        StringBuilder sb = new StringBuilder();
        sb.append(exception.getURI())
            .append(" [")
            .append(exception.getLineNumber())
            .append(":")
            .append(exception.getColumnNumber())
            .append("] ")
            .append(exception.getMessage());
        System.err.println(sb.toString());
    }

}
