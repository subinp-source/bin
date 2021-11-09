/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmssmarteditwebservices.products.controller;

import static de.hybris.platform.cmsfacades.util.models.CatalogVersionModelMother.CatalogVersion.STAGED;
import static de.hybris.platform.cmsfacades.util.models.ContentCatalogModelMother.CatalogTemplate.ID_APPLE;
import static de.hybris.platform.webservicescommons.testsupport.client.WebservicesAssert.assertResponse;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasKey;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cmsfacades.uniqueidentifier.EncodedItemComposedKey;
import de.hybris.platform.cmsfacades.util.models.CatalogVersionModelMother;
import de.hybris.platform.cmsfacades.util.models.MediaModelMother;
import de.hybris.platform.cmsfacades.util.models.ProductModelMother;
import de.hybris.platform.cmsfacades.util.models.SiteModelMother;
import de.hybris.platform.cmssmarteditwebservices.constants.CmssmarteditwebservicesConstants;
import de.hybris.platform.cmssmarteditwebservices.dto.ProductSearchResultWsDTO;
import de.hybris.platform.cmssmarteditwebservices.dto.ProductWsDTO;
import de.hybris.platform.cmssmarteditwebservices.util.ApiBaseIntegrationTest;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.oauth2.constants.OAuth2Constants;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.webservicescommons.testsupport.server.NeedsEmbeddedServer;

import java.util.Comparator;
import java.util.HashMap;

import javax.annotation.Resource;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.ImmutableMap;


@NeedsEmbeddedServer(webExtensions =
{ CmssmarteditwebservicesConstants.EXTENSIONNAME, OAuth2Constants.EXTENSIONNAME })
@IntegrationTest
public class ProductControllerWebServiceTest extends ApiBaseIntegrationTest
{
	private static final String MASK = "mask";
	private static final String TEXT = "text";
	private static final String PAGE_SIZE = "pageSize";
	private static final String LANG_ISO_CODE = "langIsoCode";
	private static final String EN_ISO_CODE = "en";
	private static final String DE_ISO_CODE = "de";


	private static final String ENDPOINT_SEARCH = "/v1/productcatalogs/{catalogId}/versions/{versionId}/products";
	private static final String ENDPOINT_GET_BY_ID = "/v1/sites/{siteId}/products";

	@Resource
	private ProductModelMother productModelMother;

	@Resource
	private CatalogVersionModelMother catalogVersionModelMother;

	@Resource
	private MediaModelMother mediaModelMother;

	@Resource
	private SiteModelMother siteModelMother;

	@Resource
	private ModelService modelService;

	private MediaModel thumbnailMedia;

	private CatalogVersionModel catalogVersion;

	@Before
	public void setup()
	{
		siteModelMother.createElectronicsWithAppleStagedAndOnlineCatalog();

		catalogVersion = catalogVersionModelMother.createAppleStagedCatalogVersionModel();
		final ProductModel mouseProduct = productModelMother.createMouseProduct(catalogVersion);
		final ProductModel carProduct = productModelMother.createCarProduct(catalogVersion);
		final ProductModel multiLangProduct = productModelMother.createMultiLangProduct(catalogVersion);

		thumbnailMedia = mediaModelMother.createThumbnailMediaModel(catalogVersion);
		mouseProduct.setThumbnail(thumbnailMedia);
		carProduct.setThumbnail(thumbnailMedia);

		modelService.saveAll(mouseProduct, carProduct, multiLangProduct);
	}

	@Test
	public void testGetProductByCode()
	{
		final Response response = getCmsManagerWsSecuredRequestBuilder() //
				.path(replaceUriVariablesWithDefaults(ENDPOINT_GET_BY_ID, new HashMap<>())) //
				.path(buildUid(ProductModelMother.MOUSE)).build().accept(MediaType.APPLICATION_JSON) //
				.get();

		assertResponse(Response.Status.OK, response);

		final ProductWsDTO entity = response.readEntity(ProductWsDTO.class);
		assertOnProductData(entity);
	}


