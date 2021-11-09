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
import de.hybris.platform.sap.productconfig.runtime.interf.model.SolvableConflictModel;
import de.hybris.platform.sap.productconfig.runtime.interf.model.impl.CsticValueModelImpl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;


public class CPQBandsawMockImpl extends BaseRunTimeConfigMockImpl
{
	protected static final BigInteger BASE_PRICE = BigInteger.valueOf(250);
	public static final String DEEP_DRAWN_PARTY = "Deep drawn parts";
	public static final String CONF_BS_OTHERAPPTYPES_D = "CONF_BS_OTHERAPPTYPES_D";
	public static final String READY_MADE_PARTY = "Ready-made parts";
	public static final String CONF_BS_OTHERAPPTYPES_RM = "CONF_BS_OTHERAPPTYPES_RM";
	public static final String SANDWICH_ELEMETS = "Sandwich elements";
	public static final String CONF_BS_OTHERAPPTYPES_SW = "CONF_BS_OTHERAPPTYPES_SW";
	public static final String OTHER_TYPES_OF_APPLICATION = "Other Types of Application";
	public static final String CONF_BS_OTHERAPPTYPES = "CONF_BS_OTHERAPPTYPES";
	public static final String MANUALLY_ROTATABLE = "Manually rotatable";
	public static final String CONF_BS_SU_GUIDESYSTEM_M = "CONF_BS_SU_GUIDESYSTEM_M";
	public static final String PNEUMATIC = "Pneumatic";
	public static final String CONF_BS_SU_GUIDESYSTEM_P = "CONF_BS_SU_GUIDESYSTEM_P";
	public static final String GUIDE_SYSTEM = "Guide System";
	public static final String CONF_BS_SU_GUIDESYSTEM = "CONF_BS_SU_GUIDESYSTEM";
	public static final String FLAGGED = "Flagged";
	public static final String PENDELUM_SAW = "Pendelum saw";
	public static final String CONF_BS_SU_PENDSAW = "CONF_BS_SU_PENDSAW";
	public static final String XCXC = "xcxc";
	public static final String CONF_BS_VARIABLECUTTINGSPEED1 = "CONF_BS_VARIABLECUTTINGSPEED1";
	public static final String VARIABLE_CUTTING_SPEED = "Variable cutting speed";
	public static final String CONF_BS_VARIABLECUTTINGSPEED = "CONF_BS_VARIABLECUTTINGSPEED";
	public static final String CUTTING_CHIP_SUCTION = "Cutting chip suction";
	public static final String CONF_BS_CUTTINGCHIPSUCTION = "CONF_BS_CUTTINGCHIPSUCTION";
	public static final String SUCTION_CONVEYOR = "Suction conveyor";
	public static final String CONF_BS_SUCTIONCONVEYOR = "CONF_BS_SUCTIONCONVEYOR";
	public static final String CARBIDE = "Carbide";
	public static final String CONF_BS_SU_BLADES_CARB = "CONF_BS_SU_BLADES_CARB";
	public static final String BI_METAL = "Bi-Metal";
	public static final String CONF_BS_SU_BLADES_METAL = "CONF_BS_SU_BLADES_METAL";
	public static final String BLADES = "Bandsaw  Blades";
	public static final String CONF_BS_SU_BLADES = "CONF_BS_SU_BLADES";
	public static final String FITTED_TO_A_STAND = "Fitted to a stand";
	public static final String CONF_BS_BASE_STAND = "CONF_BS_BASE_STAND";
	public static final String BENCH_MOUNTED = "Bench mounted";
	public static final String CONF_BS_BASE_MOUNTED = "CONF_BS_BASE_MOUNTED";
	public static final String BANDSAW_MOUNTING = "Mounting";
	public static final String CONF_BS_BASE_MOUNTING = "CONF_BS_BASE_MOUNTING";
	public static final String CONF_BS_BASE_CUPBOARD_NO = "CONF_BS_BASE_CUPBOARD_NO";
	public static final String CONF_BS_BASE_CUPBOARD_YES = "CONF_BS_BASE_CUPBOARD_YES";
	public static final String INCLUDE_TOOL_CUPBOARD = "Include Tool Cupboard";
	public static final String CONF_BS_BASE_CUPBOARD = "CONF_BS_BASE_CUPBOARD";
	public static final String BEVEL_CUTTING = "Bevel Cutting";
	public static final String CONF_BS_BEVEL_CUT = "CONF_BS_BEVEL_CUT";
	public static final String MITRE = "Mitre";
	public static final String CONF_BS_MITRE = "CONF_BS_MITRE";
	public static final String RIPPING = "Ripping";
	public static final String CONF_BS_RIPPING = "CONF_BS_RIPPING";
	public static final String CROSS_CUTTING = "Cross Cutting";
	public static final String CONF_BS_CROSS_CUT = "CONF_BS_CROSS_CUT";
	public static final String STRAIGHT_CUTTING = "Straight Cutting";
	public static final String CONF_BS_STRAIGHT_CUT = "CONF_BS_STRAIGHT_CUT";
	public static final String WOODWORK_CAPABILITIES = "Woodwork capabilities";
	public static final String CONF_BS_WOODCP = "CONF_BS_WOODCP";
	public static final String _20 = "20\"";
	public static final String CONF_BS_XLDTH = "CONF_BS_XLDTH";
	public static final String _18 = "18\"";
	public static final String CONF_BS_LARGEWIDTH = "CONF_BS_LARGEWIDTH";
	public static final String _16 = "16\"";
	public static final String CONF_BS_MEDIUMWIDTH = "CONF_BS_MEDIUMWIDTH";
	public static final String _14 = "14\"";
	public static final String CONF_BS_SMALLWIDTH = "CONF_BS_SMALLWIDTH";
	public static final String THROAT_WIDTH = "Throat Width";
	public static final String CONF_BS_THROATWIDTH = "CONF_BS_THROATWIDTH";
	public static final String PUWR98_WORKPLACE = "PUWR98 (workplace)";
	public static final String CONF_BS_SAFETY_WORKPLACE = "CONF_BS_SAFETY_WORKPLACE";
	public static final String BS_4163_2014_EDUCATIONAL_ESTABLISHMENTS = "BS 4163:2014 (educational establishments)";
	public static final String CONF_BS_SAFETY_EDU = "CONF_BS_SAFETY_EDU";
	public static final String SAFETY_REQUIREMENTS = "Safety Requirements";
	public static final String CONF_BS_SAFETY = "CONF_BS_SAFETY";
	public static final String _12 = "12\"";
	public static final String CONF_BS_RESAWLARGE = "CONF_BS_RESAWLARGE";
	public static final String _10 = "10\"";
	public static final String CONF_BS_RESAWMEDIUM = "CONF_BS_RESAWMEDIUM";
	public static final String _6 = "6\"";
	public static final String CONF_BS_RESAWSMALL = "CONF_BS_RESAWSMALL";
	public static final String RESAW_CAPACITY = "Resaw Capacity";
	public static final String CONF_BS_RESAW_CP = "CONF_BS_RESAW_CP";
	public static final String _1100W = "1100W";
	public static final String CONF_BS_MOTOR_EXTRA = "CONF_BS_MOTOR_EXTRA";
	public static final String _550W = "550W";
	public static final String CONF_BS_MOTOR_NORMAL = "CONF_BS_MOTOR_NORMAL";
	public static final String MOTOR = "Motor";
	public static final String CONF_BS_MOTOR = "CONF_BS_MOTOR";
	public static final String CONF_BS_MOBILITY_NO = "CONF_BS_MOBILITY_NO";
	public static final String CONF_BS_MOBILITY_YES = "CONF_BS_MOBILITY_YES";
	public static final String MOBILITY_KIT = "Mobility kit";
	public static final String CONF_BS_MOBILITYKIT = "CONF_BS_MOBILITYKIT";
	public static final String WELDED_STEEL = "Welded Steel";
	public static final String CONF_BS_STEELFRAME = "CONF_BS_STEELFRAME";
	public static final String CAST_IRON = "Cast Iron";
	public static final String CONF_BS_IRONFRAME = "CONF_BS_IRONFRAME";
	public static final String FRAME_DESIGN = "Frame Design";
	public static final String SAW_BAND_RELIEF_WHEN_IDLING = "Saw band relief when idling";
	public static final String QUICK_RELEASE_BLADE_TENSIONING_LEVER = "Quick release blade tensioning lever";
	public static final String CONF_BS_FRAMEDESIGN = "CONF_BS_FRAMEDESIGN";
	public static final String CONF_BS_BANDRELIEF = "CONF_BS_BANDRELIEF";
	public static final String CONF_BS_QUICKRELEASEBLADE = "CONF_BS_QUICKRELEASEBLADE";
	public static final String TILT_ANGLE_RIGHT = "Tilting angle right";
	public static final String TILT_ANGLE_LEFT = "Tilting angle left";
	public static final String CONF_BS_TILT_ANGLE_R = "CONF_BS_TILT_ANGLE_R";
	public static final String CONF_BS_TILT_ANGLE_L = "CONF_BS_TILT_ANGLE_L";
	public static final String NO = "No";
	public static final String YES = "Yes";
	public static final String CONF_BS_TILT_NO = "CONF_BS_TILT_NO";
	public static final String CONF_BS_TILT_YES = "CONF_BS_TILT_YES";
	public static final String TILT_TABLE = "Tilting Table";
	public static final String CONF_BS_TILTTABLE = "CONF_BS_TILTTABLE";
	public static final String ADDITIONAL_OPTIONS = "Additional Options";
	public static final String CONF_BS_ADDOPTIONS = "CONF_BS_ADDOPTIONS";

