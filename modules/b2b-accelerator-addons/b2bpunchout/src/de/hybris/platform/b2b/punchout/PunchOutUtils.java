/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2b.punchout;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.StringWriter;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.PropertyException;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.sax.SAXSource;

import org.apache.commons.codec.binary.Base64;
import org.cxml.CXML;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;


public class PunchOutUtils
{

	private PunchOutUtils()
	{
		throw new IllegalStateException("Cannot Instantiate an Utility Class");
	}

	private static final String XML_WITHOUT_STANDALONE = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
	private static final String DOCTYPE = "<!DOCTYPE cXML SYSTEM \"http://xml.cXML.org/schemas/cXML/1.2.024/cXML.dtd\">";
	private static final String XERCES_FEATURE_PREFIX = "http://apache.org/xml/features/";
	private static final String LOAD_EXTERNAL_DTD_FEATURE = "nonvalidating/load-external-dtd";
	private static final String SAX_FEATURE_PREFIX = "http://xml.org/sax/features/";
	private static final String EXTERNAL_GENERAL_ENTITIES_FEATURE = "external-general-entities";
	private static final String EXTERNAL_PARAMETER_ENTITIES_FEATURE = "external-parameter-entities";
	protected static final String LOAD_EXTERNAL_DTD = XERCES_FEATURE_PREFIX + LOAD_EXTERNAL_DTD_FEATURE;
	protected static final String EXTERNAL_GENERAL_ENTITIES = SAX_FEATURE_PREFIX + EXTERNAL_GENERAL_ENTITIES_FEATURE;
	protected static final String EXTERNAL_PARAMETER_ENTITIES = SAX_FEATURE_PREFIX + EXTERNAL_PARAMETER_ENTITIES_FEATURE;


	public static CXML unmarshallCXMLFromFile(final String relativeFilePath) throws FileNotFoundException
	{
		final InputStream fileInputStream = PunchOutUtils.class.getClassLoader().getResourceAsStream(relativeFilePath);

		if (fileInputStream == null)
		{
			throw new FileNotFoundException("Could not find file [" + relativeFilePath + "]");
		}

		try
		{
			final JAXBContext jaxbContext = JAXBContext.newInstance(CXML.class);
			final Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();

			final SAXParserFactory spf = SAXParserFactory.newInstance();
			spf.setValidating(false);
			spf.setFeature(LOAD_EXTERNAL_DTD, false);
			spf.setFeature(EXTERNAL_GENERAL_ENTITIES, false);
			spf.setFeature(EXTERNAL_PARAMETER_ENTITIES, false);
			spf.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
			spf.setXIncludeAware(false);

			final SAXParser parser = spf.newSAXParser();
			final XMLReader xmlReader = parser.getXMLReader();
			xmlReader.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
			final SAXSource source = new SAXSource(xmlReader, new InputSource(fileInputStream));

			return (CXML) unmarshaller.unmarshal(source);
		}
		catch (final Exception e)
		{
			throw new PunchOutException(PunchOutResponseCode.INTERNAL_SERVER_ERROR, e.getMessage(), e);
		}
	}

	public static String marshallFromBeanTree(final CXML cxml)
	{
		final StringWriter writer = new StringWriter();

		try
		{
			final JAXBContext context = JAXBContext.newInstance(CXML.class);
			final Marshaller m = context.createMarshaller();
			removeStandalone(m);
			setHeader(m);
			m.marshal(cxml, writer);
		}
		catch (final JAXBException e)
		{
			throw new PunchOutException(e.getErrorCode(), e.getMessage(), e);
		}

		String xml = writer.toString();
		xml = XML_WITHOUT_STANDALONE + DOCTYPE + xml;

		return xml;
	}

	public static void removeStandalone(final Marshaller marshaller) throws PropertyException
	{
		marshaller.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);
	}

	public static void setHeader(final Marshaller m) throws PropertyException
	{
		m.setProperty("com.sun.xml.bind.xmlHeaders", XML_WITHOUT_STANDALONE + DOCTYPE);
	}


	/**
	 * Transforms a CXML into a Base64 String.
	 *
	 * @param cxml
	 *           the cxml object.
	 * @return Base64 String
	 */
	public static String transformCXMLToBase64(final CXML cxml)
	{
		final String cXML = marshallFromBeanTree(cxml);
		final String cXMLEncoded = Base64.encodeBase64String(cXML.getBytes());

		return cXMLEncoded;
	}

}
