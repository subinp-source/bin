/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.odata2services.odata.monitoring;

import de.hybris.platform.inboundservices.model.InboundRequestMediaModel;
import de.hybris.platform.integrationservices.enums.HttpMethod;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * Parameter object for the {@link InboundRequestService}
 */
public class InboundRequestServiceParameter
{
	private final List<RequestBatchEntity> requests;
	private final List<ResponseChangeSetEntity> responses;
	private final List<InboundRequestMediaModel> medias;
	private final HttpMethod httpMethod;
	private final String userId;
	private final String sapPassport;

	private InboundRequestServiceParameter(final List<RequestBatchEntity> requests, final List<ResponseChangeSetEntity> responses,
			final List<InboundRequestMediaModel> medias, final HttpMethod httpMethod, final String userId, final String sapPassport)
	{
		this.requests = requests != null ? requests : new ArrayList<>();
		this.responses = responses != null ? responses : new ArrayList<>();
		this.medias = medias != null ? medias : new ArrayList<>();
		this.httpMethod = httpMethod;
		this.userId = userId != null ? userId : "";
		this.sapPassport = sapPassport;
	}

	public static InboundRequestServiceParameterBuilder inboundRequestServiceParameter()
	{
		return new InboundRequestServiceParameterBuilder();
	}

	/**
	 * Gets the information pertaining to the inbound requests extracted from the requests
	 * @return List of {@link RequestBatchEntity}
	 */
	public List<RequestBatchEntity> getRequests()
	{
		return requests;
	}

	/**
	 * Gets the information pertaining to the inbound requests extracted from the responses
	 * @return List of {@link ResponseChangeSetEntity}
	 */
	public List<ResponseChangeSetEntity> getResponses()
	{
		return responses;
	}

	/**
	 * Gets the request bodies for all entities contained in a request
	 * @return List of {@link InboundRequestMediaModel}
	 */
	public List<InboundRequestMediaModel> getMedias()
	{
		return medias;
	}

	/**
	 * Gets the HTTP method used to make the request
	 * @return the {@link HttpMethod} or null
	 */
	public HttpMethod getHttpMethod()
	{
		return httpMethod;
	}

	/**
	 * Gets the user ID that made the request
	 * @return User ID
	 */
	public String getUserId()
	{
		return userId;
	}

	/**
	 * Gets the SAP Passport from the request header
	 * @return SAP Passport
	 */
	public String getSapPassport()
	{
		return sapPassport;
	}

	@Override
	public boolean equals(final Object o)
	{
		if (this == o)
		{
			return true;
		}

		if (o == null || getClass() != o.getClass())
		{
			return false;
		}

		final InboundRequestServiceParameter that = (InboundRequestServiceParameter) o;

		return new EqualsBuilder()
				.append(requests, that.requests)
				.append(responses, that.responses)
				.append(medias, that.medias)
				.append(httpMethod, that.httpMethod)
				.append(userId, that.userId)
				.append(sapPassport, that.sapPassport)
				.isEquals();
	}

	@Override
	public int hashCode()
	{
		return new HashCodeBuilder(17, 37)
				.append(requests)
				.append(responses)
				.append(medias)
				.append(httpMethod)
				.append(userId)
				.append(sapPassport)
				.toHashCode();
	}

	public static class InboundRequestServiceParameterBuilder
	{
		private List<RequestBatchEntity> requests;
		private List<ResponseChangeSetEntity> responses;
		private List<InboundRequestMediaModel> medias;
		private HttpMethod httpMethod;
		private String userId;
		private String sapPassport;

		/**
		 * Sets the information pertaining to the inbound requests extracted from the requests
		 * @return the builder
		 */
		public InboundRequestServiceParameterBuilder withRequests(final List<RequestBatchEntity> requests)
		{
			this.requests = requests;
			return this;
		}

		/**
		 * Sets the information pertaining to the inbound requests extracted from the responses
		 * @return the builder
		 */
		public InboundRequestServiceParameterBuilder withResponses(final List<ResponseChangeSetEntity> responses)
		{
			this.responses = responses;
			return this;
		}

		/**
		 * Sets the request bodies for all entities contained in a request
		 * @return the builder
		 */
		public InboundRequestServiceParameterBuilder withMedias(final List<InboundRequestMediaModel> medias)
		{
			this.medias = medias;
			return this;
		}

		/**
		 * Sets the HTTP method used to make the request
		 * @return the builder
		 */
		public InboundRequestServiceParameterBuilder withHttpMethod(final HttpMethod httpMethod)
		{
			this.httpMethod = httpMethod;
			return this;
		}

		/**
		 * Sets the user ID that made the request
		 * @return the builder
		 */
		public InboundRequestServiceParameterBuilder withUserId(final String userId)
		{
			this.userId = userId;
			return this;
		}

		/**
		 * Sets the SAP Passport that came in with the request's SAP-PASSPORT header
		 * @return the builder
		 */
		public InboundRequestServiceParameterBuilder withSapPassport(final String sapPassport)
		{
			this.sapPassport = sapPassport;
			return this;
		}

		public InboundRequestServiceParameter build()
		{
			return new InboundRequestServiceParameter(
					requests,
					responses,
					medias,
					httpMethod,
					userId,
					sapPassport);
		}
	}
}
