/*
 * CSS Parser Project
 *
 * Copyright (C) 1999-2011 David Schweinsberg.  All rights reserved.
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
 *
 */

package com.steadystate.css.parser;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.util.Locale;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.w3c.css.sac.AttributeCondition;
import org.w3c.css.sac.CombinatorCondition;
import org.w3c.css.sac.Condition;
import org.w3c.css.sac.ConditionalSelector;
import org.w3c.css.sac.DescendantSelector;
import org.w3c.css.sac.InputSource;
import org.w3c.css.sac.Selector;
import org.w3c.css.sac.SelectorList;
import org.w3c.css.sac.SimpleSelector;
import org.w3c.dom.css.CSSRule;
import org.w3c.dom.css.CSSRuleList;
import org.w3c.dom.css.CSSStyleSheet;

import com.steadystate.css.ErrorHandler;

/**
 * @author rbri
 */
public class SACParserCSS21Test {

    private Locale systemLocale_;

    @Before
    public void setUp() {
        systemLocale_ = Locale.getDefault();
        Locale.setDefault(Locale.ENGLISH);
    }

    @After
    public void tearDown() {
        Locale.setDefault(systemLocale_);
    }

    @Test
    public void selectorList() throws Exception {
        selectorList("h1:first-line", 1);
        selectorList("h1", 1);
        selectorList("h1, h2", 2);
        selectorList("h1:first-line, h2", 2);
        selectorList("h1, h2, h3", 3);
        selectorList("h1, h2,\nh3", 3);
        selectorList("h1, h2, h3#id", 3);
        selectorList("h1.class, h2, h3", 3);
    }

    @Test
    public void selector() throws Exception {
        selectorType("a#id.class:link", Selector.SAC_CONDITIONAL_SELECTOR);
        selectorType("a#id.class", Selector.SAC_CONDITIONAL_SELECTOR);
        selectorType("a#id:link", Selector.SAC_CONDITIONAL_SELECTOR);
        selectorType("a#id", Selector.SAC_CONDITIONAL_SELECTOR);
        selectorType("a.class:link", Selector.SAC_CONDITIONAL_SELECTOR);
        selectorType("a.class", Selector.SAC_CONDITIONAL_SELECTOR);
        selectorType("a:link", Selector.SAC_CONDITIONAL_SELECTOR);
        selectorType("a", Selector.SAC_ELEMENT_NODE_SELECTOR);
        selectorType("#id.class:link", Selector.SAC_CONDITIONAL_SELECTOR);
        selectorType("#id.class", Selector.SAC_CONDITIONAL_SELECTOR);
        selectorType("#id:link", Selector.SAC_CONDITIONAL_SELECTOR);
        selectorType("#id", Selector.SAC_CONDITIONAL_SELECTOR);
        selectorType(".class:link", Selector.SAC_CONDITIONAL_SELECTOR);
        selectorType(".class", Selector.SAC_CONDITIONAL_SELECTOR);
        selectorType(":link", Selector.SAC_CONDITIONAL_SELECTOR);
        selectorType("a:visited", Selector.SAC_CONDITIONAL_SELECTOR);
        selectorType("a:active", Selector.SAC_CONDITIONAL_SELECTOR);

        selectorType("h1:first-line", Selector.SAC_DESCENDANT_SELECTOR, Selector.SAC_ELEMENT_NODE_SELECTOR,
                Selector.SAC_PSEUDO_ELEMENT_SELECTOR);
        selectorType("a:first-letter", Selector.SAC_DESCENDANT_SELECTOR, Selector.SAC_ELEMENT_NODE_SELECTOR,
                Selector.SAC_PSEUDO_ELEMENT_SELECTOR);

        selectorType("h1 a", Selector.SAC_DESCENDANT_SELECTOR, Selector.SAC_ELEMENT_NODE_SELECTOR,
                Selector.SAC_ELEMENT_NODE_SELECTOR);
        selectorType("h1  a", Selector.SAC_DESCENDANT_SELECTOR, Selector.SAC_ELEMENT_NODE_SELECTOR,
                Selector.SAC_ELEMENT_NODE_SELECTOR);
    }

