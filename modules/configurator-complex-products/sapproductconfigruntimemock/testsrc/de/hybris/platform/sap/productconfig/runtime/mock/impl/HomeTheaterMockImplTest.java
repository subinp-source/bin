/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package de.hybris.platform.sap.productconfig.runtime.mock.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.sap.productconfig.runtime.interf.model.ConfigModel;
import de.hybris.platform.sap.productconfig.runtime.interf.model.CsticModel;
import de.hybris.platform.sap.productconfig.runtime.interf.model.CsticValueModel;
import de.hybris.platform.sap.productconfig.runtime.interf.model.InstanceModel;
import de.hybris.platform.sap.productconfig.runtime.interf.model.PriceModel;
import de.hybris.platform.sap.productconfig.runtime.interf.model.SolvableConflictModel;
import de.hybris.platform.sap.productconfig.runtime.interf.model.impl.CsticValueModelImpl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.junit.Before;
import org.junit.Test;


@SuppressWarnings("javadoc")
@UnitTest
public class HomeTheaterMockImplTest
{
	private HomeTheaterMockImpl classUnderTest;
	private ConfigModel model;
	private InstanceModel instance;
	private InstanceModel videoSystemSubInstance;
	private InstanceModel audioSystemSubInstance;
	private InstanceModel sourceComponentSubInstance;

	@Before
	public void setUp()
	{
		classUnderTest = (HomeTheaterMockImpl) new RunTimeConfigMockFactory()
				.createConfigMockForProductCode("CONF_HOME_THEATER_ML");
		model = classUnderTest.createDefaultConfiguration();
		classUnderTest.showDeltaPrices(false);
		instance = model.getRootInstance();
		videoSystemSubInstance = instance.getSubInstance(HomeTheaterMockImpl.VIDEO_SYSTEM_SUBINSTANCE_ID);
		audioSystemSubInstance = instance.getSubInstance(HomeTheaterMockImpl.AUDIO_SYSTEM_SUBINSTANCE_ID);
		sourceComponentSubInstance = instance.getSubInstance(HomeTheaterMockImpl.SOURCE_COMPONENTS_SUBINSTANCE_ID);
	}

	@Test
	public void testMotorizedVisible()
	{

		final List<CsticValueModel> assignedValues = getAssignedValueList(HomeTheaterMockImpl.PROJSCREEN_PULLDOWN);

		final CsticModel projectionScreen = videoSystemSubInstance.getCstic(HomeTheaterMockImpl.PROJECTION_SCREEN);
		projectionScreen.setAssignedValues(assignedValues);
		final CsticModel motorized = videoSystemSubInstance.getCstic(HomeTheaterMockImpl.MOTORIZED_PULLDOWN);

		classUnderTest.checkCstic(model, videoSystemSubInstance, motorized);

		final List<CsticValueModel> assignedValuesAfterChange = motorized.getAssignedValues();
		assertEquals(true, motorized.isVisible());

	}

	@Test
	public void testMotorizedInvisible()
	{

		final List<CsticValueModel> assignedValues = getAssignedValueList(HomeTheaterMockImpl.PROJSCREEN_FIXED);

		final CsticModel projectionScreen = videoSystemSubInstance.getCstic(HomeTheaterMockImpl.PROJECTION_SCREEN);
		projectionScreen.setAssignedValues(assignedValues);
		final CsticModel motorized = videoSystemSubInstance.getCstic(HomeTheaterMockImpl.MOTORIZED_PULLDOWN);

		classUnderTest.checkCstic(model, videoSystemSubInstance, motorized);

		final List<CsticValueModel> assignedValuesAfterChange = motorized.getAssignedValues();
		assertEquals(false, motorized.isVisible());

	}

	@Test
	public void testProjectionScreenVisible()
	{

		final List<CsticValueModel> assignedValues = getAssignedValueList(HomeTheaterMockImpl.PROJECTOR_DLP);

		final CsticModel projectorType = videoSystemSubInstance.getCstic(HomeTheaterMockImpl.PROJECTOR_TYPE);
		projectorType.setAssignedValues(assignedValues);
		final CsticModel projectionScreen = videoSystemSubInstance.getCstic(HomeTheaterMockImpl.PROJECTION_SCREEN);

		classUnderTest.checkCstic(model, videoSystemSubInstance, projectionScreen);

		assertEquals(true, projectionScreen.isVisible());

	}

