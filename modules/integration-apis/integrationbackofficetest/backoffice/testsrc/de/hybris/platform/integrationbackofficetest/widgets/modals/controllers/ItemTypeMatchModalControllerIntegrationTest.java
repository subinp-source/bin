/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.integrationbackofficetest.widgets.modals.controllers;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.core.Registry;
import de.hybris.platform.integrationbackoffice.utility.DefaultItemTypeMatchSelector;
import de.hybris.platform.integrationbackoffice.widgets.modals.controllers.ItemTypeMatchModalController;
import de.hybris.platform.integrationservices.config.DefaultIntegrationServicesConfiguration;
import de.hybris.platform.integrationservices.enums.ItemTypeMatchEnum;
import de.hybris.platform.integrationservices.model.IntegrationObjectItemModel;
import de.hybris.platform.integrationservices.model.IntegrationObjectModel;
import de.hybris.platform.integrationservices.util.IntegrationObjectTestUtil;
import de.hybris.platform.integrationservices.util.IntegrationTestUtil;
import de.hybris.platform.servicelayer.ServicelayerTest;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.support.GenericApplicationContext;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;

import com.hybris.cockpitng.testing.util.CockpitTestUtil;

@IntegrationTest
public class ItemTypeMatchModalControllerIntegrationTest extends ServicelayerTest
{
	private static DefaultItemTypeMatchSelector itemTypeMatchSelector;

	private final static ItemTypeMatchModalController itemTypeMatchModalController = new ItemTypeMatchModalController();

	private final Class itemTypeMatchModalControllerClass = ItemTypeMatchModalController.class;

	private IntegrationObjectModel integrationObjectModel;

	@BeforeClass
	public static void beanSetUp()
	{
		final GenericApplicationContext applicationContext = (GenericApplicationContext) Registry.getApplicationContext();

		final DefaultListableBeanFactory beanFactory = (DefaultListableBeanFactory) applicationContext.getBeanFactory();

		final AbstractBeanDefinition itemSearchConfigurationDefinition = BeanDefinitionBuilder.rootBeanDefinition(
				DefaultIntegrationServicesConfiguration.class).getBeanDefinition();

		beanFactory.registerBeanDefinition("itemSearchConfiguration", itemSearchConfigurationDefinition);
		final DefaultIntegrationServicesConfiguration defaultIntegrationServicesConfiguration = Registry
				.getApplicationContext()
				.getBean("itemSearchConfiguration", DefaultIntegrationServicesConfiguration.class);

		final AbstractBeanDefinition itemTypeMatchSelectorDefinition = BeanDefinitionBuilder.rootBeanDefinition(
				DefaultItemTypeMatchSelector.class)
		                                                                                    .getBeanDefinition();
		beanFactory.registerBeanDefinition("itemTypeMatchSelector", itemTypeMatchSelectorDefinition);

		itemTypeMatchSelector = Registry.getApplicationContext()
		                                .getBean("itemTypeMatchSelector", DefaultItemTypeMatchSelector.class);

		itemTypeMatchSelector.setItemSearchConfiguration(defaultIntegrationServicesConfiguration);
		itemTypeMatchModalController.setItemTypeMatchSelector(itemTypeMatchSelector);

	}

	@Before
	public void setUp() throws Exception
	{
		CockpitTestUtil.mockZkEnvironment();
		final String[] integrationObjectDefinition = {
				"INSERT_UPDATE IntegrationObject; code[unique = true]; integrationType(code)",
				"; BasicItemTypeMatchTest; INBOUND",
				"INSERT_UPDATE IntegrationObjectItem; integrationObject(code)[unique = true]; code[unique = true]; type(code); root[default = false];itemTypeMatch(code)",
				"; BasicItemTypeMatchTest ; CatalogVersion                  ; CatalogVersion                  ;      ; ALL_SUB_AND_SUPER_TYPES",
				"; BasicItemTypeMatchTest ; Product                         ; Product                         ; true ;                        ",
				"; BasicItemTypeMatchTest ; Catalog                         ; Catalog                         ;      ; ALL_SUBTYPES           ",
				"; BasicItemTypeMatchTest ; ClassificationAttributeTypeEnum ; ClassificationAttributeTypeEnum ;      ;                        "
		};
		IntegrationTestUtil.importImpEx(integrationObjectDefinition);
		integrationObjectModel = IntegrationObjectTestUtil.findIntegrationObjectByCode("BasicItemTypeMatchTest");
	}

