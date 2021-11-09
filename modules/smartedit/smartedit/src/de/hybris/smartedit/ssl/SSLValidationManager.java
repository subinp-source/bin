/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.smartedit.ssl;

import java.net.Socket;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.annotation.PostConstruct;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509ExtendedTrustManager;

import org.apache.commons.lang3.ArrayUtils;


/**
 * The SSL validation manager makes it possible to enable or disable SSL validation.
 * <p>
 * Note that the SSL validation should only disabled for testing in dev/staging/qa environments where no properly signed
 * certificates exist, and/or for internal systems communicating with other internal systems. It is strongly suggested
 * to enable SSL validation in production environments.
 */
public class SSLValidationManager
{
   private static final String TLS = "TLS";
   private final boolean isSSLValidationEnabled;

   /**
    * Constructor.
    *
    * @param isSSLValidationEnabled
    *           true to enable SSL validation, false otherwise
    */
   public SSLValidationManager(final boolean isSSLValidationEnabled)
   {
      this.isSSLValidationEnabled = isSSLValidationEnabled;
   }

   /**
    * Disables SSL certificate validation.
    *
    * @throws Exception
    *            if an error occurs while disabling SSL validation
    */
   @PostConstruct
   public void disableSSLValidation() throws Exception
   {
      if (!isSSLValidationEnabled)
      {
         final SSLContext sslc = SSLContext.getInstance(TLS);
         final TrustManager trustAllManager = new TrustAllManager();
         sslc.init(null, ArrayUtils.toArray(trustAllManager), null);
         HttpsURLConnection.setDefaultSSLSocketFactory(sslc.getSocketFactory());
         HttpsURLConnection.setDefaultHostnameVerifier(new TrustAllHostnameVerifier());
      }
   }

   /**
    * Trust manager that does not perform any checks and allows all connections.
    */
   private static class TrustAllManager extends X509ExtendedTrustManager
   {

      @Override
      public void checkClientTrusted(final X509Certificate[] chain, final String authType) throws CertificateException
      {
         // Intentionally left empty
      }

      @Override
      public void checkServerTrusted(final X509Certificate[] chain, final String authType) throws CertificateException
      {
         // Intentionally left empty
      }

      @Override
      public X509Certificate[] getAcceptedIssuers()
      {
         return new X509Certificate[0];
      }

      @Override
      public void checkClientTrusted(final X509Certificate[] chain, final String authType, final Socket socket)
            throws CertificateException
      {
         // Intentionally left empty
      }

      @Override
      public void checkServerTrusted(final X509Certificate[] chain, final String authType, final Socket socket)
            throws CertificateException
      {
         // Intentionally left empty
      }

      @Override
      public void checkClientTrusted(final X509Certificate[] chain, final String authType, final SSLEngine engine)
            throws CertificateException
      {
         // Intentionally left empty
      }

      @Override
      public void checkServerTrusted(final X509Certificate[] chain, final String authType, final SSLEngine engine)
            throws CertificateException
      {
         // Intentionally left empty
      }
   }

   /**
    * The default hostname verifier. It always returns true when SSL validation is disabled.
    */
   private class TrustAllHostnameVerifier implements HostnameVerifier
   {
      @Override
      public boolean verify(final String hostname, final SSLSession session)
      {
         return !isSSLValidationEnabled;
      }
   }

}
