/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsocc.jaxb.adapters;

import de.hybris.platform.cmsocc.jaxb.adapters.KeyMapAdaptedEntryAdapter.KeyMapAdaptedEntry;

import java.util.List;
import java.util.Objects;

import javax.xml.XMLConstants;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.collections.CollectionUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;


/**
 * This Adapter is used to convert KeyMapAdaptedEntry into an HTML or XML document element.
 *
 * Example:
 *
 * <pre>
 *   <code>
 *   public class Container
 *   {
 *     private List<KeyMapAdaptedEntry> entries;
 *   }
 *   </code>
 * </pre>
 *
 * would have the following JSON representation:
 *
 * <pre>
 *   <code>
 *   {
 *       "key1": "strValue",
 *       "key2": {"subkey1": "strValue", "subkey2": {"subKey3": "strValue}}
 *       "key3": ["strUid1", "strUid2", "strUid3"]
 *   }
 *   </code>
 * </pre>
 */
public class KeyMapAdaptedEntryAdapter extends XmlAdapter<Object, KeyMapAdaptedEntry>
{
	/**
	 * This class represents a key-value entry in a Map<String, Object>. Value object can be string value, map value or
	 * an array. Map value in turn is also a list of KeyMapAdaptedEntry objects.
	 */
	public static class KeyMapAdaptedEntry
	{
		public String key;
		public String strValue;
		public List<KeyMapAdaptedEntry> mapValue;
		public List<String> arrayValue;
	}

	private final Document doc;

	public KeyMapAdaptedEntryAdapter()
	{
		try
		{

			final DocumentBuilderFactory df = DocumentBuilderFactory.newInstance();
			df.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
			final DocumentBuilder builder = df.newDocumentBuilder();
			builder.setEntityResolver(new ExternalEntityResolver());
			doc = builder.newDocument();
		}
		catch (final Exception e)
		{
			throw new IllegalStateException(e);
		}
	}

	@Override
	public KeyMapAdaptedEntry unmarshal(final Object object) throws Exception
	{
		throw new UnsupportedOperationException();
	}

	/**
	 * convert KeyMapAdaptedEntry object into an HTML or XML document element object.
	 */
	@Override
	public Element marshal(final KeyMapAdaptedEntry adaptedEntry)
	{
		final Element element = doc.createElement(adaptedEntry.key);
		if (adaptedEntry.strValue != null)
		{
			element.setTextContent(adaptedEntry.strValue);
			return element;
		}
		else if (adaptedEntry.mapValue != null)
		{
			adaptedEntry.mapValue.stream() //
					.map(item -> marshal(item)) //
					.filter(Objects::nonNull) //
					.forEach(child -> element.appendChild(child));

			return element;
		}
		else if (!CollectionUtils.isEmpty(adaptedEntry.arrayValue))
		{
			element.setTextContent(String.join(" ", adaptedEntry.arrayValue));
			return element;
		}
		return null;
	}
}