	@Test
	public void testProjectionScreenInVisible()
	{

		final List<CsticValueModel> assignedValues = getAssignedValueList(HomeTheaterMockImpl.PROJECTOR_NONE);

		final CsticModel projectorType = videoSystemSubInstance.getCstic(HomeTheaterMockImpl.PROJECTOR_TYPE);
		projectorType.setAssignedValues(assignedValues);
		final CsticModel projectionScreen = videoSystemSubInstance.getCstic(HomeTheaterMockImpl.PROJECTION_SCREEN);

		classUnderTest.checkCstic(model, videoSystemSubInstance, projectionScreen);

		assertEquals(false, projectionScreen.isVisible());

	}

	@Test
	public void testFlatTVInVisible()
	{

		final List<CsticValueModel> assignedValues = getAssignedValueList(HomeTheaterMockImpl.PROJECTOR_DLP);

		final CsticModel projectorType = videoSystemSubInstance.getCstic(HomeTheaterMockImpl.PROJECTOR_TYPE);
		projectorType.setAssignedValues(assignedValues);
		final CsticModel flatTV = videoSystemSubInstance.getCstic(HomeTheaterMockImpl.FLAT_PANEL_TV);

		classUnderTest.checkCstic(model, videoSystemSubInstance, flatTV);

		assertEquals(false, flatTV.isVisible());

	}

	@Test
	public void testFlatTVInVisibleRoomSize()
	{

		final List<CsticValueModel> assignedValues = getAssignedValueList(HomeTheaterMockImpl.PROJECTOR_NONE);
		final CsticModel projectorType = videoSystemSubInstance.getCstic(HomeTheaterMockImpl.PROJECTOR_TYPE);
		projectorType.setAssignedValues(assignedValues);

		final List<CsticValueModel> assignedValues1 = getAssignedValueList(HomeTheaterMockImpl.ROOM_SIZE_LARGE);
		final CsticModel roomSize = instance.getCstic(HomeTheaterMockImpl.ROOM_SIZE);
		roomSize.setAssignedValues(assignedValues1);


		final CsticModel flatTV = videoSystemSubInstance.getCstic(HomeTheaterMockImpl.FLAT_PANEL_TV);

		classUnderTest.checkCstic(model, videoSystemSubInstance, flatTV);

		assertEquals(false, flatTV.isVisible());

	}



	@Test
	public void testFlatTVVisible()
	{

		final List<CsticValueModel> assignedValues = getAssignedValueList(HomeTheaterMockImpl.PROJECTOR_NONE);

		final CsticModel projectorType = videoSystemSubInstance.getCstic(HomeTheaterMockImpl.PROJECTOR_TYPE);
		projectorType.setAssignedValues(assignedValues);
		final CsticModel flatTV = videoSystemSubInstance.getCstic(HomeTheaterMockImpl.FLAT_PANEL_TV);

		classUnderTest.checkCstic(model, videoSystemSubInstance, flatTV);

		assertEquals(true, flatTV.isVisible());

	}

	@Test
	public void testLightSourceInVisible()
	{

		final List<CsticValueModel> assignedValues = getAssignedValueList(HomeTheaterMockImpl.PROJECTOR_NONE);

		final CsticModel projectorType = videoSystemSubInstance.getCstic(HomeTheaterMockImpl.PROJECTOR_TYPE);
		projectorType.setAssignedValues(assignedValues);
		final CsticModel flatTV = videoSystemSubInstance.getCstic(HomeTheaterMockImpl.LIGHT_SOURCE);

		classUnderTest.checkCstic(model, videoSystemSubInstance, flatTV);

		assertEquals(false, flatTV.isVisible());

	}





	@Test
	public void testSoundbarInvisibleForSmallRoom()
	{

		final List<CsticValueModel> assignedValues = getAssignedValueList(HomeTheaterMockImpl.TYPE_SOUNDBAR);
		final CsticModel speakerType = audioSystemSubInstance.getCstic(HomeTheaterMockImpl.SPEAKERTYPE);
		speakerType.setAssignedValues(assignedValues);

		final List<CsticValueModel> assignedValues1 = getAssignedValueList(HomeTheaterMockImpl.ROOM_SIZE_SMALL);
		final CsticModel roomSize = instance.getCstic(HomeTheaterMockImpl.ROOM_SIZE);
		roomSize.setAssignedValues(assignedValues1);

		final CsticModel soundbar = audioSystemSubInstance.getCstic(HomeTheaterMockImpl.SOUNDBAR);
		classUnderTest.checkCstic(model, audioSystemSubInstance, soundbar);
		assertEquals(false, soundbar.isVisible());

	}

