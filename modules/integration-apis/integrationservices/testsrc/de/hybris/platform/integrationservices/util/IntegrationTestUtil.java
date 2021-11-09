/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.integrationservices.util;

import static org.assertj.core.api.Assertions.assertThat;

import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.catalog.model.ProductFeatureModel;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.impex.jalo.ImpExException;
import de.hybris.platform.servicelayer.ServicelayerTestLogic;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;

import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.slf4j.Logger;
import org.springframework.context.ApplicationContext;

import spock.util.concurrent.PollingConditions;

/**
 * Test utilities.
 */
public class IntegrationTestUtil extends ServicelayerTestLogic
{
	private static final Logger LOG = Log.getLogger(IntegrationTestUtil.class);
	private static final String SELECT_TEMPLATE = "SELECT {%s} FROM {%s}";
	private static final int TIMEOUT_SEC = 10;

	/**
	 * Imports impex into the platform
	 *
	 * @param content IMPEX content to be imported.
	 * @throws ImpExException when import failed. Most likely the IMPEX is invalid.
	 */
	public static void importCsv(final String content) throws ImpExException
	{
		importStream(IOUtils.toInputStream(content, StandardCharsets.UTF_8), StandardCharsets.UTF_8.name(), randomName(".impex"));
	}

	/**
	 * Imports impex into the platform
	 *
	 * @param lines IMPEX lines to be imported.
	 * @throws ImpExException when import failed. Most likely the IMPEX is invalid.
	 */
	public static void importImpEx(final String... lines) throws ImpExException
	{
		final String content = String.join(System.lineSeparator(), lines);
		importCsv(content);
	}

	/**
	 * Imports a CatalogVersion and Catalog
	 *
	 * @param version CatalogVersion version value
	 * @param catalog Catalog id
	 * @param active  Boolean set to the active attribute
	 * @return The persisted CatalogVersionModel
	 * @throws ImpExException when import failed. Most likely the IMPEX is invalid.
	 */
	public static CatalogVersionModel importCatalogVersion(final String version, final String catalog, final boolean active)
			throws ImpExException
	{
		createCatalogWithId(catalog);
		importImpEx(
				"INSERT_UPDATE CatalogVersion ; catalog(id)[unique = true]; version[unique = true]; active;",
				String.format("; %s; %s; %b", catalog, version, active)
		);
		return getCatalogVersionByVersionCatalogIdAndActive(version, catalog, active);
	}

	/**
	 * Imports a Catalog
	 *
	 * @param id Catalog id
	 * @return The persisted CatalogModel
	 * @throws ImpExException when import failed. Most likely the IMPEX is invalid.
	 */
	public static CatalogModel createCatalogWithId(final String id) throws ImpExException
	{
		importImpEx(
				"INSERT_UPDATE Catalog ; id[unique = true]",
				String.format("; %s", id));
		return getCatalogById(id);
	}

	private static CatalogModel getCatalogById(final String id)
	{
		return IntegrationTestUtil.findAny(CatalogModel.class,
				it -> it.getId().equals(id))
		                          .orElse(null);
	}

	private static CatalogVersionModel getCatalogVersionByVersionCatalogIdAndActive(final String version, final String catalogId,
	                                                                                final boolean active)
	{
		return IntegrationTestUtil.findAny(CatalogVersionModel.class,
				it -> it.getVersion().equals(version) && it.getCatalog().getId().equals(catalogId) && it.getActive() == active)
		                          .orElse(null);
	}

	public static void assertModelDoesNotExist(final ItemModel model)
	{
		assertThat(getModelByExample(model)).isNull();
	}

	public static <T> T assertModelExists(final T sample)
	{
		final T persisted = getModelByExample(sample);
		assertThat(persisted).isNotNull();
		return persisted;
	}

	public static <T> T getModelByExample(final T sample)
	{
		final FlexibleSearchService flexibleSearchService = getFlexibleSearch();
		try
		{
			return flexibleSearchService.getModelByExample(sample);
		}
		catch (final ModelNotFoundException e)
		{
			return null;
		}
	}

	/**
	 * Refreshes model to its current state in the persistent storage.
	 *
	 * @param item an item model to get fresh state for. WARNING: this method has a side-effect and updates this parameter to the
	 *             fresh state.
	 * @return fresh state of the specified model in the persistent storage.
	 */
	public static <T extends ItemModel> T refresh(final T item)
	{
		getModelService().refresh(item);
		return item;
	}

	/**
	 * Retrieves an item model matching the specified condition.
	 *
	 * @param type      type of the item to find
	 * @param condition a condition the item should match
	 * @return an optional containing first found matching item (it's possible more than one item matches the condition)
	 * or an empty optional, if there are no items matching the condition in the persistent storage.
	 */
	public static <T extends ItemModel> Optional<T> findAny(final Class<T> type, final Predicate<T> condition)
	{
		return findAll(type).stream()
		                    .filter(condition)
		                    .findAny();
	}

