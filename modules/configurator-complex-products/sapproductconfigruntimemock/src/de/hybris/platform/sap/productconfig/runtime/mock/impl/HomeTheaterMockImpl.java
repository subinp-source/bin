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

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;

import com.google.common.collect.ImmutableSet;


public class HomeTheaterMockImpl extends BaseRunTimeConfigMockImpl
{
	protected static final String CONFLICT_TEXT_PROJECTOR = "You need to set a projector type as you speficied a light source";
	protected static final String CONFLICT_TEXT_PROJECTION_SCREEN = "Projector type is needed as you specified a projection screen";
	protected static final String CONFLICT_TEXT_PANEL_TV = "Not possible to select a projector type if a panel TV has been selected";
	protected static final String CONFLICT_TEXT_GAMING = "Gaming console cannot be selected with LCD projector";
	protected static final long BASE_PRICE = 1800;
	public static final String CONFIG_NAME = "Config Name";
	public static final String ROOT_INSTANCE_ID = ROOT_INST_ID;
	public static final String ROOT_INSTANCE_NAME = "CONF_HOME_THEATER_ML";
	public static final String ROOT_INSTANCE_LANG_DEP_NAME = "Configurable Home Theater";
	public static final String KB_ID = "23";

	protected static final String VIDEO_SYSTEM_SUBINSTANCE_ID = "2";
	public static final String VIDEO_SYSTEM_SUBINSTANCE_NAME = "CONF_HT_VIDEO_SYSTEM";
	public static final String VIDEO_SYSTEM_SUBINSTANCE_LANG_DEP_NAME = "Video System";

	protected static final String AUDIO_SYSTEM_SUBINSTANCE_ID = "3";
	public static final String AUDIO_SYSTEM_SUBINSTANCE_NAME = "CONF_HT_AUDIO_SYSTEM";
	public static final String AUDIO_SYSTEM_SUBINSTANCE_LANG_DEP_NAME = "Audio System";

	protected static final String SOURCE_COMPONENTS_SUBINSTANCE_ID = "4";
	public static final String SOURCE_COMPONENTS_SUBINSTANCE_NAME = "CONF_HT_COMPONENTS_SYSTEM";
	public static final String SOURCE_COMPONENTS_SUBINSTANCE_LANG_DEP_NAME = "Source Components";

	public static final String PROJECTION_SCREEN = "PROJECTION_SCREEN";
	private static final String LANG_DEPENDENT_PROJECTION_SCREEN = "Projection Screen";
	protected static final String PROJSCREEN_FIXED = "FIXED_SCREEN";
	private static final String PROJSCREEN_FIXED_DESCRIPTION = "Fixed-frame Screen";
	protected static final String PROJSCREEN_PULLDOWN = "PULLDOWN_SCREEN";
	private static final String PROJSCREEN_PULLDOWN_DESCRIPTION = "Pulldown Screen";

	protected static final String MOTORIZED_PULLDOWN = "MOTORIZED_PULLDOWN";
	private static final String LANG_DEPENDENT_MOTORIZED_PULLDOWN = "Motorized";

	public static final String FLAT_PANEL_TV = "FLAT_PANEL_TV";
	private static final String LANG_DEPENDENT_FLAT_PANEL_TV = "Flat-panel TV";
	private static final String FLAT_PANEL_TV_HDTV = "HDTV";
	private static final String FLAT_PANEL_TV_HDTV_DESCRIPTION = "HDTV";
	private static final String FLAT_PANEL_TV_LCD = "LCD";
	private static final String FLAT_PANEL_TV_LCD_DESCRIPTION = "LCD";
	private static final String FLAT_PANEL_TV_OLED = "OLED";
	private static final String FLAT_PANEL_TV_OLED_DESCRIPTION = "OLED";

	public static final String PROJECTOR_TYPE = "PROJECTOR_TYPE";
	private static final String LANG_DEPENDENT_PROJECTOR_TYPE = "Projector Type";
	public static final String PROJECTOR_NONE = "PROJECTOR_NONE";
	private static final String PROJECTOR_NONE_DESCRIPTION = "No Projector";
	public static final String PROJECTOR_DLP = "PROJECTOR_DLP";
	private static final String PROJECTOR_DLP_DESCRIPTION = "Digital Light Processing (DLP)";
	protected static final String PROJECTOR_LCD = "PROJECTOR_LCD";
	protected static final String PROJECTOR_LCD_DESCRIPTION = "Liquid Crystal Display (LCD)";

	public static final String LIGHT_SOURCE = "LIGHT_SOURCE";
	private static final String LANG_DEPENDENT_LIGHT_SOURCE = "Light Source";
	protected static final String LIGHT_SOURCE_LAMP = "LIGHT_SOURCE_LAMP";
	private static final String LIGHT_SOURCE_LAMP_DESCRIPTION = "Lamp";
	private static final String LIGHT_SOURCE_LED = "LIGHT_SOURCE_LED";
	private static final String LIGHT_SOURCE_LED_DESCRIPTION = "LED";
	private static final String LIGHT_SOURCE_LASER = "LIGHT_SOURCE_LASER";
	private static final String LIGHT_SOURCE_LASER_DESCRIPTION = "Laser";

	public static final String ROOM_SIZE = "ROOM_SIZE";
	private static final String LANG_DEPENDENT_ROOM_SIZE = "Room size";
	public static final String ROOM_SIZE_SMALL = "ROOM_SIZE_SMALL";
	private static final String ROOM_SIZE_SMALL_DESCRIPTION = "10-24 sqm";
	public static final String ROOM_SIZE_MEDIUM = "ROOM_SIZE_MEDIUM";
	private static final String ROOM_SIZE_MEDIUM_DESCRIPTION = "25-40 sqm";
	public static final String ROOM_SIZE_LARGE = "ROOM_SIZE_LARGE";
	private static final String ROOM_SIZE_LARGE_DESCRIPTION = "41-60 sqm";

