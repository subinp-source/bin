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
import de.hybris.platform.sap.productconfig.runtime.interf.model.CsticGroupModel;
import de.hybris.platform.sap.productconfig.runtime.interf.model.CsticModel;
import de.hybris.platform.sap.productconfig.runtime.interf.model.CsticValueModel;
import de.hybris.platform.sap.productconfig.runtime.interf.model.InstanceModel;
import de.hybris.platform.sap.productconfig.runtime.interf.model.PriceModel;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.util.CollectionUtils;


public class CPQScrewdriverMockImpl extends BaseRunTimeConfigMockImpl
{
	protected static final String ROOT_INSTANCE_NAME = "CONF_SCREWDRIVER_S";
	protected static final String ROOT_INSTANCE_LANG_DEP_NAME = "Screwdriver mock instance";

	protected static final String CONF_SCREWDRIVER_MODE = "MODE";
	protected static final String CONF_SCREWDRIVER_PROFESSIONAL = "PROFESSIONAL";
	protected static final String CONF_SCREWDRIVER_STANDARD = "STANDARD";
	protected static final String DESCRIPTION_MODE = "Mode";
	protected static final String DESCRIPTION_MODE_PROFESSIONAL = "Professional";
	protected static final String DESCRIPTION_MODE_STANDARD = "Standard";

	protected static final String CONF_SCREWDRIVER_COL = "SCREWDRIVER_COL";
	protected static final String CONF_SCREWDRIVER_GREEN = "GREEN";
	protected static final String CONF_SCREWDRIVER_BLUE = "BLUE";
	protected static final String CONF_SCREWDRIVER_YELLOW = "YELLOW";
	protected static final String DESCRIPTION_SCREWDRIVER_COL = "Color";
	protected static final String DESCRIPTION_SCREWDRIVER_COL_GREEN = "Green";
	protected static final String DESCRIPTION_SCREWDRIVER_COL_BLUE = "Blue";
	protected static final String DESCRIPTION_SCREWDRIVER_COL_YELLOW = "Yellow";

	protected static final String CONF_SCREWDRIVER_POWER = "SCREWDRIVER_POWER";
	protected static final String CONF_SCREWDRIVER_3_6V = "3.6V";
	protected static final String CONF_SCREWDRIVER_10_8V = "10.8V";
	protected static final String CONF_SCREWDRIVER_14_6V = "14.6V";
	protected static final String CONF_SCREWDRIVER_18V = "18V";
	protected static final String DESCRIPTION_SCREWDRIVER_POWER = "Power";
	protected static final String DESCRIPTION_SCREWDRIVER_POWER_3_6V = "3.6 V";
	protected static final String DESCRIPTION_SCREWDRIVER_POWER_10_8V = "10.8 V";
	protected static final String DESCRIPTION_SCREWDRIVER_POWER_14_6V = "14.6 V";
	protected static final String DESCRIPTION_SCREWDRIVER_POWER_18V = "18 V";

	protected static final String CONF_SCREWDRIVER_GEARS = "SCREWDRIVER_GEARS";
	protected static final String CONF_SCREWDRIVER_1GEAR = "1GEAR";
	protected static final String CONF_SCREWDRIVER_2GEAR = "2GEAR";
	protected static final String CONF_SCREWDRIVER_3GEAR = "3GEAR";
	protected static final String DESCRIPTION_SCREWDRIVER_GEARS = "Number of Gears";
	protected static final String DESCRIPTION_SCREWDRIVER_GEARS_1GEAR = "1";
	protected static final String DESCRIPTION_SCREWDRIVER_GEARS_2GEAR = "2";
	protected static final String DESCRIPTION_SCREWDRIVER_GEARS_3GEAR = "3";

	protected static final String CONF_SCREWDRIVER_TORQUE = "SCREWDRIVER_TORQUE";
	protected static final String CONF_SCREWDRIVER_15_25NM = "15/25NM";
	protected static final String CONF_SCREWDRIVER_25_45NM = "25/45NM";
	protected static final String CONF_SCREWDRIVER_50_90NM = "50/90NM";
	protected static final String DESCRIPTION_SCREWDRIVER_TORQUE = "Torque (soft/hard material)";
	protected static final String DESCRIPTION_SCREWDRIVER_TORQUE_15_25NM = "15/25 Nm";
	protected static final String DESCRIPTION_SCREWDRIVER_TORQUE_25_45NM = "25/45 Nm";
	protected static final String DESCRIPTION_SCREWDRIVER_TORQUE_50_90NM = "50/90 Nm";

