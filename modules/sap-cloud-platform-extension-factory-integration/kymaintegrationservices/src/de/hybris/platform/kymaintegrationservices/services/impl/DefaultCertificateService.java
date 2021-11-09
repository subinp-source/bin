/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.kymaintegrationservices.services.impl;


import static de.hybris.platform.kymaintegrationservices.utils.KymaApiExportHelper.API_REG_SERVICE_ID;
import static de.hybris.platform.kymaintegrationservices.utils.KymaApiExportHelper.DEFAULT_API_REG_SERVICE_ID;
import static de.hybris.platform.kymaintegrationservices.utils.KymaApiExportHelper.DEFAULT_GETINFO_DESTINATION_ID;
import static de.hybris.platform.kymaintegrationservices.utils.KymaApiExportHelper.DEFAULT_RENEWAL_SERVICE_ID;
import static de.hybris.platform.kymaintegrationservices.utils.KymaApiExportHelper.GETINFO_DESTINATION_ID_KEY;
import static de.hybris.platform.kymaintegrationservices.utils.KymaApiExportHelper.RENEWAL_SERVICE_ID;
import static de.hybris.platform.kymaintegrationservices.utils.KymaEventExportUtils.DEFAULT_EVENTS_SERVICE_ID;
import static de.hybris.platform.kymaintegrationservices.utils.KymaEventExportUtils.EVENTS_SERVICE_ID;
import static java.nio.charset.StandardCharsets.UTF_8;

import de.hybris.platform.apiregistryservices.exceptions.CredentialException;
import de.hybris.platform.apiregistryservices.model.AbstractDestinationModel;
import de.hybris.platform.apiregistryservices.model.ConsumedCertificateCredentialModel;
import de.hybris.platform.apiregistryservices.model.ConsumedDestinationModel;
import de.hybris.platform.apiregistryservices.model.DestinationTargetModel;
import de.hybris.platform.apiregistryservices.services.DestinationService;
import de.hybris.platform.apiregistryservices.services.DestinationTargetService;
import de.hybris.platform.apiregistryservices.utils.SecurityUtils;
import de.hybris.platform.kymaintegrationservices.dto.CertificateRenewalData;
import de.hybris.platform.kymaintegrationservices.dto.CertificateRequestPayload;
import de.hybris.platform.kymaintegrationservices.dto.CertificateResponsePayload;
import de.hybris.platform.kymaintegrationservices.dto.KymaApiData;
import de.hybris.platform.kymaintegrationservices.dto.KymaCertificateCreation;
import de.hybris.platform.kymaintegrationservices.dto.KymaInfoData;
import de.hybris.platform.kymaintegrationservices.dto.KymaSecurityData;
import de.hybris.platform.kymaintegrationservices.services.CertificateService;
import static de.hybris.platform.kymaintegrationservices.utils.KymaConfigurationUtils.getTargetName;
import de.hybris.platform.kymaintegrationservices.utils.RestTemplateWrapper;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.task.TaskModel;
import de.hybris.platform.task.TaskService;
import de.hybris.platform.util.Config;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.SignatureException;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.Base64;
import java.util.Date;
import java.util.List;

import javax.security.auth.x500.X500Principal;
import javax.xml.bind.DatatypeConverter;

import org.apache.commons.lang.StringUtils;
import org.bouncycastle.jcajce.provider.asymmetric.rsa.BCRSAPublicKey;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.bouncycastle.pkcs.PKCS10CertificationRequest;
import org.bouncycastle.pkcs.PKCS10CertificationRequestBuilder;
import org.bouncycastle.pkcs.jcajce.JcaPKCS10CertificationRequestBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;


/**
 * Kyma specific implementation of {@link CertificateService}.
 */