	@Test
	public void testSoundbarVisible()
	{

		final List<CsticValueModel> assignedValues1 = getAssignedValueList(HomeTheaterMockImpl.ROOM_SIZE_MEDIUM);
		final CsticModel roomSize = instance.getCstic(HomeTheaterMockImpl.ROOM_SIZE);
		roomSize.setAssignedValues(assignedValues1);

		final List<CsticValueModel> assignedValues = getAssignedValueList(HomeTheaterMockImpl.TYPE_SOUNDBAR);
		final CsticModel speakerType = audioSystemSubInstance.getCstic(HomeTheaterMockImpl.SPEAKERTYPE);
		speakerType.setAssignedValues(assignedValues);

		final CsticModel soundbar = audioSystemSubInstance.getCstic(HomeTheaterMockImpl.SOUNDBAR);
		classUnderTest.checkCstic(model, audioSystemSubInstance, soundbar);
		assertEquals(true, soundbar.isVisible());

	}


	@Test
	public void testSoundbarInvisibleForDifferentType()
	{

		final List<CsticValueModel> assignedValues1 = getAssignedValueList(HomeTheaterMockImpl.ROOM_SIZE_MEDIUM);
		final CsticModel roomSize = instance.getCstic(HomeTheaterMockImpl.ROOM_SIZE);
		roomSize.setAssignedValues(assignedValues1);

		final List<CsticValueModel> assignedValues = getAssignedValueList(HomeTheaterMockImpl.TYPE_COMPACT);
		final CsticModel speakerType = audioSystemSubInstance.getCstic(HomeTheaterMockImpl.SPEAKERTYPE);
		speakerType.setAssignedValues(assignedValues);

		final CsticModel soundbar = audioSystemSubInstance.getCstic(HomeTheaterMockImpl.SOUNDBAR);
		classUnderTest.checkCstic(model, audioSystemSubInstance, soundbar);
		assertEquals(false, soundbar.isVisible());

	}

	@Test
	public void testCompactSpeakerInvisibleForDifferentType()
	{

		final List<CsticValueModel> assignedValues1 = getAssignedValueList(HomeTheaterMockImpl.ROOM_SIZE_MEDIUM);
		final CsticModel roomSize = instance.getCstic(HomeTheaterMockImpl.ROOM_SIZE);
		roomSize.setAssignedValues(assignedValues1);

		final List<CsticValueModel> assignedValues = getAssignedValueList(HomeTheaterMockImpl.TYPE_SOUNDBAR);
		final CsticModel speakerType = audioSystemSubInstance.getCstic(HomeTheaterMockImpl.SPEAKERTYPE);
		speakerType.setAssignedValues(assignedValues);

		final CsticModel compactSpeaker = audioSystemSubInstance.getCstic(HomeTheaterMockImpl.SPEAKER_COMPACT);
		classUnderTest.checkCstic(model, audioSystemSubInstance, compactSpeaker);
		assertEquals(false, compactSpeaker.isVisible());
	}

	@Test
	public void testProjectorTypeRaisesConflict()
	{

		final CsticModel lightSource = getLightSourceCsticWithConflict();

		classUnderTest.checkCstic(model, videoSystemSubInstance, lightSource);

		final List<SolvableConflictModel> solvableConflicts = model.getSolvableConflicts();
		assertNotNull(solvableConflicts);
		assertEquals(1, solvableConflicts.size());
		assertEquals(HomeTheaterMockImpl.CONFLICT_TEXT_PROJECTOR, solvableConflicts.get(0).getDescription());
	}


