/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package de.hybris.platform.sap.productconfig.runtime.mock.impl;

import de.hybris.platform.sap.productconfig.runtime.interf.model.ConfigModel;
import de.hybris.platform.sap.productconfig.runtime.interf.model.ConflictingAssumptionModel;
import de.hybris.platform.sap.productconfig.runtime.interf.model.CsticGroupModel;
import de.hybris.platform.sap.productconfig.runtime.interf.model.CsticModel;
import de.hybris.platform.sap.productconfig.runtime.interf.model.CsticValueModel;
import de.hybris.platform.sap.productconfig.runtime.interf.model.InstanceModel;
import de.hybris.platform.sap.productconfig.runtime.interf.model.PriceModel;
import de.hybris.platform.sap.productconfig.runtime.interf.model.SolvableConflictModel;
import de.hybris.platform.sap.productconfig.runtime.interf.model.impl.ConfigModelImpl;
import de.hybris.platform.sap.productconfig.runtime.interf.model.impl.ConflictingAssumptionModelImpl;
import de.hybris.platform.sap.productconfig.runtime.interf.model.impl.CsticGroupModelImpl;
import de.hybris.platform.sap.productconfig.runtime.interf.model.impl.InstanceModelImpl;
import de.hybris.platform.sap.productconfig.runtime.interf.model.impl.PriceModelImpl;
import de.hybris.platform.sap.productconfig.runtime.interf.model.impl.SolvableConflictModelImpl;
import de.hybris.platform.sap.productconfig.runtime.mock.ConfigMock;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;


public class BaseRunTimeConfigMockImpl implements ConfigMock
{
	private static final Logger LOG = Logger.getLogger(BaseRunTimeConfigMockImpl.class);
	private static final String VERSION = "0";
	protected static final String ROOT_INST_ID = "1";
	private static final String MSG_VALUE_MISSING = "At this point we expect that an '%s' value '%s' exits for ctsic '%s'!";

	private String configId = "";
	private int conflictAssumptionIdCounter = 0;
	private boolean showDeltaPrices = true;
	protected String productCode;

	private CommonI18NService i18NService;

	public BaseRunTimeConfigMockImpl()
	{

	}

	public BaseRunTimeConfigMockImpl(final String productCode)
	{
		this.productCode = productCode;
	}

	public void showDeltaPrices(final boolean showDeltaPrices)
	{
		this.showDeltaPrices = showDeltaPrices;
	}


	protected InstanceModel createDefaultRootInstance(final ConfigModel model, final String productCode, final String langDepName)
	{
		final InstanceModel rootInstance = createInstance();
		rootInstance.setId(ROOT_INST_ID);
		rootInstance.setName(productCode);
		rootInstance.setLanguageDependentName(langDepName);
		rootInstance.setRootInstance(true);
		rootInstance.setComplete(false);
		rootInstance.setConsistent(true);
		rootInstance.setSubInstances(Collections.emptyList());
		model.setRootInstance(rootInstance);
		return rootInstance;
	}


	protected ConfigModel createDefaultConfigModel(final String name, final boolean isSingelLevel)
	{
		final ConfigModel model = new ConfigModelImpl();
		model.setId(configId);
		model.setVersion(VERSION);
		model.setKbId(configId);
		model.setName(name);
		model.setComplete(false);
		model.setConsistent(true);
		model.setSingleLevel(isSingelLevel);
		return model;
	}

	protected ConfigModel createDefaultConfigModel(final String name)
	{
		return createDefaultConfigModel(name, true);
	}

	protected InstanceModel createInstance()
	{
		final InstanceModel instance = new InstanceModelImpl();
		instance.setRootInstance(false);
		instance.setComplete(false);
		instance.setConsistent(false);
		instance.setSubInstances(new ArrayList<>());
		instance.setPosition("");
		return instance;
	}


	@Override
	public void checkModel(final ConfigModel model)
	{
		model.setComplete(false);
		model.setConsistent(false);
		model.setSolvableConflicts(new ArrayList<SolvableConflictModel>(1));
		model.getMessages().clear();
		resetConflictAssumptionId();

		final InstanceModel rootInstance = model.getRootInstance();
		if (rootInstance != null)
		{
			checkInstance(model, rootInstance);
			model.setComplete(rootInstance.isComplete());
			model.setConsistent(rootInstance.isConsistent());
		}
	}