	protected static final String CONF_SCREWDRIVER_TORQUE_LEVEL = "TORQUE_LEVEL";
	protected static final String CONF_SCREWDRIVER_12LEVELS = "12LEVELS";
	protected static final String CONF_SCREWDRIVER_16LEVELS = "16LEVELS";
	protected static final String CONF_SCREWDRIVER_20LEVELS = "20LEVELS";
	protected static final String DESCRIPTION_TORQUE_LEVEL = "Levels for Torque Adjustment";
	protected static final String DESCRIPTION_TORQUE_LEVEL_12LEVELS = "12";
	protected static final String DESCRIPTION_TORQUE_LEVEL_16LEVELS = "16";
	protected static final String DESCRIPTION_TORQUE_LEVEL_20LEVELS = "20";

	protected static final String CONF_SCREWDRIVER_BATTERY = "SCREWDRIVER_BATTERY";
	protected static final String CONF_SCREWDRIVER_15AH = "1.5AH";
	protected static final String CONF_SCREWDRIVER_25AH = "2.5AH";
	protected static final String CONF_SCREWDRIVER_35AH = "3.5AH";
	protected static final String DESCRIPTION_SCREWDRIVER_BATTERY = "Battery Capacity";
	protected static final String DESCRIPTION_SCREWDRIVER_BATTERY_1_5AH = "1.5 Ah";
	protected static final String DESCRIPTION_SCREWDRIVER_BATTERY_2_5AH = "2.5 Ah";
	protected static final String DESCRIPTION_SCREWDRIVER_BATTERY_3_5AH = "3.5 Ah";

	protected static final String CONF_SCREWDRIVER_DRILL_CHUCK = "DRILL_CHUCK";
	protected static final String CONF_SCREWDRIVER_4_9MM = "4-9";
	protected static final String CONF_SCREWDRIVER_2_12MM = "2-12";
	protected static final String CONF_SCREWDRIVER_2_18MM = "2-18";
	protected static final String DESCRIPTION_DRILL_CHUCK = "Drill Chuck Size";
	protected static final String DESCRIPTION_DRILL_CHUCK_4_9 = "4-9 mm";
	protected static final String DESCRIPTION_DRILL_CHUCK_2_12 = "2-12 mm";
	protected static final String DESCRIPTION_DRILL_CHUCK_2_18 = "2-18 mm";

	protected static final String CONF_SCREWDRIVER_OUTDOOR_CHARGING = "OUTDOOR_CHARGING";
	protected static final String CONF_SCREWDRIVER_OUTDOOR_CHARGING_YES = "YES";
	protected static final String CONF_SCREWDRIVER_OUTDOOR_CHARGING_NO = "NO";
	protected static final String DESCRIPTION_OUTDOOR_CHARGING = "Outdoor Charging Station";

	protected static final String CONF_SCREWDRIVER_ADDITIONAL_OPTIONS = "ADDTIONAL_OPTIONS";
	protected static final String CONF_SCREWDRIVER_LED = "LED";
	protected static final String CONF_SCREWDRIVER_POWER_LEVEL = "POWER_LEVEL";
	protected static final String CONF_SCREWDRIVER_ROTATION = "ROTATION";
	protected static final String CONF_SCREWDRIVER_SOUND = "SOUND";
	protected static final String CONF_SCREWDRIVER_VIBRATION = "VIBRATION";
	protected static final String DESCRIPTION_ADDTIONAL_OPTIONS = "Additional Options";
	protected static final String DESCRIPTION_ADDITIONAL_OPTIONS_LED = "Lighting LED";
	protected static final String DESCRIPTION_ADDITIONAL_OPTIONS_POWER_LEVEL = "Display Power Level";
	protected static final String DESCRIPTION_ADDITIONAL_OPTIONS_ROTATION = "Right & Left Rotation (Switch)";
	protected static final String DESCRIPTION_ADDITIONAL_OPTIONS_SOUND = "Sound Absorption";
	protected static final String DESCRIPTION_ADDITIONAL_OPTIONS_VIBRATION = "Vibration Absorption";

