/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.configurablebundleservices.bundle;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.configurablebundleservices.model.BundleTemplateModel;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.order.EntryGroup;

import java.util.List;
import java.util.Set;

import javax.annotation.Nonnull;

import javax.annotation.Nullable;


/**
 * Bundle template service that exposes methods to deal with bundle template operations.
 *
 * @spring.bean bundleTemplateService
 */
public interface BundleTemplateService
{

	/**
	 * This method returns the root bundleTemplate {@link BundleTemplateModel} of the given
	 * <code>bundleTemplateModel</code>. In case the given <code>bundleTemplateModel</code> is the root template the
	 * given <code>bundleTemplateModel</code> is returned as root template.
	 *
	 * @param bundleTemplateModel
	 *           Bundle Template Model
	 * @return {@link BundleTemplateModel}
	 */
	@Nonnull
	BundleTemplateModel getRootBundleTemplate(@Nonnull final BundleTemplateModel bundleTemplateModel);

	/**
	 * This method returns the bundle template by bundle id <code>bundleId</code>
	 *
	 * @param bundleId
	 *           Bundle Id
	 * @return BundleTemplateModel {@link BundleTemplateModel}
	 */
	@Nonnull
	BundleTemplateModel getBundleTemplateForCode(@Nonnull final String bundleId);

	/**
	 * Get all components (child bundle templates) where the current device is included in the products list
	 *
	 * @param model
	 *           Product Model
	 * @return List - {@link BundleTemplateModel}
	 */
	@Nonnull
	List<BundleTemplateModel> getBundleTemplatesByProduct(@Nonnull final ProductModel model);

	/**
	 * Returns the next child bundle template. The logic retrieves the parent of the given <code>bundleTemplate</code>
	 * and selects the parents next child template, e.g. SMAR_Handset (given <code>bundleTemplate</code>) -> SMAR (parent
	 * template) -> SMAR_Plan (next child template of parent), SMAR_Plan is returned.
	 *
	 * @param bundleTemplate
	 *           Bundle Template Model
	 * @return {@link BundleTemplateModel}
	 */
	@Nullable
	BundleTemplateModel getSubsequentBundleTemplate(@Nonnull BundleTemplateModel bundleTemplate);

	/**
	 * Returns the previous child bundle template. The logic retrieves the parent of the given
	 * <code>bundleTemplate</code> and selects the parents next child template, e.g. SMAR_Plan (given
	 * <code>bundleTemplate</code>) -> SMAR (parent template) -> SMAR_Handset (previous child template of parent),
	 * SMAR_Handset is returned.
	 *
	 * @param bundleTemplate
	 *           Bundle Template Model
	 * @return {@link BundleTemplateModel}
	 */
	@Nullable
	BundleTemplateModel getPreviousBundleTemplate(@Nonnull BundleTemplateModel bundleTemplate);

	/**
	 * Returns a relative child bundle template. The logic retrieves the parent of the given <code>bundleTemplate</code>
	 * and selects the parents relative child template
	 *
	 * @param bundleTemplate
	 *           Bundle Template Model
	 * @param relativePosition
	 *           the relative position of the child template to be returned
	 * @return {@link BundleTemplateModel}
	 */
	@Nullable
	BundleTemplateModel getRelativeBundleTemplate(@Nonnull final BundleTemplateModel bundleTemplate, final int relativePosition);

	/**
	 * Collect all leaf components of bundle.
	 *
	 * @param anyComponent
	 *           any component in bundle hierarchy
	 * @return list of leaf components in correct order
	 */
	@Nonnull
	List<BundleTemplateModel> getLeafComponents(@Nonnull final BundleTemplateModel anyComponent);

	/**
	 * This method returns the bundle template by bundle id <code>bundleId</code>
	 *
	 * @param bundleId
	 *           Bundle Id
	 * @param version
	 *           bundle version
	 * @return BundleTemplateModel {@link BundleTemplateModel}
	 */
	@Nonnull
	BundleTemplateModel getBundleTemplateForCode(@Nonnull final String bundleId, @Nonnull final String version);

