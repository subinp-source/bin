/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.rendering.impl;


import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.cms2.model.pages.AbstractPageModel;
import de.hybris.platform.cms2.servicelayer.data.CMSDataFactory;
import de.hybris.platform.cms2.servicelayer.data.RestrictionData;
import de.hybris.platform.cms2.servicelayer.services.CMSPageService;
import de.hybris.platform.cmsfacades.common.service.RestrictionAwareService;
import de.hybris.platform.cmsfacades.common.validator.FacadeValidationService;
import de.hybris.platform.cmsfacades.constants.CmsfacadesConstants;
import de.hybris.platform.cmsfacades.data.AbstractPageData;
import de.hybris.platform.cmsfacades.dto.RenderingPageValidationDto;
import de.hybris.platform.cmsfacades.exception.ValidationException;
import de.hybris.platform.cmsfacades.rendering.PageRenderingService;
import de.hybris.platform.cmsfacades.rendering.cache.RenderingCacheService;
import de.hybris.platform.cmsfacades.rendering.suppliers.page.RenderingPageModelSupplier;
import de.hybris.platform.core.servicelayer.data.SearchPageData;
import de.hybris.platform.servicelayer.dto.converter.Converter;

import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.validation.FieldError;
import org.springframework.validation.Validator;


/**
 * Default implementation for {@link PageRenderingService}.
 */
public class DefaultPageRenderingService implements PageRenderingService
{
	private Converter<AbstractPageModel, AbstractPageData> pageModelToDataRenderingConverter;
	private FacadeValidationService facadeValidationService;
	private Validator renderingPageValidator;
	private Validator findAllPagesRenderingPageValidator;
	private RestrictionAwareService restrictionAwareService;
	private CMSDataFactory cmsDataFactory;
	private List<RenderingPageModelSupplier> renderingPageModelSuppliers;
	private CMSPageService cmsPageService;
	private RenderingCacheService<AbstractPageData> renderingCacheService;

	@Override
	public AbstractPageData getPageRenderingData(final String pageTypeCode, final String pageLabelOrId, final String code)
			throws CMSItemNotFoundException
	{
		validateParameters(pageTypeCode, pageLabelOrId, code);

		final String pageQualifier = getPageQualifier(pageLabelOrId, code);
		final RestrictionData restrictionData = getRestrictionData(pageTypeCode, code);
		final AbstractPageModel pageModel = getPageModel(pageTypeCode, pageQualifier);
		return getPageData(pageModel, restrictionData);
	}

	@Override
	public AbstractPageData getPageRenderingData(final String pageId) throws CMSItemNotFoundException
	{
		final RestrictionData restrictionData = getRestrictionData(AbstractPageModel._TYPECODE, null);
		final AbstractPageModel pageModel = getCmsPageService().getPageForId(pageId);
		return getPageData(pageModel, restrictionData);
	}

	@Override
	public SearchPageData<AbstractPageData> findAllRenderingPageData(final String typeCode, final SearchPageData searchPageData)
	{
		validateParametersForFindAllPages(typeCode);
		final String pageType = Objects.nonNull(typeCode) ? typeCode : AbstractPageModel._TYPECODE;

		final RestrictionData restrictionData = getRestrictionData(pageType, null);
		final SearchPageData<AbstractPageModel> pagesResult = getCmsPageService().findAllPages(pageType, searchPageData);

		// convert model to data by applying restriction context (time, user, lang, etc.)
		final List<AbstractPageData> convertedDataList = pagesResult.getResults().stream()
				.map(model -> getPageData(model, restrictionData)).collect(Collectors.toList());

		final SearchPageData<AbstractPageData> convertedResult = new SearchPageData<>();
		convertedResult.setResults(convertedDataList);
		convertedResult.setPagination(pagesResult.getPagination());
		convertedResult.setSorts(pagesResult.getSorts());

		return convertedResult;
	}

	/**
	 * Returns {@link AbstractPageData} based on {@link AbstractPageModel} and {@link RestrictionData}.
	 *
	 * @param pageModel
	 *           the {@link AbstractPageModel}.
	 * @param restrictionData
	 *           the {@link RestrictionData}
	 * @return the {@link AbstractPageData}.
	 */
	protected AbstractPageData getPageData(final AbstractPageModel pageModel, final RestrictionData restrictionData)
	{
		return getRestrictionAwareService().execute(restrictionData, () -> getRenderingCacheService().cacheOrElse(pageModel,
				() -> getPageModelToDataRenderingConverter().convert(pageModel)));
	}

	/**
	 * Returns {@link RestrictionData} based on pageTypeCode and code. Never null.
	 *
	 * @param pageType
	 *           the page type.
	 * @param code
	 *           the code. If the page type is ProductPage then the code should be a product code. If the page type is
	 *           CategoryPage then the code should be a category code. If the page type is CatalogPage then the code
	 *           should be a catalog page.
	 * @return the {@link RestrictionData}.
	 */
	protected RestrictionData getRestrictionData(final String pageType, final String code)
	{
		return getRenderingPageModelSuppliers().stream() //
				.filter(supplier -> supplier.getConstrainedBy().test(pageType)).findFirst()
				.flatMap(supplier -> supplier.getRestrictionData(code)).orElse(getCmsDataFactory().createRestrictionData());
	}

