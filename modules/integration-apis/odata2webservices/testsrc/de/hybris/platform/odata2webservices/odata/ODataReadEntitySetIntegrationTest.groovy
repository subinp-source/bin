/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.odata2webservices.odata

import com.google.common.collect.ImmutableMap
import com.jayway.jsonpath.Criteria
import com.jayway.jsonpath.Filter
import de.hybris.bootstrap.annotations.IntegrationTest
import de.hybris.platform.catalog.model.CatalogModel
import de.hybris.platform.catalog.model.CatalogVersionModel
import de.hybris.platform.core.model.product.ProductModel
import de.hybris.platform.impex.jalo.ImpExException
import de.hybris.platform.integrationservices.model.IntegrationObjectModel
import de.hybris.platform.integrationservices.util.IntegrationTestUtil
import de.hybris.platform.odata2services.odata.ODataContextGenerator
import de.hybris.platform.odata2services.odata.asserts.ODataAssertions
import de.hybris.platform.odata2webservices.odata.builders.ODataRequestBuilder
import de.hybris.platform.odata2webservices.odata.builders.PathInfoBuilder
import de.hybris.platform.servicelayer.ServicelayerSpockSpecification
import de.hybris.platform.servicelayer.config.ConfigurationService
import de.hybris.platform.servicelayer.search.FlexibleSearchService
import de.hybris.platform.servicelayer.search.SearchResult
import org.apache.olingo.odata2.api.commons.HttpStatusCodes
import org.apache.olingo.odata2.api.processor.ODataContext
import org.apache.olingo.odata2.api.processor.ODataRequest
import org.apache.olingo.odata2.api.processor.ODataResponse
import org.junit.Test

import javax.annotation.Resource
import java.util.stream.Collectors

import static de.hybris.platform.odata2webservices.odata.ODataFacadeTestUtils.PRODUCTS_ENTITYSET
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE

@IntegrationTest
class ODataReadEntitySetIntegrationTest extends ServicelayerSpockSpecification {
    private static final String IO = "GetIntegrationTestIO"
    private static final String COUNT_PATH = "d.__count"
    private static final String NEXT_PAGE_LINK_PATH = "d.__next"
    private static final String PRODUCTS_ENTITY_SET_SIZE = "4"
    private static final String INLINECOUNT = '$inlinecount'
    private static final String TOP = '$top'
    private static final String SKIP = '$skip'
    private static final String INTEGRATIONKEY = "integrationKey"
    private static final String CODE = "code"
    private static final String SKIPTOKEN = '$skiptoken'
    private static final String COUNT = '$count'
    private static final int MAX_PAGE_SIZE = 3
    private static final int DEFAULT_PAGE_SIZE = 1
    private static final String ALL_RESULTS_PATH = "\$['d']['results'][*]"
    private static final String SOME_RESULTS_PATH = "\$['d']['results'][?]"
    private static final String ALLPAGES = "allpages"
    private static final String CATALOG = "Default"
    private static final String VERSION = "Staged"

    @Resource
    private FlexibleSearchService flexibleSearchService
    @Resource
    private ODataContextGenerator oDataContextGenerator
    @Resource(name = "defaultODataFacade")
    private ODataFacade facade
    @Resource(name = "defaultConfigurationService")
    private ConfigurationService configurationService
    
