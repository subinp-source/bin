/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.searchservices.indexer.service.impl;

import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.searchservices.core.SnRuntimeException;
import de.hybris.platform.searchservices.document.data.SnDocument;
import de.hybris.platform.searchservices.indexer.SnIndexerException;
import de.hybris.platform.searchservices.indexer.service.SnIndexerContext;
import de.hybris.platform.searchservices.indexer.service.SnIndexerFieldWrapper;
import de.hybris.platform.searchservices.indexer.service.SnIndexerValueProvider;
import de.hybris.platform.searchservices.util.ParameterUtils;

import java.io.IOException;
import java.net.URL;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.databind.type.TypeFactory;


/**
 * Implementation for {@link SnIndexerValueProvider} that can extract values from a documents resource.
 */
public class DocumentsResourceSnIndexerValueProvider implements SnIndexerValueProvider<ItemModel>
{
	public static final String ID = "documentsResourceSnIndexerValueProvider";

	public static final String RESOURCE_PARAM = "resource";

	protected static final Set<Class<?>> SUPPORTED_QUALIFIER_CLASSES = Set.of();

	private final Map<String, Map<String, SnDocument>> documentsMapping = new ConcurrentHashMap<>();

	@Override
	public Set<Class<?>> getSupportedQualifierClasses() throws SnIndexerException
	{
		return SUPPORTED_QUALIFIER_CLASSES;
	}

	@Override
	public void provide(final SnIndexerContext indexerContext,
			final Collection<SnIndexerFieldWrapper> fieldWrappers, final ItemModel source, final SnDocument target)
			throws SnIndexerException
	{
		for (final SnIndexerFieldWrapper fieldWrapper : fieldWrappers)
		{
			final Map<String, SnDocument> documents = loadResource(fieldWrapper);
			final Optional<SnDocument> document = extractDocument(documents, target);

			if (document.isPresent())
			{
				target.setFieldValue(fieldWrapper.getField(),
						extractDocumentValue(document.get(), fieldWrapper));
			}
		}
	}

	protected Map<String, SnDocument> loadResource(final SnIndexerFieldWrapper fieldWrapper)
	{
		final String resource = ParameterUtils.getString(fieldWrapper.getValueProviderParameters(), RESOURCE_PARAM, null);
		if (StringUtils.isBlank(resource))
		{
			return Collections.emptyMap();
		}

		return documentsMapping.computeIfAbsent(resource, this::doLoadResource);
	}

	protected Map<String, SnDocument> doLoadResource(final String resource)
	{
		try
		{
			final ObjectMapper objectMapper = new ObjectMapper();

			final URL url = DocumentsResourceSnIndexerValueProvider.class.getResource(resource);
			final TypeFactory typeFactory = objectMapper.getTypeFactory();
			final CollectionType type = typeFactory.constructCollectionType(List.class, SnDocument.class);

			final List<SnDocument> documents = objectMapper.readValue(url, type);

			return documents.stream().collect(Collectors.toMap(SnDocument::getId, document -> document));
		}
		catch (final IOException e)
		{
			throw new SnRuntimeException(e);
		}
	}

	protected Optional<SnDocument> extractDocument(final Map<String, SnDocument> documents, final SnDocument target)
	{
		final String id = target.getId();
		if (StringUtils.isNotBlank(id))
		{
			return Optional.ofNullable(documents.get(id));
		}

		return Optional.empty();
	}

	protected Object extractDocumentValue(final SnDocument document, final SnIndexerFieldWrapper fieldWrapper)
	{
		if (MapUtils.isEmpty(document.getFields()))
		{
			return null;
		}

		return document.getFields().get(fieldWrapper.getFieldId());
	}
}
