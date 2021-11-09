/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.odata2services.odata.persistence

import de.hybris.bootstrap.annotations.UnitTest
import de.hybris.platform.core.model.ItemModel
import de.hybris.platform.integrationservices.item.IntegrationItem
import de.hybris.platform.integrationservices.model.TypeDescriptor
import de.hybris.platform.integrationservices.search.PaginationParameters
import de.hybris.platform.integrationservices.search.WhereClauseConditions
import org.apache.olingo.odata2.api.edm.EdmEntitySet
import org.apache.olingo.odata2.api.edm.EdmEntityType
import org.apache.olingo.odata2.api.ep.entry.ODataEntry
import org.junit.Test
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

import static de.hybris.platform.odata2services.odata.persistence.ItemLookupRequest.itemLookupRequestBuilder

@UnitTest
class ItemLookupRequestUnitTest extends Specification {
    private static final String OBJECT_CODE = "IntegrationObjectCode"
    private static final String INTEGRATION_KEY = "item|key"

    @Shared
    def entitySet = Stub(EdmEntitySet) {
        getEntityType() >> Stub(EdmEntityType)
    }
    def itemType = Stub TypeDescriptor
    def item = Stub(IntegrationItem) {
        getIntegrationKey() >> INTEGRATION_KEY
        getItemType() >> itemType
    }

    @Test
    def "builds item lookup request with entitySet, acceptLocale & typeDescriptor successfully"() {
        given:
        def request = itemLookupRequestBuilder()
                .withEntitySet(entitySet)
                .withAcceptLocale(Locale.ENGLISH)
                .withTypeDescriptor(itemType)
                .build()

        expect:
        request != null
        request.entitySet == entitySet
        request.entityType == entitySet.entityType
        request.acceptLocale == Locale.ENGLISH
        request.integrationKey == null
        request.ODataEntry == null
        request.integrationObjectCode == null
        request.requestedItem == Optional.empty()
        request.typeDescriptor == itemType
        !request.countOnly
        !request.count
        !request.noFilterResult
        request.expand == null
        request.navigationSegments == []
        request.orderBy == []
        request.paginationParameters == Optional.empty()
        request.serviceRoot == null
        request.requestUri == null
        request.filter == null
    }

    @Test
    @Unroll
    def 'cannot be built without a #msg'() {
        when:
        itemLookupRequestBuilder()
                .withEntitySet(entitySet)
                .withAcceptLocale(Locale.ENGLISH)
                .withTypeDescriptor()
                .build()

        then:
        thrown(IllegalArgumentException)

        where:
        msg              | entitySetVal | acceptLocaleVal | typeDescriptorVal
        'typeDescriptor' | entitySet    | Locale.ENGLISH  | null
        'acceptLocale'   | entitySet    | null            | Stub(TypeDescriptor)
        'entitySet'      | null         | Locale.ENGLISH  | Stub(TypeDescriptor)
    }

    @Test
    def "typeDescriptor can be derived from the item.itemType"() {
        given:
        def request = minimumRequiredBuilder()
                .withIntegrationItem(item)
                .build()

        expect:
        request.requestedItem == Optional.of(item)
        request.typeDescriptor == item.itemType
    }

    @Test
    def "can be built with a typeDescriptor that is different than the item.itemType"() {
        given:
        def differentItemType = Stub TypeDescriptor
        def request = itemLookupRequestBuilder()
                .withEntitySet(entitySet)
                .withAcceptLocale(Locale.ENGLISH)
                .withIntegrationItem(item)
                .withTypeDescriptor(differentItemType)
                .build()

        expect:
        request.requestedItem == Optional.of(item)
        request.typeDescriptor == differentItemType
        differentItemType != item.itemType
    }

    @Test
    def "integration key can be derived from the context integration item"() {
        given:
        def request = minimumRequiredBuilder()
                .withIntegrationItem(item)
                .build()

        expect:
        request.integrationKey == item.integrationKey
    }

