/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.searchservices.integration.core.service

import de.hybris.bootstrap.annotations.IntegrationTest
import de.hybris.platform.catalog.CatalogVersionService
import de.hybris.platform.catalog.model.CatalogVersionModel
import de.hybris.platform.core.model.product.ProductModel
import de.hybris.platform.core.model.user.UserModel
import de.hybris.platform.impex.jalo.ImpExException
import de.hybris.platform.product.ProductService
import de.hybris.platform.searchservices.core.service.SnContext
import de.hybris.platform.searchservices.core.service.SnContextFactory
import de.hybris.platform.searchservices.core.service.SnSessionService
import de.hybris.platform.servicelayer.i18n.I18NService
import de.hybris.platform.servicelayer.search.FlexibleSearchService
import de.hybris.platform.servicelayer.search.SearchResult
import de.hybris.platform.servicelayer.session.SessionService
import de.hybris.platform.servicelayer.user.UserService

import java.nio.charset.StandardCharsets

import javax.annotation.Resource

import org.junit.Test


@IntegrationTest
public class SnSessionServiceSpec extends AbstractSnCoreSpec {

	private static final String CATALOG_ID = "hwcatalog"
	private static final String CATALOG_VERSION_STAGED = "Staged"
	private static final String CATALOG_VERSION_ONLINE = "Online"

	private static final String PRODUCT_CODE = "product1"
	private static final String PRODUCT_NAME = "Red shirt"

	private static final String TEST_ATTRIBUTE = "test"
	private static final Object TEST_ATTRIBUTE_VALUE = "value"

	private static final String USER_ANONYMOUS = "anonymous"
	private static final String USER_ADMIN = "admin"

	private static final String PRODUCTS_QUERY = "select {PK} from {Product}"
	private static final int PRODUCTS_COUNT_ONLINE = 3
	private static final int PRODUCTS_COUNT_STAGED = 3

	@Resource
	SessionService sessionService

	@Resource
	UserService userService

	@Resource
	CatalogVersionService catalogVersionService

	@Resource
	I18NService i18nService

	@Resource
	FlexibleSearchService flexibleSearchService

	@Resource
	ProductService productService

	@Resource
	SnContextFactory snContextFactory

	@Resource
	SnSessionService snSessionService

	@Override
	public Object loadData() {
		loadDefaultData()
	}

	@Test
	public void previouslySetSessionAttributesAreVisibleInLocalSession() {
		when:
		sessionService.setAttribute(TEST_ATTRIBUTE, TEST_ATTRIBUTE_VALUE)

		Object value = null
		try {
			snSessionService.initializeSession()
			value = sessionService.getAttribute(TEST_ATTRIBUTE)
		}
		finally {
			snSessionService.destroySession()
		}

		then:
		value == TEST_ATTRIBUTE_VALUE
	}

	@Test
	public void attributesSetInLocalSessionAreVisibleInLocalSession() {
		when:
		Object value = null
		try {
			snSessionService.initializeSession()
			sessionService.setAttribute(TEST_ATTRIBUTE, TEST_ATTRIBUTE_VALUE)
			value = sessionService.getAttribute(TEST_ATTRIBUTE)
		}
		finally {
			snSessionService.destroySession()
		}

		then:
		value == TEST_ATTRIBUTE_VALUE
	}

	@Test
	public void attributesSetInLocalSessionAreNotVisibleAfterLocalSessionIsDestroyed() {
		when:
		try {
			snSessionService.initializeSession()
			sessionService.setAttribute(TEST_ATTRIBUTE, TEST_ATTRIBUTE_VALUE)
		}
		finally {
			snSessionService.destroySession()
		}

		final Object value = sessionService.getAttribute(TEST_ATTRIBUTE)

		then:
		value == null
	}

	@Test
	public void languageFallbackIsEnabledInLocalSession() throws ImpExException {
		given:
		final CatalogVersionModel catalogVersion = catalogVersionService.getCatalogVersion(CATALOG_ID, CATALOG_VERSION_ONLINE)

		when:
		String name = null
		String nameEn = null
		String nameDe = null
		try {
			snSessionService.initializeSession()

			i18nService.setCurrentLocale(LOCALE_DE)

			final ProductModel product = productService.getProductForCode(catalogVersion, PRODUCT_CODE)
			name = product.getName()
			nameEn = product.getName(LOCALE_EN)
			nameDe = product.getName(LOCALE_DE)
		}
		finally {
			snSessionService.destroySession()
		}

		then:
		name == PRODUCT_NAME
		nameEn == PRODUCT_NAME
		nameDe == PRODUCT_NAME
	}

	@Test
	public void restrictionsAreEnabledInLocalSession() throws ImpExException {
		given:
		importData("/searchservices/test/integration/snRestrictions.impex", StandardCharsets.UTF_8.name())

		final CatalogVersionModel onlineCatalogVersion = catalogVersionService.getCatalogVersion(CATALOG_ID,
			CATALOG_VERSION_ONLINE)
		final CatalogVersionModel stagedCatalogVersion = catalogVersionService.getCatalogVersion(CATALOG_ID,
			CATALOG_VERSION_STAGED)

		when:
		SearchResult<ProductModel> searchResult1 = null
		SearchResult<ProductModel> searchResult2 = null

		try {
			snSessionService.initializeSession()

			catalogVersionService.setSessionCatalogVersions(List.of(onlineCatalogVersion, stagedCatalogVersion))
			searchResult1 = flexibleSearchService.search(PRODUCTS_QUERY)

			catalogVersionService.setSessionCatalogVersions(List.of(onlineCatalogVersion))
			searchResult2 = flexibleSearchService.search(PRODUCTS_QUERY)
		}
		finally {
			snSessionService.destroySession()
		}

		then:
		searchResult1.getResult().size() == PRODUCTS_COUNT_ONLINE + PRODUCTS_COUNT_STAGED
		searchResult2.getResult().size() == PRODUCTS_COUNT_ONLINE
	}