    def setupSpec() throws Exception {
        IntegrationTestUtil.importImpEx(
                '$io = integrationObject(code)',
                '$item = integrationObjectItem(integrationObject(code), code)',
                '$descriptor = attributeDescriptor(enclosingType(code), qualifier)',
                '$attributeType = returnIntegrationObjectItem(integrationObject(code), code)',
                "INSERT_UPDATE IntegrationObject; code[unique = true]",
                "                               ; $IO",
                'INSERT_UPDATE IntegrationObjectItem; $io[unique = true]; code[unique = true]; type(code)    ; root[default = false]',
                "                                   ; $IO               ; Catalog            ; Catalog       ;",
                "                                   ; $IO               ; Product            ; Product       ; true",
                "                                   ; $IO               ; CatalogVersion     ; CatalogVersion;",
                'INSERT_UPDATE IntegrationObjectItemAttribute; $item[unique = true]; attributeName[unique = true]; $descriptor   ; $attributeType        ; unique[default = false]',
                "                                            ; $IO:Product                                       ; code          ; Product:code          ;",
                "                                            ; $IO:Product                                       ; catalogVersion; Product:catalogVersion; $IO:CatalogVersion",
                "                                            ; $IO:Catalog                                       ; id            ; Catalog:id            ;",
                "                                            ; $IO:CatalogVersion                                ; catalog       ; CatalogVersion:catalog; $IO:Catalog",
                "                                            ; $IO:CatalogVersion                                ; version       ; CatalogVersion:version;"
        )
    }

    def cleanup() {
        IntegrationTestUtil.removeAll ProductModel
    }

    def cleanupSpec() {
        IntegrationTestUtil.removeAll IntegrationObjectModel
        IntegrationTestUtil.removeSafely CatalogModel, { it.id == CATALOG }
        IntegrationTestUtil.removeSafely CatalogVersionModel, { it.version == VERSION }
    }

    def setMaxPageSize() {
        configurationService.getConfiguration().setProperty("odata2services.page.size.max", MAX_PAGE_SIZE.toString())
    }

    def setDefaultPageSize() {
        configurationService.getConfiguration().setProperty("odata2services.page.size.default", DEFAULT_PAGE_SIZE.toString())
    }

    @Test
    def "get all Packages when no Packages EntitySet has been defined"() {
        when:
        final ODataResponse oDataResponse = getWithEntitySetAndQueryParams("Packages", new HashMap<>())

        then:
        ODataAssertions.assertThat(oDataResponse)
                .hasStatus(HttpStatusCodes.NOT_FOUND)
                .jsonBody()
                .hasPathWithValue("\$.error.message.value", "Could not find an entity set or function import for 'Packages'.")
    }

    @Test
    def "get all Products when no Products exist"() throws ImpExException {
        when:
        final ODataResponse oDataResponse = getWithEntitySetAndQueryParams("Products", new HashMap<>())

        then:
        ODataAssertions.assertThat(oDataResponse)
                .hasStatus(HttpStatusCodes.OK)
                .jsonBody()
                .pathHasSize(ALL_RESULTS_PATH, 0)
    }

    @Test
    def "get all products with no url query parameters returns default page size of products"() {
        given:
        setDefaultPageSize()
        givenProductsExist()

        when:
        final ODataResponse oDataResponse = getProductsWithQueryParams(new HashMap<>())

        then:
        ODataAssertions.assertThat(oDataResponse)
                .hasStatus(HttpStatusCodes.OK)
                .jsonBody()
                .pathHasSize(ALL_RESULTS_PATH, DEFAULT_PAGE_SIZE)
                .doesNotHavePath(COUNT_PATH)
                .hasPathWithValueContaining(NEXT_PAGE_LINK_PATH, SKIPTOKEN + "=" + DEFAULT_PAGE_SIZE)
    }

    @Test
    def "get all products with \$top returns the top number of products and next link"() {
        given:
        givenProductsExist()
        final int requestedTop = 1

        when:
        final ODataResponse oDataResponse = getProductsWithQueryParams(ImmutableMap.of(TOP, Integer.toString(requestedTop)))

        then:
        final List<String> products = getProductNamesSubList(0, requestedTop)
        final Filter product1Filter = Filter.filter(Criteria.where(CODE).eq(products.get(0)))

        ODataAssertions.assertThat(oDataResponse)
                .hasStatus(HttpStatusCodes.OK)
                .jsonBody()
                .pathContainsMatchingElement(SOME_RESULTS_PATH, product1Filter)
                .pathHasSize(ALL_RESULTS_PATH, requestedTop)
                .doesNotHavePath(COUNT_PATH)
                .hasPathWithValueContaining(NEXT_PAGE_LINK_PATH, SKIPTOKEN + "=" + requestedTop)
    }