public class DefaultCertificateService implements CertificateService
{
	private static final int BASE64_OUTPUT_LINE_LENGTH = 64;
	private static final int KEY_VERIFICATION_RANDOM_BYTE_ARRAY_SIZE = 10000;
	private static final Logger LOG = LoggerFactory.getLogger(DefaultCertificateService.class);
	private static final String CERTIFICATE_SIGN_ALGORITHM = "apiregistryservices.certificate.sign.algorithm";
	private static final String CERTIFICATE_EXPIRY_RATIO = "kymaintegrationservices.certificate.expiry.ratio";
	public static final String CERTIFICATE_HEADER = "-----BEGIN NEW CERTIFICATE REQUEST-----\n";
	public static final String CERTIFICATE_FOOTER = "\n-----END NEW CERTIFICATE REQUEST-----\n";
	public static final String CERTIFICATE_KEY_ALGORITHM = "kyma.certificate.key.algorithm";
	public static final String CERTIFICATE_KEY_SUBJECT = "kyma.certificate.key.subject";
	private ModelService modelService;
	private TaskService taskService;
	private DestinationService<AbstractDestinationModel> destinationService;
	private RestTemplate restTemplate;
	private DestinationTargetService destinationTargetService;
	private RestTemplateWrapper restTemplateWrapper;

	/**
	 * Default kyma implementation
	 *
	 * @see CertificateService#retrieveCertificate(URI, ConsumedCertificateCredentialModel)
	 * @param certificateUrl
	 *           Url to retrieve client certificate.
	 * @param certificationCredential
	 *           Credential to be updated.
	 * @return updatedModel
	 * @throws CredentialException
	 *            in case when failed to generate PrivateKey, CSR, Certificate.
	 */
	@Override
	public ConsumedCertificateCredentialModel retrieveCertificate(final URI certificateUrl,
			final ConsumedCertificateCredentialModel certificationCredential) throws CredentialException
	{
		byte[] certBytes = null;

		try
		{
			final KymaSecurityData kymaSecurityData = getRestTemplate().getForObject(certificateUrl, KymaSecurityData.class);

			final KymaApiData apiData = kymaSecurityData.getApi();
			final KymaCertificateCreation keyData = kymaSecurityData.getCertificate();

			final X509Certificate cert = getX509Certificate(certificationCredential, kymaSecurityData.getCsrUrl(), keyData);

			if (LOG.isInfoEnabled())
			{
				LOG.info(String.format("%s Certificate retrieved", getTargetName()));
			}
			scheduleCertificateRenewalTask(certificationCredential, cert);

			certBytes = cert.getTBSCertificate();
			return updateAllConsumedDestinationCredentialsAndGetInfoUrl(certificationCredential, apiData);
		}
		catch (final RestClientException e)
		{
			final String errorMessage = String.format(
					"Failed to retrieve certificate metadata with URL: [{%s}]. Please make sure if token is still valid.",
					certificateUrl.getHost());
			LOG.error(errorMessage, e);
			throw new CredentialException(errorMessage, e);
		}
		catch (final ModelSavingException e)
		{
			final String errorMessage = String.format(
					"Failed to retrieve certificate metadata with URL: [{%s}]. Error while saving certificate and destination URLs",
					certificateUrl.getHost());
			LOG.error(e.getMessage(), e);
			throw new CredentialException(errorMessage, e);
		}
		catch (final CertificateEncodingException e)
		{
			final String errorMessage = String.format(
					"Failed to retrieve certificate metadata with URL: [{%s}]. %s. Error while converting the certificate to byte array",
					certificateUrl.getHost(), e.getMessage());
			LOG.error(e.getMessage(), e);
			throw new CredentialException(errorMessage, e);
		}
		finally
		{
			if(certBytes != null)
			{
				Arrays.fill(certBytes, (byte) 0);
			}
		}
	}