	@Override
	public void checkInstance(final ConfigModel model, final InstanceModel instance)
	{
		instance.setComplete(true);
		instance.setConsistent(true);

		final List<CsticModel> cstics = instance.getCstics();

		for (final CsticModel cstic : cstics)
		{

			checkCstic(model, instance, cstic);

			if (!cstic.isComplete())
			{
				instance.setComplete(false);
			}
			if (!cstic.isConsistent())
			{
				instance.setConsistent(false);
			}

			model.getMessages();
		}

		for (final InstanceModel subInstance : instance.getSubInstances())
		{
			checkInstance(model, subInstance);
			if (!subInstance.isComplete())
			{
				instance.setComplete(false);
			}
			if (!subInstance.isConsistent())
			{
				instance.setConsistent(false);
			}
		}
	}

	@Override
	public void checkCstic(final ConfigModel model, final InstanceModel instance, final CsticModel cstic)
	{
		if (cstic.isRetractTriggered())
		{
			cstic.clearValues();
			cstic.setRetractTriggered(false);
		}
		cstic.getMessages().clear();
		// Check "consistent"
		cstic.setConsistent(true);

		// Check "complete"
		cstic.setComplete(true);
		if (cstic.isRequired() && cstic.getAssignedValues().isEmpty())
		{
			cstic.setComplete(false);
		}
		// Check "visible"
		cstic.setVisible(true);
		cstic.setReadonly(false);

		// make sure newly assigned value is in domain
		if (cstic.isAllowsAdditionalValues() && !cstic.isMultivalued() && cstic.getAssignedValues().size() == 1)
		{
			final CsticValueModel csticValueModel = cstic.getAssignedValues().get(0);
			List<CsticValueModel> assignableValues = cstic.getAssignableValues();
			if (!assignableValues.contains(csticValueModel))
			{
				assignableValues = new ArrayList(cstic.getAssignableValues());
				assignableValues.add(csticValueModel);
				if (CsticModel.TYPE_FLOAT == cstic.getValueType())
				{
					formatValueNumeric(csticValueModel, csticValueModel.getName());
				}
				else
				{
					csticValueModel.setLanguageDependentName(csticValueModel.getName());
				}
				cstic.setAssignableValues(assignableValues);
			}
		}
	}

	protected void formatValueNumeric(final CsticValueModel value, final String strValue)
	{
		final DecimalFormat format = new DecimalFormat("#,###,##0.00#", DecimalFormatSymbols.getInstance(Locale.ENGLISH));
		value.setName(strValue);
		value.setLanguageDependentName(format.format(new BigDecimal(strValue)));
	}

	protected void addCsticGroup(final List<CsticGroupModel> csticGroups, final String name, final String description,
			final String... cstics)
	{
		final CsticGroupModel csticGroup = new CsticGroupModelImpl();
		csticGroup.setName(name);
		csticGroup.setDescription(description);

		final List<String> csticNames = new ArrayList<>();
		if (null != cstics)
		{
			for (final String cstic : cstics)
			{
				csticNames.add(cstic);
			}
		}

		csticGroup.setCsticNames(csticNames);
		csticGroups.add(csticGroup);
	}



	protected PriceModel createPrice(final long priceValue)
	{
		return createPrice(new BigDecimal(priceValue));
	}

	protected PriceModel createPrice(final BigDecimal priceValue)
	{
		return createPrice(priceValue, false);
	}

	protected PriceModel createPrice(final BigDecimal priceValue, final BigDecimal obsoletePriceValue)
	{
		return createPrice(priceValue, false, obsoletePriceValue);
	}

	protected PriceModel createPrice(final BigDecimal priceValue, final boolean allowZeroPrice)
	{
		return createPrice(priceValue, allowZeroPrice, null);
	}

	protected PriceModel createPrice(final BigDecimal priceValue, final boolean allowZeroPrice,
			final BigDecimal obsoletePriceValue)
	{
		PriceModel price = PriceModel.NO_PRICE;
		if (priceValue != null && (allowZeroPrice || priceValue.longValue() != 0))
		{
			price = new PriceModelImpl();
			price.setCurrency(getCurrencyIso());
			price.setPriceValue(priceValue);
			price.setObsoletePriceValue(obsoletePriceValue);
		}
		return price;
	}

	protected void handleValuePrice(final ConfigModel model, final CsticModel cstic, final String valueName,
			final BigDecimal surcharge)
	{
		handleValuePrice(model, cstic, valueName, surcharge, null);
	}

