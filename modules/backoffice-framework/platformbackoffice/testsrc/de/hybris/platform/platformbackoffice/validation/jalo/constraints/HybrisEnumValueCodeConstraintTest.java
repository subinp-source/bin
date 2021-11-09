/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package de.hybris.platform.platformbackoffice.validation.jalo.constraints;

import static org.assertj.core.api.Assertions.assertThat;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.core.enums.CreditCardType;
import de.hybris.platform.core.model.order.payment.CreditCardPaymentInfoModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.platformbackoffice.validation.model.constraints.HybrisEnumValueCodeConstraintModel;
import de.hybris.platform.validation.exceptions.HybrisConstraintViolation;
import de.hybris.platform.validation.model.constraints.jsr303.AbstractConstraintTest;

import java.util.Set;

import org.junit.Test;


@IntegrationTest
public class HybrisEnumValueCodeConstraintTest extends AbstractConstraintTest
{
	@Test
	public void shouldValidateHybrisEnumValueCodeConstraintSuccessfully()
	{
		// given
		final CreditCardType creditCardTypeOnModel = CreditCardType.VISA;
		final CreditCardType creditCardTypeOnConstraint = CreditCardType.VISA;

		final CreditCardPaymentInfoModel creditCardPaymentInfoModel = preparePaymentInfoWithCreditCardType(creditCardTypeOnModel);
		final HybrisEnumValueCodeConstraintModel constraint = new HybrisEnumValueCodeConstraintModel();
		modelService.initDefaults(constraint);
		constraint.setId("hybrisEnumValueCode");
		constraint.setValue(creditCardTypeOnConstraint.getCode());
		constraint.setTarget(CreditCardPaymentInfoModel.class);
		constraint.setQualifier("type");
		constraint.setType(typeService.getComposedTypeForCode(CreditCardPaymentInfoModel._TYPECODE));
		modelService.save(constraint);

		validationService.reloadValidationEngine();

		// when
		final Set<HybrisConstraintViolation> result = validationService.validate(creditCardPaymentInfoModel);

		// then
		assertThat(result).isEmpty();
	}

	@Test
	public void shouldValidateHybrisEnumValueCodeConstraintErrors()
	{
		// given
		final CreditCardType creditCardTypeOnModel = CreditCardType.VISA;
		final CreditCardType creditCardTypeOnConstraint = CreditCardType.AMEX;

		final CreditCardPaymentInfoModel creditCardPaymentInfoModel = preparePaymentInfoWithCreditCardType(creditCardTypeOnModel);
		final HybrisEnumValueCodeConstraintModel constraint = new HybrisEnumValueCodeConstraintModel();
		modelService.initDefaults(constraint);
		constraint.setId("hybrisEnumValueCode");
		constraint.setValue(creditCardTypeOnConstraint.getCode());
		constraint.setTarget(CreditCardPaymentInfoModel.class);
		constraint.setQualifier("type");
		constraint.setType(typeService.getComposedTypeForCode(CreditCardPaymentInfoModel._TYPECODE));
		modelService.save(constraint);

		validationService.reloadValidationEngine();

		// when
		final Set<HybrisConstraintViolation> result = validationService.validate(creditCardPaymentInfoModel);

		// then
		assertThat(result).isNotEmpty().extracting("localizedMessage")
				.containsOnly("The attribute \"type\" must be equal to enum value \"amex\".");
	}

	private CreditCardPaymentInfoModel preparePaymentInfoWithCreditCardType(final CreditCardType visa)
	{
		final UserModel userModel = new UserModel();
		userModel.setUid("123");

		final CreditCardPaymentInfoModel creditCardPaymentInfoModel = new CreditCardPaymentInfoModel();
		creditCardPaymentInfoModel.setNumber("4178244152074458");
		creditCardPaymentInfoModel.setCode("code");
		creditCardPaymentInfoModel.setCcOwner("ccOwner");
		creditCardPaymentInfoModel.setValidToMonth("02");
		creditCardPaymentInfoModel.setValidToYear("19");
		creditCardPaymentInfoModel.setValidFromMonth("01");
		creditCardPaymentInfoModel.setValidFromYear("07");
		creditCardPaymentInfoModel.setUser(userModel);
		creditCardPaymentInfoModel.setType(visa);
		modelService.save(creditCardPaymentInfoModel);
		modelService.detach(creditCardPaymentInfoModel);
		return creditCardPaymentInfoModel;
	}
}