	/**
	 * Checks if {@link BundleTemplateModel} has a selection criteria of auto pick type.
	 *
	 * @param bundleTemplate
	 *           Bundle Template Model
	 * @return <code>true</code> if selection criteria has auto pick type, otherwise
	 *         <code>false</code>
	 * @deprecated since 1811 - auto pick component is a part of subscriptionbundles
	 */
	@Deprecated(since = "1811", forRemoval = true)
	boolean isAutoPickComponent(@Nullable final BundleTemplateModel bundleTemplate);

	/**
	 * This methods returns all root bundle templates.
	 *
	 * @param catVer
	 *           catalog version where bundle templates will be searched in
	 * @return {@link List} of {@link BundleTemplateModel}s
	 */
	@Nonnull
	List<BundleTemplateModel> getAllRootBundleTemplates(final CatalogVersionModel catVer);


	/**
	 * Returns the given <code>bundleTemplate</code>'s absolute position in the list of child templates that are assigned
	 * to the parent template
	 *
	 * @param bundleTemplate
	 *           the child template whose position shall be returned
	 *
	 * @return absolute position as int
	 */
	int getPositionInParent(@Nonnull BundleTemplateModel bundleTemplate);

	/**
	 * This methods returns all approved root bundle templates.
	 *
	 * @param catalogVersion
	 *           catalog version where bundle templates will be searched in
	 * @return {@link List} of {@link BundleTemplateModel}s
	 */
	@Nonnull
	List<BundleTemplateModel> getAllApprovedRootBundleTemplates(CatalogVersionModel catalogVersion);

	/**
	 * Checks if the given {@link BundleTemplateModel} was used in any cart or order entry
	 *
	 * @param bundleTemplate
	 *           Bundle Template Model
	 * @return true if given bundleTemplate is used in any cart/order entry, otherwise false
	 * @deprecated since 1905 - used only in cockpits which is deprecated
	 */
	@Deprecated(since = "1905", forRemoval = true)
	boolean isBundleTemplateUsed(@Nonnull BundleTemplateModel bundleTemplate);

	/**
	 * Get well-formatted string with the name of the given component.
	 *
	 * @param bundleTemplate
	 *           a bundle component
	 * @return the name of the component of component id (if the name is null)
	 */
	@Nonnull
	String getBundleTemplateName(BundleTemplateModel bundleTemplate);

	/**
	 * Returns {@code EntryGroup} of type {@link de.hybris.platform.core.enums.GroupType#CONFIGURABLEBUNDLE} the given
	 * entry belongs to.
	 *
	 * @param entry
	 *           entry to get group for
	 * @return entry group or null if the entry does not belong to any bundle groups
	 */
	EntryGroup getBundleEntryGroup(@Nonnull AbstractOrderEntryModel entry);

	/**
	 * For given Set of entry group numbers find the one which id is of type {@code CONFIGURABLEBUNDLE} in given order.
	 *
	 * @param order
	 *           Order to get bundle group for
	 * @param entryGroupNumbers
	 *           List of Integers representing numbers of EntryGroups
	 * @return EntryGroup of type CONFIGURABLEBUNDLE in given order, referenced in entryGroupNumbers
	 *
	 * @throws IllegalArgumentException
	 *            when there is more than one group of the given type found
	 */
	EntryGroup getBundleEntryGroup(final AbstractOrderModel order, final Set<Integer> entryGroupNumbers);

	/**
	 * Creates entry group tree reflecting the structure of a bundle template and add the tree to the given order.
	 *
	 * @param bundleTemplate
	 *           any component of a bundle template
	 * @param order
	 *           order to add the bundle to
	 * @return root {@link EntryGroup} of the structure created
	 */
	@Nonnull
	EntryGroup createBundleTree(@Nonnull final BundleTemplateModel bundleTemplate, @Nonnull final AbstractOrderModel order);
}