	protected String buildUid(final String uid)
	{
		final EncodedItemComposedKey encodedItemComposedKey = new EncodedItemComposedKey();
		encodedItemComposedKey.setItemId(uid);
		encodedItemComposedKey.setCatalogId(catalogVersion.getCatalog().getId());
		encodedItemComposedKey.setCatalogVersion(catalogVersion.getVersion());
		return encodedItemComposedKey.toEncoded();
	}

	@Test
	public void testFindProductByEnglishText()
	{
		// WHEN
		final Response response = getCmsManagerWsSecuredRequestBuilder() //
				.path(replaceUriVariablesWithDefaults(ENDPOINT_SEARCH, new HashMap<>())) //
				.queryParam(TEXT, ProductModelMother.EN_MULTI_LANG_NAME) //
				.queryParam(PAGE_SIZE, 10) //
				.queryParam(LANG_ISO_CODE, EN_ISO_CODE) //
				.build() //
				.accept(MediaType.APPLICATION_JSON) //
				.get();

		// THEN
		assertResponse(Response.Status.OK, response);
		final ProductSearchResultWsDTO entity = response.readEntity(ProductSearchResultWsDTO.class);
		assertThat(entity.getProducts(), notNullValue());
		assertThat(entity.getProducts(), hasSize(1));
		final ProductWsDTO productWsDTO = entity.getProducts().get(0);
		assertThat(productWsDTO, hasProperty(ProductModel.NAME));
		assertThat(productWsDTO.getName(), hasKey(EN_ISO_CODE));
		assertThat(productWsDTO.getName().get(EN_ISO_CODE), is(ProductModelMother.EN_MULTI_LANG_NAME));
	}

	@Test
	public void testFindProductByGermanText()
	{
		// WHEN
		final Response response = getCmsManagerWsSecuredRequestBuilder() //
				.path(replaceUriVariablesWithDefaults(ENDPOINT_SEARCH, new HashMap<>())) //
				.queryParam(TEXT, ProductModelMother.DE_MULTI_LANG_NAME) //
				.queryParam(PAGE_SIZE, 10) //
				.queryParam(LANG_ISO_CODE, DE_ISO_CODE) //
				.build() //
				.accept(MediaType.APPLICATION_JSON) //
				.get();

		// THEN
		assertResponse(Response.Status.OK, response);
		final ProductSearchResultWsDTO entity = response.readEntity(ProductSearchResultWsDTO.class);
		assertThat(entity.getProducts(), notNullValue());
		assertThat(entity.getProducts(), hasSize(1));
		final ProductWsDTO productWsDTO = entity.getProducts().get(0);
		assertThat(productWsDTO, hasProperty(ProductModel.NAME));
		assertThat(productWsDTO.getName(), hasKey(DE_ISO_CODE));
		assertThat(productWsDTO.getName().get(DE_ISO_CODE), is(ProductModelMother.DE_MULTI_LANG_NAME));
	}

	@Test
	public void testNotFindEnglishProductWithGermanSearchText()
	{
		// WHEN
		final Response response = getCmsManagerWsSecuredRequestBuilder() //
				.path(replaceUriVariablesWithDefaults(ENDPOINT_SEARCH, new HashMap<>())) //
				.queryParam(TEXT, ProductModelMother.DE_MULTI_LANG_NAME) //
				.queryParam(PAGE_SIZE, 10) //
				.queryParam(LANG_ISO_CODE, EN_ISO_CODE) //
				.build() //
				.accept(MediaType.APPLICATION_JSON) //
				.get();

		// THEN
		assertResponse(Response.Status.OK, response);
		final ProductSearchResultWsDTO entity = response.readEntity(ProductSearchResultWsDTO.class);
		assertThat(entity.getPagination().getCount(), equalTo(0));
	}