    @Test
    def "integration key specified for the request overrides the key value in the integration item"() {
        given:
        def request = minimumRequiredBuilder()
                .withIntegrationKey('request|integration|key')
                .withIntegrationItem(item)
                .build()

        expect:
        request.integrationKey == 'request|integration|key'
    }

    @Test
    def 'can be presented as an ItemConversionRequest'() {
        given:
        def lookup = itemLookupRequestBuilder()
                .withEntitySet(entitySet)
                .withAcceptLocale(Locale.ENGLISH)
                .withIntegrationObject(OBJECT_CODE)
                .withIntegrationItem(item)
                .build()
        def conversionOptions = Stub ConversionOptions
        def item = Stub ItemModel

        when:
        def conversion = lookup.toConversionRequest item, conversionOptions

        then:
        conversion.entitySet == entitySet
        conversion.acceptLocale == lookup.acceptLocale
        conversion.integrationObjectCode == lookup.integrationObjectCode
        conversion.options == conversionOptions
        conversion.value == item
        conversion.conversionLevel == 0
    }

    @Test
    def 'can be presented as an ItemConversionRequest with default conversion options'() {
        given:
        def lookup = itemLookupRequestBuilder()
                .withEntitySet(entitySet)
                .withAcceptLocale(Locale.ENGLISH)
                .withIntegrationObject(OBJECT_CODE)
                .withIntegrationItem(item)
                .build()
        def item = Stub ItemModel

        when:
        def conversion = lookup.toConversionRequest item

        then:
        conversion.entitySet == entitySet
        conversion.acceptLocale == lookup.acceptLocale
        conversion.integrationObjectCode == lookup.integrationObjectCode
        !conversion.options.expandPresent
        !conversion.options.navigationSegmentPresent
        conversion.value == item
        conversion.conversionLevel == 0
    }

    @Test
    @Unroll
    def "page parameters can be specified with top=#top and skip=#skip"() {
        given:
        def request = minimumRequiredBuilder()
                .withTop(top)
                .withSkip(skip)
                .build()

        expect:
        request.getPaginationParameters() == expectedPaginationParams
        request.getTop() == expectedTop
        request.getSkip() == expectedSkip

        where:
        skip | top  | expectedPaginationParams                          | expectedTop | expectedSkip
        null | null | Optional.empty()                                  | 0           | 0
        null | 100  | Optional.of(PaginationParameters.create(0, 100))  | 100         | 0
        100  | null | Optional.of(PaginationParameters.create(100, 0))  | 0           | 100
        100  | 50   | Optional.of(PaginationParameters.create(100, 50)) | 50          | 100
    }

    @Test
    @Unroll
    def "oDataEntry can be specified"() {
        given:
        def request = minimumRequiredBuilder()
                .withODataEntry(providedEntry)
                .build()

        expect:
        request.getODataEntry() == providedEntry

        where:
        providedEntry << [Stub(ODataEntry), null]
    }

    @Test
    @Unroll
    def "integrationObjectCode can be specified"() {
        given:
        def request = minimumRequiredBuilder()
                .withIntegrationObject(providedCode)
                .build()

        expect:
        request.integrationObjectCode == providedCode

        where:
        providedCode << [OBJECT_CODE, "", null]
    }

    @Test
    @Unroll
    def 'can specify to include total count in the result'() {
        given:
        def request = minimumRequiredBuilder()
                .withCount(count)
                .build()

        expect:
        request.count == count
        request.includeTotalCount() == count

        where:
        count << [true, false]
    }

    @Test
    @Unroll
    def 'count only request can be specified'() {
        given:
        def request = minimumRequiredBuilder()
                .withCountOnly(countOnly)
                .build()

        expect:
        request.isCountOnly() == countOnly

        where:
        countOnly << [true, false]
    }

