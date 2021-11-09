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
package de.hybris.platform.rulebuilderbackoffice.components;

import org.zkoss.zk.ui.annotation.ComponentAnnotation;
import org.zkoss.zk.ui.event.CheckEvent;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Button;


@ComponentAnnotation("checked:@ZKBIND(ACCESS=both, SAVE_EVENT=onCheck)")
public class ToggleButton extends Button
{
	private boolean checked = false;
	private String checkedIconSclass;
	private String uncheckedIconSclass;

	public ToggleButton()
	{
		updateIconSclass(checked);

		addCheckedEventListener();
	}

	private void addCheckedEventListener()
	{
		addEventListener(Events.ON_CLICK, event -> setChecked(!isChecked()));
	}

	@Override
	public boolean equals(final Object obj)
	{
		return this == obj;
	}

	@Override
	public int hashCode()
	{
		return super.hashCode();
	}

	public final void updateIconSclass(final boolean checked)
	{
		if (checked)
		{
			setIconSclass(getCheckedIconSclass());
		}
		else
		{
			setIconSclass(getUncheckedIconSclass());
		}
	}

	public final boolean isChecked()
	{
		return checked;
	}

	public final void setChecked(final boolean checked)
	{
		updateIconSclass(checked);

		if (this.checked != checked)
		{
			this.checked = checked;
			smartUpdate("checked", checked);

			// create an onCheck event then post it back
			final CheckEvent checkEvent = new CheckEvent(Events.ON_CHECK, this, checked);
			Events.postEvent(checkEvent);
		}
	}

	public final String getCheckedIconSclass()
	{
		return checkedIconSclass;
	}

	public final void setCheckedIconSclass(final String checkedIconSclass)
	{
		this.checkedIconSclass = checkedIconSclass;
	}

	public final String getUncheckedIconSclass()
	{
		return uncheckedIconSclass;
	}

	public final void setUncheckedIconSclass(final String uncheckedIconSclass)
	{
		this.uncheckedIconSclass = uncheckedIconSclass;
	}
}
