/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package ydocumentcartpackage.persistence.polyglot.repository.documentcart;

import static de.hybris.platform.persistence.polyglot.PolyglotPersistence.getReferenceTo;
import static de.hybris.platform.persistence.polyglot.PolyglotPersistence.identityFromLong;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.core.PK;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.type.CollectionType;
import de.hybris.platform.persistence.polyglot.PolyglotPersistence;
import de.hybris.platform.persistence.polyglot.model.Identity;
import de.hybris.platform.persistence.polyglot.model.Reference;
import de.hybris.platform.persistence.polyglot.model.SingleAttributeKey;
import de.hybris.platform.servicelayer.ServicelayerBaseTest;
import de.hybris.platform.util.RelationsInfo;

import java.time.Instant;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.Test;
import org.mockito.Mockito;

import com.google.common.collect.Sets;

@IntegrationTest
public class DocumentRelationHandlerTest extends ServicelayerBaseTest
{
	@Test
	public void shouldHandleEntitiesInheritance()
	{
		//given
		final Identity documentId = identityFromLong(1234);
		final Identity relatedEntityId = identityFromLong(123);

		final Identity eSuperId = identityFromLong(1);
		final Identity eId = identityFromLong(2);
		final Identity eSubId = identityFromLong(3);

		final SingleAttributeKey attr = PolyglotPersistence.getNonlocalizedKey("attr");

		//create document
		final DocumentRelationsHandler drh = Mockito.spy(new TestRelationHandler());
		final Document d = new Document(documentId, 0L, drh);

		//create related entity
		final Entity relatedEntity = createEntity(drh, d, relatedEntityId, "SomeType");
		final Reference relatedEntityRef = getReferenceTo(relatedEntity.getId());

		//create relating entities: E extends ESuper, ESub extends E
		final Entity eSuper = createEntity(drh, d, eSuperId, "ESuper");
		final Entity e = createEntity(drh, d, eId, "E", "ESuper");
		final Entity eSub = createEntity(drh, d, eSubId, "ESup", "E", "ESuper");

		//set relations
		eSuper.set(attr, relatedEntityRef);
		e.set(attr, relatedEntityRef);
		eSub.set(attr, relatedEntityRef);

		final RelationsInfo relationsInfo = RelationsInfo.builder()
		                                                 .withForeignKeyAttr(
				                                                 Pair.of(attr.getQualifier(),
						                                                 PK.fromLong(relatedEntityId.toLongValue())))
		                                                 .withTargetItemType("E")
		                                                 .build();

		//when
		final Collection<Reference> relatedItems = d.getRelatedItems(relationsInfo);

		//then
		assertThat(relatedItems).isNotNull().extracting(Reference::getIdentity).containsExactlyInAnyOrder(eId, eSubId);
	}

	@Test
	public void shouldReturnOnlyEntitiesRelatingToGivenEntity()
	{
		//given
		final Identity documentId = identityFromLong(1234);
		final Identity relatedEntity1Id = identityFromLong(123);
		final Identity relatedEntity2Id = identityFromLong(456);

		final Identity re1aId = identityFromLong(1);
		final Identity re1bId = identityFromLong(2);
		final Identity re2aId = identityFromLong(3);
		final Identity re2bId = identityFromLong(4);

		final SingleAttributeKey attr = PolyglotPersistence.getNonlocalizedKey("attr");

		//create document
		final DocumentRelationsHandler drh = Mockito.spy(new TestRelationHandler());
		final Document d = new Document(documentId, 0L, drh);

		//create related entity
		final Entity relatedEntity1 = createEntity(drh, d, relatedEntity1Id, "RelatedType");
		final Reference relatedEntity1Ref = getReferenceTo(relatedEntity1.getId());
		final Entity relatedEntity2 = createEntity(drh, d, relatedEntity2Id, "RelatedType");
		final Reference relatedEntity2Ref = getReferenceTo(relatedEntity2.getId());

		//create relating entities
		final Entity re1a = createEntity(drh, d, re1aId, "RelatingType");
		final Entity re1b = createEntity(drh, d, re1bId, "RelatingType");
		final Entity re2a = createEntity(drh, d, re2aId, "RelatingType");
		final Entity re2b = createEntity(drh, d, re2bId, "RelatingType");

		//set relations
		re1a.set(attr, relatedEntity1Ref);
		re1b.set(attr, relatedEntity1Ref);
		re2a.set(attr, relatedEntity2Ref);
		re2b.set(attr, relatedEntity2Ref);

		final RelationsInfo relationsInfo = RelationsInfo.builder()
		                                                 .withForeignKeyAttr(
				                                                 Pair.of(attr.getQualifier(),
						                                                 PK.fromLong(relatedEntity1Id.toLongValue())))
		                                                 .withTargetItemType("RelatingType")
		                                                 .build();

		//when
		final Collection<Reference> relatedItems = d.getRelatedItems(relationsInfo);

		//then
		assertThat(relatedItems).isNotNull().extracting(Reference::getIdentity).containsExactlyInAnyOrder(re1aId, re1bId);
	}


