/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.kymaintegrationservices.services.impl

import de.hybris.bootstrap.annotations.UnitTest
import de.hybris.platform.apiregistryservices.model.DestinationTargetModel
import de.hybris.platform.apiregistryservices.model.ExposedDestinationModel
import de.hybris.platform.apiregistryservices.services.ApiRegistrationService
import de.hybris.platform.apiregistryservices.services.DestinationTargetService
import de.hybris.platform.kymaintegrationservices.dto.KymaRegistrationRequest
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException
import org.junit.Test
import spock.lang.Specification

@UnitTest
class DefaultKymaDestinationTargetRegistrationServiceTest extends Specification {

    public static final String SERVICES_API_URL = "https://localhost:9002"
    public static final String DEFAULT_TEMPLATE = "Default_Template"
    public static final String NEW_DESTINATION_TARGET = "new_destination_target"
    public static final String EXISTING_DESTINATION_TARGET = "existing_destination_target"
    public static final String TOKEN_URL = "https://kyma.token.url"

    def apiRegistrationService = Mock(ApiRegistrationService)

    def kymaDestinationTargetRegistrationService = new DefaultKymaDestinationTargetRegistrationService(
            apiRegistrationService: apiRegistrationService)

    @Test
    def "re-register an existing destination target"() {
        given:
        def kymaRegistrationRequest = new KymaRegistrationRequest()
        kymaRegistrationRequest.setServicesApiUrl(SERVICES_API_URL)
        kymaRegistrationRequest.setDestinationTargetId(EXISTING_DESTINATION_TARGET)
        kymaRegistrationRequest.setTokenUrl(TOKEN_URL)
        kymaRegistrationRequest.setTemplateDestinationTargetId(DEFAULT_TEMPLATE)

        def destinationTargetService = Mock(DestinationTargetService) {
            getDestinationTargetById(EXISTING_DESTINATION_TARGET) >> Stub(DestinationTargetModel) {
                getDestinations() >> Arrays.asList(Stub(ExposedDestinationModel), Stub(ExposedDestinationModel))
                getTemplate() >> Boolean.FALSE
            }
            getDestinationTargetById(DEFAULT_TEMPLATE) >> Stub(DestinationTargetModel) {
                getTemplate() >> Boolean.TRUE
            }
        }
        kymaDestinationTargetRegistrationService.setDestinationTargetService(destinationTargetService)

        when:
        kymaDestinationTargetRegistrationService.registerDestinationTarget(kymaRegistrationRequest,true)

        then:
        2 * apiRegistrationService.registerExposedDestination(_)
        0 * destinationTargetService.createDestinationTarget(_, _, _)
        0 * destinationTargetService.registerDestinationTarget(_, _)
        0 * destinationTargetService.createDestinations(_, _, _)
        0 * destinationTargetService.createEventConfigurations(_, _, _)
    }

    @Test
    def "register a new destination target"() {
        given:
        def kymaRegistrationRequest = new KymaRegistrationRequest()
        kymaRegistrationRequest.setServicesApiUrl(SERVICES_API_URL)
        kymaRegistrationRequest.setDestinationTargetId(NEW_DESTINATION_TARGET)
        kymaRegistrationRequest.setTokenUrl(TOKEN_URL)
        kymaRegistrationRequest.setTemplateDestinationTargetId(DEFAULT_TEMPLATE)

        def destinationTargetService = Mock(DestinationTargetService) {
            getDestinationTargetById(NEW_DESTINATION_TARGET) >> { throw new ModelNotFoundException("Destination target not found!") }
            getDestinationTargetById(DEFAULT_TEMPLATE) >> Stub(DestinationTargetModel){
                getTemplate() >> Boolean.TRUE
            }
        }

        kymaDestinationTargetRegistrationService.setDestinationTargetService(destinationTargetService)

        when:
        kymaDestinationTargetRegistrationService.registerDestinationTarget(kymaRegistrationRequest, false)

        then:
        0 * apiRegistrationService.registerExposedDestination(_)
        1 * destinationTargetService.createDestinationTarget(_, _, _)
        1 * destinationTargetService.registerDestinationTarget(_, _)
        1 * destinationTargetService.createDestinations(_, _, _)
        1 * destinationTargetService.createEventConfigurations(_, _, _)
    }

}