	protected static final String CONF_SCREWDRIVER_ACCESSORY = "ACCESSORY";
	protected static final String CONF_SCREWDRIVER_SECONDBATTERY = "BATTERY";
	protected static final String CONF_SCREWDRIVER_BITS = "BITS";
	protected static final String CONF_SCREWDRIVER_CASE = "CASE";
	protected static final String CONF_SCREWDRIVER_DRILLS = "DRILLS";
	protected static final String DESCRIPTION_ACCESSORY = "Accessories";
	protected static final String DESCRIPTION_ACCESSORY_SECOND_BATTERY = "Second Battery";
	protected static final String DESCRIPTION_ACCESSORY_BITS = "Bit Set";
	protected static final String DESCRIPTION_ACCESSORY_CASE = "Protection Case";
	protected static final String DESCRIPTION_ACCESSORY_DRILLS = "Drill Set";

	protected static final String CONF_SCREWDRIVER_WARRANTY = "WARRANTY";
	protected static final String CONF_SCREWDRIVER_WARRANTY_YES = "YES";
	protected static final String CONF_SCREWDRIVER_WARRANTY_NO = "NO";
	protected static final String DESCRIPTION_WARRANTY = "Warranty Extension (48 Months)";

	protected static final String DESCRIPTION_SCREWDRIVER_YES = "Yes";
	protected static final String DESCRIPTION_SCREWDRIVER_NO = "No";
	private static final long SURCHARGE_CONF_SCREWDRIVER_OUTDOOR_CHARGING_YES = 20;
	private static final long SURCHARGE_CONF_SCREWDRIVER_WARRANTY_YES = 15;
	private static final long SURCHARGE_CONF_SCREWDRIVER_SECONDBATTERY = 15;
	private static final long SURCHARGE_CONF_SCREWDRIVER_BITS = 20;
	private static final long SURCHARGE_CONF_SCREWDRIVER_CASE = 10;
	private static final long SURCHARGE_CONF_SCREWDRIVER_DRILLS = 20;
	protected static final String VARIANT_CODE_CONF_SCREWDRIVER_S_PROF_2_12 = "CONF_SCREWDRIVER_S-PROF-2-12";
	protected static final String VARIANT_CODE_CONF_SCREWDRIVER_S_PROF_2_18 = "CONF_SCREWDRIVER_S-PROF-2-18";
	protected static final String VARIANT_CODE_CONF_SCREWDRIVER_S_STD_2_12 = "CONF_SCREWDRIVER_S-STD-2-12";
	protected static final String VARIANT_CODE_CONF_SCREWDRIVER_S_STD_4_9 = "CONF_SCREWDRIVER_S-STD-4-9";
	private final String variantProductCode;


	public CPQScrewdriverMockImpl(final String variantProductCode)
	{
		super();
		this.variantProductCode = variantProductCode;
	}

	protected String getVariantProductCode()
	{
		return variantProductCode;
	}

	@Override
	public ConfigModel createDefaultConfiguration()
	{

		// Model
		final ConfigModel model = createDefaultConfigModel("Configuration for Screwdriver " + getConfigId());

		setPrice(model);

		// root instance
		final InstanceModel rootInstance = createDefaultRootInstance(model, ROOT_INSTANCE_NAME, ROOT_INSTANCE_LANG_DEP_NAME);

		// cstic groups
		final List<CsticGroupModel> csticGroups = createCsticGroupList();
		rootInstance.setCsticGroups(csticGroups);

		// Characteristics and Values
		final List<CsticModel> cstics = new ArrayList<>();
		cstics.add(createMode());
		cstics.add(createColor());
		cstics.add(createPower());
		cstics.add(createGears());
		cstics.add(createTorque());
		cstics.add(createTorqueLevel());
		cstics.add(createBattery());
		cstics.add(createDrillChuckSize());
		cstics.add(createOutDoorCharging());
		cstics.add(createAdditionalOptions());
		cstics.add(createAccessories());
		cstics.add(createWarranty());
		rootInstance.setCstics(cstics);

		return model;
	}

	protected void setPrice(final ConfigModel model)
	{
		final PriceModel basePrice = createPrice(160);
		model.setBasePrice(basePrice);

		final PriceModel selectedOptionsPrice = createPrice(0);
		model.setSelectedOptionsPrice(selectedOptionsPrice);

		final PriceModel currentTotalPrice = createPrice(160);
		model.setCurrentTotalPrice(currentTotalPrice);
	}

