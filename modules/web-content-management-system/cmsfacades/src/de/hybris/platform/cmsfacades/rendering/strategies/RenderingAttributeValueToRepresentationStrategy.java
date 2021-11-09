/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.rendering.strategies;

import de.hybris.platform.cms2.common.functions.Converter;
import de.hybris.platform.cms2.model.contents.components.AbstractCMSComponentModel;
import de.hybris.platform.cmsfacades.cmsitems.AttributeValueToRepresentationConverter;
import de.hybris.platform.cmsfacades.cmsitems.AttributeValueToRepresentationStrategy;
import de.hybris.platform.cmsfacades.cmsitems.converter.DefaultCMSItemConverter;
import de.hybris.platform.cmsfacades.data.AbstractCMSComponentData;
import de.hybris.platform.cmsfacades.rendering.cache.RenderingCacheService;
import de.hybris.platform.cmsfacades.rendering.predicates.attributes.AttributeContainsCMSComponentsPredicate;
import de.hybris.platform.cmsfacades.rendering.visibility.RenderingVisibilityService;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.Collection;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

import org.springframework.beans.factory.annotation.Required;


/**
 * This class is used by the {@link DefaultCMSItemConverter} to convert items into a representation suited for rendering
 * purposes.
 * The transformation strategy is be based on the following rules:
 * - Simple Value:
 * -> If it's a Cms Component it needs to be converted into an AbstractCMSComponentData
 * (through the cmsComponentModelToDataRenderingConverter)
 * -> Otherwise, use the default transformation function.
 * - Collection:
 * -> If the collection contains Cms Components, get simple representation (uid).
 * -> Otherwise, use the default transformation function for each item.
 * - Localized Value:
 * -> Get value for current language, and use same approach as Simple Value.
 * - Localized Collection:
 * -> For each element, do the same as Localized Value.
 */
public class RenderingAttributeValueToRepresentationStrategy implements AttributeValueToRepresentationStrategy
{
	// --------------------------------------------------------------------------
	// Variables
	// --------------------------------------------------------------------------
	private ModelService modelService;
	private RenderingVisibilityService renderingVisibilityService;
	private AttributeContainsCMSComponentsPredicate attributeContainsCMSComponentsPredicate;
	private Converter<AbstractCMSComponentModel, String> simpleCmsComponentAttributeConverter;
	private de.hybris.platform.servicelayer.dto.converter.Converter<AbstractCMSComponentModel, AbstractCMSComponentData> cmsComponentModelToDataRenderingConverter;
	private AttributeValueToRepresentationConverter<Collection<Object>, Collection<Object>> collectionToRepresentationConverter;
	private RenderingCacheService<AbstractCMSComponentData> renderingCacheService;

	// --------------------------------------------------------------------------
	// Public API
	// --------------------------------------------------------------------------

	@Override
	public Supplier<Object> getSimpleGetter(final AttributeDescriptorModel attribute, final ItemModel sourceModel,
			final Function<Object, Object> goDeeperOrSerialize)
	{
		final Function<Object, Object> elementTransformationFunction = getElementToRenderingDataTransformationFunction(attribute,
				goDeeperOrSerialize);
		return () -> elementTransformationFunction
				.apply(getModelService().getAttributeValue(sourceModel, attribute.getQualifier()));
	}

	@Override
	@SuppressWarnings("unchecked")
	public Supplier<Object> getCollectionGetter(final AttributeDescriptorModel attribute, final ItemModel sourceModel,
			final Function<Object, Object> goDeeperOrSerialize)
	{
		final Function<Object, Object> elementTransformationFunction = getElementToRenderingDataTransformationFunction(attribute,
				goDeeperOrSerialize);

		return () ->
		{
			if (attributeContainsCmsComponents(attribute))
			{
				// CMS Components inside a collection need to be treated in a very simplistic way; instead of processing them further
				// in the default way they just need to be converted into a uid -> this is done in the simpleCmsComponentConverter.
				return getCollectionToRepresentationConverter().convert(attribute,
						getAttributeValue(attribute, sourceModel),
						element -> getSimpleCmsComponentAttributeConverter().convert((AbstractCMSComponentModel) element));
			}
			else
			{
				return getCollectionToRepresentationConverter().convert(attribute,
						getAttributeValue(attribute, sourceModel), elementTransformationFunction);
			}
		};
	}

