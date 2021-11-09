/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package ydocumentcartpackage.persistence.polyglot.repository.documentcart;

import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.core.Constants;
import de.hybris.platform.core.model.order.CartEntryModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.product.UnitModel;
import de.hybris.platform.core.order.EntryGroup;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.flexiblesearch.QueryOptions;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.persistence.flexiblesearch.polyglot.PolyglotPersistenceFlexibleSearchSupport;
import de.hybris.platform.persistence.polyglot.ItemStateRepository;
import de.hybris.platform.persistence.polyglot.PolyglotPersistence;
import de.hybris.platform.persistence.polyglot.TypeInfoFactory;
import de.hybris.platform.persistence.polyglot.config.TypeInfo;
import de.hybris.platform.persistence.polyglot.model.Identity;
import de.hybris.platform.persistence.polyglot.model.ItemState;
import de.hybris.platform.persistence.polyglot.model.SingleAttributeKey;
import de.hybris.platform.persistence.polyglot.model.TypeId;
import de.hybris.platform.persistence.polyglot.search.FindResult;
import de.hybris.platform.persistence.polyglot.search.criteria.Conditions;
import de.hybris.platform.persistence.polyglot.search.criteria.Conditions.ComparisonCondition.CmpOperator;
import de.hybris.platform.persistence.polyglot.search.criteria.Criteria;
import de.hybris.platform.persistence.polyglot.search.criteria.Criteria.CriteriaBuilder;
import de.hybris.platform.persistence.polyglot.uow.PolyglotPersistenceConcurrentModificationException;
import de.hybris.platform.servicelayer.ServicelayerBaseTest;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.type.TypeService;
import de.hybris.platform.servicelayer.user.UserService;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.Resource;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Streams;


@IntegrationTest
public class YDocumentCartRepositoryTest extends ServicelayerBaseTest
{
	private static final SingleAttributeKey version = PolyglotPersistence.getNonlocalizedKey(Item.HJMPTS);
	private static final SingleAttributeKey key = PolyglotPersistence.getNonlocalizedKey(Item.PK);

	private final TypeInfo typeInfo = TypeInfoFactory.getTypeInfo(Constants.TC.Cart);
	private final ItemStateRepository repository = PolyglotPersistence.getRepository(typeInfo).requireSingleRepositoryOnly();

	@Resource
	private ModelService modelService;
	@Resource
	private UserService userService;
	@Resource
	private CommonI18NService commonI18NService;
	@Resource
	private TypeService typeService;

	private ProductModel product;
	private UnitModel unit;

	private CartModelsCreator cartModelsCreator;

	@Before
	public void setUp()
	{
		cartModelsCreator = new CartModelsCreator(modelService, userService, commonI18NService);

		final CatalogModel catalog = cartModelsCreator.createCatalog();
		product = cartModelsCreator.createProduct(cartModelsCreator.createCatalogVersion(catalog));
		unit = cartModelsCreator.createUnit();

		modelService.saveAll();
	}

	@Test
	public void checkGettingItemStateByCartIdentity()
	{
		//given
		final CartModel cartModel1 = cartModelsCreator.createCart(CartModel.DESCRIPTION, 1d);
		cartModelsCreator.createCartEntry(unit, product, cartModel1, 1L);
		cartModelsCreator.createCartEntry(unit, product, cartModel1, 4L);
		modelService.saveAll();

		//when
		final Identity cartIdentity = PolyglotPersistence.identityFromLong(cartModel1.getPk().getLong());
		final ItemState result = repository.get(cartIdentity);

		//then
		assertThat((Identity) result.get(key)).isEqualTo(cartIdentity);
	}

	@Test
	public void checkGettingItemStateByCartEntryIdentity()
	{
		//given
		final CartModel cartModel1 = cartModelsCreator.createCart(CartModel.DESCRIPTION, 1d);
		cartModelsCreator.createCartEntry(unit, product, cartModel1, 1L);
		final CartEntryModel cartEntryModel12 = cartModelsCreator.createCartEntry(unit, product, cartModel1, 4L);
		cartModelsCreator.createCartEntry(unit, product, cartModel1, 8L);
		modelService.saveAll();

		//when
		final Identity cartEntryIdentity = PolyglotPersistence.identityFromLong(cartEntryModel12.getPk().getLong());
		final ItemState result = repository.get(cartEntryIdentity);

		//then
		assertThat((Identity) result.get(key)).isEqualTo(cartEntryIdentity);
	}