	@Override
	public ConsumedCertificateCredentialModel renewCertificate(final ConsumedCertificateCredentialModel certificationCredential)
			throws CredentialException
	{
		LOG.info("Renewing the certificate now");

		final DestinationTargetModel destinationTarget = getDestinationTargetService().getDestinationTargetByCredentialId(certificationCredential.getId());

		final AbstractDestinationModel getinfoDestination = getDestinationService().getDestinationByIdAndByDestinationTargetId(Config.getParameter(GETINFO_DESTINATION_ID_KEY), destinationTarget.getId());

		final KymaCertificateCreation keyData = getCertificateCreationData(getinfoDestination.getUrl(),
				getRestTemplateWrapper().getRestTemplate(getinfoDestination.getCredential()));

		final AbstractDestinationModel renewalDestination = getDestinationService().getDestinationByIdAndByDestinationTargetId(Config.getParameter(RENEWAL_SERVICE_ID), destinationTarget.getId());

		final X509Certificate cert = getX509Certificate(certificationCredential, renewalDestination.getUrl(), keyData);

		scheduleCertificateRenewalTask(certificationCredential, cert);
		getModelService().save(certificationCredential);

		return certificationCredential;
	}

	protected X509Certificate getX509Certificate(final ConsumedCertificateCredentialModel certificationCredential, final String csrUrl, final KymaCertificateCreation keyData) throws CredentialException {
		final KeyPair keyPair = generateKeyPair(keyData.getKeyAlgorithm());

		certificationCredential.setPrivateKey(encodeToBase64(keyPair.getPrivate().getEncoded()));

		final String certificateText;

		if (StringUtils.isEmpty(certificationCredential.getCertificateData()))
		{
			certificateText = getCertificate(keyPair, keyData.getSubject(), csrUrl, getRestTemplate());
		}
		else
		{
			certificateText = getCertificate(keyPair, keyData.getSubject(), csrUrl,
					getRestTemplateWrapper().getRestTemplate(certificationCredential));
		}

		final X509Certificate cert = verifyCredential(certificateText, keyPair, keyData);
		certificationCredential.setCertificateData(certificateText);
		return cert;
	}

	protected void scheduleCertificateRenewalTask(final ConsumedCertificateCredentialModel consumedCertificateCredential, final X509Certificate cert)
	{
		final long expiryDateTimeStamp = cert.getNotAfter().getTime();
		final long startDateTimeStamp = cert.getNotBefore().getTime();
		final long aheadTime = (expiryDateTimeStamp - startDateTimeStamp) / Config.getInt(CERTIFICATE_EXPIRY_RATIO, 6) ;
		final long nextExecutionDateInMillis = expiryDateTimeStamp - aheadTime ;
		final Date nextExecutionDate = new Date(nextExecutionDateInMillis);
		final CertificateRenewalData certificateRenewalData = new CertificateRenewalData();
		certificateRenewalData.setAheadTime(aheadTime);
		certificateRenewalData.setExpiryDate(cert.getNotAfter());

		final TaskModel task = getModelService().create(TaskModel.class);
		task.setRunnerBean("kymaCertificateRenewalTaskRunner");
		task.setExecutionDate(nextExecutionDate);
		task.setContext(certificateRenewalData);
		task.setContextItem(consumedCertificateCredential);
		getTaskService().scheduleTask(task);
	}

	protected X509Certificate verifyCredential(final String certificateText, final KeyPair keyPair, final KymaCertificateCreation keyData)
			throws CredentialException
	{
		final byte[] certBytes = DatatypeConverter.parseBase64Binary(certificateText);

		try
		{
			final X509Certificate cert = SecurityUtils.generateCertificateFromDER(certBytes);

			verifyKeyAlgorithm(cert, keyData);
			verifySubject(cert, keyData);
			verifySignatureAlgorithm(cert);
			verifyKeyPairs(cert, keyPair);
			return cert;
		}
		catch (final CertificateException | NoSuchAlgorithmException | SignatureException | InvalidKeyException e)
		{
			final String errorMessage = String.format("Credential verification is failed. %s", e.getMessage());
			LOG.error(errorMessage, e);
			throw new CredentialException(errorMessage, e);
		}
		finally
		{
			Arrays.fill(certBytes, (byte) 0);
		}
	}