	protected void handleValuePrice(final ConfigModel model, final CsticModel cstic, final String pricedValueName,
			final BigDecimal surcharge, final BigDecimal obsoletePrice)
	{
		if (CollectionUtils.isNotEmpty(cstic.getAssignedValues())
				&& cstic.getAssignedValues().stream().anyMatch(value -> pricedValueName.equals(value.getName())))
		{
			handlePriceInCaseValueIsSelected(model, cstic, surcharge, pricedValueName, obsoletePrice);
		}
		else
		{
			handlePriceInCaseValueIsNotSelected(cstic, pricedValueName, surcharge, obsoletePrice);
		}
	}

	protected void handlePriceInCaseValueIsNotSelected(final CsticModel cstic, final String valueName, final BigDecimal surcharge,
			final BigDecimal obsoletePrice)
	{
		final CsticValueModel assignableValue = cstic.getAssignableValues().stream()
				.filter(value -> valueName.equals(value.getName())).findAny().orElseThrow(
						() -> new IllegalStateException(String.format(MSG_VALUE_MISSING, "assignable", valueName, cstic.getName())));
		// value price should be always calculated, as it is shown on overview even in delta price mode
		setValuePrice(surcharge, obsoletePrice, assignableValue);
		if (showDeltaPrices)
		{
			setDeltaPrice(surcharge, obsoletePrice, assignableValue);
		}
	}

	protected void handlePriceInCaseValueIsSelected(final ConfigModel model, final CsticModel cstic,
			final BigDecimal selectedSurcharge, final String selectedValueName, final BigDecimal obsoletePrice)
	{
		final PriceModel currentTotalPrice = model.getCurrentTotalPrice();
		final PriceModel currentSelectedOptionsPrice = model.getSelectedOptionsPrice();

		model.setSelectedOptionsPrice(createPrice(currentSelectedOptionsPrice.getPriceValue().add(selectedSurcharge), true));
		model.setCurrentTotalPrice(createPrice(currentTotalPrice.getPriceValue().add(selectedSurcharge), true));

		final CsticValueModel assignedValue = cstic.getAssignedValues().stream()
				.filter(value -> selectedValueName.equals(value.getName())).findAny().orElseThrow(() -> new IllegalStateException(
						String.format(MSG_VALUE_MISSING, "assigned", selectedValueName, cstic.getName())));

		final CsticValueModel assignableValue = cstic.getAssignableValues().stream()
				.filter(value -> selectedValueName.equals(value.getName())).findAny().orElseThrow(() -> new IllegalStateException(
						String.format(MSG_VALUE_MISSING, "assignable", selectedValueName, cstic.getName())));

		calculateTotalSavings(model, selectedSurcharge, obsoletePrice);

		// value price should be always calculated, as it is shown on overview even in delta price mode
		setValuePrice(selectedSurcharge, obsoletePrice, assignedValue);
		setValuePrice(selectedSurcharge, obsoletePrice, assignableValue);
		if (showDeltaPrices)
		{
			setZeroDeltaPrice(selectedSurcharge, obsoletePrice, assignedValue);
			if (cstic.isMultivalued())
			{
				// delta price is independent from selected value
				setDeltaPrice(selectedSurcharge, obsoletePrice, assignableValue);
			}
			else
			{
				// delta price is dependent on selected value, hence other delta prices need to be recalculated
				setZeroDeltaPrice(selectedSurcharge, obsoletePrice, assignableValue);
				cstic.getAssignableValues().stream().filter(value -> !value.getName().equals(selectedValueName))
						.forEach(value -> setDeltaPriceBasedOnSelectedSurcharge(selectedSurcharge, value));
			}
		}
	}

	protected void calculateTotalSavings(final ConfigModel model, final BigDecimal selectedSurcharge,
			final BigDecimal obsoletePrice)
	{
		if (obsoletePrice != null && obsoletePrice.compareTo(selectedSurcharge) != 0)
		{
			final PriceModel currentSavings = model.getCurrentTotalSavings();
			final BigDecimal currentSavingsValue = (currentSavings != null) ? currentSavings.getPriceValue() : BigDecimal.ZERO;

			model.setCurrentTotalSavings(createPrice(currentSavingsValue.add(obsoletePrice).subtract(selectedSurcharge), true));
		}
	}