	private static final String COLOUR_HT = "COLOUR_HT";
	private static final String LANG_DEPENDENT_COLOUR_HT = "Color of Home Theater System";
	private static final String COLOUR_HT_BLACK = "COLOUR_HT_BLACK";
	private static final String COLOUR_HT_BLACK_DESCRIPTION = "Black";
	private static final String COLOUR_HT_WHITE = "COLOUR_HT_WHITE";
	private static final String COLOUR_HT_WHITE_DESCRIPTION = "White";
	private static final String COLOUR_HT_TITAN = "COLOUR_HT_TITAN";
	private static final String COLOUR_HT_TITAN_DESCRIPTION = "Titan";

	protected static final String REAR_SPEAKER = "REAR_SPEAKER";
	private static final String LANG_DEPENDENT_REAR_SPEAKER = "Rear Speakers";
	private static final String REAR_SPEAKER_CRP = "REAR_SPEAKER_CRP";
	private static final String REAR_SPEAKER_CRP_DESCRIPTION = "Philips CRP836";
	private static final String REAR_SPEAKER_CANTON = "REAR_SPEAKER_CANTON";
	private static final String REAR_SPEAKER_CANTON_DESCRIPTION = "Canton Movie 165";
	private static final String REAR_SPEAKER_SONOS = "REAR_SPEAKER_SONOS";
	private static final String REAR_SPEAKER_SONOS_DESCRIPTION = "Sonos One";

	protected static final String SUBWOOFER = "SUBWOOFER";
	private static final String LANG_DEPENDENT_SUBWOOFER = "Subwoofer";
	private static final String SUBWOOFER_LOEWE = "SUBWOOFER_LOEWE";
	private static final String SUBWOOFER_LOEWE_DESCRIPTION = "LOEWE 525 W";
	private static final String SUBWOOFER_SONOS = "SUBWOOFER_SONOS";
	private static final String SUBWOOFER_SONOS_DESCRIPTION = "Sonos SUB";

	public static final String SPEAKERTYPE = "SPEAKER_TYPE";
	private static final String LANG_DEPENDENT_SPEAKERTYPE = "Speaker Type";
	public static final String TYPE_SOUNDBAR = "TYPE_SOUNDBAR";
	private static final String TYPE_SOUNDBAR_DESCRIPTION = "Soundbar";
	public static final String TYPE_COMPACT = "TYPE_COMPACT";
	private static final String TYPE_COMPACT_DESCRIPTION = "Compact Speaker";

	public static final String SPEAKERTYPEFRONT = "SPEAKER_TYPE_FRONT";
	private static final String LANG_DEPENDENT_SPEAKERTYPEFRONT = "Front Speaker Type";
	public static final String TYPE_COLUMN_FRONT = "TYPE_COLUMN_FRONT";
	private static final String TYPE_COLUMN_FRONT_DESCRIPTION = "Column Speakers";
	public static final String TYPE_TOWER_FRONT = "TYPE_TOWER_FRONT";
	private static final String TYPE_TOWER_FRONT_DESCRIPTION = "Tower Speakers";
	public static final String TYPE_COMPACT_FRONT = "TYPE_COMAPCT_FRONT";
	private static final String TYPE_COMPACT_FRONT_DESCRIPTION = "Compact Speakers";




	public static final String SOUNDBAR = "SOUNDBAR";
	private static final String LANG_DEPENDENT_SOUNDBAR = "Soundbar";
	private static final String SOUNDBAR_T = "SOUNDBAR_T";
	private static final String SOUNDBAR_T_DESCRIPTION = "Teufel Soundbar";
	private static final String SOUNDBAR_I = "SOUNDBAR_I";
	private static final String SOUNDBAR_I_DESCRIPTION = "Irradio Soundbar";

	public static final String SPEAKER_COMPACT = "SPEAKER_COMPACT";
	private static final String LANG_DEPENDENT_SPEAKER_COMPACT = "Compact Speaker";
	protected static final String SPEAKER_COMPACT_MAGNAT = "SPEAKER_COMPACT_MAGNAT";
	private static final String SPEAKER_COMPACT_MAGNAT_DESCRIPTION = "Magnat Center 51";
	private static final String SPEAKER_COMPACT_JAMO = "SPEAKER_COMPACT_JAMO";
	private static final String SPEAKER_COMPACT_JAMO_DESCRIPTION = "Jamo 360 C 35";

	protected static final String COLUMN_SPEAKER = "COLUMN_SPEAKER";
	private static final String LANG_DEPENDENT_COLUMN_SPEAKER = "Column Speakers";
	private static final String COLUMN_SPEAKER_850 = "COLUMN_SPEAKER_850";
	private static final String COLUMN_SPEAKER_850_DESCRIPTION = "Teufel Columa Trios";
	private static final String COLUMN_SPEAKER_1250 = "COLUMN_SPEAKER_1250";
	private static final String COLUMN_SPEAKER_1250_DESCRIPTION = "Sony SA-VS700ED";
	private static final String COLUMN_SPEAKER_2050 = "COLUMN_SPEAKER_2050";
	private static final String COLUMN_SPEAKER_2050_DESCRIPTION = "Canton CD 200 ";

	public static final String TOWER_SPEAKER = "TOWER_SPEAKER";
	private static final String LANG_DEPENDENT_TOWER_SPEAKER = "Tower Speakers";
	private static final String TOWER_SPEAKER_ULTIMATE = "TOWER_SPEAKER_ULTIMATE";
	private static final String TOWER_SPEAKER_ULTIMATE_DESCRIPTION = "Teufel Ultima 40";
	private static final String TOWER_SPEAKER_VENTO = "TOWER_SPEAKER_VENTO";
	private static final String TOWER_SPEAKER_VENTO_DESCRIPTION = "Canton Vento 9";

	protected static final String SPEAKER_COMPACT_FRONT = "SPEAKER_COMPACT_FRONT";
	private static final String LANG_DEPENDENT_SPEAKER_COMPACT_FRONT = "Compact Speakers";
	private static final String SPEAKER_COMPACT_FRONT_U = "SPEAKER_COMPACT_FRONT_U";
	private static final String SPEAKER_COMPACT_FRONT_U_DESCRIPTION = "Teufel Ultima 20";
	private static final String SPEAKER_COMPACT_FRONT_Q = "SPEAKER_COMPACT_FRONT_Q";
	private static final String SPEAKER_COMPACT_FRONT_Q_DESCRIPTION = "SVSound 300 W";

