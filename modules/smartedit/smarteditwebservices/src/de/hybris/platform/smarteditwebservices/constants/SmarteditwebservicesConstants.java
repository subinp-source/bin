/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.smarteditwebservices.constants;

/**
 * Global class for all Smarteditwebservices constants. You can add global constants for your extension into this class.
 */
@SuppressWarnings("squid:S2068")
public final class SmarteditwebservicesConstants extends GeneratedSmarteditwebservicesConstants
{
   public static final String EXTENSIONNAME = "smarteditwebservices";
   public static final String API_VERSION = "/v1";

   public static final String FIELD_REQUIRED = "field.required";
   public static final String INVALID_CONFIGURATION_KEY = "invalid.configuration.key";

   // Swagger documentation constants
   public static final String LICENSE_URL_PROPERTY = EXTENSIONNAME + ".license.url";
   public static final String TERMS_OF_SERVICE_URL_PROPERTY = EXTENSIONNAME + ".terms.of.service.url";
   public static final String LICENSE_PROPERTY = EXTENSIONNAME + ".licence";
   public static final String DOCUMENTATION_DESC_PROPERTY = EXTENSIONNAME + ".documentation.desc";
   public static final String DOCUMENTATION_TITLE_PROPERTY = EXTENSIONNAME + ".documentation.title";
   public static final String DOCUMENTATION_API_VERSION = "1.0";
   public static final String AUTHORIZATION_HOST = EXTENSIONNAME + ".oauth.host";
   public static final String HOST = EXTENSIONNAME + ".host";
   public static final String HOST_DEFAULT = "hostname";
   public static final String PASSWORD_AUTHORIZATION_SCOPE = EXTENSIONNAME + ".oauth2.password.scope";
   public static final String PASSWORD_AUTHORIZATION_NAME = "oauth2_password";
   public static final String CLIENT_CREDENTIAL_AUTHORIZATION_SCOPE = EXTENSIONNAME + ".oauth2.clientCredentials.scope";
   public static final String CLIENT_CREDENTIAL_AUTHORIZATION_NAME = "oauth2_client_credentials";
   public static final String AUTHORIZATION_URL = EXTENSIONNAME + ".oauth2.tokenUrl";
   /**
    * @deprecated since 2005, please use {@value #PASSWORD_AUTHORIZATION_SCOPE} instead
    */
   @Deprecated(since = "2005", forRemoval = true)
   public static final String AUTHORIZATION_SCOPE_PROPERTY = EXTENSIONNAME + ".oauth.scope";

   private SmarteditwebservicesConstants()
   {
      // empty to avoid instantiating this constant class
   }

}
