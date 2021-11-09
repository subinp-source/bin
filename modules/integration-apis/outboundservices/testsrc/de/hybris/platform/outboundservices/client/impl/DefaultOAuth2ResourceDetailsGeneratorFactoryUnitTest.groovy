/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.outboundservices.client.impl

import de.hybris.bootstrap.annotations.UnitTest
import de.hybris.platform.apiregistryservices.jalo.ConsumedCertificateCredential
import de.hybris.platform.apiregistryservices.model.AbstractCredentialModel
import de.hybris.platform.apiregistryservices.model.ConsumedCertificateCredentialModel
import de.hybris.platform.apiregistryservices.model.ConsumedDestinationModel
import de.hybris.platform.apiregistryservices.model.ConsumedOAuthCredentialModel
import de.hybris.platform.apiregistryservices.model.ExposedDestinationModel
import de.hybris.platform.apiregistryservices.model.ExposedOAuthCredentialModel
import org.junit.Test
import spock.lang.Specification

@UnitTest
class DefaultOAuth2ResourceDetailsGeneratorFactoryUnitTest extends Specification {

	def defaultOAuth2ResourceDetailsGeneratorFactory = new DefaultOAuth2ResourceDetailsGeneratorFactory()
	
	@Test
	def "factory isApplicable is #applicable for credential of type #credentialModelName"() {

		expect:
		defaultOAuth2ResourceDetailsGeneratorFactory.isApplicable(credentialModel) == applicable

		where:
		credentialModelName                  | credentialModel                          | applicable
		"ExposedOAuthCredentialModel"        | new ExposedOAuthCredentialModel()        | true
		"ConsumedOAuthCredentialModel"       | new ConsumedOAuthCredentialModel()       | true
		"ConsumedCertificateCredentialModel" | new ConsumedCertificateCredentialModel() | false
	}

	@Test
	def "the correct generator is instantiated for credential model of type  #credentialModelName"() {

		expect:
		defaultOAuth2ResourceDetailsGeneratorFactory.getGenerator(credentialModel).getClass() == generatorType

		where:
		credentialModelName            | credentialModel                    | generatorType
		"ExposedOAuthCredentialModel"  | new ExposedOAuthCredentialModel()  | ExposedOAuth2ResourceDetailsGenerator.class
		"ConsumedOAuthCredentialModel" | new ConsumedOAuthCredentialModel() | ConsumedOAuth2ResourceDetailsGenerator.class
	}

    @Test
    def "An exception is thrown when no generator is applicable for the credential model"() {
        given:
        def credentialModel = Stub(ConsumedCertificateCredentialModel){
            isApplicable() >> false
        }

        when:
        defaultOAuth2ResourceDetailsGeneratorFactory.getGenerator(credentialModel)

        then:
        thrown IllegalStateException
    }
}