	private static final String BLURAY_PLAYER = "BLURAY_PLAYER";
	private static final String LANG_DEPENDENT_BLURAY_PLAYER = "Blu-ray Player";
	private static final String BLURAY_PLAYER_SONY = "BLURAY_PLAYER_SONY";
	private static final String BLURAY_PLAYER_SONY_DESCRIPTION = "Sony Ultra HD";
	private static final String BLURAY_PLAYER_3D = "BLURAY_PLAYER_3D";
	private static final String BLURAY_PLAYER_3D_DESCRIPTION = "Panasonic Full HD 3D";

	private static final String VIDEO_SERVER = "VIDEO_SERVER";
	private static final String LANG_DEPENDENT_VIDEO_SERVER = "Video Server";
	private static final String VIDEO_SERVER_YES = "VIDEO_SERVER_YES";
	private static final String VIDEO_SERVER_YES_DESCRIPTION = "Yes";
	private static final String VIDEO_SERVER_NO = "VIDEO_SERVER_NO";
	private static final String VIDEO_SERVER_NO_DESCRIPTION = "No";

	protected static final String GAMING_CONSOLE = "GAMING_CONSOLE";
	private static final String LANG_DEPENDENT_GAMING_CONSOLE = "Gaming Console";
	protected static final String GAMING_CONSOLE_YES = "GAMING_CONSOLE_YES";
	private static final String GAMING_CONSOLE_YES_DESCRIPTION = "Yes";
	private static final String GAMING_CONSOLE_NO = "GAMING_CONSOLE_NO";
	private static final String GAMING_CONSOLE_NO_DESCRIPTION = "No";
	private static final long SURCHARGE_SOUNDBAR_80 = 80;
	private static final long SURCHARGE_SPEAKER_COMPACT_DEF = 100;
	protected static final long SURCHARGE_COLUMN_SPEAKER_1250 = 70;
	protected static final long SURCHARGE_COLUMN_SPEAKER_2050 = 300;
	protected static final long SURCHARGE_SPEAKER_COMPACT_FRONT_Q = 280;
	protected static final long SURCHARGE_TOWER_SPEAKER_VENTO = 500;
	protected static final long SURCHARGE_REAR_SPEAKER_CANTON = 50;
	protected static final long SURCHARGE_REAR_SPEAKER_SONOS = 20;
	protected static final long SURCHARGE_SUBWOOFER_SONOS = 110;





