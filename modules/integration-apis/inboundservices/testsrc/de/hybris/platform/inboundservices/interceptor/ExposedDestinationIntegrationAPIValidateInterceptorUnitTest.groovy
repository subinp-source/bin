package de.hybris.platform.inboundservices.interceptor

import de.hybris.bootstrap.annotations.UnitTest
import de.hybris.platform.apiregistryservices.model.DestinationTargetModel
import de.hybris.platform.apiregistryservices.model.ExposedDestinationModel
import de.hybris.platform.inboundservices.model.InboundChannelConfigurationModel
import de.hybris.platform.servicelayer.interceptor.InterceptorContext
import de.hybris.platform.servicelayer.interceptor.InterceptorException
import org.junit.Test
import spock.lang.Issue
import spock.lang.Specification

@UnitTest
@Issue('https://cxjira.sap.com/browse/GRIFFIN-4252')
class ExposedDestinationIntegrationAPIValidateInterceptorUnitTest extends Specification
{
    def interceptor = new ExposedDestinationIntegrationAPIValidateInterceptor()
    def destinationTargetModel = new DestinationTargetModel(id: "testId")
    def exposedDestinationModel = new ExposedDestinationModel(destinationTarget: destinationTargetModel)

    @Test
    def "Throw exception if Integration API already registered with current Destination Target"()
    {
        given:
        def exposedDestination = Stub(ExposedDestinationModel){
            getPk() >> null
            getDestinationTarget() >> Stub(DestinationTargetModel){
                getId() >> destinationTargetModel.id
            }

            getInboundChannelConfiguration() >> Stub(InboundChannelConfigurationModel){
                getExposedDestinations() >> [exposedDestinationModel]
            }
        }

        when:
        interceptor.onValidate(exposedDestination, Stub(InterceptorContext))

        then:
        def exception = thrown(InterceptorException)
        exception.message.contains("has already been exposed in this DestinationTarget")
    }

    @Test
    def "No exception being thrown if Integration API exposed in a different Destination Target"()
    {
        given:
        def exposedDestination = Stub(ExposedDestinationModel){
            getPk() >> null
            getDestinationTarget() >> Stub(DestinationTargetModel){
                getId() >> "anotherTestId"
            }

            getInboundChannelConfiguration() >> Stub(InboundChannelConfigurationModel){
                getExposedDestinations() >> [exposedDestinationModel]
            }
        }

        when:
        interceptor.onValidate(exposedDestination, Stub(InterceptorContext))

        then:
        noExceptionThrown()
    }
}