	public static final String ROOT_INSTANCE_NAME = "CONF_BANDSAW_ML";
	public static final String ROOT_INSTANCE_LANG_DEP_NAME = "Bandsaw mock instance";
	public static final String BASE_SUBINSTANCE_ID = "2";
	public static final String BASE_SUBINSTANCE_NAME = "CONF_BANDSAW_BASE";
	public static final String BASE_SUBINSTANCE_LANG_DEP_NAME = "Base";
	public static final String SAW_UNIT_SUBINSTANCE_ID = "3";
	public static final String SAW_UNIT_SUBINSTANCE_NAME = "CONF_BANDSAW_SAWINGUNIT";
	public static final String SAW_UNIT_SUBINSTANCE_LANG_DEP_NAME = "Sawing Unit";

	public static final int NUM_CSTIC_TYPE_LENGTH = 2;
	public static final int NUM_MAX_VALUE = 90;

	private static final Logger LOG = Logger.getLogger(CPQBandsawMockImpl.class);

	protected static final long SURCHARGE_CONF_BS_LARGEWIDTH = 30;
	protected static final long SURCHARGE_CONF_BS_MOTOR_EXTRA = 70;
	private static final long SURCHARGE_CONF_BS_XLDTH = 50;
	private static final long SURCHARGE_CONF_BS_BASE_STAND = 30;
	private static final long SURCHARGE_CONF_BS_MOBILITY_YES = 20;