    @Test
    public void condition() throws Exception {
        conditionType("a#id.class:link", Condition.SAC_AND_CONDITION, Condition.SAC_AND_CONDITION,
                Condition.SAC_ID_CONDITION, Condition.SAC_CLASS_CONDITION, Condition.SAC_PSEUDO_CLASS_CONDITION);
        conditionType("a#id.class", Condition.SAC_AND_CONDITION, Condition.SAC_ID_CONDITION,
                Condition.SAC_CLASS_CONDITION);
        conditionType("a#id:link", Condition.SAC_AND_CONDITION, Condition.SAC_ID_CONDITION,
                Condition.SAC_PSEUDO_CLASS_CONDITION);
        conditionType("a#id", Condition.SAC_ID_CONDITION);
        conditionType("a.class:link", Condition.SAC_AND_CONDITION, Condition.SAC_CLASS_CONDITION,
                Condition.SAC_PSEUDO_CLASS_CONDITION);
        conditionType("a.class", Condition.SAC_CLASS_CONDITION);
        conditionType("a:link", Condition.SAC_PSEUDO_CLASS_CONDITION);
        conditionType("#id.class:link", Condition.SAC_AND_CONDITION, Condition.SAC_AND_CONDITION,
                Condition.SAC_ID_CONDITION, Condition.SAC_CLASS_CONDITION, Condition.SAC_PSEUDO_CLASS_CONDITION);
        conditionType("#id.class", Condition.SAC_AND_CONDITION, Condition.SAC_ID_CONDITION,
                Condition.SAC_CLASS_CONDITION);
        conditionType("#id:link", Condition.SAC_AND_CONDITION, Condition.SAC_ID_CONDITION,
                Condition.SAC_PSEUDO_CLASS_CONDITION);
        conditionType("#id", Condition.SAC_ID_CONDITION);
        conditionType(".class:link", Condition.SAC_AND_CONDITION, Condition.SAC_CLASS_CONDITION,
                Condition.SAC_PSEUDO_CLASS_CONDITION);
        conditionType(".class", Condition.SAC_CLASS_CONDITION);
        conditionType(":link", Condition.SAC_PSEUDO_CLASS_CONDITION);
    }

    @Test
    public void attributeCondition() throws Exception {
        attributeConditionValue(".class", "class");
        attributeConditionValue("#id", "id");
        attributeConditionValue("h1.class", "class");
        attributeConditionValue("h1#id", "id");
        attributeConditionValue("a:link", "link");
        attributeConditionValue(":link", "link");
        attributeConditionValue("a:visited", "visited");
        attributeConditionValue(":visited", "visited");
        attributeConditionValue("a:active", "active");
        attributeConditionValue(":active", "active");
    }

    private void selectorList(final String cssText, final int length) throws Exception {
        final SelectorList selectors = createSelectors(cssText);
        Assert.assertEquals(length, selectors.getLength());
    }

    private void selectorType(final String cssText, final int... selectorTypes) throws Exception {
        final SelectorList selectors = createSelectors(cssText);
        final Selector selector = selectors.item(0);
        Assert.assertEquals(selectorTypes[0], selector.getSelectorType());
        if (selectorTypes[0] == Selector.SAC_DESCENDANT_SELECTOR) {
            final DescendantSelector descendantSelector = (DescendantSelector) selector;
            final Selector ancestor = descendantSelector.getAncestorSelector();
            Assert.assertEquals(selectorTypes[1], ancestor.getSelectorType());
            final SimpleSelector simple = descendantSelector.getSimpleSelector();
            Assert.assertEquals(selectorTypes[2], simple.getSelectorType());
        }
    }

    private void conditionType(final String cssText, final int... conditionTypes) throws Exception {
        final Condition condition = createCondition(cssText);
        conditionType(condition, 0, conditionTypes);
    }

    private int conditionType(final Condition condition, int initial, final int... conditionTypes) {
        Assert.assertEquals(conditionTypes[initial], condition.getConditionType());
        if (conditionTypes[initial] == Condition.SAC_AND_CONDITION) {
            final CombinatorCondition combinatorCondition = (CombinatorCondition) condition;
            final Condition first = combinatorCondition.getFirstCondition();
            final Condition second = combinatorCondition.getSecondCondition();
            initial = conditionType(first, ++initial, conditionTypes);
            initial = conditionType(second, ++initial, conditionTypes);
        }
        return initial;
    }

    private void attributeConditionValue(final String cssText, final String value) throws Exception {
        final Condition condition = createCondition(cssText);
        final AttributeCondition attributeCondition = (AttributeCondition) condition;
        Assert.assertEquals(value, attributeCondition.getValue());
    }

    private SelectorList createSelectors(final String cssText) throws Exception {
        final InputSource source = new InputSource(new StringReader(cssText));
        return new SACParserCSS21().parseSelectors(source);
    }

    private Condition createCondition(final String cssText) throws Exception {
        final SelectorList selectors = createSelectors(cssText);
        final Selector selector = selectors.item(0);
        final ConditionalSelector conditionalSelector = (ConditionalSelector) selector;
        return conditionalSelector.getCondition();
    }

    @Test
    public void dojoCSS() throws Exception {
        final InputStream is = getClass().getClassLoader().getResourceAsStream("dojo.css");
        Assert.assertNotNull(is);

        final Reader r = new InputStreamReader(is);
        final InputSource source = new InputSource(r);

        final CSSOMParser parser = new CSSOMParser(new SACParserCSS21());
        final ErrorHandler errorHandler = new ErrorHandler();
        parser.setErrorHandler(errorHandler);

        final CSSStyleSheet sheet = parser.parseStyleSheet(source, null, null);

        Assert.assertEquals(17, sheet.getCssRules().getLength());
        Assert.assertEquals(0, errorHandler.getErrorCount());
        Assert.assertEquals(0, errorHandler.getFatalErrorCount());
        Assert.assertEquals(0, errorHandler.getWarningCount());
    }

