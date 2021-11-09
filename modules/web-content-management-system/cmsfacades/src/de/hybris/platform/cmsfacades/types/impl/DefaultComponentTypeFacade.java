/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.types.impl;

import static de.hybris.platform.cmsfacades.constants.CmsfacadesConstants.TYPE_READONLY_MODE;
import static de.hybris.platform.cmsfacades.constants.CmsfacadesConstants.TYPE_REQUIRED_FIELDS;
import static java.util.stream.Collectors.toList;

import de.hybris.platform.cms2.common.service.SessionSearchRestrictionsDisabler;
import de.hybris.platform.cms2.data.PageableData;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.cms2.exceptions.TypePermissionException;
import de.hybris.platform.cms2.model.pages.AbstractPageModel;
import de.hybris.platform.cms2.servicelayer.services.admin.CMSAdminPageService;
import de.hybris.platform.cms2.servicelayer.services.admin.CMSAdminSiteService;
import de.hybris.platform.cms2.servicelayer.services.admin.CMSAdminTypeRestrictionsService;
import de.hybris.platform.cmsfacades.common.validator.FacadeValidationService;
import de.hybris.platform.cmsfacades.data.CMSComponentTypesForPageSearchData;
import de.hybris.platform.cmsfacades.data.ComponentTypeData;
import de.hybris.platform.cmsfacades.data.StructureTypeCategory;
import de.hybris.platform.cmsfacades.data.StructureTypeMode;
import de.hybris.platform.cmsfacades.types.ComponentTypeFacade;
import de.hybris.platform.cmsfacades.types.ComponentTypeNotFoundException;
import de.hybris.platform.cmsfacades.types.service.ComponentTypeStructure;
import de.hybris.platform.cmsfacades.types.service.ComponentTypeStructureService;
import de.hybris.platform.cmsfacades.types.service.StructureTypeModeService;
import de.hybris.platform.cmsfacades.types.validator.ComponentTypeForPageSearchValidator;
import de.hybris.platform.core.model.type.TypeModel;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.servicelayer.search.impl.SearchResultImpl;
import de.hybris.platform.servicelayer.type.TypeService;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.assertj.core.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;


/**
 * This implementation of the {@link ComponentTypeFacade} will get the
 * {@link de.hybris.platform.core.model.type.ComposedTypeModel} items and convert them to DTOs.
 * <p>
 * The types available are determined by using the {@link ComponentTypeStructureService} to get all registered component
 * types.
 * </p>
 */
public class DefaultComponentTypeFacade implements ComponentTypeFacade
{
	private static final Logger LOG = LoggerFactory.getLogger(DefaultComponentTypeFacade.class);

	private ComponentTypeStructureService componentTypeStructureService;
	private StructureTypeModeService structureTypeModeService;
	private Converter<ComponentTypeStructure, ComponentTypeData> componentTypeStructureConverter;
	private TypeService typeService;
	private CMSAdminSiteService cmsAdminSiteService;
	private CMSAdminPageService cmsAdminPageService;
	private CMSAdminTypeRestrictionsService cmsAdminTypeRestrictionsService;
	private SessionSearchRestrictionsDisabler sessionSearchRestrictionsDisabler;
	private FacadeValidationService facadeValidationService;
	private ComponentTypeForPageSearchValidator cmsComponentTypeForPageSearchValidator;