	@Override
	public ConfigModel createDefaultConfiguration()
	{

		// Model
		final ConfigModel model = createDefaultConfigModel("Configuration for Bandsaw " + getConfigId(), false);

		createPrice(model);

		// root instance
		final InstanceModel rootInstance = createDefaultRootInstance(model, ROOT_INSTANCE_NAME, ROOT_INSTANCE_LANG_DEP_NAME);

		// cstic groups
		final List<CsticGroupModel> csticGroups = createCsticGroupList();
		rootInstance.setCsticGroups(csticGroups);

		// Characteristics and Values
		final List<CsticModel> cstics = new ArrayList<>();
		cstics.add(createFramedesign());
		cstics.add(createMobilityKit());
		cstics.add(createMotor());
		cstics.add(createResawCapacity());
		cstics.add(createSafety());
		cstics.add(createThroatWidth());
		cstics.add(createWoodworkCapabilities());
		cstics.add(createTiltTable());
		cstics.add(createTiltAngleL());
		cstics.add(createTiltAngleR());
		cstics.add(createOtherTypesOfApplication());
		rootInstance.setCstics(cstics);

		//Subinstance Base
		final InstanceModel subInstanceBase = createSubInstance(BASE_SUBINSTANCE_ID, BASE_SUBINSTANCE_NAME,
				BASE_SUBINSTANCE_LANG_DEP_NAME);

		final List<CsticGroupModel> baseSubInstanceCsticGroups = new ArrayList<>();
		addCsticGroup(baseSubInstanceCsticGroups, InstanceModel.GENERAL_GROUP_NAME, "", CONF_BS_BASE_MOUNTING,
				CONF_BS_BASE_CUPBOARD);
		subInstanceBase.setCsticGroups(baseSubInstanceCsticGroups);

		final List<CsticModel> subInstanceBaseCstics = new ArrayList<>();
		subInstanceBaseCstics.add(createCupboard());
		subInstanceBaseCstics.add(createBaseMounting());
		subInstanceBase.setCstics(subInstanceBaseCstics);

		//Subinstance Saw Unit
		final InstanceModel subInstanceSawUnit = createSubInstance(SAW_UNIT_SUBINSTANCE_ID, SAW_UNIT_SUBINSTANCE_NAME,
				SAW_UNIT_SUBINSTANCE_LANG_DEP_NAME);

		final List<CsticGroupModel> sawUnitSubInstanceCsticGroups = new ArrayList<>();
		addCsticGroup(sawUnitSubInstanceCsticGroups, InstanceModel.GENERAL_GROUP_NAME, "", CONF_BS_SU_BLADES,
				CONF_BS_SU_GUIDESYSTEM, CONF_BS_SU_PENDSAW, CONF_BS_ADDOPTIONS);
		subInstanceSawUnit.setCsticGroups(sawUnitSubInstanceCsticGroups);

		final List<CsticModel> subInstanceSawUnitCstics = new ArrayList<>();
		subInstanceSawUnitCstics.add(createBlades());
		subInstanceSawUnitCstics.add(createAdditionalOptions());
		subInstanceSawUnitCstics.add(createPendsaw());
		subInstanceSawUnitCstics.add(createGuideSystem());
		subInstanceSawUnit.setCstics(subInstanceSawUnitCstics);

		final List<InstanceModel> subInstances = new ArrayList<>();
		subInstances.add(subInstanceBase);
		subInstances.add(subInstanceSawUnit);
		rootInstance.setSubInstances(subInstances);

		return model;
	}