	protected void setValuePrice(final BigDecimal surcharge, final BigDecimal obsoletePrice, final CsticValueModel value)
	{
		value.setValuePrice(createPrice(surcharge, obsoletePrice));
	}

	protected void setDeltaPrice(final BigDecimal surcharge, final BigDecimal obsoletePrice, final CsticValueModel value)
	{
		value.setDeltaPrice(createPrice(value.getDeltaPrice().getPriceValue().add(surcharge), true,
				obsoletePrice != null ? obsoletePrice.subtract(surcharge) : null));
	}

	protected void setZeroDeltaPrice(final BigDecimal surcharge, final BigDecimal obsoletePrice, final CsticValueModel value)
	{
		value.setDeltaPrice(createPrice(BigDecimal.ZERO, true, obsoletePrice != null ? obsoletePrice.subtract(surcharge) : null));
	}

	protected void setDeltaPriceBasedOnSelectedSurcharge(final BigDecimal selectedSurcharge, final CsticValueModel value)
	{
		value.setDeltaPrice(createPrice(value.getDeltaPrice().getPriceValue().subtract(selectedSurcharge), true,
				value.getDeltaPrice().getObsoletePriceValue()));
	}

	protected void resetValuePrices(final CsticModel cstic)
	{
		final PriceModel zeroPrice = createPrice(BigDecimal.ZERO, true);
		cstic.getAssignableValues().stream().forEach(value -> {
			value.setDeltaPrice(zeroPrice);
			value.setValuePrice(zeroPrice);
		});
		final List<CsticValueModel> assignedValues = cstic.getAssignedValues();
		if (!CollectionUtils.isEmpty(assignedValues))
		{
			assignedValues.stream().forEach(value -> {
				value.setDeltaPrice(zeroPrice);
				value.setValuePrice(zeroPrice);
			});
		}

	}

	protected SolvableConflictModel createSolvableConflict(final CsticValueModel value, final CsticModel cstic,
			final InstanceModel instance, final String conflictText)
	{

		return createSolvableConflict(value, cstic, instance, conflictText, null, null);
	}

	protected SolvableConflictModel createSolvableConflict(final CsticValueModel value, final CsticModel cstic,
			final InstanceModel instance, final String conflictText, final CsticValueModel value2, final CsticModel cstic2)
	{

		return createSolvableConflict(value, cstic, instance, conflictText, value2, cstic2, instance);
	}

	protected SolvableConflictModel createSolvableConflict(final CsticValueModel value, final CsticModel cstic,
			final InstanceModel instance, final String conflictText, final CsticValueModel value2, final CsticModel cstic2,
			final InstanceModel instance2)
	{

		final SolvableConflictModel solvableConflictModel = new SolvableConflictModelImpl();
		String conflictLongName = "";
		if (conflictText != null)
		{
			conflictLongName = conflictText;
		}
		else
		{
			conflictLongName = "Precondition " + System.currentTimeMillis() + " violated";
		}

		solvableConflictModel.setDescription(conflictLongName);

		final List<ConflictingAssumptionModel> conflictingAssumptionsList = new ArrayList<>();
		conflictingAssumptionsList.add(createConflictAssumption(value, cstic, instance));
		if (cstic2 != null)
		{
			conflictingAssumptionsList.add(createConflictAssumption(value2, cstic2, instance2));
		}
		if (CollectionUtils.isNotEmpty(conflictingAssumptionsList))
		{
			final String groupId = conflictingAssumptionsList.get(0).getId();
			solvableConflictModel.setId(groupId);
		}
		solvableConflictModel.setConflictingAssumptions(conflictingAssumptionsList);
		return solvableConflictModel;
	}


	protected SolvableConflictModel createSolvableConflict(final CsticValueModel value, final CsticModel cstic,
			final InstanceModel instance)
	{
		return createSolvableConflict(value, cstic, instance, null);
	}

	protected ConflictingAssumptionModel createConflictAssumption(final CsticValueModel value, final CsticModel cstic,
			final InstanceModel instance)
	{

		final String observableName = cstic.getName();
		final String observableValueName = value.getName();
		final String asumptionId = Integer.toString(getNextConflictAssumptionId());

		final ConflictingAssumptionModel assumptionModel = new ConflictingAssumptionModelImpl();
		assumptionModel.setCsticName(observableName);
		assumptionModel.setValueName(observableValueName);
		assumptionModel.setInstanceId(instance.getId());
		assumptionModel.setId(asumptionId);

		return assumptionModel;
	}

