/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commerceservices.search.searchservices.provider.impl;

import de.hybris.platform.core.PK;
import de.hybris.platform.core.model.media.MediaContainerModel;
import de.hybris.platform.core.model.media.MediaFormatModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.searchservices.core.SnException;
import de.hybris.platform.searchservices.core.service.SnExpressionEvaluator;
import de.hybris.platform.searchservices.indexer.SnIndexerException;
import de.hybris.platform.searchservices.indexer.service.SnIndexerContext;
import de.hybris.platform.searchservices.indexer.service.SnIndexerFieldWrapper;
import de.hybris.platform.searchservices.indexer.service.SnIndexerValueProvider;
import de.hybris.platform.searchservices.util.ParameterUtils;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.keyvalue.MultiKey;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Required;


/**
 * Implementation of {@link SnIndexerValueProvider} for product image attributes.
 */
public class ProductImageAttributeSnIndexerValueProvider
		extends AbstractProductSnIndexerValueProvider<ProductModel, ProductImageAttributeSnIndexerValueProvider.ProductImageData>
{
	public static final String ID = "productImageAttributeSnIndexerValueProvider";

	public static final String EXPRESSION_PARAM = "expression";
	public static final String EXPRESSION_DEFAULT_VALUE = MediaModel.URL;

	public static final String MEDIA_EXPRESSION_PARAM = "mediaExpression";
	public static final String MEDIA_EXPRESSION_DEFAULT_VALUE = ProductModel.GALLERYIMAGES;

	public static final String MEDIA_CONTAINER_PARAM = "mediaContainer";
	public static final String MEDIA_CONTAINER_DEFAULT_VALUE = null;

	public static final String MEDIA_FORMAT_PARAM = "mediaFormat";
	public static final String MEDIA_FORMAT_DEFAULT_VALUE = null;

	protected static final Set<Class<?>> SUPPORTED_QUALIFIER_CLASSES = Set.of(Locale.class);

	private SnExpressionEvaluator snExpressionEvaluator;

	@Override
	public Set<Class<?>> getSupportedQualifierClasses() throws SnIndexerException
	{
		return SUPPORTED_QUALIFIER_CLASSES;
	}

	@Override
	protected Object getFieldValue(final SnIndexerContext indexerContext, final SnIndexerFieldWrapper fieldWrapper,
			final ProductModel source, final ProductImageData data) throws SnIndexerException
	{
		try
		{
			final String expression = resolveExpression(fieldWrapper);
			final String productSelector = resolveProductSelector(fieldWrapper);
			final Set<ProductModel> products = data.getProducts().get(productSelector);
			final String mediaExpression = resolveMediaExpression(fieldWrapper);
			final String mediaContainer = resolveMediaContainer(fieldWrapper);
			final String mediaFormat = resolveMediaFormat(fieldWrapper);
			final MultiKey<String> mediaKey = buildMediaKey(mediaExpression, mediaContainer, mediaFormat);

			final Set<MediaModel> medias = new LinkedHashSet<>();

			for (final ProductModel product : products)
			{
				medias.addAll(data.getMedias().get(product.getPk()).get(mediaKey));
			}

			if (CollectionUtils.isEmpty(medias))
			{
				return null;
			}

			if (fieldWrapper.isLocalized())
			{
				final List<Locale> locales = fieldWrapper.getQualifiers().stream().map(qualifier -> qualifier.getAs(Locale.class))
						.collect(Collectors.toList());
				return snExpressionEvaluator.evaluate(medias, expression, locales);
			}
			else
			{
				return snExpressionEvaluator.evaluate(medias, expression);
			}
		}
		catch (final SnException e)
		{
			throw new SnIndexerException(e);
		}
	}

	@Override
	protected ProductImageData loadData(final SnIndexerContext indexerContext,
			final Collection<SnIndexerFieldWrapper> fieldWrappers, final ProductModel source) throws SnIndexerException
	{
		final Map<String, Set<ProductModel>> products = collectProducts(fieldWrappers, source);
		final Set<ProductModel> mergedProducts = mergeProducts(products);
		final Map<PK, Map<MultiKey<String>, Set<MediaModel>>> medias = collectMedias(fieldWrappers, mergedProducts);

		final ProductImageData data = new ProductImageData();
		data.setProducts(products);
		data.setMedias(medias);

		return data;
	}

	protected Map<PK, Map<MultiKey<String>, Set<MediaModel>>> collectMedias(final Collection<SnIndexerFieldWrapper> fieldWrappers,
			final Collection<ProductModel> products) throws SnIndexerException
	{
		final Set<MultiKey<String>> mediaKeys = collectMediaKeys(fieldWrappers);
		final Set<String> mediaExpressions = collectMediaExpressions(fieldWrappers);
		final Map<PK, Map<MultiKey<String>, Set<MediaModel>>> medias = new HashMap<>();

		for (final ProductModel product : products)
		{
			final Map<MultiKey<String>, Set<MediaModel>> productMedias = new LinkedHashMap<>();
			for (final MultiKey<String> mediaKey : mediaKeys)
			{
				productMedias.put(mediaKey, new LinkedHashSet<>());
			}

			for (final String mediaExpression : mediaExpressions)
			{
				doCollectMedias(productMedias, mediaExpression, product);
			}

			medias.put(product.getPk(), productMedias);
		}

		return medias;
	}

	protected Set<MultiKey<String>> collectMediaKeys(final Collection<SnIndexerFieldWrapper> fieldWrappers)
	{
		final Set<MultiKey<String>> mediaKeys = new HashSet<>();

		for (final SnIndexerFieldWrapper fieldWrapper : fieldWrappers)
		{
			final String mediaExpression = resolveMediaExpression(fieldWrapper);
			final String mediaContainer = resolveMediaContainer(fieldWrapper);
			final String mediaFormat = resolveMediaFormat(fieldWrapper);
			mediaKeys.add(buildMediaKey(mediaExpression, mediaContainer, mediaFormat));
		}

		return mediaKeys;
	}

	protected Set<String> collectMediaExpressions(final Collection<SnIndexerFieldWrapper> fieldWrappers)
	{
		final Set<String> mediaExpressions = new HashSet<>();

		for (final SnIndexerFieldWrapper fieldWrapper : fieldWrappers)
		{
			final String mediaExpression = resolveMediaExpression(fieldWrapper);
			mediaExpressions.add(mediaExpression);
		}

		return mediaExpressions;
	}

	protected void doCollectMedias(final Map<MultiKey<String>, Set<MediaModel>> data, final String mediaExpression,
			final ProductModel product) throws SnIndexerException
	{
		try
		{
			final Object mediaSource = snExpressionEvaluator.evaluate(product, mediaExpression);
			if (mediaSource instanceof Collection)
			{
				for (final Object mediaSourceItem : (Collection) mediaSource)
				{
					doCollectMediasFromObject(data, mediaExpression, mediaSourceItem);
				}
			}
			else
			{
				doCollectMediasFromObject(data, mediaExpression, mediaSource);
			}
		}
		catch (final SnException e)
		{
			throw new SnIndexerException(e);
		}
	}

	protected void doCollectMediasFromObject(final Map<MultiKey<String>, Set<MediaModel>> data, final String mediaExpression,
			final Object mediaSource)
	{
		if (mediaSource instanceof MediaContainerModel)
		{
			doCollectMediasFromMediaContainer(data, mediaExpression, (MediaContainerModel) mediaSource);
		}
		else if (mediaSource instanceof MediaModel)
		{
			doCollectMediasFromMedia(data, mediaExpression, null, (MediaModel) mediaSource);
		}
	}

	protected void doCollectMediasFromMediaContainer(final Map<MultiKey<String>, Set<MediaModel>> data,
			final String mediaExpression, final MediaContainerModel mediaContainer)
	{
		final Collection<MediaModel> medias = mediaContainer.getMedias();
		if (CollectionUtils.isEmpty(medias))
		{
			return;
		}

		for (final MediaModel media : medias)
		{
			doCollectMediasFromMedia(data, mediaExpression, mediaContainer, media);
		}
	}

	protected void doCollectMediasFromMedia(final Map<MultiKey<String>, Set<MediaModel>> data, final String mediaExpression,
			final MediaContainerModel mediaContainer, final MediaModel media)
	{
		final MediaFormatModel mediaFormat = media.getMediaFormat();
		for (final MultiKey<String> mediaKey : buildMediaKeys(mediaExpression, mediaContainer, mediaFormat))
		{
			final Set<MediaModel> dataMedias = data.get(mediaKey);
			if (dataMedias != null)
			{
				dataMedias.add(media);
			}
		}
	}

	protected MultiKey<String> buildMediaKey(final String mediaExpression, final String mediaContainer, final String mediaFormat)
	{
		return new MultiKey<>(mediaExpression, mediaContainer, mediaFormat);
	}

	protected Set<MultiKey<String>> buildMediaKeys(final String mediaExpression, final MediaContainerModel mediaContainer,
			final MediaFormatModel mediaFormat)
	{
		String mediaContainerKey = MEDIA_CONTAINER_DEFAULT_VALUE;
		if (mediaContainer != null && StringUtils.isNotBlank(mediaContainer.getQualifier()))
		{
			mediaContainerKey = mediaContainer.getQualifier();
		}

		String mediaFormatKey = MEDIA_FORMAT_DEFAULT_VALUE;
		if (mediaFormat != null && StringUtils.isNotBlank(mediaFormat.getQualifier()))
		{
			mediaFormatKey = mediaFormat.getQualifier();
		}

		final Set<MultiKey<String>> mediaKeys = new HashSet<>();
		mediaKeys.add(buildMediaKey(mediaExpression, mediaContainerKey, mediaFormatKey));
		mediaKeys.add(buildMediaKey(mediaExpression, MEDIA_CONTAINER_DEFAULT_VALUE, mediaFormatKey));
		mediaKeys.add(buildMediaKey(mediaExpression, mediaContainerKey, MEDIA_FORMAT_DEFAULT_VALUE));
		mediaKeys.add(buildMediaKey(mediaExpression, MEDIA_CONTAINER_DEFAULT_VALUE, MEDIA_FORMAT_DEFAULT_VALUE));

		return mediaKeys;
	}

	protected String resolveExpression(final SnIndexerFieldWrapper fieldWrapper)
	{
		return ParameterUtils.getString(fieldWrapper.getValueProviderParameters(), EXPRESSION_PARAM, EXPRESSION_DEFAULT_VALUE);
	}

	protected String resolveMediaExpression(final SnIndexerFieldWrapper fieldWrapper)
	{
		return ParameterUtils
				.getString(fieldWrapper.getValueProviderParameters(), MEDIA_EXPRESSION_PARAM, MEDIA_EXPRESSION_DEFAULT_VALUE);
	}

	protected String resolveMediaContainer(final SnIndexerFieldWrapper fieldWrapper)
	{
		return ParameterUtils
				.getString(fieldWrapper.getValueProviderParameters(), MEDIA_CONTAINER_PARAM, MEDIA_CONTAINER_DEFAULT_VALUE);
	}

	protected String resolveMediaFormat(final SnIndexerFieldWrapper fieldWrapper)
	{
		return ParameterUtils.getString(fieldWrapper.getValueProviderParameters(), MEDIA_FORMAT_PARAM, MEDIA_FORMAT_DEFAULT_VALUE);
	}

	public SnExpressionEvaluator getSnExpressionEvaluator()
	{
		return snExpressionEvaluator;
	}

	@Required
	public void setSnExpressionEvaluator(final SnExpressionEvaluator snExpressionEvaluator)
	{
		this.snExpressionEvaluator = snExpressionEvaluator;
	}

	protected static class ProductImageData
	{
		private Map<String, Set<ProductModel>> products;
		private Map<PK, Map<MultiKey<String>, Set<MediaModel>>> medias;

		public Map<String, Set<ProductModel>> getProducts()
		{
			return products;
		}

		public void setProducts(final Map<String, Set<ProductModel>> products)
		{
			this.products = products;
		}

		public Map<PK, Map<MultiKey<String>, Set<MediaModel>>> getMedias()
		{
			return medias;
		}

		public void setMedias(final Map<PK, Map<MultiKey<String>, Set<MediaModel>>> medias)
		{
			this.medias = medias;
		}
	}
}