	@Test
	public void shouldReturnListOfRelatedEntitiesOrderedByCreationTime()
	{
		final Identity documentId = identityFromLong(1234);
		final Identity relatedEntityId = identityFromLong(123);

		final SingleAttributeKey attr = PolyglotPersistence.getNonlocalizedKey("attr");
		final SingleAttributeKey creationTimeAttr = PolyglotPersistence.getNonlocalizedKey(Item.CREATION_TIME);

		//create document
		final DocumentRelationsHandler drh = Mockito.spy(new TestRelationHandler());
		final Document d = new Document(documentId, 0L, drh);

		createEntity(drh, d, relatedEntityId, "RelatedType");


		final Set<Entity> relatingEntities = IntStream.range(0, 10)
		                                              .mapToObj(i -> createRelatingEntityWithRandomCreationTIme(relatedEntityId,
				                                              attr, drh, d, identityFromLong(i)))
		                                              .collect(Collectors.toSet());

		final List<Reference> expectedOrder = relatingEntities.stream()
		                                                      .sorted(Comparator.comparing(
				                                                      entity -> entity.get(creationTimeAttr)))
		                                                      .map(Entity::getId).map(PolyglotPersistence::getReferenceTo)
		                                                      .collect(Collectors.toList());

		final RelationsInfo relationsInfo = RelationsInfo.builder()
		                                                 .withForeignKeyAttr(
				                                                 Pair.of(attr.getQualifier(),
						                                                 PK.fromLong(relatedEntityId.toLongValue())))
		                                                 .withTargetItemType("RelatingType")
		                                                 .withTypeOfCollection(CollectionType.LIST)
		                                                 .build();

		assertThat(d.getRelatedItems(relationsInfo)).isInstanceOf(List.class).asList().containsExactlyElementsOf(expectedOrder);
	}

	@Test
	public void shouldReturnListOfRelatedEntitiesOrderedByOrderingAttrAscending()
	{
		testWithOrderingAttribute(true);
	}

	@Test
	public void shouldReturnListOfRelatedEntitiesOrderedByOrderingAttrDescending()
	{
		testWithOrderingAttribute(false);
	}

	private void testWithOrderingAttribute(final boolean ascending)
	{
		final Identity documentId = identityFromLong(1234);
		final Identity relatedEntityId = identityFromLong(123);

		final SingleAttributeKey attr = PolyglotPersistence.getNonlocalizedKey("attr");
		final SingleAttributeKey orderingAttr = PolyglotPersistence.getNonlocalizedKey("orderingAttr");

		//create document
		final DocumentRelationsHandler drh = Mockito.spy(new TestRelationHandler());
		final Document d = new Document(documentId, 0L, drh);

		createEntity(drh, d, relatedEntityId, "RelatedType");


		final Set<Entity> relatingEntities = IntStream.range(0, 10)
		                                              .mapToObj(i -> createRelatingEntityWithRandomOrderingAttribute(
				                                              relatedEntityId,
				                                              attr, orderingAttr, drh, d, identityFromLong(i)))
		                                              .collect(Collectors.toSet());

		Comparator<Entity> comparing = Comparator.comparing(entity -> entity.get(orderingAttr));
		if (!ascending)
		{
			comparing = comparing.reversed();
		}

		final List<Reference> expectedOrder = relatingEntities.stream()
		                                                      .sorted(comparing)
		                                                      .map(Entity::getId).map(PolyglotPersistence::getReferenceTo)
		                                                      .collect(Collectors.toList());

		final RelationsInfo relationsInfo = RelationsInfo.builder()
		                                                 .withForeignKeyAttr(
				                                                 Pair.of(attr.getQualifier(),
						                                                 PK.fromLong(relatedEntityId.toLongValue())))
		                                                 .withTargetItemType("RelatingType")
		                                                 .withTypeOfCollection(CollectionType.LIST)
		                                                 .withOrdering(orderingAttr.getQualifier(), ascending)
		                                                 .build();

		assertThat(d.getRelatedItems(relationsInfo)).isInstanceOf(List.class).asList().containsExactlyElementsOf(expectedOrder);
	}


	private Entity createRelatingEntityWithRandomOrderingAttribute(final Identity relatedEntityId, final SingleAttributeKey attr,
	                                                               final SingleAttributeKey orderingAttr,
	                                                               final DocumentRelationsHandler drh, final Document d,
	                                                               final Identity relatingEntityId)
	{
		final Entity relatingEntity = createRelatingEntityWithRandomCreationTIme(relatedEntityId, attr, drh, d, relatingEntityId);
		relatingEntity.set(orderingAttr, RandomUtils.nextLong(0, 10000));
		return relatingEntity;
	}


	private Entity createRelatingEntityWithRandomCreationTIme(final Identity relatedEntityId,
	                                                          final SingleAttributeKey attr,
	                                                          final DocumentRelationsHandler drh,
	                                                          final Document d, final Identity relatingEntityId)
	{
		final Instant creationTime = Instant.now().plusSeconds(RandomUtils.nextLong(100, 5000));

		final Entity relatingEntity = createEntity(drh, d, relatingEntityId, "RelatingType");
		relatingEntity.set(attr, getReferenceTo(relatedEntityId));
		relatingEntity.set(PolyglotPersistence.getNonlocalizedKey(Item.CREATION_TIME), creationTime);
		return relatingEntity;
	}


	private Entity createEntity(final DocumentRelationsHandler drh, final Document d, final Identity id, final String className,
	                            final String... superClasses)
	{
		final Entity e = Entity.builder(d)
		                       .withId(id)
		                       .build();

		final HashSet<String> allClasses = Sets.newHashSet(superClasses);
		allClasses.add(className);

		doReturn(className).when(drh).getEntityCode(Mockito.eq(e));
		doReturn(allClasses).when(drh).getAllEntityCodeRelatedTypeCodes(className);

		d.addEntity(e);

		return e;
	}


	public static class TestRelationHandler extends DocumentRelationsHandler
	{
		@Override
		public String getEntityCode(final Entity entity)
		{
			return super.getEntityCode(entity);
		}

		@Override
		public Set<String> getAllEntityCodeRelatedTypeCodes(final String entityCode)
		{
			return super.getAllEntityCodeRelatedTypeCodes(entityCode);
		}
	}
}