	protected void createPrice(final ConfigModel model)
	{
		final PriceModel basePrice = createPrice(BASE_PRICE.intValue());
		model.setBasePrice(basePrice);

		final PriceModel selectedOptionsPrice = createPrice(0);
		model.setSelectedOptionsPrice(selectedOptionsPrice);

		final PriceModel currentTotalPrice = createPrice(BASE_PRICE.intValue());
		model.setCurrentTotalPrice(currentTotalPrice);
	}

	protected CsticModel createOtherTypesOfApplication()
	{
		final CsticModelBuilder builder = new CsticModelBuilder().withInstance(ROOT_INST_ID, ROOT_INSTANCE_NAME);
		builder.withName(CONF_BS_OTHERAPPTYPES, OTHER_TYPES_OF_APPLICATION);
		builder.stringType().multiSelection();
		builder.addOption(CONF_BS_OTHERAPPTYPES_SW, SANDWICH_ELEMETS).addOption(CONF_BS_OTHERAPPTYPES_RM, READY_MADE_PARTY)
				.addOption(CONF_BS_OTHERAPPTYPES_D, DEEP_DRAWN_PARTY);
		builder.withDefaultUIState();
		return builder.build();
	}

	protected CsticModel createFramedesign()
	{
		final CsticModelBuilder builder = new CsticModelBuilder().withInstance(ROOT_INST_ID, ROOT_INSTANCE_NAME);
		builder.withName(CONF_BS_FRAMEDESIGN, FRAME_DESIGN);
		builder.stringType().singleSelection();
		builder.addSelectedOption(CONF_BS_IRONFRAME, CAST_IRON).addOption(CONF_BS_STEELFRAME, WELDED_STEEL);
		builder.withDefaultUIState();
		return builder.build();
	}

	protected CsticModel createMobilityKit()
	{
		final CsticModelBuilder builder = new CsticModelBuilder().withInstance(ROOT_INST_ID, ROOT_INSTANCE_NAME);
		builder.withName(CONF_BS_MOBILITYKIT, MOBILITY_KIT);
		builder.stringType().singleSelection();
		builder.addSelectedOption(CONF_BS_MOBILITY_YES, YES).addOption(CONF_BS_MOBILITY_NO, NO);
		builder.withDefaultUIState();
		return builder.build();
	}

	protected CsticModel createMotor()
	{
		final CsticModelBuilder builder = new CsticModelBuilder().withInstance(ROOT_INST_ID, ROOT_INSTANCE_NAME);
		builder.withName(CONF_BS_MOTOR, MOTOR);
		builder.stringType().singleSelection();
		builder.addSelectedOption(CONF_BS_MOTOR_NORMAL, _550W).addOption(CONF_BS_MOTOR_EXTRA, _1100W);
		builder.withDefaultUIState();
		return builder.build();
	}