	protected void verifySubject(final X509Certificate cert, final KymaCertificateCreation keyData) throws CertificateException
	{
		final List<String> expectedSubject = Arrays.asList(keyData.getSubject().split("\\s*,\\s*"));
		final List<String> certificateSubject = Arrays.asList(cert.getSubjectX500Principal().getName().split("\\s*,\\s*"));

		if (!expectedSubject.containsAll(certificateSubject))
		{
			throw new CertificateException("Certificate subject is not valid");
		}
	}

	protected void verifySignatureAlgorithm(final X509Certificate cert) throws CertificateException
	{
		if (!Config.getString(CERTIFICATE_SIGN_ALGORITHM, "SHA256WithRSA").equalsIgnoreCase(cert.getSigAlgName()))
		{
			throw new CertificateException("Certificate signature algorithm is not valid");
		}
	}

	protected void verifyKeyAlgorithm(final X509Certificate cert, final KymaCertificateCreation keyData)
			throws CertificateException
	{
		final String certificateKeyAlgorithm = cert.getPublicKey().getAlgorithm()
				.concat(String.valueOf(((BCRSAPublicKey) cert.getPublicKey()).getModulus().bitLength()));

		if (!certificateKeyAlgorithm.equalsIgnoreCase(keyData.getKeyAlgorithm()))
		{
			throw new CertificateException("Public key algorithm is not valid");
		}
	}

	protected void verifyKeyPairs(final X509Certificate cert, final KeyPair keyPair)
			throws NoSuchAlgorithmException, InvalidKeyException, SignatureException, CertificateException
	{
		final byte[] randomBytes = new byte[KEY_VERIFICATION_RANDOM_BYTE_ARRAY_SIZE];
		final SecureRandom secureRandom = new SecureRandom();
		secureRandom.nextBytes(randomBytes);


		final Signature sig = Signature.getInstance(Config.getString(CERTIFICATE_SIGN_ALGORITHM, "SHA256withRSA"));
		sig.initSign(keyPair.getPrivate());
		sig.update(randomBytes);
		final byte[] signature = sig.sign();

		sig.initVerify(cert.getPublicKey());
		sig.update(randomBytes);

		if (!sig.verify(signature))
		{
			throw new CertificateException("Public key and private key don't match");
		}
	}

	protected ConsumedCertificateCredentialModel updateAllConsumedDestinationCredentialsAndGetInfoUrl(final ConsumedCertificateCredentialModel certificationCredential, final KymaApiData apiData ) throws CredentialException
	{
		final String getInfoDestinationId = Config.getString(GETINFO_DESTINATION_ID_KEY, DEFAULT_GETINFO_DESTINATION_ID);
		updateConsumedDestinationUrl(getInfoDestinationId, apiData.getInfoUrl(), certificationCredential);
        updateAllConsumedDestinationCredentials(certificationCredential);
		return certificationCredential;
	}

	protected void updateAllConsumedDestinationCredentials(final ConsumedCertificateCredentialModel certificationCredential) throws CredentialException
	{
        getModelService().save(certificationCredential);

        final String getInfoDestinationId = Config.getString(GETINFO_DESTINATION_ID_KEY, DEFAULT_GETINFO_DESTINATION_ID);
        updateConsumedDestinationCredentials(getInfoDestinationId, certificationCredential);

		final String eventsDestinationId = Config.getString(EVENTS_SERVICE_ID, DEFAULT_EVENTS_SERVICE_ID);
		updateConsumedDestinationCredentials(eventsDestinationId, certificationCredential);

		final String renewalDestinationId = Config.getString(RENEWAL_SERVICE_ID, DEFAULT_RENEWAL_SERVICE_ID);
		updateConsumedDestinationCredentials(renewalDestinationId, certificationCredential);

		final String servicesDestinationId = Config.getString(API_REG_SERVICE_ID, DEFAULT_API_REG_SERVICE_ID);
		updateConsumedDestinationCredentials(servicesDestinationId, certificationCredential);
	}

