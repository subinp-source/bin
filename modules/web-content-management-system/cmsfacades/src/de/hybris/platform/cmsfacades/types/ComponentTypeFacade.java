/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.types;


import de.hybris.platform.cms2.data.PageableData;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.cmsfacades.data.CMSComponentTypesForPageSearchData;
import de.hybris.platform.cmsfacades.data.ComponentTypeData;
import de.hybris.platform.cmsfacades.data.StructureTypeCategory;
import de.hybris.platform.servicelayer.search.SearchResult;

import java.util.List;


/**
 * Facade for getting CMS component type information about available types and their attributes.
 */
public interface ComponentTypeFacade
{
	/**
	 * Find all cms component types. This does not include abstract component types nor action component types.
	 *
	 * @return list of component types; never <tt>null</tt>
	 * @deprecated since 1905, please use {@link ComponentTypeFacade#getAllCmsItemTypes(List, boolean)}
	 */
	@Deprecated(since = "1905", forRemoval = true)
	List<ComponentTypeData> getAllComponentTypes();

	/**
	 * Find all component types by category. This does not include abstract component types nor action component types.
	 *
	 * @param category
	 *           - the category of the component type to retrieve
	 * @return list of types; never <tt>null</tt>
	 * @deprecated since 1905, please use {@link ComponentTypeFacade#getAllCmsItemTypes(List, boolean)}
	 */
	@Deprecated(since = "1905", forRemoval = true)
	List<ComponentTypeData> getAllComponentTypes(final String category);

	/**
	 * Get a single cms component type.
	 *
	 * @param code
	 *           - the type code of the component type to retrieve
	 * @return the cms component type
	 * @throws ComponentTypeNotFoundException
	 *            when the code provided does not match any existing types
	 * @deprecated since 1905, please use
	 *             {@link ComponentTypeFacade#getCmsItemTypeByCodeAndMode(String, String, boolean)}
	 */
	@Deprecated(since = "1905", forRemoval = true)
	ComponentTypeData getComponentTypeByCode(final String code) throws ComponentTypeNotFoundException;


	/**
	 * Get a single component type structure for a given structure type mode.
	 *
	 * @param code
	 *           - the type code of the component type to retrieve
	 * @param mode
	 *           - the mode of the structure type
	 * @return the component type structure or null when the code and mode provided do not match any existing types
	 * @throws ComponentTypeNotFoundException
	 *            when the code provided does not match any existing types
	 * @deprecated since 1905, please use
	 *             {@link ComponentTypeFacade#getCmsItemTypeByCodeAndMode(String, String, boolean)}
	 */
	@Deprecated(since = "1905", forRemoval = true)
	ComponentTypeData getComponentTypeByCodeAndMode(final String code, String mode) throws ComponentTypeNotFoundException;

	/**
	 * Find all cms item types by category. This does not include abstract component types nor action component types.
	 *
	 * @param categories
	 *           - the list of categories for the cms item type to retrieve
	 * @param readOnly
	 *           - make all attributes read only (editable=false)
	 * @return list of types; never <tt>null</tt>. Empty result is returned if the list of categories is empty.
	 */
	List<ComponentTypeData> getAllCmsItemTypes(List<StructureTypeCategory> categories, boolean readOnly);

	/**
	 * Get a single cms item type structure.
	 *
	 * @param code
	 *           - the type code of the cms item type to retrieve
	 * @param mode
	 *           - the optional mode of the structure type, can be null.
	 * @return the cms item type structure or null when the code and mode provided do not match any existing types
	 * @throws ComponentTypeNotFoundException
	 *            when the code provided does not match any existing types
	 */
	ComponentTypeData getCmsItemTypeByCodeAndMode(String code, String mode, boolean readOnly)
			throws ComponentTypeNotFoundException;

	/**
	 * A paged Search for ComponentTypes that are valid to be used in a given page. Optionally filter by name/type code.
	 *
	 * @param searchData
	 * 			- The page and filter data
	 * @param pageableData
	 * 			- The paging information
	 * @return A SearchResult containing the paging information and the results
	 * @throws CMSItemNotFoundException
	 * 			When the expected page is not found.
	 */
	default SearchResult<ComponentTypeData> getAllComponentTypesForPage(CMSComponentTypesForPageSearchData searchData, PageableData pageableData)
			throws CMSItemNotFoundException
	{
		throw new UnsupportedOperationException("ComponentTypeFacade.getAllComponentTypesForPage is not implemented.");
	}
}