	@Test
	public void testFindProductByText()
	{
		final Response response = getCmsManagerWsSecuredRequestBuilder() //
				.path(replaceUriVariablesWithDefaults(ENDPOINT_SEARCH, new HashMap<>())) //
				.queryParam(TEXT, ProductModelMother.MOUSE) //
				.queryParam(PAGE_SIZE, 10) //
				.build() //
				.accept(MediaType.APPLICATION_JSON) //
				.get();

		assertResponse(Response.Status.OK, response);

		final ProductSearchResultWsDTO entity = response.readEntity(ProductSearchResultWsDTO.class);
		assertThat(entity.getProducts(), notNullValue());
		assertThat(entity.getProducts(), hasSize(1));
		assertOnProductData(entity.getProducts().get(0));
	}

	@Test
	public void testFindProductByMask()
	{
		final Response response = getCmsManagerWsSecuredRequestBuilder() //
				.path(replaceUriVariablesWithDefaults(ENDPOINT_SEARCH, new HashMap<>())) //
				.queryParam(MASK, ProductModelMother.MOUSE) //
				.queryParam(PAGE_SIZE, 10) //
				.build() //
				.accept(MediaType.APPLICATION_JSON) //
				.get();

		assertResponse(Response.Status.OK, response);

		final ProductSearchResultWsDTO entity = response.readEntity(ProductSearchResultWsDTO.class);
		assertThat(entity.getProducts(), notNullValue());
		assertThat(entity.getProducts(), hasSize(1));
		assertOnProductData(entity.getProducts().get(0));
	}

	@Test
	public void testFindProductByTextAndMask_shouldReturnOneMouseProduct()
	{
		final Response response = getCmsManagerWsSecuredRequestBuilder() //
				.path(replaceUriVariablesWithDefaults(ENDPOINT_SEARCH, new HashMap<>())) //
				.queryParam(TEXT, ProductModelMother.MOUSE) //
				.queryParam(MASK, ProductModelMother.CAR) //
				.queryParam(PAGE_SIZE, 10) //
				.build() //
				.accept(MediaType.APPLICATION_JSON) //
				.get();

		assertResponse(Response.Status.OK, response);

		final ProductSearchResultWsDTO entity = response.readEntity(ProductSearchResultWsDTO.class);
		assertThat(entity.getProducts(), notNullValue());
		assertThat(entity.getProducts().size(), is(1));
		assertOnProductData(entity.getProducts().get(0));
	}

	@Test
	public void testFindProductByTextOrMaskWhenTextAndMaskAreNull_shouldReturnAllProducts()
	{
		final Response response = getCmsManagerWsSecuredRequestBuilder() //
				.path(replaceUriVariablesWithDefaults(ENDPOINT_SEARCH, new HashMap<>())) //
				.queryParam(PAGE_SIZE, 10) //
				.build() //
				.accept(MediaType.APPLICATION_JSON) //
				.get();

		assertResponse(Response.Status.OK, response);

		final ProductSearchResultWsDTO entity = response.readEntity(ProductSearchResultWsDTO.class);
		assertThat(entity.getProducts(), notNullValue());
		assertThat(entity.getProducts(), hasSize(3));

		entity.getProducts().sort(getProductWsDTOComparator());
		assertOnProductData(entity.getProducts().get(1));
	}

	protected Comparator<ProductWsDTO> getProductWsDTOComparator()
	{
		return new Comparator<ProductWsDTO>()
		{
			@Override
			public int compare(final ProductWsDTO product1, final ProductWsDTO product2)
			{
				return product1.getUid().compareTo(product2.getUid());
			}
		};
	}

	protected void assertOnProductData(final ProductWsDTO productWsDTO)
	{
		assertThat(productWsDTO,
				allOf(hasProperty("name", is(ImmutableMap.<String, String> builder().put("en", ProductModelMother.MOUSE).build())),
						hasProperty("code", is(ProductModelMother.MOUSE)), //
						hasProperty("catalogId", is(ID_APPLE.name())), //
						hasProperty("catalogVersion", is(STAGED.getVersion()))));
		assertThat(productWsDTO.getThumbnail(), notNullValue());
		assertThat(productWsDTO.getThumbnail().getCode(), is(thumbnailMedia.getCode()));
	}

}