	protected void updateConsumedDestinationCredentials(final String consumedDestinationId, final ConsumedCertificateCredentialModel  certificationCredential) throws CredentialException
	{
		final ConsumedDestinationModel consumedDestination = getConsumedDestination(consumedDestinationId, certificationCredential);
		if (consumedDestination.getCredential() != null && consumedDestination.getCredential().getId().equals(certificationCredential.getId()))
		{
			consumedDestination.setCredential(certificationCredential);
		}

		getModelService().save(consumedDestination);
	}

	protected void updateConsumedDestinationUrl(final String consumedDestinationId, final String url, final ConsumedCertificateCredentialModel  certificationCredential) throws CredentialException
	{
		final ConsumedDestinationModel consumedDestination = getConsumedDestination(consumedDestinationId, certificationCredential);

		if (consumedDestination.getCredential() != null && consumedDestination.getCredential().getId().equals(certificationCredential.getId()))
		{
			consumedDestination.setUrl(url);
		}

		getModelService().saveAll(certificationCredential, consumedDestination);
	}

	protected ConsumedDestinationModel getConsumedDestination(final String consumedDestinationId, final ConsumedCertificateCredentialModel  certificationCredential) throws CredentialException
	{
		final DestinationTargetModel destinationTarget = getDestinationTargetService()
				.getDestinationTargetByCredentialId(certificationCredential.getId());

		final ConsumedDestinationModel consumedDestination = (ConsumedDestinationModel) getDestinationService()
				.getDestinationByIdAndByDestinationTargetId(consumedDestinationId, destinationTarget.getId());

		if (consumedDestination == null)
		{
			final String errorMessage = String.format("Missing Consumed Destination with id : [{%s}]",
					consumedDestinationId);
			LOG.error(errorMessage);
			throw new CredentialException(errorMessage);
		}

		return consumedDestination;
	}

	protected String encodeToBase64(final byte[] toBeEncoded)
	{
		final byte[] lineSeparator = new byte[]
		{ 13, 10 };
		return Base64.getMimeEncoder(BASE64_OUTPUT_LINE_LENGTH, lineSeparator).encodeToString(toBeEncoded);
	}

	protected KymaCertificateCreation getCertificateCreationData(final String getinfoUrlString, final RestTemplate template)
			throws CredentialException
	{
		final URI getinfoUrl;
		try
		{
			getinfoUrl = new URI(getinfoUrlString);
		}
		catch (final URISyntaxException e)
		{
			throw new CredentialException("Invalid getinfo url retrieved", e);
		}

		final ResponseEntity<KymaInfoData> response;
		try
		{
			response = template.getForEntity(getinfoUrl,
					KymaInfoData.class);

		}
		catch (final RestClientException e)
		{
			final String errorMessage = String.format("Failed to retrieve certificate creation data with URL: [{%s}]", getinfoUrl.getHost());
			LOG.error(errorMessage, e);
			throw new CredentialException(errorMessage, e);
		}
		if(response.getBody() == null)
		{
			final String errorMessage = String.format("Invalid response from getinfo endpoint: [{%s}]", getinfoUrl.getHost());
			LOG.error(errorMessage);
			throw new CredentialException(errorMessage);
		}
		return response.getBody().getCertificate();
	}


	protected String getCertificate(final KeyPair keyPair, final String keySubject, final String csrUrlString, final RestTemplate template)
			throws CredentialException
	{
		final URI csrUrl;
		try
		{
			csrUrl = new URI(csrUrlString);
		}
		catch (final URISyntaxException e)
		{
			throw new CredentialException("Invalid CSR url retrieved", e);
		}

		final ResponseEntity<CertificateResponsePayload> response;
		try
		{
			response = template.postForEntity(csrUrl, generateCertificateRequest(keyPair, keySubject),
					CertificateResponsePayload.class);

		}
		catch (final RestClientException e)
		{
			final String errorMessage = String.format("Failed to retrieve certificate with URL: [{%s}]", csrUrl.getHost());
			LOG.error(errorMessage, e);
			throw new CredentialException(errorMessage, e);
		}
		return response.getBody().getCrt();
	}