	public void resetConflictAssumptionId()
	{
		conflictAssumptionIdCounter = 0;
	}

	public int getNextConflictAssumptionId()
	{
		return conflictAssumptionIdCounter++;
	}

	public void setConfigId(final String nextConfigId)
	{
		configId = nextConfigId;
	}

	public String getConfigId()
	{
		return configId;
	}


	protected String getCurrencyIso()
	{
		if (i18NService != null)
		{
			return i18NService.getCurrentCurrency().getIsocode();
		}
		LOG.info("Unit test mode, price currency for mock product is set to EUR");
		return "EUR";
	}

	@Override
	public void setI18NService(final CommonI18NService i18nService)
	{
		i18NService = i18nService;

	}

	protected void checkPreconditionViolated(final InstanceModel instance, final CsticModel cstic,
			final List<SolvableConflictModel> conflicts, final String preconditionCsticName,
			final Set<String> preconditionCsticValueNames, final String conflictText)
	{
		final CsticModel csticCarryingPrecondition = instance.getCstic(preconditionCsticName);
		if (csticCarryingPrecondition == null)
		{
			throw new IllegalStateException("We expect to see a characteristic with name: " + preconditionCsticName);
		}
		if (!CollectionUtils.isEmpty(cstic.getAssignedValues())
				&& !preconditionCsticValueNames.contains(csticCarryingPrecondition.getSingleValue()))
		{
			conflicts.add(createSolvableConflict(cstic.getAssignedValues().get(0), cstic, instance, conflictText,
					csticCarryingPrecondition.getAssignedValues().get(0), csticCarryingPrecondition));
      cstic.setConsistent(false);
    	csticCarryingPrecondition.setConsistent(false);
		}
	}

	protected void checkPreconditionViolated(final InstanceModel instance, final CsticModel cstic,
			final List<SolvableConflictModel> conflicts, final String preconditionCsticName, final String preconditionCsticValueName,
			final String conflictText)
	{
		checkPreconditionViolated(instance, cstic, conflicts, preconditionCsticName,
				Collections.singleton(preconditionCsticValueName), conflictText);
	}


	public CsticModel retrieveCstic(final InstanceModel instance, final String csticName)
	{

		final List<CsticModel> cstics = instance.getCstics();
		for (final CsticModel cstic : cstics)
		{
			if (cstic.getName().equalsIgnoreCase(csticName))
			{
				return cstic;
			}
		}
		return null;
	}


	public CsticValueModel retrieveAssignableValue(final InstanceModel instance, final String csticName, final String valueName)
	{
		final CsticModel cstic = retrieveCstic(instance, csticName);
		if (cstic != null)
		{
			final List<CsticValueModel> values = cstic.getAssignableValues();
			for (final CsticValueModel currentValue : values)
			{
				if (currentValue.getName().equals(valueName))
				{
					return currentValue;
				}
			}
		}
		return null;
	}


	protected void removeAssignableValue(final InstanceModel instance, final String csticToChange, final String valueToRemove)
	{
		final CsticModel cstic = retrieveCstic(instance, csticToChange);
		final List<CsticValueModel> assignableValues = cstic.getAssignableValues();
		final List<CsticValueModel> newAssignableValues = new ArrayList();
		for (final CsticValueModel currentValue : assignableValues)
		{
			if (!currentValue.getName().equals(valueToRemove))
			{
				newAssignableValues.add(currentValue);
			}
		}
		if (assignableValues.size() != newAssignableValues.size())
		{
			cstic.setAssignableValues(newAssignableValues);
			cstic.setAssignedValues(Collections.emptyList());
		}
	}


	protected void addAssignableValue(final InstanceModel instance, final String csticToChange, final String valueToAdd,
			final String valueDescriptionToAdd, final int index)
	{
		final CsticModel cstic = retrieveCstic(instance, csticToChange);
		final CsticValueModel value = retrieveAssignableValue(instance, csticToChange, valueToAdd);
		if (value == null)
		{
			final List<CsticValueModel> assignableValues = cstic.getAssignableValues();
			final CsticValueModel newValue = new CsticValueModelBuilder().withName(valueToAdd, valueDescriptionToAdd).build();
			assignableValues.add(index, newValue);
			cstic.setAssignableValues(assignableValues);
		}
	}


	@Override
	public ConfigModel createDefaultConfiguration()
	{

		return null;
	}

}
