/*
 * $Id: MediaListImpl.java,v 1.3 2005-07-14 00:25:05 davidsch Exp $
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

import java.io.IOException;
import java.io.Serializable;
import java.io.StringReader;

import java.util.Vector;

import org.w3c.dom.DOMException;

import org.w3c.dom.stylesheets.MediaList;

import org.w3c.css.sac.InputSource;
import org.w3c.css.sac.CSSParseException;
import org.w3c.css.sac.SACMediaList;

import com.steadystate.css.parser.SACParser;

/**
 *
 * @author <a href="mailto:davidsch@users.sourceforge.net">David Schweinsberg</a>
 * @version $Id: MediaListImpl.java,v 1.3 2005-07-14 00:25:05 davidsch Exp $
 */
public class MediaListImpl implements MediaList, Serializable {

    private Vector _media = new Vector();
    
    private void setMedia(SACMediaList mediaList)
    {
        for (int i = 0; i < mediaList.getLength(); i++)
        {
            this._media.addElement(mediaList.item(i));
        }
    }

    public MediaListImpl(SACMediaList mediaList)
    {
        this.setMedia(mediaList);
    }

    public String getMediaText() {
        StringBuffer sb = new StringBuffer("");
        for (int i = 0; i < _media.size(); i++) {
            sb.append(_media.elementAt(i).toString());
            if (i < _media.size() - 1) {
                sb.append( ", " );
            }
        }
        return sb.toString();
    }

    public void setMediaText(String mediaText) throws DOMException {
        InputSource source = new InputSource(new StringReader(mediaText));
        try
        {
            this.setMedia(new SACParser().parseMedia(source));
        }
        catch (CSSParseException e)
        {
            throw new DOMException(DOMException.SYNTAX_ERR,
                e.getLocalizedMessage());
        }
        catch (IOException e)
        {
            throw new DOMException(DOMException.NOT_FOUND_ERR,
                e.getLocalizedMessage());
        }
    }

    public int getLength() {
        return _media.size();
    }

    public String item(int index) {
        return (index < _media.size()) ? (String) _media.elementAt(index) : null;
    }

    public void deleteMedium(String oldMedium) throws DOMException {
        for (int i = 0; i < _media.size(); i++) {
            String str = (String) _media.elementAt(i);
            if (str.equalsIgnoreCase(oldMedium)) {
                _media.removeElementAt(i);
                return;
            }
        }
        throw new DOMExceptionImpl(
            DOMException.NOT_FOUND_ERR,
            DOMExceptionImpl.NOT_FOUND);
    }

    public void appendMedium(String newMedium) throws DOMException {
        _media.addElement(newMedium);
    }

    public String toString() {
        return getMediaText();
    }
}
