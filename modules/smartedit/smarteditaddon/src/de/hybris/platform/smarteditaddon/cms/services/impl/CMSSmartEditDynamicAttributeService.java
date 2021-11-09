/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.smarteditaddon.cms.services.impl;

import static de.hybris.platform.smarteditaddon.constants.SmarteditContractHTMLAttributes.CLASS_ATTRIBUTE;
import static de.hybris.platform.smarteditaddon.constants.SmarteditContractHTMLAttributes.SMARTEDIT_CONTRACT_CATALOG_VERSION_UUID_ATTRIBUTE;
import static de.hybris.platform.smarteditaddon.constants.SmarteditContractHTMLAttributes.SMARTEDIT_CONTRACT_COMPONENT_CLASS;
import static de.hybris.platform.smarteditaddon.constants.SmarteditContractHTMLAttributes.SMARTEDIT_CONTRACT_COMPONENT_ID_ATTRIBUTE;
import static de.hybris.platform.smarteditaddon.constants.SmarteditContractHTMLAttributes.SMARTEDIT_CONTRACT_COMPONENT_TYPE_ATTRIBUTE;
import static de.hybris.platform.smarteditaddon.constants.SmarteditContractHTMLAttributes.SMARTEDIT_CONTRACT_COMPONENT_UUID_ATTRIBUTE;

import de.hybris.platform.acceleratorcms.services.CMSDynamicAttributeService;
import de.hybris.platform.cms2.misc.CMSFilter;
import de.hybris.platform.cms2.model.contents.CMSItemModel;
import de.hybris.platform.cms2.model.contents.components.AbstractCMSComponentModel;
import de.hybris.platform.cms2.model.contents.containers.AbstractCMSComponentContainerModel;
import de.hybris.platform.cms2.model.contents.contentslot.ContentSlotModel;
import de.hybris.platform.cms2.registry.CMSComponentContainerRegistry;
import de.hybris.platform.cmsfacades.data.ItemData;
import de.hybris.platform.cmsfacades.uniqueidentifier.UniqueItemIdentifierService;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.session.SessionService;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.jsp.PageContext;

import org.springframework.beans.factory.annotation.Required;


/**
 * SmartEdit implementation of the {@link CMSDynamicAttributeService}.
 */
public class CMSSmartEditDynamicAttributeService implements CMSDynamicAttributeService
{

	private UniqueItemIdentifierService uniqueItemIdentifierService;

	private SessionService sessionService;

	private CMSComponentContainerRegistry cmsComponentContainerRegistry;

	@Override
	public Map<String, String> getDynamicComponentAttributes(final AbstractCMSComponentModel component,
			final ContentSlotModel contentSlot)
	{
		final Map<String, String> dynamicAttributes = new HashMap<>();

		if (component != null && contentSlot != null && isEnabled() && shouldTheComponentBeWrapped(component, contentSlot))
		{

			final ItemData itemData = getUniqueItemIdentifierService().getItemData(component).orElseThrow(
					() -> new UnknownIdentifierException("Cannot generate uuid for component in CMSSmartEditDynamicAttributeService"));

			final ItemData catalogVersionData = getUniqueItemIdentifierService().getItemData(component.getCatalogVersion())
					.orElseThrow(() -> new UnknownIdentifierException(
							"Cannot generate uuid for component in CMSSmartEditDynamicAttributeService"));


			dynamicAttributes.put(SMARTEDIT_CONTRACT_COMPONENT_ID_ATTRIBUTE, component.getUid());
			dynamicAttributes.put(SMARTEDIT_CONTRACT_COMPONENT_UUID_ATTRIBUTE, itemData.getItemId());
			dynamicAttributes.put(SMARTEDIT_CONTRACT_COMPONENT_TYPE_ATTRIBUTE, component.getItemtype());
			dynamicAttributes.put(SMARTEDIT_CONTRACT_CATALOG_VERSION_UUID_ATTRIBUTE, catalogVersionData.getItemId());

			dynamicAttributes.put(CLASS_ATTRIBUTE, SMARTEDIT_CONTRACT_COMPONENT_CLASS);
		}

		return dynamicAttributes;
	}

