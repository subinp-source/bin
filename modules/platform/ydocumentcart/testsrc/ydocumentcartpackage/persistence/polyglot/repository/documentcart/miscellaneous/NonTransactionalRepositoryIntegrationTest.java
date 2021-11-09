/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package ydocumentcartpackage.persistence.polyglot.repository.documentcart.miscellaneous;

import static org.assertj.core.api.Assertions.assertThat;

import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.persistence.PolyglotPersistenceGenericItemSupport;
import de.hybris.platform.persistence.polyglot.ItemStateRepository;
import de.hybris.platform.servicelayer.ServicelayerBaseTest;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.user.UserService;

import java.util.UUID;

import javax.annotation.Resource;

import org.junit.Test;

import ydocumentcartpackage.persistence.polyglot.repository.documentcart.CartModelsCreator;


public class NonTransactionalRepositoryIntegrationTest extends ServicelayerBaseTest
{

	@Resource(name = "documentCartRepository")
	ItemStateRepository repository;

	@Resource
	private ModelService modelService;
	@Resource
	private UserService userService;
	@Resource
	private CommonI18NService commonI18NService;

	@Test
	public void repositoryshouldBeNonTransactional()
	{
		//then
		assertThat(repository.isSupported(new PolyglotPersistenceGenericItemSupport.TransactionalRepository())).isFalse();
	}

	@Test
	public void repositoryshouldBeNonTransactionalWhenCheckingFromPolyglotPersistenceGenericItemSupport()
	{
		//given
		final CartModelsCreator cartModelsCreator = new CartModelsCreator(modelService, userService, commonI18NService);
		final CartModel cart = cartModelsCreator.createCart(UUID.randomUUID().toString(), 4.55D);
		modelService.save(cart);

		//then
		assertThat(PolyglotPersistenceGenericItemSupport.isBackedByTransactionalRepository(cart.getPk())).isFalse();
	}
}