	protected CsticModel createMode()
	{
		final CsticModelBuilder builder = new CsticModelBuilder().withInstance(ROOT_INST_ID, ROOT_INSTANCE_NAME);
		builder.withName(CONF_SCREWDRIVER_MODE, DESCRIPTION_MODE);
		builder.stringType().singleSelection();

		final boolean isProfessionalModeSelected = VARIANT_CODE_CONF_SCREWDRIVER_S_PROF_2_12.equals(getVariantProductCode())
				|| VARIANT_CODE_CONF_SCREWDRIVER_S_PROF_2_18.equals(getVariantProductCode());
		final boolean isStandardModeSelected = VARIANT_CODE_CONF_SCREWDRIVER_S_STD_2_12.equals(getVariantProductCode())
				|| VARIANT_CODE_CONF_SCREWDRIVER_S_STD_4_9.equals(getVariantProductCode());

		builder.addOption(CONF_SCREWDRIVER_PROFESSIONAL, DESCRIPTION_MODE_PROFESSIONAL, isProfessionalModeSelected)
				.addOption(CONF_SCREWDRIVER_STANDARD, DESCRIPTION_MODE_STANDARD, isStandardModeSelected);
		builder.withDefaultUIState().required();
		return builder.build();
	}

	protected CsticModel createColor()
	{
		final CsticModelBuilder builder = new CsticModelBuilder().withInstance(ROOT_INST_ID, ROOT_INSTANCE_NAME);
		builder.withName(CONF_SCREWDRIVER_COL, DESCRIPTION_SCREWDRIVER_COL);
		builder.stringType().singleSelection();

		final boolean isBlueSelected = VARIANT_CODE_CONF_SCREWDRIVER_S_PROF_2_12.equals(getVariantProductCode())
				|| VARIANT_CODE_CONF_SCREWDRIVER_S_PROF_2_18.equals(getVariantProductCode());

		final boolean isGreenSelected = VARIANT_CODE_CONF_SCREWDRIVER_S_STD_2_12.equals(getVariantProductCode())
				|| VARIANT_CODE_CONF_SCREWDRIVER_S_STD_4_9.equals(getVariantProductCode());

		builder.addOption(CONF_SCREWDRIVER_GREEN, DESCRIPTION_SCREWDRIVER_COL_GREEN, isGreenSelected)
				.addOption(CONF_SCREWDRIVER_YELLOW, DESCRIPTION_SCREWDRIVER_COL_YELLOW)
				.addOption(CONF_SCREWDRIVER_BLUE, DESCRIPTION_SCREWDRIVER_COL_BLUE, isBlueSelected);

		builder.withDefaultUIState();
		return builder.build();
	}

	protected CsticModel createPower()
	{
		final CsticModelBuilder builder = new CsticModelBuilder().withInstance(ROOT_INST_ID, ROOT_INSTANCE_NAME);
		builder.withName(CONF_SCREWDRIVER_POWER, DESCRIPTION_SCREWDRIVER_POWER);
		builder.stringType().singleSelection();

		final boolean is18Selected = VARIANT_CODE_CONF_SCREWDRIVER_S_PROF_2_12.equals(getVariantProductCode());
		final boolean is14Selected = VARIANT_CODE_CONF_SCREWDRIVER_S_PROF_2_18.equals(getVariantProductCode());
		final boolean is10Selected = VARIANT_CODE_CONF_SCREWDRIVER_S_STD_2_12.equals(getVariantProductCode())
				|| VARIANT_CODE_CONF_SCREWDRIVER_S_STD_4_9.equals(getVariantProductCode());

		builder.addOption(CONF_SCREWDRIVER_3_6V, DESCRIPTION_SCREWDRIVER_POWER_3_6V)
				.addOption(CONF_SCREWDRIVER_10_8V, DESCRIPTION_SCREWDRIVER_POWER_10_8V, is10Selected)
				.addOption(CONF_SCREWDRIVER_14_6V, DESCRIPTION_SCREWDRIVER_POWER_14_6V, is14Selected)
				.addOption(CONF_SCREWDRIVER_18V, DESCRIPTION_SCREWDRIVER_POWER_18V, is18Selected);

		builder.withDefaultUIState();
		return builder.build();
	}