    @Test
    def "get all products with \$top exceeding max page size returns max page size of items"() {
        given:
        setMaxPageSize()
        givenProductsExist()

        when:
        final ODataResponse oDataResponse = getProductsWithQueryParams(ImmutableMap.of(TOP, (MAX_PAGE_SIZE + 1).toString()))

        then:
        ODataAssertions.assertThat(oDataResponse)
                .hasStatus(HttpStatusCodes.OK)
                .jsonBody()
                .pathHasSize(ALL_RESULTS_PATH, MAX_PAGE_SIZE)
                .doesNotHavePath(COUNT_PATH)
                .hasPathWithValueContaining(NEXT_PAGE_LINK_PATH, SKIPTOKEN + "=" + MAX_PAGE_SIZE)
    }

    @Test
    def "get all products with \$inlinecount=none and \$top=2 returns the first 2 products and nextlink"() {
        given:
        givenProductsExist(['prod1', 'prod2', 'prod3'])
        final int requestedTop = 2
        final ODataRequestBuilder oDataRequest = requestBuilder(PRODUCTS_ENTITYSET)
                .withParameter(INLINECOUNT, "none")
                .withParameter(TOP, requestedTop)

        when:
        final ODataResponse oDataResponse = handleRequest(oDataRequest)

        then:
        final List<String> products = getProductNamesSubList(0, requestedTop)
        final Filter product1Filter = Filter.filter(Criteria.where(CODE).eq(products.get(0)))
        final Filter product2Filter = Filter.filter(Criteria.where(CODE).eq(products.get(1)))

        ODataAssertions.assertThat(oDataResponse)
                .hasStatus(HttpStatusCodes.OK)
                .jsonBody()
                .pathContainsMatchingElement(SOME_RESULTS_PATH, product1Filter)
                .pathContainsMatchingElement(SOME_RESULTS_PATH, product2Filter)
                .pathHasSize(ALL_RESULTS_PATH, requestedTop)
                .doesNotHavePath(COUNT_PATH)
                .hasPathWithValueContaining(NEXT_PAGE_LINK_PATH, SKIPTOKEN + "=" + requestedTop)
    }

    @Test
    def "get all products with \$inlinecount=allpages and \$top=1 returns the first product, count and nextlink"() {
        given:
        givenProductsExist()
        final int requestedTop = 1
        final ODataRequestBuilder oDataRequest = requestBuilder(PRODUCTS_ENTITYSET)
                .withParameter(INLINECOUNT, ALLPAGES)
                .withParameter(TOP, requestedTop)

        when:
        final ODataResponse oDataResponse = handleRequest(oDataRequest)

        then:
        final List<String> products = getProductNamesSubList(0, requestedTop)

        ODataAssertions.assertThat(oDataResponse)
                .hasStatus(HttpStatusCodes.OK)
                .jsonBody()
                .pathContainsMatchingElement(SOME_RESULTS_PATH, Filter.filter(Criteria.where(CODE).eq(products.get(0))))
                .pathHasSize(ALL_RESULTS_PATH, requestedTop)
                .hasPathWithValue(COUNT_PATH, PRODUCTS_ENTITY_SET_SIZE)
                .hasPathWithValueContaining(NEXT_PAGE_LINK_PATH, SKIPTOKEN + "=" + requestedTop)
    }