	protected CsticModel createResawCapacity()
	{
		final CsticModelBuilder builder = new CsticModelBuilder().withInstance(ROOT_INST_ID, ROOT_INSTANCE_NAME);
		builder.withName(CONF_BS_RESAW_CP, RESAW_CAPACITY);
		builder.stringType().singleSelection();
		builder.addSelectedOption(CONF_BS_RESAWSMALL, _6).addOption(CONF_BS_RESAWMEDIUM, _10).addOption(CONF_BS_RESAWLARGE, _12);
		builder.withDefaultUIState();
		return builder.build();
	}

	protected CsticModel createSafety()
	{
		final CsticModelBuilder builder = new CsticModelBuilder().withInstance(ROOT_INST_ID, ROOT_INSTANCE_NAME);
		builder.withName(CONF_BS_SAFETY, SAFETY_REQUIREMENTS);
		builder.stringType().multiSelection();
		builder.addSelectedOption(CONF_BS_SAFETY_EDU, BS_4163_2014_EDUCATIONAL_ESTABLISHMENTS)
				.addSelectedOption(CONF_BS_SAFETY_WORKPLACE, PUWR98_WORKPLACE);
		builder.withDefaultUIState();
		return builder.build();
	}

	protected CsticModel createThroatWidth()
	{
		final CsticModelBuilder builder = new CsticModelBuilder().withInstance(ROOT_INST_ID, ROOT_INSTANCE_NAME);
		builder.withName(CONF_BS_THROATWIDTH, THROAT_WIDTH);
		builder.stringType().singleSelection();
		builder.addOption(CONF_BS_SMALLWIDTH, _14).addSelectedOption(CONF_BS_MEDIUMWIDTH, _16).addOption(CONF_BS_LARGEWIDTH, _18)
				.addOption(CONF_BS_XLDTH, _20);
		builder.withDefaultUIState();
		return builder.build();
	}

	protected CsticModel createWoodworkCapabilities()
	{
		final CsticModelBuilder builder = new CsticModelBuilder().withInstance(ROOT_INST_ID, ROOT_INSTANCE_NAME);
		builder.withName(CONF_BS_WOODCP, WOODWORK_CAPABILITIES);
		builder.stringType().multiSelection();
		builder.addOption(CONF_BS_STRAIGHT_CUT, STRAIGHT_CUTTING).addOption(CONF_BS_CROSS_CUT, CROSS_CUTTING)
				.addOption(CONF_BS_RIPPING, RIPPING).addOption(CONF_BS_MITRE, MITRE).addOption(CONF_BS_BEVEL_CUT, BEVEL_CUTTING);
		builder.withDefaultUIState();
		return builder.build();
	}

	protected CsticModel createTiltTable()
	{
		final CsticModelBuilder builder = new CsticModelBuilder().withInstance(ROOT_INST_ID, ROOT_INSTANCE_NAME);
		builder.withName(CONF_BS_TILTTABLE, TILT_TABLE);
		builder.stringType().singleSelection();
		builder.addOption(CONF_BS_TILT_YES, YES).addOption(CONF_BS_TILT_NO, NO);
		builder.withDefaultUIState();
		return builder.build();
	}

	protected CsticModel createTiltAngleL()
	{
		final CsticModelBuilder builder = new CsticModelBuilder().withInstance(ROOT_INST_ID, ROOT_INSTANCE_NAME);
		builder.withName(CONF_BS_TILT_ANGLE_L, TILT_ANGLE_LEFT);
		builder.numericType(0, NUM_CSTIC_TYPE_LENGTH).addAssignableValue(createAssignableForIntervals("-10 - -5"));
		builder.withDefaultUIState().hidden().withPlaceHolder("-10 - -5");
		return builder.build();
	}

	protected CsticModel createTiltAngleR()
	{
		final CsticModelBuilder builder = new CsticModelBuilder().withInstance(ROOT_INST_ID, ROOT_INSTANCE_NAME);
		builder.withName(CONF_BS_TILT_ANGLE_R, TILT_ANGLE_RIGHT);
		builder.numericType(0, NUM_CSTIC_TYPE_LENGTH).addAssignableValue(createAssignableForIntervals("45 - 60"));
		builder.withDefaultUIState().hidden().withPlaceHolder("45 - 60");
		return builder.build();
	}

