/*
 * $Id: SACParserCSS21Test.java,v 1.4 2009-09-11 11:54:38 waldbaer Exp $
 *
 * CSS Parser Project
 *
 * Copyright (C) 1999-2008 David Schweinsberg.  All rights reserved.
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

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.w3c.css.sac.AttributeCondition;
import org.w3c.css.sac.CSSException;
import org.w3c.css.sac.CombinatorCondition;
import org.w3c.css.sac.Condition;
import org.w3c.css.sac.ConditionalSelector;
import org.w3c.css.sac.DescendantSelector;
import org.w3c.css.sac.InputSource;
import org.w3c.css.sac.Parser;
import org.w3c.css.sac.Selector;
import org.w3c.css.sac.SelectorList;
import org.w3c.css.sac.SimpleSelector;
import org.w3c.dom.css.CSSStyleSheet;

public class SACParserCSS21Test extends TestCase {

    private static final Parser SAC_PARSER = new SACParserCSS21();

    public void testSelectorList() throws CSSException, IOException
    {
        this.testSelectorList("h1:first-line", 1);
        this.testSelectorList("h1", 1);
        this.testSelectorList("h1, h2", 2);
        this.testSelectorList("h1:first-line, h2", 2);
        this.testSelectorList("h1, h2, h3", 3);
        this.testSelectorList("h1, h2,\nh3", 3);
        this.testSelectorList("h1, h2, h3#id", 3);
        this.testSelectorList("h1.class, h2, h3", 3);
    }

    public void testSelector() throws CSSException, IOException
    {
        this.testSelectorType("a#id.class:link", Selector.SAC_CONDITIONAL_SELECTOR);
        this.testSelectorType("a#id.class", Selector.SAC_CONDITIONAL_SELECTOR);
        this.testSelectorType("a#id:link", Selector.SAC_CONDITIONAL_SELECTOR);
        this.testSelectorType("a#id", Selector.SAC_CONDITIONAL_SELECTOR);
        this.testSelectorType("a.class:link", Selector.SAC_CONDITIONAL_SELECTOR);
        this.testSelectorType("a.class", Selector.SAC_CONDITIONAL_SELECTOR);
        this.testSelectorType("a:link", Selector.SAC_CONDITIONAL_SELECTOR);
        this.testSelectorType("a", Selector.SAC_ELEMENT_NODE_SELECTOR);
        this.testSelectorType("#id.class:link", Selector.SAC_CONDITIONAL_SELECTOR);
        this.testSelectorType("#id.class", Selector.SAC_CONDITIONAL_SELECTOR);
        this.testSelectorType("#id:link", Selector.SAC_CONDITIONAL_SELECTOR);
        this.testSelectorType("#id", Selector.SAC_CONDITIONAL_SELECTOR);
        this.testSelectorType(".class:link", Selector.SAC_CONDITIONAL_SELECTOR);
        this.testSelectorType(".class", Selector.SAC_CONDITIONAL_SELECTOR);
        this.testSelectorType(":link", Selector.SAC_CONDITIONAL_SELECTOR);
        this.testSelectorType("a:visited", Selector.SAC_CONDITIONAL_SELECTOR);
        this.testSelectorType("a:active", Selector.SAC_CONDITIONAL_SELECTOR);

        this.testSelectorType("h1:first-line", Selector.SAC_DESCENDANT_SELECTOR, Selector.SAC_ELEMENT_NODE_SELECTOR, Selector.SAC_PSEUDO_ELEMENT_SELECTOR);
        this.testSelectorType("a:first-letter", Selector.SAC_DESCENDANT_SELECTOR, Selector.SAC_ELEMENT_NODE_SELECTOR, Selector.SAC_PSEUDO_ELEMENT_SELECTOR);

        this.testSelectorType("h1 a", Selector.SAC_DESCENDANT_SELECTOR, Selector.SAC_ELEMENT_NODE_SELECTOR, Selector.SAC_ELEMENT_NODE_SELECTOR);
        this.testSelectorType("h1  a", Selector.SAC_DESCENDANT_SELECTOR, Selector.SAC_ELEMENT_NODE_SELECTOR, Selector.SAC_ELEMENT_NODE_SELECTOR);
    }

    public void testCondition() throws CSSException, IOException
    {
        this.testConditionType("a#id.class:link", Condition.SAC_AND_CONDITION, Condition.SAC_AND_CONDITION, Condition.SAC_ID_CONDITION, Condition.SAC_CLASS_CONDITION, Condition.SAC_PSEUDO_CLASS_CONDITION);
        this.testConditionType("a#id.class", Condition.SAC_AND_CONDITION, Condition.SAC_ID_CONDITION, Condition.SAC_CLASS_CONDITION);
        this.testConditionType("a#id:link", Condition.SAC_AND_CONDITION, Condition.SAC_ID_CONDITION, Condition.SAC_PSEUDO_CLASS_CONDITION);
        this.testConditionType("a#id", Condition.SAC_ID_CONDITION);
        this.testConditionType("a.class:link", Condition.SAC_AND_CONDITION, Condition.SAC_CLASS_CONDITION, Condition.SAC_PSEUDO_CLASS_CONDITION);
        this.testConditionType("a.class", Condition.SAC_CLASS_CONDITION);
        this.testConditionType("a:link", Condition.SAC_PSEUDO_CLASS_CONDITION);
        this.testConditionType("#id.class:link", Condition.SAC_AND_CONDITION, Condition.SAC_AND_CONDITION, Condition.SAC_ID_CONDITION, Condition.SAC_CLASS_CONDITION, Condition.SAC_PSEUDO_CLASS_CONDITION);
        this.testConditionType("#id.class", Condition.SAC_AND_CONDITION, Condition.SAC_ID_CONDITION, Condition.SAC_CLASS_CONDITION);
        this.testConditionType("#id:link", Condition.SAC_AND_CONDITION, Condition.SAC_ID_CONDITION, Condition.SAC_PSEUDO_CLASS_CONDITION);
        this.testConditionType("#id", Condition.SAC_ID_CONDITION);
        this.testConditionType(".class:link", Condition.SAC_AND_CONDITION, Condition.SAC_CLASS_CONDITION, Condition.SAC_PSEUDO_CLASS_CONDITION);
        this.testConditionType(".class", Condition.SAC_CLASS_CONDITION);
        this.testConditionType(":link", Condition.SAC_PSEUDO_CLASS_CONDITION);
    }

    public void testAttributeCondition() throws CSSException, IOException
    {
        this.testAttributeConditionValue(".class", "class");
        this.testAttributeConditionValue("#id", "id");
        this.testAttributeConditionValue("h1.class", "class");
        this.testAttributeConditionValue("h1#id", "id");
        this.testAttributeConditionValue("a:link", "link");
        this.testAttributeConditionValue(":link", "link");
        this.testAttributeConditionValue("a:visited", "visited");
        this.testAttributeConditionValue(":visited", "visited");
        this.testAttributeConditionValue("a:active", "active");
        this.testAttributeConditionValue(":active", "active");
    }


    private void testSelectorList(String cssText, int length)
        throws CSSException, IOException
    {
        SelectorList selectors = this.createSelectors(cssText);
        Assert.assertEquals(length, selectors.getLength());
    }

    private void testSelectorType(String cssText, int... selectorTypes)
        throws CSSException, IOException
    {
        SelectorList selectors = this.createSelectors(cssText);
        Selector selector = selectors.item(0);
        Assert.assertEquals(selectorTypes[0], selector.getSelectorType());
        if (selectorTypes[0] == Selector.SAC_DESCENDANT_SELECTOR)
        {
            DescendantSelector descendantSelector =
                (DescendantSelector) selector;
            Selector ancestor = descendantSelector.getAncestorSelector();
            Assert.assertEquals(selectorTypes[1], ancestor.getSelectorType());
            SimpleSelector simple = descendantSelector.getSimpleSelector();
            Assert.assertEquals(selectorTypes[2], simple.getSelectorType());
        }
    }

    private void testConditionType(String cssText, int... conditionTypes)
        throws CSSException, IOException
    {
        Condition condition = this.createCondition(cssText);
        this.testConditionType(condition, 0, conditionTypes);
    }

    private int testConditionType(Condition condition, int initial, int... conditionTypes)
    {
        Assert.assertEquals(conditionTypes[initial], condition.getConditionType());
        if (conditionTypes[initial] == Condition.SAC_AND_CONDITION)
        {
            CombinatorCondition combinatorCondition =
                (CombinatorCondition) condition;
            Condition first = combinatorCondition.getFirstCondition();
            Condition second = combinatorCondition.getSecondCondition();
            initial = this.testConditionType(first, ++initial, conditionTypes);
            initial = this.testConditionType(second, ++initial, conditionTypes);
        }
        return initial;
    }

    private void testAttributeConditionValue(String cssText, String value)
        throws CSSException, IOException
    {
        Condition condition = this.createCondition(cssText);
        AttributeCondition attributeCondition = (AttributeCondition) condition;
        Assert.assertEquals(value, attributeCondition.getValue());
    }

    private SelectorList createSelectors(String cssText)
        throws CSSException, IOException
    {
        InputSource source = new InputSource(new StringReader(cssText));
        return SAC_PARSER.parseSelectors(source);
    }

    private Condition createCondition(String cssText)
        throws CSSException, IOException
    {
        SelectorList selectors = this.createSelectors(cssText);
        Selector selector = selectors.item(0);
        ConditionalSelector conditionalSelector = (ConditionalSelector) selector;
        return conditionalSelector.getCondition();
    }

    public void testDojoCSS() throws Exception {

        InputStream is = getClass().getClassLoader().getResourceAsStream("dojo.css");
        assertNotNull(is);

        Reader r = new InputStreamReader(is);
        InputSource source = new InputSource(r);

        SACParserCSS21 sp = new SACParserCSS21();
        CSSOMParser parser = new CSSOMParser(sp);
        CSSStyleSheet sheet = parser.parseStyleSheet(source, null, null);

//        System.out.println(sheet);
        assertEquals(17, sheet.getCssRules().getLength());
    }

    public void testTestCSS() throws Exception {

        InputStream is = getClass().getClassLoader().getResourceAsStream("test.css");
        assertNotNull(is);

        Reader r = new InputStreamReader(is);
        InputSource source = new InputSource(r);

        CSSOMParser parser = new CSSOMParser(new SACParserCSS21());
        CSSStyleSheet sheet = parser.parseStyleSheet(source, null, null);

//        System.out.println(sheet);
//        assertEquals(21, sheet.getCssRules().getLength());
        assertEquals(78, sheet.getCssRules().getLength());
    }

}