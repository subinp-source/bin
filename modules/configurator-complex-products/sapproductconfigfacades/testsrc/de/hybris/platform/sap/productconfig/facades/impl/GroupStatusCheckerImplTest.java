package de.hybris.platform.sap.productconfig.facades.impl;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.sap.productconfig.facades.CsticData;
import de.hybris.platform.sap.productconfig.facades.CsticValueData;
import de.hybris.platform.sap.productconfig.facades.UiGroupData;
import de.hybris.platform.sap.productconfig.facades.UiType;
import de.hybris.platform.sap.productconfig.runtime.interf.CsticGroup;
import de.hybris.platform.sap.productconfig.runtime.interf.impl.CsticGroupImpl;
import de.hybris.platform.sap.productconfig.runtime.interf.model.CsticModel;
import de.hybris.platform.sap.productconfig.runtime.interf.model.impl.CsticModelImpl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Test;


@SuppressWarnings("javadoc")
@UnitTest
public class GroupStatusCheckerImplTest
{
	private final GroupStatusCheckerImpl classUnderTest = new GroupStatusCheckerImpl();

	@Test
	public void testCheckCompletenessGroupNull()
	{
		assertTrue(classUnderTest.checkCompleteness(null));
	}

	@Test
	public void testCheckCompletenessCsticListNull()
	{
		final UiGroupData group = new UiGroupData();
		group.setCstics(null);
		assertTrue(classUnderTest.checkCompleteness(group));
	}

	@Test
	public void testCheckCompletenessCsticListEmpty()
	{
		final UiGroupData group = new UiGroupData();
		group.setCstics(Collections.EMPTY_LIST);
		assertTrue(classUnderTest.checkCompleteness(group));
	}

	@Test
	public void testCheckCompletenessCsticListWithNonRequiredCsticsWithValues()
	{
		assertTrue(classUnderTest.checkCompleteness(createGroupWithNonRequiredCsticsWithValues()));
	}

	@Test
	public void testCheckCompletenessCsticListWithNonRequiredCsticsWithoutValues()
	{
		assertTrue(classUnderTest.checkCompleteness(createGroupWithNonRequiredCsticsWithoutValues()));
	}

	@Test
	public void testCheckCompletenessCsticListWithRequiredCsticsWithValues()
	{
		assertTrue(classUnderTest.checkCompleteness(createGroupWithRequiredCsticsWithValues()));
	}

	@Test
	public void testCheckCompletenessCsticListWithRequiredCsticsWithoutValues()
	{
		assertFalse(classUnderTest.checkCompleteness(createGroupWithRequiredCsticsWithoutValues()));
	}

	@Test
	public void testCheckCompletenessCsticListWithRequiredMultiValueCsticsWithoutValues()
	{
		assertFalse(classUnderTest.checkCompleteness(createGroupWithRequiredMultiValueCsticsWithoutValues()));
	}

	@Test
	public void testCheckCompletenessCsticListWithRequiredMultiValueCsticsWithValues()
	{
		assertTrue(classUnderTest.checkCompleteness(createGroupWithRequiredMultiValueCsticsWithValues()));
	}

	@Test
	public void testCheckCompletenessCsticListWithRequiredMultiImageValueCsticsWithoutValues()
	{
		assertFalse(classUnderTest.checkCompleteness(createGroupWithRequiredMultiImageValueCsticsWithoutValues()));
	}

	@Test
	public void testCheckCompletenessCsticListWithRequiredMultiImageValueCsticsWithValues()
	{
		assertTrue(classUnderTest.checkCompleteness(createGroupWithRequiredMultiImageValueCsticsWithValues()));
	}

	@Test
	public void testCheckCompletenessForSubGroupsNull()
	{
		assertTrue(classUnderTest.checkCompletenessForSubGroups(null));
	}

	@Test
	public void testCheckCompletenessForSubGroupsEmpty()
	{
		assertTrue(classUnderTest.checkCompletenessForSubGroups(Collections.EMPTY_LIST));
	}

	@Test
	public void testCheckCompletenessForSubGroupsAllComplete()
	{
		assertTrue(classUnderTest.checkCompletenessForSubGroups(createGroupListCompleteConsistent()));
	}

	@Test
	public void testCheckCompletenessForSubGroupsAllIncomplete()
	{
		assertFalse(classUnderTest.checkCompletenessForSubGroups(createGroupListIncompleteInconsistent()));
	}

	@Test
	public void testCheckCompletenessForSubGroupsMixed()
	{
		assertFalse(classUnderTest.checkCompletenessForSubGroups(createGroupListMixed()));
	}

	@Test
	public void testCheckConsistencyNull()
	{
		assertTrue(classUnderTest.checkConsistency(null));
	}

	@Test
	public void testCheckConsistencyEmpty()
	{
		final CsticGroup group = new CsticGroupImpl();
		group.setCstics(Collections.EMPTY_LIST);
		assertTrue(classUnderTest.checkConsistency(group));
	}

