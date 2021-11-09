/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.accountsummaryaddon.document.criteria;

import de.hybris.platform.accountsummaryaddon.model.B2BDocumentTypeModel;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;


/**
 *
 */
public class DocumentTypeCriteria extends DefaultCriteria
{
	private static final Logger LOG = Logger.getLogger(DocumentTypeCriteria.class);

	protected final String DOCUMENT_TYPE_QUERY = "{" + B2BDocumentTypeModel._TYPECODE + ":" + B2BDocumentTypeModel.CODE + "} = %s";

	private String b2bDocumentTypeCode;


	public DocumentTypeCriteria(final String filterByKey)
	{
		this(filterByKey, StringUtils.EMPTY, StringUtils.EMPTY);
	}

	public DocumentTypeCriteria(final String filterByKey, final String documentTypeCode, final String documentStatus)
	{
		super(filterByKey, documentStatus);
		this.setB2bDocumentTypeCode(documentTypeCode);
	}


	/**
	 * @return the b2bDocumentTypeCode
	 */
	public String getB2bDocumentTypeCode()
	{
		return b2bDocumentTypeCode;
	}

	/**
	 * @param b2bDocumentTypeCode
	 *           the b2bDocumentTypeCode to set
	 */
	protected final void setB2bDocumentTypeCode(final String b2bDocumentTypeCode)
	{
		this.b2bDocumentTypeCode = b2bDocumentTypeCode;
	}

	@Override
	public void setCriteriaValues(final FilterByCriteriaData filterByCriteriaData)
	{
		super.setCriteriaValues(filterByCriteriaData);
		setB2bDocumentTypeCode(filterByCriteriaData.getDocumentTypeCode());
	}

	@Override
	public void populateCriteriaQueryAndParamsMap(final List<String> whereQueryList, final Map<String, Object> queryParamsMap)
	{
		if (StringUtils.isNotBlank(getB2bDocumentTypeCode()))
		{
			final String formattedQuery = String.format(DOCUMENT_TYPE_QUERY, "?b2bDocumentTypeCode");
			if (LOG.isDebugEnabled())
			{
				LOG.debug(String.format(QUERY_CRITERIA, formattedQuery, getB2bDocumentTypeCode()));
			}
			whereQueryList.add(formattedQuery);
			queryParamsMap.put("b2bDocumentTypeCode", getB2bDocumentTypeCode());
		}

		super.populateCriteriaQueryAndParamsMap(whereQueryList, queryParamsMap);
	}

}