	/**
	 * {@inheritDoc}
	 *
	 * @deprecated
	 */
	@Override
	@Deprecated(since = "1905", forRemoval = true)
	public List<ComponentTypeData> getAllComponentTypes()
	{
		return getAllCmsItemTypes(Arrays.asList(StructureTypeCategory.values()), false);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @deprecated
	 */
	@Override
	@Deprecated(since = "1905", forRemoval = true)
	public List<ComponentTypeData> getAllComponentTypes(final String category)
	{
		return getAllCmsItemTypes(Collections.singletonList(StructureTypeCategory.valueOf(category)), false);
	}

	@Override
	public List<ComponentTypeData> getAllCmsItemTypes(final List<StructureTypeCategory> categories, final boolean readOnly)
	{
		final Predicate<ComponentTypeStructure> nonNullTypeCode = (structure) -> Objects.nonNull(structure.getTypecode());

		final Predicate<ComponentTypeData> includeCategory = (structure) -> categories //
				.stream() //
				.anyMatch(category -> category.name().equalsIgnoreCase(structure.getCategory()));

		setStructureTypeContext(readOnly);

		return getComponentTypeStructureService().getComponentTypeStructures() //
				.stream() //
				.filter(nonNullTypeCode) //
				.map(this::convertComponentStructure) //
				.filter(Optional::isPresent) //
				.map(Optional::get).filter(includeCategory).collect(toList());
	}

	@Override
	public SearchResult<ComponentTypeData> getAllComponentTypesForPage(final CMSComponentTypesForPageSearchData searchData, final PageableData pageableData)
			throws CMSItemNotFoundException
	{
		getFacadeValidationService().validate(getCmsComponentTypeForPageSearchValidator(), searchData);

		final Set<String> typeRestrictionsForPage = getTypeRestrictionsForPage(searchData.getPageId());
		final List<ComponentTypeData> matchingTypes = getAllComponentTypes(searchData.getRequiredFields(), searchData.isReadOnly()).stream()
					.filter(type -> this.isTypeMatchingCriteria(type, typeRestrictionsForPage, searchData.getMask()))
					.collect(toList());

		final int requestedStart = pageableData.getCurrentPage() * pageableData.getPageSize();
		final List<ComponentTypeData> pagedTypes = matchingTypes.stream()
				.skip(requestedStart)
				.limit(pageableData.getPageSize())
				.collect(toList());

		return new SearchResultImpl<>(pagedTypes, matchingTypes.size(), pageableData.getPageSize(), requestedStart);
	}

	/**
	 * This method retrieves the codes of the component types applicable (type restrictions) on the given page.
	 *
	 * @param pageId
	 * 			The id of the page whose type restrictions to retrieve.
	 * @return
	 * 			A set including the code of the component types allowed in the given page.
	 * @throws CMSItemNotFoundException when the page for the given pageId is not found.
	 */
	protected Set<String> getTypeRestrictionsForPage(final String pageId) throws CMSItemNotFoundException
	{
		try
		{
			return getSessionSearchRestrictionsDisabler().execute(() -> {
				final AbstractPageModel page = getCmsAdminPageService().getPageForIdFromActiveCatalogVersion(pageId);
				return cmsAdminTypeRestrictionsService.getTypeRestrictionsForPage(page).stream()
						.map(TypeModel::getCode)
						.collect(Collectors.toSet());
			});
		}
		catch (final UnknownIdentifierException ex)
		{
			throw new CMSItemNotFoundException("Cannot find page with id " + pageId);
		}
	}

	/**
	 * Gets all component types. It uses the requiredFields to see if there are some fields that are not needed and avoid wasting
	 * time calculating things that are not needed.
	 *
	 * @param requiredFields
	 *			The list of fields that must be returned.
	 * @param isReadOnly
	 * 			Whether attributes must be returned in readOnly mode or not. Is only important if the attributes field is returned.
	 * @return the list of {@link ComponentTypeData} retrieved.
	 */
	protected List<ComponentTypeData> getAllComponentTypes(final List<String> requiredFields, final boolean isReadOnly)
	{
		// Sets the required fields in the context. The converter uses this information to determine which fields need to be processed.
		// This helps improve performance.
		setStructureTypeContext(TYPE_REQUIRED_FIELDS, requiredFields);

		final List<StructureTypeCategory> categories = Collections.singletonList(StructureTypeCategory.COMPONENT);
		return getAllCmsItemTypes(categories, isReadOnly);
	}

	/**
	 * This method is used to determine if a given ComponentTypeData is valid based on the given criteria. It checks that the code of the type is
	 * included in the given typeRestrictions and that the mask matches either the name or the code.
	 *
	 * Note: If the mask is empty, this method will skip the name and code check.
	 *
	 * @param componentTypeData
	 * 			The object that contains the information about the type to check.
	 * @param typeRestrictionsForPage
	 * 			The set of type restrictions that are valid for the page.
	 * @param mask
	 * 			The mask applied when searching. Is applied to name and code
	 * @return <tt>TRUE</tt> if the type is valid for the page, <tt>FALSE</tt> otherwise.
	 */
	protected boolean isTypeMatchingCriteria(final ComponentTypeData componentTypeData, final Set<String> typeRestrictionsForPage, final String mask)
	{
		if (typeRestrictionsForPage.contains(componentTypeData.getCode()))
		{
			final String regex = ".*" + getValueOrDefault(mask).toLowerCase() + ".*";
			final String code = getValueOrDefault(componentTypeData.getCode()).toLowerCase();
			final String name = getValueOrDefault(componentTypeData.getName()).toLowerCase();

			return Strings.isNullOrEmpty(mask) || code.toLowerCase().matches(regex) || name.toLowerCase().matches(regex);
		}

		return false;
	}

	/**
	 * Utility function. Checks if the given value is null. If it is, the function returns an empty string. Otherwise, it
	 * returns the given value.
	 *
	 * @param value the value to check
	 * @return An empty string if the provided value is null, otherwise the original string.
	 */
	protected String getValueOrDefault(final String value)
	{
		return (Strings.isNullOrEmpty(value)) ? "" : value;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @deprecated
	 */
	@Override
	@Deprecated(since = "1905", forRemoval = true)
	public ComponentTypeData getComponentTypeByCode(final String code) throws ComponentTypeNotFoundException
	{
		return getCmsItemTypeByCodeAndMode(code, null, false);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @deprecated
	 */
	@Override
	@Deprecated(since = "1905", forRemoval = true)
	public ComponentTypeData getComponentTypeByCodeAndMode(final String code, final String mode)
			throws ComponentTypeNotFoundException
	{
		return getCmsItemTypeByCodeAndMode(code, mode, false);
	}

	@Override
	public ComponentTypeData getCmsItemTypeByCodeAndMode(final String code, final String mode, final boolean readOnly)
			throws ComponentTypeNotFoundException
	{
		setStructureTypeContext(readOnly);
		final ComponentTypeStructure componentType = Objects.isNull(mode) ? getComponentTypeStructureByCode(code)
				: getStructureTypeModeService().getComponentTypeByCodeAndMode(code, StructureTypeMode.valueOf(mode));
		if (componentType == null)
		{
			throw new ComponentTypeNotFoundException("Component Type [" + code + "] is not supported.");
		}
		return getComponentTypeStructureConverter().convert(componentType);
	}

	/**
	 * Sets the type structure context. The context contains information about whether all attributes for a given type
	 * should be editable or not. In read-only mode, all attributes will have their <tt>editable</tt> property set to
	 * <tt>FALSE</tt>.
	 *
	 * @param readOnly
	 *           defines whether all attributes will have their <tt>editable</tt> property set to <tt>FALSE</tt>.
	 */
	protected void setStructureTypeContext(final boolean readOnly)
	{
		setStructureTypeContext(TYPE_READONLY_MODE, readOnly);
	}

	/**
	 * Adds a new key and value pair to the type structure context.
	 *
	 * @param key
	 * 			The key of the item to add into the structure context.
	 * @param value
	 * 			The value of the item to add into the structure context.
	 */
	protected void setStructureTypeContext(final String key, final Object value)
	{
		final Map<String, Object> originalContext = getCmsAdminSiteService().getTypeContext();
		Map<String, Object> newContext = (Objects.isNull(originalContext)) ?
				new HashMap<>() : new HashMap<>(originalContext);

		newContext.put(key, value);
		getCmsAdminSiteService().setTypeContext(newContext);
	}

	/**
	 * Returns a {@link ComponentTypeStructure} by its code.
	 *
	 * @param code
	 *           - the code of the cms item type.
	 * @return the {@link ComponentTypeStructure}
	 * @throws ComponentTypeNotFoundException
	 *            if cms item type can not be found by its code.
	 */
	protected ComponentTypeStructure getComponentTypeStructureByCode(final String code) throws ComponentTypeNotFoundException
	{
		final ComponentTypeStructure componentType = getComponentTypeStructureService().getComponentTypeStructure(code);
		if (componentType == null)
		{
			throw new ComponentTypeNotFoundException("Component Type [" + code + "] is not supported.");
		}

		return componentType;
	}

	/**
	 * Converts {@link ComponentTypeStructure} to {@link ComponentTypeData}. Returns <tt>null</tt> in case of
	 * {@link TypePermissionException}.
	 *
	 * @param structure
	 *           the {@link ComponentTypeStructure}
	 * @return the {@link ComponentTypeData}
	 */
	protected Optional<ComponentTypeData> convertComponentStructure(final ComponentTypeStructure structure)
	{
		try
		{
			return Optional.of(getComponentTypeStructureConverter().convert(structure));
		}
		catch (final TypePermissionException e)
		{
			if (LOG.isDebugEnabled())
			{
				LOG.debug("Insufficient type permission", e);
			}
			return Optional.empty();
		}
	}

	protected ComponentTypeStructureService getComponentTypeStructureService()
	{
		return componentTypeStructureService;
	}

	@Required
	public void setComponentTypeStructureService(final ComponentTypeStructureService componentTypeStructureService)
	{
		this.componentTypeStructureService = componentTypeStructureService;
	}

	protected Converter<ComponentTypeStructure, ComponentTypeData> getComponentTypeStructureConverter()
	{
		return componentTypeStructureConverter;
	}

	@Required
	public void setComponentTypeStructureConverter(
			final Converter<ComponentTypeStructure, ComponentTypeData> componentTypeStructureConverter)
	{
		this.componentTypeStructureConverter = componentTypeStructureConverter;
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

	protected StructureTypeModeService getStructureTypeModeService()
	{
		return structureTypeModeService;
	}

	@Required
	public void setStructureTypeModeService(final StructureTypeModeService structureTypeModeService)
	{
		this.structureTypeModeService = structureTypeModeService;
	}

	protected CMSAdminSiteService getCmsAdminSiteService()
	{
		return cmsAdminSiteService;
	}

	@Required
	public void setCmsAdminSiteService(final CMSAdminSiteService cmsAdminSiteService)
	{
		this.cmsAdminSiteService = cmsAdminSiteService;
	}

	public CMSAdminTypeRestrictionsService getCmsAdminTypeRestrictionsService()
	{
		return cmsAdminTypeRestrictionsService;
	}

	@Required
	public void setCmsAdminTypeRestrictionsService(final CMSAdminTypeRestrictionsService cmsAdminTypeRestrictionsService)
	{
		this.cmsAdminTypeRestrictionsService = cmsAdminTypeRestrictionsService;
	}

	public CMSAdminPageService getCmsAdminPageService()
	{
		return cmsAdminPageService;
	}

	@Required
	public void setCmsAdminPageService(final CMSAdminPageService cmsAdminPageService)
	{
		this.cmsAdminPageService = cmsAdminPageService;
	}

	public SessionSearchRestrictionsDisabler getSessionSearchRestrictionsDisabler()
	{
		return sessionSearchRestrictionsDisabler;
	}

	@Required
	public void setSessionSearchRestrictionsDisabler(final SessionSearchRestrictionsDisabler sessionSearchRestrictionsDisabler)
	{
		this.sessionSearchRestrictionsDisabler = sessionSearchRestrictionsDisabler;
	}

	public FacadeValidationService getFacadeValidationService()
	{
		return facadeValidationService;
	}

	@Required
	public void setFacadeValidationService(final FacadeValidationService facadeValidationService)
	{
		this.facadeValidationService = facadeValidationService;
	}

	public ComponentTypeForPageSearchValidator getCmsComponentTypeForPageSearchValidator()
	{
		return cmsComponentTypeForPageSearchValidator;
	}

	@Required
	public void setCmsComponentTypeForPageSearchValidator(final ComponentTypeForPageSearchValidator cmsComponentTypeForPageSearchValidator)
	{
		this.cmsComponentTypeForPageSearchValidator = cmsComponentTypeForPageSearchValidator;
	}
}