	@Test
	public void checkRemoveItemStateByCartIdentity()
	{
		//given
		final CartModel cartModel1 = cartModelsCreator.createCart(CartModel.DESCRIPTION, 1d);
		cartModelsCreator.createCartEntry(unit, product, cartModel1, 1L);
		modelService.saveAll();

		//when
		final Identity cartIdentity = PolyglotPersistence.identityFromLong(cartModel1.getPk().getLong());
		final ItemState itemToRemove = repository.get(cartIdentity);
		repository.remove(itemToRemove);

		//then
		final ItemState removedItem = repository.get(cartIdentity);
		assertThat(removedItem).isNull();
	}

	@Test
	public void checkRemoveItemStateByCartEntryIdentity()
	{
		//given
		final CartModel cartModel1 = cartModelsCreator.createCart(CartModel.DESCRIPTION, 1d);
		final CartEntryModel cartEntryModel11 = cartModelsCreator.createCartEntry(unit, product, cartModel1, 1L);
		cartModelsCreator.createCartEntry(unit, product, cartModel1, 3L);
		modelService.saveAll();

		final Identity cartIdentity = PolyglotPersistence.identityFromLong(cartModel1.getPk().getLong());
		final Identity cartEntryIdentity = PolyglotPersistence.identityFromLong(cartEntryModel11.getPk().getLong());

		//when
		final ItemState itemToRemove = repository.get(cartEntryIdentity);
		repository.remove(itemToRemove);

		//then
		final ItemState removedItem = repository.get(cartEntryIdentity);
		assertThat(removedItem).isNull();
		final ItemState cart = repository.get(cartIdentity);
		assertThat(cart).isNotNull();
	}

	@Test
	public void checkRemoveItemStateThrowsAnExceptionWhenVersionDoesNotMatch()
	{
		//given
		final CartModel cartModel1 = cartModelsCreator.createCart(CartModel.DESCRIPTION, 1d);
		cartModelsCreator.createCartEntry(unit, product, cartModel1, 1L);
		cartModelsCreator.createCartEntry(unit, product, cartModel1, 3L);
		modelService.saveAll();

		final Identity cartIdentity = PolyglotPersistence.identityFromLong(cartModel1.getPk().getLong());

		//when
		final ItemState itemToRemove = repository.get(cartIdentity);
		cartModel1.setDescription("some other description");
		modelService.save(cartModel1);

		boolean hasThrown = false;
		try
		{
			repository.remove(itemToRemove);
			Assert.fail("exception expected");
		}
		catch (final PolyglotPersistenceConcurrentModificationException e)
		{
			hasThrown = true;
		}
		assertThat(hasThrown).isTrue();
	}

	@Test
	public void testCartVersion()
	{
		//given
		final CartModel cartModel = cartModelsCreator.createCart(CartModel.DESCRIPTION, 1d);
		modelService.saveAll();

		//when
		final Identity cartIdentity = PolyglotPersistence.identityFromLong(cartModel.getPk().getLong());
		final ItemState result = repository.get(cartIdentity);

		//then
		assertThat(result.<Long>get(version)).isGreaterThan(1L);
	}


	@Test
	public void testCartWithCartEntryVersion()
	{
		//given
		final CartModel cart = cartModelsCreator.createCart(CartModel.DESCRIPTION, 1d);
		final CartEntryModel cartEntry = cartModelsCreator.createCartEntry(unit, product, cart, 1L);
		modelService.saveAll();

		//when
		final Identity cartIdentity = PolyglotPersistence.identityFromLong(cart.getPk().getLong());
		final Identity cartEntryIdentity = PolyglotPersistence.identityFromLong(cartEntry.getPk().getLong());
		final ItemState cartState = repository.get(cartIdentity);
		final ItemState cartEntryState = repository.get(cartEntryIdentity);

		//then
		assertThat(cartState.<Long>get(version)).isEqualTo(cartEntryState.<Long>get(version));
	}

	@Test
	public void testCartWithCartEntryVersionAfterUpdate()
	{
		//given
		final CartModel cart = cartModelsCreator.createCart(CartModel.DESCRIPTION, 1d);
		final CartEntryModel cartEntry = cartModelsCreator.createCartEntry(unit, product, cart, 1L);
		modelService.saveAll();

		cartEntry.setQuantity(2L);
		modelService.save(cartEntry);

		//when
		final Identity cartIdentity = PolyglotPersistence.identityFromLong(cart.getPk().getLong());
		final Identity cartEntryIdentity = PolyglotPersistence.identityFromLong(cartEntry.getPk().getLong());
		final ItemState cartState = repository.get(cartIdentity);
		final ItemState cartEntryState = repository.get(cartEntryIdentity);

		//then
		assertThat(cartState.<Long>get(version)).isEqualTo(cartEntryState.<Long>get(version));
	}