	@Test
	public void testMultipleConflicts()
	{

		//create first conflict
		final CsticModel lightSource = getLightSourceCsticWithConflict();
		classUnderTest.checkCstic(model, videoSystemSubInstance, lightSource);

		//create second conflict
		final List<CsticValueModel> assignedValuesProjectionScreen = getAssignedValueList(HomeTheaterMockImpl.PROJSCREEN_PULLDOWN);
		final CsticModel projectionScreen = videoSystemSubInstance.getCstic(HomeTheaterMockImpl.PROJECTION_SCREEN);
		projectionScreen.setAssignedValues(assignedValuesProjectionScreen);
		classUnderTest.checkCstic(model, videoSystemSubInstance, projectionScreen);

		assertEquals(2, model.getSolvableConflicts().size());

	}

	@Test
	public void testConflictSetsOverallConsistency()
	{
		final CsticModel lightSource = getLightSourceCsticWithConflict();
		classUnderTest.checkCstic(model, videoSystemSubInstance, lightSource);
		assertFalse(model.getRootInstance().isConsistent());
	}

	@Test
	public void testTowerSpeakerInVisibleForDifferentType()
	{

		final List<CsticValueModel> assignedValues1 = getAssignedValueList(HomeTheaterMockImpl.ROOM_SIZE_LARGE);
		final CsticModel roomSize = instance.getCstic(HomeTheaterMockImpl.ROOM_SIZE);
		roomSize.setAssignedValues(assignedValues1);

		final List<CsticValueModel> assignedValues = getAssignedValueList(HomeTheaterMockImpl.TYPE_COMPACT_FRONT);
		final CsticModel speakerType = audioSystemSubInstance.getCstic(HomeTheaterMockImpl.SPEAKERTYPEFRONT);
		speakerType.setAssignedValues(assignedValues);

		final CsticModel towerSpeaker = audioSystemSubInstance.getCstic(HomeTheaterMockImpl.TOWER_SPEAKER);
		classUnderTest.checkCstic(model, audioSystemSubInstance, towerSpeaker);
		assertEquals(false, towerSpeaker.isVisible());
		assertTrue(model.getRootInstance().isConsistent());
	}

	@Test
	public void testTowerSpeakerVisibleForType()
	{

		final List<CsticValueModel> assignedValues1 = getAssignedValueList(HomeTheaterMockImpl.ROOM_SIZE_LARGE);
		final CsticModel roomSize = instance.getCstic(HomeTheaterMockImpl.ROOM_SIZE);
		roomSize.setAssignedValues(assignedValues1);

		final List<CsticValueModel> assignedValues = getAssignedValueList(HomeTheaterMockImpl.TYPE_TOWER_FRONT);
		final CsticModel speakerType = audioSystemSubInstance.getCstic(HomeTheaterMockImpl.SPEAKERTYPEFRONT);
		speakerType.setAssignedValues(assignedValues);

		final CsticModel towerSpeaker = audioSystemSubInstance.getCstic(HomeTheaterMockImpl.TOWER_SPEAKER);
		classUnderTest.checkCstic(model, audioSystemSubInstance, towerSpeaker);
		assertEquals(true, towerSpeaker.isVisible());

	}




	@Test
	public void testCompactSpeakerVisibleForDifferentTypeAndSmallRoom()
	{

		final List<CsticValueModel> assignedValues = getAssignedValueList(HomeTheaterMockImpl.TYPE_SOUNDBAR);
		final CsticModel speakerType = audioSystemSubInstance.getCstic(HomeTheaterMockImpl.SPEAKERTYPE);
		speakerType.setAssignedValues(assignedValues);

		final List<CsticValueModel> assignedValues1 = getAssignedValueList(HomeTheaterMockImpl.ROOM_SIZE_SMALL);
		final CsticModel roomSize = instance.getCstic(HomeTheaterMockImpl.ROOM_SIZE);
		roomSize.setAssignedValues(assignedValues1);

		final CsticModel compactSpeaker = audioSystemSubInstance.getCstic(HomeTheaterMockImpl.SPEAKER_COMPACT);
		classUnderTest.checkCstic(model, audioSystemSubInstance, compactSpeaker);
		assertEquals(true, compactSpeaker.isVisible());

	}



	@Test
	public void testTowerSpeakerInvisible()
	{

		final List<CsticValueModel> assignedValues = getAssignedValueList(HomeTheaterMockImpl.ROOM_SIZE_SMALL);
		final CsticModel roomSize = instance.getCstic(HomeTheaterMockImpl.ROOM_SIZE);
		roomSize.setAssignedValues(assignedValues);

		final CsticModel towerSpeaker = audioSystemSubInstance.getCstic(HomeTheaterMockImpl.TOWER_SPEAKER);
		classUnderTest.checkCstic(model, audioSystemSubInstance, towerSpeaker);
		assertEquals(false, towerSpeaker.isVisible());

	}