	protected CsticModel createCupboard()
	{
		final CsticModelBuilder builder = new CsticModelBuilder().withInstance(BASE_SUBINSTANCE_ID, BASE_SUBINSTANCE_NAME);
		builder.withName(CONF_BS_BASE_CUPBOARD, INCLUDE_TOOL_CUPBOARD);
		builder.stringType().singleSelection();
		builder.addOption(CONF_BS_BASE_CUPBOARD_YES, YES).addSelectedOption(CONF_BS_BASE_CUPBOARD_NO, NO);
		builder.withDefaultUIState().hidden();
		return builder.build();
	}

	protected CsticModel createBaseMounting()
	{
		final CsticModelBuilder builder = new CsticModelBuilder().withInstance(BASE_SUBINSTANCE_ID, BASE_SUBINSTANCE_NAME);
		builder.withName(CONF_BS_BASE_MOUNTING, BANDSAW_MOUNTING);
		builder.stringType().singleSelection();
		builder.addSelectedOption(CONF_BS_BASE_MOUNTED, BENCH_MOUNTED).addOption(CONF_BS_BASE_STAND, FITTED_TO_A_STAND);
		builder.withDefaultUIState();
		return builder.build();
	}

	protected CsticModel createBlades()
	{
		final CsticModelBuilder builder = new CsticModelBuilder().withInstance(SAW_UNIT_SUBINSTANCE_ID, SAW_UNIT_SUBINSTANCE_NAME);
		builder.withName(CONF_BS_SU_BLADES, BLADES);
		builder.stringType().singleSelection();
		builder.addSelectedOption(CONF_BS_SU_BLADES_METAL, BI_METAL).addOption(CONF_BS_SU_BLADES_CARB, CARBIDE);
		builder.withDefaultUIState();
		return builder.build();
	}

	protected CsticModel createPendsaw()
	{
		final CsticModelBuilder builder = new CsticModelBuilder().withInstance(SAW_UNIT_SUBINSTANCE_ID, SAW_UNIT_SUBINSTANCE_NAME);
		builder.withName(CONF_BS_SU_PENDSAW, PENDELUM_SAW);
		builder.simpleFlag(FLAGGED);
		builder.withDefaultUIState();
		return builder.build();
	}

	protected CsticModel createGuideSystem()
	{
		final CsticModelBuilder builder = new CsticModelBuilder().withInstance(SAW_UNIT_SUBINSTANCE_ID, SAW_UNIT_SUBINSTANCE_NAME);
		builder.withName(CONF_BS_SU_GUIDESYSTEM, GUIDE_SYSTEM);
		builder.stringType().singleSelection();
		builder.addOption(CONF_BS_SU_GUIDESYSTEM_P, PNEUMATIC).addOption(CONF_BS_SU_GUIDESYSTEM_M, MANUALLY_ROTATABLE);
		builder.withDefaultUIState();
		return builder.build();
	}

	protected CsticModel createAdditionalOptions()
	{
		final CsticModelBuilder builder = new CsticModelBuilder().withInstance(SAW_UNIT_SUBINSTANCE_ID, SAW_UNIT_SUBINSTANCE_NAME);
		builder.withName(CONF_BS_ADDOPTIONS, ADDITIONAL_OPTIONS);
		builder.stringType().multiSelection();
		builder.addSelectedOption(CONF_BS_QUICKRELEASEBLADE, QUICK_RELEASE_BLADE_TENSIONING_LEVER)
				.addOption(CONF_BS_BANDRELIEF, SAW_BAND_RELIEF_WHEN_IDLING).addOption(CONF_BS_SUCTIONCONVEYOR, SUCTION_CONVEYOR)
				.addOption(CONF_BS_CUTTINGCHIPSUCTION, CUTTING_CHIP_SUCTION)
				.addOption(CONF_BS_VARIABLECUTTINGSPEED, VARIABLE_CUTTING_SPEED);
		builder.withDefaultUIState();
		return builder.build();
	}

	protected InstanceModel createSubInstance(final String instanceId, final String name, final String langDepName)
	{
		final InstanceModel subInstance = createInstance();
		subInstance.setId(instanceId);
		subInstance.setName(name);
		subInstance.setLanguageDependentName(langDepName);
		subInstance.setRootInstance(false);
		subInstance.setComplete(true);
		subInstance.setConsistent(true);
		subInstance.setSubInstances(Collections.emptyList());

		return subInstance;
	}