	protected CsticModel createGears()
	{
		final CsticModelBuilder builder = new CsticModelBuilder().withInstance(ROOT_INST_ID, ROOT_INSTANCE_NAME);
		builder.withName(CONF_SCREWDRIVER_GEARS, DESCRIPTION_SCREWDRIVER_GEARS);
		builder.stringType().singleSelection();

		final boolean is3GearSelected = VARIANT_CODE_CONF_SCREWDRIVER_S_PROF_2_12.equals(getVariantProductCode())
				|| VARIANT_CODE_CONF_SCREWDRIVER_S_PROF_2_18.equals(getVariantProductCode());
		final boolean is2GearSelected = VARIANT_CODE_CONF_SCREWDRIVER_S_STD_2_12.equals(getVariantProductCode())
				|| VARIANT_CODE_CONF_SCREWDRIVER_S_STD_4_9.equals(getVariantProductCode());

		builder.addOption(CONF_SCREWDRIVER_1GEAR, DESCRIPTION_SCREWDRIVER_GEARS_1GEAR)
				.addOption(CONF_SCREWDRIVER_2GEAR, DESCRIPTION_SCREWDRIVER_GEARS_2GEAR, is2GearSelected)
				.addOption(CONF_SCREWDRIVER_3GEAR, DESCRIPTION_SCREWDRIVER_GEARS_3GEAR, is3GearSelected);

		builder.withDefaultUIState();
		return builder.build();
	}

	protected CsticModel createTorque()
	{
		final CsticModelBuilder builder = new CsticModelBuilder().withInstance(ROOT_INST_ID, ROOT_INSTANCE_NAME);
		builder.withName(CONF_SCREWDRIVER_TORQUE, DESCRIPTION_SCREWDRIVER_TORQUE);
		builder.stringType().singleSelection();

		final boolean is5090Selected = VARIANT_CODE_CONF_SCREWDRIVER_S_PROF_2_12.equals(getVariantProductCode())
				|| VARIANT_CODE_CONF_SCREWDRIVER_S_PROF_2_18.equals(getVariantProductCode());
		final boolean is1525Selected = VARIANT_CODE_CONF_SCREWDRIVER_S_STD_2_12.equals(getVariantProductCode())
				|| VARIANT_CODE_CONF_SCREWDRIVER_S_STD_4_9.equals(getVariantProductCode());

		builder.addOption(CONF_SCREWDRIVER_15_25NM, DESCRIPTION_SCREWDRIVER_TORQUE_15_25NM, is1525Selected)
				.addOption(CONF_SCREWDRIVER_25_45NM, DESCRIPTION_SCREWDRIVER_TORQUE_25_45NM)
				.addOption(CONF_SCREWDRIVER_50_90NM, DESCRIPTION_SCREWDRIVER_TORQUE_50_90NM, is5090Selected);

		builder.withDefaultUIState();
		return builder.build();
	}

	protected CsticModel createTorqueLevel()
	{
		final CsticModelBuilder builder = new CsticModelBuilder().withInstance(ROOT_INST_ID, ROOT_INSTANCE_NAME);
		builder.withName(CONF_SCREWDRIVER_TORQUE_LEVEL, DESCRIPTION_TORQUE_LEVEL);
		builder.stringType().singleSelection();

		final boolean is20LevelsEnabled = VARIANT_CODE_CONF_SCREWDRIVER_S_PROF_2_12.equals(getVariantProductCode())
				|| VARIANT_CODE_CONF_SCREWDRIVER_S_PROF_2_18.equals(getVariantProductCode());
		final boolean is12LevelsEnabled = VARIANT_CODE_CONF_SCREWDRIVER_S_STD_2_12.equals(getVariantProductCode());
		final boolean is16LevelsEnabled = VARIANT_CODE_CONF_SCREWDRIVER_S_STD_4_9.equals(getVariantProductCode());

		builder.addOption(CONF_SCREWDRIVER_12LEVELS, DESCRIPTION_TORQUE_LEVEL_12LEVELS, is12LevelsEnabled)
				.addOption(CONF_SCREWDRIVER_16LEVELS, DESCRIPTION_TORQUE_LEVEL_16LEVELS, is16LevelsEnabled)
				.addOption(CONF_SCREWDRIVER_20LEVELS, DESCRIPTION_TORQUE_LEVEL_20LEVELS, is20LevelsEnabled);

		builder.withDefaultUIState();
		return builder.build();
	}