	@Override
	public Supplier<Object> getLocalizedGetter(final AttributeDescriptorModel attribute, final ItemModel sourceModel,
			final Function<Object, Object> goDeeperOrSerialize)
	{
		final Function<Object, Object> elementTransformationFunction = getElementToRenderingDataTransformationFunction(attribute,
				goDeeperOrSerialize);
		return () -> elementTransformationFunction.apply(getAttributeValue(attribute, sourceModel));
	}

	@Override
	public Supplier<Object> getLocalizedCollectionGetter(final AttributeDescriptorModel attribute, final ItemModel sourceModel,
			final Function<Object, Object> goDeeperOrSerialize)
	{
		final Function<Object, Object> elementTransformationFunction = getElementToRenderingDataTransformationFunction(attribute,
				goDeeperOrSerialize);
		return () -> getCollectionToRepresentationConverter().convert(attribute,
				getAttributeValue(attribute, sourceModel), elementTransformationFunction);
	}

	// --------------------------------------------------------------------------
	// Helper Methods
	// --------------------------------------------------------------------------

	/**
	 * Helper method to determine whether an attribute contains cms components.
	 *
	 * @param attributeDescriptorModel
	 * 		The attribute to analyze.
	 * @return True if the attribute contains cms components, false otherwise.
	 */
	protected boolean attributeContainsCmsComponents(final AttributeDescriptorModel attributeDescriptorModel)
	{
		return getAttributeContainsCMSComponentsPredicate().test(attributeDescriptorModel);
	}

	/**
	 * This method returns the transformation function to use to render CMS components.
	 * The payload of the component is retrieved from the cache if {@link RenderingCacheService} is enabled.
	 *
	 * @return The transformation function to use for CMS components.
	 */
	protected Function<Object, Object> getCmsComponentRenderingDataTransformationFunction()
	{
		return (component) -> {
			final AbstractCMSComponentModel abstractCMSComponent = (AbstractCMSComponentModel) component;
			return getRenderingCacheService().cacheOrElse(abstractCMSComponent,
					() -> getCmsComponentModelToDataRenderingConverter().convert(abstractCMSComponent));
		};
	}

	/**
	 * This method receives a transformation function, and wraps it in a new function that contains necessary logic for
	 * rendering purposes.
	 *
	 * @param attributeDescriptorModel
	 * 		The model that describes the attribute whose element(s) will be transformed with the provided
	 * 		transformation function.
	 * @param defaultTransformationFunction
	 * 		The default function used to transform element(s) in the provided attribute.
	 * @return The new function that contains the extra logic needed for rendering purposes.
	 */
	protected Function<Object, Object> getElementToRenderingDataTransformationFunction(
			final AttributeDescriptorModel attributeDescriptorModel, final Function<Object, Object> defaultTransformationFunction)
	{
		return element -> Optional.ofNullable(element)
				.filter(el -> isElementEligibleForRendering(attributeDescriptorModel, el))
				.map(el -> attributeContainsCmsComponents(attributeDescriptorModel) ?
						getCmsComponentRenderingDataTransformationFunction().apply(el) :
						defaultTransformationFunction.apply(el)
				)
				.orElse(null);
	}