	@After
	public void tearDown()
	{
		IntegrationTestUtil.removeSafely(IntegrationObjectModel.class, object -> object.getCode().equals("BasicItemTypeMatchTest"));
	}

	@Test
	public void testGetChangedIntegrationObjectItems() throws Exception
	{
		final Listbox listbox = mock(Listbox.class);
		final IntegrationObjectItemModel classificationAttributeTypeEnum = integrationObjectModel.getItems()
		                                                                                         .stream()
		                                                                                         .filter(ioi -> ioi.getCode()
		                                                                                                           .equals("ClassificationAttributeTypeEnum"))
		                                                                                         .findFirst()
		                                                                                         .get();
		final IntegrationObjectItemModel catalog = integrationObjectModel.getItems()
		                                                                 .stream()
		                                                                 .filter(ioi -> ioi.getCode().equals("Catalog"))
		                                                                 .findFirst()
		                                                                 .get();
		final IntegrationObjectItemModel product = integrationObjectModel.getItems()
		                                                                 .stream()
		                                                                 .filter(ioi -> ioi.getCode().equals("Product"))
		                                                                 .findFirst()
		                                                                 .get();
		final IntegrationObjectItemModel catalogVersion = integrationObjectModel.getItems()
		                                                                        .stream()
		                                                                        .filter(ioi -> ioi.getCode()
		                                                                                          .equals("CatalogVersion"))
		                                                                        .findFirst()
		                                                                        .get();

		final Listitem row1 = createARow(catalogVersion,
				ItemTypeMatchEnum.ALL_SUB_AND_SUPER_TYPES); // ItemTypeMatchEnum not changed
		final Listitem row2 = createARow(product, ItemTypeMatchEnum.ALL_SUB_AND_SUPER_TYPES); //ItemTypeMatchEnum changed
		final Listitem row3 = createARow(catalog, ItemTypeMatchEnum.RESTRICT_TO_ITEM_TYPE); //ItemTypeMatchEnum changed
		final Listitem row4 = createARow(classificationAttributeTypeEnum,
				ItemTypeMatchEnum.ALL_SUBTYPES); //ItemTypeMatchEnum changed

		when(listbox.getItems()).thenReturn(Arrays.asList(row1, row2, row3, row4));

		final List<IntegrationObjectItemModel> changedList = callChangedIntegrationObjectItems(listbox);

		assertEquals("The size of the changed list", changedList.size(), 3);
	}

	private List<IntegrationObjectItemModel> callChangedIntegrationObjectItems(final Listbox listbox) throws Exception
	{
		final Field field = itemTypeMatchModalControllerClass.getDeclaredField("itemTypeMatcherListBox");
		field.setAccessible(true);
		field.set(itemTypeMatchModalController, listbox);
		final Method getChangedIntegrationObjectItemsMethod = getPrivateMethodOfItemTypeMatchModalController(
				"getChangedIntegrationObjectItems");
		return (List<IntegrationObjectItemModel>) getChangedIntegrationObjectItemsMethod.invoke(itemTypeMatchModalController);
	}

	private Listitem createARow(final IntegrationObjectItemModel integrationObjectItemModel,
	                            final ItemTypeMatchEnum selectedItemTypeMatchEnum)
	{
		final Listitem aRow = new Listitem();
		aRow.setValue(integrationObjectItemModel);
		final Listcell itemTypeMatchDropDownColumn = new Listcell();
		itemTypeMatchDropDownColumn.setId(integrationObjectItemModel.getPk().getHex() + "_ddc");
		aRow.appendChild(itemTypeMatchDropDownColumn);

		final Listbox selectElement = new Listbox();
		selectElement.setId(integrationObjectItemModel.getPk().getHex() + "_ddb");
		itemTypeMatchDropDownColumn.appendChild(selectElement);

		final Listitem aSelectedOption = new Listitem();
		aSelectedOption.setLabel(selectedItemTypeMatchEnum.getCode());
		aSelectedOption.setSelected(true);
		selectElement.appendChild(aSelectedOption);
		return aRow;

	}

	private Method getPrivateMethodOfItemTypeMatchModalController(final String privateMethodName) throws Exception
	{
		final Method method = itemTypeMatchModalControllerClass.getDeclaredMethod(privateMethodName);
		method.setAccessible(true);
		return method;

	}

}