	@Test
	public void testCartVersionIsGreaterAfterUpdate()
	{
		//given
		final CartModel cart = cartModelsCreator.createCart(CartModel.DESCRIPTION, 1d);
		final CartEntryModel cartEntry = cartModelsCreator.createCartEntry(unit, product, cart, 1L);
		modelService.saveAll();

		final Identity cartIdentityBefore = PolyglotPersistence.identityFromLong(cart.getPk().getLong());
		final ItemState cartStateBefore = repository.get(cartIdentityBefore);

		cartEntry.setQuantity(2L);
		modelService.save(cartEntry);

		//when
		final Identity cartIdentityAfter = PolyglotPersistence.identityFromLong(cart.getPk().getLong());
		final ItemState cartStateAfter = repository.get(cartIdentityAfter);

		//then
		assertThat(cartStateAfter.<Long>get(version)).isGreaterThan(cartStateBefore.<Long>get(version));
	}

	@Test
	public void checkGettingCartEntryWithFSPolyglotDialectIsNull()
	{
		//given
		final CartModel cart = cartModelsCreator.createCart(CartModel.DESCRIPTION, 1d);
		final CartEntryModel cartEntry1 = cartModelsCreator.createCartEntry(unit, product, cart, 1L);
		cartEntry1.setBasePrice(null);
		final CartEntryModel cartEntry2 = cartModelsCreator.createCartEntry(unit, product, cart, 4L);
		cartEntry2.setBasePrice(1d);
		modelService.saveAll();
		final Identity expectedItem = PolyglotPersistence.identityFromLong(cartEntry1.getPk().getLong());

		//when
		final String query = "GET {CartEntry} WHERE {order}=?order AND {basePrice} IS NULL";
		final Map<String, Object> params = Collections.singletonMap(CartEntryModel.ORDER, cart.getPk());
		final List<Identity> result = PolyglotPersistenceFlexibleSearchSupport
				.tryToConvertToPolyglotCriteria(QueryOptions.newBuilder().withQuery(query).withValues(params).build())
				.map(repository::find)
				.stream()
				.flatMap(FindResult::getResult)
				.map(m -> (Identity) m.get(PolyglotPersistence.getNonlocalizedKey("pk")))
				.collect(Collectors.toList());

		//then
		assertThat(result).isNotNull();
		assertThat(result).hasSize(1);
		assertThat(result).contains(expectedItem);
	}

	@Test
	public void checkGettingICartEntryWithFSPolyglotDialectIsNotNull()
	{
		//given
		final CartModel cart = cartModelsCreator.createCart(CartModel.DESCRIPTION, 1d);
		final CartEntryModel cartEntry1 = cartModelsCreator.createCartEntry(unit, product, cart, 1L);
		cartEntry1.setBasePrice(null);
		final CartEntryModel cartEntry2 = cartModelsCreator.createCartEntry(unit, product, cart, 4L);
		cartEntry2.setBasePrice(1d);
		modelService.saveAll();
		final Identity expectedItem = PolyglotPersistence.identityFromLong(cartEntry2.getPk().getLong());

		//when
		final String query = "GET {CartEntry} WHERE {order}=?order AND {basePrice} IS NOT NULL";
		final Map<String, Object> params = Collections.singletonMap(CartEntryModel.ORDER, cart.getPk());
		final List<Identity> result = PolyglotPersistenceFlexibleSearchSupport
				.tryToConvertToPolyglotCriteria(QueryOptions.newBuilder().withQuery(query).withValues(params).build())
				.map(repository::find)
				.stream()
				.flatMap(FindResult::getResult)
				.map(m -> (Identity) m.get(PolyglotPersistence.getNonlocalizedKey("pk")))
				.collect(Collectors.toList());

		//then
		assertThat(result).isNotNull();
		assertThat(result).hasSize(1);
		assertThat(result).contains(expectedItem);
	}