	protected CsticValueModel createAssignableForIntervals(final String intervalValue)
	{
		final CsticValueModel csticsValue = new CsticValueModelImpl();
		csticsValue.setLanguageDependentName(intervalValue);
		csticsValue.setName(intervalValue);
		csticsValue.setDomainValue(true);
		return csticsValue;
	}

	public CsticValueModel retrieveValue(final InstanceModel instance, final String csticName)
	{
		CsticValueModel value = null;

		final List<CsticModel> cstics = instance.getCstics();
		for (final CsticModel cstic : cstics)
		{
			if (cstic.getName().equalsIgnoreCase(csticName))
			{
				final List<CsticValueModel> values = cstic.getAssignedValues();
				if (values != null && !values.isEmpty())
				{
					value = values.get(0);
				}
				break;
			}
		}
		return value;
	}

	@Override
	public void checkModel(final ConfigModel model)
	{
		createPrice(model);
		super.checkModel(model);
	}

	@Override
	public void checkCstic(final ConfigModel model, final InstanceModel instance, final CsticModel cstic)
	{
		super.checkCstic(model, instance, cstic);

		if (cstic.getName().equalsIgnoreCase(CONF_BS_TILT_ANGLE_L) || cstic.getName().equalsIgnoreCase(CONF_BS_TILT_ANGLE_R))
		{
			checkTiltTable(instance, cstic);
			final String strValue = cstic.getSingleValue();
			if (!StringUtils.isEmpty(strValue))
			{
				handleFloatInterval(model, instance, cstic);
			}
		}

		if (cstic.getName().equalsIgnoreCase(CONF_BS_BASE_CUPBOARD))
		{
			checkBaseMount(instance, cstic);
		}

		if (cstic.getName().equalsIgnoreCase(CONF_BS_THROATWIDTH))
		{
			resetValuePrices(cstic);
			handleValuePrice(model, cstic, CONF_BS_LARGEWIDTH, BigDecimal.valueOf(SURCHARGE_CONF_BS_LARGEWIDTH));
			handleValuePrice(model, cstic, CONF_BS_XLDTH, BigDecimal.valueOf(SURCHARGE_CONF_BS_XLDTH));
		}

		if (cstic.getName().equalsIgnoreCase(CONF_BS_MOTOR))
		{
			resetValuePrices(cstic);
			handleValuePrice(model, cstic, CONF_BS_MOTOR_EXTRA, BigDecimal.valueOf(SURCHARGE_CONF_BS_MOTOR_EXTRA));
		}

		if (cstic.getName().equalsIgnoreCase(CONF_BS_BASE_MOUNTING))
		{
			resetValuePrices(cstic);
			handleValuePrice(model, cstic, CONF_BS_BASE_STAND, BigDecimal.valueOf(SURCHARGE_CONF_BS_BASE_STAND));
		}

		if (cstic.getName().equalsIgnoreCase(CONF_BS_MOBILITYKIT))
		{
			resetValuePrices(cstic);
			handleValuePrice(model, cstic, CONF_BS_MOBILITY_YES, BigDecimal.valueOf(SURCHARGE_CONF_BS_MOBILITY_YES));
		}


	}




	protected DecimalFormat handelDecimalFormat(final CsticModel cstic)
	{
		final DecimalFormat format;

		format = new DecimalFormat("#0.0", DecimalFormatSymbols.getInstance(Locale.ENGLISH));
		format.setParseBigDecimal(true);
		format.setMaximumFractionDigits(cstic.getNumberScale());
		return format;
	}


	protected void handleFloatInterval(final ConfigModel model, final InstanceModel instance, final CsticModel cstic)
	{

		final List<Float> floatIntervals = new ArrayList<>();

		extractIntervalBoundaries(cstic, floatIntervals);
		if (!isInInterval(model, instance, cstic, floatIntervals))
		{
			checkConflict(model, instance, cstic);
		}

	}

	public boolean isInInterval(final ConfigModel model, final InstanceModel instance, final CsticModel cstic,
			final List<Float> floatIntervals)
	{
		float floatFrom;
		float floatUntil;
		boolean isInInterval = false;

		Number numValue = null;
		final DecimalFormat format = handelDecimalFormat(cstic);
		final String strValue = cstic.getSingleValue();
		if (!StringUtils.isEmpty(strValue))
		{
			try
			{
				numValue = format.parse(strValue);
			}
			catch (final ParseException e)
			{
				LOG.error("String value cannot be parsed. " + e);
			}
		}

		final int ITERATOR_SIZE_MIN_MAX = 2;
		for (int ii = 0; ii < floatIntervals.size(); ii = ii + ITERATOR_SIZE_MIN_MAX)
		{
			floatFrom = floatIntervals.get(ii).floatValue();
			floatUntil = floatIntervals.get(ii + 1).floatValue();
			if (numValue != null && numValue.floatValue() >= floatFrom && numValue.floatValue() <= floatUntil)
			{
				isInInterval = true;
			}
		}

		return isInInterval;

	}

