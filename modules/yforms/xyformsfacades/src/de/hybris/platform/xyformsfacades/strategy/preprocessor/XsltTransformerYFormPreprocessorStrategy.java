/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.xyformsfacades.strategy.preprocessor;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import javax.xml.XMLConstants;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.commons.io.IOUtils;


/**
 * Processor that applies XSLT Transformations to a formData
 */
public class XsltTransformerYFormPreprocessorStrategy extends TransformerYFormPreprocessorStrategy
{
	// To support XSLT 2.0 and XPATH 2.0 the following TransformerFactory is defined
	private static final String TRANSFORMER_FACTORY = "net.sf.saxon.TransformerFactoryImpl";

	private String xsltContent;

	@Override
	protected String transform(final String xmlContent, final Map<String, Object> params) throws YFormProcessorException
	{
		return transform(IOUtils.toInputStream(xsltContent, StandardCharsets.UTF_8), IOUtils.toInputStream(xmlContent, StandardCharsets.UTF_8));
	}

	protected String transform(final InputStream xsltStream, final InputStream xmlStream) throws YFormProcessorException
	{
		final Map<String, Object> transformationParameters = new HashMap<>();
		final String output = transform(xsltStream, xmlStream, transformationParameters);
		return output;
	}

	protected String transform(final InputStream xsltStream, final InputStream xmlStream,
			final Map<String, Object> transformParameters) throws YFormProcessorException
	{
		try(final ByteArrayOutputStream os = new ByteArrayOutputStream())
		{
			final TransformerFactory transformerFactory = TransformerFactory.newInstance(TRANSFORMER_FACTORY, null);
			transformerFactory.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "");
			transformerFactory.setAttribute(XMLConstants.ACCESS_EXTERNAL_STYLESHEET, "");

			transformerFactory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
			final Transformer transformer = transformerFactory.newTransformer(new StreamSource(xsltStream));

			for (final Map.Entry<String, Object> tpEntry : transformParameters.entrySet())
			{
				transformer.setParameter(tpEntry.getKey(), tpEntry.getValue());
			}
			transformer.transform(new StreamSource(xmlStream), new StreamResult(os));
			return os.toString(StandardCharsets.UTF_8);
		}
		catch (final TransformerException | IOException e)
		{
			throw new YFormProcessorException(e);
		}
	}

	public void setXsltStream(final InputStream xsltStream) throws IOException
	{
		this.xsltContent = IOUtils.toString(xsltStream, StandardCharsets.UTF_8);
	}
}