	/**
	 * This method is used to determine if an element is allowed to be rendered.
	 * - If the element is not a component, it's always allowed.
	 * - If it's a component, it must be explicitly allowed (visible and no restrictions in effect).
	 *
	 * @param attributeDescriptorModel
	 * 		The model describing the attribute to analyze.
	 * @param element
	 * 		The item to analyze.
	 * @return true if the element is allowed to be rendered. False otherwise.
	 */
	protected boolean isElementEligibleForRendering(final AttributeDescriptorModel attributeDescriptorModel, final Object element)
	{
		return !attributeContainsCmsComponents(attributeDescriptorModel) ||
				getRenderingVisibilityService().isVisible((AbstractCMSComponentModel) element);
	}

	/**
	 * This method is used to get the value of the desired attribute in the provided source model.
	 *
	 * @param attribute
	 * 		The attribute to retrieve from the model
	 * @param sourceModel
	 * 		The model where to retrieve the value from.
	 * @param <T>
	 * 		The type of the returned value.
	 * @return The value of the attribute in the provided source model.
	 */
	protected <T> T getAttributeValue(final AttributeDescriptorModel attribute, final ItemModel sourceModel)
	{
		return getModelService().getAttributeValue(sourceModel, attribute.getQualifier());
	}


	// --------------------------------------------------------------------------
	// Getters/Setters
	// --------------------------------------------------------------------------
	protected ModelService getModelService()
	{
		return modelService;
	}

	@Required
	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
	}

	protected AttributeContainsCMSComponentsPredicate getAttributeContainsCMSComponentsPredicate()
	{
		return attributeContainsCMSComponentsPredicate;
	}

	@Required
	public void setAttributeContainsCMSComponentsPredicate(
			final AttributeContainsCMSComponentsPredicate attributeContainsCMSComponentsPredicate)
	{
		this.attributeContainsCMSComponentsPredicate = attributeContainsCMSComponentsPredicate;
	}

	protected Converter<AbstractCMSComponentModel, String> getSimpleCmsComponentAttributeConverter()
	{
		return simpleCmsComponentAttributeConverter;
	}

	@Required
	public void setSimpleCmsComponentAttributeConverter(
			final Converter<AbstractCMSComponentModel, String> simpleCmsComponentAttributeConverter)
	{
		this.simpleCmsComponentAttributeConverter = simpleCmsComponentAttributeConverter;
	}

	protected AttributeValueToRepresentationConverter<Collection<Object>, Collection<Object>> getCollectionToRepresentationConverter()
	{
		return collectionToRepresentationConverter;
	}

	@Required
	public void setCollectionToRepresentationConverter(
			final AttributeValueToRepresentationConverter<Collection<Object>, Collection<Object>> collectionToRepresentationConverter)
	{
		this.collectionToRepresentationConverter = collectionToRepresentationConverter;
	}

	protected RenderingVisibilityService getRenderingVisibilityService()
	{
		return renderingVisibilityService;
	}

	@Required
	public void setRenderingVisibilityService(
			final RenderingVisibilityService renderingVisibilityService)
	{
		this.renderingVisibilityService = renderingVisibilityService;
	}

	protected de.hybris.platform.servicelayer.dto.converter.Converter<AbstractCMSComponentModel, AbstractCMSComponentData> getCmsComponentModelToDataRenderingConverter()
	{
		return cmsComponentModelToDataRenderingConverter;
	}

	@Required
	public void setCmsComponentModelToDataRenderingConverter(
			final de.hybris.platform.servicelayer.dto.converter.Converter<AbstractCMSComponentModel, AbstractCMSComponentData> cmsComponentModelToDataRenderingConverter)
	{
		this.cmsComponentModelToDataRenderingConverter = cmsComponentModelToDataRenderingConverter;
	}

	public RenderingCacheService<AbstractCMSComponentData> getRenderingCacheService()
	{
		return renderingCacheService;
	}

	@Required
	public void setRenderingCacheService(
			final RenderingCacheService<AbstractCMSComponentData> renderingCacheService)
	{
		this.renderingCacheService = renderingCacheService;
	}
}
