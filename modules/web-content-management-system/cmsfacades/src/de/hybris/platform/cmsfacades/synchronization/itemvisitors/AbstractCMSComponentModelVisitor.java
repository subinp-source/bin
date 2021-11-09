/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.synchronization.itemvisitors;

import static com.google.common.collect.Lists.newLinkedList;

import de.hybris.platform.cms2.model.contents.CMSItemModel;
import de.hybris.platform.cms2.model.contents.components.AbstractCMSComponentModel;
import de.hybris.platform.cms2.model.contents.contentslot.ContentSlotModel;
import de.hybris.platform.cms2.model.navigation.CMSNavigationNodeModel;
import de.hybris.platform.cms2.servicelayer.services.AttributeDescriptorModelHelperService;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.servicelayer.exceptions.AttributeNotSupportedException;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.model.visitor.ItemVisitor;
import de.hybris.platform.servicelayer.type.TypeService;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;


/**
 * Abstract class for visiting {@link AbstractCMSComponentModel} models for the cms synchronization service to work
 * properly. In this implementation, it will collect all component's child (collection or not) extending or being
 * {@link CMSItemModel}
 *
 * @param <CMSCOMPONENTTYPE>
 *           the component type that extends {@link AbstractCMSComponentModel}
 */
public abstract class AbstractCMSComponentModelVisitor<CMSCOMPONENTTYPE extends AbstractCMSComponentModel>
		implements ItemVisitor<CMSCOMPONENTTYPE>
{
	private static Logger LOG = LoggerFactory.getLogger(AbstractCMSComponentModelVisitor.class);
	private AttributeDescriptorModelHelperService attributeDescriptorModelHelperService;
	private TypeService typeService;
	private ModelService modelService;

	@Override
	public List<ItemModel> visit(final CMSCOMPONENTTYPE source, final List<ItemModel> arg1, final Map<String, Object> arg2)
	{
		if (getTypeService() == null)
		{
			// case of not-fixed acceleratorfacades code that is not using the parent bean properly
			return newLinkedList(source.getRestrictions());
		}

		final List<ItemModel> toVisit = newLinkedList();
		final ComposedTypeModel composedTypeModel = getTypeService().getComposedTypeForClass(source.getClass());
		Stream.of(composedTypeModel.getDeclaredattributedescriptors(), composedTypeModel.getInheritedattributedescriptors())
				.flatMap(Collection::stream)
				.filter(attributeDescriptorModel -> isClassAssignableFrom(CMSItemModel.class, attributeDescriptorModel))
				.filter(attributeDescriptorModel -> !isClassAssignableFrom(ContentSlotModel.class, attributeDescriptorModel))
				.filter(attributeDescriptorModel -> !isClassAssignableFrom(CMSNavigationNodeModel.class, attributeDescriptorModel))
				.forEach(attributeDescriptorModel -> collectChildItems(source, toVisit, attributeDescriptorModel));
		return toVisit;
	}

	/**
	 * Collects all child items of type {@link CMSItemModel} for the provided source component.
	 *
	 * @param source
	 *           - the component which attributes is inspected to collect child items
	 * @param toVisit
	 *           - the list of items collected which will be used by the synchronization service
	 * @param attributeDescriptorModel
	 *           - the attribute descriptor object containing the information about the attribute of interest
	 */
	protected void collectChildItems(final CMSCOMPONENTTYPE source, final List<ItemModel> toVisit,
			final AttributeDescriptorModel attributeDescriptorModel)
	{
		Optional.ofNullable(getAttributeValue(source, attributeDescriptorModel)).ifPresent(propertyValue -> {
			if (Collection.class.isAssignableFrom(propertyValue.getClass()))
			{
				final Collection<CMSItemModel> collection = (Collection<CMSItemModel>) propertyValue;
				if (!collection.isEmpty())
				{
					toVisit.addAll(collection);
				}
			}
			else
			{
				toVisit.add((CMSItemModel) propertyValue);
			}
		});
	}

	/**
	 * Gets the value by calling the getter for the attribute defined by the provided attribute descriptor.
	 * <p>
	 * When the attribute is not readable (due to configurations in *items.xml), the ModelService will return an
	 * {@link AttributeNotSupportedException} and the attribute will not be added to the list of items to visit by the
	 * synchronization service.
	 *
	 * @param source
	 *           - the source object which the getter method will be called
	 * @param attributeDescriptorModel
	 *           - the attribute descriptor object containing the information about the attribute of interest
	 * @return the result of the getter; can be {@code NULL}
	 */
	protected Object getAttributeValue(final CMSCOMPONENTTYPE source, final AttributeDescriptorModel attributeDescriptorModel)
	{
		try
		{
			return getModelService().getAttributeValue(source, attributeDescriptorModel.getQualifier());
		}
		catch (final AttributeNotSupportedException e)
		{
			// ignore attributes that are not readable
			LOG.debug(e.getMessage(), e);
		}
		return null;
	}

	protected boolean isClassAssignableFrom(final Class clazz, final AttributeDescriptorModel attributeDescriptor)
	{
		return clazz.isAssignableFrom(getAttributeDescriptorModelHelperService().getAttributeClass(attributeDescriptor));
	}

	protected TypeService getTypeService()
	{
		return typeService;
	}

	@Required
	public void setTypeService(final TypeService typeService)
	{
		this.typeService = typeService;
	}

	protected ModelService getModelService()
	{
		return modelService;
	}

	@Required
	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
	}

	protected AttributeDescriptorModelHelperService getAttributeDescriptorModelHelperService()
	{
		return attributeDescriptorModelHelperService;
	}

	@Required
	public void setAttributeDescriptorModelHelperService(
			final AttributeDescriptorModelHelperService attributeDescriptorModelHelperService)
	{
		this.attributeDescriptorModelHelperService = attributeDescriptorModelHelperService;
	}
}