	protected CsticModel createBattery()
	{
		final CsticModelBuilder builder = new CsticModelBuilder().withInstance(ROOT_INST_ID, ROOT_INSTANCE_NAME);
		builder.withName(CONF_SCREWDRIVER_BATTERY, DESCRIPTION_SCREWDRIVER_BATTERY);
		builder.stringType().singleSelection();

		final boolean is35Selected = VARIANT_CODE_CONF_SCREWDRIVER_S_PROF_2_12.equals(getVariantProductCode())
				|| VARIANT_CODE_CONF_SCREWDRIVER_S_PROF_2_18.equals(getVariantProductCode());
		final boolean is15Selected = VARIANT_CODE_CONF_SCREWDRIVER_S_STD_2_12.equals(getVariantProductCode());
		final boolean is25Selected = VARIANT_CODE_CONF_SCREWDRIVER_S_STD_4_9.equals(getVariantProductCode());

		builder.addOption(CONF_SCREWDRIVER_15AH, DESCRIPTION_SCREWDRIVER_BATTERY_1_5AH, is15Selected)
				.addOption(CONF_SCREWDRIVER_25AH, DESCRIPTION_SCREWDRIVER_BATTERY_2_5AH, is25Selected)
				.addOption(CONF_SCREWDRIVER_35AH, DESCRIPTION_SCREWDRIVER_BATTERY_3_5AH, is35Selected);
		builder.withDefaultUIState();
		return builder.build();
	}

	protected CsticModel createDrillChuckSize()
	{
		final CsticModelBuilder builder = new CsticModelBuilder().withInstance(ROOT_INST_ID, ROOT_INSTANCE_NAME);
		builder.withName(CONF_SCREWDRIVER_DRILL_CHUCK, DESCRIPTION_DRILL_CHUCK);
		builder.stringType().singleSelection();
		final boolean is2_12Selected = VARIANT_CODE_CONF_SCREWDRIVER_S_PROF_2_12.equals(getVariantProductCode())
				|| VARIANT_CODE_CONF_SCREWDRIVER_S_STD_2_12.equals(getVariantProductCode());
		final boolean is2_18Selected = VARIANT_CODE_CONF_SCREWDRIVER_S_PROF_2_18.equals(getVariantProductCode());
		final boolean is4_9Selected = VARIANT_CODE_CONF_SCREWDRIVER_S_STD_4_9.equals(getVariantProductCode());

		builder.addOption(CONF_SCREWDRIVER_2_12MM, DESCRIPTION_DRILL_CHUCK_2_12, is2_12Selected)
				.addOption(CONF_SCREWDRIVER_2_18MM, DESCRIPTION_DRILL_CHUCK_2_18, is2_18Selected)
				.addOption(CONF_SCREWDRIVER_4_9MM, DESCRIPTION_DRILL_CHUCK_4_9, is4_9Selected);
		builder.withDefaultUIState();
		return builder.build();
	}

	protected CsticModel createOutDoorCharging()
	{
		final CsticModelBuilder builder = new CsticModelBuilder().withInstance(ROOT_INST_ID, ROOT_INSTANCE_NAME);
		builder.withName(CONF_SCREWDRIVER_OUTDOOR_CHARGING, DESCRIPTION_OUTDOOR_CHARGING);
		builder.stringType().singleSelection();

		final boolean isChargingSelected = VARIANT_CODE_CONF_SCREWDRIVER_S_PROF_2_12.equals(getVariantProductCode())
				|| VARIANT_CODE_CONF_SCREWDRIVER_S_PROF_2_18.equals(getVariantProductCode());

		final boolean isChargingNoSelected = VARIANT_CODE_CONF_SCREWDRIVER_S_STD_2_12.equals(getVariantProductCode())
				|| VARIANT_CODE_CONF_SCREWDRIVER_S_STD_4_9.equals(getVariantProductCode());

		builder.addOption(CONF_SCREWDRIVER_OUTDOOR_CHARGING_YES, DESCRIPTION_SCREWDRIVER_YES, isChargingSelected)
				.addOption(CONF_SCREWDRIVER_OUTDOOR_CHARGING_NO, DESCRIPTION_SCREWDRIVER_NO, isChargingNoSelected);
		builder.withDefaultUIState();
		return builder.build();
	}

