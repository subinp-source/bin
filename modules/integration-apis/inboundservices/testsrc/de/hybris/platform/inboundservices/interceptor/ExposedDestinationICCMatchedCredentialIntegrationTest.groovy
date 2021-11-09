package de.hybris.platform.inboundservices.interceptor

import de.hybris.bootstrap.annotations.IntegrationTest
import de.hybris.platform.apiregistryservices.model.BasicCredentialModel
import de.hybris.platform.apiregistryservices.model.DestinationTargetModel
import de.hybris.platform.apiregistryservices.model.EndpointModel
import de.hybris.platform.apiregistryservices.model.ExposedDestinationModel
import de.hybris.platform.apiregistryservices.model.ExposedOAuthCredentialModel
import de.hybris.platform.inboundservices.model.InboundChannelConfigurationModel
import de.hybris.platform.integrationservices.model.IntegrationObjectModel
import de.hybris.platform.integrationservices.util.IntegrationTestUtil
import de.hybris.platform.servicelayer.ServicelayerTransactionalSpockSpecification
import de.hybris.platform.webservicescommons.model.OAuthClientDetailsModel
import org.junit.Test
import spock.lang.Issue

@IntegrationTest
@Issue('https://cxjira.sap.com/browse/GRIFFIN-4231')
class ExposedDestinationICCMatchedCredentialIntegrationTest extends ServicelayerTransactionalSpockSpecification {

    private static final def IO = "InboundProductIO"
    private static final def URL = "http://localhost:9002/test"
    private static final def USERNAME = "user"
    private static final def PASSWORD = "pass"
    def setup() {
        IntegrationTestUtil.importImpEx(
                'INSERT_UPDATE IntegrationObject; code[unique = true]',
                "                                      ; $IO ",
                'INSERT_UPDATE InboundChannelConfiguration; integrationObject(code)[unique = true]; authenticationType(code)',
                "                                         ; $IO                        ; BASIC",
                'INSERT_UPDATE BasicCredential;id[unique=true];username;password',
                "                                ;basicCred; $USERNAME ; $PASSWORD ",
                'INSERT_UPDATE OAuthClientDetails;clientId[unique=true];resourceIds;scope;authorizedGrantTypes;clientSecret;authorities',
                "                                ;oldDefaultTest;hybris;basic;authorization_code,refresh_token,password,client_credentials;password;ROLE_TRUSTED_CLIENT",
                'INSERT_UPDATE ExposedOAuthCredential;id[unique=true];oAuthClientDetails(clientId);password',
                "                                    ;exposedOAuthCredential;oldDefaultTest;secret",
                'INSERT_UPDATE DestinationTarget;id[unique=true];destinationChannel(code)[default=DEFAULT];template',
                "                               ;template_default;;true",
                'INSERT_UPDATE Endpoint;id[unique=true];version;specUrl;specData;name;description',
                "                       ;e1;v1;$URL;e1;n1;des"
        )
    }
    def cleanup() {
        IntegrationTestUtil.removeAll InboundChannelConfigurationModel
        IntegrationTestUtil.removeAll IntegrationObjectModel
        IntegrationTestUtil.removeAll BasicCredentialModel
        IntegrationTestUtil.removeAll OAuthClientDetailsModel
        IntegrationTestUtil.removeAll ExposedOAuthCredentialModel
        IntegrationTestUtil.removeAll DestinationTargetModel
        IntegrationTestUtil.removeAll EndpointModel
        IntegrationTestUtil.removeAll ExposedDestinationModel
    }

    @Test
    def "Impex:there is no exception when credential of ExposedDestination match authenticationType of InboundChannelConfiguration"() {
        when:
        IntegrationTestUtil.importImpEx(
                'INSERT_UPDATE ExposedDestination;id[unique=true];url;endpoint(id);additionalProperties;destinationTarget(id)[default=template_default];active;credential(id);inboundChannelConfiguration(integrationObject(code))',
                "                                ;template_first_dest;$URL;e1;;;true;basicCred;$IO",
                "                                ;template_second_dest;$URL;e1;;;true;basicCred;$IO"
        )
        then:
        noExceptionThrown()
    }

    @Test
    def "Impex: throw exception when credential of ExposedDestination not match authenticationType of InboundChannelConfiguration"() {
        when:
        IntegrationTestUtil.importImpEx(
                'INSERT_UPDATE ExposedDestination;id[unique=true];url;endpoint(id);additionalProperties;destinationTarget(id)[default=template_default];active;credential(id);inboundChannelConfiguration(integrationObject(code))',
                "                              ;template_first_dest;$URL;e1;;;true;exposedOAuthCredential;$IO"
        )

        then:
        def e = thrown AssertionError
        e.message.contains "does not match assigned credential type of InboundChannelConfiguration"
    }
}