	/**
	 * Validates input parameters.
	 *
	 * @param pageTypeCode
	 *           the page type code.
	 * @param pageLabelOrId
	 *           the page label or id.
	 * @param code
	 *           the code (product code, catalog code or category code)
	 * @throws CMSItemNotFoundException
	 *            when the page label or id is not valid
	 */
	@SuppressWarnings("squid:S1162")
	protected void validateParameters(final String pageTypeCode, final String pageLabelOrId, final String code)
			throws CMSItemNotFoundException
	{
		final RenderingPageValidationDto validationDto = new RenderingPageValidationDto();
		validationDto.setPageTypeCode(pageTypeCode);
		validationDto.setCode(code);
		validationDto.setPageLabelOrId(pageLabelOrId);

		try
		{
			getFacadeValidationService().validate(getRenderingPageValidator(), validationDto);
		}
		catch (final ValidationException e)
		{
			final Predicate<FieldError> isPageNotFoundError = error -> "pageLabelOrId".equals(error.getField())
					&& CmsfacadesConstants.INVALID_PAGE_LABEL_OR_ID.equals(error.getCode());

			if (Objects.nonNull(e.getValidationObject()) //
					&& Objects.nonNull(e.getValidationObject().getFieldErrors()) //
					&& e.getValidationObject().getFieldErrors().stream().anyMatch(isPageNotFoundError))
			{
				throw new CMSItemNotFoundException("No content page found matching the provided label or id: " + pageLabelOrId, e);
			}
			else
			{
				throw e;
			}
		}
	}

	/**
	 * Validates that the provided type code represents a valid page type.
	 *
	 * @param pageTypeCode
	 *           the page type code.
	 */
	protected void validateParametersForFindAllPages(final String pageTypeCode)
	{
		final RenderingPageValidationDto validationDto = new RenderingPageValidationDto();
		validationDto.setPageTypeCode(pageTypeCode);

		getFacadeValidationService().validate(getFindAllPagesRenderingPageValidator(), validationDto);
	}

	/**
	 * Returns the qualifier that is used to extract the page.
	 *
	 * @param pageLabelOrId
	 *           the page label or id
	 * @param code
	 *           the code.
	 * @return the qualifier.
	 * @implSpec This implementation return {@code pageLabelOrId == null ? code : pageLabelOrId}.
	 */
	protected String getPageQualifier(final String pageLabelOrId, final String code)
	{
		return pageLabelOrId == null ? code : pageLabelOrId;
	}

	/**
	 * Returns the {@link AbstractPageModel}.
	 *
	 * @param pageType
	 *           the page type.
	 * @param qualifier
	 *           the qualifier of the page. See {@code getPageQualifier()} for more information.
	 * @return the {@link AbstractPageModel}.
	 * @throws CMSItemNotFoundException
	 *            if the page does not exist.
	 */
	protected AbstractPageModel getPageModel(final String pageType, final String qualifier) throws CMSItemNotFoundException
	{
		return getRenderingPageModelSuppliers().stream() //
				.filter(supplier -> supplier.getConstrainedBy().test(pageType)).findFirst()
				.flatMap(supplier -> supplier.getPageModel(qualifier)).orElseThrow(() -> new CMSItemNotFoundException(
						"No AbstractPageModel found for given page type: " + pageType + " and qualifier " + qualifier));

	}

	protected List<RenderingPageModelSupplier> getRenderingPageModelSuppliers()
	{
		return renderingPageModelSuppliers;
	}

	@Required
	public void setRenderingPageModelSuppliers(final List<RenderingPageModelSupplier> renderingPageModelSuppliers)
	{
		this.renderingPageModelSuppliers = renderingPageModelSuppliers;
	}

	protected Converter<AbstractPageModel, AbstractPageData> getPageModelToDataRenderingConverter()
	{
		return pageModelToDataRenderingConverter;
	}

	@Required
	public void setPageModelToDataRenderingConverter(
			final Converter<AbstractPageModel, AbstractPageData> pageModelToDataRenderingConverter)
	{
		this.pageModelToDataRenderingConverter = pageModelToDataRenderingConverter;
	}

	protected FacadeValidationService getFacadeValidationService()
	{
		return facadeValidationService;
	}

	@Required
	public void setFacadeValidationService(final FacadeValidationService facadeValidationService)
	{
		this.facadeValidationService = facadeValidationService;
	}

	protected Validator getRenderingPageValidator()
	{
		return renderingPageValidator;
	}

	@Required
	public void setRenderingPageValidator(final Validator renderingPageValidator)
	{
		this.renderingPageValidator = renderingPageValidator;
	}

	protected RestrictionAwareService getRestrictionAwareService()
	{
		return restrictionAwareService;
	}

	@Required
	public void setRestrictionAwareService(final RestrictionAwareService restrictionAwareService)
	{
		this.restrictionAwareService = restrictionAwareService;
	}

	protected CMSDataFactory getCmsDataFactory()
	{
		return cmsDataFactory;
	}

	@Required
	public void setCmsDataFactory(final CMSDataFactory cmsDataFactory)
	{
		this.cmsDataFactory = cmsDataFactory;
	}

	protected CMSPageService getCmsPageService()
	{
		return cmsPageService;
	}

	@Required
	public void setCmsPageService(final CMSPageService cmsPageService)
	{
		this.cmsPageService = cmsPageService;
	}

	public RenderingCacheService<AbstractPageData> getRenderingCacheService()
	{
		return renderingCacheService;
	}

	@Required
	public void setRenderingCacheService(final RenderingCacheService<AbstractPageData> renderingCacheService)
	{
		this.renderingCacheService = renderingCacheService;
	}

	protected Validator getFindAllPagesRenderingPageValidator()
	{
		return findAllPagesRenderingPageValidator;
	}

	@Required
	public void setFindAllPagesRenderingPageValidator(final Validator findAllPagesRenderingPageValidator)
	{
		this.findAllPagesRenderingPageValidator = findAllPagesRenderingPageValidator;
	}
}