    @Test
    @Unroll
    def 'noFilterResult can be specified'() {
        given:
        def request = minimumRequiredBuilder()
                .withHasNoFilterResult(hasNoFilter)
                .build()

        expect:
        request.noFilterResult == hasNoFilter

        where:
        hasNoFilter << [true, false]
    }

    @Test
    def 'expand can be specified'() {
        given:
        def providedExpand = []
        def request = minimumRequiredBuilder()
                .withExpand(providedExpand)
                .build()

        expect:
        request.getExpand() == providedExpand
    }

    @Test
    def 'navigationSegments can be specified'() {
        given:
        def providedNavigationSegments = []
        def request = minimumRequiredBuilder()
                .withNavigationSegments(providedNavigationSegments)
                .build()

        expect:
        request.getNavigationSegments() == providedNavigationSegments
    }

    @Test
    def 'orderBy can be specified'() {
        given:
        def providedOrderBy = []
        def request = minimumRequiredBuilder()
                .withOrderBy(providedOrderBy)
                .build()

        expect:
        request.getOrderBy() == providedOrderBy
    }

    @Test
    @Unroll
    def 'pageParameters can be specified'() {
        given:
        def request = minimumRequiredBuilder()
                .withPageParameters(providedPageParams)
                .build()

        expect:
        request.getPaginationParameters() == result

        where:
        providedPageParams                | result
        PaginationParameters.create(0, 0) | Optional.of(providedPageParams)
        null                              | Optional.empty()
    }

    @Test
    def 'serviceRoot can be specified'() {
        given:
        def serviceRoot = new URI("")
        def request = minimumRequiredBuilder()
                .withServiceRoot(serviceRoot)
                .build()

        expect:
        request.getServiceRoot() == serviceRoot
    }

    @Test
    def 'requestUri can be specified'() {
        given:
        def requestUri = new URI("")
        def request = minimumRequiredBuilder()
                .withRequestUri(requestUri)
                .build()

        expect:
        request.getRequestUri() == requestUri
    }

    @Test
    def 'filter can be specified'() {
        given:
        def filter = Stub(WhereClauseConditions)
        def request = minimumRequiredBuilder()
                .withFilter(filter)
                .build()

        expect:
        request.getFilter() == filter
    }

    @Test
    def "from builds a new request with the same fields as the original request"() {
        given:
        def originalRequest = itemLookupRequestBuilder()
                .withEntitySet(entitySet)
                .withAcceptLocale(Locale.ENGLISH)
                .withODataEntry(Stub(ODataEntry))
                .withIntegrationObject("SomeObjCode")
                .withIntegrationItem(item)
                .withTypeDescriptor(itemType)
                .withCountOnly(true)
                .withCount(true)
                .withHasNoFilterResult(false)
                .build()

        when:
        def request = itemLookupRequestBuilder().from(originalRequest).build()

        then:
        request != originalRequest
        request.entitySet == originalRequest.entitySet
        request.entityType == originalRequest.entityType
        request.acceptLocale == originalRequest.acceptLocale
        request.ODataEntry == originalRequest.getODataEntry()
        request.integrationObjectCode == originalRequest.integrationObjectCode
        request.requestedItem == originalRequest.requestedItem
        request.typeDescriptor == originalRequest.typeDescriptor
        request.countOnly == originalRequest.countOnly
        request.count == originalRequest.count
        request.noFilterResult == originalRequest.noFilterResult
        request.navigationSegments == originalRequest.navigationSegments
        request.orderBy == originalRequest.orderBy
        request.paginationParameters == originalRequest.paginationParameters
        request.serviceRoot == originalRequest.serviceRoot
        request.requestUri == originalRequest.requestUri
        request.filter == originalRequest.filter
    }

    ItemLookupRequest.ItemLookupRequestBuilder minimumRequiredBuilder() {
        itemLookupRequestBuilder()
                .withEntitySet(entitySet)
                .withAcceptLocale(Locale.ENGLISH)
                .withTypeDescriptor(Stub(TypeDescriptor))
    }
}
