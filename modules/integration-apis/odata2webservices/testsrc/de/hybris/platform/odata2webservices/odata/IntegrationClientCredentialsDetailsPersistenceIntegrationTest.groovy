/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.odata2webservices.odata

import de.hybris.bootstrap.annotations.IntegrationTest
import de.hybris.platform.core.model.user.EmployeeModel
import de.hybris.platform.inboundservices.model.IntegrationClientCredentialsDetailsModel
import de.hybris.platform.integrationservices.util.IntegrationTestUtil
import de.hybris.platform.servicelayer.ServicelayerSpockSpecification
import org.junit.Test

@IntegrationTest
class IntegrationClientCredentialsDetailsPersistenceIntegrationTest extends ServicelayerSpockSpecification {
    private static final String CLIENT_ID = "Cred1"
    private static final String EXISTING_USER_UID = "Kaitlin"

    def setupSpec() {
        userWithUidExists(EXISTING_USER_UID)
    }

    def cleanup() {
        IntegrationTestUtil.remove IntegrationClientCredentialsDetailsModel, { it.clientId == CLIENT_ID }
    }

    def cleanupSpec() {
        IntegrationTestUtil.removeSafely EmployeeModel, { it.uid == EXISTING_USER_UID }
    }

    @Test
    def "can create an IntegrationClientCredentialsDetails with an existing non-admin user"() {
        when:
        IntegrationTestUtil.importImpEx(
                'INSERT_UPDATE IntegrationClientCredentialsDetails; clientId[unique=true]; user(uid)',
                "                                                 ; $CLIENT_ID           ; $EXISTING_USER_UID"
        )

        then:
        noExceptionThrown()
        def optionalClientCredentialDetails = findClientCredentialsDetailsWithClientId(CLIENT_ID)
        optionalClientCredentialDetails.isPresent()
        optionalClientCredentialDetails.get().authorizedGrantTypes == ["client_credentials"] as Set
    }

    @Test
    def "cannot create an IntegrationClientCredentialsDetails without providing a user"() {
        when:
        IntegrationTestUtil.importImpEx(
                'INSERT_UPDATE IntegrationClientCredentialsDetails; clientId[unique=true]',
                "                                                 ; $CLIENT_ID           "
        )

        then:
        thrown(AssertionError)
        findClientCredentialsDetailsWithClientId(CLIENT_ID).isEmpty()
    }

    @Test
    def "cannot create an IntegrationClientCredentialsDetails with admin user"() {
        when:
        IntegrationTestUtil.importImpEx(
                'INSERT_UPDATE IntegrationClientCredentialsDetails; clientId[unique=true]; user(uid)',
                "                                                 ; $CLIENT_ID           ; admin"
        )

        then:
        thrown(AssertionError)
        findClientCredentialsDetailsWithClientId(CLIENT_ID).isEmpty()
    }

    @Test
    def "cannot update an existing IntegrationClientCredentialsDetails user"() {
        given:
        "IntegrationClientCredentialsDetails with $EXISTING_USER_UID exists"
        IntegrationTestUtil.importImpEx(
                'INSERT_UPDATE IntegrationClientCredentialsDetails; clientId[unique=true]; user(uid)         ',
                "                                                 ; $CLIENT_ID           ; $EXISTING_USER_UID"
        )
        def userUid2 = 'someOtherUser'
        userWithUidExists(userUid2)

        when:
        IntegrationTestUtil.importImpEx(
                'UPDATE IntegrationClientCredentialsDetails; clientId[unique=true]; user(uid)',
                "                                          ; $CLIENT_ID           ; $userUid2"
        )

        then:
        def optionalClientCredentialDetails = findClientCredentialsDetailsWithClientId(CLIENT_ID)
        optionalClientCredentialDetails.isPresent()
        optionalClientCredentialDetails.get().user.uid == EXISTING_USER_UID
    }

    @Test
    def "cannot update an existing IntegrationClientCredentialsDetails authorizedGrantTypes"() {
        given:
        "IntegrationClientCredentialsDetails with $EXISTING_USER_UID exists"
        IntegrationTestUtil.importImpEx(
                'INSERT_UPDATE IntegrationClientCredentialsDetails; clientId[unique=true]; user(uid)         ',
                "                                                 ; $CLIENT_ID           ; $EXISTING_USER_UID"
        )

        when:
        IntegrationTestUtil.importImpEx(
                'UPDATE IntegrationClientCredentialsDetails; clientId[unique=true]; user(uid)         ; authorizedGrantTypes',
                "                                          ; $CLIENT_ID           ; $EXISTING_USER_UID; password"
        )

        then:
        def optionalClientCredentialDetails = findClientCredentialsDetailsWithClientId(CLIENT_ID)
        optionalClientCredentialDetails.isPresent()
        optionalClientCredentialDetails.get().authorizedGrantTypes == ["client_credentials"] as Set
    }

    @Test
    def "cannot create IntegrationClientCredentialsDetails with a provided value for authorizedGrantTypes not equal to the default value [\"client_credentials\"]"() {
        when:
        IntegrationTestUtil.importImpEx(
                'INSERT_UPDATE IntegrationClientCredentialsDetails; clientId[unique=true]; user(uid)         ; authorizedGrantTypes',
                "                                                 ; $CLIENT_ID           ; $EXISTING_USER_UID; password"
        )

        then:
        thrown(AssertionError)
        def optionalClientCredentialDetails = findClientCredentialsDetailsWithClientId(CLIENT_ID)
        optionalClientCredentialDetails.isEmpty()
    }

    private static Optional<IntegrationClientCredentialsDetailsModel> findClientCredentialsDetailsWithClientId(clientIdValue) {
        IntegrationTestUtil.findAny(IntegrationClientCredentialsDetailsModel, {
            it.clientId == clientIdValue
        })
    }

    private static userWithUidExists(String uid) {
        IntegrationTestUtil.importImpEx(
                "INSERT_UPDATE Employee; uid[unique = true]",
                "                      ; $uid"
        )
    }
}