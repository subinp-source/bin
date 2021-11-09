package de.hybris.platform.webhookservices.service.impl

import de.hybris.bootstrap.annotations.UnitTest
import de.hybris.platform.core.model.ItemModel
import de.hybris.platform.outboundservices.facade.SyncParameters

import de.hybris.platform.outboundservices.facade.impl.DefaultOutboundServiceFacade
import org.junit.Test
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.retry.backoff.FixedBackOffPolicy
import org.springframework.retry.policy.SimpleRetryPolicy
import org.springframework.retry.support.RetryTemplate
import rx.Observable
import spock.lang.Specification
import spock.lang.Unroll

@UnitTest
class WebhookImmediateRetryOutboundServiceFacadeUnitTest extends Specification {

    def TEST_INTEGRATION_OBJECT = "TestIntegrationObject"
    def TEST_DESTINATION = "TestDestination"
    def CHANGED_ITEM = Stub(ItemModel)
    def outboundServiceFacade = Mock(DefaultOutboundServiceFacade)
    def webhookServicesRetryTemplate = retryTemplate()
    def webhookImmediateRetryOutboundServiceFacade = new WebhookImmediateRetryOutboundServiceFacade(outboundServiceFacade, webhookServicesRetryTemplate)

    @Test
    @Unroll
    def "#attempts attempts to send the item are based when the response is #status"() {
        given:
        def parameters = syncParameters()
        
        when:
        webhookImmediateRetryOutboundServiceFacade.send(parameters)

        then:
        attempts * outboundServiceFacade.send(syncParameters()) >> stubObservable(status)

        where:
        attempts | status
        2        | HttpStatus.INTERNAL_SERVER_ERROR
        1        | HttpStatus.BAD_REQUEST
        1        | HttpStatus.OK
        1        | HttpStatus.MULTIPLE_CHOICES
        1        | HttpStatus.CONTINUE
    }

    @Test
    @Unroll
    def "#attempts attempts to send the item are based when the response is #status for send without sync parameters"() {
        when:
        webhookImmediateRetryOutboundServiceFacade.send(CHANGED_ITEM, TEST_INTEGRATION_OBJECT, TEST_DESTINATION)

        then:
        attempts * outboundServiceFacade.send(_ as SyncParameters) >> stubObservable(status)

        where:
        attempts | status
        2        | HttpStatus.INTERNAL_SERVER_ERROR
        1        | HttpStatus.BAD_REQUEST
        1        | HttpStatus.OK
        1        | HttpStatus.MULTIPLE_CHOICES
        1        | HttpStatus.CONTINUE
    }

    def syncParameters() {
        SyncParameters.syncParametersBuilder().withIntegrationObjectCode(TEST_INTEGRATION_OBJECT).withItem(CHANGED_ITEM).withDestinationId(TEST_DESTINATION).build()
    }

    def stubObservable(HttpStatus httpStatus) {
        return Observable.just(Stub(ResponseEntity) {
            getStatusCode() >> httpStatus
        })
    }

    def retryTemplate() {
        RetryTemplate retryTemplate = new RetryTemplate()

        FixedBackOffPolicy fixedBackOffPolicy = new FixedBackOffPolicy()
        fixedBackOffPolicy.setBackOffPeriod(1000l)
        retryTemplate.setBackOffPolicy(fixedBackOffPolicy)

        Map<Class<? extends Throwable>, Boolean> exceptions = new HashMap()
        exceptions.put(WebhookRetryableException.class, true)
        SimpleRetryPolicy retryPolicy = new SimpleRetryPolicy(2, exceptions, true)
        retryTemplate.setRetryPolicy(retryPolicy)

        return retryTemplate
    }
}
