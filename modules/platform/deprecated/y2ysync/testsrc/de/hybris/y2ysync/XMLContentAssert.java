/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.y2ysync;

import java.io.IOException;
import java.io.InputStream;

import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.Assertions;
import org.custommonkey.xmlunit.XMLAssert;
import org.custommonkey.xmlunit.exceptions.XpathException;
import org.xml.sax.SAXException;
import org.xmlunit.builder.DiffBuilder;
import org.xmlunit.builder.Input;
import org.xmlunit.diff.ComparisonType;
import org.xmlunit.diff.DefaultNodeMatcher;
import org.xmlunit.diff.Diff;
import org.xmlunit.diff.DifferenceEvaluators;
import org.xmlunit.diff.ElementSelectors;


/**
 *
 */
public class XMLContentAssert extends AbstractAssert<XMLContentAssert, String>
{

	private boolean ignoreComments = false;
	private boolean ignoreWhitespaces = false;
	private boolean ignoreNodeOrder = false;

	private XMLContentAssert(final String actual)
	{
		super(actual, XMLContentAssert.class);
	}

	public static XMLContentAssert assertThat(final String actual)
	{
		return new XMLContentAssert(actual);
	}

	public XMLContentAssert ignoreComments()
	{
		ignoreComments = true;
		return this;
	}

	public XMLContentAssert ignoreWhitespaces()
	{
		ignoreWhitespaces = true;
		return this;
	}

	public XMLContentAssert ignoreNodeOrder()
	{
		ignoreNodeOrder = true;
		return this;
	}


	public XMLContentAssert isIdenticalToResource(final String resourceName)
	{
		final InputStream resourceAsStream = getClass().getResourceAsStream(resourceName);
		Assertions.assertThat(resourceAsStream).isNotNull();
		final Diff diff = isIdenticalTo(Input.fromStream(resourceAsStream));
		Assertions.assertThat(diff.getDifferences())
		          .as("expected [%s] to be identical to resource [%s], but differences have been found", actual, resourceName)
		          .isEmpty();
		return this;
	}

	public XMLContentAssert isIdenticalTo(final String expectedXml)
	{
		Assertions.assertThat(expectedXml).isNotNull();
		final Diff diff = isIdenticalTo(Input.fromString(expectedXml));
		Assertions.assertThat(diff.getDifferences())
		          .as("expected [%s] to be identical to [%s], but differences have been found", actual, expectedXml)
		          .isEmpty();
		return this;
	}

	private Diff isIdenticalTo(final Input.Builder input)
	{

		final DiffBuilder compare = DiffBuilder.compare(input);
		compare.withTest(actual);
		compare.checkForIdentical();

		if (ignoreComments)
		{
			compare.ignoreComments();
		}
		if (ignoreWhitespaces)
		{
			compare.ignoreWhitespace();
		}
		if (ignoreNodeOrder)
		{
			compare.withNodeMatcher(new DefaultNodeMatcher(ElementSelectors.byNameAndText));
			compare.withDifferenceEvaluator(DifferenceEvaluators.chain(DifferenceEvaluators.Default,
					DifferenceEvaluators.downgradeDifferencesToEqual(
							ComparisonType.CHILD_NODELIST_SEQUENCE)));
		}
		return compare.build();
	}

	/**
	 * @deprecated since 2011 - this method is checking only number of xpaths and not the whole xml document.
	 * Use {@link #isIdenticalTo(String)}
	 */
	@Deprecated(since = "2011", forRemoval = true)
	public XMLContentAssert hasTheSameContentAs(final String expectedXml) throws SAXException, IOException, XpathException
	{
		XMLAssert.assertXpathEvaluatesTo("1", "count(/extension)", actual);
		XMLAssert.assertXpathEvaluatesTo("1", "count(//rawItems)", actual);
		XMLAssert.assertXpathEvaluatesTo("1", "count(//canonicalItems)", actual);
		XMLAssert.assertXpathEvaluatesTo("1", "count(//targetItems)", actual);
		XMLAssert.assertXpathEvaluatesTo("2", "count(//rawItems/item)", actual);
		XMLAssert.assertXpathEvaluatesTo("1", "count(//rawItems/item[type=\"testContainer_Product\"])", actual);
		XMLAssert.assertXpathEvaluatesTo("1", "count(//rawItems/item[type=\"testContainer_Title\"])", actual);
		XMLAssert.assertXpathEvaluatesTo("2", "count(//canonicalItems/item)", actual);
		XMLAssert.assertXpathEvaluatesTo("1", "count(//canonicalItems/item[type=\"testContainer_ProductCanonical\"])", actual);
		XMLAssert.assertXpathEvaluatesTo("1", "count(//canonicalItems/item[type=\"testContainer_TitleCanonical\"])", actual);
		XMLAssert.assertXpathEvaluatesTo("2", "count(//targetItems/item)", actual);
		XMLAssert.assertXpathEvaluatesTo("1", "count(//targetItems/item[type=\"testContainer_ProductTarget\"])", actual);
		XMLAssert.assertXpathEvaluatesTo("1", "count(//targetItems/item[type=\"testContainer_TitleTarget\"])", actual);
		XMLAssert.assertXpathsEqual("/extension/rawItems/item[type=\"testContainer_Product\"]", actual,
				"/extension/rawItems/item[type=\"testContainer_Product\"]", expectedXml);
		XMLAssert.assertXpathsEqual("/extension/rawItems/item[type=\"testContainer_Title\"]", actual,
				"/extension/rawItems/item[type=\"testContainer_Title\"]", expectedXml);
		XMLAssert.assertXpathsEqual("/extension/canonicalItems/item[type=\"testContainer_ProductCanonical\"]", actual,
				"/extension/canonicalItems/item[type=\"testContainer_ProductCanonical\"]", expectedXml);
		XMLAssert.assertXpathsEqual("/extension/canonicalItems/item[type=\"testContainer_TitleCanonical\"]", actual,
				"/extension/canonicalItems/item[type=\"testContainer_TitleCanonical\"]", expectedXml);
		XMLAssert.assertXpathsEqual("/extension/targetItems/item[type=\"testContainer_ProductTarget\"]", actual,
				"/extension/targetItems/item[type=\"testContainer_ProductTarget\"]", expectedXml);
		XMLAssert.assertXpathsEqual("/extension/targetItems/item[type=\"testContainer_TitleTarget\"]", actual,
				"/extension/targetItems/item[type=\"testContainer_TitleTarget\"]", expectedXml);
		return this;
	}
}