	@Test
	public void checkGettingCartEntryByCriteriaWithComparisionEqualNull()
	{
		//given
		final CartModel cart = cartModelsCreator.createCart(CartModel.DESCRIPTION, 1d);
		final CartEntryModel cartEntry1 = cartModelsCreator.createCartEntry(unit, product, cart, 1L);
		cartEntry1.setBasePrice(null);
		final CartEntryModel cartEntry2 = cartModelsCreator.createCartEntry(unit, product, cart, 4L);
		cartEntry2.setBasePrice(1d);
		modelService.saveAll();
		final Identity expectedItem = PolyglotPersistence.identityFromLong(cartEntry1.getPk().getLong());

		//when
		final Criteria criteria = getCriteriaBuilder(CartEntryModel._TYPECODE)
				.withCondition(
						Conditions.and(
								Conditions.cmp(PolyglotPersistence.getNonlocalizedKey(CartEntryModel.ORDER),
										CmpOperator.EQUAL, CartEntryModel.ORDER),
								Conditions.cmp(PolyglotPersistence.getNonlocalizedKey(CartEntryModel.BASEPRICE),
										CmpOperator.EQUAL, null)))
				.withParameters(Collections
						.singletonMap(CartEntryModel.ORDER,
								PolyglotPersistence.identityFromLong(cart.getPk().getLongValue())))
				.build();

		final List<Identity> result = repository.find(criteria)
		                                        .getResult()
		                                        .map(m -> (Identity) m.get(PolyglotPersistence.getNonlocalizedKey("pk")))
		                                        .collect(Collectors.toList());

		//then
		assertThat(result).hasSize(1);
		assertThat(result).contains(expectedItem);
	}

	@Test
	public void checkGettingCartEntryByCriteriaWithComparisionNotEqualNull()
	{
		//given
		final CartModel cart = cartModelsCreator.createCart(CartModel.DESCRIPTION, 1d);
		final CartEntryModel cartEntry1 = cartModelsCreator.createCartEntry(unit, product, cart, 1L);
		cartEntry1.setBasePrice(null);
		final CartEntryModel cartEntry2 = cartModelsCreator.createCartEntry(unit, product, cart, 4L);
		cartEntry2.setBasePrice(1d);
		modelService.saveAll();
		final Identity expectedItem = PolyglotPersistence.identityFromLong(cartEntry2.getPk().getLong());

		//when
		final Criteria criteria = getCriteriaBuilder(CartEntryModel._TYPECODE)
				.withCondition(
						Conditions.and(
								Conditions.cmp(PolyglotPersistence.getNonlocalizedKey(CartEntryModel.ORDER),
										CmpOperator.EQUAL, CartEntryModel.ORDER),
								Conditions.cmp(PolyglotPersistence.getNonlocalizedKey(CartEntryModel.BASEPRICE),
										CmpOperator.NOT_EQUAL, null)))
				.withParameters(Collections
						.singletonMap(CartEntryModel.ORDER,
								PolyglotPersistence.identityFromLong(cart.getPk().getLongValue())))
				.build();
		final List<Identity> result = repository.find(criteria)
		                                        .getResult()
		                                        .map(m -> (Identity) m.get(PolyglotPersistence.getNonlocalizedKey("pk")))
		                                        .collect(Collectors.toList());

		//then
		assertThat(result).hasSize(1);
		assertThat(result).contains(expectedItem);
	}

	@Test
	public void shouldSortCollectionInProperOrderWithNullValuesFirst()
	{
		//given
		final CartModel cart = cartModelsCreator.createCart(CartModel.DESCRIPTION, 1d);
		final CartEntryModel cartEntry1 = cartModelsCreator.createCartEntry(unit, product, cart, 1L);
		cartEntry1.setBasePrice(0.5d);
		final CartEntryModel cartEntry2 = cartModelsCreator.createCartEntry(unit, product, cart, 4L);
		cartEntry2.setBasePrice(null);
		final CartEntryModel cartEntry3 = cartModelsCreator.createCartEntry(unit, product, cart, 4L);
		cartEntry3.setBasePrice(1d);
		modelService.saveAll();

		final Stream<Identity> expectedOrder = Stream.of(cartEntry2, cartEntry1, cartEntry3)
		                                             .map(v -> PolyglotPersistence.identityFromLong(v.getPk().getLongValue()));

		//when
		final String query = "GET {CartEntry} WHERE {order}=?order ORDER BY {basePrice}";
		final Map<String, Object> params = Collections.singletonMap(CartEntryModel.ORDER, cart.getPk());
		final List<Identity> result = PolyglotPersistenceFlexibleSearchSupport
				.tryToConvertToPolyglotCriteria(QueryOptions.newBuilder().withQuery(query).withValues(params).build())
				.map(repository::find)
				.stream()
				.flatMap(FindResult::getResult)
				.map(m -> (Identity) m.get(PolyglotPersistence.getNonlocalizedKey("pk")))
				.collect(Collectors.toList());

		//then
		assertThat(result).isNotNull();
		assertThat(result).hasSize(3);
		Streams.forEachPair(expectedOrder, result.stream(),
				(o1, o2) -> assertThat(o1).isEqualTo(o2));
	}