    @Test
    public void emptyCSS() throws Exception {
        final InputSource source = new InputSource(new StringReader(""));
        final CSSOMParser parser = new CSSOMParser(new SACParserCSS21());

        final ErrorHandler errorHandler = new ErrorHandler();
        parser.setErrorHandler(errorHandler);

        final CSSStyleSheet sheet = parser.parseStyleSheet(source, null, null);
        Assert.assertEquals(0, errorHandler.getErrorCount());
        Assert.assertEquals(0, errorHandler.getFatalErrorCount());
        Assert.assertEquals(0, errorHandler.getWarningCount());

        Assert.assertEquals(0, sheet.getCssRules().getLength());
    }

    @Test
    public void whitespaceOnlyCSS() throws Exception {
        final InputSource source = new InputSource(new StringReader("  \t \r\n \n"));
        final CSSOMParser parser = new CSSOMParser(new SACParserCSS21());

        final ErrorHandler errorHandler = new ErrorHandler();
        parser.setErrorHandler(errorHandler);

        final CSSStyleSheet sheet = parser.parseStyleSheet(source, null, null);
        Assert.assertEquals(0, errorHandler.getErrorCount());
        Assert.assertEquals(0, errorHandler.getFatalErrorCount());
        Assert.assertEquals(0, errorHandler.getWarningCount());

        Assert.assertEquals(0, sheet.getCssRules().getLength());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void importRuleOnly() throws Exception {
        final String css = "@import 'subs.css';";

        final InputSource source = new InputSource(new StringReader(css));
        final CSSOMParser parser = new CSSOMParser(new SACParserCSS21());

        final ErrorHandler errorHandler = new ErrorHandler();
        parser.setErrorHandler(errorHandler);

        final CSSStyleSheet sheet = parser.parseStyleSheet(source, null, null);
        Assert.assertEquals(0, errorHandler.getErrorCount());
        Assert.assertEquals(0, errorHandler.getFatalErrorCount());
        Assert.assertEquals(0, errorHandler.getWarningCount());

        final CSSRuleList rules = sheet.getCssRules();

        Assert.assertEquals(1, rules.getLength());

        final CSSRule rule = rules.item(0);
        Assert.assertEquals("@import url(subs.css);", rule.getCssText());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void importRulesOnly() throws Exception {
        final String css = "@import 'subs.css'; @import 'subs1.css'; @import 'subs2.css';";

        final InputSource source = new InputSource(new StringReader(css));
        final CSSOMParser parser = new CSSOMParser(new SACParserCSS21());

        final ErrorHandler errorHandler = new ErrorHandler();
        parser.setErrorHandler(errorHandler);

        final CSSStyleSheet sheet = parser.parseStyleSheet(source, null, null);

        Assert.assertEquals(0, errorHandler.getErrorCount());
        Assert.assertEquals(0, errorHandler.getFatalErrorCount());
        Assert.assertEquals(0, errorHandler.getWarningCount());

        final CSSRuleList rules = sheet.getCssRules();

        Assert.assertEquals(3, rules.getLength());

        CSSRule rule = rules.item(0);
        Assert.assertEquals("@import url(subs.css);", rule.getCssText());

        rule = rules.item(1);
        Assert.assertEquals("@import url(subs1.css);", rule.getCssText());

        rule = rules.item(2);
        Assert.assertEquals("@import url(subs2.css);", rule.getCssText());
    }

    /**
     * @see "http://www.w3.org/TR/CSS21/syndata.html#at-rules"
     * @throws Exception if the test fails
     */
    @Test
    public void atRules1() throws Exception {
        final String css = "@import 'subs.css';\n"
            + "h1 { color: blue }\n"
            + "@import 'list.css';\n";

        final InputSource source = new InputSource(new StringReader(css));
        final CSSOMParser parser = new CSSOMParser(new SACParserCSS21());

        final ErrorHandler errorHandler = new ErrorHandler();
        parser.setErrorHandler(errorHandler);

        final CSSStyleSheet sheet = parser.parseStyleSheet(source, null, null);

        Assert.assertEquals(1, errorHandler.getErrorCount());
        final String expected = "@import rule must occur before all other rules, except the @charset rule."
                + " (Invalid token \"@import\". Was expecting one of: <S>, \"<!--\", \"-->\".)";
        Assert.assertEquals(expected, errorHandler.getErrorMessage());
        Assert.assertEquals("3", errorHandler.getErrorLines());
        Assert.assertEquals("1", errorHandler.getErrorColumns());

        Assert.assertEquals(0, errorHandler.getFatalErrorCount());
        Assert.assertEquals(0, errorHandler.getWarningCount());

        final CSSRuleList rules = sheet.getCssRules();

        Assert.assertEquals(2, rules.getLength());

        CSSRule rule = rules.item(0);
        Assert.assertEquals("@import url(subs.css);", rule.getCssText());

        rule = rules.item(1);
        Assert.assertEquals("h1 { color: blue }", rule.getCssText());
    }

    /**
     * @see "http://www.w3.org/TR/CSS21/syndata.html#at-rules"
     * @throws Exception if the test fails
     */
    @Test
    public void atRules2() throws Exception {
        final String css = "@import 'subs.css';\n"
            + "@media print {\n"
            + "  @import 'print-main.css';\n"
            + "  body { font-size: 10pt }\n"
            + "}\n"
            + "h1 {color: blue }\n";

        final InputSource source = new InputSource(new StringReader(css));
        final CSSOMParser parser = new CSSOMParser(new SACParserCSS21());

        final ErrorHandler errorHandler = new ErrorHandler();
        parser.setErrorHandler(errorHandler);

        final CSSStyleSheet sheet = parser.parseStyleSheet(source, null, null);

        Assert.assertEquals(1, errorHandler.getErrorCount());
        final String expected = "@import rule must occur before all other rules, except the @charset rule."
                + " (Invalid token \"@import\". Was expecting: <S>.)";
        Assert.assertEquals(expected, errorHandler.getErrorMessage());
        Assert.assertEquals("3", errorHandler.getErrorLines());
        Assert.assertEquals("3", errorHandler.getErrorColumns());

        Assert.assertEquals(0, errorHandler.getFatalErrorCount());
        Assert.assertEquals(0, errorHandler.getWarningCount());

        final CSSRuleList rules = sheet.getCssRules();

        Assert.assertEquals(3, rules.getLength());

        CSSRule rule = rules.item(0);
        Assert.assertEquals("@import url(subs.css);", rule.getCssText());

        rule = rules.item(1);
        Assert.assertEquals("@media print {body { font-size: 10pt } }", rule.getCssText());

        rule = rules.item(2);
        Assert.assertEquals("h1 { color: blue }", rule.getCssText());
    }

    /**
     * @import after rule
     * @throws Exception
     */
    @Test
    public void atRules2b() throws Exception {
        final String css = "@import 'subs.css';\n"
            + "@media print {\n"
            + "  body { font-size: 10pt }\n"
            + "  @import 'print-main.css';\n"
            + "}\n"
            + "h1 {color: blue }\n";

        final InputSource source = new InputSource(new StringReader(css));
        final CSSOMParser parser = new CSSOMParser(new SACParserCSS21());

        final ErrorHandler errorHandler = new ErrorHandler();
        parser.setErrorHandler(errorHandler);

        final CSSStyleSheet sheet = parser.parseStyleSheet(source, null, null);

        Assert.assertEquals(1, errorHandler.getErrorCount());
        final String expected = "@import rule must occur before all other rules, except the @charset rule."
                + " (Invalid token \"@import\". Was expecting: <S>.)";
        Assert.assertEquals(expected, errorHandler.getErrorMessage());
        Assert.assertEquals("4", errorHandler.getErrorLines());
        Assert.assertEquals("3", errorHandler.getErrorColumns());

        Assert.assertEquals(0, errorHandler.getFatalErrorCount());
        Assert.assertEquals(0, errorHandler.getWarningCount());

        final CSSRuleList rules = sheet.getCssRules();

        Assert.assertEquals(3, rules.getLength());

        CSSRule rule = rules.item(0);
        Assert.assertEquals("@import url(subs.css);", rule.getCssText());

        rule = rules.item(1);
        Assert.assertEquals("@media print {body { font-size: 10pt } }", rule.getCssText());

        rule = rules.item(2);
        Assert.assertEquals("h1 { color: blue }", rule.getCssText());
    }

    /**
     * @see "http://www.w3.org/TR/CSS21/syndata.html#parsing-errors"
     * @throws Exception if the test fails
     */
    @Test
    public void unknownProperty() throws Exception {
        final String css = "h1 { color: red; rotation: 70minutes }\n";

        final InputSource source = new InputSource(new StringReader(css));
        final CSSOMParser parser = new CSSOMParser(new SACParserCSS21());

        final ErrorHandler errorHandler = new ErrorHandler();
        parser.setErrorHandler(errorHandler);

        final CSSStyleSheet sheet = parser.parseStyleSheet(source, null, null);

        Assert.assertEquals(0, errorHandler.getErrorCount());
        Assert.assertEquals(0, errorHandler.getFatalErrorCount());
        Assert.assertEquals(0, errorHandler.getWarningCount());

        final CSSRuleList rules = sheet.getCssRules();

        Assert.assertEquals(1, rules.getLength());

        final CSSRule rule = rules.item(0);
        // parser accepts this
        Assert.assertEquals("h1 { color: red; rotation: 70minutes }", rule.getCssText());
    }

    /**
     * @see "http://www.w3.org/TR/CSS21/syndata.html#parsing-errors"
     * @throws Exception if the test fails
     */
    @Test
    public void illegalValues() throws Exception {
        final String css = "img { float: left } /* correct CSS 2.1 */\n"
                    + "img { float: left here } /* 'here' is not a value of 'float' */\n"
                    + "img { background: \"red\" } /* keywords cannot be quoted */\n"
                    + "img { background: \'red\' } /* keywords cannot be quoted */\n"
                    + "img { border-width: 3 } /* a unit must be specified for length values */\n";

        final InputSource source = new InputSource(new StringReader(css));
        final CSSOMParser parser = new CSSOMParser(new SACParserCSS21());

        final ErrorHandler errorHandler = new ErrorHandler();
        parser.setErrorHandler(errorHandler);

        final CSSStyleSheet sheet = parser.parseStyleSheet(source, null, null);

        Assert.assertEquals(0, errorHandler.getErrorCount());
        Assert.assertEquals(0, errorHandler.getFatalErrorCount());
        Assert.assertEquals(0, errorHandler.getWarningCount());

        final CSSRuleList rules = sheet.getCssRules();

        Assert.assertEquals(5, rules.getLength());

        CSSRule rule = rules.item(0);
        Assert.assertEquals("img { float: left }", rule.getCssText());

        // parser accepts this
        rule = rules.item(1);
        Assert.assertEquals("img { float: left here }", rule.getCssText());
        rule = rules.item(2);
        Assert.assertEquals("img { background: \"red\" }", rule.getCssText());
        rule = rules.item(3);
        Assert.assertEquals("img { background: \"red\" }", rule.getCssText());
        rule = rules.item(4);
        Assert.assertEquals("img { border-width: 3 }", rule.getCssText());
    }

    /**
     * @see "http://www.w3.org/TR/CSS21/syndata.html#parsing-errors"
     * @throws Exception if the test fails
     */
    @Test
    public void malformedDeclaration() throws Exception {
        final String css = "p { color:green }\n"
                    + "p { color:green; color } /* malformed declaration missing ':', value */\n"
                    + "p { color:red;   color; color:green } /* same with expected recovery */\n"
                    + "p { color:green; color: } /* malformed declaration missing value */\n"
                    + "p { color:red;   color:; color:green } /* same with expected recovery */\n"
                    + "p { color:green; color{;color:maroon} } /* unexpected tokens { } */\n"
                    + "p { color:red;   color{;color:maroon}; color:green } /* same with recovery */\n";

        final InputSource source = new InputSource(new StringReader(css));
        final CSSOMParser parser = new CSSOMParser(new SACParserCSS21());

        final ErrorHandler errorHandler = new ErrorHandler();
        parser.setErrorHandler(errorHandler);

        final CSSStyleSheet sheet = parser.parseStyleSheet(source, null, null);

        Assert.assertEquals(7, errorHandler.getErrorCount());
        final String expected = "Error in declaration. (Invalid token \"}\". Was expecting one of: <S>, \":\".)"
                + " Error in declaration. (Invalid token \";\". Was expecting one of: <S>, \":\".)"
                + " Error in expression. (Invalid token \"}\". Was expecting one of: <S>, <NUMBER>, \"inherit\", "
                        + "<IDENT>, <STRING>, <PLUS>, <HASH>, <EMS>, <EXS>, <LENGTH_PX>, <LENGTH_CM>, <LENGTH_MM>, "
                        + "<LENGTH_IN>, <LENGTH_PT>, <LENGTH_PC>, <ANGLE_DEG>, <ANGLE_RAD>, <ANGLE_GRAD>, <TIME_MS>, "
                        + "<TIME_S>, <FREQ_HZ>, <FREQ_KHZ>, <DIMENSION>, <PERCENTAGE>, <URI>, <FUNCTION>, \"-\".)"
                + " Error in expression. (Invalid token \";\". Was expecting one of: <S>, <NUMBER>, \"inherit\", "
                        + "<IDENT>, <STRING>, <PLUS>, <HASH>, <EMS>, <EXS>, <LENGTH_PX>, <LENGTH_CM>, <LENGTH_MM>, "
                        + "<LENGTH_IN>, <LENGTH_PT>, <LENGTH_PC>, <ANGLE_DEG>, <ANGLE_RAD>, <ANGLE_GRAD>, <TIME_MS>, "
                        + "<TIME_S>, <FREQ_HZ>, <FREQ_KHZ>, <DIMENSION>, <PERCENTAGE>, <URI>, <FUNCTION>, \"-\".)"
                + " Error in declaration. (Invalid token \"{\". Was expecting one of: <S>, \":\".)"
                + " Error in style rule. (Invalid token \" \". Was expecting one of: <EOF>, \"}\", \";\".)"
                + " Error in declaration. (Invalid token \"{\". Was expecting one of: <S>, \":\".)";
        Assert.assertEquals(expected, errorHandler.getErrorMessage());
        Assert.assertEquals("2 3 4 5 6 6 7", errorHandler.getErrorLines());
        Assert.assertEquals("24 23 25 24 23 38 23", errorHandler.getErrorColumns());

        Assert.assertEquals(0, errorHandler.getFatalErrorCount());
        Assert.assertEquals(1, errorHandler.getWarningCount());
        Assert.assertEquals("Ignoring the following declarations in this rule.", errorHandler.getWarningMessage());
        Assert.assertEquals("6", errorHandler.getWarningLines());
        Assert.assertEquals("38", errorHandler.getWarningColumns());

        final CSSRuleList rules = sheet.getCssRules();

        Assert.assertEquals(7, rules.getLength());

        CSSRule rule = rules.item(0);
        Assert.assertEquals("p { color: green }", rule.getCssText());

        // parser accepts this
        rule = rules.item(1);
        Assert.assertEquals("p { color: green }", rule.getCssText());
        rule = rules.item(2);
        Assert.assertEquals("p { color: red; color: green }", rule.getCssText());
        rule = rules.item(3);
        Assert.assertEquals("p { color: green }", rule.getCssText());
        rule = rules.item(4);
        Assert.assertEquals("p { color: red; color: green }", rule.getCssText());
        rule = rules.item(5);
        Assert.assertEquals("p { color: green }", rule.getCssText());
        rule = rules.item(6);
        Assert.assertEquals("p { color: red; color: green }", rule.getCssText());
    }

    /**
     * @see "http://www.w3.org/TR/CSS21/syndata.html#parsing-errors"
     * @throws Exception if the test fails
     */
    @Test
    public void malformedStatements() throws Exception {
        final String css = "p { color:green }\n"
                    + "p @here {color:red} /* ruleset with unexpected at-keyword '@here' */\n"
                    + "@foo @bar; /* at-rule with unexpected at-keyword '@bar' */\n"
                    // TODO + "}} {{ - }} /* ruleset with unexpected right brace */\n"
                    // TODO + ") ( {} ) p {color: red } /* ruleset with unexpected right parenthesis */\n"
                    + "p { color:blue; }\n";

        final InputSource source = new InputSource(new StringReader(css));
        final CSSOMParser parser = new CSSOMParser(new SACParserCSS21());

        final ErrorHandler errorHandler = new ErrorHandler();
        parser.setErrorHandler(errorHandler);

        final CSSStyleSheet sheet = parser.parseStyleSheet(source, null, null);

        Assert.assertEquals(1, errorHandler.getErrorCount());
        final String expected = "Error in style rule. "
                + "(Invalid token \"@here\". Was expecting one of: <S>, <LBRACE>, <COMMA>.)";
        Assert.assertEquals(expected, errorHandler.getErrorMessage());
        Assert.assertEquals("2", errorHandler.getErrorLines());
        Assert.assertEquals("3", errorHandler.getErrorColumns());

        Assert.assertEquals(0, errorHandler.getFatalErrorCount());
        Assert.assertEquals(1, errorHandler.getWarningCount());
        Assert.assertEquals("Ignoring the following declarations in this rule.", errorHandler.getWarningMessage());
        Assert.assertEquals("2", errorHandler.getWarningLines());
        Assert.assertEquals("3", errorHandler.getWarningColumns());

        final CSSRuleList rules = sheet.getCssRules();

        Assert.assertEquals(3, rules.getLength());

        CSSRule rule = rules.item(0);
        Assert.assertEquals("p { color: green }", rule.getCssText());

        rule = rules.item(1);
        Assert.assertEquals("@foo @bar;", rule.getCssText());

        rule = rules.item(2);
        Assert.assertEquals("p { color: blue }", rule.getCssText());
    }

    /**
     * @see "http://www.w3.org/TR/CSS21/syndata.html#parsing-errors"
     * @throws Exception if the test fails
     */
    @Test
    public void atRulesWithUnknownAtKeywords() throws Exception {
        final String css = "@three-dee {\n"
                            + "  @background-lighting {\n"
                            + "    azimuth: 30deg;\n"
                            + "    elevation: 190deg;\n"
                            + "  }\n"
                            + "  h1 { color: red }\n"
                            + "  }\n"
                            + "  h1 { color: blue }\n";

        final InputSource source = new InputSource(new StringReader(css));
        final CSSOMParser parser = new CSSOMParser(new SACParserCSS21());

        final ErrorHandler errorHandler = new ErrorHandler();
        parser.setErrorHandler(errorHandler);

        final CSSStyleSheet sheet = parser.parseStyleSheet(source, null, null);

        Assert.assertEquals(0, errorHandler.getErrorCount());
        Assert.assertEquals(0, errorHandler.getFatalErrorCount());
        Assert.assertEquals(0, errorHandler.getWarningCount());

        final CSSRuleList rules = sheet.getCssRules();

        Assert.assertEquals(2, rules.getLength());

        CSSRule rule = rules.item(0);
        Assert.assertEquals("@three-dee {\n"
                            + "  @background-lighting {\n"
                            + "    azimuth: 30;\n"
                            + "    elevation: 190;\n"
                            + "  }\n"
                            + "  h1 { color: red }\n"
                            + "  }", rule.getCssText());

        rule = rules.item(1);
        Assert.assertEquals("h1 { color: blue }", rule.getCssText());
    }

    /**
     * @see "http://www.w3.org/TR/CSS21/syndata.html#parsing-errors"
     * @throws Exception if the test fails
     */
    @Test
    public void unexpectedEndOfStyleSheet() throws Exception {
        final String css = "@media screen {\n"
                            + "  p:before { content: Hello";

        final InputSource source = new InputSource(new StringReader(css));
        final CSSOMParser parser = new CSSOMParser(new SACParserCSS21());

        final ErrorHandler errorHandler = new ErrorHandler();
        parser.setErrorHandler(errorHandler);

        final CSSStyleSheet sheet = parser.parseStyleSheet(source, null, null);

        Assert.assertEquals(1, errorHandler.getErrorCount());
        final String expected = "Error in @media rule. (Invalid token \"<EOF>\". Was expecting one of: <S>, <IDENT>, "
                + "<HASH>, <IMPORT_SYM>, <PAGE_SYM>, \"}\", \".\", \":\", \"*\", \"[\", <ATKEYWORD>.)";
        Assert.assertEquals(expected, errorHandler.getErrorMessage());
        Assert.assertEquals("2", errorHandler.getErrorLines());
        Assert.assertEquals("27", errorHandler.getErrorColumns());

        Assert.assertEquals(0, errorHandler.getFatalErrorCount());
        Assert.assertEquals(1, errorHandler.getWarningCount());
        Assert.assertEquals("Ignoring the whole rule.", errorHandler.getWarningMessage());
        Assert.assertEquals("2", errorHandler.getWarningLines());
        Assert.assertEquals("27", errorHandler.getWarningColumns());

        final CSSRuleList rules = sheet.getCssRules();

        Assert.assertEquals(1, rules.getLength());

        final CSSRule rule = rules.item(0);
        Assert.assertEquals("@media screen {p before { content: Hello } }", rule.getCssText());
    }

    @Test
    public void unexpectedEndOfMediaRule() throws Exception {
        final String css = "@media screen {\n"
                            + "  p:before { content: Hello }";

        final InputSource source = new InputSource(new StringReader(css));
        final CSSOMParser parser = new CSSOMParser(new SACParserCSS21());

        final ErrorHandler errorHandler = new ErrorHandler();
        parser.setErrorHandler(errorHandler);

        final CSSStyleSheet sheet = parser.parseStyleSheet(source, null, null);

        Assert.assertEquals(1, errorHandler.getErrorCount());
        final String expected = "Error in @media rule. (Invalid token \"<EOF>\". Was expecting one of: <S>, <IDENT>, "
                + "<HASH>, <IMPORT_SYM>, <PAGE_SYM>, \"}\", \".\", \":\", \"*\", \"[\", <ATKEYWORD>.)";
        Assert.assertEquals(expected, errorHandler.getErrorMessage());
        Assert.assertEquals("2", errorHandler.getErrorLines());
        Assert.assertEquals("29", errorHandler.getErrorColumns());

        Assert.assertEquals(0, errorHandler.getFatalErrorCount());
        Assert.assertEquals(1, errorHandler.getWarningCount());
        Assert.assertEquals("Ignoring the whole rule.", errorHandler.getWarningMessage());
        Assert.assertEquals("2", errorHandler.getWarningLines());
        Assert.assertEquals("29", errorHandler.getWarningColumns());

        final CSSRuleList rules = sheet.getCssRules();

        Assert.assertEquals(1, rules.getLength());

        final CSSRule rule = rules.item(0);
        Assert.assertEquals("@media screen {p before { content: Hello } }", rule.getCssText());
    }

    @Test
    public void unexpectedEndOfPageRule() throws Exception {
        final String css = "@page :pageStyle { size: 21.0cm 29.7cm;";

        final InputSource source = new InputSource(new StringReader(css));
        final CSSOMParser parser = new CSSOMParser(new SACParserCSS21());

        final ErrorHandler errorHandler = new ErrorHandler();
        parser.setErrorHandler(errorHandler);

        final CSSStyleSheet sheet = parser.parseStyleSheet(source, null, null);

        Assert.assertEquals(1, errorHandler.getErrorCount());
        final String expected = "Error in @page rule. "
                + "(Invalid token \"<EOF>\". Was expecting one of: <S>, <IDENT>, \"}\", \";\".)";
        Assert.assertEquals(expected, errorHandler.getErrorMessage());
        Assert.assertEquals("1", errorHandler.getErrorLines());
        Assert.assertEquals("39", errorHandler.getErrorColumns());

        Assert.assertEquals(0, errorHandler.getFatalErrorCount());
        Assert.assertEquals(1, errorHandler.getWarningCount());
        Assert.assertEquals("Ignoring the whole rule.", errorHandler.getWarningMessage());
        Assert.assertEquals("1", errorHandler.getWarningLines());
        Assert.assertEquals("39", errorHandler.getWarningColumns());

        final CSSRuleList rules = sheet.getCssRules();

        Assert.assertEquals(1, rules.getLength());

        final CSSRule rule = rules.item(0);
        Assert.assertEquals("@page :pageStyle {size: 21cm 29.7cm}", rule.getCssText());
    }

    /**
     * @see "http://www.w3.org/TR/CSS21/syndata.html#parsing-errors"
     *
     * @throws Exception in case of failure
     */
    @Test
    public void unexpectedEndOfString() throws Exception {
        final String css = "p {\n"
                            + "  color: green;\n"
                            + "  font-family: 'Courier New Times\n"
                            + "  color: red;\n"
                            + "  color: green;\n"
                            + "}";

        final InputSource source = new InputSource(new StringReader(css));
        final CSSOMParser parser = new CSSOMParser(new SACParserCSS21());

        final ErrorHandler errorHandler = new ErrorHandler();
        parser.setErrorHandler(errorHandler);

        final CSSStyleSheet sheet = parser.parseStyleSheet(source, null, null);

        Assert.assertEquals(2, errorHandler.getErrorCount());
        final String expected = "Error in expression. "
                + "(Invalid token \"\\'\". Was expecting one of: <S>, <NUMBER>, \"inherit\", "
                        + "<IDENT>, <STRING>, <PLUS>, <HASH>, <EMS>, <EXS>, <LENGTH_PX>, <LENGTH_CM>, <LENGTH_MM>, "
                        + "<LENGTH_IN>, <LENGTH_PT>, <LENGTH_PC>, <ANGLE_DEG>, <ANGLE_RAD>, <ANGLE_GRAD>, <TIME_MS>, "
                        + "<TIME_S>, <FREQ_HZ>, <FREQ_KHZ>, <DIMENSION>, <PERCENTAGE>, <URI>, <FUNCTION>, \"-\".)"
                + " Error in style rule. (Invalid token \"\\n  \". Was expecting one of: <EOF>, \"}\", \";\".)";
        Assert.assertEquals(expected, errorHandler.getErrorMessage());
        Assert.assertEquals("3 4", errorHandler.getErrorLines());
        Assert.assertEquals("16 14", errorHandler.getErrorColumns());

        Assert.assertEquals(0, errorHandler.getFatalErrorCount());
        Assert.assertEquals(1, errorHandler.getWarningCount());
        Assert.assertEquals("Ignoring the following declarations in this rule.", errorHandler.getWarningMessage());
        Assert.assertEquals("4", errorHandler.getWarningLines());
        Assert.assertEquals("14", errorHandler.getWarningColumns());

        final CSSRuleList rules = sheet.getCssRules();

        Assert.assertEquals(1, rules.getLength());

        final CSSRule rule = rules.item(0);
        // TODO
        Assert.assertEquals("p { color: green }", rule.getCssText());
    }

    /**
     * @see "http://www.w3.org/TR/CSS21/syndata.html#strings"
     *
     * @throws Exception in case of failure
     */
    @Test
    public void strings() throws Exception {
        final String css = "h1 { background: url(\"this is a 'string'\") }\n"
                            + "h2 { background: url(\"this is a \\\"string\\\"\") }\n"
                            + "h4 { background: url('this is a \"string\"') }\n"
                            + "h5 { background: url('this is a \\'string\\'') }"
                            + "h6 { background: url('this is a \\\r\n string') }";
        final InputSource source = new InputSource(new StringReader(css));
        final CSSOMParser parser = new CSSOMParser(new SACParserCSS21());

        final ErrorHandler errorHandler = new ErrorHandler();
        parser.setErrorHandler(errorHandler);

        final CSSStyleSheet sheet = parser.parseStyleSheet(source, null, null);

        Assert.assertEquals(0, errorHandler.getErrorCount());
        Assert.assertEquals(0, errorHandler.getFatalErrorCount());
        Assert.assertEquals(0, errorHandler.getWarningCount());

        final CSSRuleList rules = sheet.getCssRules();

        Assert.assertEquals(5, rules.getLength());

        CSSRule rule = rules.item(0);
        Assert.assertEquals("h1 { background: url(this is a 'string') }", rule.getCssText());

        rule = rules.item(1);
        Assert.assertEquals("h2 { background: url(this is a \"string\") }", rule.getCssText());

        rule = rules.item(2);
        Assert.assertEquals("h4 { background: url(this is a \"string\") }", rule.getCssText());

        rule = rules.item(3);
        Assert.assertEquals("h5 { background: url(this is a 'string') }", rule.getCssText());

        rule = rules.item(4);
        Assert.assertEquals("h6 { background: url(this is a  string) }", rule.getCssText());
    }
}
