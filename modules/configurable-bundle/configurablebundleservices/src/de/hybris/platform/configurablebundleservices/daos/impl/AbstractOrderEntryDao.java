/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.configurablebundleservices.daos.impl;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNullStandardMessage;

import de.hybris.platform.core.PK;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.internal.dao.AbstractItemDao;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.servicelayer.type.TypeService;
import de.hybris.platform.configurablebundleservices.daos.OrderEntryDao;
import de.hybris.platform.configurablebundleservices.model.BundleTemplateModel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nonnull;

import org.springframework.beans.factory.annotation.Required;


/**
 * Abstract implementation of the {@link OrderEntryDao}.
 * 
 * @deprecated since 1905. Use entryGroupNumber instead of bundleNo
 */
@Deprecated(since = "1905", forRemoval = true)
public abstract class AbstractOrderEntryDao<O extends AbstractOrderModel, E extends AbstractOrderEntryModel> extends
		AbstractItemDao implements OrderEntryDao<O, E>
{

	private TypeService typeService;

	private static final String FIND_ENTRIES_BY_MASTERORDER_QUERY = "GET {" + AbstractOrderEntryModel._TYPECODE
			+ "} WHERE {" + AbstractOrderEntryModel.ORDER + "}=?masterAbstractOrder AND {"
			+ AbstractOrderEntryModel.ITEMTYPE + "}=?itemType";

	private static final String FIND_ENTRIES_BY_MASTERORDER_AND_BUNDLENO_QUERY = FIND_ENTRIES_BY_MASTERORDER_QUERY;

	private static final String FIND_ENTRIES_BY_MASTERORDER_AND_BUNDLENO_AND_TEMPLATE_QUERY = FIND_ENTRIES_BY_MASTERORDER_QUERY;

	private static final String FIND_ENTRIES_BY_MASTERORDER_AND_BUNDLENO_AND_PRODUCT_QUERY = FIND_ENTRIES_BY_MASTERORDER_QUERY
			+ " AND {" + AbstractOrderEntryModel.PRODUCT + "}=?product";

	@Override
	@Nonnull
	public List<E> findEntriesByMasterCartAndBundleNo(@Nonnull final O masterAbstractOrder, final int bundleNo)
	{
		validateParameterNotNullStandardMessage("masterAbstractOrder", masterAbstractOrder);

		final Map<String, Object> params = new HashMap<String, Object>();
		params.put("masterAbstractOrder", masterAbstractOrder);
		params.put("itemType", getItemType());

		final FlexibleSearchQuery flexibleSearchQuery = new FlexibleSearchQuery(FIND_ENTRIES_BY_MASTERORDER_AND_BUNDLENO_QUERY,
				params);

		final SearchResult<E> results = search(flexibleSearchQuery);

		return results.getResult();
	}

	@Override
	@Nonnull
	public List<E> findEntriesByMasterCartAndBundleNoAndTemplate(@Nonnull final O masterAbstractOrder, final int bundleNo,
																 @Nonnull final BundleTemplateModel bundleTemplate)
	{
		validateParameterNotNullStandardMessage("masterAbstractOrder", masterAbstractOrder);
		validateParameterNotNullStandardMessage("bundleTemplate", bundleTemplate);

		final Map<String, Object> params = new HashMap<String, Object>();
		params.put("masterAbstractOrder", masterAbstractOrder);
		params.put("itemType", getItemType());

		final FlexibleSearchQuery flexibleSearchQuery = new FlexibleSearchQuery(
				FIND_ENTRIES_BY_MASTERORDER_AND_BUNDLENO_AND_TEMPLATE_QUERY, params);

		final SearchResult<E> results = search(flexibleSearchQuery);

		return results.getResult();
	}

	@Override
	@Nonnull
	public List<E> findEntriesByMasterCartAndBundleNoAndProduct(@Nonnull final O masterAbstractOrder, final int bundleNo,
																@Nonnull final ProductModel product)
	{
		validateParameterNotNullStandardMessage("masterAbstractOrder", masterAbstractOrder);
		validateParameterNotNullStandardMessage("product", product);

		final Map<String, Object> params = new HashMap<String, Object>();
		params.put("masterAbstractOrder", masterAbstractOrder);
		params.put("product", product);
		params.put("itemType", getItemType());

		final FlexibleSearchQuery flexibleSearchQuery = new FlexibleSearchQuery(
				FIND_ENTRIES_BY_MASTERORDER_AND_BUNDLENO_AND_PRODUCT_QUERY, params);

		final SearchResult<E> results = search(flexibleSearchQuery);

		return results.getResult();
	}

	abstract public PK getItemType();

	@Required
	public void setTypeService(final TypeService typeService)
	{
		this.typeService = typeService;
	}

	protected TypeService getTypeService()
	{
		return typeService;
	}

}
