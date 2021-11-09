/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.cockpitng.dataaccess.facades.permissions;

import com.hybris.backoffice.daos.BackofficeUserRightsDao;
import com.hybris.cockpitng.dataaccess.facades.permissions.Permission;
import com.hybris.cockpitng.dataaccess.facades.permissions.PermissionInfo;
import com.hybris.cockpitng.labels.LabelService;
import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.core.Tenant;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.security.PrincipalModel;
import de.hybris.platform.core.model.security.UserRightModel;
import de.hybris.platform.core.model.type.AtomicTypeModel;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.jalo.security.JaloSecurityException;
import de.hybris.platform.jalo.security.Principal;
import de.hybris.platform.jalo.security.UserRight;
import de.hybris.platform.jalo.type.AttributeDescriptor;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.security.permissions.PermissionAssignment;
import de.hybris.platform.servicelayer.security.permissions.PermissionCheckResult;
import de.hybris.platform.servicelayer.security.permissions.PermissionCheckingService;
import de.hybris.platform.servicelayer.security.permissions.PermissionManagementService;
import de.hybris.platform.servicelayer.type.TypeService;
import de.hybris.platform.servicelayer.user.UserService;
import org.apache.commons.lang.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyCollection;
import static org.mockito.Matchers.anyList;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class DefaultPlatformPermissionManagementFacadeStrategyTest
{
	private static final Logger LOG = LoggerFactory.getLogger(DefaultPlatformPermissionManagementFacadeStrategyTest.class);
	private static final String PRINCIPAL_ID = "PRINCIPAL_ID";
	private static final String TYPE_CODE = "Product";
	private static final String PERMISSION_NAME_READ = "read";
	private static final String PERMISSION_NAME_CHANGE = "change";
	private static final String PERMISSION_NAME_REMOVE = "remove";
	private static final String PERMISSION_NAME_CREATE = "create";
	private static final String COMPOSED_TYPE_MODEL_CODE = "ComposedTypeModel";
	private static final String ATTRIBUTE_DESCRIPTOR_MODEL_CODE = "AttributeDescriptorModel";
	private static final String FIELD_CODE = "code";
	private static final String LABEL = "label";
	private static final int USER_RIGHT_PK = 42;
	private static final int EXPECTED_PERMISSIONS_NUMBER = 4;

	@InjectMocks
	private transient DefaultPlatformPermissionManagementFacadeStrategy testSubject;

	@Mock
	private transient PrincipalModel testUser;

	@Mock
	private transient TypeService typeService;

	@Mock
	private transient UserService userService;

	@Mock
	private transient ModelService modelService;

	@Mock
	private transient LabelService labelService;

	@Mock
	private transient FlexibleSearchService flexibleSearchService;

	@Mock
	private transient PermissionCheckingService permissionCheckingService;

	@Mock
	private transient PermissionManagementService permissionManagementService;

	@Mock
	private transient BackofficeUserRightsDao backofficeUserRightsDao;

	@Mock
	private transient ComposedTypeModel composedTypeModel;

	@Mock
	private transient ComposedTypeModel composedTypeModelForAttributeDescriptorModel;

	@Mock
	private transient AttributeDescriptorModel attributeDescriptorModel;

	@Mock
	private transient PermissionCheckResult permissionCheckResult;

	@Mock
	private transient PermissionAssignment permissionAssignment;

	@Mock
	private transient Principal jaloPrincipal;

	@Mock
	private transient Tenant tenant;

	@Mock
	private transient UserModel userModel;

	private transient Collection<PermissionAssignment> permissionAssignments;
	private transient Permission permission;
	private transient Map<UserRight, String> userRightMap;
	private transient Set<AttributeDescriptorModel> attributeDescriptorModels;

	@Before
	public void setUp()
	{
		when(testUser.getUid()).thenReturn(PRINCIPAL_ID);
		when(flexibleSearchService.getModelByExample(Mockito.any())).thenReturn(testUser);
		when(typeService.getComposedTypeForCode(TYPE_CODE)).thenReturn(composedTypeModel);

		final PermissionAssignment permissionAssignmentRead = new PermissionAssignment(PERMISSION_NAME_READ, testUser);
		final PermissionAssignment permissionAssignmentChange = new PermissionAssignment(PERMISSION_NAME_CHANGE, testUser);
		final PermissionAssignment permissionAssignmentCreate = new PermissionAssignment(PERMISSION_NAME_CREATE, testUser);
		final PermissionAssignment permissionAssignmentRemove = new PermissionAssignment(PERMISSION_NAME_REMOVE, testUser);

		permissionAssignments = new ArrayList<>();
		permissionAssignments.add(permissionAssignmentRead);
		permissionAssignments.add(permissionAssignmentChange);
		permissionAssignments.add(permissionAssignmentCreate);
		permissionAssignments.add(permissionAssignmentRemove);

		when(permissionManagementService.getTypePermissions(composedTypeModel)).thenReturn(permissionAssignments);
		when(permissionManagementService.getTypePermissionsForPrincipal(composedTypeModel, testUser))
				.thenReturn(permissionAssignments);

		doAnswer((Answer<Object>) invocation -> permissionCheckResult).when(permissionCheckingService)
				.checkAttributeDescriptorPermission(TYPE_CODE, FIELD_CODE, PERMISSION_NAME_CREATE);
		doAnswer((Answer<Object>) invocation -> permissionCheckResult).when(permissionCheckingService)
				.checkAttributeDescriptorPermission(TYPE_CODE, FIELD_CODE, PERMISSION_NAME_CHANGE);
		doAnswer((Answer<Object>) invocation -> permissionCheckResult).when(permissionCheckingService)
				.checkAttributeDescriptorPermission(TYPE_CODE, FIELD_CODE, PERMISSION_NAME_REMOVE);

		final Collection<UserRightModel> userRightModels = new ArrayList<>();
		userRightModels.add(mock(UserRightModel.class));
		when(backofficeUserRightsDao.findUserRightsByCode(anyString())).thenReturn(userRightModels);

		final UserRight userRight = new UserRight()
		{
			@Override
			public de.hybris.platform.core.PK getPK()
			{
				return de.hybris.platform.core.PK.fromLong(USER_RIGHT_PK);
			}

			@Override
			public Tenant getTenant()
			{
				return tenant;
			}
		};

		userModel.setUid(testUser.getUid());

		userRightMap = new HashMap<>();
		userRightMap.put(userRight, StringUtils.EMPTY);

		when(attributeDescriptorModel.getQualifier()).thenReturn(FIELD_CODE);
		when(attributeDescriptorModel.getName()).thenReturn(PERMISSION_NAME_READ);

		attributeDescriptorModels = new HashSet<>();
		attributeDescriptorModels.add(attributeDescriptorModel);

		when(userModel.getUid()).thenReturn(PRINCIPAL_ID);
		when(userService.getUserForUID(PRINCIPAL_ID)).thenReturn(userModel);
		when(userService.isUserExisting(PRINCIPAL_ID)).thenReturn(Boolean.TRUE);
		when(modelService.getSource(testUser)).thenReturn(jaloPrincipal);
		when(modelService.getAll(anyCollection(), anyCollection())).thenReturn(userRightModels);
		when(labelService.getObjectLabel(PRINCIPAL_ID)).thenReturn(LABEL);
	}

	@Test
	public void shouldReturnPermissionInfosOfPrincipalsWhoHasPermissionAssignmentsForTheType()
	{
		// given
		when(typeService.getAttributeDescriptorsForType(composedTypeModel)).thenReturn(attributeDescriptorModels);
		when(permissionManagementService.getAttributePermissions(attributeDescriptorModel)).thenReturn(permissionAssignments);
		when(composedTypeModel.getCode()).thenReturn(TYPE_CODE);

		// when
		final Collection<PermissionInfo> permissionInfos = testSubject.getPrincipalsWithPermissionAssignment(TYPE_CODE);

		// then
		assertThat(permissionInfos).isNotNull().isNotEmpty();
	}

	@Test
	public void shouldReturnTypePermissionForPrincipalAndTypeCodeAndPermissionName()
	{
		// when
		final Permission permission = testSubject.getTypePermission(PRINCIPAL_ID, TYPE_CODE, PERMISSION_NAME_READ);

		// then
		assertThat(permission).isNotNull();
	}

	@Test
	public void shouldReturnFieldPermissionForPrincipalAndTypeCodeWithFieldCodeAndPermissionName()
	{
		// given
		when(permissionCheckingService.checkAttributeDescriptorPermission(TYPE_CODE, FIELD_CODE, PERMISSION_NAME_READ))
				.thenReturn(permissionCheckResult);

		// when
		final Permission permission = testSubject.getFieldPermission(PRINCIPAL_ID, TYPE_CODE, FIELD_CODE, PERMISSION_NAME_READ);

		// then
		assertThat(permission).isNotNull();
		assertThat(permission.getField()).isNotNull();
	}

	@Test
	public void shouldRetrieveFieldPermissionInfoForPrincipalAndTypeCodeWithFieldCode()
	{
		// given
		when(typeService.getAttributeDescriptorsForType(composedTypeModel)).thenReturn(attributeDescriptorModels);
		when(typeService.getAttributeDescriptor(composedTypeModel, FIELD_CODE)).thenReturn(attributeDescriptorModel);
		when(permissionManagementService.getAttributePermissions(attributeDescriptorModel)).thenReturn(permissionAssignments);

		// when
		final PermissionInfo permissionInfo = testSubject.getFieldPermissionInfo(PRINCIPAL_ID, TYPE_CODE, FIELD_CODE);

		// then
		assertThat(permissionInfo).isNotNull();
		assertThat(permissionInfo.getPermissions()).isNotNull();
		assertThat(permissionInfo.getPermission(PERMISSION_NAME_READ)).isNotNull();
	}

	@Test
	public void shouldRetrieveTypePermissionInfosContainingComposedTypeAndAttributeDescriptor()
	{
		// given
		when(userService.isUserExisting(PRINCIPAL_ID)).thenReturn(Boolean.TRUE);
		when(userService.getUserForUID(PRINCIPAL_ID)).thenReturn(userModel);
		when(modelService.getSource(userModel)).thenReturn(jaloPrincipal);
		when(modelService.getAllSources(anyCollection(), anyCollection())).thenReturn(mock(List.class));
		when(jaloPrincipal.getItemPermissionsMap(anyList())).thenReturn(mock(Map.class));
		when(permissionCheckResult.isGranted()).thenReturn(Boolean.TRUE);
		when(permissionManagementService.getTypePermissionsForPrincipal(any(), any())).thenReturn(permissionAssignments);
		when(permissionAssignment.isGranted()).thenReturn(Boolean.TRUE);
		when(composedTypeModel.getCode()).thenReturn(COMPOSED_TYPE_MODEL_CODE);
		when(composedTypeModelForAttributeDescriptorModel.getCode()).thenReturn(ATTRIBUTE_DESCRIPTOR_MODEL_CODE);
		when(attributeDescriptorModel.getEnclosingType()).thenReturn(composedTypeModelForAttributeDescriptorModel);

		final Set<ItemModel> itemModels = new HashSet<>();
		itemModels.add(composedTypeModel);
		itemModels.add(attributeDescriptorModel);
		when(modelService.getAll(anyCollection(), anyCollection())).thenReturn(itemModels);

		// when
		final Collection<PermissionInfo> resultPermissionInfos = testSubject.getTypePermissionInfosForPrincipal(PRINCIPAL_ID);

		// then
		assertThat(resultPermissionInfos.stream()
				.filter(permissionInfo -> permissionInfo.getTypeCode().equals(COMPOSED_TYPE_MODEL_CODE)).collect(Collectors.toList()))
						.isNotNull().hasSize(1);
		assertThat(resultPermissionInfos.stream()
				.filter(permissionInfo -> permissionInfo.getTypeCode().equals(ATTRIBUTE_DESCRIPTOR_MODEL_CODE))
				.collect(Collectors.toList())).isNotNull().hasSize(1);
	}

	@Test
	public void shouldSetAttributePermission()
	{
		// given
		final boolean referenceAccess = true;
		permission = new Permission(false, !referenceAccess, PERMISSION_NAME_READ, PRINCIPAL_ID, TYPE_CODE, FIELD_CODE);

		when(typeService.getAttributeDescriptor(composedTypeModel, FIELD_CODE)).thenReturn(attributeDescriptorModel);
		when(typeService.getAttributeDescriptorsForType(composedTypeModel))
				.thenReturn(Collections.singleton(attributeDescriptorModel));

		testSubject.setPermission(permission);
		final PermissionInfo permissionInfo = testSubject.updatePermissionInfo(permission);
		final Permission tmpPermission = permissionInfo.getPermission(PERMISSION_NAME_READ);
		tmpPermission.setDenied(referenceAccess);
		testSubject.setPermission(tmpPermission);

		// when
		final PermissionInfo updatedPermissionInfo = testSubject.updatePermissionInfo(tmpPermission);
		final Permission retrievedPermissionInfo = updatedPermissionInfo.getPermission(PERMISSION_NAME_READ);

		// then
		assertThat(tmpPermission.isDenied()).isEqualTo(retrievedPermissionInfo.isDenied());
		assertThat(retrievedPermissionInfo.isDenied()).isTrue();
	}

	@Test
	public void shouldSetTypePermission()
	{
		// given
		final boolean referenceAccess = false;
		final String blankField = StringUtils.EMPTY;
		permission = new Permission(false, !referenceAccess, PERMISSION_NAME_READ, PRINCIPAL_ID, TYPE_CODE, blankField);

		final AtomicTypeModel atomicTypeModel = mock(AtomicTypeModel.class);
		when(typeService.getAtomicTypeForCode(TYPE_CODE)).thenReturn(atomicTypeModel);

		testSubject.setPermission(permission);

		final PermissionInfo referencePermissionInfo = testSubject.updatePermissionInfo(permission);
		final Permission referencePermission = referencePermissionInfo.getPermission(PERMISSION_NAME_READ);
		referencePermission.setDenied(referenceAccess);

		testSubject.setPermission(referencePermission);

		// when
		final PermissionInfo retrievedPermissionInfo = testSubject.updatePermissionInfo(referencePermission);
		final Permission retrievedPermission = retrievedPermissionInfo.getPermission(PERMISSION_NAME_READ);

		// then
		assertThat(referencePermission.isDenied()).isEqualTo(retrievedPermission.isDenied());
		assertThat(retrievedPermission.isDenied()).isFalse();
	}

	@Test
	public void shouldDeleteItemPermission()
	{
		// given
		permission = new Permission(true, false, PERMISSION_NAME_READ, PRINCIPAL_ID, TYPE_CODE, null);

		when(modelService.getSource(testUser)).thenReturn(jaloPrincipal);
		when(userService.isUserExisting(PRINCIPAL_ID)).thenReturn(Boolean.TRUE);

		final ArrayList<UserRightModel> list = new ArrayList<>();
		list.add(mock(UserRightModel.class));
		when(modelService.getAllSources(anyCollection(), anyCollection())).thenReturn(list);

		when(jaloPrincipal.getPK()).thenReturn(de.hybris.platform.core.PK.fromLong(USER_RIGHT_PK));
		when(jaloPrincipal.getTenant()).thenReturn(tenant);
		when(jaloPrincipal.getItemPermissionsMap(list)).thenReturn(userRightMap);

		testSubject.deletePermission(permission);

		final Set<AttributeDescriptorModel> set = new HashSet<>();
		set.add(attributeDescriptorModel);
		when(typeService.getAttributeDescriptorsForType(composedTypeModel)).thenReturn(set);

		final AttributeDescriptor attributeDescriptorJalo = mock(AttributeDescriptor.class);
		when(modelService.getSource(attributeDescriptorModel)).thenReturn(attributeDescriptorJalo);
		when(attributeDescriptorJalo.getPermissionMap(list)).thenReturn(userRightMap);
		when(permissionManagementService.getAttributePermissions(attributeDescriptorModel)).thenReturn(permissionAssignments);

		permission = new Permission(true, false, PERMISSION_NAME_READ, PRINCIPAL_ID, TYPE_CODE, FIELD_CODE);

		// when
		testSubject.deletePermission(permission);

		// then
		try
		{
			verify(jaloPrincipal).setItemPermissionsByMap(list, userRightMap);
			verify(attributeDescriptorJalo).setPermissionsByMap(list, userRightMap);
		}
		catch (final JaloSecurityException e)
		{
			LOG.error(e.getMessage(), e);
		}
	}

	@Test
	public void shouldUpdatePermissionInfo()
	{
		// given
		permission = new Permission(true, true, PERMISSION_NAME_READ, PRINCIPAL_ID, TYPE_CODE, null);

		// when
		final PermissionInfo permissionInfo = testSubject.updatePermissionInfo(permission);
		final Permission updatedPermission = permissionInfo.getPermission(PERMISSION_NAME_READ);

		// then
		assertThat(updatedPermission).isNotNull();
		assertThat(updatedPermission.getName()).isEqualTo(permission.getName());
	}

	@Test
	public void shouldRetrievePermissionInfoForPrincipal()
	{
		// when
		final PermissionInfo permissionInfo = testSubject.getPrincipalPermissionInfo(PRINCIPAL_ID, TYPE_CODE);

		// then
		assertThat(permissionInfo.getPermissionInfoType()).isEqualTo(PermissionInfo.PermissionInfoType.PRINCIPAL);
		assertThat(permissionInfo.getTypeCode()).isEqualTo(TYPE_CODE);
		assertThat(permissionInfo.getPrincipal()).isEqualTo(PRINCIPAL_ID);
		assertThat(permissionInfo.getPermissions()).hasSize(EXPECTED_PERMISSIONS_NUMBER);
	}

	@Test
	public void shouldRetrievePermissionInfoForType()
	{
		// when
		final PermissionInfo permissionInfo = testSubject.getTypePermissionInfo(PRINCIPAL_ID, TYPE_CODE);

		// then
		assertThat(permissionInfo.getPermissionInfoType()).isEqualTo(PermissionInfo.PermissionInfoType.TYPE);
		assertThat(permissionInfo.getTypeCode()).isEqualTo(TYPE_CODE);
		assertThat(permissionInfo.getPrincipal()).isEqualTo(PRINCIPAL_ID);
		assertThat(permissionInfo.getPermissions()).hasSize(EXPECTED_PERMISSIONS_NUMBER);
	}
}