	@Test
	public void testCheckConsistencyCsticListNull()
	{
		final CsticGroup group = new CsticGroupImpl();
		group.setCstics(null);
		assertTrue(classUnderTest.checkConsistency(group));
	}

	@Test
	public void testCheckConsistencyAllConsistent()
	{
		assertTrue(classUnderTest.checkConsistency(createGroupCsticsAllConsistent()));
	}

	@Test
	public void testCheckConsistencyAllInconsistent()
	{
		assertFalse(classUnderTest.checkConsistency(createGroupCsticsAllInconsistent()));
	}

	@Test
	public void testCheckConsistencyMixed()
	{
		assertFalse(classUnderTest.checkConsistency(createGroupCsticsMixed()));
	}

	@Test
	public void testCheckConsistencyForSubGroupsNull()
	{
		assertTrue(classUnderTest.checkConsistencyForSubGroups(null));
	}

	@Test
	public void testCheckConsistencyForSubGroupsEmpty()
	{
		assertTrue(classUnderTest.checkConsistencyForSubGroups(Collections.EMPTY_LIST));
	}

	@Test
	public void testCheckConsistencyForSubGroupsAllConsistent()
	{
		assertTrue(classUnderTest.checkConsistencyForSubGroups(createGroupListCompleteConsistent()));
	}

	@Test
	public void testCheckConsistencyForSubGroupsAllInconsistent()
	{
		assertFalse(classUnderTest.checkConsistencyForSubGroups(createGroupListIncompleteInconsistent()));
	}

	@Test
	public void testCheckConsistencyForSubGroupsMixed()
	{
		assertFalse(classUnderTest.checkConsistencyForSubGroups(createGroupListMixed()));
	}

	private CsticGroup createGroupCsticsAllConsistent()
	{
		final List<CsticModel> cstics = new ArrayList();
		cstics.add(createCstic("CSTIC1", false, false, true, "VALUE1"));
		cstics.add(createCstic("CSTIC2", false, false, true, "VALUE2"));
		final CsticGroup group = new CsticGroupImpl();
		group.setCstics(cstics);
		return group;

	}

	private CsticGroup createGroupCsticsAllInconsistent()
	{
		final List<CsticModel> cstics = new ArrayList();
		cstics.add(createCstic("CSTIC1", false, false, false, "VALUE1"));
		cstics.add(createCstic("CSTIC2", false, false, false, "VALUE2"));
		final CsticGroup group = new CsticGroupImpl();
		group.setCstics(cstics);
		return group;
	}

	private CsticGroup createGroupCsticsMixed()
	{
		final List<CsticModel> cstics = new ArrayList();
		cstics.add(createCstic("CSTIC1", false, false, false, "VALUE1"));
		cstics.add(createCstic("CSTIC2", false, false, true, "VALUE2"));
		final CsticGroup group = new CsticGroupImpl();
		group.setCstics(cstics);
		return group;
	}



	private List<UiGroupData> createGroupListIncompleteInconsistent()
	{
		final List<UiGroupData> groups = new ArrayList();
		addNewGroup(groups, false, false);
		addNewGroup(groups, false, false);
		return groups;
	}

	private List<UiGroupData> createGroupListMixed()
	{
		final List<UiGroupData> groups = new ArrayList();
		addNewGroup(groups, true, true);
		addNewGroup(groups, false, false);
		return groups;
	}

	private List<UiGroupData> createGroupListCompleteConsistent()
	{
		final List<UiGroupData> groups = new ArrayList();
		addNewGroup(groups, true, true);
		addNewGroup(groups, true, true);
		return groups;
	}

	protected void addNewGroup(final List<UiGroupData> groups, final boolean complete, final boolean consistent)
	{
		final UiGroupData group = new UiGroupData();
		group.setComplete(complete);
		group.setConsistent(consistent);
		groups.add(group);
	}

	protected UiGroupData createGroupWithNonRequiredCsticsWithValues()
	{
		final List<CsticData> cstics = new ArrayList();

		cstics.add(createCstic("CSTIC1", UiType.RADIO_BUTTON, false, "VALUE1"));
		cstics.add(createCstic("CSTIC2", UiType.RADIO_BUTTON, false, "VALUE2"));
		final UiGroupData group = new UiGroupData();
		group.setCstics(cstics);
		return group;
	}

	protected UiGroupData createGroupWithRequiredCsticsWithoutValues()
	{
		final List<CsticData> cstics = new ArrayList();

		cstics.add(createCstic("CSTIC1", UiType.RADIO_BUTTON, true, "VALUE1"));
		cstics.add(createCstic("CSTIC2", UiType.RADIO_BUTTON, true, null));
		final UiGroupData group = new UiGroupData();
		group.setCstics(cstics);
		return group;
	}