    @Test
    def "get all products with \$top=2 and \$skip=1 returns the 2nd through 3rd product, count and nextlink"() {
        given:
        givenProductsExist()
        final int requestedTop = 2
        final int requestedSkip = 1
        final ODataRequestBuilder oDataRequest = requestBuilder(PRODUCTS_ENTITYSET)
                .withParameter(SKIP, requestedSkip)
                .withParameter(TOP, requestedTop)

        when:
        final ODataResponse oDataResponse = handleRequest(oDataRequest)

        then:
        final List<String> products = getProductNamesSubList(requestedSkip, requestedSkip + requestedTop)
        final Filter product1Filter = Filter.filter(Criteria.where(CODE).eq(products.get(0)))
        final Filter product2Filter = Filter.filter(Criteria.where(CODE).eq(products.get(1)))

        ODataAssertions.assertThat(oDataResponse)
                .hasStatus(HttpStatusCodes.OK)
                .jsonBody()
                .pathHasSize(ALL_RESULTS_PATH, requestedTop)
                .pathContainsMatchingElement(SOME_RESULTS_PATH, product1Filter)
                .pathContainsMatchingElement(SOME_RESULTS_PATH, product2Filter)
                .doesNotHavePath(COUNT_PATH)
                .hasPathWithValueContaining(NEXT_PAGE_LINK_PATH, SKIPTOKEN + "=" + (requestedTop + requestedSkip))
    }

    @Test
    def "get all products with \$top=1 and \$skip=2 returns the 3rd product and nextlink"() {
        given:
        givenProductsExist()
        final int requestedTop = 1
        final int requestedSkip = 2
        final ODataRequestBuilder oDataRequest = requestBuilder(PRODUCTS_ENTITYSET)
                .withParameter(SKIP, requestedSkip)
                .withParameter(TOP, requestedTop)

        when:
        final ODataResponse oDataResponse = handleRequest(oDataRequest)

        then:
        final List<String> products = getProductNamesSubList(requestedSkip, requestedSkip + requestedTop)
        final Filter product1Filter = Filter.filter(Criteria.where(CODE).eq(products.get(0)))

        ODataAssertions.assertThat(oDataResponse)
                .hasStatus(HttpStatusCodes.OK)
                .jsonBody()
                .pathHasSize(ALL_RESULTS_PATH, requestedTop)
                .pathContainsMatchingElement(SOME_RESULTS_PATH, product1Filter)
                .doesNotHavePath(COUNT_PATH)
                .hasPathWithValueContaining(NEXT_PAGE_LINK_PATH, SKIPTOKEN + "=" + (requestedTop + requestedSkip))
    }

    @Test
    def "get all products with \$inlinecount=allpages, \$top=2 and \$skip=3 returns last page with 1 product, count and NO nextlink"() {
        given:
        givenProductsExist()
        final Integer requestedSkip = 3
        final ODataRequestBuilder oDataRequest = requestBuilder(PRODUCTS_ENTITYSET)
                .withParameter(INLINECOUNT, ALLPAGES)
                .withParameter(SKIP, requestedSkip)
                .withParameter(TOP, 2)

        when:
        final ODataResponse oDataResponse = handleRequest(oDataRequest)

        then:
        final List<String> products = getProductNamesSubList(requestedSkip, requestedSkip + 1)
        final Filter product1Filter = Filter.filter(Criteria.where(CODE).eq(products.get(0)))

        ODataAssertions.assertThat(oDataResponse)
                .hasStatus(HttpStatusCodes.OK)
                .jsonBody()
                .pathHasSize(ALL_RESULTS_PATH, 1)
                .pathContainsMatchingElement(SOME_RESULTS_PATH, product1Filter)
                .doesNotHavePath(NEXT_PAGE_LINK_PATH)
                .hasPathWithValue(COUNT_PATH, "4")
    }

    @Test
    def "get products with \$skip=# exceeding the number of existing products"() {
        given: "1 product exists and skip=2"
        givenProductsExist(['prod1'])
        final ODataRequestBuilder oDataRequest = requestBuilder(PRODUCTS_ENTITYSET)
                .withParameter(INLINECOUNT, ALLPAGES)
                .withParameter(SKIP, 2)

        when:
        final ODataResponse oDataResponse = handleRequest(oDataRequest)

        then:
        ODataAssertions.assertThat(oDataResponse)
                .hasStatus(HttpStatusCodes.OK)
                .jsonBody()
                .pathHasSize(ALL_RESULTS_PATH, 0)
                .hasPathWithValue(COUNT_PATH, "1")
                .doesNotHavePath(NEXT_PAGE_LINK_PATH)
    }

