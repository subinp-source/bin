/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package de.hybris.platform.platformbackoffice.validation.jalo.constraints;

import static org.assertj.core.api.Assertions.assertThat;

import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.validation.model.constraints.jsr303.AbstractConstraintTest;
import de.hybris.platform.validation.model.constraints.jsr303.NotNullConstraintModel;
import de.hybris.platform.validation.model.constraints.jsr303.NullConstraintModel;

import org.junit.Test;


public class NullAndNoNullConstraintOnReferenceTypeTest extends AbstractConstraintTest
{
	private static final String USER_ID = "userId";

	@Test
	public void shouldValidateNullReferenceWithoutErrors()
	{
		//given
		final UserModel user = createUser(USER_ID);
		//when
		createNullConstraintForReference();
		//then
		assertThat(validationService.validate(user)).isEmpty();
	}

	@Test
	public void shouldValidateNullReferenceWithErrors()
	{
		//given
		final UserModel user = createUserWithPaymentAddress(USER_ID);
		//when
		createNullConstraintForReference();
		//then
		assertThat(validationService.validate(user)).isNotEmpty();
	}

	@Test
	public void shouldValidateNotNullReferenceWithoutErrors()
	{
		//given
		final UserModel user = createUserWithPaymentAddress(USER_ID);
		//when
		createNotNullConstraintForReference();
		//then
		assertThat(validationService.validate(user)).isEmpty();
	}

	@Test
	public void shouldValidateNotNullReferenceWithErrors()
	{
		//given
		final UserModel user = createUser(USER_ID);
		//when
		createNotNullConstraintForReference();
		//then
		assertThat(validationService.validate(user)).isNotEmpty();
	}

	private UserModel createUser(final String userId)
	{
		final UserModel user = modelService.create(UserModel.class);
		user.setUid(userId);
		modelService.save(user);
		return user;
	}

	private UserModel createUserWithPaymentAddress(final String userId)
	{
		final UserModel user = createUser(userId);
		final AddressModel addressModel = modelService.create(AddressModel.class);
		addressModel.setOwner(user);
		modelService.save(addressModel);
		user.setDefaultPaymentAddress(addressModel);
		modelService.save(user);
		return user;
	}

	private void createNullConstraintForReference()
	{
		final AttributeDescriptorModel attrDesc = typeService
				.getAttributeDescriptor(typeService.getComposedTypeForClass(UserModel.class), UserModel.DEFAULTPAYMENTADDRESS);
		final NullConstraintModel nullConstraint = modelService.create(NullConstraintModel.class);
		nullConstraint.setId("nullConstraint");
		nullConstraint.setDescriptor(attrDesc);
		modelService.save(nullConstraint);
		assertThat(getDefaultMessage(Constraint.NULL.msgKey)).isEqualTo(nullConstraint.getDefaultMessage());
		validationService.reloadValidationEngine();
	}

	private void createNotNullConstraintForReference()
	{
		final AttributeDescriptorModel attrDesc = typeService
				.getAttributeDescriptor(typeService.getComposedTypeForClass(UserModel.class), UserModel.DEFAULTPAYMENTADDRESS);
		final NotNullConstraintModel notNullConstraint = modelService.create(NotNullConstraintModel.class);
		notNullConstraint.setId("notNullConstraint");
		notNullConstraint.setDescriptor(attrDesc);
		modelService.save(notNullConstraint);
		assertThat(getDefaultMessage(Constraint.NOT_NULL.msgKey)).isEqualTo(notNullConstraint.getDefaultMessage());
		validationService.reloadValidationEngine();
	}
}