	@Test
	public void shouldSortCollectionInProperOrderWithoutNullValues()
	{
		//given
		final CartModel cart = cartModelsCreator.createCart(CartModel.DESCRIPTION, 1d);
		final CartEntryModel cartEntry1 = cartModelsCreator.createCartEntry(unit, product, cart, 1L);
		cartEntry1.setBasePrice(0.5d);
		final CartEntryModel cartEntry2 = cartModelsCreator.createCartEntry(unit, product, cart, 4L);
		cartEntry2.setBasePrice(1.1d);
		final CartEntryModel cartEntry3 = cartModelsCreator.createCartEntry(unit, product, cart, 4L);
		cartEntry3.setBasePrice(1d);
		modelService.saveAll();

		final Stream<Identity> expectedOrder = Stream.of(cartEntry1, cartEntry3, cartEntry2)
		                                             .map(v -> PolyglotPersistence.identityFromLong(v.getPk().getLongValue()));

		//when
		final String query = "GET {CartEntry} WHERE {order}=?order ORDER BY {basePrice}";
		final Map<String, Object> params = Collections.singletonMap(CartEntryModel.ORDER, cart.getPk());
		final List<Identity> result = PolyglotPersistenceFlexibleSearchSupport
				.tryToConvertToPolyglotCriteria(QueryOptions.newBuilder().withQuery(query).withValues(params).build())
				.map(repository::find)
				.stream()
				.flatMap(FindResult::getResult)
				.map(m -> (Identity) m.get(PolyglotPersistence.getNonlocalizedKey("pk")))
				.collect(Collectors.toList());

		//then
		assertThat(result).hasSize(3);
		Streams.forEachPair(expectedOrder, result.stream(),
				(o1, o2) -> assertThat(o1).isEqualTo(o2));
	}

	@Test
	public void shouldSaveCartWithEntryGroups()
	{
		final EntryGroup group1 = createEntryGroup(1);
		final EntryGroup group2 = createEntryGroup(2);
		final EntryGroup root = createEntryGroup(0, group1, group2);

		final CartModel cart = cartModelsCreator.createCart(CartModel.DESCRIPTION, 1d);
		final CartEntryModel cartEntry1 = cartModelsCreator.createCartEntry(unit, product, cart, 1L);
		cartEntry1.setBasePrice(0.5d);
		final CartEntryModel cartEntry2 = cartModelsCreator.createCartEntry(unit, product, cart, 4L);
		cartEntry2.setBasePrice(1.1d);
		final CartEntryModel cartEntry3 = cartModelsCreator.createCartEntry(unit, product, cart, 4L);
		cartEntry3.setBasePrice(1d);

		cart.setEntryGroups(Arrays.asList(root, group1, group2));

		modelService.saveAll();
		modelService.detach(cart);


		final CartModel cart2 = modelService.get(cart.getPk());
		assertThat(cart2.getEntryGroups()).isNotNull()
		                                  .usingRecursiveFieldByFieldElementComparator()
		                                  .containsExactly(root, group1, group2);
	}

	private EntryGroup createEntryGroup(final int groupNumber, final EntryGroup... groups)
	{
		final EntryGroup root = new EntryGroup();
		root.setGroupNumber(groupNumber);
		root.setChildren(Arrays.asList(groups));
		return root;
	}

	private CriteriaBuilder getCriteriaBuilder(final String typeCode)
	{
		final ComposedType composedType = modelService.getSource(typeService.getComposedTypeForCode(typeCode));
		final Identity typeIdentity = PolyglotPersistence.identityFromLong(composedType.getPK().getLongValue());

		final List<TypeId> subTypes = composedType.getAllSubTypes().stream()
		                                          .map(ct -> TypeId.fromTypeId(
				                                          PolyglotPersistence.identityFromLong(ct.getPK().getLongValue()),
				                                          ct.getItemTypeCode()))
		                                          .collect(toList());

		return Criteria.builder(TypeId.fromTypeId(typeIdentity, composedType.getItemTypeCode()), subTypes);
	}
}
