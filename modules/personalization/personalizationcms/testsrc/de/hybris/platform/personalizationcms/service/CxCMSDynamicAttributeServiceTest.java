/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.personalizationcms.service;

import static de.hybris.platform.personalizationcms.service.CxCMSDynamicAttributeService.JSP_NAMES;
import static de.hybris.platform.personalizationcms.service.CxCMSDynamicAttributeService.SPA_NAMES;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.cms2.misc.CMSFilter;
import de.hybris.platform.cms2.model.contents.CMSItemModel;
import de.hybris.platform.cms2.model.contents.components.AbstractCMSComponentModel;
import de.hybris.platform.personalizationcms.strategy.CmsCxAware;
import de.hybris.platform.servicelayer.session.MockSessionService;

import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;

import org.junit.Before;
import org.junit.Test;


@UnitTest
public class CxCMSDynamicAttributeServiceTest
{
	private static final String ACTION = "action";
	private static final String VARIATION = "variation";
	private static final String CUSTOMIZATION = "customization";
	private static final String CONTAINER_UID = "uid";
	private static final String CONTAINER_SOURCE = "source";
	private static final String CONTAINER_TYPE = "type";

	private static final Set<String> containerJspKeySet = Set.of( //
			JSP_NAMES.containerId, //
			JSP_NAMES.containerType, //
			JSP_NAMES.sourceId);

	private static final Set<String> actionsJspKeySet = Set.of( //
			JSP_NAMES.containerId, //
			JSP_NAMES.containerType, //
			JSP_NAMES.sourceId, //
			JSP_NAMES.actionId, //
			JSP_NAMES.variationId, //
			JSP_NAMES.customizationId);

	private static final Set<String> containerSpaKeySet = Set.of( //
			SPA_NAMES.containerId, //
			SPA_NAMES.containerType, //
			SPA_NAMES.sourceId);

	private static final Set<String> actionsSpaKeySet = Set.of( //
			SPA_NAMES.containerId, //
			SPA_NAMES.containerType, //
			SPA_NAMES.sourceId, //
			SPA_NAMES.actionId, //
			SPA_NAMES.variationId, //
			SPA_NAMES.customizationId);

	private final MockSessionService sessionService = new MockSessionService();
	private CxCMSDynamicAttributeService service;

	@Before
	public void setup()
	{
		service = new CxCMSDynamicAttributeService();
		service.setSessionService(sessionService);
	}

	@Test
	public void groupNameTest()
	{
		//when
		final String groupName = service.groupName();
		//then
		assertEquals("smartedit", groupName);
	}

	@Test
	public void constrainedByNullTest()
	{
		//given
		final CMSItemModel model = null;
		//when
		final Predicate<CMSItemModel> constrain = service.getConstrainedBy();
		//then
		assertFalse(constrain.test(model));
	}

	@Test
	public void constrainedByCmsItemTest()
	{
		//given
		final CMSItemModel model = new CMSItemModel();
		//when
		final Predicate<CMSItemModel> constrain = service.getConstrainedBy();
		//then
		assertFalse(constrain.test(model));
	}

	@Test
	public void constrainedByCxAwareTest()
	{
		//given
		final CMSItemModel model = new CmsCxAwareItemModel();
		//when
		final Predicate<CMSItemModel> constrain = service.getConstrainedBy();
		//then
		assertTrue(constrain.test(model));
	}

	@Test
	public void getPropertiesNull()
	{
		//given
		final CMSItemModel model = null;
		//when
		final Map<String, Object> properties = service.getProperties(model);
		//then
		assertNotNull(properties);
		assertEquals(0, properties.size());
	}

	@Test
	public void getPropertiesCmsItem()
	{
		//given
		final CMSItemModel model = new CMSItemModel();
		//when
		final Map<String, Object> properties = service.getProperties(model);
		//then
		assertNotNull(properties);
		assertEquals(0, properties.size());
	}

	@Test
	public void getPropertiesCmsAwareItem()
	{
		//given
		final AbstractCMSComponentModel model = new CmsCxAwareItemModel();
		//when
		final Map<String, Object> properties = service.getProperties(model);
		//then
		assertNotNull(properties);
		assertEquals(0, properties.size());
	}

	@Test
	public void getPropertiesCmsAwareItemWithContainer()
	{
		//given
		final CMSItemModel model = builcContainerAwareItem();

		//when
		final Map<String, Object> properties = service.getProperties(model);
		//then
		assertNotNull(properties);
		assertEquals(3, properties.size());
		assertEquals(containerSpaKeySet, properties.keySet());
		assertEquals(CONTAINER_UID, properties.get(SPA_NAMES.containerId));
		assertEquals(CONTAINER_TYPE, properties.get(SPA_NAMES.containerType));
		assertEquals(CONTAINER_SOURCE, properties.get(SPA_NAMES.sourceId));
	}