	@Test
	public void testSubInstancesCreated()
	{
		final List<InstanceModel> instances = instance.getSubInstances();
		assertEquals(3, instances.size());

		InstanceModel instance = instances.get(0);
		assertEquals("CONF_HT_VIDEO_SYSTEM", instance.getName());
		assertEquals(3, instance.getCsticGroups().size());
		assertEquals(5, instance.getCstics().size());

		instance = instances.get(1);
		assertEquals("CONF_HT_AUDIO_SYSTEM", instance.getName());
		assertEquals(4, instance.getCsticGroups().size());
		assertEquals(9, instance.getCstics().size());

		instance = instances.get(2);
		assertEquals("CONF_HT_COMPONENTS_SYSTEM", instance.getName());
		assertEquals(1, instance.getCsticGroups().size());
		assertEquals(3, instance.getCstics().size());

	}

	@Test
	public void testCheckModel()
	{
		model.setBasePrice(PriceModel.NO_PRICE);
		classUnderTest.checkModel(model);
		assertEquals(HomeTheaterMockImpl.BASE_PRICE, model.getBasePrice().getPriceValue().longValue());
	}

	@Test
	public void testCheckCsticColumnSpeaker()
	{
		final CsticModel cstic = audioSystemSubInstance.getCstic(HomeTheaterMockImpl.COLUMN_SPEAKER);

		classUnderTest.checkCstic(model, instance, cstic);
		assertEquals(HomeTheaterMockImpl.SURCHARGE_COLUMN_SPEAKER_2050,
				cstic.getAssignableValues().get(2).getValuePrice().getPriceValue().longValue());
	}

	@Test
	public void testCheckCsticFrontSpeaker()
	{
		final CsticModel cstic = audioSystemSubInstance.getCstic(HomeTheaterMockImpl.SPEAKER_COMPACT_FRONT);

		classUnderTest.checkCstic(model, audioSystemSubInstance, cstic);
		assertEquals(HomeTheaterMockImpl.SURCHARGE_SPEAKER_COMPACT_FRONT_Q,
				cstic.getAssignableValues().get(1).getValuePrice().getPriceValue().longValue());
	}

	@Test
	public void testCheckCsticRearSpeaker()
	{
		final CsticModel cstic = audioSystemSubInstance.getCstic(HomeTheaterMockImpl.REAR_SPEAKER);

		classUnderTest.checkCstic(model, instance, cstic);
		assertEquals(HomeTheaterMockImpl.SURCHARGE_REAR_SPEAKER_CANTON,
				cstic.getAssignableValues().get(1).getValuePrice().getPriceValue().longValue());
	}

	@Test
	public void testCheckCsticSubwoofer()
	{
		final CsticModel cstic = audioSystemSubInstance.getCstic(HomeTheaterMockImpl.SUBWOOFER);

		classUnderTest.checkCstic(model, instance, cstic);
		assertEquals(HomeTheaterMockImpl.SURCHARGE_SUBWOOFER_SONOS,
				cstic.getAssignableValues().get(1).getValuePrice().getPriceValue().longValue());
	}

	@Test
	public void testCheckGamingConflict()
	{
		final CsticModel cstic = getGamingCsticWithConflict();
		final List<SolvableConflictModel> conflicts = new ArrayList<>();
		classUnderTest.checkGamingConflict(sourceComponentSubInstance, cstic, conflicts, model);
		assertTrue(CollectionUtils.isNotEmpty(conflicts));
		final SolvableConflictModel conflict = conflicts.get(0);
		assertEquals(HomeTheaterMockImpl.CONFLICT_TEXT_GAMING, conflict.getDescription());
		assertEquals(2, conflict.getConflictingAssumptions().size());
	}

	@Test
	public void testCheckCsticsGaming()
	{
		final CsticModel cstic = getGamingCsticWithConflict();

		classUnderTest.checkCstic(model, sourceComponentSubInstance,
				sourceComponentSubInstance.getCstic(HomeTheaterMockImpl.GAMING_CONSOLE));
		assertTrue(CollectionUtils.isNotEmpty(model.getSolvableConflicts()));

	}