    @Test
    def "get products \$count"() {
        given:
        givenProductsExist()
        final ODataRequest oDataRequest = requestBuilder(PRODUCTS_ENTITYSET, PathInfoBuilder.pathInfo()
                .withServiceName(IO)
                .withEntitySet(PRODUCTS_ENTITYSET)
                .withNavigationSegment(COUNT)).build()

        final ODataContext context = oDataContextGenerator.generate(oDataRequest)

        when:
        final ODataResponse oDataResponse = facade.handleRequest(context)

        then:
        ODataAssertions.assertThat(oDataResponse)
                .hasStatus(HttpStatusCodes.OK)
                .hasBody(PRODUCTS_ENTITY_SET_SIZE)
    }

    @Test
    def "get products with an invalid \$top"() {
        when:
        final ODataResponse oDataResponse = getProductsWithQueryParams(ImmutableMap.of(TOP, "invalidTop"))

        then:
        ODataAssertions.assertThat(oDataResponse)
                .hasStatus(HttpStatusCodes.BAD_REQUEST)
    }

    @Test
    def "get products with an invalid \$inlinecount"() {
        when:
        final ODataResponse oDataResponse = getProductsWithQueryParams(ImmutableMap.of(INLINECOUNT, "invalidInlineCount"))

        then:
        ODataAssertions.assertThat(oDataResponse)
                .hasStatus(HttpStatusCodes.BAD_REQUEST)
    }

    @Test
    def "get products with an invalid \$skip"() {
        when:
        final ODataResponse oDataResponse = getProductsWithQueryParams(ImmutableMap.of(SKIP, "invalidSkip"))

        then:
        ODataAssertions.assertThat(oDataResponse)
                .hasStatus(HttpStatusCodes.BAD_REQUEST)
    }

    @Test
    def "get products with an invalid \$count"() {
        when:
        final ODataResponse oDataResponse = getProductsWithQueryParams(ImmutableMap.of(COUNT, "invalidValue"))

        then:
        ODataAssertions.assertThat(oDataResponse)
                .hasStatus(HttpStatusCodes.BAD_REQUEST)
    }

