/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.customerinterestsservices.productinterest.daos;

import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.customerinterestsservices.model.ProductInterestModel;
import de.hybris.platform.notificationservices.enums.NotificationType;
import de.hybris.platform.servicelayer.internal.dao.GenericDao;
import de.hybris.platform.store.BaseStoreModel;

import java.util.List;
import java.util.Map;
import java.util.Optional;


/**
 * The dao of ProductInterest
 */
public interface ProductInterestDao extends GenericDao<ProductInterestModel>
{
	/**
	 * This method will get all the interests of the current customer
	 *
	 * @param customerModel
	 *           the customer of which all the ProductInterests to be found
	 * @param baseStore
	 *           the baseStore of the ProductInterest
	 * @param baseSite
	 *           the baseSite of the ProductInterest
	 * @return all the ProductInterests of the current customer
	 */
	List<ProductInterestModel> findProductInterestsByCustomer(final CustomerModel customerModel,
			final BaseStoreModel baseStore, final BaseSiteModel baseSite);

	/**
	 * This method is used to find the ProductInterestModel by its productModel, customerModel, notificationType and
	 * baseStore.
	 *
	 * @param productModel
	 *           the product of the ProductInterest
	 * @param customerModel
	 *           the customer of the ProductInterest
	 * @param notificationType
	 *           the notificationType of the ProductInterest
	 * @param baseStore
	 *           the baseStore of the ProductInterest
	 * @param baseSite
	 *           the baseSite of the ProductInterest
	 * @return ProductInterestModel if found and an empty Optional otherwise
	 */
	Optional<ProductInterestModel> findProductInterest(final ProductModel productModel, final CustomerModel customerModel,
			final NotificationType notificationType, final BaseStoreModel baseStore, final BaseSiteModel baseSite);

	/**
	 * This method is used to find expired and effective ProductInterestModels by its productModel, customerModel,
	 * notificationType and baseStore.
	 *
	 * @param customerModel
	 *           the customer of the ProductInterest
	 * @param baseStore
	 *           the baseStore of the ProductInterest
	 * @param baseSite
	 *           the baseSite of the ProductInterest
	 * @return ProductInterestModel if found and an empty Optional otherwise
	 */
	List<ProductInterestModel> findProductInterests(final CustomerModel customerModel, final BaseStoreModel baseStore,
			final BaseSiteModel baseSite);

	/**
	 * This method will get all the expired interests
	 *
	 * @return all the expired ProductInterests
	 */
	List<ProductInterestModel> findExpiredProductInterests();


	/**
	 *
	 * Find interests watched by current customer
	 *
	 * @param customerModel
	 *           the customer of the ProductInterest
	 * @param baseStore
	 *           the baseStore of the ProductInterest
	 * @param baseSite
	 *           the baseSite of the ProductInterest
	 * @param pageableData
	 *           the pagination data
	 *
	 * @return Map whose key is Product PK and value is the Map of NotificationType PK as key and creation time as value.
	 */
	Map<String, Map<String, String>> findProductsByCustomerInterests(final CustomerModel customerModel,
			final BaseStoreModel baseStore, final BaseSiteModel baseSite, final PageableData pageableData);

	/**
	 *
	 * Find all interests watched by current customer
	 *
	 * @param customerModel
	 *           the customer of the ProductInterest
	 * @param baseStore
	 *           the baseStore of the ProductInterest
	 * @param baseSite
	 *           the baseSite of the ProductInterest
	 *
	 * @return Map whose key is Product PK and value is the Map of NotificationType PK as key and creation time as value.
	 */
	Map<String, Map<String, String>> findProductInterestRelationsByCustomer(final CustomerModel customerModel,
			final BaseStoreModel baseStore, final BaseSiteModel baseSite);

	/**
	 * Find the total count of products watched by customer
	 *
	 * @param customerModel
	 *           the customer of the ProductInterest
	 * @param baseStore
	 *           the baseStore of the ProductInterest
	 * @param baseSite
	 *           the baseSite of the ProductInterest
	 * @param pageableData
	 *           the pagination data
	 * @return the total count of products watched by customer
	 */
	int findProductsCountByCustomerInterests(CustomerModel customerModel, BaseStoreModel baseStore, BaseSiteModel baseSite,
			PageableData pageableData);

	/**
	 * retrieve such ProductInterests whose notification type is BACK_IN_STOCK and not expired
	 *
	 * @return The list of ProductInterests to send BACK_IN_STOCK notification
	 *
	 */
	List<ProductInterestModel> findProductInterestsByNotificationType(final NotificationType notificationType);

}
