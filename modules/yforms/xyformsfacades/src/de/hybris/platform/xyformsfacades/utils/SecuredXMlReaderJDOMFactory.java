/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.xyformsfacades.utils;

import javax.xml.XMLConstants;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.jdom2.JDOMException;
import org.jdom2.input.sax.XMLReaderJDOMFactory;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;


public class SecuredXMlReaderJDOMFactory implements XMLReaderJDOMFactory
{
	@Override
	public XMLReader createXMLReader() throws JDOMException
	{
		try
		{
			SAXParserFactory factory = SAXParserFactory.newInstance();
			factory.setNamespaceAware(true);
			factory.setValidating(false);
			factory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);

			// The settings below are suggested by the OWASP site https://owasp.org/index.php/XML_External_Entity_%28XXE%29_Processing
			factory.setXIncludeAware(false);

			// Xerces 2 only - http://xerces.apache.org/xerces2-j/features.html#disallow-doctype-decl
			// This is the PRIMARY defense. If DTDs (doctypes) are disallowed, almost all XML entity attacks are prevented
			// A fatal error is thrown if the incoming document contains a DOCTYPE declaration.
			factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);

			// Xerces 1 - http://xerces.apache.org/xerces-j/features.html#external-general-entities
			// Xerces 2 - http://xerces.apache.org/xerces2-j/features.html#external-general-entities
			// 	Do not include external general entities.
			factory.setFeature("http://xml.org/sax/features/external-general-entities", false);

			// Xerces 1 - http://xerces.apache.org/xerces-j/features.html#external-parameter-entities
			// Xerces 2 - http://xerces.apache.org/xerces2-j/features.html#external-parameter-entities
			// Do not include external parameter entities or the external DTD subset.
			factory.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
			SAXParser saxParser = factory.newSAXParser();
			return saxParser.getXMLReader();
		}
		catch (ParserConfigurationException | SAXException e)
		{
			throw new JDOMException("Error while constructing XMLReader", e);
		}
	}

	@Override
	public boolean isValidating()
	{
		return false;
	}
}