    @Test
    def "get a specific products collection attribute"() {
        given:
        IntegrationTestUtil.importImpEx(
                '$io = integrationObject(code)',
                '$item = integrationObjectItem(integrationObject(code), code)',
                '$descriptor = attributeDescriptor(enclosingType(code), qualifier)',
                '$attributeType = returnIntegrationObjectItem(integrationObject(code), code)',
                'INSERT_UPDATE IntegrationObjectItem; $io[unique = true]; code[unique = true]; type(code); root[default = false]',
                "                                   ; $IO               ; Category           ; Category  ;",
                'INSERT_UPDATE IntegrationObjectItemAttribute; $item[unique = true]; attributeName[unique = true]; $descriptor            ; $attributeType; unique[default = false]',
                "                                            ; $IO:Category        ; code                        ; Category:code          ;",
                "                                            ; $IO:Product         ; supercategories             ; Product:supercategories; $IO:Category"
        )
        def category1Code = "testCategory|with|pipes"
        def category1CodeEncoded = "testCategory%7Cwith%7Cpipes"
        def prodCode = "testProduct001|with|pipes"
        def prodCodeEncoded = "testProduct001%7Cwith%7Cpipes"
        def prodIntegrationKey = "$VERSION|$CATALOG|" + prodCodeEncoded
        IntegrationTestUtil.importImpEx(
                "INSERT_UPDATE Catalog; id[unique = true]; name[lang = en]; defaultCatalog;",
                "                     ; $CATALOG         ; $CATALOG       ; true",
                "INSERT_UPDATE CatalogVersion; catalog(id)[unique = true]; version[unique = true]; active;",
                "                            ; $CATALOG                  ; $VERSION              ; true",
                "INSERT_UPDATE Category; code[unique = true]; catalogVersion(catalog(id), version)",
                "                      ; $category1Code     ; $CATALOG:$VERSION                   ",
                "                      ; testCategory2      ; $CATALOG:$VERSION                   ",
                "INSERT_UPDATE Product; code[unique = true] ; catalogVersion(catalog(id), version) ; supercategories(code)",
                "                     ; $prodCode               ; $CATALOG:$VERSION                ; $category1Code,testCategory2 "
        )
        final ODataRequest oDataRequest = requestBuilder(PRODUCTS_ENTITYSET, PathInfoBuilder.pathInfo()
                .withServiceName(IO)
                .withEntitySet(PRODUCTS_ENTITYSET)
                .withNavigationSegment("supercategories").withEntityKeys(prodIntegrationKey)).build()

        when:
        final ODataResponse oDataResponse = facade.handleRequest(oDataContextGenerator.generate(oDataRequest))

        then:
        final Filter category1Filter = Filter.filter(Criteria.where(CODE).eq(category1Code).and(INTEGRATIONKEY).eq(category1CodeEncoded))
        final Filter category2Filter = Filter.filter(Criteria.where(CODE).eq("testCategory2").and(INTEGRATIONKEY).eq("testCategory2"))
        ODataAssertions.assertThat(oDataResponse)
                .hasStatus(HttpStatusCodes.OK)
                .jsonBody()
                .pathHasSize(ALL_RESULTS_PATH, 2)
                .pathContainsMatchingElement(SOME_RESULTS_PATH, category1Filter)
                .pathContainsMatchingElement(SOME_RESULTS_PATH, category2Filter)
    }

    def givenProductsExist(productCodes = ['prod1', 'prod2', 'prod3', 'prod4']) {
        IntegrationTestUtil.importImpEx(
                "INSERT_UPDATE Catalog; id[unique = true]; name[lang = en]; defaultCatalog;",
                "                     ; $CATALOG         ; $CATALOG       ; true",
                "INSERT_UPDATE CatalogVersion; catalog(id)[unique = true]; version[unique = true]; active;",
                "                            ; $CATALOG                  ; $VERSION              ; true"
        )
        for(String code: productCodes) {
            IntegrationTestUtil.importImpEx(
                    "INSERT_UPDATE Product; code[unique = true] ; catalogVersion(catalog(id), version)",
                    "                     ; $code               ; $CATALOG:$VERSION                   "
            )
        }
    }

    def getProductNamesSubList(final Integer startIndex, final Integer endIndex) {
        final SearchResult<ProductModel> products = flexibleSearchService.search("SELECT {PK} FROM {Product}")
        products.getResult().subList(startIndex, endIndex).stream().map({ p -> p.code }).collect(Collectors.toList())
    }

    def getProductsWithQueryParams(final Map<String, String> queryParams) {
        getWithEntitySetAndQueryParams(PRODUCTS_ENTITYSET, queryParams)
    }

    def getWithEntitySetAndQueryParams(final String entitySetName, final Map<String, String> queryParams) {
        final ODataRequestBuilder oDataRequest = requestBuilder(entitySetName)
                .withParameters(queryParams)
        handleRequest(oDataRequest)
    }

    def requestBuilder(final String entitySetName, final PathInfoBuilder pathInfo = PathInfoBuilder.pathInfo()
            .withServiceName(IO)
            .withEntitySet(entitySetName)) {
        ODataRequestBuilder.oDataGetRequest()
                .withAccepts(APPLICATION_JSON_VALUE)
                .withPathInfo(pathInfo)
    }

    def handleRequest(final ODataRequestBuilder builder) {
        final ODataContext context = oDataContextGenerator.generate(builder.build())
        facade.handleRequest(context)
    }
}