	protected UiGroupData createGroupWithRequiredCsticsWithValues()
	{
		final List<CsticData> cstics = new ArrayList();

		cstics.add(createCstic("CSTIC1", UiType.RADIO_BUTTON, true, "VALUE1"));
		cstics.add(createCstic("CSTIC2", UiType.RADIO_BUTTON, true, "VALUE2"));
		final UiGroupData group = new UiGroupData();
		group.setCstics(cstics);
		return group;
	}

	protected UiGroupData createGroupWithNonRequiredCsticsWithoutValues()
	{
		final List<CsticData> cstics = new ArrayList();

		cstics.add(createCstic("CSTIC1", UiType.RADIO_BUTTON, false, "VALUE1"));
		cstics.add(createCstic("CSTIC2", UiType.RADIO_BUTTON, false, null));
		final UiGroupData group = new UiGroupData();
		group.setCstics(cstics);
		return group;
	}

	protected UiGroupData createGroupWithRequiredMultiValueCsticsWithoutValues()
	{
		final List<CsticData> cstics = new ArrayList();

		cstics.add(createCstic("CSTIC1", UiType.CHECK_BOX_LIST, true, "VALUE1"));
		cstics.add(createCstic("CSTIC2", UiType.CHECK_BOX_LIST, true, null));
		final UiGroupData group = new UiGroupData();
		group.setCstics(cstics);
		return group;
	}

	protected UiGroupData createGroupWithRequiredMultiValueCsticsWithValues()
	{
		final List<CsticData> cstics = new ArrayList();

		cstics.add(createCstic("CSTIC1", UiType.CHECK_BOX_LIST, true, "VALUE1"));
		cstics.add(createCstic("CSTIC2", UiType.CHECK_BOX_LIST, true, "VALUE2"));
		final UiGroupData group = new UiGroupData();
		group.setCstics(cstics);
		return group;
	}

	protected UiGroupData createGroupWithRequiredMultiImageValueCsticsWithoutValues()
	{
		final List<CsticData> cstics = new ArrayList();

		cstics.add(createCstic("CSTIC1", UiType.MULTI_SELECTION_IMAGE, true, "VALUE1"));
		cstics.add(createCstic("CSTIC2", UiType.MULTI_SELECTION_IMAGE, true, null));
		final UiGroupData group = new UiGroupData();
		group.setCstics(cstics);
		return group;
	}

	protected UiGroupData createGroupWithRequiredMultiImageValueCsticsWithValues()
	{
		final List<CsticData> cstics = new ArrayList();

		cstics.add(createCstic("CSTIC1", UiType.MULTI_SELECTION_IMAGE, true, "VALUE1"));
		cstics.add(createCstic("CSTIC2", UiType.MULTI_SELECTION_IMAGE, true, "VALUE2"));
		final UiGroupData group = new UiGroupData();
		group.setCstics(cstics);
		return group;
	}

	protected CsticData createCstic(final String key, final UiType type, final boolean required, final String value)
	{
		final CsticData cstic = new CsticData();
		cstic.setKey(key);
		cstic.setType(type);
		cstic.setRequired(required);
		cstic.setValue(value);
		if (type == UiType.CHECK_BOX_LIST || type == UiType.MULTI_SELECTION_IMAGE)
		{
			cstic.setValue(null);
			final List<CsticValueData> domainvalues = new ArrayList();
			final CsticValueData domainValue1 = new CsticValueData();
			domainValue1.setName("NOTSELECTEDVALUE");
			domainValue1.setSelected(false);
			domainvalues.add(domainValue1);

			final CsticValueData domainValue2 = new CsticValueData();
			domainValue2.setName(value);
			if (value != null)
			{
				domainValue2.setSelected(true);
			}
			else
			{
				domainValue2.setSelected(false);
			}
			domainvalues.add(domainValue2);
			cstic.setDomainvalues(domainvalues);
		}
		return cstic;
	}

	protected CsticModel createCstic(final String name, final boolean multivalued, final boolean required,
			final boolean consistent, final String value)
	{
		final CsticModel cstic = new CsticModelImpl();
		cstic.setName(name);
		cstic.setMultivalued(multivalued);
		cstic.setRequired(required);
		cstic.setConsistent(consistent);
		//cstic.setValue(value);
		if (cstic.isMultivalued())
		{
			//cstic.setValue(null);
			final List<CsticValueData> domainvalues = new ArrayList();
			final CsticValueData domainValue1 = new CsticValueData();
			domainValue1.setName("NOTSELECTEDVALUE");
			domainValue1.setSelected(false);
			domainvalues.add(domainValue1);

			final CsticValueData domainValue2 = new CsticValueData();
			domainValue2.setName(value);
			if (value != null)
			{
				domainValue2.setSelected(true);
			}
			else
			{
				domainValue2.setSelected(false);
			}
			domainvalues.add(domainValue2);
			//cstic.setDomainvalues(domainvalues);
		}

		return cstic;
	}
}