	protected CsticModel createAdditionalOptions()
	{
		final CsticModelBuilder builder = new CsticModelBuilder().withInstance(ROOT_INST_ID, ROOT_INSTANCE_NAME);
		builder.withName(CONF_SCREWDRIVER_ADDITIONAL_OPTIONS, DESCRIPTION_ADDTIONAL_OPTIONS);
		builder.stringType().multiSelection();

		final boolean isSelected = VARIANT_CODE_CONF_SCREWDRIVER_S_PROF_2_12.equals(getVariantProductCode())
				|| VARIANT_CODE_CONF_SCREWDRIVER_S_PROF_2_18.equals(getVariantProductCode());

		final boolean isSelectedMinimum = isSelected || VARIANT_CODE_CONF_SCREWDRIVER_S_STD_2_12.equals(getVariantProductCode())
				|| VARIANT_CODE_CONF_SCREWDRIVER_S_STD_4_9.equals(getVariantProductCode());

		builder.addOption(CONF_SCREWDRIVER_LED, DESCRIPTION_ADDITIONAL_OPTIONS_LED, isSelectedMinimum)
				.addOption(CONF_SCREWDRIVER_POWER_LEVEL, DESCRIPTION_ADDITIONAL_OPTIONS_POWER_LEVEL, isSelected)
				.addOption(CONF_SCREWDRIVER_ROTATION, DESCRIPTION_ADDITIONAL_OPTIONS_ROTATION, isSelectedMinimum)
				.addOption(CONF_SCREWDRIVER_SOUND, DESCRIPTION_ADDITIONAL_OPTIONS_SOUND, isSelected)
				.addOption(CONF_SCREWDRIVER_VIBRATION, DESCRIPTION_ADDITIONAL_OPTIONS_VIBRATION, isSelected);

		builder.withDefaultUIState();
		return builder.build();
	}

	protected CsticModel createAccessories()
	{
		final CsticModelBuilder builder = new CsticModelBuilder().withInstance(ROOT_INST_ID, ROOT_INSTANCE_NAME);
		builder.withName(CONF_SCREWDRIVER_ACCESSORY, DESCRIPTION_ACCESSORY);
		builder.stringType().multiSelection();

		final boolean isSelected = VARIANT_CODE_CONF_SCREWDRIVER_S_PROF_2_12.equals(getVariantProductCode())
				|| VARIANT_CODE_CONF_SCREWDRIVER_S_PROF_2_18.equals(getVariantProductCode());
		final boolean isSelectedBattery = isSelected || VARIANT_CODE_CONF_SCREWDRIVER_S_STD_2_12.equals(getVariantProductCode())
				|| VARIANT_CODE_CONF_SCREWDRIVER_S_STD_4_9.equals(getVariantProductCode());

		builder.addOption(CONF_SCREWDRIVER_SECONDBATTERY, DESCRIPTION_ACCESSORY_SECOND_BATTERY, isSelectedBattery)
				.addOption(CONF_SCREWDRIVER_BITS, DESCRIPTION_ACCESSORY_BITS, isSelected)
				.addOption(CONF_SCREWDRIVER_CASE, DESCRIPTION_ACCESSORY_CASE, isSelected)
				.addOption(CONF_SCREWDRIVER_DRILLS, DESCRIPTION_ACCESSORY_DRILLS, isSelected);
		builder.withDefaultUIState();
		return builder.build();
	}

	protected CsticModel createWarranty()
	{
		final CsticModelBuilder builder = new CsticModelBuilder().withInstance(ROOT_INST_ID, ROOT_INSTANCE_NAME);
		builder.withName(CONF_SCREWDRIVER_WARRANTY, DESCRIPTION_WARRANTY);
		builder.stringType().singleSelection();

		final boolean isWarrantySelected = VARIANT_CODE_CONF_SCREWDRIVER_S_PROF_2_12.equals(getVariantProductCode())
				|| VARIANT_CODE_CONF_SCREWDRIVER_S_PROF_2_18.equals(getVariantProductCode());
		final boolean isWarrantyNoSelected = VARIANT_CODE_CONF_SCREWDRIVER_S_STD_2_12.equals(getVariantProductCode())
				|| VARIANT_CODE_CONF_SCREWDRIVER_S_STD_4_9.equals(getVariantProductCode());

		builder.addOption(CONF_SCREWDRIVER_WARRANTY_YES, DESCRIPTION_SCREWDRIVER_YES, isWarrantySelected)
				.addOption(CONF_SCREWDRIVER_WARRANTY_NO, DESCRIPTION_SCREWDRIVER_NO, isWarrantyNoSelected);
		builder.withDefaultUIState();
		return builder.build();
	}