	@Override
	public ConfigModel createDefaultConfiguration()
	{
		// Model
		final StringBuilder configurationText = new StringBuilder("Configuration for ");

		final ConfigModel model = createDefaultConfigModel(
				configurationText.append(ROOT_INSTANCE_NAME).append(",").append(getConfigId()).toString(), false);
		setBasePrices(model);

		model.setKbId(KB_ID);

		// root instance
		final InstanceModel rootInstance = createDefaultRootInstance(model, ROOT_INSTANCE_NAME, ROOT_INSTANCE_LANG_DEP_NAME);

		// cstic groups:
		final List<CsticGroupModel> csticGroups = new ArrayList<>();
		addCsticGroup(csticGroups, InstanceModel.GENERAL_GROUP_NAME, null, ROOM_SIZE, COLOUR_HT);
		rootInstance.setCsticGroups(csticGroups);

		// cstics and Values:
		final List<CsticModel> cstics = new ArrayList<>();

		cstics.add(createCsticRoomSize());
		cstics.add(createCsticColourHT());
		rootInstance.setCstics(cstics);

		// 'Video System' subInstance
		final InstanceModel subInstanceVideoSystem = createSubInstance(VIDEO_SYSTEM_SUBINSTANCE_ID, VIDEO_SYSTEM_SUBINSTANCE_NAME,
				VIDEO_SYSTEM_SUBINSTANCE_LANG_DEP_NAME);

		// cstics and groups for 'Video System' subInstance:
		final List<CsticGroupModel> videoSystemSubInstanceCsticGroups = new ArrayList<>();
		addCsticGroup(videoSystemSubInstanceCsticGroups, "PROJECTOR", "Projector", PROJECTOR_TYPE, LIGHT_SOURCE);
		addCsticGroup(videoSystemSubInstanceCsticGroups, "PROJECTION_SCREEN", "Projection Screen", PROJECTION_SCREEN,
				MOTORIZED_PULLDOWN);
		addCsticGroup(videoSystemSubInstanceCsticGroups, FLAT_PANEL_TV, LANG_DEPENDENT_FLAT_PANEL_TV, FLAT_PANEL_TV);


		subInstanceVideoSystem.setCsticGroups(videoSystemSubInstanceCsticGroups);

		// cstics and values for 'Video System' subInstance:
		final List<CsticModel> subInstanceVideoSystemCstics = new ArrayList<>();
		subInstanceVideoSystemCstics.add(createCsticProjectorType(VIDEO_SYSTEM_SUBINSTANCE_ID));
		subInstanceVideoSystemCstics.add(createCsticProjectionScreen(VIDEO_SYSTEM_SUBINSTANCE_ID));
		subInstanceVideoSystemCstics.add(createCsticPanelTV(VIDEO_SYSTEM_SUBINSTANCE_ID));
		subInstanceVideoSystemCstics.add(createCsticLightSource(VIDEO_SYSTEM_SUBINSTANCE_ID));
		subInstanceVideoSystemCstics.add(createCsticMotorizedFlag(VIDEO_SYSTEM_SUBINSTANCE_ID));
		subInstanceVideoSystem.setCstics(subInstanceVideoSystemCstics);

		// 'Audio System' subInstance
		final InstanceModel subInstanceAudioSystem = createSubInstance(AUDIO_SYSTEM_SUBINSTANCE_ID, AUDIO_SYSTEM_SUBINSTANCE_NAME,
				AUDIO_SYSTEM_SUBINSTANCE_LANG_DEP_NAME);

		// cstics and groups for 'Audio System' subInstance:
		final List<CsticGroupModel> audioSystemSubInstanceCsticGroups = new ArrayList<>();

		addCsticGroup(audioSystemSubInstanceCsticGroups, "Front_Speakers", "Front Speakers", SPEAKERTYPEFRONT, COLUMN_SPEAKER,
				TOWER_SPEAKER, SPEAKER_COMPACT_FRONT);
		addCsticGroup(audioSystemSubInstanceCsticGroups, "Center_Speaker", "Center Speaker", SPEAKERTYPE, SOUNDBAR,
				SPEAKER_COMPACT);
		addCsticGroup(audioSystemSubInstanceCsticGroups, "Rear_Speakers", LANG_DEPENDENT_REAR_SPEAKER, REAR_SPEAKER);
		addCsticGroup(audioSystemSubInstanceCsticGroups, "Subwoofer", "Subwoofer", SUBWOOFER);

		subInstanceAudioSystem.setCsticGroups(audioSystemSubInstanceCsticGroups);

		// cstics and values for 'Audio System' subInstance:
		final List<CsticModel> subInstanceAudioSystemCstics = new ArrayList<>();
		subInstanceAudioSystemCstics.add(createCsticRearSpeaker(AUDIO_SYSTEM_SUBINSTANCE_ID));
		subInstanceAudioSystemCstics.add(createCsticSubwoofer(AUDIO_SYSTEM_SUBINSTANCE_ID));

		subInstanceAudioSystemCstics.add(createCsticSpeakerType(AUDIO_SYSTEM_SUBINSTANCE_ID));
		subInstanceAudioSystemCstics.add(createCsticSoundbar(AUDIO_SYSTEM_SUBINSTANCE_ID));
		subInstanceAudioSystemCstics.add(createCsticSpeakerCompact(AUDIO_SYSTEM_SUBINSTANCE_ID));

		subInstanceAudioSystemCstics.add(createCsticSpeakerTypeFront(AUDIO_SYSTEM_SUBINSTANCE_ID));
		subInstanceAudioSystemCstics.add(createCsticColumnSpeaker(AUDIO_SYSTEM_SUBINSTANCE_ID));
		subInstanceAudioSystemCstics.add(createCsticTowerSpeaker(AUDIO_SYSTEM_SUBINSTANCE_ID));
		subInstanceAudioSystemCstics.add(createCsticCompactSpeakerFront(AUDIO_SYSTEM_SUBINSTANCE_ID));
		subInstanceAudioSystem.setCstics(subInstanceAudioSystemCstics);


		// 'Source Components' subInstance
		final InstanceModel subInstanceSourceComponentes = createSubInstance(SOURCE_COMPONENTS_SUBINSTANCE_ID,
				SOURCE_COMPONENTS_SUBINSTANCE_NAME, SOURCE_COMPONENTS_SUBINSTANCE_LANG_DEP_NAME);

		// cstics and groups for 'Source Components' subInstance:
		final List<CsticGroupModel> sourceCompoenentsSubInstanceCsticGroups = new ArrayList<>();
		addCsticGroup(sourceCompoenentsSubInstanceCsticGroups, InstanceModel.GENERAL_GROUP_NAME, "", BLURAY_PLAYER, VIDEO_SERVER,
				GAMING_CONSOLE);
		subInstanceSourceComponentes.setCsticGroups(sourceCompoenentsSubInstanceCsticGroups);

		// cstics and values for 'Source Components' subInstance:
		final List<CsticModel> subInstanceSourceComponentsSystemCstics = new ArrayList<>();
		subInstanceSourceComponentsSystemCstics.add(createCsticBlurayPlayer(SOURCE_COMPONENTS_SUBINSTANCE_ID));
		subInstanceSourceComponentsSystemCstics.add(createCsticVideoServer(SOURCE_COMPONENTS_SUBINSTANCE_ID));
		subInstanceSourceComponentsSystemCstics.add(createCsticGamingConsole(SOURCE_COMPONENTS_SUBINSTANCE_ID));
		subInstanceSourceComponentes.setCstics(subInstanceSourceComponentsSystemCstics);


		// add instances
		final List<InstanceModel> subInstances = new ArrayList<>();
		subInstances.add(subInstanceVideoSystem);
		subInstances.add(subInstanceAudioSystem);
		subInstances.add(subInstanceSourceComponentes);
		rootInstance.setSubInstances(subInstances);

		return model;
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



	protected void setBasePrices(final ConfigModel model)
	{
		final PriceModel basePrice = createPrice(BASE_PRICE);
		model.setBasePrice(basePrice);

		final PriceModel selectedOptionsPrice = createPrice(0);
		model.setSelectedOptionsPrice(selectedOptionsPrice);

		model.setCurrentTotalPrice(basePrice);
	}


	@Override
	public void checkModel(final ConfigModel model)
	{
		setBasePrices(model);
		super.checkModel(model);
	}

	@Override
	public void checkCstic(final ConfigModel model, final InstanceModel instance, final CsticModel cstic)
	{
		super.checkCstic(model, instance, cstic);

		checkVideoSystem(instance, cstic, model);
		checkAudioSystem(model, instance, cstic);

		checkConflicts(model, instance, cstic);

		checkValuePrices(model, cstic);
	}

	protected void checkValuePrices(final ConfigModel model, final CsticModel cstic)
	{
		if (cstic.getName().equalsIgnoreCase(SOUNDBAR))
		{
			resetValuePrices(cstic);
			handleValuePrice(model, cstic, SOUNDBAR_I, BigDecimal.valueOf(SURCHARGE_SOUNDBAR_80));
		}

		if (cstic.getName().equalsIgnoreCase(SPEAKER_COMPACT))
		{
			resetValuePrices(cstic);
			handleValuePrice(model, cstic, SPEAKER_COMPACT_JAMO, BigDecimal.valueOf(SURCHARGE_SPEAKER_COMPACT_DEF));
		}

		if (cstic.getName().equalsIgnoreCase(COLUMN_SPEAKER))
		{
			resetValuePrices(cstic);
			handleValuePrice(model, cstic, COLUMN_SPEAKER_1250, BigDecimal.valueOf(SURCHARGE_COLUMN_SPEAKER_1250));
			handleValuePrice(model, cstic, COLUMN_SPEAKER_2050, BigDecimal.valueOf(SURCHARGE_COLUMN_SPEAKER_2050));
		}

		if (cstic.getName().equalsIgnoreCase(SPEAKER_COMPACT_FRONT))
		{
			resetValuePrices(cstic);
			handleValuePrice(model, cstic, SPEAKER_COMPACT_FRONT_Q, BigDecimal.valueOf(SURCHARGE_SPEAKER_COMPACT_FRONT_Q));
		}

		if (cstic.getName().equalsIgnoreCase(TOWER_SPEAKER))
		{
			resetValuePrices(cstic);
			handleValuePrice(model, cstic, TOWER_SPEAKER_VENTO, BigDecimal.valueOf(SURCHARGE_TOWER_SPEAKER_VENTO));
		}

		if (cstic.getName().equalsIgnoreCase(REAR_SPEAKER))
		{
			resetValuePrices(cstic);
			handleValuePrice(model, cstic, REAR_SPEAKER_CANTON, BigDecimal.valueOf(SURCHARGE_REAR_SPEAKER_CANTON));
			handleValuePrice(model, cstic, REAR_SPEAKER_SONOS, BigDecimal.valueOf(SURCHARGE_REAR_SPEAKER_SONOS));
		}

		if (cstic.getName().equalsIgnoreCase(SUBWOOFER))
		{
			resetValuePrices(cstic);
			handleValuePrice(model, cstic, SUBWOOFER_SONOS, BigDecimal.valueOf(SURCHARGE_SUBWOOFER_SONOS));
		}
	}

	protected void checkConflicts(final ConfigModel model, final InstanceModel instance, final CsticModel cstic)
	{

		final List<SolvableConflictModel> conflicts = new ArrayList<>();

		if (cstic.getName().equalsIgnoreCase(LIGHT_SOURCE))
		{
			checkPreconditionViolated(instance, cstic, conflicts, PROJECTOR_TYPE, ImmutableSet.of(PROJECTOR_DLP, PROJECTOR_LCD),
					CONFLICT_TEXT_PROJECTOR);
		}

		if (cstic.getName().equalsIgnoreCase(PROJECTION_SCREEN))
		{
			checkPreconditionViolated(instance, cstic, conflicts, PROJECTOR_TYPE, ImmutableSet.of(PROJECTOR_DLP, PROJECTOR_LCD),
					CONFLICT_TEXT_PROJECTION_SCREEN);
		}

		if (cstic.getName().equalsIgnoreCase(FLAT_PANEL_TV))
		{
			checkPreconditionViolated(instance, cstic, conflicts, PROJECTOR_TYPE, ImmutableSet.of(PROJECTOR_NONE),
					CONFLICT_TEXT_PANEL_TV);
		}

		if (cstic.getName().equalsIgnoreCase(GAMING_CONSOLE))
		{
			checkGamingConflict(instance, cstic, conflicts, model);
		}

		final List<SolvableConflictModel> existingConflicts = model.getSolvableConflicts();
		if (CollectionUtils.isNotEmpty(existingConflicts) && CollectionUtils.isNotEmpty(conflicts))
		{
			existingConflicts.addAll(conflicts);
			model.setSolvableConflicts(existingConflicts);
		}
		else if (CollectionUtils.isNotEmpty(conflicts))
		{
			model.setSolvableConflicts(conflicts);
		}
		model.getRootInstance().setConsistent(CollectionUtils.isEmpty(model.getSolvableConflicts()));
	}



	protected void checkGamingConflict(final InstanceModel instance, final CsticModel cstic,
			final List<SolvableConflictModel> conflicts, final ConfigModel model)
	{
		final InstanceModel subInstanceVideo = model.getRootInstance().getSubInstance(VIDEO_SYSTEM_SUBINSTANCE_ID);
		final CsticModel projectorType = subInstanceVideo.getCstic(PROJECTOR_TYPE);
		final boolean isGamingSelected = GAMING_CONSOLE_YES.equals(cstic.getSingleValue());
		final boolean isLcd = PROJECTOR_LCD.equals(projectorType.getSingleValue());

		if (isGamingSelected && isLcd)
		{
			conflicts.add(createSolvableConflict(cstic.getAssignedValues().get(0), cstic, instance, CONFLICT_TEXT_GAMING,
					projectorType.getAssignedValues().get(0), projectorType, subInstanceVideo));
      cstic.setConsistent(false);
    	projectorType.setConsistent(false);
		}

	}

	protected void checkAudioSystem(final ConfigModel model, final InstanceModel instance, final CsticModel cstic)
	{
		if (cstic.getName().equalsIgnoreCase(SOUNDBAR))
		{
			final InstanceModel rootInstance = model.getRootInstance();
			checkRoomSizeAndType(rootInstance, instance, cstic, ROOM_SIZE_SMALL, TYPE_SOUNDBAR);
		}

		if (cstic.getName().equalsIgnoreCase(SPEAKER_COMPACT))
		{

			final InstanceModel rootInstance = model.getRootInstance();
			checkSpeakerType(rootInstance, instance, cstic, TYPE_COMPACT);

		}

		if (cstic.getName().equalsIgnoreCase(SPEAKERTYPE))
		{
			final InstanceModel rootInstance = model.getRootInstance();
			checkRoomSize(rootInstance, cstic, ROOM_SIZE_SMALL);
		}

		if (cstic.getName().equalsIgnoreCase(TOWER_SPEAKER))
		{
			final InstanceModel rootInstance = model.getRootInstance();
			checkRoomSizeAndFrontType(rootInstance, instance, cstic, ROOM_SIZE_SMALL, TYPE_TOWER_FRONT);
		}



		final Map<String, String> frontSpeakerRelatedCstics = Map.of(COLUMN_SPEAKER, TYPE_COLUMN_FRONT, TOWER_SPEAKER,
				TYPE_TOWER_FRONT, SPEAKER_COMPACT_FRONT, TYPE_COMPACT_FRONT);

		final CsticModel frontSpeakerType = instance.getCstic(SPEAKERTYPEFRONT);
		if (frontSpeakerRelatedCstics.containsKey(cstic.getName()) && frontSpeakerType != null)
		{
			checkVisibility(cstic, frontSpeakerRelatedCstics.get(cstic.getName()), frontSpeakerType.getSingleValue());
		}

	}

	protected void checkVideoSystem(final InstanceModel instance, final CsticModel cstic, final ConfigModel model)
	{

		if (cstic.getName().equalsIgnoreCase(MOTORIZED_PULLDOWN))
		{
			checkProjectionScreen(instance, cstic);
		}

		if (cstic.getName().equalsIgnoreCase(PROJECTION_SCREEN) || cstic.getName().equalsIgnoreCase(LIGHT_SOURCE))
		{
			checkProjector(instance, cstic);
		}


		if (cstic.getName().equalsIgnoreCase(FLAT_PANEL_TV))
		{
			final InstanceModel rootInstance = model.getRootInstance();
			checkNoProjectorAndRoomSize(rootInstance, instance, cstic);
		}


	}


	protected void checkVisibility(final CsticModel cstic, final String expectedType, final String actualType)
	{

		if (!expectedType.equals(actualType))
		{
			cstic.setAssignedValues(Collections.emptyList());
			cstic.setVisible(false);
		}


	}


	protected void checkSpeakerType(final InstanceModel rootInstance, final InstanceModel instance, final CsticModel cstic,
			final String type)
	{

		final CsticValueModel value = retrieveValue(rootInstance, ROOM_SIZE);
		final CsticValueModel valueType = retrieveValue(instance, SPEAKERTYPE);

		if (value != null && value.getName().equalsIgnoreCase(ROOM_SIZE_SMALL))
		{
			cstic.setVisible(true);
		}
		else
		{

			if (valueType != null && valueType.getName().equalsIgnoreCase(type))
			{
				cstic.setVisible(true);
			}
			else
			{
				hideAndClearSelectedValues(cstic);
			}
		}

	}

	protected void checkRoomSizeAndType(final InstanceModel rootInstance, final InstanceModel instance, final CsticModel cstic,
			final String size, final String type)
	{

		final CsticValueModel value = retrieveValue(rootInstance, ROOM_SIZE);
		final CsticValueModel valueType = retrieveValue(instance, SPEAKERTYPE);

		if (value != null && value.getName().equalsIgnoreCase(size))
		{
			hideAndClearSelectedValues(cstic);
		}
		else
		{

			if (valueType != null && valueType.getName().equalsIgnoreCase(type))
			{
				cstic.setVisible(true);
			}
			else
			{
				hideAndClearSelectedValues(cstic);
			}

		}
	}

	protected void checkRoomSizeAndFrontType(final InstanceModel rootInstance, final InstanceModel instance,
			final CsticModel cstic, final String size, final String type)
	{

		final CsticValueModel value = retrieveValue(rootInstance, ROOM_SIZE);
		final CsticValueModel valueType = retrieveValue(instance, SPEAKERTYPEFRONT);

		if (value != null && value.getName().equalsIgnoreCase(size))
		{
			hideAndClearSelectedValues(cstic);
			removeAssignableValue(instance, SPEAKERTYPEFRONT, TYPE_TOWER_FRONT);
		}
		else
		{
			addAssignableValue(instance, SPEAKERTYPEFRONT, TYPE_TOWER_FRONT, TYPE_TOWER_FRONT_DESCRIPTION, 1);
			if (valueType != null && valueType.getName().equalsIgnoreCase(type))
			{
				cstic.setVisible(true);
			}
			else
			{
				hideAndClearSelectedValues(cstic);
			}

		}
	}

	protected void hideAndClearSelectedValues(final CsticModel cstic)
	{
		cstic.setVisible(false);
		cstic.setAssignedValues(Collections.emptyList());
	}

	protected void checkRoomSize(final InstanceModel instance, final CsticModel cstic, final String size)
	{


		final CsticValueModel value = retrieveValue(instance, ROOM_SIZE);

		if (value != null && value.getName().equalsIgnoreCase(size))
		{
			hideAndClearSelectedValues(cstic);
		}
		else
		{
			cstic.setVisible(true);
		}
	}

	protected void checkProjector(final InstanceModel instance, final CsticModel cstic)
	{

		final CsticValueModel value = retrieveValue(instance, PROJECTOR_TYPE);

		if (value != null && value.getName().length() > 0 && !value.getName().equalsIgnoreCase(PROJECTOR_NONE))
		{

			cstic.setVisible(true);

		}
		else
		{
			cstic.setVisible(false);
		}
	}

	protected void checkNoProjectorAndRoomSize(final InstanceModel rootinstance, final InstanceModel instance,
			final CsticModel cstic)
	{
		final CsticValueModel value = retrieveValue(instance, PROJECTOR_TYPE);
		final CsticValueModel size = retrieveValue(rootinstance, ROOM_SIZE);

		if (value != null && value.getName().length() > 0 && value.getName().equalsIgnoreCase(PROJECTOR_NONE))
		{
			if (size != null && size.getName().length() > 0 && size.getName().equalsIgnoreCase(ROOM_SIZE_LARGE))
			{
				cstic.setVisible(false);
			}
			else
			{
				cstic.setVisible(true);
			}

		}
		else
		{
			hideAndClearSelectedValues(cstic);
		}
	}

	protected void checkProjectionScreen(final InstanceModel instance, final CsticModel cstic)
	{
		final CsticValueModel value = retrieveVisibleValue(instance, PROJECTION_SCREEN);

		if (value != null && value.getName().equalsIgnoreCase(PROJSCREEN_PULLDOWN))
		{
			cstic.setVisible(true);
		}
		else
		{
			cstic.setVisible(false);
		}
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

	public CsticValueModel retrieveVisibleValue(final InstanceModel instance, final String csticName)
	{
		CsticValueModel value = null;

		final List<CsticModel> cstics = instance.getCstics();
		for (final CsticModel cstic : cstics)
		{
			if (cstic.getName().equalsIgnoreCase(csticName) && cstic.isVisible())
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

	protected CsticModel createCsticMotorizedFlag(final String instanceId)
	{
		final CsticModelBuilder builder = new CsticModelBuilder().withInstance(instanceId, null);
		builder.withName(MOTORIZED_PULLDOWN, LANG_DEPENDENT_MOTORIZED_PULLDOWN);
		builder.simpleFlag("Select").selected();
		builder.withDefaultUIState();
		builder.hidden();
		return builder.build();
	}

	protected CsticModel createCsticGamingConsole(final String instanceId)
	{
		final CsticModelBuilder builder = new CsticModelBuilder().withInstance(instanceId, null);
		builder.withName(GAMING_CONSOLE, LANG_DEPENDENT_GAMING_CONSOLE);
		builder.stringType().singleSelection();
		builder.addOption(GAMING_CONSOLE_YES, GAMING_CONSOLE_YES_DESCRIPTION).addOption(GAMING_CONSOLE_NO,
				GAMING_CONSOLE_NO_DESCRIPTION);
		builder.withDefaultUIState();
		return builder.build();
	}

	protected CsticModel createCsticVideoServer(final String instanceId)
	{
		final CsticModelBuilder builder = new CsticModelBuilder().withInstance(instanceId, null);
		builder.withName(VIDEO_SERVER, LANG_DEPENDENT_VIDEO_SERVER);
		builder.stringType().singleSelection();
		builder.addOption(VIDEO_SERVER_YES, VIDEO_SERVER_YES_DESCRIPTION).addOption(VIDEO_SERVER_NO, VIDEO_SERVER_NO_DESCRIPTION);
		builder.withDefaultUIState();
		return builder.build();
	}

	protected CsticModel createCsticBlurayPlayer(final String instanceId)
	{
		final CsticModelBuilder builder = new CsticModelBuilder().withInstance(instanceId, null);
		builder.withName(BLURAY_PLAYER, LANG_DEPENDENT_BLURAY_PLAYER);
		builder.stringType().singleSelection();
		builder.addOption(BLURAY_PLAYER_SONY, BLURAY_PLAYER_SONY_DESCRIPTION).addOption(BLURAY_PLAYER_3D,
				BLURAY_PLAYER_3D_DESCRIPTION);
		builder.withDefaultUIState();
		return builder.build();
	}

	protected CsticModel createCsticCompactSpeakerFront(final String instanceId)
	{
		final CsticModelBuilder builder = new CsticModelBuilder().withInstance(instanceId, null);
		builder.withName(SPEAKER_COMPACT_FRONT, LANG_DEPENDENT_SPEAKER_COMPACT_FRONT);
		builder.stringType().singleSelection();
		builder.addOption(SPEAKER_COMPACT_FRONT_U, SPEAKER_COMPACT_FRONT_U_DESCRIPTION).addOption(SPEAKER_COMPACT_FRONT_Q,
				SPEAKER_COMPACT_FRONT_Q_DESCRIPTION);

		builder.withDefaultUIState();

		return builder.build();
	}

	protected CsticModel createCsticTowerSpeaker(final String instanceId)
	{
		final CsticModelBuilder builder = new CsticModelBuilder().withInstance(instanceId, null);
		builder.withName(TOWER_SPEAKER, LANG_DEPENDENT_TOWER_SPEAKER);
		builder.stringType().singleSelection();
		builder.addOption(TOWER_SPEAKER_ULTIMATE, TOWER_SPEAKER_ULTIMATE_DESCRIPTION).addOption(TOWER_SPEAKER_VENTO,
				TOWER_SPEAKER_VENTO_DESCRIPTION);

		builder.withDefaultUIState();

		return builder.build();
	}

	protected CsticModel createCsticColumnSpeaker(final String instanceId)
	{
		final CsticModelBuilder builder = new CsticModelBuilder().withInstance(instanceId, null);
		builder.withName(COLUMN_SPEAKER, LANG_DEPENDENT_COLUMN_SPEAKER);
		builder.stringType().singleSelection();
		builder.addOption(COLUMN_SPEAKER_850, COLUMN_SPEAKER_850_DESCRIPTION)
				.addOption(COLUMN_SPEAKER_1250, COLUMN_SPEAKER_1250_DESCRIPTION)
				.addOption(COLUMN_SPEAKER_2050, COLUMN_SPEAKER_2050_DESCRIPTION);

		builder.withDefaultUIState();

		return builder.build();
	}

	protected CsticModel createCsticSpeakerCompact(final String instanceId)
	{
		final CsticModelBuilder builder = new CsticModelBuilder().withInstance(instanceId, null);
		builder.withName(SPEAKER_COMPACT, LANG_DEPENDENT_SPEAKER_COMPACT);
		builder.stringType().singleSelection();
		builder.addOption(SPEAKER_COMPACT_MAGNAT, SPEAKER_COMPACT_MAGNAT_DESCRIPTION).addOption(SPEAKER_COMPACT_JAMO,
				SPEAKER_COMPACT_JAMO_DESCRIPTION);

		builder.withDefaultUIState();

		return builder.build();
	}

	protected CsticModel createCsticSpeakerType(final String instanceId)
	{
		final CsticModelBuilder builder = new CsticModelBuilder().withInstance(instanceId, null);
		builder.withName(SPEAKERTYPE, LANG_DEPENDENT_SPEAKERTYPE);
		builder.stringType().singleSelection();
		builder.addOption(TYPE_SOUNDBAR, TYPE_SOUNDBAR_DESCRIPTION).addOption(TYPE_COMPACT, TYPE_COMPACT_DESCRIPTION);

		builder.withDefaultUIState();

		return builder.build();
	}

	protected CsticModel createCsticSpeakerTypeFront(final String instanceId)
	{
		final CsticModelBuilder builder = new CsticModelBuilder().withInstance(instanceId, null);
		builder.withName(SPEAKERTYPEFRONT, LANG_DEPENDENT_SPEAKERTYPEFRONT);
		builder.stringType().singleSelection();
		builder.addSelectedOption(TYPE_COLUMN_FRONT, TYPE_COLUMN_FRONT_DESCRIPTION)
				.addOption(TYPE_TOWER_FRONT, TYPE_TOWER_FRONT_DESCRIPTION)
				.addOption(TYPE_COMPACT_FRONT, TYPE_COMPACT_FRONT_DESCRIPTION);

		builder.withDefaultUIState();

		return builder.build();
	}

	protected CsticModel createCsticSoundbar(final String instanceId)
	{
		final CsticModelBuilder builder = new CsticModelBuilder().withInstance(instanceId, null);
		builder.withName(SOUNDBAR, LANG_DEPENDENT_SOUNDBAR);
		builder.stringType().singleSelection();
		builder.addOption(SOUNDBAR_T, SOUNDBAR_T_DESCRIPTION).addOption(SOUNDBAR_I, SOUNDBAR_I_DESCRIPTION);

		builder.withDefaultUIState();

		return builder.build();
	}


	protected CsticModel createCsticSubwoofer(final String instanceId)
	{
		final CsticModelBuilder builder = new CsticModelBuilder().withInstance(instanceId, null);
		builder.withName(SUBWOOFER, LANG_DEPENDENT_SUBWOOFER);
		builder.stringType().singleSelection();
		builder.addOption(SUBWOOFER_LOEWE, SUBWOOFER_LOEWE_DESCRIPTION).addOption(SUBWOOFER_SONOS, SUBWOOFER_SONOS_DESCRIPTION);

		builder.withDefaultUIState();

		return builder.build();
	}


	protected CsticModel createCsticRearSpeaker(final String instanceId)
	{
		final CsticModelBuilder builder = new CsticModelBuilder().withInstance(instanceId, null);
		builder.withName(REAR_SPEAKER, LANG_DEPENDENT_REAR_SPEAKER);
		builder.stringType().singleSelection();
		builder.addOption(REAR_SPEAKER_CRP, REAR_SPEAKER_CRP_DESCRIPTION)
				.addOption(REAR_SPEAKER_CANTON, REAR_SPEAKER_CANTON_DESCRIPTION)
				.addOption(REAR_SPEAKER_SONOS, REAR_SPEAKER_SONOS_DESCRIPTION);

		builder.withDefaultUIState();

		return builder.build();
	}



	protected CsticModel createCsticColourHT()
	{
		final CsticModelBuilder builder = new CsticModelBuilder().withInstance(ROOT_INSTANCE_ID, ROOT_INSTANCE_NAME);
		builder.withName(COLOUR_HT, LANG_DEPENDENT_COLOUR_HT);
		builder.stringType().singleSelection();
		builder.addOption(COLOUR_HT_BLACK, COLOUR_HT_BLACK_DESCRIPTION).addOption(COLOUR_HT_WHITE, COLOUR_HT_WHITE_DESCRIPTION)
				.addOption(COLOUR_HT_TITAN, COLOUR_HT_TITAN_DESCRIPTION);
		return builder.build();
	}


	protected CsticModel createCsticRoomSize()
	{
		final CsticModelBuilder builder = new CsticModelBuilder().withInstance(ROOT_INSTANCE_ID, ROOT_INSTANCE_NAME);
		builder.withName(ROOM_SIZE, LANG_DEPENDENT_ROOM_SIZE);
		builder.stringType().singleSelection();
		builder.addOption(ROOM_SIZE_SMALL, ROOM_SIZE_SMALL_DESCRIPTION).addOption(ROOM_SIZE_MEDIUM, ROOM_SIZE_MEDIUM_DESCRIPTION)
				.addOption(ROOM_SIZE_LARGE, ROOM_SIZE_LARGE_DESCRIPTION);
		return builder.build();
	}



	protected CsticModel createCsticLightSource(final String instanceId)
	{
		final CsticModelBuilder builder = new CsticModelBuilder().withInstance(instanceId, null);
		builder.withName(LIGHT_SOURCE, LANG_DEPENDENT_LIGHT_SOURCE);
		builder.stringType().singleSelection();
		builder.addOption(LIGHT_SOURCE_LAMP, LIGHT_SOURCE_LAMP_DESCRIPTION)
				.addOption(LIGHT_SOURCE_LASER, LIGHT_SOURCE_LASER_DESCRIPTION)
				.addOption(LIGHT_SOURCE_LED, LIGHT_SOURCE_LED_DESCRIPTION);
		builder.withDefaultUIState();
		return builder.build();
	}


	protected CsticModel createCsticProjectionScreen(final String instanceId)
	{
		final CsticModelBuilder builder = new CsticModelBuilder().withInstance(instanceId, null);
		builder.withName(PROJECTION_SCREEN, LANG_DEPENDENT_PROJECTION_SCREEN);
		builder.stringType().singleSelection();
		builder.addOption(PROJSCREEN_FIXED, PROJSCREEN_FIXED_DESCRIPTION).addOption(PROJSCREEN_PULLDOWN,
				PROJSCREEN_PULLDOWN_DESCRIPTION);
		builder.withDefaultUIState();
		return builder.build();
	}

	protected CsticModel createCsticPanelTV(final String instanceId)
	{
		final CsticModelBuilder builder = new CsticModelBuilder().withInstance(instanceId, null);
		builder.withName(FLAT_PANEL_TV, LANG_DEPENDENT_FLAT_PANEL_TV);
		builder.stringType().singleSelection();
		builder.addOption(FLAT_PANEL_TV_HDTV, FLAT_PANEL_TV_HDTV_DESCRIPTION)
				.addOption(FLAT_PANEL_TV_LCD, FLAT_PANEL_TV_LCD_DESCRIPTION)
				.addOption(FLAT_PANEL_TV_OLED, FLAT_PANEL_TV_OLED_DESCRIPTION);

		builder.withDefaultUIState();

		return builder.build();
	}

	protected CsticModel createCsticProjectorType(final String instanceId)
	{
		final CsticModelBuilder builder = new CsticModelBuilder().withInstance(instanceId, null);
		builder.withName(PROJECTOR_TYPE, LANG_DEPENDENT_PROJECTOR_TYPE);
		builder.stringType().singleSelection();
		builder.addSelectedOption(PROJECTOR_NONE, PROJECTOR_NONE_DESCRIPTION).addOption(PROJECTOR_DLP, PROJECTOR_DLP_DESCRIPTION)
				.addOption(PROJECTOR_LCD, PROJECTOR_LCD_DESCRIPTION);

		builder.withDefaultUIState();

		return builder.build();
	}
}