	/**
	 * Retrieves all model items matching the specified condition in the persistent storage.
	 *
	 * @param type      type of the items to search for.
	 * @param condition condition the searched items should match.
	 * @return a collection of all matching items or an empty collection, if the persistent storage does not have items matching the
	 * specified condition.
	 */
	public static <T extends ItemModel> Collection<T> findAll(final Class<T> type, final Predicate<T> condition)
	{
		return findAll(type).stream()
		                    .filter(condition)
		                    .collect(Collectors.toList());
	}

	/**
	 * Retrieves all model items existing in the persistent storage.
	 *
	 * @param type type of the items to be found.
	 * @return a collection of all items or an empty collection, if there are no items in the persistent storage.
	 */
	public static <T extends ItemModel> Collection<T> findAll(final Class<T> type)
	{
		try
		{
			final String pk = staticStringFieldValue(type, "PK");
			final String typeCode = staticStringFieldValue(type, "_TYPECODE");
			final String query = String.format(SELECT_TEMPLATE, pk, typeCode);
			return findAll(query).stream()
			                     .map(type::cast)
			                     .collect(Collectors.toList());
		}
		catch (final NoSuchFieldException | IllegalAccessException e)
		{
			throw new IllegalArgumentException(type + " does not have public static PK/_TYPECODE fields", e);
		}
	}

	/**
	 * Removes all model items existing in the persistent storage.
	 *
	 * @param type type of the items to be deleted from the persistent storage.
	 * @return a collection of all deleted items or an empty collection, if there are no items in the persistent storage.
	 */
	public static <T extends ItemModel> Collection<T> removeAll(final Class<T> type)
	{
		final Collection<T> allItems = findAll(type);
		removeAllSafely(allItems);
		return allItems;
	}

	private static void removeAllSafely(final Collection<?> items)
	{
		try
		{
			getModelService().removeAll(items);
		}
		catch (final RuntimeException e)
		{
			LOG.warn("Failed to remove some items", e);
		}
	}

	/**
	 * Removes  a specific instance of ItemModel
	 *
	 * @param model item model to remove
	 */
	public static <T> void remove(final T model)
	{
		getModelService().refresh(model);
		getModelService().remove(model);
	}

	/**
	 * Removes items matching the predicate. This method catches possible exceptions thrown in the process of item removal and
	 * suppresses them.
	 *
	 * @param type      type of the items to remove
	 * @param predicate condition the items to be removed should match
	 */
	public static <T extends ItemModel> void removeSafely(final Class<T> type, final Predicate<T> predicate)
	{
		try
		{
			remove(type, predicate);
		}
		catch (final RuntimeException e)
		{
			LOG.warn("Failed to remove instances of {} matching a predicate", type, e);
		}
	}

	public static <T extends ItemModel> void remove(final Class<T> type, final Predicate<T> predicate)
	{
		findAll(type).stream()
		             .map(type::cast)
		             .filter(predicate)
		             .forEach(IntegrationTestUtil::remove);
	}

	private static String randomName(final String suffix)
	{
		return RandomStringUtils.randomAlphanumeric(10) + suffix;
	}

	private static String staticStringFieldValue(final Class<? extends ItemModel> type, final String fieldName)
			throws NoSuchFieldException, IllegalAccessException
	{
		return (String) type.getField(fieldName).get(null);
	}

	/**
	 * Executes the specified Flexible Search query.
	 *
	 * @param query query to execute
	 * @return result of the query. It may be an empty collection, if the query did not find matching data.
	 */
	private static Collection<Object> findAll(final String query)
	{
		return getFlexibleSearch().search(query).getResult();
	}

	private static FlexibleSearchService getFlexibleSearch()
	{
		return getService("flexibleSearchService", FlexibleSearchService.class);
	}

	private static ModelService getModelService()
	{
		return getService("modelService", ModelService.class);
	}

	static <T> T getService(final String name, final Class<T> type)
	{
		final ApplicationContext context = Registry.getApplicationContext();
		return context.getBean(name, type);
	}

	public static PollingConditions condition()
	{
		final PollingConditions condition = new PollingConditions();
		condition.setTimeout(TIMEOUT_SEC);
		return condition;
	}

	public static Object getClassificationAttributeValue(final String productCode, final String attributeName)
	{
		return findAny(ProductFeatureModel.class, f -> Objects.equals(f.getProduct().getCode(), productCode)
				&& Objects.equals(f.getClassificationAttributeAssignment().getClassificationAttribute().getCode(), attributeName))
				.map(ProductFeatureModel::getValue)
				.orElse(null);
	}

	public static List<Object> getClassificationAttributeCollectionValue(final String productCode, final String attributeName)
	{
		return findAll(ProductFeatureModel.class).stream()
		                                         .map(ProductFeatureModel.class::cast)
		                                         .filter(f -> Objects.equals(f.getProduct().getCode(), productCode)
				                                         && Objects.equals(f.getClassificationAttributeAssignment()
				                                                            .getClassificationAttribute()
				                                                            .getCode(), attributeName))
		                                         .sorted(Comparator.comparingInt(ProductFeatureModel::getValuePosition))
		                                         .map(ProductFeatureModel::getValue)
		                                         .collect(Collectors.toList());
	}
}
