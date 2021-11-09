/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.configurablebundleservices.bundle.impl;

import de.hybris.platform.commerceservices.order.CommerceCartModificationException;
import de.hybris.platform.core.enums.GroupType;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.configurablebundleservices.bundle.AbstractBundleComponentEditableChecker;
import de.hybris.platform.configurablebundleservices.model.BundleTemplateModel;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

import de.hybris.platform.core.order.EntryGroup;
import de.hybris.platform.order.EntryGroupService;
import org.apache.commons.collections.CollectionUtils;

import javax.annotation.Nonnull;

import org.springframework.beans.factory.annotation.Required;


/**
 * Default implementation of the {@link AbstractBundleComponentEditableChecker}
 */
public abstract class DefaultAbstractBundleComponentEditableChecker<O extends AbstractOrderModel>
		implements AbstractBundleComponentEditableChecker<O>
{
	private EntryGroupService entryGroupService;


	@Override
	public boolean isAutoPickComponent(final BundleTemplateModel bundleTemplate)
	{
		return false;
	}

	@Override
	public boolean isRequiredDependencyMet(@Nonnull final O order, @Nonnull final BundleTemplateModel bundleTemplate,
			@Nonnull final Integer entryGroupNumber)
	{
		final Collection<BundleTemplateModel> requiredComponents = bundleTemplate.getRequiredBundleTemplates();
		if (CollectionUtils.isEmpty(requiredComponents))
		{
			return true;
		}
		final EntryGroup rootGroup = getEntryGroupService().getRoot(order, entryGroupNumber);
		final Set<Integer> requiredGroupNumbers = bundleTemplatesToGroupNumbers(rootGroup, requiredComponents);
		final Set<Integer> notEmptyGroupNumbers = getPopulatedGroupNumbers(order);
		return CollectionUtils.isSubCollection(requiredGroupNumbers, notEmptyGroupNumbers);
	}

	@Nonnull
	protected Set<Integer> bundleTemplatesToGroupNumbers(
			@Nonnull final EntryGroup rootGroup, @Nonnull final Collection<BundleTemplateModel> components)
	{
		final Set<String> requiredComponentIds = components.stream()
				.map(BundleTemplateModel::getId)
				.collect(Collectors.toSet());
		return getEntryGroupService().getLeaves(rootGroup).stream()
				.filter(group -> GroupType.CONFIGURABLEBUNDLE.equals(group.getGroupType()))
				.filter(group -> requiredComponentIds.contains(group.getExternalReferenceId()))
				.map(EntryGroup::getGroupNumber)
				.collect(Collectors.toSet());
	}

	@Nonnull
	protected Set<Integer> getPopulatedGroupNumbers(@Nonnull final AbstractOrderModel order)
	{
		return order.getEntries().stream()
				.map(AbstractOrderEntryModel::getEntryGroupNumbers)
				.flatMap(Collection::stream)
				.collect(Collectors.toSet());
	}

	protected void checkComponentIsLeaf(@Nonnull final BundleTemplateModel bundleTemplate) throws CommerceCartModificationException
	{
		if(bundleTemplate.getChildTemplates() != null && !bundleTemplate.getChildTemplates().isEmpty())
		{
			throw new CommerceCartModificationException("Component '" + bundleTemplate.getId()
					+ "' cannot be modified as it has non-emptpy list of child components");
		}
	}

	protected EntryGroupService getEntryGroupService()
	{
		return entryGroupService;
	}

	@Required
	public void setEntryGroupService(EntryGroupService entryGroupService)
	{
		this.entryGroupService = entryGroupService;
	}
}