	@Override
	public Map<String, String> getDynamicContentSlotAttributes(final ContentSlotModel contentSlot, final PageContext pageContext,
			final Map<String, String> initialMaps)
	{

		final Map<String, String> dynamicAttributes = new HashMap<>();

		if (isEnabled() && contentSlot != null)
		{

			final ItemData itemData = getUniqueItemIdentifierService().getItemData(contentSlot)
					.orElseThrow(() -> new UnknownIdentifierException(
							"Cannot generate uuid for content slot in CMSSmartEditDynamicAttributeService"));

			final ItemData catalogVersionData = getUniqueItemIdentifierService().getItemData(contentSlot.getCatalogVersion())
					.orElseThrow(() -> new UnknownIdentifierException(
							"Cannot generate uuid for component in CMSSmartEditDynamicAttributeService"));


			dynamicAttributes.put(SMARTEDIT_CONTRACT_COMPONENT_ID_ATTRIBUTE, contentSlot.getUid());
			dynamicAttributes.put(SMARTEDIT_CONTRACT_COMPONENT_UUID_ATTRIBUTE, itemData.getItemId());
			dynamicAttributes.put(SMARTEDIT_CONTRACT_COMPONENT_TYPE_ATTRIBUTE, contentSlot.getItemtype());
			dynamicAttributes.put(SMARTEDIT_CONTRACT_CATALOG_VERSION_UUID_ATTRIBUTE, catalogVersionData.getItemId());
			dynamicAttributes.put(CLASS_ATTRIBUTE, SMARTEDIT_CONTRACT_COMPONENT_CLASS);
		}

		return dynamicAttributes;
	}

	@Override
	public void afterAllItems(final PageContext pageContext)
	{
		if (!isEnabled())
		{
			return;
		}
	}

	/**
	 * will wrapp in a div any smarteditcomponent that is neither a NavigationBarComponent nor
	 * NavigationBarCollectionComponent
	 *
	 * @param cmsItemModel
	 * @return
	 */
	@Override
	public String getFallbackElement(final CMSItemModel cmsItemModel)
	{
		if (!isEnabled())
		{
			return null;
		}
		else
		{
			return "div";
		}
	}


	/**
	 * Checks if smarteditaddon is enabled by checking the presence of a cmsTicketId in session.
	 *
	 * @return true if this dyanamic attribute service is enabled and false otherwise.
	 */
	protected boolean isEnabled()
	{
		return getSessionService().getAttribute(CMSFilter.PREVIEW_TICKET_ID_PARAM) != null;
	}

	/**
	 * Checks if the current component is neither the direct child of the containing contentSlot not the child of a
	 * {@link AbstractCMSComponentContainerModel}, and instead is nested within other CMS component.
	 *
	 * @param component
	 *           The component to verify.
	 * @param contentSlot
	 *           The slot containing the current component.
	 * @return true if it's a direct child or child of a {@link AbstractCMSComponentContainerModel}, false otherwise.
	 */
	protected boolean shouldTheComponentBeWrapped(final AbstractCMSComponentModel component, final ContentSlotModel contentSlot)
	{
		if (component == null || contentSlot == null)
		{
			throw new IllegalArgumentException(
					"CMSSmartEditDynamicAttributeService.shouldTheComponentBeWrapped.component.and.slot.required");
		}

		return (contentSlot.getCmsComponents().stream().filter(e -> {

			if (e instanceof AbstractCMSComponentContainerModel)
			{
				final AbstractCMSComponentContainerModel container = (AbstractCMSComponentContainerModel) e;
				return getCmsComponentContainerRegistry().getStrategy(container).getDisplayComponentsForContainer(container).stream()
						.filter(f -> f.getUid().equals(component.getUid())).findAny().isPresent();
			}
			else
			{
				return e.getUid().equals(component.getUid());
			}
		}).findAny().isPresent());
	}

	protected SessionService getSessionService()
	{
		return sessionService;
	}

	@Required
	public void setSessionService(final SessionService sessionService)
	{
		this.sessionService = sessionService;
	}

	protected CMSComponentContainerRegistry getCmsComponentContainerRegistry()
	{
		return cmsComponentContainerRegistry;
	}

	@Required
	public void setCmsComponentContainerRegistry(CMSComponentContainerRegistry cmsComponentContainerRegistry)
	{
		this.cmsComponentContainerRegistry = cmsComponentContainerRegistry;
	}

	protected UniqueItemIdentifierService getUniqueItemIdentifierService()
	{
		return uniqueItemIdentifierService;
	}

	@Required
	public void setUniqueItemIdentifierService(final UniqueItemIdentifierService uniqueItemIdentifierService)
	{
		this.uniqueItemIdentifierService = uniqueItemIdentifierService;
	}

}
