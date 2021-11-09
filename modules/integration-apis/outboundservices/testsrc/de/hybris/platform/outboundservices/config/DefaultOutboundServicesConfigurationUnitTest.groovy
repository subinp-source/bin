package de.hybris.platform.outboundservices.config

import de.hybris.bootstrap.annotations.UnitTest
import de.hybris.platform.servicelayer.config.ConfigurationService
import org.apache.commons.configuration.Configuration
import org.apache.commons.configuration.ConversionException
import org.junit.Test
import spock.lang.Specification
import spock.lang.Unroll

@UnitTest
class DefaultOutboundServicesConfigurationUnitTest extends Specification {

    private static final String MAX_RESPONSE_PAYLOAD_SIZE_KEY = "outboundservices.response.payload.max.size.bytes";
    private static final int DEFAULT_MAX_RESPONSE_PAYLOAD_SIZE = 1024;
    private static final String SUCCESS_RETENTION_PROPERTY_KEY = "outboundservices.monitoring.success.payload.retention";
    private static final String ERROR_RETENTION_PROPERTY_KEY = "outboundservices.monitoring.error.payload.retention";
    private static final String MONITORING_ENABLED_KEY = "outboundservices.monitoring.enabled";
    private static final String REQUEST_EXECUTION_TIMEOUT_KEY = "outboundservices.request.execution.timeout.millisecs";
    private static final long DEFAULT_REQUEST_EXECUTION_TIMEOUT_VALUE_MILLISECS = 5000

    def configuration = Mock Configuration

    def configurationService = Stub(ConfigurationService) {
        getConfiguration() >> configuration
    }

    def outboundServicesConfiguration = new DefaultOutboundServicesConfiguration(configurationService: configurationService)

    @Test
    @Unroll
    def "success payload retention #status when outboundservices.monitoring.success.payload.retention is #value"() {
        given:
        configuration.getBoolean(SUCCESS_RETENTION_PROPERTY_KEY) >> value

        expect:
        outboundServicesConfiguration.isPayloadRetentionForSuccessEnabled() == value

        where:
        status     | value
        'enabled'  | true
        'disabled' | false
    }

    @Test
    @Unroll
    def "success payload retention is enabled by default if #exceptionName is thrown"() {
        given:
        configuration.getBoolean(SUCCESS_RETENTION_PROPERTY_KEY) >> { throw exception }

        expect:
        !outboundServicesConfiguration.isPayloadRetentionForSuccessEnabled()

        where:
        exception                    | exceptionName
        new NoSuchElementException() | 'NoSuchElementException'
        new ConversionException()    | 'ConversionException'
    }

    @Test
    def "error retention is disabled if outboundservices.monitoring.error.payload.retention is false"() {
        given:
        configuration.getBoolean(ERROR_RETENTION_PROPERTY_KEY) >> false

        expect:
        !outboundServicesConfiguration.isPayloadRetentionForErrorEnabled()
    }

    @Test
    @Unroll
    def "error retention is enabled by default if #exceptionName is thrown"() {
        given:
        configuration.getBoolean(ERROR_RETENTION_PROPERTY_KEY) >> { throw exception }

        expect:
        outboundServicesConfiguration.isPayloadRetentionForErrorEnabled()

        where:
        exception                    | exceptionName
        new NoSuchElementException() | 'NoSuchElementException'
        new ConversionException()    | 'ConversionException'
    }

    @Test
    @Unroll
    def "Monitoring is #status if outboundservices.monitoring.enabled is #value"() {
        given:
        configuration.getBoolean(MONITORING_ENABLED_KEY) >> value

        expect:
        outboundServicesConfiguration.isMonitoringEnabled() == value

        where:
        status     | value
        'enabled'  | true
        'disabled' | false
    }

    @Test
    @Unroll
    def "Monitoring is enabled by default if #exceptionName is thrown"() {
        given:
        configuration.getBoolean(MONITORING_ENABLED_KEY) >> { throw exception }

        expect:
        outboundServicesConfiguration.isMonitoringEnabled()

        where:
        exception                    | exceptionName
        new NoSuchElementException() | 'NoSuchElementException'
        new ConversionException()    | 'ConversionException'
    }

    @Test
    def "max response payload size is returned if outboundservices.response.payload.max.size.bytes is set"() {
        given:
        configuration.getInt(MAX_RESPONSE_PAYLOAD_SIZE_KEY) >> DEFAULT_MAX_RESPONSE_PAYLOAD_SIZE

        expect:
        outboundServicesConfiguration.getMaximumResponsePayloadSize() == DEFAULT_MAX_RESPONSE_PAYLOAD_SIZE
    }

    @Test
    @Unroll
    def "max response payload size is the default value if #exceptionName is thrown"() {
        given:
        configuration.getInt(MAX_RESPONSE_PAYLOAD_SIZE_KEY) >> { throw exception }

        expect:
        outboundServicesConfiguration.getMaximumResponsePayloadSize() == DEFAULT_MAX_RESPONSE_PAYLOAD_SIZE

        where:
        exception                    | exceptionName
        new NoSuchElementException() | 'NoSuchElementException'
        new ConversionException()    | 'ConversionException'
    }

    @Test
    def "max response payload size is set successfully"() {
        given:
        def exampleMaximumResponsePayloadSize = 1000

        when:
        outboundServicesConfiguration.setMaximumResponsePayloadSize(exampleMaximumResponsePayloadSize)

        then:
        1 * configuration.setProperty(MAX_RESPONSE_PAYLOAD_SIZE_KEY, String.valueOf(exampleMaximumResponsePayloadSize))
    }

    @Test
    def "getRequestExecutionTimeout returns default timeout value"() {
        given:
        configuration.getLong(REQUEST_EXECUTION_TIMEOUT_KEY) >> DEFAULT_REQUEST_EXECUTION_TIMEOUT_VALUE_MILLISECS

        expect:
        outboundServicesConfiguration.getRequestExecutionTimeout() == DEFAULT_REQUEST_EXECUTION_TIMEOUT_VALUE_MILLISECS
    }

    @Test
    def "setRequestExecutionTimeout sets a new timeout value successfully"() {
        given:
        def newTimeout = 5000

        when:
        outboundServicesConfiguration.setRequestExecutionTimeout(newTimeout)

        then:
        1 * configuration.setProperty(REQUEST_EXECUTION_TIMEOUT_KEY, String.valueOf(newTimeout))
    }

    @Test
    def "setRequestExecutionTimeout sets a default timeout when its value is negative"() {
        given:
        def negativeTimeout = -5000

        when:
        outboundServicesConfiguration.setRequestExecutionTimeout(negativeTimeout)

        then:
        1 * configuration.setProperty(REQUEST_EXECUTION_TIMEOUT_KEY,
                String.valueOf(DEFAULT_REQUEST_EXECUTION_TIMEOUT_VALUE_MILLISECS))
    }

    @Test
    @Unroll
    def "property timeout is the default value if #exceptionName is thrown"() {
        given:
        configuration.getLong(REQUEST_EXECUTION_TIMEOUT_KEY) >> { throw exception }

        expect:
        outboundServicesConfiguration.getRequestExecutionTimeout() == DEFAULT_REQUEST_EXECUTION_TIMEOUT_VALUE_MILLISECS

        where:
        exception                    | exceptionName
        new NoSuchElementException() | 'NoSuchElementException'
        new ConversionException()    | 'ConversionException'
    }
}