	protected CertificateRequestPayload generateCertificateRequest(final KeyPair keyPair, final String keySubject)
			throws CredentialException
	{
		final CertificateRequestPayload request = new CertificateRequestPayload();
		final byte[] csr = generateCSR(keySubject, keyPair);

		request.setCsr(encodeToBase64(csr));
		Arrays.fill(csr, (byte) 0);
		return request;
	}

	protected byte[] generateCSR(final String rdnAttributes, final KeyPair keypair) throws CredentialException
	{
		try
		{
			final ContentSigner signGen = new JcaContentSignerBuilder(Config.getString(CERTIFICATE_SIGN_ALGORITHM, "SHA256WithRSA"))
					.build(keypair.getPrivate());

			final X500Principal x500Principal = new X500Principal(rdnAttributes);

			final PKCS10CertificationRequestBuilder csr = new JcaPKCS10CertificationRequestBuilder(x500Principal,
					keypair.getPublic());

			final PKCS10CertificationRequest request = csr.build(signGen);

			final ByteArrayOutputStream out = new ByteArrayOutputStream();
			out.write(CERTIFICATE_HEADER.getBytes(UTF_8));
			out.write(Base64.getEncoder().encode(request.getEncoded()));
			out.write(CERTIFICATE_FOOTER.getBytes(UTF_8));
			out.close();
			return out.toByteArray();
		}
		catch (final OperatorCreationException | IOException e)
		{
			LOG.error("Cannot create certificate", e);
			throw new CredentialException("Cannot create certificate", e);
		}
	}

	protected KeyPair generateKeyPair(final String keyAlgorithm) throws CredentialException
	{
		final String[] keyAlg = keyAlgorithm.split("(?<=\\D)(?=\\d)|(?<=\\d)(?=\\D)");

		final KeyPairGenerator keyPairGenerator;
		try
		{
			keyPairGenerator = KeyPairGenerator.getInstance(keyAlg[0]);
			keyPairGenerator.initialize(Integer.parseInt(keyAlg[1]));
			return keyPairGenerator.generateKeyPair();
		}
		catch (final NoSuchAlgorithmException e)
		{
			LOG.error("Cannot generate Key Pair", e);
			throw new CredentialException("Cannot generate Key Pair", e);
		}
	}

	protected ModelService getModelService()
	{
		return modelService;
	}

	@Required
	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
	}

	protected TaskService getTaskService()
	{
		return taskService;
	}

	@Required
	public void setTaskService(final TaskService taskService)
	{
		this.taskService = taskService;
	}

	protected DestinationService<AbstractDestinationModel> getDestinationService()
	{
		return destinationService;
	}

	@Required
	public void setDestinationService(final DestinationService<AbstractDestinationModel> destinationService)
	{
		this.destinationService = destinationService;
	}

	protected RestTemplate getRestTemplate()
	{
		return restTemplate;
	}

	@Required
	public void setRestTemplate(final RestTemplate restTemplate)
	{
		this.restTemplate = restTemplate;
	}

	protected DestinationTargetService getDestinationTargetService()
	{
		return destinationTargetService;
	}

	@Required
	public void setDestinationTargetService(final DestinationTargetService destinationTargetService)
	{
		this.destinationTargetService = destinationTargetService;
	}

	protected RestTemplateWrapper getRestTemplateWrapper()
	{
		return restTemplateWrapper;
	}

   @Required
	public void setRestTemplateWrapper(final RestTemplateWrapper restTemplateWrapper)
	{
		this.restTemplateWrapper = restTemplateWrapper;
	}
}