	@Override
	public void checkModel(final ConfigModel model)
	{
		setPrice(model);
		super.checkModel(model);
	}

	@Override
	public void checkCstic(final ConfigModel model, final InstanceModel instance, final CsticModel cstic)
	{
		super.checkCstic(model, instance, cstic);

		if (cstic.getName().equalsIgnoreCase(CONF_SCREWDRIVER_COL))
		{
			checkColor(instance, cstic);
		}

		if (cstic.getName().equalsIgnoreCase(CONF_SCREWDRIVER_OUTDOOR_CHARGING))
		{
			resetValuePrices(cstic);
			handleValuePrice(model, cstic, CONF_SCREWDRIVER_OUTDOOR_CHARGING_YES,
					BigDecimal.valueOf(SURCHARGE_CONF_SCREWDRIVER_OUTDOOR_CHARGING_YES));
		}

		if (cstic.getName().equalsIgnoreCase(CONF_SCREWDRIVER_WARRANTY))
		{
			resetValuePrices(cstic);
			handleValuePrice(model, cstic, CONF_SCREWDRIVER_WARRANTY_YES,
					BigDecimal.valueOf(SURCHARGE_CONF_SCREWDRIVER_WARRANTY_YES));
		}

		if (cstic.getName().equalsIgnoreCase(CONF_SCREWDRIVER_ACCESSORY))
		{
			resetValuePrices(cstic);
			handleValuePrice(model, cstic, CONF_SCREWDRIVER_SECONDBATTERY,
					BigDecimal.valueOf(SURCHARGE_CONF_SCREWDRIVER_SECONDBATTERY));
			handleValuePrice(model, cstic, CONF_SCREWDRIVER_BITS, BigDecimal.valueOf(SURCHARGE_CONF_SCREWDRIVER_BITS));
			handleValuePrice(model, cstic, CONF_SCREWDRIVER_CASE, BigDecimal.valueOf(SURCHARGE_CONF_SCREWDRIVER_CASE));
			handleValuePrice(model, cstic, CONF_SCREWDRIVER_DRILLS, BigDecimal.valueOf(SURCHARGE_CONF_SCREWDRIVER_DRILLS));
		}


	}





	protected void checkColor(final InstanceModel instance, final CsticModel cstic)
	{

		final List<CsticValueModel> assignedValues = instance.getCstic(CONF_SCREWDRIVER_MODE).getAssignedValues();

		if (!CollectionUtils.isEmpty(assignedValues))
		{
			if (assignedValues.get(0).getName().equals(CONF_SCREWDRIVER_PROFESSIONAL))
			{
				cstic.setSingleValue(CONF_SCREWDRIVER_BLUE);
			}

			if (assignedValues.get(0).getName().equals(CONF_SCREWDRIVER_STANDARD))
			{
				cstic.setSingleValue(CONF_SCREWDRIVER_GREEN);
			}

		}
	}

	protected List<CsticGroupModel> createCsticGroupList()
	{
		final List<CsticGroupModel> groups = new ArrayList<>();

		// General group:
		addCsticGroup(groups, InstanceModel.GENERAL_GROUP_NAME, null, null);

		//First group:
		addCsticGroup(groups, "1", "Basics", CONF_SCREWDRIVER_MODE, CONF_SCREWDRIVER_COL);

		//Second group:
		addCsticGroup(groups, "2", "Technical Specification", CONF_SCREWDRIVER_POWER, CONF_SCREWDRIVER_GEARS,
				CONF_SCREWDRIVER_TORQUE, CONF_SCREWDRIVER_TORQUE_LEVEL, CONF_SCREWDRIVER_BATTERY, CONF_SCREWDRIVER_DRILL_CHUCK);

		//Third group:
		addCsticGroup(groups, "3", "Add-Ons", CONF_SCREWDRIVER_OUTDOOR_CHARGING, CONF_SCREWDRIVER_ADDITIONAL_OPTIONS,
				CONF_SCREWDRIVER_ACCESSORY, CONF_SCREWDRIVER_WARRANTY);

		return groups;
	}
}
