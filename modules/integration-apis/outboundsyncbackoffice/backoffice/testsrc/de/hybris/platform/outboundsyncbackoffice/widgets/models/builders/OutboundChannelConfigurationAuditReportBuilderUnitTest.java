package de.hybris.platform.outboundsyncbackoffice.widgets.models.builders;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.outboundsync.model.OutboundChannelConfigurationModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.assertj.core.util.Maps;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.gson.Gson;

@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class OutboundChannelConfigurationAuditReportBuilderUnitTest
{
	@InjectMocks
	private OutboundChannelConfigurationAuditReportBuilder auditReportBuilder = new OutboundChannelConfigurationAuditReportBuilder();

	@Test
	public void testTraversePayload()
	{
		final Gson jsonToMap = new Gson();

		final HashMap<String, String> emptyMap = new HashMap<>();
		auditReportBuilder.traversePayload(emptyMap);
		assertTrue(emptyMap.isEmpty());

		final Map map1 = Maps.newHashMap("test_id", "test");
		auditReportBuilder.traversePayload(map1);
		assertTrue(map1.containsKey("test_id"));

		final Map map2 = Maps.newHashMap("testList", new ArrayList<>());
		auditReportBuilder.traversePayload(map2);
		assertTrue(map2.containsKey("testList"));
		assertTrue(((List) map2.get("testList")).isEmpty());

		// use type_id as key and remove list.
		final HashMap map3 = jsonToMap.fromJson("{CList: [{\"C1_id\": \"1\"}]}", HashMap.class);
		auditReportBuilder.traversePayload(map3);
		final HashMap expected = jsonToMap.fromJson("{\"C1_id : 1\": {}}", HashMap.class);
		assertEquals(map3, expected);

		final HashMap map4 = jsonToMap.fromJson(
				"{CList: [{C1_id: \"1\", C1Name: \"cn\", C1Url: \"sap.com\"}, {C2_id: \"2\", C2Name: \"x\", C2Url: \"Griffin\"}]}",
				HashMap.class);
		auditReportBuilder.traversePayload(map4);
		final HashMap expected4 = jsonToMap.fromJson(
				"{\"C1_id : 1\": {C1Name: \"cn\", C1Url: \"sap.com\"}, \"C2_id : 2\": {C2Name: \"x\", C2Url: \"Griffin\"}}",
				HashMap.class);
		assertEquals(map4, expected4);

		final HashMap map5 = jsonToMap.fromJson(
				"{l1: [{O1_id: \"v1\"}, {O2_id: \"O2_v\", O2_l: [{subO2_key_id: \"v\"}, {subO3_key_id: \"v\"}]}, {}]}",
				HashMap.class);
		auditReportBuilder.traversePayload(map5);
		final HashMap expected5 = jsonToMap.fromJson(
				"{\"O1_id : v1\": {}, \"O2_id : O2_v\": {\"subO3_key_id : v\": {}, \"subO2_key_id : v\": {}}}",
				HashMap.class);
		assertEquals(map5, expected5);

		//use typeA_id of typeA as key in a map.
		final HashMap map6 = jsonToMap.fromJson(
				"{integration: {integration_id: \"A\"}}",
				HashMap.class);
		auditReportBuilder.traversePayload(map6);
		final HashMap expected6 = jsonToMap.fromJson("{\"integration_id : A\": {}}",
				HashMap.class);
		assertEquals(map6, expected6);

		final HashMap map7 = jsonToMap.fromJson(
				"{team: {team_id: \"1\", teamProp: {object: {object_id: \"stout\"}}}}",
				HashMap.class);
		auditReportBuilder.traversePayload(map7);
		final HashMap expected7 = jsonToMap.fromJson("{\"team_id : 1\": {teamProp: {\"object_id : stout\": {}}}}",
				HashMap.class);
		assertEquals(map7, expected7);

	}

	@Test
	public void testGetDownloadFileName()
	{
		final ItemModel itemModel = mock(OutboundChannelConfigurationModel.class);
		auditReportBuilder.setSelectedModel(itemModel);
		when(((OutboundChannelConfigurationModel) itemModel).getCode()).thenReturn("testOutboundChannelConfiguration");
		assertEquals(auditReportBuilder.getDownloadFileName(), "testOutboundChannelConfiguration");
	}
}