	@Test
	public void testRemoveAssignableValue()
	{
		classUnderTest.removeAssignableValue(audioSystemSubInstance, HomeTheaterMockImpl.SPEAKERTYPEFRONT,
				HomeTheaterMockImpl.TYPE_TOWER_FRONT);
		final CsticModel speakerType = audioSystemSubInstance.getCstic(HomeTheaterMockImpl.SPEAKERTYPEFRONT);
		assertEquals(2, speakerType.getAssignableValues().size());
	}

	@Test
	public void testRemoveAssignableValueNotFound()
	{
		classUnderTest.removeAssignableValue(audioSystemSubInstance, HomeTheaterMockImpl.SPEAKERTYPEFRONT,
				HomeTheaterMockImpl.PROJECTOR_LCD);
		final CsticModel speakerType = audioSystemSubInstance.getCstic(HomeTheaterMockImpl.SPEAKERTYPEFRONT);
		assertEquals(3, speakerType.getAssignableValues().size());
	}

	@Test
	public void testRetrieveAssignableValue()
	{
		final CsticValueModel result = classUnderTest.retrieveAssignableValue(audioSystemSubInstance,
				HomeTheaterMockImpl.SPEAKERTYPEFRONT, HomeTheaterMockImpl.PROJECTOR_LCD);
		assertNull(result);
	}

	@Test
	public void testRetrieveAssignableValueNotFound()
	{
		final CsticValueModel result = classUnderTest.retrieveAssignableValue(audioSystemSubInstance,
				HomeTheaterMockImpl.SPEAKERTYPEFRONT, HomeTheaterMockImpl.TYPE_TOWER_FRONT);
		assertNotNull(result);
		assertEquals(HomeTheaterMockImpl.TYPE_TOWER_FRONT, result.getName());
	}

	@Test
	public void testAddAssignableValue()
	{
		classUnderTest.addAssignableValue(audioSystemSubInstance, HomeTheaterMockImpl.SPEAKERTYPEFRONT,
				HomeTheaterMockImpl.PROJECTOR_LCD, HomeTheaterMockImpl.PROJECTOR_LCD_DESCRIPTION, 1);
		final CsticModel cstic = audioSystemSubInstance.getCstic(HomeTheaterMockImpl.SPEAKERTYPEFRONT);
		assertEquals(4, cstic.getAssignableValues().size());
		assertEquals(HomeTheaterMockImpl.PROJECTOR_LCD, cstic.getAssignableValues().get(1).getName());

	}

	protected CsticModel getGamingCsticWithConflict()
	{
		final CsticModel cstic = sourceComponentSubInstance.getCstic(HomeTheaterMockImpl.GAMING_CONSOLE);
		cstic.addValue(HomeTheaterMockImpl.GAMING_CONSOLE_YES);

		final CsticModel csticProjectorType = videoSystemSubInstance.getCstic(HomeTheaterMockImpl.PROJECTOR_TYPE);
		csticProjectorType.clearValues();
		csticProjectorType.addValue(HomeTheaterMockImpl.PROJECTOR_LCD);
		return cstic;
	}


	protected List<CsticValueModel> getAssignedValueList(final String value)
	{
		final List<CsticValueModel> assignedValues = new ArrayList<>();
		final CsticValueModel csticValue = new CsticValueModelImpl();
		csticValue.setName(value);
		assignedValues.add(csticValue);

		return assignedValues;
	}


	protected CsticModel getLightSourceCsticWithConflict()
	{
		final List<CsticValueModel> assignedValues = getAssignedValueList(HomeTheaterMockImpl.PROJECTOR_NONE);
		final CsticModel projectorType = videoSystemSubInstance.getCstic(HomeTheaterMockImpl.PROJECTOR_TYPE);
		projectorType.setAssignedValues(assignedValues);

		final List<CsticValueModel> assignedValuesLightSource = getAssignedValueList(HomeTheaterMockImpl.LIGHT_SOURCE_LAMP);
		final CsticModel lightSource = videoSystemSubInstance.getCstic(HomeTheaterMockImpl.LIGHT_SOURCE);
		lightSource.setAssignedValues(assignedValuesLightSource);
		return lightSource;
	}



}