	public void extractIntervalBoundaries(final CsticModel cstic, final List<Float> floatIntervals)
	{
		for (int i = 0; i < cstic.getAssignableValues().size(); i++)
		{
			final String interval = cstic.getAssignableValues().get(i).getLanguageDependentName();
			final int numberOfHyphens = StringUtils.countMatches(interval, "-");

			final int ARRAY_SIZE_MIN_AND_MAX_VALUE = 2;
			String[] values = new String[ARRAY_SIZE_MIN_AND_MAX_VALUE];
			if (numberOfHyphens == 1)
			{
				values = interval.split("-");
			}
			else
			{
				final int indexofSeparator = interval.indexOf('-', 1);
				final String lowValue = interval.substring(0, indexofSeparator);
				values[0] = lowValue;
				final int beginV = indexofSeparator + 1;
				final int endV = interval.length();

				final String maxValue = interval.substring(beginV, endV);
				values[1] = maxValue;

			}

			for (int j = 0; j < values.length; j++)
			{
				floatIntervals.add(Float.valueOf(values[j].trim()));
			}

		}
	}

	protected void checkConflict(final ConfigModel model, final InstanceModel instance, final CsticModel cstic)
	{
		if (cstic.getAssignedValues().isEmpty())
		{
			return;
		}

		final CsticValueModel assignedValue = cstic.getAssignedValues().get(0);

		if (assignedValue != null)
		{
			final List<SolvableConflictModel> solvableConflictModelList = model.getSolvableConflicts();
			final DecimalFormat format = handelDecimalFormat(cstic);
			String formattedValue;
			try
			{
				formattedValue = format.format(format.parse(assignedValue.getName()));
			}
			catch (final ParseException e)
			{
				formattedValue = assignedValue.getName();
				LOG.debug("Parsing Exception in mock: ", e);
			}
			final String conflictText = "Value " + formattedValue + " is not part of interval " + cstic.getPlaceholder();
			final SolvableConflictModel conflict = createSolvableConflict(assignedValue, cstic, instance, conflictText);
			solvableConflictModelList.add(conflict);
			model.setSolvableConflicts(solvableConflictModelList);
			cstic.setConsistent(false);
		}
	}


	protected void checkBaseMount(final InstanceModel instance, final CsticModel cstic)
	{
		final CsticValueModel value = retrieveValue(instance, CONF_BS_BASE_MOUNTING);

		if (value != null && value.getName().equalsIgnoreCase(CONF_BS_BASE_STAND))
		{
			cstic.setVisible(true);
		}
		else
		{
			cstic.setVisible(false);
		}
	}

	protected void checkTiltTable(final InstanceModel instance, final CsticModel cstic)
	{
		final CsticValueModel value = retrieveValue(instance, CONF_BS_TILTTABLE);

		if (value != null && value.getName().equalsIgnoreCase(CONF_BS_TILT_YES))
		{
			cstic.setVisible(true);
		}
		else
		{
			cstic.setVisible(false);
		}
	}

	protected List<CsticGroupModel> createCsticGroupList()
	{
		final List<CsticGroupModel> groups = new ArrayList<>();

		// General group:
		addCsticGroup(groups, InstanceModel.GENERAL_GROUP_NAME, null, null);

		//First group:
		addCsticGroup(groups, "1", "General", CONF_BS_WOODCP, CONF_BS_THROATWIDTH, CONF_BS_SAFETY, CONF_BS_RESAW_CP, CONF_BS_MOTOR,
				CONF_BS_MOBILITYKIT, CONF_BS_FRAMEDESIGN, CONF_BS_OTHERAPPTYPES);

		//Second group:
		addCsticGroup(groups, "2", "Table", CONF_BS_TILTTABLE, CONF_BS_TILT_ANGLE_L, CONF_BS_TILT_ANGLE_R);

		return groups;
	}


}
