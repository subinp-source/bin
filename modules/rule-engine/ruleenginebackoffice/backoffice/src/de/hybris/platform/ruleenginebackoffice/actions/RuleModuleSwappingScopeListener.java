/*
 * [y] hybris Platform
 *
 * Copyright (c) 2018 SAP SE or an SAP affiliate company.  All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package de.hybris.platform.ruleenginebackoffice.actions;

import org.zkoss.zk.ui.IdSpace;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.ext.Scope;
import org.zkoss.zk.ui.ext.ScopeListener;


/**
 * The session scope listener, reacting on session attribute SWAPPING changes
 */
public class RuleModuleSwappingScopeListener implements ScopeListener
{
	public static final String SWAPPING_ATTR = "SWAPPING";
	public static final String MODULE_SWAPPING_SCOPE_LISTENER_ATTR = "MODULE_SWAPPING_SCOPE_LISTENER";

	private String swappingId;
	private Session session;

	public RuleModuleSwappingScopeListener(final Session session, final String swappingId)
	{
		this.swappingId = swappingId;
		this.session = session;
	}

	public void setSwappingId(final String swappingId)
	{
		this.swappingId = swappingId;
	}

	@Override
	public void attributeAdded(final Scope scope, final String s, final Object o)
	{
		// empty
	}

	@Override
	public void attributeReplaced(final Scope scope, final String s, final Object o)
	{
		if (s.equals(SWAPPING_ATTR) && swappingId.equals(o))
		{
			final Object newValue = session.getAttribute(SWAPPING_ATTR);
			if (newValue instanceof ModuleSwappingData)
			{
				final ModuleSwappingData swappingData = (ModuleSwappingData) newValue;
				if (swappingId.equals(swappingData.getSwappingId()))
				{
					session.removeAttribute(SWAPPING_ATTR);
				}
			}
		}
	}

	@Override
	public void attributeRemoved(final Scope scope, final String s)
	{
		// empty
	}

	@Override
	public void parentChanged(final Scope scope, final Scope scope1)
	{
		// empty
	}

	@Override
	public void idSpaceChanged(final Scope scope, final IdSpace idSpace)
	{
		// empty
	}

	public static class ModuleSwappingData
	{
		private String swappingId;
		private String moduleName;
		private String moduleOldVersion;
		private String moduleNewVersion;
		private boolean failed;
		private String failureReason;

		public ModuleSwappingData(final String swappingId, final String moduleName, final String moduleOldVersion,
				final String moduleNewVersion)
		{
			this.swappingId = swappingId;
			this.moduleName = moduleName;
			this.moduleOldVersion = moduleOldVersion;
			this.moduleNewVersion = moduleNewVersion;
		}

		public ModuleSwappingData(final String swappingId, final String failureReason)
		{
			this.swappingId = swappingId;
			this.failed = true;
			this.failureReason = failureReason;
		}

		public String getModuleName()
		{
			return moduleName;
		}

		public String getModuleOldVersion()
		{
			return moduleOldVersion;
		}

		public String getModuleNewVersion()
		{
			return moduleNewVersion;
		}

		public boolean isFailed()
		{
			return failed;
		}

		public String getFailureReason()
		{
			return failureReason;
		}

		public String getSwappingId()
		{
			return swappingId;
		}
	}
}
