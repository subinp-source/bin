/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.odata2webservicesfeaturetests.useraccess;

import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.impex.jalo.ImpExException;
import de.hybris.platform.integrationservices.util.IntegrationTestUtil;
import de.hybris.platform.odata2webservices.constants.Odata2webservicesConstants;
import de.hybris.platform.odata2webservicesfeaturetests.ws.BasicAuthRequestBuilder;

import org.springframework.http.MediaType;

import groovy.lang.Closure;

public class UserAccessTestUtils
{
	protected static final String SERVICE = "TestCatalog";
	protected static final String ADMIN_USER = "userintegrationadmin";
	protected static final String PASSWORD = "password";

    protected static void givenCatalogIOExists() throws ImpExException
    {
        givenCatalogItemExistsForIO(SERVICE);
    }

    protected static void givenCatalogItemExistsForIO(final String objectCode) throws ImpExException
    {
        IntegrationTestUtil.importImpEx("INSERT_UPDATE IntegrationObject; code[unique = true]",
                "                                                              ; " + objectCode,
                "INSERT_UPDATE IntegrationObjectItem; integrationObject(code)[unique = true]; code[unique = true]; type(code)",
                "                                   ; " + objectCode + "                    ; Catalog            ; Catalog",
                "$returnItem=returnIntegrationObjectItem(integrationObject(code), code)",
                "$descriptor=attributeDescriptor(enclosingType(code), qualifier)",
                "INSERT_UPDATE IntegrationObjectItemAttribute; integrationObjectItem(integrationObject(code), code)[unique = true]; attributeName[unique = true]; $descriptor  ; $returnItem; unique[default = false]",
                "                                            ; " + objectCode + ":Catalog                                         ; id                          ; Catalog:id   ;            ;",
                "                                            ; " + objectCode + ":Catalog                                         ; name                        ; Catalog:name ;            ;");
    }

    public static void givenUserExistsWithUidAndGroups(final String uid, final String password, final String groups)
		    throws ImpExException
    {
        createUser(uid, password, groups);
    }

    public static void givenUserExistsWithUidAndGroups(final String uid, final String password) throws ImpExException
    {
        UserAccessTestUtils.givenUserExistsWithUidAndGroups(uid, password, "");
    }

    public static void givenUserExistsWithUidAndGroups(final String uid) throws ImpExException
    {
        UserAccessTestUtils.givenUserExistsWithUidAndGroups(uid, PASSWORD, "");
    }

    protected static BasicAuthRequestBuilder basicAuthRequest(final String path)
    {
        return new BasicAuthRequestBuilder().extensionName(Odata2webservicesConstants.EXTENSIONNAME)
                                            .accept(MediaType.APPLICATION_XML_VALUE)
                                            .path(path);
    }

    public static void createUser(final String uid, final String pwd, final String groups) throws ImpExException
    {
        IntegrationTestUtil.importImpEx(
                "$password=@password[translator = de.hybris.platform.impex.jalo.translators.UserPasswordTranslator]",
                "INSERT_UPDATE Employee; UID[unique = true] ; groups(uid)[mode = append] ;$password",
                "                      ; " + uid + "        ; " + groups + "             ;*:" + pwd);
    }

    public static void createUser(final String uid, final String pwd) throws ImpExException
    {
        UserAccessTestUtils.createUser(uid, pwd, "");
    }

    public static void createUser(final String uid) throws ImpExException
    {
        UserAccessTestUtils.createUser(uid, PASSWORD, "");
    }

    public static void createUser() throws ImpExException
    {
        UserAccessTestUtils.createUser(ADMIN_USER, PASSWORD, "");
    }

    public static void deleteUser(final String uid)
    {
        IntegrationTestUtil.removeSafely(UserModel.class, user -> user.getUid().equals(uid));
    }
}