	@Test
	public void getPropertiesCmsAwareItemWithActions()
	{
		//given
		final AbstractCMSComponentModel model = builcCxAwareItem();

		//when
		final Map<String, Object> properties = service.getProperties(model);
		//then
		assertNotNull(properties);
		assertEquals(6, properties.size());
		assertEquals(actionsSpaKeySet, properties.keySet());
		assertEquals(CONTAINER_UID, properties.get(SPA_NAMES.containerId));
		assertEquals(CONTAINER_TYPE, properties.get(SPA_NAMES.containerType));
		assertEquals(CONTAINER_SOURCE, properties.get(SPA_NAMES.sourceId));
		assertEquals(ACTION, properties.get(SPA_NAMES.actionId));
		assertEquals(VARIATION, properties.get(SPA_NAMES.variationId));
		assertEquals(CUSTOMIZATION, properties.get(SPA_NAMES.customizationId));
	}

	@Test
	public void getAttributesCmsAwareItemWithContainer()
	{
		//given
		final AbstractCMSComponentModel model = builcContainerAwareItem();
		previewTicketExist();

		//when
		final Map<String, String> properties = service.getDynamicComponentAttributes(model, null);
		//then
		assertNotNull(properties);
		assertEquals(3, properties.size());
		assertEquals(containerJspKeySet, properties.keySet());
		assertEquals(CONTAINER_UID, properties.get(JSP_NAMES.containerId));
		assertEquals(CONTAINER_TYPE, properties.get(JSP_NAMES.containerType));
		assertEquals(CONTAINER_SOURCE, properties.get(JSP_NAMES.sourceId));
	}

	@Test
	public void getAttributesCmsAwareItemWithActions()
	{
		//given
		final AbstractCMSComponentModel model = builcCxAwareItem();
		previewTicketExist();
		//when
		final Map<String, String> properties = service.getDynamicComponentAttributes(model, null);
		//then
		assertNotNull(properties);
		assertEquals(6, properties.size());
		assertEquals(actionsJspKeySet, properties.keySet());
		assertEquals(CONTAINER_UID, properties.get(JSP_NAMES.containerId));
		assertEquals(CONTAINER_TYPE, properties.get(JSP_NAMES.containerType));
		assertEquals(CONTAINER_SOURCE, properties.get(JSP_NAMES.sourceId));
		assertEquals(ACTION, properties.get(JSP_NAMES.actionId));
		assertEquals(VARIATION, properties.get(JSP_NAMES.variationId));
		assertEquals(CUSTOMIZATION, properties.get(JSP_NAMES.customizationId));
	}

	@Test
	public void getAttributesCmsAwareItemNoPreview()
	{
		//given
		final AbstractCMSComponentModel model = builcCxAwareItem();
		//when
		final Map<String, String> properties = service.getDynamicComponentAttributes(model, null);
		//then
		assertNotNull(properties);
		assertEquals(0, properties.size());
	}

	private void previewTicketExist()
	{
		sessionService.setAttribute(CMSFilter.PREVIEW_TICKET_ID_PARAM, new Object());
	}

	private CmsCxAwareItemModel builcContainerAwareItem()
	{
		return new CmsCxAwareItemModel()//
				.setCxContainerType(CONTAINER_TYPE)//
				.setCxContainerUid(CONTAINER_UID)//
				.setContainerSourceId(CONTAINER_SOURCE);
	}

	private CmsCxAwareItemModel builcCxAwareItem()
	{
		return builcContainerAwareItem()//
				.setCxActionCode(ACTION)//
				.setCxVariationCode(VARIATION)//
				.setCxCustomizationCode(CUSTOMIZATION);
	}

	private static class CmsCxAwareItemModel extends AbstractCMSComponentModel implements CmsCxAware
	{
		private String cxContainerUid;
		private String cxContainerType;
		private String cxActionCode;
		private String cxCustomizationCode;
		private String cxVariationCode;
		private String containerSourceId;

		@Override
		public String getCxContainerUid()
		{
			return cxContainerUid;
		}

		public CmsCxAwareItemModel setCxContainerUid(final String cxContainerUid)
		{
			this.cxContainerUid = cxContainerUid;
			return this;
		}

		@Override
		public String getCxContainerType()
		{
			return cxContainerType;
		}

		public CmsCxAwareItemModel setCxContainerType(final String cxContainerType)
		{
			this.cxContainerType = cxContainerType;
			return this;
		}

		@Override
		public String getCxActionCode()
		{
			return cxActionCode;
		}

		public CmsCxAwareItemModel setCxActionCode(final String cxActionCode)
		{
			this.cxActionCode = cxActionCode;
			return this;
		}

		@Override
		public String getCxCustomizationCode()
		{
			return cxCustomizationCode;
		}

		public CmsCxAwareItemModel setCxCustomizationCode(final String cxCustomizationCode)
		{
			this.cxCustomizationCode = cxCustomizationCode;
			return this;
		}

		@Override
		public String getCxVariationCode()
		{
			return cxVariationCode;
		}

		public CmsCxAwareItemModel setCxVariationCode(final String cxVariationCode)
		{
			this.cxVariationCode = cxVariationCode;
			return this;
		}

		@Override
		public String getContainerSourceId()
		{
			return containerSourceId;
		}

		public CmsCxAwareItemModel setContainerSourceId(final String containerSourceId)
		{
			this.containerSourceId = containerSourceId;
			return this;
		}
	}
}