	@Test
	public disableRestrictions() throws ImpExException {
		given:
		importData("/searchservices/test/integration/snRestrictions.impex", StandardCharsets.UTF_8.name())

		final CatalogVersionModel onlineCatalogVersion = catalogVersionService.getCatalogVersion(CATALOG_ID,
			CATALOG_VERSION_ONLINE)
		final CatalogVersionModel stagedCatalogVersion = catalogVersionService.getCatalogVersion(CATALOG_ID,
			CATALOG_VERSION_STAGED)

		when:
		SearchResult<ProductModel> searchResult1 = null
		SearchResult<ProductModel> searchResult2 = null

		try {
			snSessionService.initializeSession()
			snSessionService.disableSearchRestrictions()

			catalogVersionService.setSessionCatalogVersions(List.of(onlineCatalogVersion, stagedCatalogVersion))
			searchResult1 = flexibleSearchService.search(PRODUCTS_QUERY)

			catalogVersionService.setSessionCatalogVersions(List.of(onlineCatalogVersion))
			searchResult2 = flexibleSearchService.search(PRODUCTS_QUERY)
		}
		finally {
			snSessionService.destroySession()
		}

		then:
		searchResult1.getResult().size() == PRODUCTS_COUNT_ONLINE + PRODUCTS_COUNT_STAGED
		searchResult2.getResult().size() == PRODUCTS_COUNT_ONLINE + PRODUCTS_COUNT_STAGED
	}

	@Test
	public enableRestrictions() throws ImpExException {
		given:
		importData("/searchservices/test/integration/snRestrictions.impex", StandardCharsets.UTF_8.name())

		final CatalogVersionModel onlineCatalogVersion = catalogVersionService.getCatalogVersion(CATALOG_ID,
			CATALOG_VERSION_ONLINE)
		final CatalogVersionModel stagedCatalogVersion = catalogVersionService.getCatalogVersion(CATALOG_ID,
			CATALOG_VERSION_STAGED)

		when:
		SearchResult<ProductModel> searchResult1 = null
		SearchResult<ProductModel> searchResult2 = null

		try {
			snSessionService.initializeSession()
			snSessionService.disableSearchRestrictions()
			snSessionService.enableSearchRestrictions()

			catalogVersionService.setSessionCatalogVersions(List.of(onlineCatalogVersion, stagedCatalogVersion))
			searchResult1 = flexibleSearchService.search(PRODUCTS_QUERY)

			catalogVersionService.setSessionCatalogVersions(List.of(onlineCatalogVersion))
			searchResult2 = flexibleSearchService.search(PRODUCTS_QUERY)
		}
		finally {
			snSessionService.destroySession()
		}

		then:
		searchResult1.getResult().size() == PRODUCTS_COUNT_ONLINE + PRODUCTS_COUNT_STAGED
		searchResult2.getResult().size() == PRODUCTS_COUNT_ONLINE
	}

	@Test
	public void createSessionFromContextWithoutUser() {
		when:
		UserModel user = null

		try {
			final SnContext context = snContextFactory.createContext(INDEX_TYPE_ID)
			snSessionService.initializeSessionForContext(context)

			user = userService.getCurrentUser()
		}
		finally {
			snSessionService.destroySession()
		}

		then:
		user != null
		user.getUid() == USER_ANONYMOUS
	}

	@Test
	public void createSessionFromContextWithUser() {
		given:
		importData("/searchservices/test/integration/snIndexConfigurationAddAdminUser.impex",
			StandardCharsets.UTF_8.name())

		when:
		UserModel user = null

		try {
			final SnContext context = snContextFactory.createContext(INDEX_TYPE_ID)
			snSessionService.initializeSessionForContext(context)

			user = userService.getCurrentUser()
		}
		finally {
			snSessionService.destroySession()
		}

		then:
		user != null
		user.getUid() == USER_ADMIN
	}

	@Test
	public void createSessionFromContextWithoutCatalogVersions() {
		when:
		Collection<CatalogVersionModel> catalogVersions = null

		try {
			final SnContext context = snContextFactory.createContext(INDEX_TYPE_ID)
			snSessionService.initializeSessionForContext(context)

			catalogVersions = catalogVersionService.getSessionCatalogVersions()
		}
		finally {
			snSessionService.destroySession()
		}

		then:
		catalogVersions != null
		catalogVersions.isEmpty()
	}

	@Test
	public void createSessionFromContextWithCatalogVersions() {
		given:
		importData("/searchservices/test/integration/snIndexTypeAddCatalogVersions.impex",
			StandardCharsets.UTF_8.name())

		final CatalogVersionModel onlineCatalogVersion = catalogVersionService.getCatalogVersion(CATALOG_ID,
			CATALOG_VERSION_ONLINE)
		final CatalogVersionModel stagedCatalogVersion = catalogVersionService.getCatalogVersion(CATALOG_ID,
			CATALOG_VERSION_STAGED)

		when:
		final SnContext context = snContextFactory.createContext(INDEX_TYPE_ID)
		Collection<CatalogVersionModel> catalogVersions = null

		try {
			snSessionService.initializeSessionForContext(context)

			catalogVersions = catalogVersionService.getSessionCatalogVersions()
		}
		finally {
			snSessionService.destroySession()
		}

		then:
		catalogVersions != null
		catalogVersions.containsAll(List.of(onlineCatalogVersion, stagedCatalogVersion))
	}
}
