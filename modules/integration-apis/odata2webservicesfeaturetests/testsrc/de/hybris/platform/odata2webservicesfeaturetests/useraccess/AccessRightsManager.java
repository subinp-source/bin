/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.odata2webservicesfeaturetests.useraccess;

import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.integrationservices.util.IntegrationTestUtil;
import de.hybris.platform.integrationservices.util.Log;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.servicelayer.security.permissions.PermissionAssignment;
import de.hybris.platform.servicelayer.security.permissions.PermissionManagementService;
import de.hybris.platform.servicelayer.security.permissions.PermissionsConstants;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.validation.constraints.NotNull;

/**
 * A utility for managing user access rights in the system. The implementation is not thread safe.
 */
public class AccessRightsManager
{
	private static final Log LOG = Log.getLogger(AccessRightsManager.class);
	private static final int CHANGE_TYPES_INITIAL_SIZE = 20;

	private final PermissionManagementService permissionManager;
	private final List<ComposedTypeModel> changedTypes;
	private boolean initialized;
	private UserModel user;
	private ComposedTypeModel type;
	private Boolean create;
	private Boolean read;
	private Boolean update;
	private Boolean delete;

	/**
	 * Instantiates this manager.
	 *
	 * @param mgr a service to use for managing user access permissions.
	 */
	public AccessRightsManager(@NotNull final PermissionManagementService mgr)
	{
		permissionManager = mgr;
		changedTypes = new ArrayList<>(CHANGE_TYPES_INITIAL_SIZE);
	}

	public AccessRightsManager forUser(final String uid)
	{
		final var user = IntegrationTestUtil.findAny(UserModel.class, u -> u.getUid().equals(uid))
		                                    .orElse(null);
		return forUser(user);
	}

	public AccessRightsManager forUser(final UserModel model)
	{
		user = model;
		return this;
	}

	public AccessRightsManager forType(final String type)
	{
		final var model = IntegrationTestUtil.findAny(ComposedTypeModel.class, m -> m.getCode().equals(type))
		                                     .orElse(null);
		return forType(model);
	}

	public AccessRightsManager forType(final ComposedTypeModel model)
	{
		type = model;
		return this;
	}

	public AccessRightsManager grantAll()
	{
		return grantRead()
				.grantCreate()
				.grantUpdate()
				.grantDelete();
	}

	public AccessRightsManager revokeAll()
	{
		return revokeRead()
				.revokeCreate()
				.revokeUpdate()
				.revokeDelete();
	}

	public AccessRightsManager grantRead()
	{
		read = true;
		return this;
	}

	public AccessRightsManager revokeRead()
	{
		read = false;
		return this;
	}

	public AccessRightsManager grantCreate()
	{
		create = true;
		return this;
	}

	public AccessRightsManager revokeCreate()
	{
		create = false;
		return this;
	}

	public AccessRightsManager grantUpdate()
	{
		update = true;
		return this;
	}

	public AccessRightsManager revokeUpdate()
	{
		update = false;
		return this;
	}

	public AccessRightsManager grantDelete()
	{
		delete = true;
		return this;
	}

	public AccessRightsManager revokeDelete()
	{
		delete = false;
		return this;
	}

	/**
	 * Applies permissions explicitly set by calling {@code grant...} or {@code revoke...} methods to
	 * the user and type, which were specified by calling {@link #forUser(UserModel)} and {@link #forType(ComposedTypeModel)}
	 * respectively.
	 * <p>Permissions, for which no {@code grant...} or {@code revoke...} methods were not called will be unspecified.</p>
	 */
	public AccessRightsManager apply()
	{
		if (user != null && type != null)
		{
			initialize();
			final Collection<PermissionAssignment> permissions = permissions();
			changedTypes.add(type);
			permissionManager.setTypePermissions(type, permissions);
			logUserPermissions();
		}
		return this;
	}

	private Collection<PermissionAssignment> permissions()
	{
		final PermissionAssignment[] permissions = {
				toPermission(PermissionsConstants.CREATE, create),
				toPermission(PermissionsConstants.READ, read),
				toPermission(PermissionsConstants.CHANGE, update),
				toPermission(PermissionsConstants.REMOVE, delete)
		};
		return Arrays.stream(permissions)
		             .filter(Objects::nonNull)
		             .collect(Collectors.toSet());
	}

	private PermissionAssignment toPermission(final String permission, final Boolean value)
	{
		return value != null
				? new PermissionAssignment(permission, user, !value)
				: null;
	}

	private void logUserPermissions()
	{
		final String c = permissionAsString(PermissionsConstants.CREATE);
		final String r = permissionAsString(PermissionsConstants.READ);
		final String u = permissionAsString(PermissionsConstants.CHANGE);
		final String d = permissionAsString(PermissionsConstants.REMOVE);
		LOG.info("{} permissions for {} (CRUD): {}{}{}{}", user.getUid(), type.getCode(), c, r, u, d);
	}

	private String permissionAsString(final String name)
	{
		final PermissionAssignment pa = contextPermission(name);
		if (pa == null)
		{
			return "?";
		}
		return pa.isGranted() ? "+" : "-";
	}

	private PermissionAssignment contextPermission(final String name)
	{
		return permissionManager.getTypePermissions(type).stream()
		                        .filter(p -> p.getPrincipal().getUid().equals(user.getUid()))
		                        .filter(p -> p.getPermissionName().equals(name))
		                        .findAny()
		                        .orElse(null);
	}

	private void initialize()
	{
		if (!initialized)
		{
			try
			{
				permissionManager.createPermission(PermissionsConstants.CHANGE);
				permissionManager.createPermission(PermissionsConstants.CREATE);
				permissionManager.createPermission(PermissionsConstants.READ);
				permissionManager.createPermission(PermissionsConstants.REMOVE);
				initialized = true;
				LOG.info("Created CRUD permissions");
			}
			catch (final ModelSavingException e)
			{
				LOG.debug("Permissions already initialized", e);
			}
		}
	}

	/**
	 * Resets access rights to the state they were in the system prior to the first call of the {@link #apply()} method
	 * on this manager.
	 */
	public AccessRightsManager reset()
	{
		changedTypes.forEach(this::resetType);
		changedTypes.clear();
		return this;
	}

	private void resetType(final ComposedTypeModel type)
	{
		permissionManager.clearTypePermissions(type);
		LOG.info("Cleared {} permissions", type.getCode());
	}
}
